package cn.tedu.shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject {
	private static BufferedImage image; // 圖片
	static {
		image = loadImage("background.png");
	}

	private int speed;
	private int y1;

	public Sky() {
		super(World.WIDTH, World.HEIGHT, 0, 0);
		speed = 1;
		y1 = -height; // y1: 負的窗口的高
	}

	/** 天空移動 */
	public void step() {
		y += speed;
		y1 += speed;
		if (y >= World.HEIGHT) { // 若y>=天空 的高(到下面了)
			y = -this.height;    // 則設置y為負的高
		}
		if (y1 >= World.HEIGHT) {// 同y
			y1 = -this.height;
		}
	}

	public BufferedImage getImage() {
		return image;
	}

	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y, null); // 畫天空
		g.drawImage(getImage(), x, y1, null); // 畫天空2
	}
	
	public boolean outOfBounds() {
		return false; //天空永不越界; 兩張圖來回切換, 所以不會越界
	}
}
