package com.dForceStudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.dForceStudio.main.Game;
import com.dForceStudio.world.Camera;

public class Entity {
	
	public static BufferedImage lifePack_en = Game.spritesheet.getSprite(128, 18, 15, 15);
	public static BufferedImage weapon_en = Game.spritesheet.getSprite(129, 33, 14, 14);
	public static BufferedImage bullet_en = Game.spritesheet.getSprite(128, 1, 16, 16);
	public static BufferedImage enemy_en = Game.spritesheet.getSprite(1, 77, 17, 24);
	public static BufferedImage cane_en_right = Game.spritesheet.getSprite(97, 81, 14, 14);
	public static BufferedImage cane_en_left = Game.spritesheet.getSprite(97, 97, 14, 14);
	public static BufferedImage cane_en_down = Game.spritesheet.getSprite(97, 113, 14, 14);
	public static BufferedImage enemy_feedback = Game.spritesheet.getSprite(129, 90, 16, 23);
	
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
	
	public static boolean isColiding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), 16, 16);
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), 16, 16);
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		//g.setColor(Color.RED);
		//g.fillRect(this.getX()-Camera.x, this.getY()-Camera.y, 16, 16);
	}

	public void tick() {
		
		
	}
	
}
