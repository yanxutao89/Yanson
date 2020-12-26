package asm;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/26 17:28
 */
public class CCC {
	public static long timer;

	public void m() throws Exception {
		long t = System.currentTimeMillis();
		Thread.sleep(100);
		timer += System.currentTimeMillis() - t;
	}
}
