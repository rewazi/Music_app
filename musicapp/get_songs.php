<?php
header('Content-Type: application/json');
require_once 'db_config.php';

function getTrackPreviewFromDeezer($artist, $track) {
    $url = "https://api.deezer.com/search?q=" . urlencode("artist:\"$artist\" track:\"$track\"") . "&limit=1";
    $response = @file_get_contents($url);
    if ($response === false) return null;
    $data = json_decode($response, true);
    if (isset($data['data'][0]['preview'])) {
        return $data['data'][0]['preview'];
    }
    return null;
}

function getTrackImageFromLastFM($artist, $track) {
    $apiKey = getenv('LASTFM_API_KEY');
    if (!$apiKey) return null;
    $url = "https://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" . $apiKey . "&artist=" . urlencode($artist) . "&track=" . urlencode($track) . "&format=json";
    $response = @file_get_contents($url);
    if ($response === false) return null;
    $data = json_decode($response, true);
    if (isset($data['track']['album']['image'])) {
        $images = $data['track']['album']['image'];
        $sizes = ['mega', 'extralarge', 'large', 'medium', 'small'];
        $imageMap = [];
        foreach ($images as $image) {
            if (!empty($image['#text'])) $imageMap[$image['size']] = $image['#text'];
        }
        foreach ($sizes as $size) {
            if (!empty($imageMap[$size])) return $imageMap[$size];
        }
    }
    return null;
}

$album_id = isset($_GET['album_id']) ? intval($_GET['album_id']) : 0;

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

while($row = $result->fetch_assoc()) {
    $updateNeeded = false;

    // Fetch REAL preview from Deezer if it's currently a placeholder or empty
    $isTestOrEmpty = empty($row['song_url']) || strpos($row['song_url'], '10.0.2.2') !== false || strpos($row['song_url'], 'soundhelix.com') !== false;

    if ($isTestOrEmpty) {
        $preview = getTrackPreviewFromDeezer($row['singer_name'], $row['title']);
        if ($preview) {
            $row['song_url'] = $preview;
            $updateNeeded = true;
        }
    }

    // Image logic (remain the same)
    $isImageEmpty = empty($row['image_url']) || strpos($row['image_url'], 'placeholder.com') !== false;
    if ($isImageEmpty) {
        if (!empty($row['album_image']) && strpos($row['album_image'], 'placeholder.com') === false) {
            $row['image_url'] = $row['album_image'];
            $row['banner_url'] = $row['album_image'];
            $updateNeeded = true;
        } else {
            $fetched_url = getTrackImageFromLastFM($row['singer_name'], $row['title']);
            if ($fetched_url) {
                $row['image_url'] = $fetched_url;
                $row['banner_url'] = $fetched_url;
                $updateNeeded = true;
            }
        }
    }

    if ($updateNeeded) {
        $update_sql = "UPDATE songs SET image_url = ?, banner_url = ?, song_url = ? WHERE id = ?";
        $upd_stmt = $conn->prepare($update_sql);
        $upd_stmt->bind_param("sssi", $row['image_url'], $row['banner_url'], $row['song_url'], $row['id']);
        $upd_stmt->execute();
        $upd_stmt->close();
    }

    unset($row['album_image']);
    $data[] = $row;
}

echo json_encode($data);
?>