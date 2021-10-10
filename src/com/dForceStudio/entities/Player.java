package com.dForceStudio.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dForceStudio.main.Game;

public class Player extends Entity{
	
	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public int speed = 1;
	public BufferedImage[] rightPlayer;
	public BufferedImage[] leftPlayer;
	public BufferedImage[] upPlayer;
	public BufferedImage[] downPlayer;
	private int frames = 0, maxFrames = 4, index = 0, maxIndex = 2;
	private boolean moved = false;

	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		upPlayer = new BufferedImage[3];
		downPlayer = new BufferedImage[3];
		
		rightPlayer[0] = Game.spritesheet.getSprite(104, 1, 18, 23);
		rightPlayer[1] = Game.spritesheet.getSprite(104, 24, 18, 23);
		rightPlayer[2] = Game.spritesheet.getSprite(104, 49, 18, 23);
		
		leftPlayer[0] = Game.spritesheet.getSprite(84, 1, 18, 23);
		leftPlayer[1] = Game.spritesheet.getSprite(84, 24, 19, 23);
		leftPlayer[2] = Game.spritesheet.getSprite(84, 49, 18, 23);
		
		upPlayer[0] = Game.spritesheet.getSprite(66, 2, 17, 23);
		upPlayer[1] = Game.spritesheet.getSprite(66, 26, 17, 23);
		upPlayer[2] = Game.spritesheet.getSprite(66, 50, 17, 23);
		
		downPlayer[0] = Game.spritesheet.getSprite(49, 3, 16, 21);
		downPlayer[1] = Game.spritesheet.getSprite(49, 26, 16, 22);
		downPlayer[2] = Game.spritesheet.getSprite(49, 52, 16, 21);
		
	}
	
	public void tick() {
		moved = false;
		if(right) {
			moved = true;
			dir = right_dir;
			x += speed;
		}
		else if(left) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}
		if (up) {
			moved = true;
			dir = up_dir;
			y -= speed;
		}
		else if (down) {
			moved = true;
			dir = down_dir;
			y += speed;
		}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
	}

	public void render(Graphics g) {
		if(dir == right_dir) {
		g.drawImage(rightPlayer[index], this.getX(), this.getY(), null);
		}
		else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX(), this.getY(), null);
		}
		if(dir == up_dir) {
			g.drawImage(upPlayer[index], this.getX(), this.getY(), null);
			}
			else if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX(), this.getY(), null);
			}
	}
	
}
