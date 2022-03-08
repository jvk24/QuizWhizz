<?php
require_once("conn.php");

$username = mysqli_real_escape_string($conn, $_POST["username"]);

$query = "SELECT quizname, enddatetime FROM QuizWhizz.test;";
$query_2 = "SELECT testId, quizname, DATE_FORMAT(enddatetime, '%Y-%m-%d') as enddatetime FROM  test WHERE enddatetime > CURDATE() and testId not in ( select testid from student_test_score where studentid in ( select id from student_login where username = '" . $username . "' ) )" ;
$result = mysqli_query($conn, $query_2);

$tests_json=[];
$I = 0;

while ($row = mysqli_fetch_assoc($result)) 
{
    $tests_json[$I++] = $row;
}

$part1="{row_data:";
$part2="}";
echo $part1 . json_encode($tests_json) . $part2;

header("Content-type: application/json");
?>