package cn.tedu.shoot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class World extends JPanel {
	public static final int WIDTH = 400; // ���f�e
	public static final int HEIGHT = 700; // ���f��

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = START; // ��e���A(�q�{���Ұʪ��A)

	private static BufferedImage start; // �Ұ�
	private static BufferedImage pause; // �Ȱ�
	private static BufferedImage gameover; // �C������
	static {
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover.png");
	}

	Sky sky = new Sky();
	Hero hero = new Hero();
	FlyingObject[] enemies = {};
	Bullet[] bullets = {};
	int x = 0;

	/** �ЫؼĤH��H(�p�ľ�+�j�ľ�+�p�e��) */
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20); // 0~19
		if (type < 7) {
			return new Airplane();
		} else if (type < 14) {
			return new BigAirplane();
		} else {
			return new Bee();
		}
	}

	int enterIndex = 0;

	/** ����ĤH�J�� */
	public void enterAction() { // 10�@���@��
		enterIndex++;
		if (enterIndex % 40 == 0) { // ����C400�@���@��
			FlyingObject obj = nextOne();
			enemies = Arrays.copyOf(enemies, enemies.length + 1);
			enemies[enemies.length - 1] = obj;
		}
	}

	public void stepAction() { // 10�@���@��
		sky.step();
		for (int i = 0; i < enemies.length; i++) {
			enemies[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;

	/** �l�u�J�� */
	public void shootAction() { // 10�@���@��
		shootIndex++;
		if (shootIndex % 30 == 0) { // ����ɶ�, ���l�u�C300�@��o�g�@�o
			Bullet[] bs = hero.shoot(); // shoot()�|�Ыؤl�u�Ʋ�,�ҥH��Bullet[]����
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // �Ʋժ��l�[:
																						// �N�s���a���ª��̫᭱�@��U�ФF
		}
	}

	public void outOfBoundsAction() { // �brun��k��, �C10�@���@�� // �]�A�ĤH�M�l�u
		int index = 0;
		FlyingObject[] enemyLives = new FlyingObject[enemies.length];
		for (int i = 0; i < enemies.length; i++) { // �M���Ҧ��ĤH
			FlyingObject f = enemies[i]; // ����C�ӼĤH
			if (!f.outOfBounds() && !f.isRemove()) { // ���V��
				enemyLives[index] = f; // �N�ĤH�K�[��s�Ʋ�
				index++; // �K�[�����۪��ĤH�~��
			}
		}
		enemies = Arrays.copyOf(enemyLives, index); // �NenemyLives��ȵ�enemies,
													// ���׬�index(�����۪��ĤH),
													// ��ldiscard

		index = 0; // ���k�s��count�l�u���۪��Ӽ�
		Bullet[] bulletLives = new Bullet[bullets.length]; // ���@�ӷs���Ʋոˤ��V�ɤl�u
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds() && !b.isRemove()) { // �u�n���V��, �N��ȵ��s���Ʋ�, ��count+1, �����V�ɼĤH�R���F
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}

	/** �l�u�P�ĤH�I�� */
	int score = 0; // ���a�o��

	public void bulletBangAction() { // 10�@���@��
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			for (int j = 0; j < enemies.length; j++) {
				FlyingObject f = enemies[j];
				if (b.isLife() && f.isLife() && f.hit(b)) {
					b.goDead(); // �l�u�h��
					f.goDead(); // �ĤH�h��
					if (f instanceof Enemy) { // �p�G�Q����H��Enemy(�p�ľ�, �j�ľ�)
						Enemy e = (Enemy) f; // �j�ରEnemy ��getScore��k
						score += e.getScore(); // �o��
					}
					if (f instanceof Award) { // �p�G��H���p�e��
						Award a = (Award) f; // �j��
						int type = a.getType();
						switch (type) { // �ھ�type���o���y
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire(); // ���o���O��
							break;
						case Award.LIFE: // ���o�ͩR
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}

	/** �^�����P�ĤH�I�� */
	public void heroBangAction() {
		for (int i = 0; i < enemies.length; i++) {
			FlyingObject f = enemies[i];
			if (f.isLife() && f.hit(hero)) {
				f.goDead();
				hero.subtractLife();
				hero.clearDoubleFire();
			}
		}
	}

	/** �˴��C������ */
	public void checkGameOverAction() {
		if(hero.getLife() <= 0) { // �C�������F
			state = GAME_OVER; // �N��e���A�קאּ�C���������A
		}
	}

	/** �{�ǱҰʰ��� */
	public void action() {
		// �Ыؤ@�Ӱ�ť����H, �ΦW������
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX(); // ���x�y��
					int y = e.getY(); // ���y�y��
					hero.moveTo(x, y); // �^�����H�۹��в���
				}
			}
			public void mouseClicked(MouseEvent e) {
				switch(state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					sky = new Sky();
					hero = new Hero();
					enemies = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START; // �קאּ�Ұʪ��A
					break;
				}
			}
			/** ���в��X�ƥ� */
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING) {
					state = PAUSE;
				}
			}
			
			/** ���в��J�ƥ� */
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE) {
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l); // ��ť�ƥ�
		this.addMouseMotionListener(l); // �[��в���

		Timer timer = new Timer(); // �Ыةw�ɾ���H
		timer.schedule(new TimerTask() { // �C10�@���@��
			public void run() {
				if (state == RUNNING) { // ��e���A���B�檬�A��, �~�Ұ�
					enterAction(); // �ĤH�J��
					stepAction(); // ���檫����
					shootAction(); // �o�g�l�u
					outOfBoundsAction(); // �R���V�ɪ����檫
					bulletBangAction(); // �l�u�P�ĤH�I��
					heroBangAction(); // �^�����P�ĤH�I��
					checkGameOverAction(); // �˴��C������
				}
				repaint(); // ���e
			}
		}, 10, 10); // �w�ɭp�e
	}

	public void paint(Graphics g) {
		sky.paintObject(g);
		hero.paintObject(g);

		for (int i = 0; i < bullets.length; i++) {
			bullets[i].paintObject(g);
		}

		for (int i = 0; i < enemies.length; i++) {
			enemies[i].paintObject(g);
		}

		g.drawString("SCORE: " + score, 10, 25); // �e��(��ܤ���)
		g.drawString("LIFE: " + hero.getLife(), 10, 45); // �e�R(��ܩR��)

		switch (state) { // �ھڪ��A��ܶK��
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(); // �Ыؤ@�ӵ��f��H
		World world = new World(); // �ЫؤF�@�ӭ��O��H
		frame.add(world); // �N���O�K�[�쵡�f��
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �]�m�������f�ɰh�X�{��
		frame.setSize(WIDTH, HEIGHT); // �]�m���f���j�p
		frame.setLocationRelativeTo(null); // �]�m���f�~�����
		frame.setVisible(true); // �]�m���f�i�� //�ɧֽ�paint()

		world.action(); // �Ұʰ���{��
	}

}
