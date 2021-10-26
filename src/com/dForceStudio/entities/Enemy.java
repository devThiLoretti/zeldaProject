package com.dForceStudio.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.dForceStudio.main.Game;
import com.dForceStudio.world.Camera;
import com.dForceStudio.world.World;

import graphics.Spritesheet;
import main.Sound;

public class Enemy extends Entity {

	private double speed = 1;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3, life = 6;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	private BufferedImage[] spritesDown;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		spritesDown = new BufferedImage[4];
		spritesDown[0] = Game.spritesheet.getSprite(2, 78, 16, 22);
		spritesDown[1] = Game.spritesheet.getSprite(2, 103, 16, 22);
		spritesDown[2] = Game.spritesheet.getSprite(2, 129, 16, 22);
		spritesDown[3] = Game.spritesheet.getSprite(2, 103, 16, 22);

	}

	public void tick() {
		if (this.isColidingToPlayer() == false) {
			if (Game.rand.nextInt(100) < 70) {
				if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), this.getY(), 0)
						&& !isColiding((int) (x + speed), this.getY())) {
					x += speed;

				}
				if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), this.getY(), 0)
						&& !isColiding((int) (x - speed), this.getY())) {
					x -= speed;

				}
				if ((int) y > Game.player.getY() && World.isFree(this.getX(), (int) (y - speed), 0)
						&& !isColiding(this.getX(), (int) (y - speed))) {
					y -= speed;

				}
				if ((int) y < Game.player.getY() && World.isFree(this.getX(), (int) (y + speed), 0)
						&& !isColiding(this.getX(), (int) (y + speed))) {
					y += speed;
				} else {

					if (Game.player.life == 0) {

					}
				}
			}

			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;

				if (index > maxIndex) {

					index = 0;

				}
				colidingBullet();

			}

		}

		if (life <= 0) {
			destroySelf();
			return;
		}
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}

	}

	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
		Sound.damage.play();
	}

	public void colidingBullet() {
		for (int i = 0; i < Game.bulletShoot.size(); i++) {
			Entity e = Game.bulletShoot.get(i);
			if (e instanceof BulletShoot) {
				if (Entity.isColiding(this, e)) {
					isDamaged = true;
					life--;
					Game.bulletShoot.remove(i);
					return;
				}
			}
		}

	}

	public boolean isColidingToPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		if (enemyCurrent.intersects(player)) {
			Game.player.isDamaged = true;
			Game.player.life--;

		}

		return enemyCurrent.intersects(player);
	}

	public boolean isColiding(int xNext, int yNext) {
		Rectangle enemyCurrent = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}

		return false;
	}

	public void render(Graphics g) {
		if(!isDamaged) {
		g.drawImage(spritesDown[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

	}
		else {
			g.drawImage(Entity.enemy_feedback, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}