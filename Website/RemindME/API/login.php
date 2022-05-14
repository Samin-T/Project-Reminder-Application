<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    include_once("connection.php");

    $email = $_POST['email'];
    $password = $_POST['password'];

    if ($email == '' || $password == '') {
        echo json_encode(array("status" => "false", "message" => "Please fill all the fields"));
    } else {
        $sql = "SELECT * FROM `users` WHERE email='$email' AND password='$password'";
        $result = mysqli_query($conn, $sql);

        if (mysqli_num_rows($result) > 0) {
            echo json_encode(array("status" => "true", "message" => "", "email" => $email)); // The email which login was successful with
        } else {
            echo json_encode(array("status" => "false", "message" => "Invalid email or password"));
        }
        mysqli_close($conn);
    }
} else {
    echo json_encode(array("status" => "false", "message" => "An error occured, please try again"));
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_select.asp