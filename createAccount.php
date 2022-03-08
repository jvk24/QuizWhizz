<?php
require_once("conn.php");

$student_name = mysqli_real_escape_string($conn, $_POST["name"]);
$username= mysqli_real_escape_string($conn, $_POST["user_name"]);
$password= mysqli_real_escape_string($conn, $_POST["password"]);

$query="SELECT id FROM QuizWhizz.student_login where 1=1 ";
$query=$query . " AND username = '" . $username . "' ";
$result = $conn->query($query);

if ($result->num_rows > 0) {
  echo "This record with username:" . $username . " already exists!";
}
else {
    $query = "INSERT INTO QuizWhizz.student_login ( student_name, username, password ) values ( ";
    $query = $query . "'" . $student_name . "',"; 
    $query = $query . "'" . $username . "',"; 
    $query = $query . "'" . $password . "')"; 
    if ($conn->query($query) == TRUE) {
        echo "New record created successfully";
    } else {
        echo "Error: [" . $query . "]" . $conn->error;
    }
}
?>