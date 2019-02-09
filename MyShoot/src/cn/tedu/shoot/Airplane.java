package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** �p�ľ� */
public class Airplane extends FlyingObject implements Enemy {
	private int speed;
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("airplane" + i + ".png");
		}
	}

	/** �c�y��k */
	public Airplane() {
		super(49, 36);
		speed = 2;
	}

	/** �p�ľ����� */
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
		return this.y >= World.HEIGHT; // this�����O��e��H: �p�ľ� >= ���f�� == �V��
	}
	
	/** ���ggetScore()�o�� */
	public int getScore(){
		return 1; // �����@�Ӥp�ľ��o1��
	}

}
