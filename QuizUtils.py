#!/usr/bin/env python3
import mysql.connector
from datetime import datetime
import os

def createTest():
   mydb = mysql.connector.connect(
     host="localhost",
     user="root",
     passwd="",
     database='QuizWhizz'
   )
   testName=input("Enter the name of new Test:")
   endDateTime=input("Enter the End date of Test in the format YYYY-MM-DD: ")
   duration=input("Enter the Duration of Test in MM:SS format: ")
   sql = "insert into QuizWhizz.test(quizname, enddatetime,duration) values (%s,%s,%s)"
   val = (testName, endDateTime,duration)
   mycursor = mydb.cursor()
   mycursor.execute(sql, val) 
   mydb.commit()
   mydb.close()
   print("Successfully created the test")

def uploadTest():
   mydb = mysql.connector.connect(
     host="localhost",
     user="root",
     passwd="",
     database='QuizWhizz'
   )
   mycursor = mydb.cursor()
   validated = False
   while not validated:
      try:
         inputCsvFile=input("Enter the input csv file to load (with full path):")
         for line in open(inputCsvFile.rstrip('\n'),'r').readlines():
             line=line.replace('"','')
             line=line.replace("'",'').rstrip()
             fields=line.split('\t')
             if (len(fields) !=6):
                 print("Invalid layout. must have 6 fields (tab separated values) only.\nLayout:\nQuestion,Option1,2,3,4,CorrectOption")
                 return
             sql="CALL QuizWhizz.insertQuestion ( %s, %s, %s, %s,%s,%s, @r) "
             val=(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5] )
             mycursor.execute(sql, val)
         validated = True
         break
      except FileNotFoundError:
         print("FILE NOT FOUND, RE-ENTER THE FILE PATH")
         continue
    
   mydb.commit()
   mydb.close()
   print("Successfully associated questions to the created quiz")
   return True

def uploadStudents():
   mydb = mysql.connector.connect(
     host="localhost",
     user="root",
     passwd="",
     database='QuizWhizz'
   )
   mycursor = mydb.cursor()
# username, password, student_name
   validated = False
   while not validated:
      try:
         inputCsvFile=input("Enter the input csv file to load students (with full path):")
         for line in open(inputCsvFile.rstrip('\n'),'r').readlines():
            line=line.replace('"','')
         validated=True
      except FileNotFoundError:
         print("FILE NOT FOUND, RE-ENTER THE FILE PATH")
         validated=False
   if validated == False:
      return
   
   rows=0
   for line in open(inputCsvFile.rstrip('\n'),'r').readlines():
     line=line.replace('"','')
     line=line.replace("'",'').rstrip()
     fields=line.split('\t')
     if (len(fields) !=3):
        print("Invalid layout. must have 3 fields ( username, password, student_name ) in (tab separated values) only.\nLayout:\nQuestion,Option1,2,3,4,CorrectOption")
        return

     sql="insert into QuizWhizz.student_login (username, password, student_name) values ( %s, %s, %s ) "
     val=(fields[0], fields[1], fields[2] )
     try:
        r=mycursor.execute(sql, val) 
        if (r == None):
           print('Uploaded username:{}, password:{}, student_name:{}'.format(fields[0],fields[1],fields[2]))
           rows=rows+1
           mydb.commit()
     except (Exception):
        print('<<<Error while Uploading username:{}, password:{}, student_name:{}'.format(fields[0],fields[1],fields[2]))
        
   mydb.close()
   if (rows == 0):
      print("No students uploaded into student_login table.")
   else:
      print("Successfully uploaded {} students into student_login table.".format(rows))
   return True

def clearData():
   confirm=input('Are you sure to clear the tables: test, questions, test_questions: ? (Y/n)')
   confirm=confirm.upper()
   if (confirm != 'Y'):
      print('Not cleared, Returning back to Main Menu..')
      return
 
   r = os.system('/Applications/XAMPP/bin/mysql -u root < /Applications/XAMPP/htdocs/quizwhizz/clearData.sql ')
   if (r == 0):
      print('Successfully cleared the tables: test, questions, test_questions')
   else:
      print('<<<Error while clearing the tables: test, questions, test_questions')

def backupDatabase():
   ddmmyy = datetime.today().strftime('%Y%m%d-%H%M%S')
   os.system('/Applications/XAMPP/bin/mysqldump -u root --databases QuizWhizz --routines=true > ~/backup/QuizWhizz.DBDump.'+ddmmyy)

   os.system('tar -cvf ~/backup/All_php_sql_files_' +ddmmyy +'.tar /Applications/XAMPP/htdocs/quizwhizz/* /Users/jayanthvasanthkumar/OneDrive*/*NEA*/python/*')
   os.system('ls -ltr ~/backup/QuizWhizz.DBDump* ~/backup/All*tar')

   print('Successfully taken the backup at '+ddmmyy+' available in ~/backup/All_php_sql_files.*.tar' )

def displayMenu():
   print("+==============QuizWhizz Utilities==============+");
   print("|  Welcome to the QuizWhizz Utilities program   |");
   print("+================ M E N U ======================+");
   print("| 1]. Create and upload new quiz                |");
   print("|                                               |");
   print("| 2]. Upload student login details              |");
   print("|                                               |");
   print("| 3]. Clear all quiz data in the database       |");
   print("|                                               |");
   print("| 4]. Backup the full database                  |");
   print("|                                               |");
   print("| 5]. Exit                                      |");
   print("+ ==============================================+");

os.environ["PATH"] += ':/Applications/XAMPP/htdocs/bin'
while True:
   displayMenu()
   try:
      option = int(input("Enter your option:"))
   except ValueError:
      pass
   #print(option)
   
   if option == 1:
      createTest()
      valid = uploadTest()
      if (not valid):
         continue
   elif option == 2:
      valid = uploadStudents()
      if (not valid):
         continue
   elif option == 3:
      clearData()
   elif option == 4:
      backupDatabase()
   elif option == 5:
      print("Thank you for using the QW Utilities")
      print("Exiting...")
      exit()
   else:
      print("Enter a valid option!")
   continue

