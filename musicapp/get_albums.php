<?php
header('Content-Type: application/json');
require_once 'db_config.php';

function getAlbumImageFromLastFM($artist, $album) {
    $apiKey = getenv('LASTFM_API_KEY');
    if (!$apiKey) {
        return null;
    }
    $url = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" . $apiKey . "&artist=" . urlencode($artist) . "&album=" . urlencode($album) . "&format=json";
    
    $response = file_get_contents($url);
    if ($response === false) {
        return null;
    }
    
    $data = json_decode($response, true);
    if (isset($data['album']['image'])) {
        foreach ($data['album']['image'] as $image) {
            if ($image['size'] == 'large') {
                return $image['#text'];
            }
        }
        // If large not found, take the first available
        if (!empty($data['album']['image'])) {
            return $data['album']['image'][0]['#text'];
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
        if (empty($row['image_url'])) {
            $image_url = getAlbumImageFromLastFM($row['singer_name'], $row['title']);
            if ($image_url) {
                // Update database
                $update_sql = "UPDATE albums SET image_url = ? WHERE id = ?";
                $stmt = $conn->prepare($update_sql);
                $stmt->bind_param("si", $image_url, $row['id']);
                $stmt->execute();
                $stmt->close();
                $row['image_url'] = $image_url;
            }
        }
        $data[] = $row;
    }
}

echo json_encode($data);