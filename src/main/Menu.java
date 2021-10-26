package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.dForceStudio.main.Game;
import com.dForceStudio.world.World;

public class Menu implements KeyListener {

	public String[] options = { "New game", "Load game", "Exit game" };
	public int curOption = 0;
	public int maxOption = options.length - 1;
	public boolean up, down, enter;
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;

	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader br = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = br.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i <val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
						}
				}catch(IOException e) {}
					}catch(FileNotFoundException e) {}
				}
		return line;
	}
	
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[]spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level" :
				World.restartGame("level" + spl2[1] + ".png");
				Game.gameState = "NORMAL";
				break;
			}
		}
	}
	
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("save.txt"));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n] += encode;
				current += value[n];
			}
			try {
				bw.write(current);
				if(i < val1.length-1) {
					bw.newLine();
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			try {
				bw.flush();
				bw.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}
		else {
			saveExists = false;
		}
		if (up) {
			up = false;
			curOption--;
			if (curOption < 0) {
				curOption = maxOption;
			}
		}
		if (down) {
			down = false;
			curOption++;
			if (curOption > maxOption) {
				curOption = 0;
			}
		}
		if(enter) {
			enter = false;
			if(options[curOption] == "New game" || options[curOption] == "Continue") {
				Game.gameState = "NORMAL";
				pause = false;
			} else if(options[curOption] == "Load game") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
		}

	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.green);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString(">Zelda: a link to Loretti<", (Game.WIDTH * Game.SCALE) / 2 - 200,
				(Game.HEIGHT * Game.SCALE) / 2 - 160);

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 24));
		g.drawString("New Game", 430, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		g.drawString("Load Game", 430, (Game.HEIGHT * Game.SCALE) / 2 - 70);
		g.drawString("Exit Game", 430, (Game.HEIGHT * Game.SCALE) / 2 - 40);

		if (options[curOption] == "New game") {
			g.drawString(">", 400, (Game.HEIGHT * Game.SCALE) / 2 - 100);
		} else if (options[curOption] == "Load game") {
			g.drawString(">", 400, (Game.HEIGHT * Game.SCALE) / 2 - 70);
		} else if (options[curOption] == "Exit game") {
			g.drawString(">", 400, (Game.HEIGHT * Game.SCALE) / 2 - 40);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
       if(options[curOption] == "New game" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			Game.gameState = "NORMAL";
		}
      
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
