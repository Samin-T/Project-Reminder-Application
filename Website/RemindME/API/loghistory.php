<?php
include_once("connection.php");

$medName = $_POST['medName'];
$medDate = $_POST['medDate'];
$medTime = $_POST['medTime'];
$status = $_POST['status'];
$userEmail = $_POST['userEmail'];

$sql = "INSERT INTO `history` (medName, medDate, medTime, `status`, userEmail) VALUES ('$medName', '$medDate', '$medTime', '$status', '$userEmail')";

if ($conn->query($sql) === TRUE) {
    echo json_encode(array("status" => "true", "message" => "history inserted"));
} else {
    echo json_encode(array("status" => "false", "message" => "Error"));
}

$conn->close();

// Tutorial from: https://www.w3schools.com/php/php_mysql_insert.asp