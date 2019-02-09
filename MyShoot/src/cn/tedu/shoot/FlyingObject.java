package cn.tedu.shoot;

import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;

public abstract class FlyingObject { // 抽出所有類別共有屬性(超類), 讓派生類繼承
	public static final int LIFE = 0; // 活著的
	public static final int DEAD = 1; // 死了的
	public static final int REMOVE = 2; // 可以刪除的
	protected int state = LIFE; // 當前狀態(默認為活著的)

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

	public FlyingObject() { // 留著默認給英雄機, 天空, 子彈

	}

	public FlyingObject(int width, int height) { // 抽出共有: 小敵機, 大敵機, 小蜜蜂
		this.width = width;
		this.height = height;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH - this.width);
		y = -this.height;
	}

	/** 飛行物移動 */
	public abstract void step();

	/** 獲取圖片 */
	public abstract BufferedImage getImage();

	// hero.paintObject(g);
	// sky.paintObject(g);
	/** 畫對象 g:畫筆 */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null);
	}

	/** 加載/讀取圖片 */
	public static BufferedImage loadImage(String fileName) { // 加參數是為了知道要讀取誰
		try {
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/** 判斷是否活著呢 */
	public boolean isLife() {
		return state == LIFE;
	}

	/** 判斷是否死了的 */
	public boolean isDead() {
		return state == DEAD;
	}

	/** 判斷是否可以刪除的 */
	public boolean isRemove() {
		return state == REMOVE;
	}

	/** 判斷飛行物是否越界 */
	public abstract boolean outOfBounds();
	
	/** 檢測敵人與子彈和英雄機的碰撞  this: 敵人  other: 英雄機, 子彈 */
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
