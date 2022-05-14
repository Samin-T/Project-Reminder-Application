<?php
// including the database connection file
include_once("connection.php");

if (isset($_POST['update'])) {

    $id = $_POST['id'];

    $name = $_POST['MedName'];
    $desc = $_POST['MedDesc'];
    $date = $_POST['MedDate'];
    $time = $_POST['MedTime'];
    $isRepeating = $_POST['isRepeat'];
    $user_email = $_POST['UserEmail'];
    $hcw_email = $_POST['HCWEmail'];

    $sql  = "UPDATE `reminders` SET med_name='$name', med_desc='$desc', date='$date', time='$time', isRepeating='$isRepeating', user_email='$user_email', hcw_email='$hcw_email' WHERE reminder_id=$id";
    if ($conn->query($sql) === TRUE) {
        header("Location: ../dashboard.php");
    } else {
        echo json_encode(array("status" => "false", "message" => "error setting reminder"));
    }
    mysqli_close($conn);
}
?>

<?php
$id = $_GET['id'];

$query = "SELECT * FROM `reminders` WHERE reminder_id='$id'";
$result = mysqli_query($conn, $query);
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $reminderID =  $row['reminder_id'];
        $medName = $row['med_name'];
        $medDesc = $row['med_desc'];
        $date = $row['date'];
        $time = $row['time'];
        $isRepeating = $row['isRepeating'];
        $userEmail = $row['user_email'];
        $hcwEmail = $row['hcw_email'];
    }
}
mysqli_close($conn);
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RemindME - ADD</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">

    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">

    <!-- CSS -->
    <link rel="stylesheet" href="../CSS/style.css">

</head>

<body>
    <div class="container pt-5 div-center">
        <form method="POST" action="edit.php">
            <div class="mb-3">
                <label for="inputMedName" class="form-label title">Name</label>
                <input name="MedName" type="text" class="form-control" id="inputMedName" value="<?php echo $medName; ?>">
            </div>
            <div class="mb-3">
                <label for="inputMedDesc" class="form-label title">Description</label>
                <input name="MedDesc" type="text" class="form-control" placeholder="e.g., taken with a full glass of water" id="inputMedDesc" value="<?php echo $medDesc; ?>">
            </div>
            <div class="mb-3">
                <label for="inputDate" class="form-label title">Date</label>
                <input name="MedDate" id="datepicker" class="form-control" id="inputDate" value="<?php echo $date; ?>">
                <span class="input-group-append">
                </span>
            </div>
            <div class="mb-3">
                <label for="inputTime" class="form-label title">Time</label>
                <input name="MedTime" type="time" class="form-control" id="inputTime" value="<?php echo $time; ?>">
                <span class="input-group-append">
                </span>
            </div>
            <div class="mb-3">
                <label for="inputUser" class="form-label title">User Email</label>
                <input name="UserEmail" type="email" class="form-control" id="inputUser" value="<?php echo $userEmail; ?>" readonly="readonly">
                <span class="input-group-append">
                </span>
            </div>
            <div class="form-check mb-3">
                <input name="repeat" class="form-check-input" type="checkbox" id="flexCheckDefault" onchange="isRepeating(this)">
                <label class="form-check-label" for="flexCheckDefault">
                    Repeat Daily
                </label>
            </div>
            <input type="hidden" id="repeat" name="isRepeat" value="<?php echo $isRepeating; ?>">
            <input type="hidden" name="id" value="<?php echo $reminderID; ?>">
            <input type="hidden" name="HCWEmail" value="<?php echo $hcwEmail; ?>">

            <div class="text-center d-grid gap-2">
                <button type="submit" name="update" class="btn btn-primary btn-block">SET REMINDER</button>
            </div>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

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

    <script>
        if (document.getElementById("repeat").value == "true") {
            document.getElementById("flexCheckDefault").checked = true;
        } else {
            document.getElementById("flexCheckDefault").checked = false;
        }

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