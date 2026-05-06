<?php
header('Content-Type: application/json');
require_once 'db_config.php';

// Логирование ошибок
$logFile = __DIR__ . '/error_log.txt';
function debugLog($message) {
    global $logFile;
    file_put_contents($logFile, date('[Y-m-d H:i:s] ') . $message . PHP_EOL, FILE_APPEND);
}

function fetchUrl($url) {
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0');
    $response = curl_exec($ch);
    curl_close($ch);
    return $response;
}

// Поиск обложки (Deezer + Last.fm fallback)
function getArt($artist, $track, $album = "") {
    // 1. Deezer Search
    $query = $artist . " " . ($album ?: $track);
    $url = "https://api.deezer.com/search?q=" . urlencode($query) . "&limit=1";
    $res = fetchUrl($url);
    if ($res) {
        $data = json_decode($res, true);
        if (!empty($data['data'][0]['album']['cover_xl'])) return $data['data'][0]['album']['cover_xl'];
        if (!empty($data['data'][0]['album']['cover_big'])) return $data['data'][0]['album']['cover_big'];
    }

    // 2. Last.fm Fallback
    $apiKey = getenv('LASTFM_API_KEY');
    if ($apiKey) {
        $url = "https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=$apiKey&artist=" . urlencode($artist) . "&track=" . urlencode($track) . "&format=json";
        $res = fetchUrl($url);
        if ($res) {
            $data = json_decode($res, true);
            if (isset($data['track']['album']['image'])) {
                $images = $data['track']['album']['image'];
                return $images[count($images)-1]['#text'];
            }
        }
    }
    return null;
}

function getTrackPreviewFromDeezer($artist, $track) {
    $url = "https://api.deezer.com/search?q=" . urlencode($artist . " " . $track) . "&limit=1";
    $res = fetchUrl($url);
    if ($res) {
        $data = json_decode($res, true);
        return $data['data'][0]['preview'] ?? null;
    }
    return null;
}

$album_id = isset($_GET['album_id']) ? intval($_GET['album_id']) : 0;

$sql = "SELECT s.*, si.name as singer_name, a.image_url as album_image, a.banner_url as album_banner, a.title as album_title
        FROM songs s
        JOIN singers si ON s.singer_id = si.id
        LEFT JOIN albums a ON s.album_id = a.id";
if ($album_id > 0) {
    $sql .= " WHERE s.album_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $album_id);
} else {
    $stmt = $conn->prepare($sql);
}

$stmt->execute();
$result = $stmt->get_result();
$data = [];

while($row = $result->fetch_assoc()) {
    $updateNeeded = false;
    $songId = $row['id'];

    // 1. Чиним аудио (song_url)
    // Ссылки Deezer (dzcdn.net или cdns-preview) временные и часто меняются.
    // Если ссылка пустая, ведет на локальный IP или является старой ссылкой Deezer — обновляем её.
    $isDeezerLink = strpos($row['song_url'], 'dzcdn.net') !== false || strpos($row['song_url'], 'cdns-preview') !== false;

    if (empty($row['song_url']) || strpos($row['song_url'], '10.0.2.2') !== false || $isDeezerLink) {
        $preview = getTrackPreviewFromDeezer($row['singer_name'], $row['title']);
        if ($preview && $preview !== $row['song_url']) {
            $row['song_url'] = $preview;
            $updateNeeded = true;
        }
    }

    // 2. Чиним картинки
    if (empty($row['image_url']) || strpos($row['image_url'], 'placeholder') !== false) {
        // Пробуем взять у альбома
        if (!empty($row['album_image']) && strpos($row['album_image'], 'placeholder') === false) {
            $row['image_url'] = $row['album_image'];
        } else {
            // Ищем в интернете
            $foundArt = getArt($row['singer_name'], $row['title'], $row['album_title'] ?? "");
            if ($foundArt) {
                $row['image_url'] = $foundArt;
                $updateNeeded = true;
                // Если это песня из альбома, обновим и альбом тоже
                if (!empty($row['album_id'])) {
                    $conn->query("UPDATE albums SET image_url = '$foundArt', banner_url = '$foundArt' WHERE id = " . $row['album_id']);
                }
            }
        }
    }

    // 3. Чиним баннер
    if (empty($row['banner_url'])) {
        $row['banner_url'] = !empty($row['album_banner']) ? $row['album_banner'] : $row['image_url'];
        $updateNeeded = true;
    }

    if ($updateNeeded) {
        $upd = $conn->prepare("UPDATE songs SET song_url = ?, image_url = ?, banner_url = ? WHERE id = ?");
        $upd->bind_param("sssi", $row['song_url'], $row['image_url'], $row['banner_url'], $songId);
        $upd->execute();
        $upd->close();
    }

    unset($row['album_image'], $row['album_banner'], $row['album_title']);
    $data[] = $row;
}

echo json_encode($data);
?>
