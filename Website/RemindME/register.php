<?php
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);

require('db_connection.php');

if (isset($_POST['register'])) {

    // Password Confirmation
    if ($_POST['password'] != $_POST['password1']) {
        echo "passwords do not match";
    } else {

        if ($_POST['password'] != '' || $_POST['email'] != '' || $_POST['name'] != '') {

            // Email Check
            $email = "SELECT * FROM hcw WHERE `email` = '" . $_POST["email"] . "' LIMIT 1";
            $result = $conn->query($email);

            if ($result->num_rows > 0) {
                echo "email already exists";
            } else {
                $sql = "INSERT INTO hcw (name, email, password) VALUES ('$_POST[name]', '$_POST[email]', '$_POST[password]')";

                if ($conn->query($sql) === TRUE) {
                    header('Location: login.php');
                } else {
                    echo "Error: " . $sql . "<br>" . $conn->error;
                }

                $conn->close();
            }
        } else {
            echo "please fill all the fields";
        }
    }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RemindME - REGISTER</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- CSS -->
    <link rel="stylesheet" href="CSS/style.css">
</head>

<body>
    <div class="container pt-5 div-center">
        <form method="POST">
            <div class="text-center">
                <img class="mb-4" src="CSS/IMG/logo.png" alt="" width="80" height="50">
            </div>

            <div class="mb-3">
                <label for="InputName" class="form-label">Full Name</label>
                <input name="name" type="text" class="form-control" id="InputName" placeholder="John Doe">
            </div>
            <div class="mb-3">
                <label for="InputEmail" class="form-label">Email</label>
                <input name="email" type="email" class="form-control" id="InputEmail" placeholder="name@example.com">
            </div>
            <div class="mb-3">
                <label for="InputPassword" class="form-label">Password</label>
                <input name="password" type="password" class="form-control" placeholder="********" id="InputPassword">
            </div>
            <div class="mb-3">
                <label for="InputCPassword" class="form-label">Confirm Password</label>
                <input name="password1" type="password" class="form-control" placeholder="********" id="InputCPassword">
            </div>
            <div class="text-center d-grid gap-2">
                <button type="submit" name="register" class="btn btn-primary btn-block">SIGN UP</button>
                <br>
                <p>Already have an account? <a href="login.php">Login</a></p>
            </div>
        </form>
    </div>
    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>