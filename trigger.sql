create or replace trigger student_trigger_delete
after delete on students
for each row
begin
delete from enrollments where sid= :old.sid;
end;
/

create or replace trigger enrollments_trigger_delete
after delete on enrollments
for each row
begin
update classes set class_size=class_size-1 where classid=:old.classid;
end;
 /

create or replace trigger enrollments_trigger_insert
after insert on enrollments
for each row
begin
update classes set class_size=class_size+1 where classid= :new.classid;
end;
 /

CREATE OR REPLACE TRIGGER logs_insert_student
AFTER INSERT ON students
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log_number.NEXTVAL, user, SYSDATE, 'Students', 'insert', :NEW.sid);
END;
/

CREATE OR REPLACE TRIGGER logs_delete_student
AFTER DELETE ON students
FOR EACH ROW
BEGIN 
INSERT INTO logs VALUES(log_number.NEXTVAL, user, SYSDATE, 'Students', 'delete', :OLD.sid);
END;
/

CREATE OR REPLACE TRIGGER logs_insert_enrollments
AFTER INSERT ON enrollments
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log_number.NEXTVAL, user, SYSDATE, 'Enrollments', 'insert', :NEW.sid || ' ' || :NEW.classid);
END;
/

CREATE OR REPLACE TRIGGER logs_delete_enrollments
AFTER DELETE ON enrollments
FOR EACH ROW
BEGIN
INSERT INTO logs VALUES(log_number.NEXTVAL, user, SYSDATE, 'Enrollments', 'delete', :OLD.sid || ' ' || :OLD.classid);
END;
/

show errors