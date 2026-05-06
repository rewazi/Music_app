<?php
// Debugging
$logFile = __DIR__ . '/error_log.txt';
function debugLog($message) {
    global $logFile;
    file_put_contents($logFile, date('[Y-m-d H:i:s] ') . $message . PHP_EOL, FILE_APPEND);
}

header('Content-Type: application/json');
require_once 'db_config.php';

function fetchUrl($url) {
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    curl_setopt($ch, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36');
    $response = curl_exec($ch);
    if(curl_errno($ch)) debugLog('CURL Error: ' . curl_error($ch));
    curl_close($ch);
    return $response;
}

function getTrackPreviewFromDeezer($artist, $track) {
    $query = "artist:\"" . trim($artist) . "\" track:\"" . trim($track) . "\"";
    $url = "https://api.deezer.com/search?q=" . urlencode($query) . "&limit=1";

    debugLog("Searching Deezer for: $query");
    $response = fetchUrl($url);
    if (!$response) return null;

    $data = json_decode($response, true);
    if (isset($data['data'][0]['preview'])) {
        return $data['data'][0]['preview'];
    }

    // Fallback search
    $url = "https://api.deezer.com/search?q=" . urlencode(trim($artist) . " " . trim($track)) . "&limit=1";
    $response = fetchUrl($url);
    $data = json_decode($response, true);
    return isset($data['data'][0]['preview']) ? $data['data'][0]['preview'] : null;
}

$album_id = isset($_GET['album_id']) ? intval($_GET['album_id']) : 0;
debugLog("Request for album_id: $album_id");

if ($album_id > 0) {
    $sql = "SELECT s.*, si.name as singer_name, a.image_url as album_image
            FROM songs s
            JOIN singers si ON s.singer_id = si.id
            LEFT JOIN albums a ON s.album_id = a.id
            WHERE s.album_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $album_id);
} else {
    $sql = "SELECT s.*, si.name as singer_name, a.image_url as album_image
            FROM songs s
            JOIN singers si ON s.singer_id = si.id
            LEFT JOIN albums a ON s.album_id = a.id";
    $stmt = $conn->prepare($sql);
}

$stmt->execute();
$result = $stmt->get_result();
$data = [];

$count = $result->num_rows;
debugLog("Songs found in DB for album $album_id: " . $count);

if ($count === 0) {
    // Check if album exists at all
    $check = $conn->query("SELECT COUNT(*) as total FROM songs WHERE album_id = $album_id");
    $row = $check->fetch_assoc();
    debugLog("Total songs in DB with album_id $album_id (without joins): " . $row['total']);
}

while($row = $result->fetch_assoc()) {
    $updateNeeded = false;
    $currentUrl = $row['song_url'];
    $isInvalidUrl = empty($currentUrl) ||
                    trim($currentUrl) == "" ||
                    strpos($currentUrl, '10.0.2.2') !== false ||
                    strpos($currentUrl, 'soundhelix.com') !== false ||
                    strpos($currentUrl, 'dzcdn.net') !== false; // Force refresh to keep URLs fresh

    if ($isInvalidUrl) {
        debugLog("Updating song: " . $row['title'] . " (Current URL: '$currentUrl')");
        $preview = getTrackPreviewFromDeezer($row['singer_name'], $row['title']);
        if ($preview) {
            $row['song_url'] = $preview;
            $updateNeeded = true;
            debugLog("Found preview for {$row['title']}: $preview");
        } else {
            $row['song_url'] = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
            $updateNeeded = true;
            debugLog("Preview NOT found for {$row['title']}, using fallback");
        }
    }

    if ($updateNeeded) {
        $update_sql = "UPDATE songs SET song_url = ? WHERE id = ?";
        $upd_stmt = $conn->prepare($update_sql);
        $upd_stmt->bind_param("si", $row['song_url'], $row['id']);
        if(!$upd_stmt->execute()) debugLog("DB Update Failed: " . $conn->error);
        $upd_stmt->close();
    }

    unset($row['album_image']);
    $data[] = $row;
}

echo json_encode($data);
?>