<?php
$servername = "localhost";
$username = "root";
$password = "";
$database = "remind";

$conn = new mysqli($servername, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_connect.asp