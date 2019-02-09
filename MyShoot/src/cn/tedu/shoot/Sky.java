package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
	private static BufferedImage image; // �Ϥ�
	static {
		image = loadImage("background.png");
	}

	private int speed;
	private int y1;

	public Sky() {
		super(World.WIDTH, World.HEIGHT, 0, 0);
		speed = 1;
		y1 = -height; // y1: �t�����f����
	}

	/** �ѪŲ��� */
	public void step() {
		y += speed;
		y1 += speed;
		if (y >= World.HEIGHT) { // �Yy>=�Ѫ� ����(��U���F)
			y = -this.height;    // �h�]�my���t����
		}
		if (y1 >= World.HEIGHT) {// �Py
			y1 = -this.height;
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null); // �e�Ѫ�
		g.drawImage(getImage(), x, y1, null); // �e�Ѫ�2
	}
	
	public boolean outOfBounds() {
		return false; //�Ѫťä��V��; ��i�ϨӦ^����, �ҥH���|�V��
	}
}
