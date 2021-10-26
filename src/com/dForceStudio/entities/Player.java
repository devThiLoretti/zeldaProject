package com.dForceStudio.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.dForceStudio.graphics.UI;
import com.dForceStudio.main.Game;
import com.dForceStudio.world.Camera;
import com.dForceStudio.world.World;

import graphics.Spritesheet;
import main.Sound;

public class Player extends Entity {

	public boolean right, left, up, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public int speed = 2;
	public BufferedImage[] rightPlayer;
	public BufferedImage[] leftPlayer;
	public BufferedImage[] upPlayer;
	public BufferedImage[] downPlayer;
	private int frames = 0, maxFrames = 4, index = 0, maxIndex = 2;
	private boolean moved = false;
	public int life = 100, maxLife = 100;
	public int ammo = 0;
	public BufferedImage playerDamaged;
	public boolean isDamaged = false;
	private int damageFrames = 0;
	private boolean hasGun = false;
	private boolean hasAmmo = false;
	public boolean shoot = false;
	int dx = 0;
	public boolean jump = false;
	public int z = 0;
	public int jumpFrames = 50, jumpCur = 0;
	public boolean isJumping = false;
	public boolean jumpUp = false, jumpDown = false;
	public int jumpSpeed = 2;
	

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		playerDamaged = Game.spritesheet.getSprite(130, 66, 18, 22);
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
		
		if(jump == true) {
			if(isJumping == false) {
			jump = false;
			isJumping = true;
			jumpUp = true;
			}
		}
		if(isJumping == true) {
			
			 {
				if(jumpUp) {
				jumpCur += jumpSpeed;
				}
				else if(jumpDown) {
					jumpCur -= jumpSpeed;
					if(jumpCur <= 0) {
						isJumping = false;
						jumpDown = false;
						jumpUp = false;
						
					}
				}
				z = jumpCur;
				if(jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
				}
			}
		}
		
		if (right && World.isFree((int) (x + speed), this.getY(), 0)) {
			moved = true;
			dir = right_dir;
			x += speed;

		} else if (left && World.isFree((int) (x - speed), this.getY(), 0)) {
			moved = true;
			dir = left_dir;
			x -= speed;

		}
		if (up && World.isFree(this.getX(), (int) (y - speed), 0)) {
			moved = true;
			dir = up_dir;
			y -= speed;
		} else if (down && World.isFree(this.getX(), (int) (y + speed), 0)) {
			moved = true;
			dir = down_dir;
			y += speed;
		}
		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if(ammo > 0) {
			hasAmmo = true;
		}
		else {
			hasAmmo = false;
		}
		

		if (shoot && hasGun && hasAmmo) {
			shoot = false;

			if (dir == right_dir) {
				int dx = 1;
				BulletShoot bulletShoot = new BulletShoot(this.getX()+9, this.getY()+7, width, height, null, dx, 0);
				Game.bulletShoot.add(bulletShoot);
				ammo--;
				
			} else if(dir == left_dir){
				int dx = -1;
				BulletShoot bulletShoot = new BulletShoot(this.getX()-9, this.getY()+9, width, height, null, dx, 0);
				Game.bulletShoot.add(bulletShoot);
				ammo--;
				
			}
			
			if (dir == up_dir) {
				int dy = -1;
				BulletShoot bulletShoot = new BulletShoot(this.getX()+9, this.getY()+7, width, height, null, 0, dy);
				Game.bulletShoot.add(bulletShoot);
				ammo--;
				
			} else if(dir == down_dir){
				int dy = 1;
				BulletShoot bulletShoot = new BulletShoot(this.getX()+9, this.getY()+9, width, height, null, 0, dy);
				Game.bulletShoot.add(bulletShoot);
				ammo--;
			}
			
		}

		this.checkColisionToLifePack();
		this.checkColisionToAmmo();
		this.checkColisionToGun();
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

		if (life <= 0 ) {
			Game.gameState = "GAME OVER";
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0, 0, 16, 21, Game.spritesheet.getSprite(48, 1, 17, 23));
			Game.entities.add(Game.player);
			Game.world = new World("/level1.png");
			return;
		}

		if (isDamaged) {
			Sound.damage.play();
			this.damageFrames++;
			if (damageFrames == 1) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}

	}

	public void checkColisionToAmmo() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof Bullets) {
				if (Entity.isColiding(this, actual)) {
					ammo += 10;
					Game.entities.remove(i);
					
				}
			}
		}
	}

	public void checkColisionToGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof Weapon) {
				if (Entity.isColiding(this, actual)) {
					hasGun = true;
					Game.entities.remove(i);
				}
			}
		}
	}

	public void checkColisionToLifePack() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity actual = Game.entities.get(i);
			if (actual instanceof LifePack) {
				if (Entity.isColiding(this, actual)) {
					life += 100;
					if (life > 100) {
						life = 100;
					}
					Game.entities.remove(i);
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y-z, null);
				if (hasGun) {
					g.drawImage(Entity.cane_en_right, this.getX() - Camera.x + 10, this.getY() - Camera.y -z+ 7, null);
				}
			} else if (dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if (hasGun) {
					g.drawImage(Entity.cane_en_left, this.getX() - Camera.x - 6, this.getY() - Camera.y + 9 -z, null);
				}
			}
			if (dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			} else if (dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y -z, null);
				if (hasGun) {
					g.drawImage(Entity.cane_en_down, this.getX() - Camera.x + 7, this.getY() - Camera.y - z + 10, null);
				}
			}
		} else {
			g.drawImage(playerDamaged, this.getX() - Camera.x, this.getY() - Camera.y-z, null);
		}
		if(isJumping) {
			g.setColor(Color.black);
			g.fillOval(this.getX()-Camera.x+4, this.getY()-Camera.y+16, 8, 8);
		}

	}
	

}
