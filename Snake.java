import java.util.LinkedList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Snake extends JPanel implements KeyListener {

	JFrame frame;

	int gWidth = 30;
	int gHeight = 30;
	int gSize = 20;
	// to fix offset b/w frame and panel
	int xOff;
	int yOff;

	long lastMove = System.currentTimeMillis();

	LinkedList<Point> snakePoints = new LinkedList<Point>();
	Point food = new Point(gWidth/2 + 3, gHeight/2);

	Color backgroundColor = new Color(240,255,250);
	Color snakeColor = new Color(0,255,0);
	Color foodColor = new Color(255,0,0);

	int keyCode;
	int moveKeyCode;

	boolean isGameOver = false;
	boolean runOnce = false;

	public Snake() {
		frame = new JFrame("budget snake");
		frame.setSize(gWidth * gSize, gHeight * gSize);
		frame.add(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addKeyListener(this);

		snakePoints.add(new Point(gWidth/2, gHeight/2));
		snakePoints.add(new Point(gWidth/2-1, gHeight/2));
		snakePoints.add(new Point(gWidth/2-2, gHeight/2));
	}

	public void paintComponent(Graphics g) {
		if(!runOnce) {			//scuffed things to fix offset with frame - allegedly there is something called pack() which is better
			xOff = frame.getWidth() - this.getWidth();
			yOff = frame.getHeight() - this.getHeight();
			frame.setSize(gWidth * gSize + xOff, gHeight * gSize + yOff);
			runOnce = true;
		}
		if(!isGameOver) {
			backgroundColor(g);

			drawBounds(g);

			drawSnake(g);

			drawFood(g);

			if(System.currentTimeMillis() - lastMove > 50) {
					moveSnake();
			//	lastMove = System.currentTimeMillis(); inside moveSnake();
			}

			checkCollisions(g);

			frame.repaint();
		}
		else {
			drawBounds(g);
			drawSnake(g);
			gameOver(g);
			if(keyCode==82) restart();	// r to restart
		}

		frame.repaint();
	}

	public void backgroundColor(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0,0,frame.getWidth(),frame.getHeight());
	}

	public void drawBounds(Graphics g) {
		g.setColor(Color.darkGray);

		g.drawRect(0, 0, gWidth * gSize, gHeight * gSize);
	}

	public void drawSnake(Graphics g) {
		g.setColor(snakeColor);
		for(Point p : snakePoints) {
			g.fillRect(p.x * gSize, p.y * gSize, gSize, gSize);
		}
		// head color
		g.setColor(new Color(0, 102, 0));

		Point head = snakePoints.getFirst();
		g.fillRect(head.x * gSize, head.y * gSize, gSize, gSize);

	}

	public void drawFood(Graphics g) {
		g.setColor(foodColor);
		g.fillRect(food.x * gSize, food.y * gSize, gSize, gSize);
	}

	public void moveSnake() {
		lastMove = System.currentTimeMillis();

		Point oldHead = snakePoints.getFirst();
		Point secondSegment = snakePoints.get(1);

		if(moveKeyCode == 87) { // up
			if(new Point(oldHead.x, oldHead.y-1).equals(secondSegment)) {
				moveKeyCode = 83;
				moveSnake();
				return;
			}

			snakePoints.addFirst(new Point(oldHead.x, oldHead.y-1));

			snakePoints.removeLast();
		}

		else if(moveKeyCode == 83) { // down
			if(new Point(oldHead.x, oldHead.y+1).equals(secondSegment)) {
				moveKeyCode = 87;
				moveSnake();
				return;
			}

			snakePoints.addFirst(new Point(oldHead.x, oldHead.y+1));

			snakePoints.removeLast();
		}

		else if(moveKeyCode == 65) { // left
			if(new Point(oldHead.x-1, oldHead.y).equals(secondSegment)) {
				moveKeyCode = 68;
				moveSnake();
				return;
			}
			snakePoints.addFirst(new Point(oldHead.x-1, oldHead.y));

			snakePoints.removeLast();
		}

		else if(moveKeyCode == 68) { // right
			if(new Point(oldHead.x+1, oldHead.y).equals(secondSegment)) {
				moveKeyCode = 65;
				moveSnake();
				return;
			}
			snakePoints.addFirst(new Point(oldHead.x+1, oldHead.y));

			snakePoints.removeLast();
		}

	}

	public void makeFood() {
		food = new Point( (int)(Math.random() * gWidth), (int)(Math.random() * gHeight));
	}

	public void increaseLength() {
		Point oldTail = snakePoints.getLast();
		snakePoints.addLast(new Point (oldTail.x, oldTail.y));
	}

	public void checkCollisions(Graphics g) {
		Point head = snakePoints.getFirst();

		if(head.x<0 || head.x >= gWidth) gameOver(g);

		if(head.y<0 || head.y >= gHeight) gameOver(g);

		for(int i = 1; i<snakePoints.size(); i++) {
			if(head.x==snakePoints.get(i).x && head.y==snakePoints.get(i).y) {
				gameOver(g);
				System.out.println("snake length: "+ snakePoints.size());
			}
		}


		if(head.x==food.x && head.y == food.y) {
			makeFood();
			for(int i = 0; i<((Math.random()*3) + 1); i++)
				increaseLength();
		}
	}

	public void gameOver(Graphics g) {
		isGameOver = true;
		g.setColor(Color.BLACK);
		g.drawString("game over", frame.getWidth()/2, frame.getHeight()/2);
	}

	public void restart() {		
		moveKeyCode = 0;

		snakePoints.clear();
		snakePoints.add(new Point(gWidth/2, gHeight/2));
		snakePoints.add(new Point(gWidth/2-1, gHeight/2));
		snakePoints.add(new Point(gWidth/2-2, gHeight/2));

		food = new Point(gWidth/2 + 3, gHeight/2);

		isGameOver = false;
	}

	public void keyPressed(KeyEvent e) {
		keyCode = e.getKeyCode();

		int oldMove = moveKeyCode;
		moveKeyCode = e.getKeyCode();
//		System.out.println(e.getKeyCode());
		// so the snake will always be moving
		if(!(moveKeyCode == 87 || moveKeyCode == 83 || moveKeyCode == 65 || moveKeyCode == 68)) moveKeyCode = oldMove;
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		Snake asdjke = new Snake();
	}
}