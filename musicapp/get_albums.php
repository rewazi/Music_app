<?php
header('Content-Type: application/json');
require_once 'db_config.php';

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

function getAlbumArt($artist, $album) {
    // 1. Пытаемся найти через Deezer (самый стабильный вариант)
    $url = "https://api.deezer.com/search/album?q=" . urlencode($artist . " " . $album) . "&limit=1";
    $res = fetchUrl($url);
    if ($res) {
        $data = json_decode($res, true);
        if (!empty($data['data'][0]['cover_xl'])) return $data['data'][0]['cover_xl'];
        if (!empty($data['data'][0]['cover_big'])) return $data['data'][0]['cover_big'];
    }

    // 2. Если не вышло, пробуем Last.fm (если есть ключ)
    $apiKey = getenv('LASTFM_API_KEY');
    if ($apiKey) {
        $url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=$apiKey&artist=" . urlencode($artist) . "&album=" . urlencode($album) . "&format=json";
        $res = fetchUrl($url);
        if ($res) {
            $data = json_decode($res, true);
            if (isset($data['album']['image'])) {
                $images = $data['album']['image'];
                for ($i = count($images) - 1; $i >= 0; $i--) {
                    if (!empty($images[$i]['#text'])) return $images[$i]['#text'];
                }
            }
        }
    }
    return null;
}

$sql = "SELECT a.*, s.name as singer_name FROM albums a JOIN singers s ON a.singer_id = s.id";
$result = $conn->query($sql);
$data = [];

if ($result) {
    while($row = $result->fetch_assoc()) {
        $img = $row['image_url'];
        $banner = $row['banner_url'];

        $isEmpty = empty($img) || strpos($img, 'placeholder') !== false || strlen($img) < 10;

        if ($isEmpty) {
            $found = getAlbumArt($row['singer_name'], $row['title']);
            if ($found) {
                $img = $found;
                $banner = $found;
                // Сохраняем в БД навсегда
                $stmt = $conn->prepare("UPDATE albums SET image_url = ?, banner_url = ? WHERE id = ?");
                $stmt->bind_param("ssi", $img, $banner, $row['id']);
                $stmt->execute();
                $stmt->close();
            }
        }

        $row['image_url'] = $img;
        $row['banner_url'] = $banner;
        $data[] = $row;
    }
}
echo json_encode($data);
?>
