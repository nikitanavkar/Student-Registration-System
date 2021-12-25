set serveroutput on

/*Procedure to get student information*/
create or replace procedure student_info(studentid in students.sid%type, showmessage OUT VARCHAR2,stud_info OUT sys_refcursor)
is
course_reg int;
student_exist1 int;
begin
select count(*) into student_exist1 from students s where s.sid=studentid;
if student_exist1 = 0
then
 dbms_output.put_line('The sid is invalid');
showmessage := 'The sid is invalid';
else
select count(*) into course_reg from students s,enrollments e, classes c where s.sid =studentid and e.sid = s.sid and e.classid = c.classid;
if course_reg = 0
then
 dbms_output.put_line('The student has not taken any course');
showmessage := 'The student has not taken any course';
else
open stud_info for
select s.sid, s.firstname, s.lastname, s.gpa, c.classid, concat(c.dept_code,c.course_no) course, c.semester, c.year  from students s,enrollments 
e, classes c where s.sid=studentid and e.sid = s.sid and e.classid = c.classid;
end if;
end if; 
end;
/
show errors

/****************************************************************************************************************************************************
*/

/*Procedure to get course information*/
create or replace procedure course_info(deptcode in prerequisites.dept_code%type,cnum in prerequisites.course_no%type,count1 in number,result out varchar2)
is
course_exist1 number;
count2 number;
pre_code prerequisites.dept_code%type;
pre_num prerequisites.course_no%type;
pre_title courses.title%type;
CURSOR course1_info is
select pre_dept_code, pre_course_no, pre_title  FROM
prerequisites p, courses c WHERE p.dept_code=deptcode and p.course_no = cnum and p.dept_code=c.dept_code and p.course_no=c.course_no;
detail course1_info%rowtype;
begin
select count(*) into course_exist1 from prerequisites p where p.dept_code=deptcode and p.course_no=cnum;
open course1_info;
fetch course1_info into detail;
while(course1_info%found) loop
count2:=count1+1;
course_info(detail.pre_dept_code, detail.pre_course_no,count2,result);
if(result is NULL) then
result := detail.pre_dept_code || detail.pre_course_no || detail.pre_title;
else
result := result || ',' || detail.pre_dept_code || detail.pre_course_no || detail.pre_title;
end if;

fetch course1_info into detail;
end loop;
if(course_exist1=0 and count1=0)then
 result := ' Does not exist';
end if;
end;
/
show errors

/****************************************************************************************************************************************************
*/

/*Procedure to get class information*/
create or replace procedure class_info(id in classes.classid%type,showmessage OUT VARCHAR2,class1_info OUT sys_refcursor)
is
class_exist2 int;
student_exist2 int;
begin
select count(*) into class_exist2 from classes c where c.classid=id;
if class_exist2 = 0
then 
dbms_output.put_line('The cid is invalid');
showmessage := 'The cid is invalid';
else
select count(*) into student_exist2 from enrollments e where e.classid=id;
if student_exist2 = 0
then
dbms_output.put_line('No student is enrolled in the class');
showmessage := 'No student is enrolled in the class';
else
open class1_info for
select c1.classid,c2.title,c1.semester,c1.year,s.sid,s.firstname,s.lastname,s.email from classes c1,courses c2, enrollments e, students s where c1.classid=id and c1.dept_code=c2.dept_code and c1.course_no= c2.course_no and c1.classid=e.classid and e.sid=s.sid;
end if;
end if;
end;
/
show errors

/****************************************************************************************************************************************************
*/

/*Procedure to enroll student*/
create or replace procedure enroll_student(std_id in students.sid%type,cl_id in classes.classid%type,showmessage OUT VARCHAR2) 
is 
student_exist3 int; 
class_exist3 int; 
count_wrong int;
check_student int;
count_enroll int;
dep_code classes.dept_code%type;
cnum classes.course_no%type;
pre_classes varchar2(100);
pre_count int;
pre_count2 int;
begin 
select count(*) into student_exist3 from students s where std_id=s.sid; 
select count(*) into class_exist3 from classes c where cl_id = c.classid;
select count(*) into count_wrong from classes c where c.class_size+1>limit and cl_id = c.classid; 
select count(*) into check_student from enrollments e where e.sid=std_id and e.classid = cl_id;
select count(*) into count_enroll from enrollments e where e.sid=std_id;
select dept_code,course_no into dep_code,cnum from classes where classid=cl_id;
course_info(dep_code, cnum, 0,pre_classes);

select count(*) into pre_count from enrollments e,classes c where sid = std_id and e.classid = c.classid 
and INSTR(pre_classes, c.dept_code || c.course_no) != 0 and e.lgrade not in ('A','A-','B+','B','B-','C+','C');

select count(*) into pre_count2 from enrollments e,classes c where sid = std_id and e.classid = c.classid 
and INSTR(pre_classes, c.dept_code || c.course_no) = 0;
if 
student_exist3=0 
then
showmessage := 'sid not found';
dbms_output.put_line('sid not found');

elsif class_exist3=0 
then
showmessage := 'classid not found';
dbms_output.put_line('classid not found');

elsif count_wrong > 0 
then
showmessage := 'class full';
dbms_output.put_line('class full');

elsif check_student>0 
then
showmessage := 'already in the course';
dbms_output.put_line('already in this class');

elsif count_enroll>4 
then
showmessage := 'overloaded!';
dbms_output.put_line('overloaded!');

elsif pre_count2>0 
then
showmessage := 'Prerequisites courses have not been completed';
dbms_output.put_line('Prerequisites courses have not been completed');
elsif pre_count>0 
then
showmessage := 'Prerequisites courses have not been completed';
dbms_output.put_line('Prerequisites courses have not been completed');

else
if count_enroll=3 
then
dbms_output.put_line('Successfully enrolled, course count is 4');
insert into enrollments values (std_id,cl_id,null);
showmessage := 'Successfully enrolled, course count is 4';
else
showmessage := 'Successfully enrolled';
insert into enrollments values (std_id,cl_id,null);
end if;
end if;
end;
/
show errors

/****************************************************************************************************************************************************
*/

/*Procedure to delete student enrollment*/
create or replace procedure delete_enrollment(std_id in students.sid%type,cl_id in classes.classid%type, showmessage OUT VARCHAR2) 
is 
student_exist4 int; 
class_exist4 int; 
enroll_exist int; 
course_count int;
enroll_exit int
dep_code classes.dept_code%type;
cnum classes.course_no%type;
pre_count int;
begin 
select count(*) into student_exist4 from students s where std_id=s.sid; 
select count(*) into class_exist4 from classes c where cl_id = c.classid; 
select count(*) into enroll_exist from enrollments e where e.sid=std_id and e.classid=cl_id;
select count(*) into enroll_exist1 from enrollments e where e.sid=std_id; 
select count(*) into course_count from enrollments e where e.classid=cl_id;
if 
student_exist4=0 
then
showmessage := 'sid not found';
dbms_output.put_line('sid not Found');
commit;
elsif class_exist4=0 
then
showmessage := 'classid not found';
dbms_output.put_line('classid not found');
commit;
elsif enroll_exist=0 
then
showmessage := 'student not enrolled in the class';
dbms_output.put_line('student not enrolled in the class');
commit;
else

select dept_code,course_no into dep_code, cnum from classes where classid=cl_id;

select count(*) into pre_count from classes cl,prerequisites p where cl.classid in 
(select classid from enrollments e where e.classid != cl_id and e.sid=std_id) 
and cl.dept_code=p.dept_code and cl.course_no = p.course_no and
p.pre_dept_code=dep_code and
p.pre_course_no=cnum;
if(pre_count=0) then
if(enroll_exist1=1) then
delete from enrollments e where e.sid = std_id and e.classid=cl_id;
showmessage := 'student enrolled in no class';
dbms_output.put_line('student enrolled in no class');
elsif(course_count=1) then
delete from enrollments e where e.sid = std_id and e.classid=cl_id;
showmessage := 'no student in this class';
dbms_output.put_line('no student in this class');
else
delete from enrollments e where e.sid = std_id and e.classid=cl_id;
showmessage := 'The student dropped successfully';
dbms_output.put_line('The student is successfully dropped');
end if;
else
showmessage := 'drop request rejected due to prerequisite requirements';
dbms_output.put_line('drop request rejected due to prerequisite requirements');

end if;
end if;

end;
/
show errors

/****************************************************************************************************************************************************
*/

/*Procedure to delete student*/
create or replace procedure delete_student(std_id in students.sid%type)
is
student_exist5 int;
student_enrolled int;
begin
select count(*) into student_exist5 from students where sid=std_id;
if student_exist5=0
then
dbms_output.put_line('sid is invalid');
else
select count(*) into student_enrolled from students s, enrollments e 
where s.sid = std_id and s.sid = e.sid;
if student_enrolled = 0
then
dbms_output.put_line('student is not enrolled in any courses');
delete from students where sid=std_id;
commit;
dbms_output.put_line('student deleted sucessfully');
else
delete from enrollments where sid = std_id;
delete from students s where s.sid = std_id;
commit;
dbms_output.put_line('student deleted sucessfully from enrollments table as well');
end if;
end if;
end;
 /
 show errors