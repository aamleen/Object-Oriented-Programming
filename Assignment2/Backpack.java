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
    private Map<Integer,String[]> Quiz;
    private Map<Integer,Map<Integer,String>> Quiz_submission;
    private Map<Integer,Map<Integer,String>> Assignment_submission;
    private Map<Integer,Map<Integer,Integer>> Assignment_grades;
    private Map<Integer,Map<Integer,Integer>> Quiz_grades;
    private static int assgn_id=0;
    Assessment(){
        Assignment=new HashMap<>();
        Quiz=new HashMap<>();
        
    }

    void add_Assignment(String problem, int mm){
        Assignment.put(assgn_id,new String[]{problem,String.valueOf(mm)});
        Assignment_submission.put(assgn_id, new HashMap<>());
        Assignment_grades.put(assgn_id, new HashMap<>());
        assgn_id+=1;
    }
    void add_Quiz(String problem){
        Quiz.put(assgn_id,new String[]{problem,"1"});
        Quiz_submission.put(assgn_id, new HashMap<>());
        Quiz_grades.put(assgn_id, new HashMap<>());
        assgn_id+=1;
    }

    void view_assessment(){
        System.out.println("List of open Asssigments: \n");
        for(int ass_id : Assignment.keySet()){
            System.out.print("ID: "+ass_id);
            System.out.print(" Assignment: "+Assignment.get(ass_id)[0]);
            System.out.print(" Max Marks: "+Assignment.get(ass_id)[1]);
            System.out.println("----------------");
        }
        System.out.println("List of open Quizzes: \n");
        for(int ass_id : Quiz.keySet()){
            System.out.print("ID: "+ass_id);
            System.out.print(" Assignment: "+Quiz.get(ass_id)[0]);
            System.out.println(" Max Marks: "+Quiz.get(ass_id)[1]);
            System.out.println("--------------------");
        }
    }

    void submit_Assignment(int choice, int stu_id, String answer){
        Assignment_submission.get(choice).get(stu_id);
    }

}

class Discussion_Forum{

}

class Professor{
}

class Student{

}
