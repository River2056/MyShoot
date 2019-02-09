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
	public static final int WIDTH = 400; // 窗口寬
	public static final int HEIGHT = 700; // 窗口高

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int state = START; // 當前狀態(默認為啟動狀態)

	private static BufferedImage start; // 啟動
	private static BufferedImage pause; // 暫停
	private static BufferedImage gameover; // 遊戲結束
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

	/** 創建敵人對象(小敵機+大敵機+小蜜蜂) */
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

	/** 控制敵人入場 */
	public void enterAction() { // 10毫秒走一次
		enterIndex++;
		if (enterIndex % 40 == 0) { // 控制每400毫秒走一次
			FlyingObject obj = nextOne();
			enemies = Arrays.copyOf(enemies, enemies.length + 1);
			enemies[enemies.length - 1] = obj;
		}
	}

	public void stepAction() { // 10毫秒走一次
		sky.step();
		for (int i = 0; i < enemies.length; i++) {
			enemies[i].step();
		}
		for (int i = 0; i < bullets.length; i++) {
			bullets[i].step();
		}
	}

	int shootIndex = 0;

	/** 子彈入場 */
	public void shootAction() { // 10毫秒走一次
		shootIndex++;
		if (shootIndex % 30 == 0) { // 控制時間, 讓子彈每300毫秒發射一發
			Bullet[] bs = hero.shoot(); // shoot()會創建子彈數組,所以用Bullet[]接收
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // 數組的追加:
																						// 將新的家到舊的最後面一位下標了
		}
	}

	public void outOfBoundsAction() { // 在run方法裡, 每10毫秒走一次 // 包括敵人和子彈
		int index = 0;
		FlyingObject[] enemyLives = new FlyingObject[enemies.length];
		for (int i = 0; i < enemies.length; i++) { // 遍歷所有敵人
			FlyingObject f = enemies[i]; // 獲取每個敵人
			if (!f.outOfBounds() && !f.isRemove()) { // 不越界
				enemyLives[index] = f; // 將敵人添加到新數組
				index++; // 添加完活著的敵人才跳
			}
		}
		enemies = Arrays.copyOf(enemyLives, index); // 將enemyLives賦值給enemies,
													// 長度為index(有活著的敵人),
													// 其餘discard

		index = 0; // 先歸零來count子彈活著的個數
		Bullet[] bulletLives = new Bullet[bullets.length]; // 做一個新的數組裝不越界子彈
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds() && !b.isRemove()) { // 只要不越界, 就賦值給新的數組, 並count+1, 等於把越界敵人刪除了
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}

	/** 子彈與敵人碰撞 */
	int score = 0; // 玩家得分

	public void bulletBangAction() { // 10毫秒走一次
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			for (int j = 0; j < enemies.length; j++) {
				FlyingObject f = enemies[j];
				if (b.isLife() && f.isLife() && f.hit(b)) {
					b.goDead(); // 子彈去死
					f.goDead(); // 敵人去死
					if (f instanceof Enemy) { // 如果被撞對象為Enemy(小敵機, 大敵機)
						Enemy e = (Enemy) f; // 強轉為Enemy 調getScore方法
						score += e.getScore(); // 得分
					}
					if (f instanceof Award) { // 如果對象為小蜜蜂
						Award a = (Award) f; // 強轉
						int type = a.getType();
						switch (type) { // 根據type取得獎勵
						case Award.DOUBLE_FIRE:
							hero.addDoubleFire(); // 取得火力值
							break;
						case Award.LIFE: // 取得生命
							hero.addLife();
							break;
						}
					}
				}
			}
		}
	}

	/** 英雄機與敵人碰撞 */
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

	/** 檢測遊戲結束 */
	public void checkGameOverAction() {
		if(hero.getLife() <= 0) { // 遊戲結束了
			state = GAME_OVER; // 將當前狀態修改為遊戲結束狀態
		}
	}

	/** 程序啟動執行 */
	public void action() {
		// 創建一個偵聽器對象, 匿名內部類
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX(); // 獲取x座標
					int y = e.getY(); // 獲取y座標
					hero.moveTo(x, y); // 英雄機隨著鼠標移動
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
					state = START; // 修改為啟動狀態
					break;
				}
			}
			/** 鼠標移出事件 */
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING) {
					state = PAUSE;
				}
			}
			
			/** 鼠標移入事件 */
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE) {
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l); // 偵聽事件
		this.addMouseMotionListener(l); // 觀察鼠標移動

		Timer timer = new Timer(); // 創建定時器對象
		timer.schedule(new TimerTask() { // 每10毫秒走一次
			public void run() {
				if (state == RUNNING) { // 當前狀態為運行狀態時, 才啟動
					enterAction(); // 敵人入場
					stepAction(); // 飛行物移動
					shootAction(); // 發射子彈
					outOfBoundsAction(); // 刪除越界的飛行物
					bulletBangAction(); // 子彈與敵人碰撞
					heroBangAction(); // 英雄機與敵人碰撞
					checkGameOverAction(); // 檢測遊戲結束
				}
				repaint(); // 重畫
			}
		}, 10, 10); // 定時計畫
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

		g.drawString("SCORE: " + score, 10, 25); // 畫分(顯示分數)
		g.drawString("LIFE: " + hero.getLife(), 10, 45); // 畫命(顯示命數)

		switch (state) { // 根據狀態選擇貼圖
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
		JFrame frame = new JFrame(); // 創建一個窗口對象
		World world = new World(); // 創建了一個面板對象
		frame.add(world); // 將面板添加到窗口中
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 設置關閉窗口時退出程序
		frame.setSize(WIDTH, HEIGHT); // 設置窗口的大小
		frame.setLocationRelativeTo(null); // 設置窗口居中顯示
		frame.setVisible(true); // 設置窗口可見 //盡快調paint()

		world.action(); // 啟動執行程序
	}

}
