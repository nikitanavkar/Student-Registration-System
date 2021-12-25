import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;


public class proj2 
{

    public static void main(String[] args)  throws SQLException
    {
    try
    {
    //Connection to Oracle server
    OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
    ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
    Connection conn = ds.getConnection("yvartak1","yuraj1996");
    int User_Selection = -1;
    BufferedReader readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
    String choice;
                
    //Available options
    while(User_Selection != 0)
    {
    System.out.println("\nPlease select one of the following options:");
    System.out.println("1- Print all the tables in the database");
    System.out.println("2- Add a student");
    System.out.println("3- Get the information for student");
    System.out.println("4- Show the prerequisites courses for a given course");
    System.out.println("5- Show the information and the list of students for a given class");
    System.out.println("6- Enroll a student into a class");
    System.out.println("7- Drop a student from a class");
    System.out.println("8- Delete a student");
    System.out.println("0- Exit");
    
    choice = readKeyBoard.readLine();
    User_Selection = Integer.parseInt(choice);
    //Function call based on choice
    
    if(User_Selection == 1)
    {
    
    // print all the tables
    printTables(conn);
    }
    else if(User_Selection == 2)
    {
    //add student
    addStudent(conn);
    }
    else if(User_Selection == 3)
    {
    //getStudentInfo
    getStudentInfo(conn);
    }
    else if(User_Selection == 4)
    {
    getCoursePrereq(conn);
    }
    else if(User_Selection == 5)
    {
    //showcourseinfo
    classInfoStudents(conn);
    }
    else if(User_Selection == 6)
    {
    enrollStudent(conn);
    }
    else if(User_Selection == 7)
    {
    dropStudent(conn);
    }
    else if(User_Selection == 8)
    {
    deleteStudent(conn);
    }
             
    }
    conn.close();
    }
    catch(SQLException ex){
    System.out.println("SQL Exception");
    }
    catch(Exception e){
    System.out.println("Exception");
    }
    }
public static void printTables(Connection conn) throws SQLException
{	
/*The proj2_procedure package is used to get the information of the tables in the database*/
try{
//Using the show_students procedure in the proj2_procedure package for students table
CallableStatement cs = conn.prepareCall("begin proj2_procedure.show_students(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);
//Printing Student table
// execute and retrieve the result set
cs.execute();
ResultSet rs = (ResultSet)cs.getObject(1);
System.out.println("STUDENTS TABLE");
System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
System.out.println("SID\t\t\tFIRSTNAME\t\tLASTNAME\t\tSTATUS\t\t\tGPA\t\tEMAIL");
System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");
while (rs.next())
{
System.out.println(rs.getString(1)+"\t\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3)+"\t\t\t"+rs.getString(4)+"\t\t\t"+rs.getString(5)+"\t\t"+rs.getString(6));
}

//Using the show_courses procedure in the proj2_procedure package for courses table
cs = conn.prepareCall("begin proj2_procedure.show_courses(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);
//Printing Course table
// execute and retrieve the result set
cs.execute();
rs = (ResultSet)cs.getObject(1);
System.out.println("\nCOURSES TABLE");
System.out.println("-------------------------------------------------------------------------------------------------------------");
System.out.println("DEPT_CODE\t\tCOURSE_NO\t\tTITLE");
System.out.println("-------------------------------------------------------------------------------------------------------------");
while (rs.next())
{
System.out.println(rs.getString(1)+"\t\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3));
}
           
//Using the show_prereq procedure in the proj2_procedure package for prerequisites table    
cs = conn.prepareCall("begin proj2_procedure.show_prereq(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);
//Printing Prerequisites table
// execute and retrieve the result set
cs.execute();
rs = (ResultSet)cs.getObject(1);
System.out.println("\nPREREQUISITES TABLE");
System.out.println("-------------------------------------------------------------------------------------------------------------");
System.out.println("DEPT_CODE\t\tCOURSE_NO\t\tPRE_DEPT_CODE\t\tPRE_COURSE_NO");
System.out.println("-------------------------------------------------------------------------------------------------------------");
while (rs.next())
{
System.out.println(rs.getString(1)+"\t\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3)+"\t\t\t"+rs.getString(4));
}

//Using the show_classes procedure in the proj2_procedure package for classes table 
cs = conn.prepareCall("begin proj2_procedure.show_classes(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);
//Printing Classes table
// execute and retrieve the result set
cs.execute();
rs = (ResultSet)cs.getObject(1);
System.out.println("\nCLASSES TABLE");
System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
System.out.println("CLASSID\tDEPT_CODE\tCOURSE_NO\tSECT_NO\t\tYEAR\t\tSEMESTER\tLIMIT\t\tCLASS_SIZE");
System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
while (rs.next())
{
System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t\t"+rs.getString(3)+"\t\t"+rs.getString(4)+"\t\t"+rs.getString(5)+"\t\t"+rs.getString(6)+"\t\t"+rs.getString(7)+"\t\t"+rs.getString(8));
}
//Using the show_enrollments procedure in the proj2_procedure package for enrollments table       
cs = conn.prepareCall("begin proj2_procedure.show_enrollments(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);
// execute and retrieve the result set
cs.execute();
rs = (ResultSet)cs.getObject(1);
System.out.println("\nENROLLMENTS TABLE");
System.out.println("-------------------------------------------------------------------------------------------------------------");
System.out.println("SID\t\t\tCLASSID\t\t\tLGRADE");
System.out.println("-------------------------------------------------------------------------------------------------------------");
while (rs.next())
{
System.out.println(rs.getString (1)+"\t\t\t"+rs.getString(2)+"\t\t\t"+rs.getString(3));
}
//Using the show_logs procedure in the proj2_procedure package for logs table
cs = conn.prepareCall("begin proj2_procedure.show_logs(?); end;");
cs.registerOutParameter(1,OracleTypes.CURSOR);  
cs.execute();
rs = (ResultSet)cs.getObject(1);
System.out.println("\nLOGS TABLE");
System.out.println("-------------------------------------------------------------------------------------------------------------");                                       
System.out.printf("logid\twho\t\ttime\t\t\ttable_name\toperation\tkey_value\n");
System.out.println("-------------------------------------------------------------------------------------------------------------");
while(rs.next())
{
System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t\t"+rs.getString(6));
}
rs.close();        
}
catch(SQLException ex){
System.out.println("SQL Exception in Print table function:");
System.out.println(ex);
}

return;
}

//----------------------------------------------------------------------------------------------------------------------

public static void addStudent(Connection conn) throws SQLException
{
/*This function is used to add a student into student table using insert_student in the proj2_procedure*/
try{
// Fetching the inputs 
BufferedReader readkey = new BufferedReader(new InputStreamReader(System.in));        
System.out.println("Enter the student information:");
System.out.println("SID:");
String sid = readkey.readLine();
System.out.println("First Name:");
String firstName = readkey.readLine();
System.out.println("Last Name:");
String lastName = readkey.readLine();
System.out.println("Status (freshman, sophomore, junior, senior, graduate):");
String status = readkey.readLine();
System.out.println("GPA (0 to 4):");
String g = readkey.readLine();
double gpa = Double.parseDouble(g);
System.out.println("Email:");
String email = readkey.readLine();

//Call insert_student procedure:
CallableStatement cs = conn.prepareCall("begin proj2_procedure.insert_student(:1,:2,:3,:4,:5,:6); end;");				
//set the parameters
cs.setString(1,sid);           
cs.setString(2,firstName);
cs.setString(3,lastName);
cs.setString(4,status);
cs.setDouble(5,gpa);
cs.setString(6,email);
cs.execute();
cs.close();
}
catch(SQLException ex){ 
System.out.println("SQL Exception in addstudent function");
System.out.println(ex);     
}
catch(Exception e){System.out.println("Exception in addStudent");}
return;
}

//---------------------------------------------------------------------------------------------------------------------

public static void getStudentInfo(Connection conn) throws SQLException
{
/*This function in being used to get the infomation of a given student*/	
try
{
BufferedReader readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the Student ID");
String sid = readKeyBoard.readLine();
CallableStatement cs = conn.prepareCall("begin student_info(:1,:2,:3); end;");
cs.setString(1,sid);
cs.registerOutParameter(2,Types.VARCHAR);
cs.registerOutParameter(3,OracleTypes.CURSOR);
cs.execute();

String showmessage = null;
showmessage =  cs.getString(2);
ResultSet rs = (ResultSet)cs.getObject(3);

//condition to check error in pl/sql
if(showmessage ==  null)
{
//Print the results
System.out.println("SID\t\tFNAME\t\tLNAME\t\tGPA\t\tCLASSID\t\tCOURSE\t\tSEMESTER\tYEAR");
System.out.println("---------------------------------------------------------------------------------------------------------------------");
while(rs.next())
{	
System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3) + "\t\t" + rs.getString(4)+ "\t\t"+ rs.getString(5)+ "\t\t"+ rs.getString(6)+ "\t\t"+ rs.getString(7)+ "\t\t"+ rs.getString(8));
}
}
else
{
System.out.println(showmessage);
}

cs.close();
}
catch(SQLException ex){
System.out.println("SQLException in getStudentInfo function");
}
catch(Exception e){
System.out.println("Exception in getStudentInfo");
}

return;
}

//---------------------------------------------------------------------------------------------------------------------------

public static void classInfoStudents(Connection conn)
{
try
{
BufferedReader readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the class ID");
String cid = readKeyBoard.readLine();
CallableStatement cs = conn.prepareCall("begin class_info(:1,:2,:3); end;");
cs.setString(1,cid);
cs.registerOutParameter(2,Types.VARCHAR);
cs.registerOutParameter(3,OracleTypes.CURSOR);
cs.execute();

String showmessage = null;
showmessage =  cs.getString(2);
ResultSet rs = (ResultSet)cs.getObject(3);

if(showmessage == null)
{
//Print the results
System.out.println("CLASSID\t\tTITLE\t\tSEMESTER\t\tYEAR\t\tSID\t\tFNAME\t\tLNAME\t\tEMAIL");
System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
while(rs.next())
{	
System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t\t" + rs.getString(4)+ "\t\t"+ rs.getString(5)+"\t\t" + rs.getString(6)+"\t\t" + rs.getString(7)+"\t\t" + rs.getString(8));
}
}
else
{
System.out.println(showmessage);
}
cs.close();
}
catch(SQLException ex){
System.out.println("SQL Exception in classInfoStudents function");
}
catch(Exception e){
System.out.println("Exception in classInfoStudents");
}

return;
}

//---------------------------------------------------------------------------------------------------

public static void deleteStudent(Connection conn) throws SQLException
{
/*This function is being used to delete a student*/
try
{
BufferedReader readKey = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the student SID to delete");
String sid = readKey.readLine();
CallableStatement cs = conn.prepareCall("begin delete_student(:1); end;");
cs.setString(1,sid);
cs.execute();
cs.close();
}
catch(SQLException ex){
System.out.println("SQLException in delete Student function");
}
catch(Exception e){
System.out.println("Exception in delete Student function");
}
}

//-----------------------------------------------------------------------------------------------------

public static void getCoursePrereq(Connection conn) throws SQLException
{
try
{
BufferedReader readKey = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the DeptCode");
String deptCode = readKey.readLine();
System.out.println("Enter the course number");
String coursen = readKey.readLine();
int courseNo = Integer.parseInt(coursen);
int count = 0;
String result;
CallableStatement cs = conn.prepareCall("begin course_info(:1,:2,:3,:4); end;");
cs.setString(1,deptCode);
cs.setInt(2,courseNo);
cs.setInt(3,count);
cs.registerOutParameter(4, Types.VARCHAR);
cs.execute();
result = cs.getString(4);
//Print the results
System.out.println("PRE-REQUISITE COURSES");
System.out.println("-----------------------------------------------------------------------------------------------");     
System.out.println(result);

cs.close();
}
catch(SQLException ex){
System.out.println("SQL Exception in getCoursePrereq funtion");
}
catch(Exception e){
System.out.println("Exception in getCoursePrereq function");
}
}

//------------------------------------------------------------------------------------------------------

public static void enrollStudent(Connection conn) throws SQLException
{
try
{
BufferedReader readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the student ID");
String sid = readKeyBoard.readLine();
System.out.println("Enter the class ID");
String cid = readKeyBoard.readLine();
CallableStatement cs = conn.prepareCall("begin enroll_student(:1,:2,:3); end;");
cs.setString(1,sid);
cs.setString(2,cid);
cs.registerOutParameter(3,Types.VARCHAR);
cs.execute();

String showmessage = null;
showmessage =  cs.getString(3);
System.out.println(showmessage);		

cs.close();
}
catch(SQLException ex){
System.out.println("SQL Exception in enrollment function");
}
catch(Exception e){
System.out.println("Exception in enrollement");
}

return;
}

//--------------------------------------------------------------------------------------------------------------

public static void dropStudent(Connection conn) throws SQLException
{  
try
{
BufferedReader readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
System.out.println("Enter the student ID");
String sid = readKeyBoard.readLine();
System.out.println("Enter the class ID");
String cid = readKeyBoard.readLine();
CallableStatement cs = conn.prepareCall("begin delete_enrollment(:1,:2,:3); end;");
cs.setString(1,sid);
cs.setString(2,cid);
cs.registerOutParameter(3,Types.VARCHAR);
cs.execute();

String showmessage = null;
showmessage =  cs.getString(3);
System.out.println(showmessage);
		
cs.close();
}
catch(SQLException ex){
System.out.println("SQL Exception in dropstudent function");
}
catch(Exception e){
System.out.println("Exception in dropstudent");
}

return;	
}
}
