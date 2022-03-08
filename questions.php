<?php
require_once("conn.php");

# To test this part, enter below line in browser::
# http://localhost/quizwhizz/questions.php?question=What&nbspis&nbspyour&nbspname?&option1=vvvv&option2=wwww&option3=xxxx&option4=zzzz&correctoption=1
# This php script inserts in 2 tables: test, test_questions. It also reads the max(testid) from test ;

$question = mysqli_real_escape_string($conn, $_POST["question"]);
$option1 = mysqli_real_escape_string($conn, $_POST["option1"]);
$option2 = mysqli_real_escape_string($conn, $_POST["option2"]);
$option3 = mysqli_real_escape_string($conn, $_POST["option3"]);
$option4 = mysqli_real_escape_string($conn, $_POST["option4"]);
$correctoption = mysqli_real_escape_string($conn, $_POST["correctOption"]);


########### ########### ########### ########### ########### ########### ########### ###########
# Step-1. Call the stored procedure : insertQuestion
########### ########### ########### ########### ########### ########### ########### ###########

$query = "CALL QuizWhizz.insertQuestion ( '" . $question . "', '" . $option1  . "', '" . $option2  . "', '" . $option3  . "', '" . $option4  . "', '" . $correctoption  . "', @ret ) ";
if ($conn -> query($query) == TRUE)
{    
  echo "Successful inserted a question!";
} else {
    echo "Error-1: inserting a question :[".$query."] ".$conn->error;
}
?>

