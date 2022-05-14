<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    include_once("connection.php");

    $email = $_POST['email'];
    $password = $_POST['password'];

    if ($email == '' || $password == '') {
        echo json_encode(array("status" => "false", "message" => "Please fill all the fields"));
    } else {
        $query = "SELECT * FROM `users` WHERE email='$email'";
        $result = mysqli_query($conn, $query);

        if (mysqli_num_rows($result) > 0) { // Email Exists
            echo json_encode(array("status" => "false", "message" => "Email provided already exists"));
        } else { // Register
            $sql = "INSERT INTO `users` (email,password) VALUES ('$email','$password')";
            if ($conn->query($sql) === TRUE) {
                echo json_encode(array("status" => "true", "message" => "Successfully registered!", "email" => $email));
            } else {
                echo json_encode(array("status" => "false", "message" => "Error occured, please try again!"));
            }
        }
        mysqli_close($conn);
    }
} else {
    echo json_encode(array("status" => "false", "message" => "Error occured, please try again!"));
}

// Tutorial from: https://www.w3schools.com/php/php_mysql_insert.asp