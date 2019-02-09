package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 子彈 */
public class Bullet extends FlyingObject {
	private static BufferedImage image;
	static {
		image = loadImage("bullet.png");
	}

	private int speed;

	/** 構造方法 x,y:隨著英雄機的位置而不同 */
	public Bullet(int x, int y) {
		super(8, 14, x, y);
		speed = 3;
	}

	/** 子彈移動 */
	public void step() {
		y -= speed; // y-(向上)
	}

	public BufferedImage getImage() {
		if (isLife()) {
			return image;
		} else if (isDead()) {
			state = REMOVE;
		}
		return null;
	}

	public boolean outOfBounds() {
		return this.y <= -this.height; // this指的是當前對象: 子彈 <= 子彈的高  == 越界
	}

}
