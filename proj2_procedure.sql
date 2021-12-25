create or replace package proj2_procedure as
procedure show_students(students_curs out sys_refcursor);
procedure show_courses(courses_curs out sys_refcursor);
procedure show_prereq(prereq_curs out sys_refcursor);
procedure show_classes(classes_curs out sys_refcursor);
procedure show_enrollments(enrollments_curs out sys_refcursor);
procedure show_logs(logs_curs out sys_refcursor);
procedure insert_student(studentid in students.sid%type,Fname in 
students.firstname%type, Lname in students.lastname%type, Stat in 
students.status%type,gp in students.gpa%type,Mail in 
students.email%type);
end;
/
create or replace package body proj2_procedure as
procedure show_students(students_curs out sys_refcursor) as
begin
open students_curs for
select * from students;
end show_students;


procedure show_courses(courses_curs out sys_refcursor) as
begin
open courses_curs for
select * from courses;
end show_courses;


procedure show_prereq(prereq_curs out sys_refcursor) as
begin
open prereq_curs for
select * from prerequisites;
end show_prereq;



procedure show_classes(classes_curs out sys_refcursor) as
begin
open classes_curs for
select * from classes;
end show_classes;


procedure show_enrollments(enrollments_curs out sys_refcursor) as
begin
open enrollments_curs for
select * from enrollments;
end show_enrollments;

procedure show_logs(logs_curs out sys_refcursor) as
begin
open logs_curs for
select * from logs;
end show_logs;

procedure insert_student(studentid in students.sid%type,Fname in
students.firstname%type, Lname in students.lastname%type, Stat in
students.status%type,gp in students.gpa%type,Mail in
students.email%type)
 is
 begin 
    insert into 
students("SID","FIRSTNAME","LASTNAME","STATUS","GPA","EMAIL") 
values(studentid,Fname,Lname,Stat,gp,Mail);
COMMIT;
end insert_student;

end proj2_procedure;
/
show errors

