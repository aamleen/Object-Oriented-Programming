import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class COVIN {

    HashMap<Integer,Hospital> hospital_data;
    HashMap<Long,Citizen> citizen_data;
    HashMap<String,Vaccine> vaccine_data;
    Map<Integer,ArrayList<Integer>> pincode_hospital;
    Map<String,ArrayList<Integer>> vaccine_hospital;
    Slots slot;
    ArrayList<String> vax_names;
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    COVIN(){
        hospital_data=new HashMap<>();
        citizen_data=new HashMap<>();
        pincode_hospital= new HashMap<>();
        vaccine_hospital= new HashMap<>();
        vaccine_data=new HashMap<>();
        vax_names=new ArrayList<>();
        slot=new Slots();
        
    }
    
    void addVaccine()throws IOException{
        System.out.println("Vaccine Name: ");
        String name=br.readLine();
        System.out.println("Number of doses: ");
        int doses=Integer.parseInt(br.readLine());
        int day_gap=0;
        if(doses>1){
            System.out.println("Gap between Doses: ");
            day_gap=Integer.parseInt(br.readLine());
        }
        if(doses<0 || day_gap<0)
        {    System.out.println("Values should be +ve");
            return;
        }
        Vaccine vax=new Vaccine(name, doses, day_gap);
        vaccine_data.put(name,vax);
        vax_names.add(name);
        vaccine_hospital.put(name,new ArrayList<>());
        vax.DisplayDetails();
    }

    void registerHospital()throws IOException{
        System.out.println("Hospital Name: ");
        String name=br.readLine();
        System.out.println("Pincode: ");
        int pincode=Integer.parseInt(br.readLine());
        Hospital h= new Hospital(name,pincode,vax_names.size());
        if(pincode_hospital.get(pincode)==null)
            pincode_hospital.put(pincode,new ArrayList<>());
        if(pincode_hospital.get(pincode).indexOf(h.UID)==-1)
            pincode_hospital.get(pincode).add(h.UID);
        hospital_data.put(h.UID,h);
        h.displayHospital();
    }

    void displayChoice()throws IOException{
        for(int i=0;i<vax_names.size();i++){
            System.out.println(i+"-> "+vax_names.get(i));
        }
    }

    void registerCitizen()throws IOException{
        System.out.println("Citizen Name: ");
        String name=br.readLine();
        System.out.println("Age: ");
        int age=Integer.parseInt(br.readLine());
        System.out.println("Unique ID: ");
        String UID=br.readLine();
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
  
    void addSlot()throws IOException{
        System.out.println("Enter Hospital ID: ");
        String ID=br.readLine();
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
        int nslots=Integer.parseInt(br.readLine());
        for(int i=0;i<nslots;i++){
            System.out.println("Enter Day Number");
            int day=Integer.parseInt(br.readLine());
            System.out.println("Enter Quantity");
            int quantity=Integer.parseInt(br.readLine());
            System.out.println("Select vaccine: ");
            displayChoice();
            int choice=Integer.parseInt(br.readLine());
            String vac_name=vax_names.get(choice);
            hospital_data.get(UID).addSlots(slot, day, quantity,vax_names.size(),choice);
            if(vaccine_hospital.get(vac_name).indexOf(UID)==-1)
                vaccine_hospital.get(vac_name).add(UID);
        }
        

    }

    void bookSlot()throws IOException{
        System.out.println("Enter patient UID: ");
        long UID=sc.nextLong();  br.readLine();
        if(citizen_data.get(UID)==null)
        {
            System.out.println("Not registered yet!!");
            return;
        }
        Citizen patient=citizen_data.get(UID);
        if(patient.vaccine!="" && patient.fullyVaccinated(vaccine_data.get(patient.vaccine).doses)){
            System.out.println("Patient is FULLY VACCINATED");
            return;
        }
        System.out.println("1. Search By Area \n2. Search By Vaccine \n3. Exit");
        System.out.println("Enter Choice:");
        int choice=Integer.parseInt(br.readLine());
        switch(choice){
            case 1: System.out.println("Enter Pincode: ");
                    int pincode=Integer.parseInt(br.readLine());
                    ArrayList<Integer> hosp_pincode=pincode_hospital.get(pincode);
                    if(hosp_pincode==null){
                        System.out.println("Invalid Pincode");
                        return;
                    }
                    for(int i=0;i<hosp_pincode.size();i++){
                        int hosp_UID=hosp_pincode.get(i);
                        if(hospital_data.get(hosp_UID).totalSlots(0)<=0){
                            hosp_pincode.remove(i);
                            continue;
                        }
                        System.out.println(hosp_UID+" -> "+hospital_data.get(hosp_UID).name);
                    }
                    System.out.println("Enter Hospital ID:");
                    int choice_hosp=Integer.parseInt(br.readLine());
                    int flag=slot.displaySlots(hospital_data.get(choice_hosp), patient.next_dose,vax_names);
                    if(flag==0)
                        return;
                    System.out.println("Choose Slot: ");
                    int booking_slot=Integer.parseInt(br.readLine());
                    int booking_day=booking_slot%10;
                    int vaccine_index=booking_slot/10;
                    flag=citizen_data.get(UID).bookSlot(slot, hospital_data.get(choice_hosp), vaccine_data.get(vax_names.get(vaccine_index)), booking_day, vaccine_index,);
                    if(flag==0)
                        return;
                    System.out.println(citizen_data.get(UID).name+" vaccinated with "+vax_names.get(vaccine_index));
                break;
            case 2: System.out.println("Enter vaccine name:");
                    String vaccine_name=br.readLine();
                    ArrayList<Integer> hosp_vaccine=vaccine_hospital.get(vaccine_name);
                    if(hosp_vaccine==null)
                        return;
                    for(int i=0;i<hosp_vaccine.size();i++){
                        int hosp_UID=hosp_vaccine.get(i);
                        if(hospital_data.get(hosp_UID).totalSlots(0)<=0){
                            hosp_vaccine.remove(i);
                            continue;
                        }
                        System.out.println(hosp_UID+" -> "+hospital_data.get(hosp_UID).name);
                    }
                    System.out.println("Enter Hospital ID:");
                    int choice_hosp1=Integer.parseInt(br.readLine());
                    int vaccine_index1=vax_names.indexOf(vaccine_name);
                    int flag1=slot.display(hospital_data.get(choice_hosp1).day_wise_slots.get(vaccine_index1),0,patient.next_dose,vaccine_name);
                    if(flag1==0){
                        System.out.println("No slots available");
                        return;
                    }
                    System.out.println("Choose Slot: ");
                    int booking_day1=Integer.parseInt(br.readLine());
                    flag1=citizen_data.get(UID).bookSlot(slot, hospital_data.get(choice_hosp1), vaccine_data.get(vaccine_name), booking_day1, vaccine_index1);
                    if(flag1==0)
                        return;
                    System.out.println(citizen_data.get(UID).name+" vaccinated with "+vax_names.get(vaccine_index1));
                    break;
            case 3:
                    return;
            default: System.out.println("Invalid Choice");
                    return;
        }
    }

    void displayHospital_slots()throws IOException{
        System.out.println("Enter Hospital ID: ");
        int hosp_ID=Integer.parseInt(br.readLine());
        slot.displaySlots(hospital_data.get(hosp_ID), 0, vax_names);
    }

    void vaccination_status()throws IOException{
        System.out.println("Enter Patient ID: ");
        Long cit_UID=sc.nextLong();  br.readLine();
        String vaccine_name=citizen_data.get(cit_UID).vaccine;
        if(vaccine_name==""){
            System.out.println("Citizen REGISTERED");
            return;
        }
        if (citizen_data.get(cit_UID).fullyVaccinated(vaccine_data.get(vaccine_name).doses)){
            System.out.println("FULLY VACCINATED");
            System.out.println("Vaccine Given: "+vaccine_name);
            System.out.println("Number of Doses Given: "+citizen_data.get(cit_UID).doses);
            return;
        }
        System.out.println("PARTIALLY VACCINATED");
        System.out.println("Vaccine Given: "+vaccine_name);
        System.out.println("Number of Doses Given: "+citizen_data.get(cit_UID).doses);
        System.out.println("Next Dose Due Date: "+citizen_data.get(cit_UID).next_dose);
    }

    void Menu()throws IOException{
        int choice=0;
        while(choice!=8){
            System.out.println("1. Add Vaccines \n2. Register Hospital \n3. Register Citizen \n4. Add Slot for Vaccination"); 
            System.out.println("5. Book Slot for Vaccination \n6. List all slots for a hospital \n7. Check Vaccination Status \n8. Exit");
            choice=Integer.parseInt(br.readLine());
            switch(choice){
                case 1: addVaccine();
                    break;
                case 2: registerHospital();
                    break;
                case 3: registerCitizen();
                    break;
                case 4: addSlot();
                    break;
                case 5: bookSlot();
                    break;
                case 6: displayHospital_slots();
                    break;
                case 7: vaccination_status();
                    break;
                case 8: break;
                default: System.out.println("Invalid Choice");
                    break;
            }
        }
    }

    public static void main(String[] args)throws IOException {
        COVIN version1=new COVIN();
        System.out.println("\tCoWin Portal Initialized");
        System.out.println("---------------------------------------");
        version1.Menu();
    }
    
}

class Hospital{

    static int ID=100000;
    String name;
    int UID;
    int pincode;
    ArrayList<Integer> available_vaccine;
    ArrayList<Map<Integer,Integer>> day_wise_slots;
    Hospital(String name, int pincode, int total_vaccines)
    {
        this.name=name;
        this.UID=Hospital.ID;
        this.pincode=pincode;
        Hospital.ID++;
        available_vaccine=new ArrayList<>();
        day_wise_slots=new ArrayList<>();
        for(int i=0;i<total_vaccines;i++){
            available_vaccine.add(0);
            day_wise_slots.add(new HashMap<>()); 
        }
        available_vaccine.add(0);
    }

    void displayHospital(){
        System.out.print("Hospital Name: "+name);
        System.out.print("  Pincode: "+pincode);
        System.out.println("  Unique ID: "+UID);
    }
    void addSlots(Slots add,int Day, int quantity,int all_vaccines,int vaccine_index){
        add.addslots(this,Day,quantity,vaccine_index);
        System.out.println("Slots added by Hospital "+UID+" for Day "+Day+" Available Quantity: "+available_vaccine.get(vaccine_index));
    }
    int totalSlots(int index){
        return available_vaccine.get(index);
    }
}
class Citizen{
    String name;
    int age;
    long UID;
    int doses;
    int next_dose;
    String vaccine;
    Citizen(String name, long UID, int age){
        this.age=age;
        this.name=name;
        this.UID=UID;
        this.doses=0;
        vaccine="";
        next_dose=1;
    }

    boolean fullyVaccinated(int total_dose){
        if(doses==total_dose)
            return true;
        else
            return false;
    }

    int bookSlot(Slots s, Hospital h, Vaccine v, int booking_day, int vaccine_index){
        int flag=0;
        if(doses<v.doses){
            flag=s.BookSlot(this, h, booking_day, vaccine_index);
            if(flag==0)
                return flag;
            vaccine=v.name;
            doses+=1;
            next_dose+=v.gap;
        }
        return flag;
    }
}

class Slots{
    
    void addslots(Hospital h, int Day,int no_of_slots,int vaccine_index){
        Map<Integer,Integer> day_slot = h.day_wise_slots.get(vaccine_index);
        if (day_slot.get(Day)==null){
            day_slot.put(Day,no_of_slots);
        }
        else{
            day_slot.put(Day,day_slot.get(Day)+no_of_slots);
        }
        h.available_vaccine.set(0, h.available_vaccine.get(0)+no_of_slots);
        h.available_vaccine.set(vaccine_index+1, h.available_vaccine.get(vaccine_index+1)+no_of_slots);
        h.day_wise_slots.set(vaccine_index, day_slot);
    }
    
    int BookSlot(Citizen c, Hospital h, Integer Day, int vaccine_index){
        if(h.day_wise_slots==null){
            return 0;
        }
        Map<Integer,Integer> day_slot=h.day_wise_slots.get(vaccine_index);
        if(day_slot.get(Day)==null || day_slot.get(Day)<0)
            return 0;
        else{
            day_slot.put(Day,day_slot.get(Day)-1);
            h.available_vaccine.set(0, h.available_vaccine.get(0)-1);
            h.available_vaccine.set(vaccine_index+1, h.available_vaccine.get(vaccine_index+1)-1);
            return 1;
        }
    }

    int displaySlots(Hospital h,int min_day,ArrayList<String> vaccine_name){
        int choice=0;
        int flag=0;
        for(int i=0;i<h.day_wise_slots.size();i++){
            flag+=display(h.day_wise_slots.get(i), choice,min_day, vaccine_name.get(i));
            choice+=10;
        }
        if(flag==0)
            System.out.println("No slots available");
        return flag;
    }
    int display(Map<Integer,Integer> vaccine_slots,int choice, int min_day, String vaccine_name ){
        int flag=0;
        for(int day : vaccine_slots.keySet()){
            if(day<min_day || vaccine_slots.get(day)<=0)
                continue;
            System.out.println(choice+" -> Day"+day+": Available Qty: "+vaccine_slots.get(day)+" Vaccine: "+vaccine_name);
            flag=1;
            choice++;
        }
        return flag;
    }

}

class Vaccine{
    String name;
    int doses;
    int gap;
    
    Vaccine(String name, int doses, int gap){
        this.name=name;
        this.doses=doses;
        this.gap=gap;
    }
    
    int get_gap(){
        
        return gap;
    }
    
    int TotalDose(){
        return doses;
    }

    void DisplayDetails(){
        System.out.print("Vaccine Name: "+name);
        System.out.print("  No. of doses: "+doses);
        System.out.println("  Gap Between Doses: "+gap);
    }

    
}
