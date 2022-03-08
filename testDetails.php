<?php
require_once("conn.php");

# To test this part, enter below line in browser::
# http://localhost/quizwhizz/testDetails.php?quizName=Testing&quizDueDate=2011-11-24

$quizName = mysqli_real_escape_string($conn, $_POST["quizName"]);
$quizDueDate = mysqli_real_escape_string($conn, $_POST["quizDueDate"]);
$quizDuration = mysqli_real_escape_string($conn, $_POST["quizDuration"]);

$query = "INSERT INTO QuizWhizz.test(quizname, enddatetime, duration)
	  VALUES('".$quizName."','".$quizDueDate."','".$quizDuration."');";

if ($conn -> query($query) == TRUE) {
    echo "Question set saved successfully!";
} else {
    echo "Error: [".$query."] ".$conn->error;
}

?>