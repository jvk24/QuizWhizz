<?php
require_once("conn.php");
# url for testing: http://localhost/QuizWhizz/retrieveScore.php
# $username = mysqli_real_escape_string($conn, $_POST["username"]);

$query_2 = "SELECT  t.testId,  t.quizname,  sl.student_name, sts.score  
FROM student_test_score sts 
JOIN student_login sl 
ON sts.studentid = sl.id
JOIN test t
ON sts.testid = t.testId 
ORDER BY t.quizname, sts.score DESC ";

$result = mysqli_query($conn, $query_2);

$tests_json=[];
$I = 0;

$pre_testid="";
while ($row = mysqli_fetch_assoc($result)) 
{
    if ($pre_testid != $row["testId"])
       $rank=1;
    else
       $rank++;
    $row["rank"] = $rank;
    $tests_json[$I++] = $row;
    $pre_testid=$row["testId"];
}

$part1="{row_data:";
$part2="}";
echo $part1 . json_encode($tests_json) . $part2;

header("Content-type: application/json");
?>
