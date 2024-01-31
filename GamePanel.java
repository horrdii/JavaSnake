import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS], y[] = new int[GAME_UNITS];
    int bodyParts = 6, applesEaten = 0;
    int appleX, appleY;
    char dir = 'r';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void newApple(){
        appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;

    }

    public void draw(Graphics g){

        if(running){

            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){

                for(int j = 0; j < SCREEN_HEIGHT/UNIT_SIZE; j++){
                    g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                }

                for(int j = 0; j < SCREEN_WIDTH/UNIT_SIZE; j++){
                    g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
                }

            }
        
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.green);
            g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
            g.setColor(Color.PINK);
            for(int i = 1; i < bodyParts; i++){

                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }

        } else{
            gameOver(g);
        }

    }

    public void move(){
        for(int i = bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(dir){
        case 'u':
            y[0] = y[0] - UNIT_SIZE;
            break;
        case 'd':
            y[0] = y[0] + UNIT_SIZE;
            break;
        case 'l':
            x[0] = x[0] - UNIT_SIZE;
            break;
        case 'r':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){

        for(int i = 1; i < bodyParts; i++){
            if(x[0] == x[i] && y[0] == y[i] ||
                x[0] < 0 || x[0] > SCREEN_WIDTH ||
                y[0] < 0 || y[0] > SCREEN_HEIGHT ){
                running = false;
            }
        }
        
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD, 75) );
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(running){
            move();
            checkCollisions();
            checkApple();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if( dir != 'r' )
                    dir = 'l';
                break;
            case KeyEvent.VK_RIGHT:
                if( dir != 'l' )
                    dir = 'r';
                break;
            case KeyEvent.VK_UP:
                if( dir != 'd' )
                    dir = 'u';
                break;
            case KeyEvent.VK_DOWN:
                if( dir != 'u' )
                    dir = 'd';
                break;
            }
        }
    }

}