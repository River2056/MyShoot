package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** �l�u */
public class Bullet extends FlyingObject {
	private static BufferedImage image;
	static {
		image = loadImage("bullet.png");
	}

	private int speed;

	/** �c�y��k x,y:�H�ۭ^��������m�Ӥ��P */
	public Bullet(int x, int y) {
		super(8, 14, x, y);
		speed = 3;
	}

	/** �l�u���� */
	public void step() {
		y -= speed; // y-(�V�W)
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
		return this.y <= -this.height; // this�����O��e��H: �l�u <= �l�u����  == �V��
	}

}
