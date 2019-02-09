package cn.tedu.shoot;

import java.util.Random;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** �p�e�� */
public class Bee extends FlyingObject implements Award {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bee" + i + ".png");
		}
	}

	private int xSpeed; // �p�e���|��ۨ�
	private int ySpeed;
	private int awardType; // ���y����(0��1)

	public Bee() {
		super(60, 50);
		Random rand = new Random();
		xSpeed = 1;
		ySpeed = 2;
		awardType = rand.nextInt(2); // 0��1�������H����(2 = 0, 1)
	}

	/** �p�e������ */
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x <= 0 || x >= World.WIDTH - this.width) {
			xSpeed *= -1; // �����ܭt��, �t���ܥ���
		}
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

		}
		return null;
	}

	public boolean outOfBounds() {
		return this.y >= World.HEIGHT; // this�����O��e��H: �p�e�� >= ���f�� == �V��
	}
	
	public int getType(){
		return awardType; // ��^���y����
	}

}