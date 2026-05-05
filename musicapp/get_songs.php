<?php
header('Content-Type: application/json');
require_once 'db_config.php';

$album_id = isset($_GET['album_id']) ? intval($_GET['album_id']) : 0;

if ($album_id > 0) {
    $sql = "SELECT s.*, si.name as singer_name
            FROM songs s
            JOIN singers si ON s.singer_id = si.id
            WHERE s.album_id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $album_id);
} else {
    $sql = "SELECT s.*, si.name as singer_name
            FROM songs s
            JOIN singers si ON s.singer_id = si.id";
    $stmt = $conn->prepare($sql);
}

$stmt->execute();
$result = $stmt->get_result();
$data = [];

while($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);
?>