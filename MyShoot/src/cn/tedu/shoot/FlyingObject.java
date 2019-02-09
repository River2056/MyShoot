package cn.tedu.shoot;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;

public abstract class FlyingObject { // ��X�Ҧ����O�@���ݩ�(�W��), ���������~��
	public static final int LIFE = 0; // ���۪�
	public static final int DEAD = 1; // ���F��
	public static final int REMOVE = 2; // �i�H�R����
	protected int state = LIFE; // ��e���A(�q�{�����۪�)

	protected int width;
	protected int height;
	protected int x;
	protected int y;

	public FlyingObject(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}

	public FlyingObject() { // �d���q�{���^����, �Ѫ�, �l�u

	}

	public FlyingObject(int width, int height) { // ��X�@��: �p�ľ�, �j�ľ�, �p�e��
		this.width = width;
		this.height = height;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH - this.width);
		y = -this.height;
	}

	/** ���檫���� */
	public abstract void step();

	/** ����Ϥ� */
	public abstract BufferedImage getImage();

	// hero.paintObject(g);
	// sky.paintObject(g);
	/** �e��H g:�e�� */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}

	/** �[��/Ū���Ϥ� */
	public static BufferedImage loadImage(String fileName) { // �[�ѼƬO���F���D�nŪ����
		try {
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/** �P�_�O�_���۩O */
	public boolean isLife() {
		return state == LIFE;
	}

	/** �P�_�O�_���F�� */
	public boolean isDead() {
		return state == DEAD;
	}

	/** �P�_�O�_�i�H�R���� */
	public boolean isRemove() {
		return state == REMOVE;
	}

	/** �P�_���檫�O�_�V�� */
	public abstract boolean outOfBounds();
	
	/** �˴��ĤH�P�l�u�M�^�������I��  this: �ĤH  other: �^����, �l�u */
	public boolean hit(FlyingObject other) {
		int x1 = this.x - other.width;
		int x2 = this.x + this.width;
		int y1 = this.y - other.height;
		int y2 = this.y + this.height;
		int x = other.x;
		int y = other.y;
		
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
	
	public void goDead() {
		state = DEAD;
	}

}
