<?php
session_start();

// Check user logged in or not
if (!isset($_SESSION['email'])) {
    header('Location: login.php');
}
?>

<?php
require('db_connection.php');

$sql = "SELECT * FROM hcw WHERE email = '{$_SESSION['email']}'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $userid = $row["id"];
        $email = $row["email"];
        $name = $row["name"];
        $password = $row["password"];
    }
}
?>

<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RemindME - DASHBOARD</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">

    <style>
        #myBtn {
            position: fixed;
            bottom: 30px;
            right: 30px;
            padding: 12px;
            border-radius: 6px;
        }

        .container {
            margin-top: 40px;
            margin-bottom: 40px;
        }
    </style>
</head>

<body>

    <!-- https://www.tutorialrepublic.com/twitter-bootstrap-tutorial/bootstrap-dropdowns.php -->
    <nav class="navbar navbar-expand-sm navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="dashboard.php">
                <img src="CSS/IMG/RemindME.png" width="80" height="30" alt="">
            </a>
            <button type="button" class="navbar-toggler" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div id="navbarCollapse" class="collapse navbar-collapse">
                <form method="POST" class="nav-item w-100">
                    <input name="search" class="form-control" type="text" placeholder="Search" aria-label="Search">
                </form>
                <ul class="nav navbar-nav ms-auto">
                    <li class="nav-item dropdown">
                        <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown"><?php echo htmlspecialchars($name); ?></a>
                        <div class="dropdown-menu dropdown-menu-end">
                            <a class="dropdown-item" href="#" type="button" data-bs-toggle="modal" data-bs-target="#profileModal">Profile Info</a>

                            <div class="dropdown-divider"></div>
                            <a style="color:red;" class="dropdown-item" href="logout.php">Logout</a>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Add Modal -->
    <div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <form method="POST" action="REMINDER/add.php">
                        <div class="mb-1">
                            <label for="medication-name" class="col-form-label">Medication Name:</label>
                            <input type="text" class="form-control" id="medication-name" name="MedName">
                        </div>
                        <div class="mb-1">
                            <label for="description-text" class="col-form-label">Description:</label>
                            <textarea class="form-control" id="description-text" name="MedDesc"></textarea>
                        </div>
                        <div class="mb-1">
                            <label for="medication-date" class="col-form-label">Date:</label>
                            <input id="datepicker" type="text" class="form-control" placeholder="dd/mm/yyyy" id="medication-date" name="MedDate">
                        </div>
                        <div class="mb-1">
                            <label for="medication-time" class="col-form-label">Time:</label>
                            <input type="time" class="form-control" placeholder="--:--" id="medication-time" name="MedTime">
                        </div>
                        <div class="mb-1">
                            <label for="user-email" class="col-form-label">User Email:</label>
                            <input type="text" class="form-control" placeholder="name@example.com" id="user-email" name="UserEmail">
                        </div>
                        <div class="form-check mb-1">
                            <input name="repeat" class="form-check-input" type="checkbox" id="flexCheckDefault" onchange="isRepeating(this)">
                            <label class="form-check-label" for="flexCheckDefault">
                                Repeat Daily
                            </label>
                            <input type="hidden" id="repeat" name="isRepeat">
                        </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" type="button" class="btn btn-primary">Add</button>
                </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Profile Modal -->
    <div class="modal fade" id="profileModal" tabindex="-1" aria-labelledby="profileModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="profileModalLabel">Profile Info</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p><b>User ID:</b> <?php echo htmlspecialchars($userid); ?></p>
                    <p><b>Name:</b> <?php echo htmlspecialchars($name); ?></p>
                    <p><b>Email:</b> <?php echo htmlspecialchars($email); ?></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <!-- Records -->
        <div>
            <ol class="list-group list-group">
                <?php // Tutorial: https://www.php.net/manual/en/mysqli-result.fetch-assoc.php
                $sql = "SELECT * FROM `reminders` WHERE `hcw_email` = '{$_SESSION['email']}';"; // List ALL

                if (isset($_POST['search'])) // Search
                    $sql = "SELECT * FROM `reminders` WHERE `hcw_email` = '{$_SESSION['email']}' AND `user_email` LIKE '%$_POST[search]%';";

                $result = mysqli_query($conn, $sql);
                while ($row = mysqli_fetch_assoc($result)) { ?>
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="ms-2 me-auto">
                            <div class="fw-bold h5"><?php echo $row["med_name"]; ?></div>
                            <b>Description: </b><?php echo $row["med_desc"]; ?>
                            <br>
                            <b>User Email: </b><?php echo $row["user_email"]; ?>
                            <br>
                            <b>Date: </b><?php echo $row["date"]; ?> | <b>Time: </b><?php echo $row["time"]; ?> | <b>Repeat Daily: </b><?php echo $row["isRepeating"]; ?>
                        </div>
                        <a href="REMINDER/edit.php?id=<?php echo $row["reminder_id"]; ?>" class="me-2">Edit</a>
                        <a href="REMINDER/delete.php?id=<?php echo $row["reminder_id"]; ?>" class="me-2">Delete</a>
                    </li>
                <?php
                } ?>
            </ol>
        </div>
    </div>

    <a href="" data-bs-toggle="modal" data-bs-target="#addModal" id="myBtn" type="button" class="btn btn-primary">ADD REMINDER</a>


    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <!-- jquery -->
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
    <script>
        $(function() {
            $("#datepicker").datepicker();
        });

        $('#datepicker').datepicker({
            dateFormat: 'dd-mm-yy'
        });
    </script>

    <!-- Alarm Repeat Checkbox -->
    <script>
        document.getElementById("repeat").value = "false";

        function isRepeating(checkbox) {
            if (checkbox.checked) {
                document.getElementById("repeat").value = "true";
            } else {
                document.getElementById("repeat").value = "false";
            }
        }
    </script>

</body>

</html>