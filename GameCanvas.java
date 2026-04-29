import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class GameCanvas extends JPanel {
  enum State { LOGIN, MENU, MAP_SELECT, MAP_HUB, PLAYING, LEADERBOARD, OVERALL_LEADERBOARD, CONTROLS, POINTS, STATISTICS, ACHIEVEMENTS, PROFILE_HUB, FRIENDS_HUB, STORE }


// 1. knapper
JButton playBtn, mapsBtn, overallBtn, statsBtn, achieveBtn, controlsBtn, pointsBtn, backBtn, 
        map1Btn, map2Btn, map3Btn, mapPlayBtn, mapLeadBtn, retryBtn, menuBtn, loginBtn, 
        createAccBtn, personalBtn, allTimeBtn, stOverallBtn, stMap1Btn, stMap2Btn, 
        stMap3Btn, achGameplayBtn, achOneTimeBtn, achSingleBtn, friendsBtn, 
        addFriendBtn, friendsHubBtn, storeBtn;

// 2. leaderboards og venner
static class ScoreEntry {
    String name, mapName; int score;
    ScoreEntry(String n, int s, String m) { this.name = n; this.score = s; this.mapName = m; }
}

ArrayList<ScoreEntry> overallLeaderboard = new ArrayList<>();
ArrayList<ScoreEntry> mapLeaderboard = new ArrayList<>();
ArrayList<ScoreEntry> personalLeaderboard = new ArrayList<>();
ArrayList<ScoreEntry> friendsLeaderboard = new ArrayList<>();

ArrayList<String> myFriends = new ArrayList<>();
ArrayList<String> justNamesOfFriends = new ArrayList<>();
boolean showingFriends = false;
boolean showingPersonal = false;

// 3. stats og coins og AP
int lifetimeCoins = 0;      
int coins = 0;              
int totalAP = 0;            
int newUserLevel = 1;       
int oldUserLevel = 1;       
int careerGms = 0;          
int careerGhosts = 0;       
int careerRounds = 0;       
int careerFruitsTotal = 0;  
int highestNoDeathEver = 0; 
int fastestRound = 9999;
// Daily challenges
String dailyChallengeDesc = "";
String dailyChallengeType = ""; 
int dailyChallengeGoal = 0;
boolean dailyChallengeDone = false;
int tDaily = 0;
// Weekly challenges
String[] weeklyChallengeDescs = new String[5];
int[] weeklyChallengeGoals = new int[5];
boolean[] weeklyChallengeDone = new boolean[5];
int[] weeklyChallengeRewards = {10, 10, 10, 10, 25};
int careerHighScore = 0;

// 5. Spil og bane
int ghostsActiveCount = 1;     
long lastGhostSpawnTime = 0;    
int[][] bane = new int[21][21];
int[][] originalBaneLayout = new int[21][21];
int selectedMap = 0;
String[] mapNames = {"CLASSIC", "OPEN FIELD", "THE SPIRAL"};
ArrayList<Ghost> ghosts = new ArrayList<>();
Timer motor;
State currentState = State.LOGIN;
int gridSize = 40;
double difficulty = 0.3;
String currentUser = "";

// 6. in-game
int score = 0, level = 1, lives = 3;
int pelletsEatenInLevel = 0; 
int sessionGhosts = 0, sessionFruits = 0;
int[] sessionFruitCounts = new int[10];
long sessionStartTime, levelStartTime;
int ghostsEatenInStreak = 0, frightenedTimer = 0;
boolean diedThisLevel = false, gameOver = false;

// 7. pacman movement og skins
int pacX, pacY, directionX, directionY, nextDireX, nextDireY, nextAngle, eatAngle;
int speed = 4, mouthAngle = 300;
private float mouthTimer = 0;
String pacColor = "YELLOW"; 
String ghostSkin = "CLASSIC";
String ownedItems = "YELLOW,CLASSIC"; 
String equippedMap = "DEFAULT";
Color wallColor = new Color(0, 150, 255);

// 8. system og shop og achievements
int storeScrollY = 0;
int statsSelectedAch = 0, achScrollY = 0, statsSelectedTab = 0;
boolean achGhostBuster = false, achMaxedOut = false;
int fruitX = -1, fruitY = -1, fruitTimer = 0, fruitSpawnTimer = 0;
boolean fruitActive = false, fruit1Spawned = false, fruit2Spawned = false;
int[] careerFruits = new int[10];

    int[][][] allMaps = {
        { // Classic 
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, {1,3,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,3,1}, {1,2,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},  {1,2,1,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,1,2,1}, {1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1}, {1,1,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,1,1}, {0,0,0,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,0,0,0}, {1,1,1,1,2,1,0,1,1,1,0,1,1,1,0,1,2,1,1,1,1}, {0,0,0,0,2,0,0,1,0,0,0,0,0,1,0,0,2,0,0,0,0}, {1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1}, {0,0,0,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,0,0,0}, {1,1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1,1}, {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1}, {1,2,1,1,2,1,1,1,1,2,1,2,1,1,1,1,2,1,1,2,1}, {1,2,2,1,2,2,2,2,2,2,0,2,2,2,2,2,2,1,2,2,1}, {1,1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,2,1,1}, {1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1,2,2,2,2,1}, {1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,2,1}, {1,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1}, {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        },
        { // Open Field 
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, {1,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,0,0,0,0,0,0,0,0,0,2,2,2,2,2,1}, {1,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,1}, {1,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,1}, {1,1,1,2,2,0,0,1,1,1,0,1,1,1,0,0,2,2,1,1,1}, {0,0,0,2,2,0,0,1,0,0,0,0,0,1,0,0,2,2,0,0,0}, {1,1,1,2,2,0,0,1,1,1,1,1,1,1,0,0,2,2,1,1,1}, {1,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,1}, {1,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,1}, {1,2,2,2,2,2,0,0,0,0,0,0,0,0,0,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},{1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1}, {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        },
        { // Spiral
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}, {1,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1}, {1,2,1,2,1,1,1,1,1,1,2,1,1,1,1,1,1,2,1,2,1}, {1,2,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,1}, {1,2,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,2,1}, {1,2,1,2,1,2,2,2,2,2,2,2,2,2,2,2,1,2,2,2,1}, {1,2,1,2,1,2,1,1,1,1,2,1,1,1,1,2,1,2,1,1,1}, {0,2,1,2,1,2,1,2,2,2,2,2,2,2,1,2,1,2,1,2,0}, {1,1,1,2,1,2,1,2,1,1,2,1,1,2,1,2,1,2,1,2,1}, {1,2,2,2,1,2,2,2,0,0,0,0,0,2,1,2,2,2,1,2,1}, {1,2,1,1,1,2,1,2,1,1,2,1,1,2,1,2,1,2,1,2,1}, {1,2,1,2,2,2,1,2,2,2,2,2,2,2,1,2,1,2,1,2,1}, {1,2,1,2,1,1,1,2,1,1,1,1,1,2,1,2,1,2,1,2,1}, {1,2,1,2,1,2,2,2,1,2,2,2,2,2,1,2,1,2,1,2,1}, {1,2,1,2,1,2,1,1,1,2,1,1,1,1,1,2,1,2,1,2,1}, {1,2,1,2,1,2,2,2,2,2,2,2,2,2,2,2,1,2,1,2,1}, {1,2,1,2,1,1,1,1,1,1,2,1,1,1,1,1,1,2,1,2,1}, {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1}, {1,2,1,1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,2,1}, {1,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,3,1}, {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        }
    };

    public GameCanvas() {
                // initialisere spøgelselet arrayet herinde, så det er klar til at blive brugt i starten af spillet
                if (ghosts.isEmpty()) {
                    ghosts.add(new Ghost(400, 320, Color.RED, speed, 400, 320));
                    ghosts.add(new Ghost(400, 320, Color.PINK, speed, 400, 320));
                    ghosts.add(new Ghost(400, 320, Color.CYAN, speed, 400, 320));
                    ghosts.add(new Ghost(400, 320, Color.ORANGE, speed, 400, 320));
                }
        this.setPreferredSize(new Dimension(840, 940));
        this.setLayout(null);
        this.setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode(); //k = en knap der er blevet trykket ned

        if (k == KeyEvent.VK_Q) { // hvis k er Q og man trykker mens man spiller så er det gameover og man får en score
            if (currentState == State.PLAYING) {
                gameOver = true;
                checkScore(); 
            } 
            // Tryk Q ved nogle af de her "menuer" havner du i profile
            else if (currentState == State.STATISTICS || currentState == State.ACHIEVEMENTS || 
                     currentState == State.STORE || currentState == State.FRIENDS_HUB ||
                     currentState == State.POINTS || currentState == State.CONTROLS) {
                switchState(State.PROFILE_HUB);
            } 
            else if (currentState == State.PROFILE_HUB || currentState == State.MAP_SELECT || 
                     currentState == State.OVERALL_LEADERBOARD || currentState == State.LEADERBOARD) {
                switchState(State.MENU);
            }
            else if (currentState == State.MAP_HUB) {
                switchState(State.MAP_SELECT);
            }
            else if (currentState == State.MENU) {
                System.exit(0);
            }
        }

        if (currentState == State.PLAYING && !gameOver) { //hvis k er W,S,A elle D så bevæger den en direktion (NextDireX/Y) og den sætter en vinkel på pacman så den vender den vej
            if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)    { nextDireX = 0;  nextDireY = -4; nextAngle = 90;  }
            if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)  { nextDireX = 0;  nextDireY = 4;  nextAngle = 270; }
            if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)  { nextDireX = -4; nextDireY = 0;  nextAngle = 180; }
            if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT) { nextDireX = 4;  nextDireY = 0;  nextAngle = 0;   }
        }
        requestFocusInWindow();
    }
});

// CONSTRUCTOR:
//knapperne bliver lavet herinde, og de bliver tilføjet til panelet, og der bliver også tilføjet actionlisteners til dem, så de kan skifte state og lave log
// Login og profil knapper
loginBtn       = createBtn("LOG IN", 330, 300);
createAccBtn   = createBtn("MAKE ACCOUNT", 330, 360);

// Main Menu
playBtn        = createBtn("PLAY", 330, 300);
mapsBtn        = createBtn("MAPS", 330, 360);
overallBtn     = createBtn("ALL RECORDS", 330, 420);
friendsHubBtn  = createBtn("MY FRIENDS", 330, 540);
storeBtn       = createBtn("STORE", 330, 400);

// Profil "Undermenuer"
statsBtn       = createBtn("STATISTICS", 330, 300);
achieveBtn     = createBtn("ACHIEVEMENTS", 330, 360);
controlsBtn    = createBtn("CONTROLS", 330, 420);
pointsBtn      = createBtn("POINT SYSTEM", 330, 480);

// map selektion og hub
map1Btn        = createBtn("CLASSIC", 330, 300);
map2Btn        = createBtn("OPEN FIELD", 330, 360);
map3Btn        = createBtn("THE SPIRAL", 330, 420);
mapPlayBtn     = createBtn("PLAY MAP", 330, 400);
mapLeadBtn     = createBtn("RECORDS", 330, 460);

// Navigationssystem
backBtn        = createBtn("BACK", 330, 850);
retryBtn       = createBtn("TRY AGAIN", 250, 450);
menuBtn        = createBtn("BACK TO MENU", 430, 450);
addFriendBtn   = createBtn("ADD FRIEND", 330, 350);

// forskellige faner i leaderboardss
personalBtn    = createBtn("PERSONAL", 230, 120);
friendsBtn     = createBtn("FRIENDS", 345, 120);
allTimeBtn     = createBtn("ALL TIME", 430, 120);

// forskellige faner i statistikker
stOverallBtn   = createBtn("OVERALL", 100, 120);
stMap1Btn      = createBtn("CLASSIC", 280, 120);
stMap2Btn      = createBtn("FIELD", 460, 120);
stMap3Btn      = createBtn("SPIRAL", 640, 120);

// forskellige faner i achievements
achGameplayBtn = createBtn("GAMEPLAY", 100, 20);
achOneTimeBtn  = createBtn("ONE TIME", 340, 20);
achSingleBtn   = createBtn("SINGLE GAME", 580, 20);

// laver knapperne og giver dem et "arcade" look
JButton[] allButtons = {
    loginBtn, createAccBtn, playBtn, mapsBtn, overallBtn, statsBtn, achieveBtn, 
    controlsBtn, pointsBtn, backBtn, map1Btn, map2Btn, map3Btn, mapPlayBtn, 
    mapLeadBtn, retryBtn, menuBtn, personalBtn, friendsBtn, allTimeBtn, 
    stOverallBtn, stMap1Btn, stMap2Btn, stMap3Btn, achGameplayBtn, 
    achOneTimeBtn, achSingleBtn, addFriendBtn, friendsHubBtn, storeBtn
};

for(JButton b : allButtons) {
    if(b != null) styleArcadeBtn(b);
}

//  actionlisteners

// Login systemmet som får knapperne til at skifte state og lave logikken for at logge ind og oprette konto
loginBtn.addActionListener(e -> handleLogin(true));
createAccBtn.addActionListener(e -> handleLogin(false));
playBtn.addActionListener(e -> { selectedMap = 0; switchState(State.PLAYING); startHeltForfra(); });

// Menu Navigation
mapsBtn.addActionListener(e -> switchState(State.MAP_SELECT));
overallBtn.addActionListener(e -> { loadLeaderboards(); switchState(State.OVERALL_LEADERBOARD); });
statsBtn.addActionListener(e -> switchState(State.STATISTICS));
achieveBtn.addActionListener(e -> { achScrollY = 0; switchState(State.ACHIEVEMENTS); });
controlsBtn.addActionListener(e -> switchState(State.CONTROLS));
pointsBtn.addActionListener(e -> switchState(State.POINTS));
storeBtn.addActionListener(e -> switchState(State.STORE));
friendsHubBtn.addActionListener(e -> switchState(State.FRIENDS_HUB));
addFriendBtn.addActionListener(e -> handleAddFriend());

// mapsystemmer 
map1Btn.addActionListener(e -> { selectedMap = 0; switchState(State.MAP_HUB); startHeltForfra(); });
map2Btn.addActionListener(e -> { selectedMap = 1; switchState(State.MAP_HUB); startHeltForfra(); });
map3Btn.addActionListener(e -> { selectedMap = 2; switchState(State.MAP_HUB); startHeltForfra(); });
mapPlayBtn.addActionListener(e -> { switchState(State.PLAYING); startHeltForfra(); });
mapLeadBtn.addActionListener(e -> { showingPersonal = false; loadLeaderboards(); switchState(State.LEADERBOARD); });
retryBtn.addActionListener(e -> { gameOver = false; switchState(State.MAP_HUB); });
menuBtn.addActionListener(e -> { gameOver = false; switchState(State.MENU); });

// fane knapper i leaderboards
stOverallBtn.addActionListener(e -> { statsSelectedTab = 0; repaint(); });
stMap1Btn.addActionListener(e -> { statsSelectedTab = 1; repaint(); });
stMap2Btn.addActionListener(e -> { statsSelectedTab = 2; repaint(); });
stMap3Btn.addActionListener(e -> { statsSelectedTab = 3; repaint(); });

achGameplayBtn.addActionListener(e -> { statsSelectedAch = 0; achScrollY = 0; repaint(); });
achOneTimeBtn.addActionListener(e -> { statsSelectedAch = 1; achScrollY = 0; repaint(); });
achSingleBtn.addActionListener(e -> { statsSelectedAch = 2; achScrollY = 0; repaint(); });

friendsBtn.addActionListener(e -> { showingPersonal = false; showingFriends = true; loadFriendsLeaderboard(); repaint(); });
personalBtn.addActionListener(e -> { showingPersonal = true; showingFriends = false; repaint(); });
allTimeBtn.addActionListener(e -> { showingPersonal = false; showingFriends = false; repaint(); });

// fysisk tilbage knap der genbruger vores kode for at trykke Q. this så at den ved vi er i vinduet
backBtn.addActionListener(e -> {
    processKeyEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_Q, 'q'));
});

// vi sætter fokus og initialisere 
generateDailyChallenge();
generateWeeklyChallenges();

this.setFocusable(true);
this.requestFocusInWindow();


// 2. map navigation og logik for at starte spillet fra map hub eller se leaderboard for det valgte map
map1Btn.addActionListener(e -> { selectedMap = 0; switchState(State.MAP_HUB); startHeltForfra(); });
map2Btn.addActionListener(e -> { selectedMap = 1; switchState(State.MAP_HUB); startHeltForfra(); });
map3Btn.addActionListener(e -> { selectedMap = 2; switchState(State.MAP_HUB); startHeltForfra(); });

mapPlayBtn.addActionListener(e -> { switchState(State.PLAYING); startHeltForfra(); });
mapLeadBtn.addActionListener(e -> { showingPersonal = false; loadLeaderboards(); switchState(State.LEADERBOARD); });

retryBtn.addActionListener(e -> { gameOver = false; switchState(State.MAP_HUB); });
menuBtn.addActionListener(e -> { gameOver = false; switchState(State.MENU); });


// 3. scroll logik for achievements og shop som virker ved at lytte på scrollhjulet og så ændre en "scroll-y" variabel som vi bruger i paintComponent til at flytte det hele op og ned, og vi sørger for at den ikke kan scrolle længere end der er indhold ved at bruge Math.max og Math.min
this.addMouseWheelListener(e -> {
    int scrollAmount = e.getWheelRotation() * 40;

    if (currentState == State.ACHIEVEMENTS) {
        achScrollY = Math.max(0, Math.min(achScrollY + scrollAmount, 4800));
        repaint();
    } 
    else if (currentState == State.STORE) {
        storeScrollY = Math.max(0, Math.min(storeScrollY + scrollAmount, 1000));
        repaint();
    }
});

// spille motoren er en timer der kører hver 20ms og opdatere spillet hvis vi er i PLAYING state og ikke er gameover, og den kalder repaint for at tegne det hele igen
motor = new Timer(20, timerEvent -> { 
    if (currentState == State.PLAYING && !gameOver) {
        updateGame(); 
    }
    repaint(); 
});
motor.start();

       this.addMouseListener(new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
    int mx = e.getX();
    int my = e.getY();

    // Profil klik fra Menu
    if ((currentState == State.MENU || currentState == State.FRIENDS_HUB) && mx < 300 && my < 100) {
        switchState(State.PROFILE_HUB);
        return;
    }

    // store logik
    if (currentState == State.STORE) {
        // Vi beregner 'sy' (scroll-y) så klikket følger med de rullende kasser
        int sy = my + storeScrollY - 250; 
        
        System.out.println("KLIK: x=" + mx + ", y=" + my + " | sy=" + sy);

        // venstre side af skidtet
        if (mx > 100 && mx < 320) { //tjekker pixels mellem 100 og 320
            if (sy > 50 && sy < 200) handlePurchaseOrEquip("BLUE", 50); //sy checker pixels mellem 50 og 200, og hvis det er der så kalder den handlePurchaseOrEquip med itemID "BLUE" og pris 50
            else if (sy > 250 && sy < 400) handlePurchaseOrEquip("PINK_MLP", 100);
            else if (sy > 450 && sy < 600) handlePurchaseOrEquip("PINK_MAP", 200);
        }
        // højre side af skidtet
        else if (mx > 420 && mx < 640) {
            if (sy > 50 && sy < 200) handlePurchaseOrEquip("RED", 50);
            else if (sy > 250 && sy < 400) handlePurchaseOrEquip("ROBOT", 150);
            else if (sy > 450 && sy < 600) handlePurchaseOrEquip("GREEN_MAP", 200);
        }
        repaint();
    }
}

}
);
switchState(State.LOGIN);
this.requestFocusInWindow();
    }

private void handlePurchaseOrEquip(String itemID, int price) {
    String id = itemID.trim();        // Fjerner mellemrum 
    boolean owned = checkOwned(id);   // tjekker om vi ejer det allerede

    // hvis vi ikke ejer det, så er det et køb, ellers er det en equip
    if (!owned) {
        // spørg om de gerne vil købe det 
        int res = JOptionPane.showConfirmDialog(this, "Vil du købe " + id + "?", "BUTIK", JOptionPane.YES_NO_OPTION);
        
        if (res == JOptionPane.YES_OPTION) {
            if (coins >= price) {       // tjekker om de har coins nok
                coins -= price;         // tager pengende
                addOwnedItem(id);       // indsætter til OwnedItems så at det kan tjekkes i fremtiden
                updateUserData();       // gemmer den nye data så at det kan gemmes til useren næste gang
                JOptionPane.showMessageDialog(this, id + " købt!");
            } else {
                JOptionPane.showMessageDialog(this, "Ikke nok mønter!");
            }
        }
    } 
    // equip check
    else {
        // tjekker map
        if (id.equals("PINK_MAP")) {
            // hvis det allerede er pink så bliver det default, ellers bliver det pink aka equipped lets gooo
            equippedMap = equippedMap.equals("PINK_MAP") ? "DEFAULT" : "PINK_MAP";
            applySkins();   // Opdater wallColor med det samme
        }
        else if (id.equals("GREEN_MAP")) {
            equippedMap = equippedMap.equals("GREEN_MAP") ? "DEFAULT" : "GREEN_MAP";
            applySkins();
        }
        else if (id.equals("ROBOT")) {
            ghostSkin = ghostSkin.equals("ROBOT") ? "CLASSIC" : "ROBOT";
        }
        else if (id.equals("BLUE") || id.equals("RED") || id.equals("PINK_MLP")) {
            pacColor = pacColor.equals(id) ? "YELLOW" : id;
        }

        updateUserData(); //som nævnt husker det til næste gang spillet åbnes
        repaint();        // tegner det så man kan se at det er equippet eller ej
    }
}

// vi laver knapper du
private JButton createBtn(String txt, int x, int y) {
    // instansiere knappen med teksten der er valgt fx play eller noget andet 
    JButton b = new JButton(txt);
    
    // størrelse
    b.setBounds(x, y, 160, 45); 
    
    // sætter fokus så at den har fokus på stadig selve spillet og ikke på knappen
    b.setFocusable(false);
    
    // Starter som usynlig, så knapper fra forskellige menuer ikke går helt i lort med hinanden
    b.setVisible(false); 
    
    // Tilføjer knappen til selve menuen så den kan ses og klikkes på
    this.add(b);
    
    // Returnerer knappen så vi kan gemme den i addActionListenerne og style den
    return b;
}

// får knappen til at se cool ud
private void styleArcadeBtn(JButton b) {
    // Sort baggrund og cyan tekst
    b.setBackground(Color.BLACK); 
    b.setForeground(Color.CYAN); 
    
    // Fjerner rammen Java tegner omkring tekst ved klik
    b.setFocusPainted(false);
    
    // vi fandt font navn og indsætter impact fonten som er en klassisk arcade font, og sætter den til 18 for at den er stor og tydelig
    b.setFont(new Font("Impact", Font.PLAIN, 18));
    
    // laver en tynd kant omkring knappen i en cyan farve for at få det til at poppe lidt mere  
    b.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 2));
    
    // gør så knappen skifter farve ved mus over knapper
    b.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) { 
            // når mussen er over knappen så skift farve til gul
            b.setForeground(Color.YELLOW); 
            b.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); 
        }
        
        @Override
        public void mouseExited(MouseEvent e) { 
            // gå tilbage til cyan når mussen fucker af
            b.setForeground(Color.CYAN); 
            b.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 255), 2)); 
        }
    });
}

    private void switchState(State s) {
    // Opdaterer spillets nuværende state
    currentState = s;
    
    // vi skjuller alle knapper så de ikke går over hinanden
    JButton[] all = {playBtn, mapsBtn, overallBtn, statsBtn, achieveBtn, controlsBtn, pointsBtn, backBtn, 
                     map1Btn, map2Btn, map3Btn, mapPlayBtn, mapLeadBtn, retryBtn, menuBtn, loginBtn, 
                     createAccBtn, personalBtn, allTimeBtn, friendsBtn, stOverallBtn, stMap1Btn, 
                     stMap2Btn, stMap3Btn, achGameplayBtn, achOneTimeBtn, achSingleBtn, 
                     addFriendBtn, friendsHubBtn, storeBtn};
    
    // går gennem alle knapper og gør dem usynlige, hvis de er er ikke null
    for (JButton b : all) if(b != null) b.setVisible(false);
    
    // vis de knapper som er i det state fx LOGIN som har create account og login
    if (s == State.LOGIN) { 
        // Vises kun ved opstart
        loginBtn.setVisible(true); 
        createAccBtn.setVisible(true); 
    }
    else if (s == State.MENU) { 
        playBtn.setVisible(true); 
        mapsBtn.setVisible(true); 
        overallBtn.setVisible(true);
        loadLeaderboards();         // Henter scores
        generateDailyChallenge();   // Opdaterer dagens udfordring
    }
    else if (s == State.MAP_SELECT) { 
        map1Btn.setVisible(true); 
        map2Btn.setVisible(true); 
        map3Btn.setVisible(true); 
        backBtn.setVisible(true); 
        backBtn.setBounds(330, 850, 160, 45); // sætter back knap
    }
    else if (s == State.MAP_HUB) {
        mapPlayBtn.setVisible(true);
        mapLeadBtn.setVisible(true);
        backBtn.setVisible(true);
        mapPlayBtn.setBounds(330, 400, 160, 45);
        mapLeadBtn.setBounds(330, 460, 160, 45);
        backBtn.setBounds(330, 850, 160, 45);
    }
    else if (s == State.ACHIEVEMENTS) {
        backBtn.setVisible(true);
        achGameplayBtn.setVisible(true);
        achOneTimeBtn.setVisible(true);
        achSingleBtn.setVisible(true);

        int btnY = 20; 
        achGameplayBtn.setBounds(100, btnY, 160, 45);
        achOneTimeBtn.setBounds(340, btnY, 160, 45);
        achSingleBtn.setBounds(580, btnY, 160, 45);
        
        backBtn.setBounds(330, 850, 160, 45);
        achScrollY = 0; // Nulstiller scroll, så man starter i toppen af listen
    }
    else if (s == State.PROFILE_HUB) {
        loadOnlyFriendNames(); 
        JButton[] profBtns = {statsBtn, achieveBtn, storeBtn, controlsBtn, pointsBtn, friendsHubBtn, backBtn};
        int y = 280; 
        for (JButton b : profBtns) {
            if (b != null) {
                b.setVisible(true);
                b.setBounds(330, y, 160, 45);
                y += 60; 
            }
        }
        backBtn.setBounds(330, 750, 160, 45); 
    }
    else if (s == State.STATISTICS || s == State.STORE || 
             s == State.FRIENDS_HUB || s == State.POINTS || s == State.CONTROLS) {
        // Generel visning for undersider - primært kun en Back-knap
        backBtn.setVisible(true);
        backBtn.setBounds(330, 850, 160, 45);
        
        if(s == State.FRIENDS_HUB) {
            addFriendBtn.setVisible(true);
            addFriendBtn.setBounds(560, 100, 160, 40);
        }
    }
    else if (s == State.PLAYING) {
        // fjerner alle knapper når vi spiller og sætter fokus så tastatur input virker
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    //tegner det med de ændringer der er i det state
    repaint();
}

    private void handleLogin(boolean isLogin) {
        
        // Skaber et lille panel med 3 rækker (Bruger, Kode, Vis-knap)
        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5)); p.setBackground(Color.BLACK);
        JLabel l1 = new JLabel("USER:"); l1.setForeground(Color.YELLOW);// Ledetekster med gult
        JLabel l2 = new JLabel("PASS:"); l2.setForeground(Color.YELLOW);
        JTextField uf = new JTextField(); JPasswordField pf = new JPasswordField();
        JCheckBox cb = new JCheckBox("Show"); cb.setBackground(Color.BLACK); cb.setForeground(Color.WHITE);
        cb.addActionListener(e -> pf.setEchoChar(cb.isSelected() ? (char)0 : '*'));
        p.add(l1); p.add(uf); p.add(l2); p.add(pf); p.add(new JLabel("")); p.add(cb);
        int res = JOptionPane.showConfirmDialog(this, p, isLogin ? "LOGIN" : "CREATE", JOptionPane.OK_CANCEL_OPTION, -1);
        if (res == JOptionPane.OK_OPTION) {
            String u = uf.getText().trim(), pas = new String(pf.getPassword()).trim();
            if (isLogin) { if (validate(u, pas)) { currentUser = u; switchState(State.MENU); } else JOptionPane.showMessageDialog(this, "WRONG!"); }//Tjek om bruger findes
            else { if (saveUser(u, pas)) JOptionPane.showMessageDialog(this, "CREATED!"); else JOptionPane.showMessageDialog(this, "EXISTS!"); } //Forsøg at gemme den nye taber
        }
        loadLeaderboards();     
loadWeeklyStatus();     
generateWeeklyChallenges();
    }

private boolean validate(String u, String p) {
    try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
        String l;
        while ((l = br.readLine()) != null) {
            String[] parts = l.split(":");
            if (parts[0].equalsIgnoreCase(u) && parts[1].equals(p)) {
                currentUser = u;
                
                // Indlæser dine 8 parametre 
                if (parts.length >= 8) {
                    this.coins = Integer.parseInt(parts[2]);
                    this.pacColor = parts[3];
                    this.ghostSkin = parts[4];
                    this.ownedItems = parts[5];
                    this.equippedMap = parts[6];
                    this.lifetimeCoins = Integer.parseInt(parts[7]); 
                }
                
                // Synkroniserer level-systemet ved login
                this.oldUserLevel = (totalAP / 100) + 1;
                
                loadLeaderboards();     
                loadWeeklyStatus();     
                generateWeeklyChallenges(); 
                
                applySkins(); 
                return true;
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
    return false;
}






private boolean saveUser(String u, String p) {
    try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt", true))) {
        // Gemmer: Navn, Kode, mønter, Gul, Classic, items, map, Lifetime
        pw.println(u + ":" + p + ":0:YELLOW:CLASSIC:YELLOW,CLASSIC:DEFAULT:0");
        return true;
    } catch (Exception e) {} return false;
}

private void updateGame() {
    // Frigiver 1 spøgelse hvert 10. sekund
    long nu = System.currentTimeMillis();
    if (ghostsActiveCount < 4 && nu - lastGhostSpawnTime > 10000) {
        ghostsActiveCount++;
        lastGhostSpawnTime = nu;
    }

    // mund
    if (directionX != 0 || directionY != 0) {
        mouthTimer += 0.25f; 
        mouthAngle = (int)(330 + Math.sin(mouthTimer) * 30);
    } else {
        mouthAngle = 330;
    }

    // streak og freaky frightenedtimer
    if (frightenedTimer > 0) {
        frightenedTimer--;
        if (frightenedTimer == 0) {
            ghostsEatenInStreak = 0;
            for (Ghost g : ghosts) g.isFrightenedLocal = false;
        }
    }

    // gør så atnår pacman er helt inde i et grid felt så kan den skifte ret
    if (pacX % gridSize == 0 && pacY % gridSize == 0) {
        int r = pacY / gridSize;
        int c = pacX / gridSize;
        
        int nr = r + (nextDireY / speed);
        int nc = c + (nextDireX / speed);
        
        if (nr >= 0 && nr < 21 && nc >= 0 && nc < 21 && bane[nr][nc] != 1) {
            directionX = nextDireX;
            directionY = nextDireY;
            eatAngle = nextAngle;
        }
    }

    // checker for vægge og stopper pacman hvis der er en væg, ellers bevæger den pacman i den retning den skal bevæge sig i, og den laver også tunnel bevægelse
    int nX = pacX + directionX;
    int nY = pacY + directionY;
    int col = (nX + (directionX > 0 ? 38 : 2)) / gridSize;
    int row = (nY + (directionY > 0 ? 38 : 2)) / gridSize;

    if (col < 0 || col >= 21) {
        pacX = nX; // Tunnel bevægelse
    } 
    else if (row >= 0 && row < 21 && bane[row][col] != 1) {
        pacX = nX;
        pacY = nY;
    } else {
        directionX = 0; directionY = 0;
        pacX = Math.round((float)pacX / gridSize) * gridSize;
        pacY = Math.round((float)pacY / gridSize) * gridSize;
    }

    // Pellets, Power Pellets og Frugt-spawn
    int er = (pacY + 20) / gridSize;
    int ec = (pacX + 20) / gridSize;
    if (er >= 0 && er < 21 && ec >= 0 && ec < 21) {
        int feltValue = bane[er][ec];
        if (feltValue == 2 || feltValue == 3) {
            bane[er][ec] = 0; 
            pelletsEatenInLevel++; 

            if (pelletsEatenInLevel == 50 && !fruit1Spawned) { spawnFruit(); fruit1Spawned = true; }
            if (pelletsEatenInLevel == 110 && !fruit2Spawned) { spawnFruit(); fruit2Spawned = true; }

            if (feltValue == 2) score += 10;
            else if (feltValue == 3) {
                score += 50;
                frightenedTimer = 600;
                ghostsEatenInStreak = 0;
                for(Ghost g : ghosts) g.isFrightenedLocal = true;
            }
            checkWin();
        }
    }

    // checker om pacman er i nærheden af frugten og hvis den er aktiv så spiser den den og giver point baseret på level og hvor
    if (fruitActive && Math.hypot(pacX - (fruitX * gridSize), pacY - (fruitY * gridSize)) < 30) {
        int[] fVals = {100, 250, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000};
        score += fVals[Math.min(level - 1, 9)];
        sessionFruitCounts[Math.min(level - 1, 9)]++;
        sessionFruits++;
        fruitActive = false;
    }

    // ai og kollision spøgelser
    for (int i = 0; i < ghosts.size(); i++) {
        if (i < ghostsActiveCount) {
            Ghost g = ghosts.get(i);
            // Sender indeks (i) og Blinky (ghosts.get(0)) med til AI'en
            g.update(pacX, pacY, bane, gridSize, difficulty, i, ghosts.get(0));
            
            if (Math.hypot(pacX - g.x, pacY - g.y) < 28) {
                if (g.isFrightenedLocal) {
                    ghostsEatenInStreak++;
                    score += 200 * (int)Math.pow(2, Math.min(ghostsEatenInStreak - 1, 3));
                    sessionGhosts++;
                    g.respawn();
                    g.isFrightenedLocal = false;
                } else {
                    lives--;
                    diedThisLevel = true;
                    if (lives > 0) respawn(); 
                    else { 
                        gameOver = true; 
                        checkScore(); 
                        checkWeeklyProgress(); 
                    }
                    break;
                }
            }
        }
    }
    if (pacX < -30) pacX = 840;
    else if (pacX > 840) pacX = -30;
}


@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    // Gør grafikken blødere (Antialiasing)
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // baggrund
    g2.setColor(Color.BLACK);
    g2.fillRect(0, 0, 840, 940);
    
   //rammer i arcade form
    if (currentState != State.PLAYING) drawArcadeBackground(g2);

    // menu for statistik, achievemetns osv
    if (currentState == State.STATISTICS) {
        drawStatistics(g);
    } else if (currentState == State.ACHIEVEMENTS) {
        drawAchievements(g2);
    } else if (currentState == State.CONTROLS) {
        drawControls(g);
    } else if (currentState == State.POINTS) {
        drawPoints(g);
    } else if (currentState == State.STORE) {
        drawStore(g2);
    } else if (currentState == State.FRIENDS_HUB) {
        drawFriendsList(g);
    } else if (currentState == State.OVERALL_LEADERBOARD) {
        drawList(g, overallLeaderboard, "GLOBAL RECORDS", true);
    } else if (currentState == State.LEADERBOARD) {
        if (showingPersonal) drawList(g, personalLeaderboard, "MY RECORDS", true);
        else if (showingFriends) drawList(g, friendsLeaderboard, "FRIENDS RECORDS", true);
        else drawList(g, mapLeaderboard, "TOP SCORES", true);
    }


    // hovedmenu og challenges
    else if (currentState == State.MENU || currentState == State.PROFILE_HUB) {
        drawTitle(g2, "PAC-MAN");
        drawPlayerIcon(g2); // XP Bar, Level og AP
        
        // coin icon
        g2.setFont(new Font("Impact", Font.PLAIN, 28));
        g2.setColor(Color.YELLOW);
        g2.drawString("COINS: " + coins, 640, 60);
        g2.setColor(new Color(255, 215, 0));
        g2.fillOval(605, 36, 26, 26);

        if (currentState == State.MENU) {
            int dy = 500;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(180, dy, 480, 90, 20, 20);
            g2.setColor(dailyChallengeDone ? Color.GREEN : new Color(0, 150, 255));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(180, dy, 480, 90, 20, 20);
            
            g2.setFont(new Font("Impact", Font.PLAIN, 22));
            g2.setColor(Color.YELLOW);
            g2.drawString("DAILY CHALLENGE (+25 AP)", 200, dy + 35);
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            g2.setColor(dailyChallengeDone ? Color.GREEN : Color.WHITE);
            g2.drawString(dailyChallengeDesc + (dailyChallengeDone ? " [OK]" : ""), 200, dy + 70);

            // weekly challenges box
            int wy = 610;
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(180, wy, 480, 210, 20, 20);
            g2.setColor(new Color(255, 215, 0)); 
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(180, wy, 480, 210, 20, 20);

            g2.setFont(new Font("Impact", Font.PLAIN, 22));
            g2.setColor(Color.YELLOW);
            g2.drawString("WEEKLY CHALLENGES", 200, wy + 35);

            g2.setFont(new Font("Arial", Font.BOLD, 13));
            for (int i = 0; i < 5; i++) {
                int curVal = (i==0?careerGhosts : i==1?careerRounds : i==2?careerFruitsTotal : i==3?careerGms : careerHighScore);
                int displayVal = Math.min(curVal, weeklyChallengeGoals[i]);
                String progressTxt = " [" + formatValue(displayVal) + "/" + formatValue(weeklyChallengeGoals[i]) + "]";
                
                if (weeklyChallengeDone[i]) {
                    g2.setColor(Color.GREEN);
                    g2.drawString("- " + weeklyChallengeDescs[i] + " [OK]", 200, wy + 70 + (i * 28));
                } else {
                    g2.setColor(i == 4 ? Color.ORANGE : Color.WHITE);
                    String reward = "(+" + weeklyChallengeRewards[i] + " Coins)";
                    g2.drawString("- " + weeklyChallengeDescs[i] + progressTxt + " " + reward, 200, wy + 70 + (i * 28));
                }
            }
        }
    }
    // gameplay tegning
    else if (currentState == State.PLAYING || currentState == State.MAP_HUB || gameOver) {
        // map
        for (int r = 0; r < 21; r++) {
            for (int c = 0; c < 21; c++) {
                int bx = c * gridSize, by = r * gridSize;
                if (bane[r][c] == 1) { // Vægge med neon-glød
                    g2.setColor(new Color(0, 0, 40)); 
                    g2.fillRect(bx, by, gridSize, gridSize);
                    g2.setColor(new Color(0, 150, 255, 180));
                    g2.setStroke(new BasicStroke(3));
                    g2.drawRect(bx + 3, by + 3, gridSize - 6, gridSize - 6);
                } else if (bane[r][c] == 2) { // Prikker
                    g2.setColor(new Color(255, 184, 151));
                    g2.fillRect(bx + 18, by + 18, 4, 4);
                } else if (bane[r][c] == 3) { // Power Pellets
                    if ((System.currentTimeMillis() / 250) % 2 == 0) {
                        g2.setColor(Color.WHITE);
                        g2.fillOval(bx + 10, by + 10, 20, 20);
                    }
                }
            }
        }

        // Tegn frugt hvis den er fremme
        if (fruitActive) drawFruitVisuals(g2, fruitX * gridSize + 10, fruitY * gridSize + 10, true, level);

        // pacmaaaan
        if (pacColor.equals("PINK_MLP")) {
            drawPinkPonySkin(g2); 
        } else {
            // Klassisk Pac-Man farvevalg
            if (pacColor.equals("BLUE")) g2.setColor(new Color(0, 150, 255));
            else if (pacColor.equals("RED")) g2.setColor(Color.RED);
            else g2.setColor(Color.YELLOW);
            
            int rotation = (directionX > 0) ? 0 : (directionX < 0 ? 180 : (directionY < 0 ? 90 : 270));
            int opening = 360 - mouthAngle;
            g2.fillArc(pacX, pacY, 36, 36, rotation + (opening / 2), mouthAngle);
        }

        // spøgelser
        for (Ghost gh : ghosts) gh.draw(g2, frightenedTimer, ghostSkin);
        
        // score og liv med hjerter, og level låst til spil
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Impact", Font.PLAIN, 22));
        g2.drawString("SCORE: " + score, 300, 895);
        for (int i = 0; i < lives; i++) g2.fillArc(95 + (i * 45), 868, 32, 32, 45, 270);

        // Game Over Overlay
        if (gameOver) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, 840, 940);
            g2.setColor(Color.RED);
            g2.setFont(new Font("Impact", Font.PLAIN, 70));
            g2.drawString("GAME OVER", 260, 450);
            this.requestFocusInWindow();
        }
        g2.setColor(Color.YELLOW);
    g2.setFont(new Font("Impact", Font.PLAIN, 22));
    
    g2.drawString("SCORE: " + score, 300, 895);
    g2.drawString("LEVEL: " + level, 680, 895); 

    for (int i = 0; i < lives; i++) {
        g2.fillArc(95 + (i * 45), 868, 32, 32, 45, 270);
    }}
}



private void generateDailyChallenge() {
    java.util.Calendar cal = java.util.Calendar.getInstance(); //henter dato
    int dayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR);
    Random dayRand = new Random(dayOfYear);
    
    //vælg 1 ud af 3 forskellige udfordringstyper: spis X spøgelser, score Y point, eller nå level Z
    int type = dayRand.nextInt(3);
    if (type == 0) {
        dailyChallengeType = "GHOSTS";
        dailyChallengeGoal = 5 + dayRand.nextInt(15);
        dailyChallengeDesc = "Eat " + dailyChallengeGoal + " Ghosts in one game";
    } else if (type == 1) {
        dailyChallengeType = "POINTS";
        dailyChallengeGoal = 5000 + (dayRand.nextInt(10) * 1000);
        dailyChallengeDesc = "Score " + dailyChallengeGoal + " points in one game";
    } else {
        dailyChallengeType = "LEVELS";
        dailyChallengeGoal = 2 + dayRand.nextInt(4);
        dailyChallengeDesc = "Reach Level " + dailyChallengeGoal + " in a run";
    }
    checkIfDailyDone();
}

private void saveDailyCompletion() { //gemmer daily udfordringen i en fil ved at skrive brugernavn og dato ind, så den kan tjekke det
    try (PrintWriter pw = new PrintWriter(new FileWriter("daily.txt", true))) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String today = cal.get(java.util.Calendar.DAY_OF_YEAR) + "-" + cal.get(java.util.Calendar.YEAR);
        pw.println(currentUser + ":" + today);
    } catch (Exception e) { e.printStackTrace(); }
}

private void checkIfDailyDone() {
    dailyChallengeDone = false; //starter med at antage det ikke er gjort
    File f = new File("daily.txt");
    if (!f.exists()) return;
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String today = cal.get(java.util.Calendar.DAY_OF_YEAR) + "-" + cal.get(java.util.Calendar.YEAR);
        String target = currentUser + ":" + today;
        String line; 
        while ((line = br.readLine()) != null) { //tjek om der er et match i filen med brugernavn og data
            if (line.trim().equals(target)) {
                dailyChallengeDone = true;
                break; 
            }
        }
    } catch (Exception e) { e.printStackTrace(); }
}

   private void drawAchievements(Graphics2D g2) {
    // toppen med sort baggrund og arcade mønster
    g2.setColor(Color.BLACK); 
    g2.fillRect(0, 0, 840, 150);
    drawArcadeBackground(g2); 
    
    g2.setColor(Color.YELLOW); 
    g2.setFont(new Font("Impact", Font.ITALIC, 45));
    String headerTxt = (statsSelectedAch == 0) ? "GAMEPLAY PROGRESS" :  //vælger fane header
                       (statsSelectedAch == 1 ? "ONE TIME CHALLENGES" : "SINGLE GAME RECORDS");
    g2.drawString(headerTxt, 80, 120);

    // data med variabler der tæller for progression og rekorder, og arrays til frugt tælling og navne
    int cGms = 0, cGho = 0, cRnd = 0, mSco = 0, mLvl = 0, mGho = 0, mNoD = 0, tDaily = 0;
    int[] tFru = new int[10];
    String[] fTypes = {"CHERRY", "STRAWBERRY", "APPLE", "BANANA", "ORANGE", "BELL", "KEY", "STAR", "HEART", "CROWN"};

    try (BufferedReader br = new BufferedReader(new FileReader("daily.txt"))) { //tæller hvor mange dailies jeg har lavet i alt
        String l; while ((l = br.readLine()) != null) if (l.startsWith(currentUser + ":")) tDaily++;
    } catch (Exception e) {}

    try (BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"))) { //gennemgå leaderboard og tæl statistikker
        String l;
        while ((l = br.readLine()) != null) {
            String[] p = l.split(":");
            if (p.length < 18 || !p[0].equalsIgnoreCase(currentUser)) continue;
            cGms++; 
            mSco = Math.max(mSco, Integer.parseInt(p[1]));
            int ghostsInGame = Integer.parseInt(p[3]);
            mGho = Math.max(mGho, ghostsInGame); //flest spøgelser i et spil
            cGho += ghostsInGame; //samlet spøgelser nogensinde
            cRnd += Integer.parseInt(p[5]); //runder klaret
            mLvl = Math.max(mLvl, Integer.parseInt(p[5])); //højeste level nået
            for(int i=0; i<10; i++) tFru[i] += Integer.parseInt(p[i+8]); //tæller frugttypet
            if (p.length >= 19) mNoD = Math.max(mNoD, Integer.parseInt(p[18])); //no death runder
        }
    } catch (Exception e) {}

    // scrolls
        Shape originalClip = g2.getClip(); //sætter et clip så der er tegnet inden for et område og sætte graffik op og ned for at lave scroll effekten
    g2.setClip(15, 150, 810, 680); 
    g2.translate(0, -achScrollY + 150);

    int yPos = 50;
    int ownedSkins = (ownedItems == null || ownedItems.isEmpty()) ? 0 : ownedItems.split(",").length; //tæller skins

    if (statsSelectedAch == 0) { // --- GAMEPLAY PROGRESS ---
        int bigGap = 350;   // afstand fra kasser  (10)
        int smallGap = 180; // afstand fra kasser  (5)


        int[] longMiles = {1, 10, 25, 50, 100, 250, 500, 1000, 2500, 5000};
        drawAchGrid(g2, "GAMES", cGms, longMiles, 80, yPos);
        drawAchGrid(g2, "GHOSTS", cGho, new int[]{10, 50, 100, 250, 500, 1000, 2500, 5000, 7500, 10000}, 80, yPos += bigGap);
        drawAchGrid(g2, "COINS", lifetimeCoins, new int[]{100, 500, 1000, 2500, 5000, 10000, 25000, 50000, 75000, 100000}, 80, yPos += bigGap);
        drawAchGrid(g2, "SKINS", ownedSkins, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 80, yPos += bigGap);
        drawAchGrid(g2, "DAILY", tDaily, longMiles, 80, yPos += bigGap);
        drawAchGrid(g2, "ROUNDS", cRnd, longMiles, 80, yPos += bigGap);
        
        // tegner frugttyper i achievemenets
        yPos += bigGap;
        for(int i=0; i<10; i++) {
            drawAchGrid(g2, fTypes[i], tFru[i], new int[]{1, 10, 25, 50, 100}, 80, yPos);
            yPos += smallGap;
        }
    } 
    else if (statsSelectedAch == 1) { // --- ONE TIME ---
        drawOneTimeAch(g2, mSco, mNoD);
    } 
    else if (statsSelectedAch == 2) { // --- SINGLE GAME ---
        drawAchGrid(g2, "SCORE", mSco, new int[]{1000, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 750000, 1000000}, 80, yPos);
        drawAchGrid(g2, "GHOSTS", mGho, new int[]{1, 4, 8, 12, 16, 20, 25, 30, 40, 50}, 80, yPos += 350);
        drawAchGrid(g2, "LEVELS", mLvl, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 80, yPos += 350);
    }

    // 4. CLEANUP
    g2.translate(0, achScrollY - 150);
    g2.setClip(originalClip);
}

 private void drawAchGrid(Graphics2D g2, String label, int currentVal, int[] miles, int x, int y) {
    // 1. OVERSKRIFT FOR KATEGORIEN (f.eks. GHOSTS: 142)
    g2.setFont(new Font("Impact", Font.PLAIN, 24));
    g2.setColor(Color.CYAN);
    String prettyName = isFruitType(label) ? formatFruitName(label, true) : label;
    g2.drawString(prettyName + ": " + formatValue(currentVal), x, y + 25);

    for (int i = 0; i < miles.length; i++) {
        int row = i / 5;
        int col = i % 5;
        int kx = x + (col * 145);
        int ky = y + 40 + (row * 150);
        
        // Tjek om milepælen er nået
        boolean completed = (currentVal >= miles[i]);

        // Baggrund: Grønlig hvis færdig, mørkgrå hvis ikke
        if (completed) {
            g2.setPaint(new GradientPaint(kx, ky, new Color(0, 255, 100, 60), kx, ky + 130, new Color(0, 100, 0, 100)));
        } else {
            g2.setPaint(new GradientPaint(kx, ky, new Color(255, 255, 255, 20), kx, ky + 130, new Color(0, 0, 0, 80)));
        }
        g2.fillRoundRect(kx, ky, 130, 130, 20, 20);

        // ramme
        g2.setStroke(new BasicStroke(completed ? 3 : 1));
        g2.setColor(completed ? Color.GREEN : new Color(255, 255, 255, 40));
        g2.drawRoundRect(kx, ky, 130, 130, 20, 20);

        // icon
        drawAchIcon(g2, label, kx + 45, ky + 30, completed);

        // Milepæls-tekst
        g2.setFont(new Font("Impact", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        String targetTxt = formatValue(miles[i]);
        int tw = g2.getFontMetrics().stringWidth(targetTxt);
        g2.drawString(targetTxt, kx + 65 - (tw/2), ky + 105);

        // Status tekst nederst
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        if (completed) {
            g2.setColor(Color.YELLOW);
            String status = "ACHIEVED!";
            g2.drawString(status, kx + 65 - (g2.getFontMetrics().stringWidth(status)/2), ky + 122);
        } else {
            g2.setColor(new Color(255, 255, 255, 150));
            String status = "LÅST";
            g2.drawString(status, kx + 65 - (g2.getFontMetrics().stringWidth(status)/2), ky + 122);
        }
    }
}

private void drawAchIcon(Graphics2D g2, String label, int x, int y, boolean completed) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Color mainColor = completed ? Color.YELLOW : Color.GRAY;
    
    if (label.equals("GAMES")) {
        g2.setColor(mainColor);
        g2.fillArc(x, y, 40, 40, 45, 270);
    } 
    else if (label.equals("GHOSTS")) {
        drawDetailedGhost(g2, x, y, completed ? Color.RED : Color.GRAY);
    } 
    else if (label.equals("COINS")) {
        g2.setColor(new Color(255, 215, 0)); // Guld
        if (!completed) g2.setColor(Color.GRAY);
        g2.fillOval(x + 5, y, 32, 32);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("$", x + 15, y + 23);
    }
    else if (label.equals("ROUNDS") || label.equals("LEVELS")) {
        // Tegner en lille stige/trappe for progression
        g2.setColor(mainColor);
        g2.fillRect(x, y + 25, 10, 15);
        g2.fillRect(x + 12, y + 15, 10, 25);
        g2.fillRect(x + 24, y + 5, 10, 35);
    }
    else if (label.equals("SKINS")) {
        // Tegner en lille t-shirt eller maske
        g2.setColor(completed ? new Color(255, 0, 255) : Color.GRAY);
        g2.fillRoundRect(x + 5, y + 5, 30, 30, 10, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 12, y + 12, 6, 6);
        g2.fillOval(x + 22, y + 12, 6, 6);
    }
    else if (label.equals("DAILY")) {
        // Tegner en lille kalender eller stjerne
        g2.setColor(completed ? Color.ORANGE : Color.GRAY);
        int[] px = {x+20, x+25, x+40, x+28, x+35, x+20, x+5, x+12, x+0, x+15};
        int[] py = {y+0, y+15, y+15, y+25, y+40, y+30, y+40, y+25, y+15, y+15};
        g2.fillPolygon(px, py, 10);
    }
    else if (isFruitType(label)) {
        drawSpecificFruitIcon(g2, label, x, y, completed);
    }
}





private boolean isFruitType(String t) {
    String[] f = {"CHERRY","STRAWBERRY","APPLE","BANANA","ORANGE","BELL","KEY","STAR","HEART","CROWN"};
    for(String s : f) if(s.equals(t)) return true;
    return false;
}

private String formatFruitName(String type, boolean plural) {
    String name = type.toLowerCase();
    if (!plural) return type;
    if (name.equals("cherry")) return "Cherries";
    if (name.equals("strawberry")) return "Strawberries";
    return type + "s"; //for fleretal af fruits
}
private void drawSpecificFruitIcon(Graphics2D g2, String type, int x, int y, boolean ok) { //tegning af frugterne
    int fLvl = 1;
    if(type.equals("STRAWBERRY")) fLvl = 2;
    else if(type.equals("APPLE")) fLvl = 3;
    else if(type.equals("BANANA")) fLvl = 4;
    else if(type.equals("ORANGE")) fLvl = 5;
    else if(type.equals("BELL")) fLvl = 6;
    else if(type.equals("KEY")) fLvl = 7;
    else if(type.equals("STAR")) fLvl = 8;
    else if(type.equals("HEART")) fLvl = 9;
    else if(type.equals("CROWN")) fLvl = 10;

    drawFruitVisuals(g2, x, y, ok, fLvl);
}
 private void drawFruitVisuals(Graphics2D g2, int fx, int fy, boolean ok, int fruitLevel) {
    if (!ok) g2.setColor(Color.GRAY); // Gør den grå hvis ikke opnået

    //hvor skal tegningen være
    if (fruitLevel == 1) { // Kirsebær
        if(ok) g2.setColor(Color.RED); g2.fillOval(fx, fy+5, 12, 12); g2.fillOval(fx+10, fy+8, 12, 12);
        g2.setColor(ok ? new Color(34, 139, 34) : Color.DARK_GRAY); g2.drawLine(fx+6, fy+5, fx+15, fy-2);
    } else if (fruitLevel == 2) { // Jordbær
        if(ok) g2.setColor(new Color(255, 50, 50)); g2.fillArc(fx, fy, 22, 25, 0, 360);
        g2.setColor(ok ? Color.WHITE : Color.DARK_GRAY); g2.fillRect(fx+5, fy+5, 2, 2); g2.fillRect(fx+12, fy+10, 2, 2);
    } else if (fruitLevel == 3) { // Æble
        if(ok) g2.setColor(Color.RED); g2.fillOval(fx, fy, 22, 22);
        g2.setColor(ok ? new Color(139, 69, 19) : Color.DARK_GRAY); g2.fillRect(fx+10, fy-4, 3, 6);
    } else if (fruitLevel == 4) { // Banan
        if(ok) g2.setColor(Color.YELLOW); g2.fillArc(fx-5, fy, 30, 20, 180, 180);
    } else if (fruitLevel == 5) { // Appelsin
        if(ok) g2.setColor(new Color(255, 140, 0)); g2.fillOval(fx, fy, 22, 22);
        if(ok) g2.setColor(new Color(0, 100, 0)); g2.fillOval(fx+10, fy-2, 5, 5);
    } else if (fruitLevel == 6) { // Klokke
        if(ok) g2.setColor(Color.YELLOW); g2.fillArc(fx, fy, 22, 22, 0, 180); g2.fillRect(fx, fy+11, 22, 5);
        if(ok) g2.setColor(Color.WHITE); g2.fillOval(fx+8, fy+16, 6, 6);
    } else if (fruitLevel == 7) { // Nøgle
        g2.setColor(ok ? Color.LIGHT_GRAY : Color.GRAY); g2.drawOval(fx+5, fy, 12, 12); g2.fillRect(fx+10, fy+12, 3, 10);
    } else if (fruitLevel == 8) { // Stjerne
        if(ok) g2.setColor(Color.YELLOW); int[] xs = {fx+11, fx+14, fx+22, fx+16, fx+18, fx+11, fx+4, fx+6, fx+0, fx+8};
        int[] ys = {fy, fy+7, fy+7, fy+12, fy+20, fy+15, fy+20, fy+12, fy+7, fy+7}; g2.fillPolygon(xs, ys, 10);
    } else if (fruitLevel == 9) { // Hjerte
        if(ok) g2.setColor(new Color(255, 105, 180)); g2.fillOval(fx, fy, 12, 12); g2.fillOval(fx+10, fy, 12, 12);
        int[] hx = {fx, fx+11, fx+22}; int[] hy = {fy+8, fy+22, fy+8}; g2.fillPolygon(hx, hy, 3);
    } else { // Level 10+: Krone
        if(ok) g2.setColor(new Color(255, 215, 0)); int[] kx = {fx, fx, fx+7, fx+11, fx+15, fx+22, fx+22};
        int[] ky = {fy+20, fy+5, fy+12, fy+5, fy+12, fy+5, fy+20}; g2.fillPolygon(kx, ky, 7);
    }
}


  private void loadLeaderboards() {
    overallLeaderboard.clear(); 
    mapLeaderboard.clear();  // rydder de tre lister for at opdatere dem med data fra filen 
    personalLeaderboard.clear();
    totalAP = 0;
    int lifG = 0, lifF = 0, lifR = 0, lifM = 0, lifGms = 0;

    try (BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"))) { //læser leaderboard filen og opdaterer de tre lister og tæller lifetime stats for AP
        String l;
        while ((l = br.readLine()) != null) {
            String[] p = l.split(":");
            if (p.length < 3) continue;

            int scoreVal = Integer.parseInt(p[1]);
            ScoreEntry se = new ScoreEntry(p[0], scoreVal, p[2]);
            overallLeaderboard.add(se);

            if (p[2].equalsIgnoreCase(mapNames[selectedMap])) {
                mapLeaderboard.add(se);
                if (p[0].equalsIgnoreCase(currentUser)) personalLeaderboard.add(se);
            }

            if (p[0].equalsIgnoreCase(currentUser)) { // Hvis det er den nuværende bruger, opdateres lifetime stats
                lifGms++; 
                lifM = Math.max(lifM, scoreVal);
                lifG += (p.length > 3) ? Integer.parseInt(p[3]) : 0;
                lifF += (p.length > 4) ? Integer.parseInt(p[4]) : 0;
                lifR += (p.length > 5) ? Integer.parseInt(p[5]) : 0;
            }
        }
        
        overallLeaderboard.sort((a, b) -> b.score - a.score);
        mapLeaderboard.sort((a, b) -> b.score - a.score);
        personalLeaderboard.sort((a, b) -> b.score - a.score);

        // Beregn total AP
        totalAP = lifGms + calcAP(lifG, new int[]{1, 5, 10, 25, 50, 100, 250, 500, 1000, 2000}) 
                         + calcAP(lifF, new int[]{1, 2, 5, 10, 20, 50, 75, 100, 150, 200}) 
                         + calcAP(lifR, new int[]{1, 2, 5, 10, 20, 50, 75, 100, 150, 200}) 
                         + calcAP(lifM, new int[]{1000, 5000, 10000, 25000, 50000, 100000, 250000, 500000, 750000, 1000000});

        int newUserLevel = (totalAP / 100) + 1;

if (newUserLevel > oldUserLevel) {
    if (oldUserLevel > 0) { // Undgå mønter ved allerførste login
        coins += 10; // +10 Coins for hver level-up
    }
    oldUserLevel = newUserLevel;
    updateUserData();
}

    } catch (Exception e) { e.printStackTrace(); }
}


    
    private void drawStatistics(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    // Baggrundsramme
    g2.setColor(new Color(0, 0, 0, 215));
    g2.fillRect(80, 120, 680, 720);
    g2.setColor(new Color(0, 150, 255));
    g2.setStroke(new BasicStroke(3));
    g2.drawRect(80, 120, 680, 720);

    // Titel baseret på valgt fane (Overall eller Map-specifik)
    g2.setColor(Color.YELLOW);
    g2.setFont(new Font("Impact", Font.PLAIN, 45));
    String title = (statsSelectedTab == 0) ? "OVERALL CAREER" : mapNames[statsSelectedTab-1] + " STATS";
    g2.drawString(title, 420 - (g2.getFontMetrics().stringWidth(title) / 2), 95);

    int labelX = 120, y = 210, gap = 32;
    int ownedSkinsCount = (ownedItems == null || ownedItems.isEmpty()) ? 0 : ownedItems.split(",").length;

    // coins og skins sektion
    g2.setFont(new Font("Impact", Font.PLAIN, 22));
    g2.setColor(Color.CYAN); 
    g2.drawString("FINANCES & COLLECTION", labelX, y); 
    
    y += 30;
    g2.setFont(new Font("Monospaced", Font.BOLD, 17));
    g2.setColor(Color.WHITE);
    drawStatLine(g2, "CURRENT COINS:", String.valueOf(coins), labelX, y);
    drawStatLine(g2, "LIFETIME COINS:", String.valueOf(lifetimeCoins), labelX, y += gap);
    drawStatLine(g2, "SKINS OWNED:", ownedSkinsCount + " / 10", labelX, y += gap);

    // total progress sektion
    y += 45;
    g2.setFont(new Font("Impact", Font.PLAIN, 22));
    g2.setColor(Color.CYAN); 
    g2.drawString("TOTAL PROGRESS", labelX, y); 
    
    y += 30;
    g2.setFont(new Font("Monospaced", Font.BOLD, 17));
    g2.setColor(Color.WHITE);
    drawStatLine(g2, "GAMES PLAYED:", String.valueOf(careerGms), labelX, y);
    drawStatLine(g2, "TOTAL GHOSTS:", String.valueOf(careerGhosts), labelX, y += gap);
    drawStatLine(g2, "TOTAL FRUITS:", String.valueOf(careerFruitsTotal), labelX, y += gap);
    drawStatLine(g2, "LEVELS CLEARED:", String.valueOf(careerRounds), labelX, y += gap);
    

    // frugt
    y += 45;
    g2.setFont(new Font("Impact", Font.PLAIN, 22));
    g2.setColor(Color.CYAN); 
    g2.drawString("DETAILED RECORDS", labelX, y); 
    y += 35;
    
    g2.setFont(new Font("Monospaced", Font.BOLD, 15));
    g2.setColor(Color.WHITE);
    g2.drawString("Visit the ACHIEVEMENTS menu for detailed fruit tracking!", labelX, y);
    String[] fNames = {"CHERRY", "STRAWBERRY", "APPLE", "BANANA", "ORANGE", "BELL", "KEY", "STAR", "HEART", "CROWN"};
int startY = y; 

for (int i = 0; i < 5; i++) {
    // Venstre kolonne (Frugt 0-4) 
    drawFruitStatRow(g2, fNames[i], careerFruits[i], i + 1, 120, startY += 35);
    
    // Højre kolonne (Frugt 5-9) 
    drawFruitStatRow(g2, fNames[i+5], careerFruits[i+5], i + 6, 420, startY);

}
}

String currentMap = equippedMap.equals("PINK_MAP") ? "PINK_MAP" : "DEFAULT";
String currentGreen = equippedMap.equals("GREEN_MAP") ? "GREEN_MAP" : "DEFAULT";


private void drawStoreItem(Graphics2D g2, String name, int price, int x, int y, String current, String id) {
    boolean owned = ownedItems.contains(id); //tjekker om den er ejet
    boolean equipped = current.equals(id); //tjekker om den er udstyret

    // Kassen
    g2.setColor(equipped ? new Color(0, 100, 0) : new Color(40, 40, 40));
    g2.fillRoundRect(x, y, 220, 150, 15, 15);
    g2.setColor(equipped ? Color.GREEN : Color.CYAN);
    g2.drawRoundRect(x, y, 220, 150, 15, 15);

    // tegning til indholdet aka ikoner
    int ix = x + 140, iy = y + 30; // Ikon-position
    if (id.equals("BLUE")) { g2.setColor(new Color(0, 150, 255)); g2.fillArc(ix, iy, 50, 50, 45, 270); }
    else if (id.equals("RED")) { g2.setColor(Color.RED); g2.fillArc(ix, iy, 50, 50, 45, 270); }
else if (id.equals("PINK_MLP")) {
    Color bodyP = new Color(255, 145, 200); 
    Color maneP = new Color(230, 30, 130);  
    
    // pinkie pie krop
    g2.setColor(bodyP);
    g2.fillOval(ix, iy + 12, 35, 22);      // Krop
    g2.fillRoundRect(ix + 5, iy + 25, 6, 18, 5, 5);  // Forben
    g2.fillRoundRect(ix + 18, iy + 25, 6, 18, 5, 5); // Bagben
    
    // halee
    g2.setColor(maneP);
    g2.fillOval(ix - 12, iy + 5, 20, 20); // Hale-krølle 1
    g2.fillOval(ix - 8, iy + 15, 18, 18); // Hale-krølle 2
    
    // hoved og "ører"
    g2.setColor(bodyP);
    g2.fillOval(ix + 22, iy + 2, 22, 22); // Hoved
    g2.fillPolygon(new int[]{ix+25, ix+30, ix+32}, new int[]{iy+5, iy-5, iy+5}, 3); // Øre
    
    // hår
    g2.setColor(maneP);
    g2.fillOval(ix + 20, iy - 5, 15, 15); // Top-krølle
    g2.fillOval(ix + 35, iy - 2, 12, 12); // Pande-hår
    g2.fillOval(ix + 18, iy + 8, 12, 15); // Side-hår
    
    // kæmpe øjne du
    g2.setColor(Color.WHITE);
    g2.fillOval(ix + 33, iy + 6, 9, 11);
    g2.setColor(new Color(0, 150, 255)); // Blå iris
    g2.fillOval(ix + 36, iy + 8, 5, 7);
    g2.setColor(Color.BLACK);
    g2.fillOval(ix + 38, iy + 10, 2, 3); // Pupil
}

    else if (id.equals("ROBOT")) { g2.setColor(Color.LIGHT_GRAY); g2.fillRect(ix, iy, 45, 45); g2.setColor(Color.RED); g2.fillRect(ix+5, iy+10, 10, 5); g2.fillRect(ix+30, iy+10, 10, 5); }
    else if (id.equals("PINK_MAP") || id.equals("GREEN")) { 
        g2.setColor(id.equals("PINK_MAP") ? new Color(255, 105, 180) : Color.GREEN);
        g2.setStroke(new BasicStroke(3)); g2.drawRect(ix, iy, 40, 40); 
    }

    // Tekst
    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Arial", Font.BOLD, 18));
    g2.drawString(name, x + 20, y + 40);
    g2.setFont(new Font("Impact", Font.PLAIN, 20));
    g2.drawString(owned ? (equipped ? "EQUIPPED" : "OWNED") : price + " COINS", x + 20, y + 90);
}




//frugtstatistikker i achievements
private void drawFruitStatRow(Graphics2D g2, String name, int count, int fLvl, int x, int y) {
    drawFruitVisuals(g2, x, y - 15, true, fLvl);
    
    // Tegn navnet
    g2.setColor(Color.WHITE);
    g2.drawString(name + ":", x + 35, y);
    
    // Tegn tallet i gult
    g2.setColor(Color.YELLOW);
    g2.drawString(String.valueOf(count), x + 210 - g2.getFontMetrics().stringWidth(String.valueOf(count)), y);
}



private void drawStatLine(Graphics2D g2, String label, String value, int x, int y) {
    g2.drawString(label, x, y);
    // Højrestiller værdien ved x=680
    int valWidth = g2.getFontMetrics().stringWidth(value);
    g2.drawString(value, 680 - valWidth, y);
}

    private void drawTitle(Graphics2D g2, String txt) {
    g2.setFont(new Font("Impact", Font.ITALIC, 85));
    int x = 420 - (g2.getFontMetrics().stringWidth(txt) / 2);
    int y = 220;

    // skygger og hovedtekst
    g2.setColor(new Color(150, 0, 0));
    g2.drawString(txt, x + 6, y + 6);
    g2.setColor(new Color(255, 50, 0));
    g2.drawString(txt, x + 3, y + 3);
    g2.setColor(Color.YELLOW);
    g2.drawString(txt, x, y);

    // remake rykker med ned 
    g2.setFont(new Font("Monospaced", Font.BOLD, 25));
    g2.setColor(new Color(255, 215, 0));
    String sub = "REMAKE";
    int subX = 420 - (g2.getFontMetrics().stringWidth(sub) / 2);
    g2.drawString(sub, subX, y + 50);
}



    private void drawArcadeBackground(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 40)); for (int i = 0; i < 940; i += 40) g2.drawLine(0, i, 840, i); for (int i = 0; i < 840; i += 40) g2.drawLine(i, 0, i, 940);
        g2.setColor(new Color(0, 50, 255)); g2.setStroke(new BasicStroke(10)); g2.drawRect(5, 5, 830, 930);
        g2.setColor(new Color(100, 150, 255)); g2.setStroke(new BasicStroke(2)); g2.drawRect(12, 12, 816, 916);
    }

    private void drawList(Graphics g, ArrayList<ScoreEntry> list, String t, boolean showMap) {
    Graphics2D g2 = (Graphics2D) g;
    
    // blå ramme og sort baggrund
    g2.setColor(new Color(0, 0, 0, 210));
    g2.fillRect(80, 200, 680, 600); // Baggrund
    g2.setColor(new Color(0, 150, 255));
    g2.setStroke(new BasicStroke(3));
    g2.drawRect(80, 200, 680, 600); // Ramme

    // titlen
    g2.setColor(Color.YELLOW);
    g2.setFont(new Font("Impact", Font.PLAIN, 45));
    String titleTxt = t.toUpperCase();
    g2.drawString(titleTxt, 420 - (g2.getFontMetrics().stringWidth(titleTxt) / 2), 100);

    // forskellige overskrifter for kolonnerne
    g2.setFont(new Font("Monospaced", Font.BOLD, 18));
    g2.setColor(Color.CYAN);
    g2.drawString("RANK", 110, 250);
    g2.drawString("PLAYER", 200, 250);
    g2.drawString("SCORE", 440, 250);
    g2.drawString("MAP", 580, 250);
    g2.drawLine(110, 265, 730, 265);

    // listen i sig selv og dens farve kode for top 3
    g2.setFont(new Font("Monospaced", Font.BOLD, 22));
    for (int i = 0; i < Math.min(list.size(), 10); i++) {
        ScoreEntry se = list.get(i);
        int y = 310 + (i * 45); 

        if (i == 0) g2.setColor(new Color(255, 215, 0));      // Guld
        else if (i == 1) g2.setColor(new Color(192, 192, 192)); // Sølv
        else if (i == 2) g2.setColor(new Color(205, 127, 50));  // Bronze
        else g2.setColor(Color.WHITE);

        // Tegn Rank ")
        g2.drawString(String.format("%2d.", i + 1), 110, y);
        
        // Tegn Navn 
        String name = se.name.toUpperCase();
        if (name.length() > 12) name = name.substring(0, 12);
        g2.drawString(name, 200, y);
        
        // Tegn Score
        g2.drawString(String.format("%7d", se.score), 440, y);
        
        // Tegn Map 
        if (showMap) {
            String mapName = se.mapName.toUpperCase();
            g2.drawString(mapName, 580, y);
        }
    }

    // Hvis listen er tom
    if (list.isEmpty()) {
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Arial", Font.ITALIC, 20));
        g2.drawString("NO RECORDS FOUND", 320, 450);
    }
}


private void drawFriendsList(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    // Baggrundsboks 
    g2.setColor(new Color(0, 0, 0, 210));
    g2.fillRect(120, 150, 600, 600);
    g2.setColor(new Color(0, 150, 255));
    g2.setStroke(new BasicStroke(3));
    g2.drawRect(120, 150, 600, 600);

    //Overskrift
    g2.setColor(Color.YELLOW);
    g2.setFont(new Font("Impact", Font.PLAIN, 45));
    g2.drawString("FRIEND LIST", 120, 120);

    //Tegn navn
    g2.setFont(new Font("Monospaced", Font.BOLD, 24));
    g2.setColor(Color.WHITE);
    
    if (justNamesOfFriends.isEmpty()) {
        g2.setColor(Color.GRAY);
        g2.drawString("NO FRIENDS ADDED YET", 280, 400);
    } else {
        for (int i = 0; i < Math.min(justNamesOfFriends.size(), 12); i++) {
            g2.setColor(Color.CYAN);
            g2.drawString((i + 1) + ".", 150, 220 + (i * 45));
            g2.setColor(Color.WHITE);
            g2.drawString(justNamesOfFriends.get(i), 220, 220 + (i * 45));
        }
    }
}

    private void drawOneTimeAch(Graphics2D g2, int mS, int maxNoDeath) {
    int yPos = 50;
    
    // dataindsamling for challenges
    boolean hasFriends = !justNamesOfFriends.isEmpty();
    boolean ghostBusterEver = false, maxedOutEver = false;
    boolean[] mapsPlayed = new boolean[3];
    int[] fruitsEatenTotal = new int[10];
    try (BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"))) {
        String l;
        while ((l = br.readLine()) != null) {
            String[] p = l.split(":");
            if (p.length < 3 || !p[0].equalsIgnoreCase(currentUser)) continue;
            for(int i=0; i<3; i++) if(p[2].equalsIgnoreCase(mapNames[i])) mapsPlayed[i] = true;
            if(p.length >= 18) for(int i=0; i<10; i++) fruitsEatenTotal[i] += Integer.parseInt(p[i+8]);
            if(p.length >= 20 && p[19].equals("1")) ghostBusterEver = true;
            if(p.length >= 21 && p[20].equals("1")) maxedOutEver = true;
        }
    } catch (Exception e) {}
    boolean globetrotter = mapsPlayed[0] && mapsPlayed[1] && mapsPlayed[2];
    boolean fruitSalad = true;
    for(int f : fruitsEatenTotal) if(f == 0) fruitSalad = false; //fruit salad kræver at man har spist mindst 1 af hver frugt i karrieren

    // challenges one time
    String[] descriptions = {
        "Create a user account", "Eat 4 ghosts in one frenzy", 
        "Reach the limit of 5 lives", "Add your first friend", 
        "Play on all three maps", "Taste all 10 fruit types"
    };
    boolean[] st = {!currentUser.isEmpty(), ghostBusterEver, maxedOutEver, hasFriends, globetrotter, fruitSalad}; 
    
    drawSimpleAchRow(g2, "SPECIAL CHALLENGES", descriptions, st, 80, yPos);

    // flawless victories one time
    yPos += 420; 
    String[] noDeathDesc = new String[10];
    boolean[] noDeathStats = new boolean[10];
    for(int i = 0; i < 10; i++) {
        noDeathDesc[i] = "Clear Level " + (i + 1) + " without dying";
        noDeathStats[i] = maxNoDeath >= (i + 1);
    }
    drawSimpleAchRow(g2, "FLAWLESS VICTORIES", noDeathDesc, noDeathStats, 80, yPos);
}

//tegne achievements med simple grønne eller grå kasser, og en titel over hver række
private void drawSimpleAchRow(Graphics2D g2, String title, String[] names, boolean[] stats, int x, int y) {
    g2.setColor(Color.CYAN);
    g2.setFont(new Font("Impact", Font.PLAIN, 22));
    g2.drawString(title, x, y - 25); 
    
    int size = 90, spacing = 50; 
    for (int i = 0; i < names.length; i++) {
        int row = i / 5;
        int col = i % 5;
        int px = x + (col * (size + spacing));
        int py = y + (row * (size + 85));
        
        boolean ok = stats[i];
        
        // Tegn kvadratet
        g2.setColor(ok ? new Color(0, 200, 0) : new Color(45, 45, 45));
        g2.fillRect(px, py, size, size);
        g2.setColor(ok ? Color.WHITE : Color.DARK_GRAY);
        g2.drawRect(px, py, size, size);
        
        // Ikon
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(ok ? Color.BLACK : Color.GRAY);
        g2.drawString(ok ? "✓" : "?", px + (size/2)-15, py + (size/2)+15);
        
        // Tekst 
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.setColor(Color.WHITE);
        String txt = names[i];
        if (txt.length() > 20) {
            String part1 = txt.substring(0, txt.lastIndexOf(" ", 15));
            String part2 = txt.substring(txt.lastIndexOf(" ", 15) + 1);
            g2.drawString(part1, px + (size/2) - (g2.getFontMetrics().stringWidth(part1)/2), py + size + 15);
            g2.drawString(part2, px + (size/2) - (g2.getFontMetrics().stringWidth(part2)/2), py + size + 28);
        } else {
            g2.drawString(txt, px + (size/2) - (g2.getFontMetrics().stringWidth(txt)/2), py + size + 15);
        }
    }
}

    private void startHeltForfra() {
    frightenedTimer = 0; 
    fruitActive = false;
    pelletsEatenInLevel = 0;
    fruit1Spawned = false;
    fruit2Spawned = false;
    sessionFruitCounts = new int[10];

    if (lives < 1 || score == 0) { 
        score = 0; lives = 3; level = 1; difficulty = 0.3; 
        sessionGhosts = 0; sessionFruits = 0;
        sessionStartTime = System.currentTimeMillis();
        levelStartTime = System.currentTimeMillis(); 
        fastestRound = 9999; 
    }
    
    for (int r = 0; r < 21; r++) System.arraycopy(allMaps[selectedMap][r], 0, bane[r], 0, 21);
    respawn();
}
    private void respawn() {
        pacX = 400; pacY = 600; directionX = 0; directionY = 0; nextDireX = 0; nextDireY = 0;
        ghosts.clear();
        // Alle spøgelser starter ved "døren" (400, 320)
        ghosts.add(new Ghost(400, 320, Color.RED, speed, 400, 320));
        ghosts.add(new Ghost(400, 320, Color.PINK, speed, 400, 320));
        ghosts.add(new Ghost(400, 320, Color.CYAN, speed, 400, 320));
        ghosts.add(new Ghost(400, 320, Color.ORANGE, speed, 400, 320));
        
        ghostsActiveCount = 1; 
        lastGhostSpawnTime = System.currentTimeMillis();
    }

    private void checkWin() {
    boolean dotsLeft = false;
    for (int r = 0; r < 21; r++) {
        for (int c = 0; c < 21; c++) {
            if (bane[r][c] == 2 || bane[r][c] == 3) dotsLeft = true;
        }
    }

    if (!dotsLeft) {
        int timeForThisRound = (int)((System.currentTimeMillis() - levelStartTime) / 1000);
        if (timeForThisRound > 0 && timeForThisRound < fastestRound) {
            fastestRound = timeForThisRound;
        }

        if (!diedThisLevel && level > highestNoDeathEver) highestNoDeathEver = level;

        if (lives < 5) lives++;
        if (lives == 5) {
            achMaxedOut = true; 
            if (level % 5 == 0) score += 2500;
        }

        score += diedThisLevel ? 500 : 1000;
        level++;
        difficulty += 0.1;
        diedThisLevel = false;
        levelStartTime = System.currentTimeMillis(); 
        startHeltForfra();
    }
    int roundBonus = 0;
if (level >= 1 && level <= 3) roundBonus = 1;
else if (level >= 4 && level <= 6) roundBonus = 5;
else if (level >= 7 && level <= 8) roundBonus = 10;
else if (level == 9) roundBonus = 25;
else if (level >= 10) roundBonus = 50;

coins += roundBonus;
updateUserData();
}


    private void drawControls(Graphics g) { //tegner inde i en menu med controls
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(120, 150, 600, 600);
        g2.setColor(new Color(0, 150, 255));
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(120, 150, 600, 600);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Impact", 0, 60));
        g.drawString("CONTROLS", 280, 120);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 30));
        g.drawString("W - UP", 350, 300);
        g.drawString("A - LEFT", 350, 360);
        g.drawString("S - DOWN", 350, 420);
        g.drawString("D - RIGHT", 350, 480);
        g.setColor(Color.RED);
        g.drawString("Q - QUIT GAME", 310, 580);
    }
    private void checkScore() {
    saveLeaderboards();
    
    loadLeaderboards(); 
    
    checkDailyDone();
    checkWeeklyProgress();
    
    retryBtn.setVisible(true);
    menuBtn.setVisible(true);
    
    repaint();
}

private void drawPlayerIcon(Graphics2D g2) {
    int x = 30, y = 30; 
    int iconSize = 60;  

    g2.setColor(Color.YELLOW);
    int startAngle = (mouthAngle/10) + 30;
    int arcAngle = 360 - (startAngle * 2);
    g2.fillArc(x, y, iconSize, iconSize, startAngle, arcAngle);
    
    int currentLevel = (totalAP / 100) + 1;
    g2.setFont(new Font("Impact", Font.PLAIN, 24));
    g2.setColor(Color.WHITE);
    g2.drawString("LVL " + currentLevel, x + iconSize + 15, y + 25);
    
    int progress = totalAP % 100; 
    int barW = 200, barH = 22;
    int fillW = (int)(barW * (progress / 100.0));
    
    // Ramme
    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(x + iconSize + 15, y + 35, barW, barH);
    
    // Grøn udfyldning 
    g2.setColor(new Color(50, 255, 50));
    g2.fillRect(x + iconSize + 15, y + 35, fillW, barH);
    
    // Lysende kant om baren
    g2.setColor(new Color(0, 150, 255));
    g2.setStroke(new BasicStroke(2));
    g2.drawRect(x + iconSize + 15, y + 35, barW, barH);
    
    // 4. ap tekst ved baren
    g2.setFont(new Font("Arial", Font.BOLD, 12));
    g2.setColor(Color.WHITE);
    String apText = progress + " / 100 AP";
    int textW = g2.getFontMetrics().stringWidth(apText);
    g2.drawString(apText, (x + iconSize + 15) + (barW/2) - (textW/2), y + 51);
}


private void drawPoints(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    // 1. baggrundboks
    g2.setColor(new Color(0, 0, 0, 220));
    g2.fillRect(100, 150, 640, 650);
    g2.setColor(new Color(0, 150, 255));
    g2.setStroke(new BasicStroke(3));
    g2.drawRect(100, 150, 640, 650);

    // overskrift
    g2.setColor(Color.YELLOW);
    g2.setFont(new Font("Impact", Font.PLAIN, 45));
    g2.drawString("POINT SYSTEM", 420 - (g2.getFontMetrics().stringWidth("POINT SYSTEM") / 2), 110);

    int x1 = 140, x2 = 420, y = 200, gap = 35;
    g2.setFont(new Font("Monospaced", Font.BOLD, 18));

    g2.setColor(Color.CYAN);
    g2.drawString("--- BASIC SCORING ---", x1, y);
    g2.setColor(Color.WHITE);
    g2.drawString("Small Pellet:    10 pts", x1, y += gap);
    g2.drawString("Power Pellet:    50 pts", x1, y += gap);
    g2.drawString("Ghost (Combo):   200-1600 pts", x1, y += gap);
    g2.drawString("Level Clear:     500-1000 pts", x1, y += gap);
    g2.drawString("Max Life Bonus:  2500 pts", x1, y += gap);

    y += 60;
    g2.setColor(Color.CYAN);
    g2.drawString("--- FRUIT VALUES ---", x1, y);
    y += 40;

    String[] fNames = {"Cherry", "Strawberry", "Apple", "Banana", "Orange", "Bell", "Key", "Star", "Heart", "Crown"};
    int[] fValues = {100, 250, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000};

    g2.setFont(new Font("Monospaced", Font.BOLD, 15));
    for (int i = 0; i < 5; i++) {
        drawFruitPointLine(g2, fNames[i], fValues[i], i + 1, x1, y + (i * 45));
        drawFruitPointLine(g2, fNames[i+5], fValues[i+5], i + 6, x2, y + (i * 45));
    }
}

private void drawFruitPointLine(Graphics2D g2, String name, int pts, int fLvl, int x, int y) {
    drawFruitVisuals(g2, x, y - 18, true, fLvl);
    
    g2.setColor(Color.WHITE);
    g2.drawString(name + ":", x + 35, y);
    g2.setColor(Color.YELLOW);
    String ptsStr = pts + " pts";
    g2.drawString(ptsStr, x + 230 - g2.getFontMetrics().stringWidth(ptsStr), y);
}


private void drawDetailedGhost(Graphics2D g2, int x, int y, Color c) {
    g2.setColor(c); g2.fillOval(x, y, 50, 45); g2.fillRect(x, y + 22, 50, 18);
    for (int i = 0; i < 3; i++) g2.fillOval(x + (i * 17), y + 35, 16, 12);
}


private String formatValue(int v) { //formaterer store tal i leaderboardet til fx 1.2K eller 3.4M for at det ikke skal fylde så meget   
    if (v >= 1000000) return (v / 1000000) + "M";
    if (v >= 1000) return (v / 1000) + "K";
    return String.valueOf(v);
} 

private void saveLeaderboards() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("leaderboard.txt", true))) {
        StringBuilder sb = new StringBuilder();

        // byg linjen i formatet: USER:SCORE:MAP:GHOSTS:FRUITS:LEVEL:MAXNODEATH:FRUIT1:FRUIT2:...:FRUIT10
        sb.append(currentUser).append(":")
          .append(score).append(":")
          .append(mapNames[selectedMap]).append(":")
          .append(sessionGhosts).append(":")
          .append(sessionFruits).append(":")
          .append(level).append(":");

        sb.append("0");

        // tilføj frugtstatistikkerne for denne session
        for (int count : sessionFruitCounts) {
            sb.append(":").append(count);
        }

        // gem efter frugtstatistikkerne om ghostbuster og maxed out er klaret i denne session
        pw.println(sb.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void handleAddFriend() {
        String fName = JOptionPane.showInputDialog(this, "Friends Username:").trim(); // tjekker om input er tomt
        if (fName == null || fName.isEmpty()) return;
        boolean exists = false;
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) { // tjekker om brugeren findes i systemet
            String l; while((l = br.readLine()) != null) if (l.split(":")[0].equalsIgnoreCase(fName)) { exists = true; break; }
        } catch (Exception e) {}
        if (!exists) { JOptionPane.showMessageDialog(this, "User Not Found!"); return; } // tjekker om de allerede er venner
        try (PrintWriter pw = new PrintWriter(new FileWriter("friends.txt", true))) {
            pw.println(currentUser + ":" + fName);
            JOptionPane.showMessageDialog(this, fName + "Has Been Added!");
        } catch (Exception e) {} 
    }

    private void loadFriendsLeaderboard() {
        friendsLeaderboard.clear();
        myFriends.clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader("friends.txt"))) {
            String l; // find alle dine venner og gem deres navne i myFriends-listen
            while ((l = br.readLine()) != null) {
                String[] p = l.split(":");
                if (p[0].equalsIgnoreCase(currentUser)) myFriends.add(p[1].toLowerCase());
            }
        } catch (Exception e) {}

        try (BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String l;
            while ((l = br.readLine()) != null) {
                String[] p = l.split(":");
                if (p.length < 3) continue;

                String name = p[0];
                String mapName = p[2];
                
                if ((myFriends.contains(name.toLowerCase()) || name.equalsIgnoreCase(currentUser))  
                    && mapName.equalsIgnoreCase(mapNames[selectedMap])) {
                    friendsLeaderboard.add(new ScoreEntry(name, Integer.parseInt(p[1]), mapName));
                }
            }
            friendsLeaderboard.sort((a, b) -> b.score - a.score);
        } catch (Exception e) {}
    }

    private void loadOnlyFriendNames() {
    justNamesOfFriends.clear(); // rydder listen før den fylde med venner
    try (BufferedReader br = new BufferedReader(new FileReader("friends.txt"))) {
        String l;
        while ((l = br.readLine()) != null) {
            String[] p = l.split(":");
            if (p[0].equalsIgnoreCase(currentUser)) {
                justNamesOfFriends.add(p[1].toUpperCase());
            }
        }
    } catch (Exception e) {}
    
}


    private void spawnFruit() {
    Random rand = new Random();
    while (true) {
        int r = rand.nextInt(21);
        int c = rand.nextInt(21);
        // Sørg for at den kun lander på en sti (0) og ikke i en væg (1)
        if (bane[r][c] == 0) {
            fruitX = c;
            fruitY = r;
            fruitActive = true;
            break;
        }
    }
}


          class Ghost {
    int x, y, dx = 0, dy = 0, s, sX, sY; 
    Color c; 
    Random rnd = new Random();  // for at tilfældigt vælge retning når de er bange eller for at tilføje lidt uforudsigelighed i deres bevægelser
    boolean isFrightenedLocal = false;
    int targetX, targetY; 

    Ghost(int x, int y, Color c, int s, int sx, int sy) { 
        this.x = x; this.y = y; this.c = c; this.s = s; this.sX = sx; this.sY = sy;  // sX og sY er startpositionen, som bruges til at respawne spøgelset når det bliver spist
    }
    
    void respawn() {  //
        this.x = sX; this.y = sY; // når et spøgelse bliver spist, skal det respawne ved startpositionen og være i normal tilstand (ikke bange)
        this.dx = 0; this.dy = 0; 
        this.isFrightenedLocal = false; 
    }

   public void update(int targetX, int targetY, int[][] bane, int gridSize, double difficulty, int id, Ghost blinky) {
    if (x % gridSize == 0 && y % gridSize == 0) {
        int r = y / gridSize;
        int c = x / gridSize;

        // Bestem målet (Target) baseret på spøgelsets ID (Personlighed)
        int tx = targetX, ty = targetY;
        
        if (id == 1) { // PINKY: Går efter 4 felter foran Pac-Man (Ambush)
            tx = targetX + (directionX * 4);
            ty = targetY + (directionY * 4);
        } else if (id >= 2) { // INKY/CLYDE: Mere tilfældige/blandede
            if (Math.hypot(x - targetX, y - targetY) > 200) {
                tx = targetX; ty = targetY;
            } else {
                tx = 0; ty = 940; // Trækker sig tilbage til hjørnet
            }
        }

        int[][] dirs = {{0, -4}, {0, 4}, {-4, 0}, {4, 0}};
        int bestIdx = -1;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < 4; i++) {
            // ingen 180 graders vending
            if (dx == -dirs[i][0] && dy == -dirs[i][1]) continue;

            int nr = r + (dirs[i][1] / 4);
            int nc = c + (dirs[i][0] / 4);

            if (nr >= 0 && nr < 21 && nc >= 0 && nc < 21 && bane[nr][nc] != 1) {
                double dist = Math.hypot((x + dirs[i][0]) - tx, (y + dirs[i][1]) - ty);
                
                // Difficulty: Jo højere, jo sjældnere tager de en tilfældig vej
                if (Math.random() > difficulty) dist = Math.random() * 1000;

                if (dist < minDistance) {
                    minDistance = dist;
                    bestIdx = i;
                }
            }
        }

        if (bestIdx != -1) {
          
            dx = dirs[bestIdx][0];
            dy = dirs[bestIdx][1];
        } else {
            dx = -dx; dy = -dy; // Vend om hvis fanget
        }
    }
    x += dx;
    y += dy;
}





    void draw(Graphics g, int t, String skin) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //
        
        if (isFrightenedLocal) {
            g2.setColor((t < 100 && (t / 10) % 2 == 0) ? Color.WHITE : new Color(33, 33, 255)); // når spøgelset er bange og der er under 100 frames tilbage, blinker det mellem blå og hvidt for at advare spilleren om at det snart ikke er bange længere
        } else {
            g2.setColor(skin.equals("ROBOT") ? Color.LIGHT_GRAY : c);
        }

        g2.fillOval(x + 4, y + 4, 32, 32); 
        g2.fillRect(x + 4, y + 20, 32, 12); 
        for (int i = 0; i < 3; i++) g2.fillOval(x + 4 + (i * 11), y + 28, 11, 10);

        g2.setColor(Color.WHITE);
        g2.fillOval(x + 8, y + 10, 10, 12); 
        g2.fillOval(x + 22, y + 10, 10, 12); 

        if (!isFrightenedLocal) {
            g2.setColor(new Color(33, 33, 255)); 
            int pX = (dx > 0) ? 2 : (dx < 0 ? -2 : 0);
            int pY = (dy > 0) ? 2 : (dy < 0 ? -2 : 0);
            g2.fillOval(x + 11 + pX, y + 14 + pY, 5, 5);
            g2.fillOval(x + 25 + pX, y + 14 + pY, 5, 5);
        }
    }
}

   private boolean checkOwned(String id) { // tjekker om du allerede ejer et item i butikken, for at undgå at du kan købe det flere gange
    if (ownedItems == null) return false;
    return ownedItems.toLowerCase().contains(id.toLowerCase().trim());
}


    private void addOwnedItem(String id) { // tilføjer et item til din ownedItems-liste, som bruges til at holde styr på hvilke items du har købt i butikken
        if (!checkOwned(id)) ownedItems += "," + id;
    }
public int calcAP(int v, int[] m) { 
    int s = 0; 
    for(int i=0; i<m.length; i++) if(v >= m[i]) s += (i==0?0:(i<5?25:(i<9?50:100))); 
    return s; 
}

private void drawStore(Graphics2D g2) { // vis i butikken om du ejer nogle items og har dem equipped så du ikke skal gætte
    String currentMap = equippedMap.equals("PINK_MAP") ? "PINK_MAP" : "DEFAULT";
String currentGreen = equippedMap.equals("GREEN_MAP") ? "GREEN_MAP" : "DEFAULT";
    g2.setColor(Color.BLACK);
    g2.fillRect(0, 0, 840, 250);
    drawTitle(g2, "ARCADE STORE");
    g2.setFont(new Font("Impact", Font.PLAIN, 30));
    g2.setColor(Color.YELLOW);
    g2.drawString("YOUR COINS: " + coins, 320, 220);

    Shape originalClip = g2.getClip();
    g2.setClip(50, 250, 740, 600); 
    g2.translate(0, -storeScrollY + 250); 
    drawStoreItem(g2, "BLUE PAC", 50, 100, 50, pacColor, "BLUE");
    drawStoreItem(g2, "RED PAC", 50, 420, 50, pacColor, "RED");
    
    drawStoreItem(g2, "MLP PINK", 100, 100, 250, pacColor, "PINK_MLP");
    drawStoreItem(g2, "ROBOTS", 150, 420, 250, ghostSkin, "ROBOT");
    
   drawStoreItem(g2, "PINK MAP", 200, 100, 450, currentMap, "PINK_MAP");
drawStoreItem(g2, "GREEN MAP", 200, 420, 450, currentGreen, "GREEN_MAP");

    g2.translate(0, storeScrollY - 250);
    g2.setClip(originalClip);
}



private void updateUserData() {
    ArrayList<String> lines = new ArrayList<>();
    
    try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) { // åbner filen for at læse den eksisterende data
        String l;
        while ((l = br.readLine()) != null) {
            String[] p = l.split(":");
            if (p[0].equalsIgnoreCase(currentUser)) {
                // Her bygger vi den nye linje med alle 8 parametre
                // Vi bruger p[0] (navn) og p[1] (kodeord) fra filen
                String updatedLine = p[0] + ":" + p[1] + ":" + coins + ":" + pacColor + ":" + 
                                     ghostSkin + ":" + ownedItems + ":" + equippedMap + ":" + lifetimeCoins;
                lines.add(updatedLine);
            } else {
                lines.add(l);
            }
        }
    } catch (Exception e) { e.printStackTrace(); }

    // skriv alt inde i filen
    try (PrintWriter pw = new PrintWriter(new FileWriter("users.txt"))) {
        for (String s : lines) {
            pw.println(s);
        }
    } catch (Exception e) { e.printStackTrace(); }
}


private void applySkins() {
    if (equippedMap.equals("PINK_MAP")) {
        wallColor = new Color(255, 105, 180);
    } else {
        wallColor = new Color(0, 150, 255);
    }
}
private void generateWeeklyChallenges() {
    java.util.Calendar cal = java.util.Calendar.getInstance(); // bruger uge-nummer og år til at skabe en unik "nøgle" for hver uge, så udfordringerne ændrer sig hver
    int weekSeed = cal.get(java.util.Calendar.WEEK_OF_YEAR) + cal.get(java.util.Calendar.YEAR);
    Random rnd = new Random(weekSeed);
    
    weeklyChallengeGoals[0] = 50 + rnd.nextInt(50); 
    weeklyChallengeDescs[0] = "Eat " + weeklyChallengeGoals[0] + " Ghosts Total";
    
    weeklyChallengeGoals[1] = 15 + rnd.nextInt(10);
    weeklyChallengeDescs[1] = "Complete " + weeklyChallengeGoals[1] + " levels total";
    
    weeklyChallengeGoals[2] = 10 + rnd.nextInt(10);
    weeklyChallengeDescs[2] = "Eat " + weeklyChallengeGoals[2] + " fruits this week";

    weeklyChallengeGoals[3] = 5 + rnd.nextInt(5);
    weeklyChallengeDescs[3] = "Play " + weeklyChallengeGoals[3] + " complete games";

    weeklyChallengeGoals[4] = 40000 + rnd.nextInt(60001); 
weeklyChallengeDescs[4] = "Get " + formatValue(weeklyChallengeGoals[4]) + " points in one game";
    
    loadWeeklyStatus(); 
}

private void saveWeeklyStatus() {
    try (PrintWriter pw = new PrintWriter(new FileWriter("weekly.txt"))) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        // Skaber uge-nøgle: f.eks. "17-2024"
        String weekKey = cal.get(java.util.Calendar.WEEK_OF_YEAR) + "-" + cal.get(java.util.Calendar.YEAR);
        
        pw.print(currentUser + ":" + weekKey);
        for (int i = 0; i < 5; i++) {
            pw.print(":" + (weeklyChallengeDone[i] ? "1" : "0"));
        }
        pw.println();
    } catch (Exception e) {}
}


private void loadWeeklyStatus() {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    String currentWeek = cal.get(java.util.Calendar.WEEK_OF_YEAR) + "-" + cal.get(java.util.Calendar.YEAR);

    try (BufferedReader br = new BufferedReader(new FileReader("weekly.txt"))) { // åbner filen og læser linje for linje for at finde den linje der matcher både brugernavn og uge-nøgle, og indlæser så status for de 5 udfordringer
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(":");
            // Tjekker bruger og uge-nøgle for at finde den rigtige linje
            if (p[0].equalsIgnoreCase(currentUser) && p[1].equals(currentWeek)) {
                for (int i = 0; i < 5; i++) {
                    weeklyChallengeDone[i] = p[i + 2].equals("1");
                }
                System.out.println("LOG: Weekly status indlæst for " + currentUser);
                return;
            }
        }
    } catch (Exception e) { /* Fil findes ikke endnu */ }
}

    private void checkWeeklyProgress() {
    for (int i = 0; i < 5; i++) {
        if (weeklyChallengeDone[i]) continue;
        
        boolean success = false;
        // Vi bruger tallene direkte fra loadLeaderboards()
        if (i == 0 && careerGhosts >= weeklyChallengeGoals[i]) success = true;
        if (i == 1 && careerRounds >= weeklyChallengeGoals[i]) success = true;
        if (i == 2 && careerFruitsTotal >= weeklyChallengeGoals[i]) success = true; 
        if (i == 3 && careerGms >= weeklyChallengeGoals[i]) success = true;
        if (i == 4 && score >= weeklyChallengeGoals[i]) success = true; 

        if (success) {
            weeklyChallengeDone[i] = true;
            coins += weeklyChallengeRewards[i];
            totalAP += (i == 4 ? 100 : 50);
            saveWeeklyStatus();
            JOptionPane.showMessageDialog(this, "WEEKLY CHALLENGE FÆRDIG!\nModtaget " + weeklyChallengeRewards[i] + " Coins");
        }
    }
}



private void checkDailyDone() {
    if (dailyChallengeDone) return;
    
    boolean won = false;
    if (dailyChallengeType.equals("GHOSTS") && sessionGhosts >= dailyChallengeGoal) won = true;
    if (dailyChallengeType.equals("POINTS") && score >= dailyChallengeGoal) won = true;
    if (dailyChallengeType.equals("LEVELS") && level >= dailyChallengeGoal) won = true;
    
    if (won) {
        dailyChallengeDone = true;
        totalAP += 25;
        coins += 25;
        saveDailyCompletion();
        JOptionPane.showMessageDialog(this, "DAILY CHALLENGE FÆRDIG!\n+25 AP & +25 Coins");
    }
}
private void drawPinkPonySkin(Graphics2D g2) {
    // Farver til Pinkie Pie
    Color bodyP = new Color(255, 145, 200); 
    Color maneP = new Color(230, 30, 130);
    
    // Bestem retning (vender ansigtet mod højre eller venstre)
    int flip = (directionX >= 0) ? 1 : -1;
    
    // krop
    g2.setColor(bodyP);
    g2.fillOval(pacX + 2, pacY + 12, 30, 20); 
    
    // ben
    g2.fillRoundRect(pacX + 10 + (flip * 5), pacY + 25, 6, 12, 5, 5); 
    g2.fillRoundRect(pacX + 10 - (flip * 5), pacY + 25, 6, 12, 5, 5);
    
    // hale
    g2.setColor(maneP);
    int tailX = (flip == 1) ? pacX - 8 : pacX + 25;
    g2.fillOval(tailX, pacY + 10, 18, 18); 
    
    // hoved
    g2.setColor(bodyP);
    int headX = (flip == 1) ? pacX + 18 : pacX - 5;
    g2.fillOval(headX, pacY + 2, 22, 22); 
    
    // hår
    g2.setColor(maneP);
    int maneX = (flip == 1) ? headX - 5 : headX + 12;
    g2.fillOval(maneX, pacY - 4, 15, 15); 
    
    // øje
    g2.setColor(Color.WHITE);
    int eyeX = (flip == 1) ? headX + 12 : headX + 2;
    g2.fillOval(eyeX, pacY + 6, 7, 9);
    g2.setColor(Color.BLACK);
    g2.fillOval(eyeX + (flip == 1 ? 3 : 1), pacY + 8, 3, 5);
}


} 
