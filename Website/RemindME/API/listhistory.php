<?php
include('connection.php');

$email = $_POST['email'];

$query = "SELECT * FROM `history` WHERE userEmail='$email'";
$result = mysqli_query($conn, $query);
$data = array();
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }
    echo json_encode($data);
}
mysqli_close($conn);

// Tutorial from: https://write.corbpie.com/php-check-if-mysql-row-found-for-query/
