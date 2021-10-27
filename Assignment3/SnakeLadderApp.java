import java.util.*;

abstract public class SnakeLadderApp {

    static Scanner sc=new Scanner(System.in);
    protected Game game;
    protected Dice dice;

    String name_input(){
        System.out.println("Enter name: ");
        String name=sc.nextLine();
        if(name.length()==0){
            System.out.println("Enter name again correctly");
            name=sc.nextLine();
        }
        return name;
    }
    
    abstract void play();   
    abstract void view_scores();
    public static void main(String[] args) {
        System.out.println("Enter 1 for single player or 2 for multiplayer");
        int choice=sc.nextInt();
        SnakeLadderApp mode;
        if(choice==1){
            sc.nextLine();
            mode=new SinglePlayer();
        }
        else{
            System.out.println("Enter no. of players: ");
            int n=sc.nextInt();
            sc.nextLine();
            if(n==1)
                mode=new SinglePlayer();
            mode=new MultiPlayer(n);
        }
        mode.play();
        System.out.println("Viewing Final Scores: ");
        mode.view_scores();
    }
    
}

class SinglePlayer extends SnakeLadderApp{     //sets up the game and takes user input then makes the player roll the dice and play
    private Player player;
    
    SinglePlayer(){
        game=new Game();
        player=new Player(name_input());
        dice=new Dice(2);
        System.out.println("The game setup is ready");
    }

    @Override
    void play(){
        while(player.getPosition()<13){
            System.out.println("Hit enter to roll the dice");
            String s=SnakeLadderApp.sc.nextLine();
            if(s.length()!=0)
                continue;
            player.play(dice, game);
        }
        System.out.println("Game over for "+player);
        System.out.println("---------------------------------");
        
    }

    @Override
    void view_scores() {
        System.out.println(player+" accumulated "+player.getScore()+" points.");
        
    }
}

class MultiPlayer extends SnakeLadderApp{
    private Player[] players;
    
    MultiPlayer(int no_of_players){
        game=new Game();
        dice=new Dice(2);
        players=new Player[no_of_players];
        for(int i=0;i<players.length;i++){
            players[i]=new Player(name_input());
        }
    }
    
    @Override
    void play(){
        int players_finished=0;
        while(players_finished<players.length){
            for(int i=0;i<players.length;i++){
                String s="abcd"; //random initialization
                if(players[i].getPosition()==13)
                    continue;
                System.out.println("\n----------------------------------------------");
                System.out.print(players[i]+"\'s Turn:");
                while(s.length()!=0){
                    System.out.println("Hit enter to roll the dice or 1 to view score");
                    s=SnakeLadderApp.sc.nextLine();
                    if(s.length()>0 && s.charAt(0)=='1')
                    {System.out.println("\n------------------------------------------------");
                     System.out.println("Viewing Scores:");
                     System.out.println("\n------------------------------------------------");
                     view_scores();
                    }
                    if(s.length()!=0)
                        System.out.println("Dice was not rolled. Try again");
                }
                players[i].play(dice, game);
                if(players[i].getPosition()==13){
                    System.out.println("Game over for "+players[i]);
                    System.out.println(players[i]+" accumulated "+players[i].getScore()+" points.");
                    players_finished+=1;
                }
                System.out.println("\n----------------------------------------------");
            }
        }
        System.out.println("\n------------------------------------------------");
    }
    
    @Override
    void view_scores(){
        Map<Integer,ArrayList<Integer>> player_scores=new HashMap<>();
        for(int i=0;i<players.length;i++){
            if(player_scores.get(players[i].getScore())==null)
            {
                player_scores.put(players[i].getScore(), new ArrayList<>());
            }
            player_scores.get(players[i].getScore()).add(i);
        }
        TreeMap<Integer,ArrayList<Integer>> sorted_scores=new TreeMap<>(Collections.reverseOrder());
        sorted_scores.putAll(player_scores);
        int i=1;
        for(Map.Entry<Integer, ArrayList<Integer>> entry : sorted_scores.entrySet()){
            System.out.print(i+"--> Score: "+entry.getKey()+"     : ");
            for(int id : entry.getValue()){
                System.out.print(players[id]+", ");
            }
            System.out.println();
            i+=1;
        }
        System.out.println("------------------------------------------");
    }
}

class Game{ //sets up the game architecture
    private Floors floor[];
    Game(){
        floor=new Floors[14];
        floor[2]=new Elevator(2,10);
        floor[5]=new Snake(5,1);
        floor[8]=new Ladder(8,12);
        floor[11]=new Cobra(11,3);
        for(int i=0;i<14;i++){
            if(floor[i]!=null)
                continue;
            floor[i]=new Empty(i);
        }
    }
    void move(Player p,int dice_no){
        if(p.getPosition()==-1 && dice_no==2){
            System.out.println("Game cannot start unless you get 1");
            return;
        }
        if(p.getPosition()==12 && dice_no==2){
            System.out.println("Player cannot move");    
            return;
        }
        if(p.getPosition()==13){
            return;
        }
        int new_position=p.getPosition()+dice_no;
        int next_position=floor[new_position].jumpOnFloor(p);
        if(next_position==new_position)
            return;
        floor[next_position].jumpOnFloor(p);
    }
   
}

class Dice {
    private final int numFaces; //maximum face value
    // Constructor: Sets the initial face value.
    private Random rand;
    public Dice(int _numFaces) {
    numFaces = _numFaces;
    rand=new Random();
    }
    // Rolls the dice
    public int roll() {
    return 1 + rand.nextInt(numFaces);
    }
}

class Player{
    private String name;
    private int score;
    private int position;
    Player(String name){
        this.name=name;
        score=0;
        position=-1;
    }
    public String toString() {
        return name;
    }
    int getScore(){
        return score;
    }
    int getPosition(){
        return position;
    }
    void setScore(int score){
        this.score=score;
    }
    void  setPosition(int position){
        this.position=position;
    }
    void play(Dice d, Game g){
        int dice_no=d.roll();
        System.out.println("Dice gave "+dice_no);
        g.move(this,dice_no);
    }
}

abstract class Floors{
    
    protected int location;
    Floors(int location){
        this.location=location;
    }
    
    int jumpOnFloor(Player p){
        p.setPosition(location);
        setScore(p);
        System.out.println("Player position: Floor "+location);
        System.out.println(p+" has reached a "+this+" floor");
        System.out.println("Total Points: "+p.getScore());
        return getNextLocation();
    }
    abstract void setScore(Player p);
    abstract int getNextLocation();
}

class Empty extends Floors{

    Empty(int location){
        super(location);
        this.location=location;
    }
    @Override
    public String toString() {
        return "Empty";
    }

    @Override
    void setScore(Player p) {
        p.setScore(p.getScore()+1);
    }

    @Override
    int getNextLocation() {
        return location;
    }
    
}

class Snake extends Floors{

    protected int score_deduction;
    protected int next_location;
    Snake(int location, int next_location){
        super(location);
        score_deduction=2;
        this.next_location=next_location;
    }
    @Override
    public String toString() {
        return "Normal Snake";
    }

    @Override
    void setScore(Player p) {
        p.setScore(p.getScore()-score_deduction);
    }

    @Override
    int getNextLocation(){
        return next_location;
    }
}

class Ladder extends Floors{

    protected int score_addition;
    protected int next_location;
    Ladder(int location,int next_location){
        super(location);
        score_addition=2;
        this.next_location=next_location;
    }
    @Override
    public String toString() {
        return "Normal Ladder";
    }

    void setScore(Player p) {
        p.setScore(p.getScore()+score_addition);
    }

    @Override
    int getNextLocation(){
        return next_location;
    }
}

class Elevator extends Ladder{

    Elevator(int location,int next_location){
        super(location,next_location);
        setScore_addition();
    }
    @Override
    public String toString() {
        return "Elevator";
    }

    void setScore_addition() {
        score_addition = 4;
    }
}

class Cobra extends Snake{

    Cobra(int location,int next_location){
        super(location,next_location);
        set_score_Deduction();
    }

    @Override
    public String toString() {
        return "Cobra";
    }

    void set_score_Deduction(){
        score_deduction=4;
    }
}
