<?php
$servername = "localhost";
$username = "root";
$password = "";
$database = "remind";


// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_connect.asp