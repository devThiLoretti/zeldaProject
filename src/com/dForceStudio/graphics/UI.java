package com.dForceStudio.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.dForceStudio.entities.Player;
import com.dForceStudio.main.Game;



public class UI {
	public void render(Graphics g) {
		g.setColor(Color.red.darker());
		g.fillRect(20, 20, Game.player.life, 10);
		g.drawString("Life", 20, 40);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString("Munição: " + Game.player.ammo, 250, 30);
	}

}
