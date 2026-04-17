import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameCanvas extends JPanel {
    int pacX = 400;
    int pacY = 600;
    int nextDireX = 0;
    int nextDireY = 0;
    int nextAngle = 30;
    int speed = 5;
    int gridSize = 40;
    int eatAngle = 30;
    int directionX = 0;
    int directionY = 0;
    int mouthAngle = 300;
    int mouthSpeed = 4;
    Timer gameTimer;
    int[][] bane = {
        {1, 1, 1, 1, 1, 1, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1, 3, 2, 2, 2, 2, 2, 2,2,2,1,2,2,2,2,2,2,2,2,3,1},
        {1, 2, 1, 1, 2, 1, 1, 1,1,2,1,2,1,1,1,1,2,1,1,2,1},
        {1, 2, 2, 2, 2, 2, 2, 2,2,2,2,2,2,2,2,2,2,2,2,2,1},
        {1, 2, 1, 1, 2, 1, 2, 1,1,1,1,1,1,1,2,1,2,1,1,2,1},
        {1, 2, 2, 2, 2, 1, 2, 2,2,2,1,2,2,2,2,1,2,2,2,2,1},
        {1, 1, 1, 1, 2, 1, 1, 1,1,2,1,2,1,1,1,1,2,1,1,1,1},
        {0, 0, 0, 1, 2, 1, 0, 0,0,0,0,0,0,0,0,1,2,1,0,0,0},
        {1, 1, 1, 1, 2, 1, 0, 1,1,1,0,1,1,1,0,1,2,1,1,1,1},
        {1, 0, 0, 0, 2, 0, 0, 1,0,0,0,0,0,1,0,0,2,0,0,0,1},
        {1, 1, 1, 1, 2, 1, 0, 1,1,1,1,1,1,1,0,1,2,1,1,1,1},
        {0, 0, 0, 1, 2, 1, 0, 0,0,0,0,0,0,0,0,1,2,1,0,0,0},
        {1, 1, 1, 1, 2, 1, 0, 1,1,1,1,1,1,1,0,1,2,1,1,1,1},
        {1, 2, 2, 2, 2, 2, 2, 2,2,2,1,2,2,2,2,2,2,2,2,2,1},
        {1, 2, 1, 1, 2, 1, 1, 1,1,2,1,2,1,1,1,1,2,1,1,2,1},
        {1, 2, 2, 1, 2, 2, 2, 2,2,2,0,2,2,2,2,2,2,1,2,2,1},
        {1, 1, 2, 1, 2, 1, 2, 1,1,1,1,1,1,1,2,1,2,1,2,1,1},
        {1, 2, 2, 2, 2, 1, 2, 2,2,2,1,2,2,2,2,1,2,2,2,2,1},
        {1, 2, 1, 1, 1, 1, 1, 1,1,2,1,2,1,1,1,1,1,1,1,2,1},
        {1, 3, 2, 2, 2, 2, 2, 2,2,2,2,2,2,2,2,2,2,2,2,3,1},
        {1, 1, 1, 1, 1, 1, 1, 1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    public GameCanvas(){
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) {nextDireX=0;    nextDireY= -speed; nextAngle=120;} 
                if (key == KeyEvent.VK_S) {nextDireX=0;    nextDireY= speed;  nextAngle =300;}
                if (key == KeyEvent.VK_A) {nextDireX= -speed;  nextDireY= 0;  nextAngle = 210;}
                if (key == KeyEvent.VK_D) {nextDireX=speed;    nextDireY= 0;  nextAngle = 30;}
            }
        });

        Timer motor = new Timer(20,timerEvent -> {
            if (pacX % gridSize == 0 && pacY % gridSize ==0) {
                int checkCol =(pacX + nextDireX+20) /gridSize;
                int checkRow = (pacY + nextDireY+20)/gridSize;

                if (checkRow>=0 && checkRow< bane.length && checkCol >=0 && checkCol < bane[0].length){
                    if(bane[checkRow][checkCol]!=1){
                        directionX = nextDireX;
                        directionY = nextDireY;
                        eatAngle = nextAngle;
                    }
                }
            }
            int nextX = pacX + directionX;
    int nextY = pacY + directionY;

    int s1X = nextX, s1Y = nextY;
    int s2X = nextX, s2Y = nextY;

    if (directionX > 0) { // Højre
        s1X = nextX + 35; s1Y = nextY + 5;
        s2X = nextX + 35; s2Y = nextY + 30;
    } else if (directionX < 0) { // Venstre
        s1X = nextX + 2;  s1Y = nextY + 5;
        s2X = nextX + 2;  s2Y = nextY + 30;
    } else if (directionY > 0) { // Ned
        s1X = nextX + 5;  s1Y = nextY + 35;
        s2X = nextX + 30; s2Y = nextY + 35;
    } else if (directionY < 0) { // Op
        s1X = nextX + 5;  s1Y = nextY + 2;
        s2X = nextX + 30; s2Y = nextY + 2;
    }

    int r1 = s1Y / gridSize, c1 = s1X / gridSize;
    int r2 = s2Y / gridSize, c2 = s2X / gridSize;

    if (r1 >= 0 && r1 < bane.length && c1 >= 0 && c1 < bane[0].length &&
        r2 >= 0 && r2 < bane.length && c2 >= 0 && c2 < bane[0].length) {
        
        if (bane[r1][c1] != 1 && bane[r2][c2] != 1) {

            pacX = nextX;
            pacY = nextY;

            int eatR = (pacY + 20) / gridSize;
            int eatC = (pacX + 20) / gridSize;
            if (bane[eatR][eatC] == 2 || bane[eatR][eatC] == 3) bane[eatR][eatC] = 0;
        } else {
            directionX = 0;
            directionY = 0;
        }
    }
    if(directionX!=0 || directionY!=0) {mouthAngle += mouthSpeed;
        if (mouthAngle >= 360 || mouthAngle <= 300) {mouthSpeed = -mouthSpeed;}
    }
    repaint();
});
        motor.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int startAngle = eatAngle + (360-mouthAngle) / 2;
        
    
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, 840, 840);

    for (int r = 0; r < bane.length; r++) { 
        for (int k = 0; k < bane[r].length; k++) { 
            
            int x = k * gridSize;
            int y = r * gridSize;

            if (bane[r][k] == 1) { 
                g.setColor(Color.BLUE);
                g.fillRect(x, y, gridSize, gridSize);
            } else if (bane[r][k] == 2) { 
                g.setColor(Color.WHITE);
                g.fillOval(x + 15, y + 15, 10, 10);
            } else if (bane[r][k] == 3) { 
                g.setColor(Color.WHITE);
                g.fillOval(x + 8, y + 8, 25, 25);
            }
        }
    }


        g.setColor(Color.YELLOW);
        g.fillArc(pacX+5,pacY+5, 30, 30, startAngle, mouthAngle); 

        Color[] GhostColors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};

        for (int i = 0; i < GhostColors.length; i++) {
            g.setColor(GhostColors[i]);
                int x = 330 + (i * 50);
                int y = 360;
                g.fillOval(x, y, 30, 30);          
                g.fillRect(x, y + 15, 30, 20);  

    
                g.fillOval(x - 0, y + 30, 8, 8); 
                g.fillOval(x + 8, y + 30, 8, 8);
                g.fillOval(x + 15, y + 30, 8, 8);
                g.fillOval(x + 22, y + 30, 8, 8);

                g.setColor(Color.WHITE);
                g.fillOval(x + 5, y + 10, 10, 12);
                g.fillOval(x + 18, y + 10, 10, 12);
                g.setColor(Color.BLUE);
                g.fillOval(x + 8, y + 13, 6, 8);
                g.fillOval(x + 23, y + 13, 6, 8);
            }
            
    }

}