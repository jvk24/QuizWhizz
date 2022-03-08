<?php
require_once("conn.php");

$testId = mysqli_real_escape_string($conn, $_POST["testId"]);
$query = "SELECT duration 
	  FROM QuizWhizz.test
	  WHERE testId = '".$testId."';";

$result = mysqli_query($conn, $query);
/*
$duration_json=[];

$I = 0;

while ($row = mysqli_fetch_assoc($result)) 
{
    $duration_json[$I++] = $row;
}
$part1="{row_data:";
$part2="}";
echo $part1 . json_encode($duration_json) . $part2;

header("Content-type: application/json");*/

echo implode(mysqli_fetch_assoc($result));

?>