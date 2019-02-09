package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 大敵機 */
public class BigAirplane extends FlyingObject implements Enemy {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bigplane" + i + ".png");
		}
	}

	private int speed;

	public BigAirplane() {
		super(69, 99);
		speed = 2;
	}

	/** 大敵機移動 */
	public void step() {
		y += speed; // y+(向下)
	}

	int deadIndex = 1;

	public BufferedImage getImage() {
		if (isLife()) {
			return images[0];
		} else if (isDead()) {
			BufferedImage img = images[deadIndex++];
			if (deadIndex == images.length) {
				state = REMOVE;
			}
			return img;
		}
		return null;
	}

	public boolean outOfBounds() {
		return this.y >= World.HEIGHT; // this指的是當前對象: 大敵機 >= 窗口高 == 越界
	}
	
	/** 重寫getScore()方法: 打掉一個大敵機得3分 */
	public int getScore(){
		return 3;
	}

}
