<?php
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);

require('connection.php');
session_start();

if (!isset($_SESSION['email'])) {
    header('Location: login.php');
}

if ($_POST['MedName'] != "" & $_POST['MedDate'] != "") {
    $sql = "INSERT INTO `reminders` (med_name, med_desc, date, time, isRepeating, user_email, hcw_email)
VALUES ('$_POST[MedName]', '$_POST[MedDesc]', '$_POST[MedDate]', '$_POST[MedTime]', '$_POST[isRepeat]', '$_POST[UserEmail]', '{$_SESSION['email']}')";

    if ($conn->query($sql) === TRUE) {
        header('Location: ../dashboard.php');
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}
$conn->close();
