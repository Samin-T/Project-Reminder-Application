<?php
include_once("connection.php");

$ID = $_POST['ID'];
$medName = mysqli_real_escape_string($conn, $_POST['MedName']);
$medDesc = mysqli_real_escape_string($conn, $_POST['MedDesc']);
$reminderDate = mysqli_real_escape_string($conn, $_POST['ReminderDate']);
$reminderTime = mysqli_real_escape_string($conn, $_POST['ReminderTime']);
$isRepeating = $_POST['isRepeating'];

if ($ID == '' || $medName == '' || $reminderDate == '' || $reminderTime == '' || $isRepeating == '') {
    echo json_encode(array("status" => "false", "message" => "param missing"));
} else {
    $sql  = "UPDATE `reminders` SET med_name='$medName', med_desc='$medDesc', date='$reminderDate', time='$reminderTime', isRepeating='$isRepeating'  WHERE reminder_id='$ID'";
    if ($conn->query($sql) === TRUE) {
        echo json_encode(array("status" => "true", "message" => "reminder set"));
    } else {
        echo json_encode(array("status" => "false", "message" => "error setting reminder"));
    }
    mysqli_close($conn);
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_update.asp