package cn.tedu.shoot;

import java.util.Random;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 小蜜蜂 */
public class Bee extends FlyingObject implements Award {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for (int i = 0; i < images.length; i++) {
			images[i] = loadImage("bee" + i + ".png");
		}
	}

	private int xSpeed; // 小蜜蜂會橫著走
	private int ySpeed;
	private int awardType; // 獎勵類型(0或1)

	public Bee() {
		super(60, 50);
		Random rand = new Random();
		xSpeed = 1;
		ySpeed = 2;
		awardType = rand.nextInt(2); // 0到1之內的隨機數(2 = 0, 1)
	}

	/** 小蜜蜂移動 */
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if (x <= 0 || x >= World.WIDTH - this.width) {
			xSpeed *= -1; // 正的變負的, 負的變正的
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
		return this.y >= World.HEIGHT; // this指的是當前對象: 小蜜蜂 >= 窗口高 == 越界
	}
	
	public int getType(){
		return awardType; // 返回獎勵類型
	}

}