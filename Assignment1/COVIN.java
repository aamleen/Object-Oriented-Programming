import java.util.*;

public class COVIN {

    HashMap<Integer,Hospital> hospital_data;
    HashMap<Long,Citizen> citizen_data;
    Vaccine vaccine;
    Slots slot;
    Map<Integer,ArrayList<Integer>> pincode_hospital;
    Map<String,ArrayList<Integer>> vaccine_hospital;
    
    //HashMap<Hospital,Integer> hospital_data=new HashMap<>();
    //ArrayList<Map<String,int[]>>
    COVIN(){
        hospital_data=new HashMap<>();
        citizen_data=new HashMap<>();
        pincode_hospital= new HashMap<>();
        vaccine_hospital= new HashMap<>();
        vaccine=new Vaccine();
    }
    Scanner sc=new Scanner(System.in);
    
    void addVaccine(){
        System.out.println("Vaccine Name: ");
        String name=sc.next();
        System.out.println("Number of doses: ");
        int doses=sc.nextInt();
        System.out.println("Gap between Doses: ");
        int day_gap=sc.nextInt();
        if(doses<0 || day_gap<0)
        {    System.out.println("Values should be +ve");
            return;
        }
        vaccine.addVaccine(name, doses, day_gap);
        vaccine_hospital.put(name,new ArrayList<>());
    }

    void registerHospital(){
        System.out.println("Hospital Name: ");
        String name=sc.nextLine();
        System.out.println("Pincode: ");
        int pincode=sc.nextInt();
        Hospital h= new Hospital(name,pincode);
        if(pincode_hospital.get(pincode)==null)
            pincode_hospital.put(pincode,new ArrayList<>());
        if(pincode_hospital.get(pincode).indexOf(h.UID)==-1)
            pincode_hospital.get(pincode).add(h.UID);
        hospital_data.put(h.UID,h);
    }

    void registerCitizen(){
        System.out.println("Citizen Name: ");
        String name=sc.nextLine();
        System.out.println("Age: ");
        int age=sc.nextInt();
        System.out.println("Unique ID: ");
        String UID=sc.next();
        if(UID.length()!=12)
        {
            System.out.println("Invalid UID given, should be 12 digits");
            return;
        }
        Citizen c= new Citizen(name, Long.parseLong(UID), age);
        if(age<18){
            System.out.println("Only above 18 are allowd");
            return;
        }
        citizen_data.put(c.UID,c);
    }
  
    void addSlot(){
        System.out.println("Enter Hospital ID: ");
        String ID=sc.next();
        if(ID.length()!=6){
            System.out.println("Invalid UID given, should be 6 digits");
            return;
        }
        int UID=Integer.parseInt(ID);
        if(hospital_data.get(UID)==null){
            System.out.println("Hospital Not Registered yet");
            return;
        }
        System.out.println("Enter number of slots to be added");
        int nslots=sc.nextInt();
        for(int i=0;i<nslots;i++){
            System.out.println("Enter Day Number");
            int day=sc.nextInt();
            System.out.println("Enter Quantity");
            int quantity=sc.nextInt();
            System.out.println("Select vaccine: ");
            vaccine.displayChoice();
            int choice=sc.nextInt();
            String vac_name=vaccine.names.get(choice);
            hospital_data.get(UID).addSlots(slot, day, quantity,Vaccine.index,choice);
            if(vaccine_hospital.get(vac_name).indexOf(UID)==-1)
                vaccine_hospital.get(vac_name).add(UID);
        }
        

    }

    void Menu(){
        int choice=0;
        while(choice!=8){
            System.out.println("1. Add Vaccines \n2. Register Hospital \n3. Register Citizen \n4. Add Slot for Vaccination"); 
            System.out.println("5. Book Slot for Vaccination \n6. List all slots for a hospital \n7. Check Vaccination Status \n8. Exit");
            choice=sc.nextInt();
            switch(choice){
                case 1: addVaccine();

            }

        }
    }

    public static void main(String[] args) {
        System.out.println("\tCoWin Portal Initialized");
        System.out.println("---------------------------------------");
        

    }
    
}

class Hospital{

    static int ID=100000;
    String name;
    int UID;
    int pincode;

    Hospital(String name, int pincode)
    {
        this.name=name;
        this.UID=Hospital.ID;
        this.pincode=pincode;
        Hospital.ID++;
    }
    void addSlots(Slots add,int Day, int quantity,int all_vaccines,int vaccine_index){
        add.addslots(UID,Day,quantity,all_vaccines,vaccine_index);


    }
}
class Citizen{
    String name;
    int age;
    long UID;
    int doses;
    Citizen(String name, long UID, int age){
        this.age=age;
        this.name=name;
        this.UID=UID;
        this.doses=0;
    }

    boolean fullyVaccinated(int total_dose){
        if(doses==total_dose)
            return true;
        else
            return false;
    }



}

class Slots{
    Map< Integer,ArrayList<Map<Integer,Integer>> > hospital= new HashMap<>();
    
    void createSlots(int size,int UID){
        ArrayList<Map<Integer,Integer>> hospital_slot= new ArrayList<Map<Integer,Integer> >();
        for(int i=0;i<size;i++)
        {
            hospital_slot.add(new HashMap<>());
        }
        hospital.put(UID, hospital_slot);
    }
    void addslots(int UID,Integer Day, int no_of_slots, int total_vaccines,int vaccine_index){
        ArrayList<Map<Integer,Integer>> hospital_slot= hospital.get(UID);
        if(hospital_slot==null){
            createSlots(total_vaccines, UID);
        }
        hospital_slot=hospital.get(UID);
        Map<Integer,Integer> day_slot = hospital_slot.get(vaccine_index);
        if (day_slot.get(Day)==null){
            day_slot.put(Day,no_of_slots);
        }
        else{
            day_slot.put(Day,day_slot.get(Day)+no_of_slots);
        }
    }
    int BookSlot(Citizen c, Integer Day, int vaccine_index,int UID){
        if(hospital.get(UID)==null){
            return 0;
        }
        Map<Integer,Integer> day_slot=hospital.get(UID).get(vaccine_index);
        if(day_slot.get(Day)==null || day_slot.get(Day)<0)
            return 0;
        else{
            day_slot.put(Day,day_slot.get(Day)-1);
            return 1;
        }
    }
}

class Vaccine{
    Map<String, int[]> vaccine_details;
    static int index=0;
    ArrayList<String> names;

    Vaccine(){
        vaccine_details=new Hashtable();
        names=new ArrayList<>();
    }
    
    void addVaccine(String name, int dose, int gap){
        vaccine_details.put(name,new int[]{dose,gap,index});     //An array of [dose,gap] will be mapped to the vaccine name
        names.add(name);
    }
    int get_gap(String name){
        if(vaccine_details.get(name)==null)
            return -1;
        return vaccine_details.get(name)[1];
    }
    void displayChoice(){
        for(int i=0;i<names.size();i++){
            System.out.println(i+"-> "+names.get(i));
        }
    }
}
