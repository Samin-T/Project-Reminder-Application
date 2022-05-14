<?php
require('db_connection.php');

session_start();

if (isset($_POST['submit'])) {
  $email = $_POST['email'];
  $password = $_POST['password'];

  if ($email != "" && $password != "") {
    $sql = "SELECT * from hcw where email='" . $email . "' and password='" . $password . "'";
    $result = mysqli_query($conn, $sql);
    $row = mysqli_fetch_array($result);

    if ($row > 0) {
      $_SESSION['email'] = $email;
      header('Location: dashboard.php');
    } else {
      echo "Email or Password is Invalid";
    }
  }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>RemindME - LOGIN</title>

  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- CSS -->
  <link rel="stylesheet" href="CSS/style.css">

</head>

<body>
  <div class="container pt-5 div-center">
    <form method="POST" action="login.php">
      <div class="text-center">
        <img class="mb-4" src="CSS/IMG/logo.png" alt="" width="80" height="50">
      </div>

      <div class="mb-3">
        <label for="InputEmail" class="form-label">Email</label>
        <input name="email" type="email" class="form-control" id="InputEmail" placeholder="name@example.com">
      </div>
      <div class="mb-3">
        <label for="InputPassword" class="form-label">Password</label>
        <input name="password" type="password" class="form-control" placeholder="********" id="InputPassword">
      </div>
      <div class="text-center d-grid gap-2">
        <input class="btn btn-primary btn-block" type="submit" name="submit" value="Login">
        <br>
        <p>Not registered? <a href="register.php">Create account</a></p>

      </div>
    </form>
  </div>
  <!-- Bootstrap Bundle with Popper -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>