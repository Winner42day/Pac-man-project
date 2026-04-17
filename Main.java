import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame Window = new JFrame("Pacman Spil");
        GameCanvas Canvas = new GameCanvas();

        Window.add(Canvas);
        Window.setSize(860, 900);
        Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Window.setLocationRelativeTo(null);
        Window.setResizable(false);
        Window.setVisible(true);
    }
}