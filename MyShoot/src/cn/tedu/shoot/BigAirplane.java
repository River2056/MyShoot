package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** �j�ľ� */
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

	/** �j�ľ����� */
	public void step() {
		y += speed; // y+(�V�U)
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
		return this.y >= World.HEIGHT; // this�����O��e��H: �j�ľ� >= ���f�� == �V��
	}
	
	/** ���ggetScore()��k: �����@�Ӥj�ľ��o3�� */
	public int getScore(){
		return 3;
	}

}
