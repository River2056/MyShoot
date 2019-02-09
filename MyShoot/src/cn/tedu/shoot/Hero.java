package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** �^����: �O���檫 */
public class Hero extends FlyingObject {
	private static BufferedImage[] images; // �^�����Ϥ��h�i �ҥH�����Ʋ�
	static {
		images = new BufferedImage[6];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("hero" + i + ".png");
		}
	}

	private int life;
	private int doubleFire;

	/** �c�y��k Constructor */
	public Hero() {
		super(97, 124, 140, 400);
		this.life = 3; // �q�{3���R
		this.doubleFire = 0; // �歿���O
	}

	/** �^�����H�۹��в��� x, y = ���Ъ�x�My */
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}

	/** �^�������� */
	public void step() {

	}

	int index = 0; // ���۪��U��
	int deadIndex = 2; // ���F���U��

	/** ���ggetImage()����Ϥ� */
	public BufferedImage getImage() {
		if (isLife()) {
			return images[index++ % 2];
		} else if (isDead()) {
			BufferedImage img = images[deadIndex++];
			if (deadIndex == images.length) {
				state = REMOVE;
			}
			return img;
		}
		return null;
	}

	/** �^�����o�g�l�u(�Ыؤl�u��H) */
	public Bullet[] shoot() {
		int xStep = this.width / 4; // this����e��H:�^���� 1/4�^�������e
		int yStep = 20; // �T�w��, �i�H�N
		if (doubleFire > 0) { // �P�_�O�_���O��
			// �Ыؤl�u��H
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + xStep * 1, this.y - yStep);
			bs[1] = new Bullet(this.x + xStep * 3, this.y - yStep);
			doubleFire -= 2; // �o�g�@��, ���O�ȴ�2
			return bs;
		} else { // �p�G�L���O��, �o�g��o�l�u(�Ыؤ@�Ӥl�u�Ʋ�, �@�Ӥ���(�l�u))
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + xStep * 2, this.y - yStep);
			return bs;
		}
	}
	
	public boolean outOfBounds() {
		return false; //�^�����ä��V��, �V�ɺ�Ȱ�
	}
	
	/** �o�R */
	public void addLife() {
		life++;
	}
	
	public int getLife() {
		return life;  // ��^�R��
	}
	
	/** �^������R */
	public void subtractLife() { // ���W�ͩR��@
		life--;
	}
	
	/** �M�Ť��O�� */
	public void clearDoubleFire() { // ���W���O���k�s
		doubleFire = 0;
	}
	
	/** �o���O�� */
	public void addDoubleFire() {
		doubleFire += 40;
	}
	
	

}
