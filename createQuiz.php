<?php
require_once("conn.php");

$question = mysqli_real_escape_string($conn, $_POST["question"]);
$option1 = mysqli_real_escape_string($conn, $_POST["option1"]);
$option2 = mysqli_real_escape_string($conn, $_POST["option2"]);
$option3 = mysqli_real_escape_string($conn, $_POST["option3"]);
$option4 = mysqli_real_escape_string($conn, $_POST["option4"]);
$correctOption = mysqli_real_escape_string($conn, $_POST["correctOption"]);

$query = "INSERT INTO QuizWhizz.questions(question, option1, option2, option3, option4, correctoption) VALUES("."'".$question."',"."'".$option1."',"."'".$option2."',"."'".$option3."',"."'".$option4."',"."".$correctOption.");";

if ($conn -> query($query) == TRUE) {
    echo "Question set saved successfully!";
} else {
    echo "Error: [".$query."] ".$conn->error;
}
?>