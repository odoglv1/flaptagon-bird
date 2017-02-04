package flaptagonbird;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * @version 8
 * @author CODE/SOUND: Owen Thompson ART/ASSET HANDLING: Michael Mincer
 * 
 * Octagons. They are an interesting shape.
 */

public class FlaptagonBird extends PApplet {
	AudioSample jump;
	AudioSample fall;
	AudioSample point;
	Minim minim;
	
	PImage icon;
	
	PImage bird;
	PImage backImg;
	PImage titleImg;
	PImage wallImg;
	PImage wall2Img;
	
	public double GRAVITY = .2;
	
	public Bird player;
	public Pipe pipeA;
	public Pipe pipeB;
	
	public boolean hasJumped = false;
	
	public int gamestate;
	public int bks = -200;
	public int pipeLoc;
	
	public int score;
	public int highScore;
	
	public boolean incrementedScore;
	
	public void setup() {
		gamestate = 1;
		
		minim = new Minim(this);
		jump = minim.loadSample("octagon.wav");
		fall = minim.loadSample("ohman.wav");
		point = minim.loadSample("stop.wav");
		
		incrementedScore = false;
		score = 0;
		highScore = 0;
		
		backImg = loadImage("NEWBACKGROUND.png");
		titleImg = loadImage("NEWTITLE.png");
		wallImg = loadImage("NEWWALL.png");
		wall2Img = loadImage("NEWWALL2.png");
		bird = loadImage("newbird.png");
		
		pipeLoc = width;
		
		icon = loadImage("STOP.PNG");
		
		surface.setTitle("Flaptagon Bird by game²");
		surface.setIcon(icon);
		
		bird.resize(35, 35);
		
		pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
		pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
		player = new Bird(bird, width / 2, height / 2);
	}

	public void draw() {
		if (gamestate == 0) {
			imageMode(CORNER);
			image(backImg, bks, 0);
			image(backImg, bks + backImg.width, 0);
			bks -= 6;
			
			pipeA.update();
			pipeA.drawPipe();
			
			pipeB.update();
			pipeB.drawPipe();
			
			player.update();
			player.drawBird();
			
			if (bks <= -1800) {
				bks = 0;
			}
			
			if (GRAVITY > -4) {
				hasJumped = false;
			}
			
			fill(0);
			textSize(32);
			text("Score: " + score, 10, 10, 150, 100);
		}
		else {
			image(titleImg, 0, 0);
			fill(0);
			textSize(32);
			text("High Score: " + highScore, 10, 233, 250, 100);
		}
	}
	
	public void settings() {
		size(600, 800);
	}
	
	public void mousePressed() {
		if (gamestate == 0) {
			if (!hasJumped) {
				GRAVITY = -5.0;
				jump.trigger();
				hasJumped = true;
			}
		}
	}
	
	public void keyPressed() {
		if (key == ' ') {
			if (gamestate == 0) {
				if (!hasJumped) {
					GRAVITY = -5.0;
					jump.trigger();
					hasJumped = true;
				}
			}
		}
		if (key == 'k') {
			if (gamestate == 1) {
				score = 0;
				gamestate = 0;
			}
		}
	}
	
	public class Bird {
		PImage sprite;
		float xPos;
		float yPos;
		
		public Bird(PImage spr, float x, float y) {
			sprite = spr;
			xPos = x;
			yPos = y;
		}
		
		public void update() {
			yPos += GRAVITY;
			GRAVITY += .2;
			
			if (this.yPos + this.sprite.height >= height) {
				if (score >= highScore) {
					highScore = score;
				}
				fall.trigger();
				try {
					java.lang.Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				pipeLoc = width;
				
				pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
				pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
				
				player = new Bird(bird, width / 2, height / 2);
				
				GRAVITY = .2;
				score = 0;
				gamestate = 1;
			}
			if (this.yPos <= 0) {
				if (score >= highScore) {
					highScore = score;
				}
				fall.trigger();
				try {
					java.lang.Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				pipeLoc = width;
				
				pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
				pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
				
				player = new Bird(bird, width / 2, height / 2);
				
				GRAVITY = .2;
				score = 0;
				gamestate = 1;
			}
		}
		
		public void drawBird() {
			image(sprite, xPos, yPos);
		}
	}
	
	public class Pipe {
		PImage sprite;
		float xPos;
		float yPos;
		
		public Pipe(PImage spr, float x, float y) {
			sprite = spr;
			xPos = x;
			yPos = y;
		}
		
		public void update() {
			if (pipeLoc < 0) {
				incrementedScore = false;
				pipeLoc = width;
				pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
				pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
			}
			if (pipeLoc >= width) {
				incrementedScore = false;
			}
			
			pipeLoc -= 3;
			xPos = pipeLoc;
			
			checkCollision();
		}
		
		public void drawPipe() {
			image(sprite, xPos, yPos);
		}
		
		public void checkCollision() {
			if (player.xPos + player.sprite.width > this.xPos && player.xPos + player.sprite.width < this.xPos + this.sprite.width) {
				if (player.yPos > pipeA.yPos && player.yPos < pipeA.yPos + pipeA.sprite.height) {
					if (score >= highScore) {
						highScore = score;
					}
					fall.trigger();
					try {
						java.lang.Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					pipeLoc = width;
					
					pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
					pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
					
					player = new Bird(bird, width / 2, height / 2);
					
					GRAVITY = .2;
					score = 0;
					gamestate = 1;
				}
				if (player.yPos + player.sprite.height > pipeB.yPos) {
					if (score >= highScore) {
						highScore = score;
					}
					fall.trigger();
					try {
						java.lang.Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					pipeLoc = width;
					
					pipeA = new Pipe(wall2Img, pipeLoc, random(-600, -400));
					pipeB = new Pipe(wallImg, pipeLoc, pipeA.yPos + pipeA.sprite.height + 150);
					
					player = new Bird(bird, width / 2, height / 2);
					
					GRAVITY = .2;
					score = 0;
					gamestate = 1;
				}
				else {
					if (!incrementedScore) {
						score++;
						point.trigger();
						incrementedScore = true;
					}
				}
			}
		}
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { flaptagonbird.FlaptagonBird.class.getName() });
	}
}