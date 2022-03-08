use QuizWhizz;

drop view statsView;

create view statsView as
SELECT 
       DATE_FORMAT(now(), '%Y/%m') as YYMM,
       t.quizname,
       count(*) as nof_students, 
       min(score) as min_score,
       max(score) as max_score,
       avg(score) as avg_score,
       stddev(score) as stdev_score,
       max(score) - min(score) as range_score,
       stddev(score)*stddev(score) as var_score,
       (select count(*) from test_questions tq where tq.testid = t.testId) as max_possible_score
from student_test_score sts
join test t
on sts.testid = t.testId
group by DATE_FORMAT(now(), '%Y/%m'), t.quizname;
