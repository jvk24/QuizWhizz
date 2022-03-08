<?php
require_once("conn.php");

$query_1 = "SELECT * FROM statsview;";
$result = mysqli_query($conn, $query_1);

$rows_json=[];
$I = 0;

while ($row = mysqli_fetch_assoc($result)) 
{
    $rows_json[$I++] = $row;
}
$part1="{row_data:";
$part2="}";
echo $part1 . json_encode($rows_json) . $part2;

header("Content-type: application/json");
?>