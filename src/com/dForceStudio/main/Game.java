package com.dForceStudio.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
	private final int SCALE = 3;

	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bulletShoot;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	public static Random rand;
	public UI ui;
	
	

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
		world = new World("/map.png");
		
		
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
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for(int i = 0; i < bulletShoot.size(); i++) {
			bulletShoot.get(i).tick();
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
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bulletShoot.size(); i++) {
			bulletShoot.get(i).render(g);
		}
		ui.render(g);
		

		// para rotacionar
		// Graphics2D g2 = (Graphics2D) g;

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
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
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			player.shoot = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
		
		
	}
}
