package com.dForceStudio.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dForceStudio.entities.Bullets;
import com.dForceStudio.entities.Enemy;
import com.dForceStudio.entities.Entity;
import com.dForceStudio.entities.LifePack;
import com.dForceStudio.entities.Weapon;
import com.dForceStudio.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
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
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Tile.TILE_WALL);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFF0026FF) {
						//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF0000) {
						//enemy
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Entity.enemy_en));
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF6A00) {
						//weapon
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Entity.weapon_en));
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFF00DC) {
						//life pack
						Game.entities.add(new LifePack(xx*16, yy*16, 16, 16, Entity.lifePack_en));
					}
					else if(pixels[xx + (yy*map.getWidth())] == 0xFFFFD800) {
						//bullets
						Game.entities.add(new Bullets(xx*16, yy*16, 16, 16, Entity.bullet_en));
					}
					
				}
				}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void render(Graphics g){
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
