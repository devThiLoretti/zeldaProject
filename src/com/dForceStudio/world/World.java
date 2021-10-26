package com.dForceStudio.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.dForceStudio.entities.Bullets;
import com.dForceStudio.entities.Enemy;
import com.dForceStudio.entities.Entity;
import com.dForceStudio.entities.LifePack;
import com.dForceStudio.entities.Player;
import com.dForceStudio.entities.Weapon;
import com.dForceStudio.main.Game;

import graphics.Spritesheet;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()]; 
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					if(pixels[xx + (yy*map.getWidth())] == 0xFF000000) {
						//floor
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_FLOOR);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFFFFFF) {
						//wall
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16, yy*16, Tile.TILE_WALL);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFF0026FF) {
						//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF0000) {
						//enemy
						Enemy en = new Enemy(xx*16, yy*16, 16, 16, Entity.enemy_en);
						Game.entities.add(en);
						Game.enemies.add(en);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF6A00) {
						//weapon
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.weapon_en));
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF00DC) {
						//life pack
						LifePack pack = new LifePack(xx*16, yy*16, 16, 16, Entity.lifePack_en);
						Game.entities.add(pack);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFFD800) {
						//bullets
						Game.entities.add(new Bullets(xx*16, yy*16, 16, 16, Entity.bullet_en));
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFF00FF21) {
						//floor 2
						tiles[xx + (yy*WIDTH)] = new FloorTile2(xx*16, yy*16, Tile.TILE_FLOOR2);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFA3FF3A) {
						//floor 3
						tiles[xx + (yy*WIDTH)] = new FloorTile2(xx*16, yy*16, Tile.TILE_FLOOR3);
					}
					
				}
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext, int zPlayer) {
		int x1 = xNext/TILE_SIZE;
		int y1 = yNext/TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1)/TILE_SIZE;
		int y2 = yNext/TILE_SIZE;
		
		int x3 = xNext/TILE_SIZE;
		int y3 = (yNext + TILE_SIZE +5)/TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1)/TILE_SIZE;
		int y4 = (yNext + TILE_SIZE + 5)/TILE_SIZE;
		
		if(!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile))) {
			return true;
	}	
		if(zPlayer > 0) {
			return true;
		}
		return false;
	} 
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 21, Game.spritesheet.getSprite(48, 1, 17, 23));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	public void render(Graphics g){
		int xStart = Camera.x /16;
		int yStart = Camera.y /16;
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
