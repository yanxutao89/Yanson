package asm;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 16:19
 */
public class CC {
	public static long timer;

	public void m() throws Exception {
		timer -= System.currentTimeMillis();
		Thread.sleep(100);
		timer += System.currentTimeMillis();
	}
}
