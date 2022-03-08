<?php
require_once("conn.php");

$username = mysqli_real_escape_string($conn, $_POST["username"]);
$testid = mysqli_real_escape_string($conn, $_POST["testid"]);
$score = mysqli_real_escape_string($conn, $_POST["score"]);

$query=" DELETE FROM student_test_score where studentid in ( select id from  student_login WHERE student_login.username = '" . $username . "' ) and testid = " . $testid ;

if ($conn -> query($query) == TRUE) {
    echo "Student previous test score cleaned up successfully!";
} else {
    echo "Error: saving in cleaningup student_test_score [".$query."] ".$conn->error;
}

 echo "Inserting into student_test_score with testid=$testid";

 $query = "INSERT INTO  student_test_score(studentid,testid,score,completion_time) select student_login.id, " . $testid . "," . $score . " , now() FROM  student_login WHERE student_login.username = '" . $username . "';";
 
 if ($conn -> query($query) == TRUE) {
     echo "\nStudent test score saved successfully!\n" . $username . "," . $testid;
 } else {
     echo "Error: saving in saving student_test_score [".$query."] ".$conn->error;
 }

?>
