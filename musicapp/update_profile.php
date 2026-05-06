<?php
require_once 'db_config.php';

$user_id = $_POST['user_id'];
$new_username = $_POST['username'];

if (empty($user_id) || empty($new_username)) {
    echo json_encode(["success" => false, "message" => "Invalid data"]);
    exit;
}

$sql = "UPDATE users SET username = '$new_username' WHERE id = '$user_id'";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["success" => true, "message" => "Profile updated successfully"]);
} else {
    echo json_encode(["success" => false, "message" => "Error updating profile: " . $conn->error]);
}

$conn->close();
?>
