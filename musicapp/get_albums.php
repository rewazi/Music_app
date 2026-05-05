<?php
header('Content-Type: application/json');
require_once 'db_config.php';

function getAlbumImageFromLastFM($artist, $album) {
    $apiKey = getenv('LASTFM_API_KEY');
    if (!$apiKey) {
        return null;
    }
    $url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" . $apiKey . "&artist=" . urlencode($artist) . "&album=" . urlencode($album) . "&format=json";
    
    $response = @file_get_contents($url);
    if ($response === false) {
        return null;
    }
    
    $data = json_decode($response, true);
    if (isset($data['album']['image'])) {
        // Last.fm provides images in various sizes. We'll try to get the largest one (mega, extralarge, etc.)
        $sizes = ['mega', 'extralarge', 'large', 'medium', 'small'];
        $imageMap = [];
        foreach ($data['album']['image'] as $image) {
            if (!empty($image['#text'])) {
                $imageMap[$image['size']] = $image['#text'];
            }
        }

        foreach ($sizes as $size) {
            if (!empty($imageMap[$size])) {
                return $imageMap[$size];
            }
        }
    }
    return null;
}

$sql = "SELECT a.*, s.name as singer_name
        FROM albums a
        JOIN singers s ON a.singer_id = s.id";

$result = $conn->query($sql);
$data = [];

if ($result) {
    while($row = $result->fetch_assoc()) {
        $updateNeeded = false;

        // Check if image_url or banner_url is empty or a placeholder
        $isImageEmpty = empty($row['image_url']) || strpos($row['image_url'], 'placeholder.com') !== false;
        $isBannerEmpty = empty($row['banner_url']) || strpos($row['banner_url'], 'placeholder.com') !== false;

        if ($isImageEmpty || $isBannerEmpty) {
            $fetched_url = getAlbumImageFromLastFM($row['singer_name'], $row['title']);
            if ($fetched_url) {
                // Set both to the same image as requested
                $row['image_url'] = $fetched_url;
                $row['banner_url'] = $fetched_url;
                $updateNeeded = true;
            }
        }

        if ($updateNeeded) {
            $update_sql = "UPDATE albums SET image_url = ?, banner_url = ? WHERE id = ?";
            $stmt = $conn->prepare($update_sql);
            $stmt->bind_param("ssi", $row['image_url'], $row['banner_url'], $row['id']);
            $stmt->execute();
            $stmt->close();
        }
        $data[] = $row;
    }
}

echo json_encode($data);
