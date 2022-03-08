<?php
require "conn.php";

$username_entered = mysqli_real_escape_string($conn, $_POST["user_name"]);
$password_entered = mysqli_real_escape_string($conn, $_POST["password"]);

$query = "SELECT id,student_name,username FROM student_login WHERE username='" . $username_entered . "' and password='" . $password_entered . "' LIMIT 1";
$result = mysqli_query($conn, $query);
$student_name="";

if (mysqli_num_rows($result) > 0) {
    while ($row = $result->fetch_row()) {
        $student_name = $row[1];
    }
    //echo "Welcome " .  $student_name;
    echo "Successful login!";
} else {
        echo "login not successful";
}
?>