package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 小敵機 */
public class Airplane extends FlyingObject implements Enemy {
	private int speed;
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("airplane" + i + ".png");
		}
	}

	/** 構造方法 */
	public Airplane() {
		super(49, 36);
		speed = 2;
	}

	/** 小敵機移動 */
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
		return this.y >= World.HEIGHT; // this指的是當前對象: 小敵機 >= 窗口高 == 越界
	}
	
	/** 重寫getScore()得分 */
	public int getScore(){
		return 1; // 打掉一個小敵機得1分
	}

}
