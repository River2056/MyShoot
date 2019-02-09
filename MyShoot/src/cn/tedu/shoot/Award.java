package cn.tedu.shoot;

public interface Award {
	public int DOUBLE_FIRE = 0; // 火力值
	public int LIFE = 1; // 命
	/** 獲取獎勵類型 */
	public int getType();
}
