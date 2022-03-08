<?php
        echo "Welcome to QuizWhiz, HELLO It is working!!!1234 " ;

require_once("dbConnect.php");
$username_str="jayanthvk";
$password_str="jayanthvk1234";
$type_str="S";
$query=" SELECT id,username,password,student_name FROM QuizWhizz.student_login ";
$query = $query . " WHERE student_login = '" . $username_str + "' ";
$query = $query . " AND   password      = '" . $password_str + "' ";

$result = $conn->query($query);


if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $row_id=$row["id"];
        $row_username=$row["username"];
        $row_password=$row["password"];
        $row_student_name=$row["student_name"];

        $arr = array(
               "result" => "SUCCESS",
               "id" => $row_id,
               "username" => $username_str,
	       "password" => $password_str,
               "student_name" => $student_name_str);

  echo "Yes inside success";
    }
} else {
        $arr = array(
               "username" => $username_str,
	       "password" => $password_str,
               "result" => "FAIL");
  
        }
  echo json_encode($arr);

?>
?>
