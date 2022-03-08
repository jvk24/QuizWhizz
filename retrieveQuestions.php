<?php
require_once("conn.php");
/*
$student_name = mysqli_real_escape_string($conn, $_POST["name"]);
$username = mysqli_real_escape_string($conn, $_POST["user_name"]);
$password = mysqli_real_escape_string($conn, $_POST["password"]);
*/
$user_name=$_POST["user_name"];
$testId = mysqli_real_escape_string($conn, $_POST["testid"]);

$query1 = "SELECT q.*  FROM questions q
JOIN test_questions tq
ON tq.questionid = q.questionid
JOIN test t 
ON t.testId = tq.testID
AND now() BETWEEN t.startdatetime and t.enddatetime;";

$query2 = "SELECT '" . $user_name . "' AS user_name,
tq.testid,
(@row_number:=@row_number + 1) AS questionid,
q.question, q.option1, q.option2, q.option3, q.option4, q.correctoption FROM QuizWhizz.questions q JOIN QuizWhizz.test_questions tq ON tq.testid = " . $testId . " AND tq.questionid = q.questionid JOIN ( select @row_number := 0) r ORDER BY q.questionid;";

$result = mysqli_query($conn, $query2);


$questions_json=[];

$I = 0;

while ($row = mysqli_fetch_assoc($result)) 
{
    $questions_json[$I++] = $row;
}
$part1="{row_data:";
$part2="}";
echo $part1 . json_encode($questions_json) . $part2;

header("Content-type: application/json");


?>

 

