package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 英雄機: 是飛行物 */
public class Hero extends FlyingObject {
	private static BufferedImage[] images; // 英雄機圖片多張 所以做成數組
	static {
		images = new BufferedImage[6];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("hero" + i + ".png");
		}
	}

	private int life;
	private int doubleFire;

	/** 構造方法 Constructor */
	public Hero() {
		super(97, 124, 140, 400);
		this.life = 3; // 默認3條命
		this.doubleFire = 0; // 單倍火力
	}

	/** 英雄機隨著鼠標移動 x, y = 鼠標的x和y */
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}

	/** 英雄機移動 */
	public void step() {

	}

	int index = 0; // 活著的下標
	int deadIndex = 2; // 死了的下標

	/** 重寫getImage()獲取圖片 */
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

	/** 英雄機發射子彈(創建子彈對象) */
	public Bullet[] shoot() {
		int xStep = this.width / 4; // this指當前對象:英雄機 1/4英雄機的寬
		int yStep = 20; // 固定的, 可隨意
		if (doubleFire > 0) { // 判斷是否火力值
			// 創建子彈對象
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x + xStep * 1, this.y - yStep);
			bs[1] = new Bullet(this.x + xStep * 3, this.y - yStep);
			doubleFire -= 2; // 發射一次, 火力值減2
			return bs;
		} else { // 如果無火力值, 發射單發子彈(創建一個子彈數組, 一個元素(子彈))
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x + xStep * 2, this.y - yStep);
			return bs;
		}
	}
	
	public boolean outOfBounds() {
		return false; //英雄機永不越界, 越界算暫停
	}
	
	/** 得命 */
	public void addLife() {
		life++;
	}
	
	public int getLife() {
		return life;  // 返回命數
	}
	
	/** 英雄機減命 */
	public void subtractLife() { // 撞上生命減一
		life--;
	}
	
	/** 清空火力值 */
	public void clearDoubleFire() { // 撞上火力值歸零
		doubleFire = 0;
	}
	
	/** 得火力值 */
	public void addDoubleFire() {
		doubleFire += 40;
	}
	
	

}
