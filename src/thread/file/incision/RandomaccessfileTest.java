package thread.file.incision;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomaccessfileTest {
	
	static RandomaccessfileTest rt =new RandomaccessfileTest();
		
	 RandomAccessFile raf = null;

	 int count = 10;

	 int count1 = count;

	 int tempcount = 0;

	 int name = 1;

	public synchronized static void increase() throws IOException {

		if (rt.raf.length() % rt.count != 0) {

			rt.tempcount = (int) rt.raf.length() % rt.count;
		}
		byte[] b = new byte[(int) rt.raf.length() / rt.count];

		// 切割没有余下的字节
		if (rt.tempcount == 0) {

			RandomAccessFile tempRaf = new RandomAccessFile("Test" + rt.name++ + ".wmv", "rw");

			rt.raf.read(b);

			tempRaf.write(b);
			
			tempRaf.close();
			
			// 切割有余下的字节
		} else {

			RandomAccessFile tempRaf = new RandomAccessFile("Test" + rt.name++ + ".wmv", "rw");

			if (rt.count1 == 1) {

				byte[] b1 = new byte[(int) (rt.raf.length() / rt.count) + rt.tempcount];

				rt.raf.readFully(b1);

				tempRaf.write(b1);
				
			} else {

				rt.raf.readFully(b);

				tempRaf.write(b);

				rt.count1--;
			}

			tempRaf.close();
		}

	}

	public static void main(String[] args) throws FileNotFoundException {

		rt.raf = new RandomAccessFile("src/并发编程_线程安全-并发合集-读写锁_2019-03-15_161432.wmv", "r");

		for (int i = 0; i < 10; i++) {
			new Thread() {
				public void run() {
					try {
						increase();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			}.start();

		}
	}
}
