<?php
header('Content-Type: application/json');
require_once 'db_config.php';

$sql = "SELECT a.*, s.name as singer_name
        FROM albums a
        JOIN singers s ON a.singer_id = s.id";

$result = $conn->query($sql);
$data = [];

if ($result) {
    while($row = $result->fetch_assoc()) {
        $data[] = $row;
    }
}

echo json_encode($data);