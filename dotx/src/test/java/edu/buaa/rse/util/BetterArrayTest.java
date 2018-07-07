package edu.buaa.rse.util;

public class BetterArrayTest {

	public static void main(String[] args) {
		BetterMap bm = new BetterMap(3);
		//bm.put((float) 1.3, "1.3");
		//bm.put((float) 1.4, "1.4");

		//bm.put((float) 1.5, "1.5");
		//bm.put((float) 1.6, "1.6");
		//bm.put((float) 0, "0");
		//bm.put((float) 2, "2");



		Object[] values = bm.values();

		String a = "";
	
		long now1 = System.currentTimeMillis();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long now2 = System.currentTimeMillis();
		long dration = now2-now1;
		System.out.println(dration/1000);
		
	}

}
