<?php
include_once("connection.php");

$medName = mysqli_real_escape_string($conn, $_POST['MedName']);
$medDesc = mysqli_real_escape_string($conn, $_POST['MedDesc']);
$reminderDate = mysqli_real_escape_string($conn, $_POST['ReminderDate']);
$reminderTime = mysqli_real_escape_string($conn, $_POST['ReminderTime']);
$isRepeating = mysqli_real_escape_string($conn, $_POST['isRepeating']);
$medUserEmail = mysqli_real_escape_string($conn, $_POST['UserEmail']);

if ($medName == '' || $reminderDate == '' || $reminderTime == '' || $isRepeating == '' || $medUserEmail == '') {
    echo json_encode(array("status" => "false", "message" => "param missing"));
} else {
    $sql = "INSERT INTO `reminders` (med_name, med_desc, date, time, isRepeating, user_email, hcw_email) VALUES ('$medName','$medDesc','$reminderDate','$reminderTime', '$isRepeating', '$medUserEmail', '')";
    if ($conn->query($sql) === TRUE) {
        echo json_encode(array("status" => "true", "message" => "reminder set"));
    } else {
        echo json_encode(array("status" => "false", "message" => "error setting reminder"));
    }
    mysqli_close($conn);
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_insert.asp