import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

public class Panel extends JPanel implements ActionListener {
	private final Board board;
    private final int MAX_LENGTH;
    private final int MAX_POS;
    private final int DELAY = 100;
	private final int HEAD = 1;
	private final int BODY = 2;
	private final int FOOD = 3;
    private final Position pos[];

    private int length;
    private int food_x;
    private int food_y;
	
    private int LEFT = 1;
    private int RIGHT = 2;
    private int UP = 4;
    private int DOWN = 8;
	private int move = RIGHT;
    private boolean alive = true;
	private boolean moved = false;

    private Timer timer;
    private Image body;
    private Image food;
    private Image head;

	private String msg = "Game Over";

    public Panel(Board board) {
    	this.board=board;
		this.MAX_LENGTH = board.getWidth()*board.getHeight();
		this.MAX_POS = (board.getWidth()/board.getPixel())-2;
		this.pos = new Position[MAX_LENGTH];
		for(int i=0;i<pos.length;i++)
			pos[i] = new Position();
		
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(board.getWidth(), board.getHeight()));
        head = setupImages(HEAD);
        body = setupImages(BODY);
        food = setupImages(FOOD);
        initGame();
    }

    private Image setupImages(int type) {     	
		BufferedImage image = new BufferedImage(board.getPixel(), board.getPixel(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		switch(type) {
		case BODY:
	        drawCircle(g2, 0, 0, board.getPixel(), Color.blue);
	        break;
	    case HEAD:
	        drawCircle(g2, 0, 0, board.getPixel(), Color.red);
	        break;
	    case FOOD:
	        drawFood(g2);
	        break;
		}
		return image;
    }

    private void initGame() {
        length = 3;

        for (int i=0; i<length; i++) {
            pos[i].setX(board.getPixel()*5 - i * 10);
            pos[i].setY(board.getPixel()*5);
        }
        
        randomFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (alive) {
            g.drawImage(food, food_x, food_y, this);
			g.drawImage(head, pos[0].getX(), pos[0].getY(), this);
            for (int i = 1; i < length; i++)
				g.drawImage(body, pos[i].getX(), pos[i].getY(), this);

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (board.getWidth()- metr.stringWidth(msg)) / 2, board.getHeight() / 2);
    }

    private void randomFood() {
        int r = (int) (Math.random() * MAX_POS)+1;
        food_x = ((r * board.getPixel()));

        r = (int) (Math.random() * MAX_POS)+1;
        food_y = ((r * board.getPixel()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (alive) {
            checkFood();
            checkCollision();            
			movement();
			repaint();
		}
	}

	public void movement() {		
		for (int i=length; i>0; i--) {
			pos[i].setX(pos[i-1].getX());
			pos[i].setY(pos[i-1].getY()) ;
		}

		if (move == LEFT)
			pos[0].setX(pos[0].getX()- board.getPixel());
		if (move == RIGHT)
			pos[0].setX(pos[0].getX()+ board.getPixel());
		if (move == UP)
			pos[0].setY(pos[0].getY()- board.getPixel());
		if (move == DOWN)
			pos[0].setY(pos[0].getY()+ board.getPixel());
		moved=true;
	}

	public void checkCollision() {		
		for (int i=length; i>0; i--) {
			if (pos[0].getX() == pos[i].getX() && pos[0].getY() == pos[i].getY()) { 
				alive = false;
			}
		}
		if (pos[0].getY() >= board.getHeight())
			alive = false;
		if (pos[0].getY() < 0)
			alive = false;
		if (pos[0].getX() >= board.getWidth())
			alive = false;
		if (pos[0].getX() < 0)
			alive = false;
		
		if (!alive)
			timer.stop();
	}

	public void checkFood() {	
		if ((pos[0].getX() == food_x) && (pos[0].getY() == food_y)) {
			length++;
			randomFood();
			int d = DELAY-length;
			if(d < 40) d = 40;
			timer.setDelay(d);
		}
	}
	
	private void drawCircle(Graphics2D g2, int x, int y, int size, Color color) {
	    g2.setPaint(color);
	    g2.fillOval(x, y, size, size);
	    g2.dispose();
	}

	private void drawFood(Graphics2D g2) {
	    drawCircle(g2, 2, 2, board.getPixel() - 2, Color.green);
	    g2.setPaint(new Color(102, 51, 0));
	    int[] x = {1, board.getPixel() / 2, board.getPixel() / 2};
	    int[] y = {1, board.getPixel() / 2, 1};
	    g2.fillPolygon(x, y, 3);
	    g2.dispose();
	}

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
			if(!moved) return;
            int key = e.getKeyCode();
			switch(key) {
				case KeyEvent.VK_LEFT:  if(move!=RIGHT) move = LEFT; break;
				case KeyEvent.VK_RIGHT: if(move!=LEFT) 	move = RIGHT; break;
				case KeyEvent.VK_UP:    if(move!=DOWN) 	move = UP; break;
				case KeyEvent.VK_DOWN:  if(move!=UP)   	move = DOWN; break;
			}
			moved = false;
        }
    }
}
