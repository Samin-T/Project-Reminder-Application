<?php
include("connection.php");
$id = $_POST['id'];

$sql = "DELETE FROM `reminders` WHERE reminder_id=$id";
if ($conn->query($sql) === TRUE) {
    echo json_encode(array("status" => "true", "message" => "reminder deleted"));
} else {
    echo json_encode(array("status" => "false", "message" => "Error"));
}

$conn->close();

// Tutorial from: https://www.w3schools.com/php/php_mysql_delete.asp