package com.dForceStudio.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.dForceStudio.entities.BulletShoot;
import com.dForceStudio.entities.Enemy;
import com.dForceStudio.entities.Entity;
import com.dForceStudio.entities.Player;
import com.dForceStudio.graphics.UI;
import com.dForceStudio.world.World;

import graphics.Spritesheet;
import main.Menu;
import main.Sound;
import main.Sound.Clips;

public class Game extends Canvas implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private boolean isRunning;
	private Thread thread;
	public final static int WIDTH = 320;
	public final static int HEIGHT = 160;
	public static final int SCALE = 3;

	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bulletShoot;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public UI ui;
	private int curLevel = 1, maxLevel = 2;
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	public Menu menu;
	public static boolean saveGame = false;

	public Game() {
		rand = new Random();
		addKeyListener(this);
		frame = new JFrame("Zelda dForce Studio");
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bulletShoot = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 21, spritesheet.getSprite(48, 1, 17, 23));
		entities.add(player);
		world = new World("/level1.png");
		menu = new Menu();
		

	}

	public void initFrame() {
		frame.add(this);
		frame.setResizable(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
		
		
	}

	public synchronized void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void tick() {
		if (gameState == "NORMAL") {
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.curLevel};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Game saved");
			}
			Sound.music.loop();
			this.restartGame = false;
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < bulletShoot.size(); i++) {
				bulletShoot.get(i).tick();
			}

			if (enemies.size() == 0) {
				// avançar próximo level
				curLevel++;
				if (curLevel > maxLevel) {
					curLevel = 1;
				}
				String newWorld = "level" + curLevel + ".png";
				World.restartGame(newWorld);

			}
		}else if(gameState == "GAME OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 15) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver = false;
				}
				else {
					this.showMessageGameOver = true;
				}
			}
		}

		if(restartGame) {
			this.restartGame = false;
			this.gameState = "NORMAL";
			curLevel = 1;
			String newWorld = "level" + curLevel + ".png";
			World.restartGame(newWorld);
		}
		else if(gameState == "MENU"){
			menu.tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bulletShoot.size(); i++) {
			bulletShoot.get(i).render(g);
		}
		ui.render(g);

		// para rotacionar
		// Graphics2D g2 = (Graphics2D) g;

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		if (gameState == "GAME OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, SCALE*HEIGHT);
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.BOLD, 100));
			g.drawString("Game Over", 240, 280);
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.CENTER_BASELINE, 30));
			g.drawString("Continue?", 450, 320);
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.CENTER_BASELINE, 30));
			if(showMessageGameOver) {
			g.drawString("Press ENTER", 430, 360);
			}
			}else if(gameState == "MENU") {
				menu.render(g);
			}

		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS = " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.jump = true;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
			
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}
			
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			player.shoot = true;
			Sound.castSpell.play();
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			restartGame = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL") {
			this.saveGame = true;
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

	}
}
