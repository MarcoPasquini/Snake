import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;

    Random random;

    Timer gameLoop;

    int speedX;
    int speedY;
    boolean isGameOver = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        random = new Random();
        generateFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();

        speedX = 0;
        speedY = 0;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);

        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        
        for(int i=0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        if(speedX == 0 && speedY == 0){
            g.setColor(Color.green);
            g.drawString("Press any arrow key to start", tileSize*8 - 9, tileSize*12);
        }
        if(isGameOver){
            g.setColor(Color.red);
            g.drawString("Game Over with score: " + String.valueOf(snakeBody.size()), tileSize - 15, tileSize);
            g.drawString("Press enter to start a new game", tileSize*8 - 9, tileSize*12);
        }else{
            g.setColor(Color.green);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 15, tileSize);
        }
    }
    public void generateFood(){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile snake, Tile food){
        return (snake.x == food.x && snake.y == food.y);
    }

    public void move(){

        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            generateFood();
        }
        for(int i=snakeBody.size()-1; i>=0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += speedX;
        snakeHead.y += speedY;

        for(int i = 0; i<snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart))
                isGameOver = true;
        }

        if(snakeHead.x*tileSize < 0 || snakeHead.y*tileSize < 0 || snakeHead.x*tileSize >= boardWidth || snakeHead.y*tileSize >= boardHeight)
            isGameOver = true;
    }
    public void reset(){
        speedX = 0;
        speedY = 0;
        snakeHead.x = 5;
        snakeHead.y = 5;
        snakeBody.clear();
        generateFood();
        isGameOver = false;
        gameLoop.start();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(isGameOver)
            gameLoop.stop();
        move();
        repaint();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && speedY != 1){
            speedX = 0;
            speedY = -1;
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN && speedY != -1){
            speedX = 0;
            speedY = 1;
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT && speedX != 1){
            speedX = -1;
            speedY = 0;
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT && speedX != -1){
            speedX = 1;
            speedY = 0;
        }else if(e.getKeyCode() == KeyEvent.VK_ENTER && isGameOver){
            reset();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
