package com.dForceStudio.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dForceStudio.main.Game;
import com.dForceStudio.world.Camera;

public class BulletShoot extends Entity{
	
	private int dx, dy;
	private double spd = 4;
	public static BufferedImage ice_right = Game.spritesheet.getSprite(97, 129, 14, 14);
	public static BufferedImage ice_left = Game.spritesheet.getSprite(81, 129, 14, 14);
	public static BufferedImage ice_up = Game.spritesheet.getSprite(113, 129, 14, 14);
	public static BufferedImage ice_down = Game.spritesheet.getSprite(129, 129, 14, 14);
	private int frames = 60, curFrames = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx*spd;
		y += dy*spd;
		curFrames++;
		if(curFrames == frames) {
			Game.bulletShoot.remove(this);
		}
	}
	
	public void render(Graphics g) {
		if(Game.player.dir == Game.player.right_dir) {
		g.drawImage(ice_right, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
		if(Game.player.dir == Game.player.left_dir) {
			g.drawImage(ice_left, this.getX()-Camera.x, this.getY()-Camera.y, null);
			}
		if(Game.player.dir == Game.player.down_dir) {
			g.drawImage(ice_down, this.getX()-Camera.x, this.getY()-Camera.y, null);
			}
		if(Game.player.dir == Game.player.up_dir) {
			g.drawImage(ice_up, this.getX()-Camera.x, this.getY()-Camera.y, null);
			}
	}
}
