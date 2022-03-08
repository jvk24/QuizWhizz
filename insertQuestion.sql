use QuizWhizz;
DELIMITER $$

create procedure insertQuestion(IN _question varchar(1000), IN _option1 varchar(100), IN _option2 varchar(100), IN _option3 varchar(100), IN _option4 varchar(100), IN _correctoption varchar(30), OUT result int)
begin
   declare _testid int default 0;
   declare _questionid int default 0;
   DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @errorOccurred :=2;

   set @errorOccurred :=1;

   set _testid=(select max(testid) from test );

   START TRANSACTION;

   INSERT INTO questions (question, option1, option2, option3, option4, correctoption)
   VALUES (_question, _option1, _option2, _option3, _option4, _correctoption);

   set _questionid = (select last_insert_id());
   INSERT INTO test_questions (testid, questionid)
   VALUES ( _testid, _questionid);

   if (@errorOccurred=2) then
      ROLLBACK;
   else
      COMMIT;
   end if;

end$$
delimiter ;

