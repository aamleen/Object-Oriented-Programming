import java.io.*;
import java.util.*;

public class Backpack {
    private course AP;
    private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    Backpack(){
        AP=new course(2,3);
    }

    void Prof_Menu()throws IOException{
        /*1. Add class material
        2. Add assessments
        3. View lecture materials
        4. View assessments
        5. Grade assessments
        6. Close assessment
        7. View comments
        8. Add comments
        9. Logout*/
        System.out.println("Instructors: ");
        AP.view_profs();
        System.out.println("Choose ID: ");
        int ID =Integer.parseInt(br.readLine());
        Professor prof =AP.getProf(ID);
        if(prof==null){
            System.out.println("Invalid Professor ID: ");
            return;
        }
        int choice;
        while(true){
            System.out.println("1. Add class material \n2. Add assessments \n3. View lecture materials");
            System.out.println("4. View assessments \n5. Grade assessments \n6. Close assessment");
            System.out.println("7. View comments \n8. Add comments \n9. Logout");
            choice= Integer.parseInt(br.readLine());
            switch (choice){
                case 1: AP.add_lectures(prof);
                    break;
                case 2: AP.add_assessments(prof);
                    break;
                case 3: AP.view_lectures(prof);
                    break;
                case 4: AP.view_assessment(prof);
                    break;
                case 5: AP.assessment_grades(prof);
                    break;
                case 6: AP.close_assessment(prof);
                    break;
                case 7: AP.view_comments(prof);
                    break;
                case 8: AP.add_comments(prof);
                    break;
                case 9: return;
                default:    System.out.println("Invalid choice!!");
            }
        }
    }

    void Student_Menu(){
        /*1. View lecture materials
        2. View assessments
        3. Submit assessment
        4. View grades
        5. View comments
        6. Add comments
        7. Logout*/
        System.out.println("Students: ");
        AP.view_studs();
        System.out.println("Choose ID: ");
        int choice;
        Professor stud=AP.getStud(choice);
        while(true){
            choice= 0;//int
            switch (choice){
                case 1: AP.view_lectures(stud);
                    break;
                case 2: AP.view_assessment(stud);
                    break;
                case 3: AP.submit_assessments(stud);
                    break;
                case 4: AP.assessment_grades(stud);
                    break;
                case 5: AP.view_comments(stud);
                    break;
                case 6: AP.add_comments(stud);
                    break;
                case 7: return;
                default:    System.out.println("Invalid choice!!");
            }
        }
    }

    void Login(){
        int choice;
        while(true){
            System.out.println("Welcome to Backpack");
            System.out.println("Enter as Instructor");
            System.out.println("Enter as Student");
            System.out.println("Exit");
            choice= 0;//int
            switch (choice){
                case 1: Prof_Menu();
                    break;
                case 2: Student_Menu();
                    break;
                case 3: return;
                default: System.out.println("Invalid choice!!");
            }
        }
    }

    public static void main(String[] args) {
        Backpack bp1=new Backpack();
        bp1.Login();
    }

}

class course{
    
    private Map<Integer,Professor> prof;
    private Map<Integer,Student> student;
    private lectures lecture;
    private Assessment assessment;
    private Discussion_Forum comments;
    course(int no_of_profs, int no_of_students){
        prof=new HashMap<>();
        student=new HashMap<>();
        lecture=new lectures();
        assessment=new Assessment(no_of_students);
        comments=new Discussion_Forum();
        int i;
        for(i=0;i<no_of_profs;i++){
            prof.put(i, new Professor(i));
        }
        for(i=0;i<no_of_students;i++){
            prof.put(i, new Student(i));
        }
        
    }

    Professor getProf(int ID){
        return prof.get(ID);
    }

    Student getStud(int ID){
        return student.get(ID);
    }

    void add_lectures(Professor prof){
        prof.add_lectures(lecture);
    }

    void add_assessments(Professor prof){
        prof.add_assessments(assessment);
    }

    void view_lectures(User use){
        use.view_lectures(lecture);
    }

    void view_assessment(User use){
        use.view_assessment(assessment);
    }

    
    
}

class Professor implements User{
    private int ID;
    private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    Professor(int ID){
        this.ID=ID;
    }
    @Override
    public String toString() {
        return ("S"+ID);
    }
    
    void add_lectures(lectures lecture){
        System.out.println("1. Add Lecture Slide \n2. Add Lecture Video");
        int choice=Integer.parseInt(br.readLine());
        String lec[];
        int n,i;
        if(choice==1){
            System.out.println("Enter number of Slides");
            n=Integer.parseInt(br.readLine());
            System.out.println("Enter Topic of Slides");
            lec=new String[n+2];
            lec[0]=br.readLine();
            System.out.println("Enter content of Slides");
            for(i=1;i<=n;i++){
                System.out.println("Content of Slide "+i+": ");
                lec[i]=br.readLine();
            }
            lec[i]=this;
            lecture.add_Slide(lec);
        }
        else if(choice==2){
            lec=new String[3];
            System.out.println("Enter Topic of Video: ");
            lec[0]=br.readLine();
            System.out.println("Enter Filename of Video: ");
            lec[1]=br.readLine();
            if(lec[1].endsWith(".mp4")){
                lec[2]=this;
                lecture.add_Video(lec);
            }
            else{
                System.out.println("File format should be .mp4");
                return;
            }
        }
        else{
            System.out.println("Invalid Choice!!");
            return;
        }
    }

    void add_assessments(Assessment assessment){
        System.out.println("1. Add Assignment \n2. Add Quiz");
        int choice=Integer.parseInt(br.readLine());
        if(choice==1){
            System.out.println("Enter problem statement: ");
            String prob=br.readLine();
            System.out.println("Enter Maximum Marks");
            int mm=Integer.parseInt(br.readLine());
            assessment.add_Assignment(prob, mm, "Assignment");
        }
        else if(choice==2){
            System.out.println("Enter quiz question: ");
            String prob=br.readLine();
            assessment.add_Assignment(prob, 1, "Quiz");
        }
        else{
            System.out.println("Invalid Choice!!");
            return;
        }
    }

    void grade_assessment(Assessment assessment){
        assessment.grade_Assignment();
    }

    void close_assessment(Assessment assessment){
        assessment.close_Assignmenet();
    }
    
    @Override
    public void add_comments(Discussion_Forum comments){
        String com[]=new com[2];
        System.out.println("Enter Comment");
        com[0]=br.readLine();
        com[1]=this;
        comments.add_Comment(com);
    }
}

class Student implements User{

    private int ID;
    Student(int ID){
        this.ID=ID;
    }
    void submit_assessments(Assessment assessment){
        assessment.submit_Assignment(this.ID);
    }

    @Override
    public void add_comments(Discussion_Forum comments){
        String com[]=new com[2];
        System.out.println("Enter Comment");
        com[0]=br.readLine();
        com[1]=this;
        comments.add_Comment(com);
    }
}


interface User{

    default void view_lectures(lectures lec){
        lec.view_lectures();
    }
    default void view_assessment(Assessment assess){
        assess.view_assessment();
    }
    default void view_comments(Discussion_Forum comments){
        comments.view_Comments();
    }
    void add_comments(Discussion_Forum comments);
    void assessment_grades();
    
}

class lectures{
    private Map<Date,String[]> videos;  //[Title FileName Uploadedby] 
    private Map<Date,String[]> slides;  //[Title Slides Uploadedby]

    lectures(){
        slides=new HashMap<>();
        videos=new HashMap<>();
    }

    void add_Slide(String arr[]){
        slides.put(new Date(),arr);
    }

    void add_Video(String vid[]){
        videos.put(new Date(),vid);
    }

    void view_lectures(){
        int i;
        System.out.println("Slides: \n");
        for(Date d : slides.keySet()){
            String arr[]=slides.get(d);
            System.out.println("Title: "+arr[0]);
            for(i=1;i<arr.length-1;i++){
                System.out.println("Slide "+i+": "+arr[i]);
            }
            System.out.println("Date of Upload: "+d);
            System.out.println("Uploaded By: "+arr[i]+"\n");
        }

        System.out.println("Videos: \n");
        for(Date d : videos.keySet()){
            String arr[]=videos.get(d);
            System.out.println("Title of Video: "+arr[0]);
            System.out.println("Video file: "+arr[1]);
            System.out.println("Date of Upload: "+d);
            System.out.println("Uploaded By: "+arr[2]+"\n");
        }
    }

}

class Assessment{

    private Map<Integer,String[]> Assignment;   //[problem maxmarks]
    private Map<Integer,Map<Integer,String>> Submission;
    private Map<Integer,Map<Integer,int[]>> Grades;
    private Map<Integer,String> type;
    private Map<Integer,Boolean> status_closed;    //True if assignment closed
    private static int assgn_id=0;
    private int no_of_students;
    private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    
    Assessment(int n){
        Assignment=new HashMap<>();
    }

    void add_Assignment(String problem, int mm, String type){
        Assignment.put(assgn_id,new String[]{problem,String.valueOf(mm)});
        Submission.put(assgn_id, new HashMap<>());
        Grades.put(assgn_id, new HashMap<>());
        type.put(assgn_id,type);
        status_closed.put(assgn_id,false);
        assgn_id+=1;
    }
    
    void view_assessment(){
        System.out.println("List of Asssigments: \n");
        for(int ass_id : Assignment.keySet()){
            System.out.print("ID: "+ass_id);
            System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
            System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
            System.out.println("----------------");
        }
        
    }

    int view_pending(){
        int flag=0;
        for(int ass_id : Assignment.keySet()){
            if(isClosed(ass_id))
                continue;
            if(Submission.get(ass_id)==null){
                System.out.print("ID: "+ass_id);
                System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
                System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
                System.out.println("----------------");
                flag=1;
            }
        }
        return flag;
    }

    int view_ungraded(){
        int flag=0;
        for(int ass_id : Submission.keySet()){
            if(isClosed(ass_id))
                continue;
            if(Submission.get(ass_id).size()==no_of_students)
                continue;
            else{
                System.out.print("ID: "+ass_id);
                System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
                System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
                System.out.println("----------------");
                flag=1;
            }
        }
        return flag;
    }

    boolean isClosed(int ID){
        return status_closed.get(ID);
    }

    void submit_Assignment(int stu_id){
        System.out.println("Pending Assessments: ");
        int flag=0;
        for(int ass_id : Submission.keySet()){
            if(isClosed(ass_id))
                continue;
            if(Submission.get(ass_id).get(stu_id)==null){
                System.out.print("ID: "+ass_id);
                System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
                System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
                System.out.println("----------------");
                flag=1;
            }
            else
                continue;
        }
        if(flag==0){
            System.out.println("No pending assessments! Enjoyy");
            return;
        }
        System.out.println("Enter ID of Assessment: ");
        int assess_id=Integer.parseInt(br.readLine());
        if(type.get(assess_id).equalsIgnoreCase("Assignment")){
            System.out.println("Enter filename of assignment: ");
            String ans=br.readLine();
            if(ans.endsWith(".zip")){
                Submission.get(assess_id).put(stu_id,ans);
            }
            else{
                System.out.println("Assignment must be submitted in .zip format");
                return;
            }
        }
        else{
            System.out.println(Assignment.get(assess_id)[0]);
            String ans=br.readLine();
            Submission.get(assess_id).put(stu_id,ans);
        }
    }

    boolean graded(int assgn_ID,int stuID){
        if(Grades.get(assgn_ID).get(stuID)==null)
            return false;
        else
            return true;
    }
    void grade_Assignment(){
        int flag=0;
        for(int ass_id : Submission.keySet()){
            if(isClosed(ass_id))
                continue;
            if(Grades.get(ass_id).size()==no_of_students)
                continue;
            else{
                System.out.print("ID: "+ass_id);
                System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
                System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
                System.out.println("----------------");
                flag=1;
            }
        }
        if(flag==0)
            return;
        int assess_id=Integer.parseInt(br.readLine());
        System.out.println("Choose ID from these ungraded submissions");
        for(int stu_id : Submission.get(assess_id).keySet()){
            if(graded(assess_id,stu_id))
                continue;
            else{
                System.out.println(stu_id+"--> S"+stu_id);
            }
        }
        int stu_id=Integer.parseInt(br.readLine());
        System.out.println("Submissions: ");
        System.out.println("Submission: "+Submission.get(assess_id).get(stu_id)+"\n-------------------------");
        System.out.println("Max Marks: "+Assignment.get(assess_id)[1]);
        System.out.println("Marks scored: ");
        int marks=Integer.parseInt(br.readLine());
        Grades.get(assess_id).put(stu_id,marks);
    }

    void close_Assignmenet(){
        System.out.println("List of OPEN Asssigments: \n");
        int flag=0;
        for(int ass_id : Assignment.keySet()){
            if(isClosed(ass_id))
                continue;
            System.out.print("ID: "+ass_id);
            System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
            System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
            System.out.println("----------------");
            flag=1;
        }
        if(flag==0){
            System.out.println("No open assignments");
            return;
        }
        System.out.println("Enter ID of assignment to close: ");
        int assess_id=Integer.parseInt(br.readLine());
        status_closed.put(assess_id, true);
    }

}

class Discussion_Forum{
    private Map<Date,String[]> comment;  //[Comment Uploadedby] 
    
    Discussion_Forum(){
        comment=new HashMap<>();
    }

    void add_Comment(String arr[]){
        slides.put(new Date(),arr);
    }

    void view_Comments(){
        System.out.println("Comments: \n");
        for(Date d : comment.keySet()){
            String arr[]=comment.get(d);
            System.out.print(arr[0]+" --"+arr[1]);
            System.out.println("Date of Upload: "+d);
        }
    }
}

