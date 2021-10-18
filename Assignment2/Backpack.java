import java.util.*;

public class Backpack {
    
}

class course{
    
}

interface commmon_User{

    default void view_lectures(lectures l){
        l.view_lectures();
    }
    void view_assessment();
    void view_comments();
    void add_comments();
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

    private Map<Integer,String[]> Assignment;
    private Map<Integer,Map<Integer,String>> Submission;
    private Map<Integer,Map<Integer,Integer>> Grades;
    private Map<Integer,String> type;
    private Map<Integer,Boolean> status_closed;    //True if assignment closed
    private static int assgn_id=0;
    private int no_of_students;
    Assessment(int n){
        Assignment=new HashMap<>();
    }

    void add_Assignment(String problem, int mm, String type){
        Assignment.put(assgn_id,new String[]{problem,String.valueOf(mm)});
        Submission.put(assgn_id, new HashMap<>());
        Grades.put(assgn_id, new HashMap<>());
        type.put(assgn_id,type);
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

    int pending_Evaluation(){
        int flag=0;
        for(int ass_id : Assignment.keySet()){
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

    void submit_Assignment(int choice, int stu_id, String answer){
        Submission.get(choice).put(stu_id,answer);

    }

    void grade_Assignment(int choice, int stu_id){
        int marks=0;
        Grades.get(choice).put(stu_id,marks);
    }

    void close_Assignmenet(){
        System.out.println("List of OPEN Asssigments: \n");
        int flag=0
        for(int ass_id : Assignment.keySet()){
            if(isClosed(ass_id))
                continue;
            System.out.print("ID: "+ass_id);
            System.out.print(type.get(ass_id)+": "+Assignment.get(ass_id)[0]);
            System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
            System.out.println("----------------");
            flag=1;
        }
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

class Professor implements commmon_User{

    
}

class Student implements commmon_User{

}
