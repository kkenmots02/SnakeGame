import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

class Snake {
  /**
   * Current direction of the Snake will always be either 8, 2, 4, or 6 … which is
   * up, down, left, right taken from the orientation of numpad keys on keyboard
   */
  private int currentDirection = 8;

  /** The number of tail pieces that need to be added to the end of body */
  private int amountToGrow = 5;
  
  
  /** The head of the Snake */
  private Rectangle head;

  /** The List of Rectangle objects that are the body of the Snake */
  private ArrayList<Rectangle> body;

  /**
   * The List of integer directions stored from keyboard input that the Snake will
   * turn based on the order in which they were inputted from the keyboard
   */
  private ArrayList<Integer> directionQ;

  /** The object the Snake will intersect with to grow */
  private Rectangle nibble = new Rectangle(200, 300, 20, 20);

  public Snake() {
    head = new Rectangle(200,200,20,20);
    nibble = new Rectangle(200,300,20,20);
    body = new ArrayList<Rectangle>();
    directionQ = new ArrayList<Integer>();
    currentDirection = 8;
    amountToGrow = 5;
    }

  public void render(Graphics2D g) {
	  g.draw(head);
	  g.draw(nibble);
	  for (Rectangle b : body) {
		  g.draw(b);
	  }
    }

  public void addDirection(int d) {
    if (directionQ.size() == 0) {
    	if (d!= currentDirection && d != 10 - currentDirection) {
    		directionQ.add(d);
    	}
    } else {
    	if (d != directionQ.get(directionQ.size()-1) && d != 10-directionQ.get(directionQ.size()-1)) {
    		directionQ.add(d);
    	}
    }
  }
  

    public void updateDirection() {
    	if (directionQ.size() > 0) currentDirection = directionQ.remove(0);
    }

  public void updatePosition() {
	  body.add(0, new Rectangle(head));
	  
	  switch (currentDirection) {
	  	case 8:
	  		head.translate(0, -20);
	  		break;
	  	case 6:
	  		head.translate(20, 0);
	  		break;
	  	case 4:
	  		head.translate(-20, 0);
	  		break;
	  	case 2:
	  		head.translate(0, 20);
	  		break;
	  }
  }

  public void moveNibble() {
	  boolean sameLoc = true;
	  int x = 0, y = 0;
	  while (sameLoc) {
		  sameLoc = false;
		  x = ((int)(Math.random()*20)) * 20;
		  y = ((int)(Math.random()*20)) * 20;
		  
		  for (Rectangle b : body) {
			  if (b.y == y && b.x == x) {
				  sameLoc = true;
			  }
		  }
	  }
	  nibble = new Rectangle(x, y, 20, 20);
  }

  public boolean eatNibble() {
    if (head.intersects(nibble)) {
    	moveNibble();
    	amountToGrow += 5;
    	return true;
    }
    return false;
  }

  public void eraseTail() {
    if (amountToGrow == 0) {
    	body.remove(body.size()-1);
    } else {
    	amountToGrow--;
    }
  }

  public boolean isDead() {
    for (Rectangle b : body) {
    	if (head.intersects(b)) {
    		return true;
    	}
    }
    return !head.intersects(new Rectangle(0,0,400,400));
  }
}

public class SnakeGameAP extends JPanel implements KeyListener, ActionListener {

  // ~~~~~~~~~~~~~~~~~~~~~~~~~~
  // UNCOMMENT after Question 1
  private Snake snake = new Snake();
  // UNCOMMENT after Question 1
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~
  private int nibblesEaten;
  private int frameCount;
  private Timer timer;
  private Image offScreenBuffer;// double buffering
  private Graphics offScreenGraphics;// double buffering

  /**
   * initializes all fields needed for each round of play (i.e. restart)
   */
  public void initRound() {
    frameCount = 0;
    nibblesEaten = 0;
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 1
    snake = new Snake();
    // UNCOMMENT after Question 1
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

  }

  /**
   * renders all objects to Graphics g (i.e. the window)
   */
  public void draw(Graphics2D g) {
    if (g == null)
      return;
    super.paint(g);// refresh the background
    g.setColor(Color.BLACK);
    g.drawString("frameCount: " + frameCount, 40, 40);
    g.drawString("score: " + nibblesEaten, 40, 80);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 1
    snake.render(g);
    // UNCOMMENT after Question 1
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

  }

  /**
   * Update all game objects .. called automatically when the timer fires<br>
   */
  public void actionPerformed(ActionEvent e) {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 2
    snake.updateDirection();
    snake.updatePosition();
    // UNCOMMENT after Question 2
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 3
    if (snake.eatNibble()) nibblesEaten++;
    snake.eraseTail();
    // UNCOMMENT after Question 3
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 4
    if (snake.isDead())
    timer.stop();
    // UNCOMMENT after Question 4
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

    frameCount++;// keep track of how many frames in the round
    repaint();// needed to refresh the animation
  }

  /**
   * handles key pressed events and updates the snake's direction
   */
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    // UNCOMMENT after Question 2
    if (keyCode == KeyEvent.VK_UP)
      snake.addDirection(8);
    else if (keyCode == KeyEvent.VK_DOWN)
      snake.addDirection(2);
    else if (keyCode == KeyEvent.VK_LEFT)
      snake.addDirection(4);
    else if (keyCode == KeyEvent.VK_RIGHT)
      snake.addDirection(6);
    // UNCOMMENT after Question 2
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~

  }

  /**
   * handles key released events and restarts the game on 'space' event
   */
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    if (keyCode == KeyEvent.VK_SPACE && !timer.isRunning()) {
      timer.start();
      initRound();
    }
  }

  /**
   * you should write all necessary initialization code in initRound() THIS METHOD
   * SHOULD ONLY BE MODIFIED IF ADDING SOUNDS OR IMAGES! .. <br>
   */
  public void init() {
    offScreenBuffer = createImage(getWidth(), getHeight());// should be 400x400
    offScreenGraphics = offScreenBuffer.getGraphics();
    initRound();
    timer = new Timer(100, this);
    // timer fires every 100 milliseconds.. and invokes method actionPerformed()
  }

  /**
   * main method needed for initialize the game window .. <br>
   * THIS METHOD SHOULD NOT BE MODIFIED! .. <br>
   */
  public static void main(String[] args) {
    JFrame window = new JFrame("Snake Game Clone");
    window.setBounds(100, 100, 400 + 6, 400 + 28);
    // inside JFrame will be 600x600
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);

    SnakeGameAP game = new SnakeGameAP();
    game.setBackground(Color.WHITE);
    window.getContentPane().add(game);
    window.setVisible(true);
    game.init();
    window.addKeyListener(game);
  }

  /**
   * Called automatically after a repaint request<br>
   * .. THIS METHOD SHOULD NOT BE MODIFIED! .. <br>
   */
  public void paint(Graphics g) {
    draw((Graphics2D) offScreenGraphics);
    g.drawImage(offScreenBuffer, 0, 0, this);
  }

  /**
   * leave empty.. needed for implementing interface KeyListener<br>
   * .. THIS METHOD SHOULD NOT BE MODIFIED! .. <br>
   */
  public void keyTyped(KeyEvent e) {
  }
}// end class SnakeGameAP
