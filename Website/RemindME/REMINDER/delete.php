<?php
include("connection.php");

$id = $_GET['id'];

$sql = "DELETE FROM `reminders` WHERE reminder_id=$id";

if ($conn->query($sql) === TRUE) {
    header("Location: ../dashboard.php");
} else {
    echo "Error deleting reminder: " . $conn->error;
}

$conn->close();
