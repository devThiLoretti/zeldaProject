package com.dForceStudio.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dForceStudio.main.Game;

public class Entity {
	
	public static BufferedImage lifePack_en = Game.spritesheet.getSprite(128, 18, 15, 15);
	public static BufferedImage weapon_en = Game.spritesheet.getSprite(129, 33, 14, 14);
	public static BufferedImage bullet_en = Game.spritesheet.getSprite(128, 1, 16, 16);
	public static BufferedImage enemy_en = Game.spritesheet.getSprite(1, 77, 17, 24);
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	public int getX() {
		return this.x;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX(), this.getY(), null);
	}

	public void tick() {
		// TODO Auto-generated method stub
		
	}
	
}
