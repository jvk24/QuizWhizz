<?php
$db_name = "QuizWhizz";
$username = "root";
$password = "";
$server_name = "localhost";
$conn = mysqli_connect($server_name, $username, $password, $db_name);

if (mysqli_connect_errno()) {
  echo "Failed to connect to Server";
  exit();
}
?>