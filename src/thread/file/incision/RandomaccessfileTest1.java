package thread.file.incision;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomaccessfileTest1 {

	public static void main(String[] args) throws IOException {
		
		RandomAccessFile raf =  new RandomAccessFile("src/并发编程_线程安全-并发合集-读写锁_2019-03-15_161432.wmv", "r");
		
		RandomAccessFile rafAll = new RandomAccessFile("src/合并.wmv", "rw");
		
		//切割成count个文件再合并,count等于几，就切割成几份文件
		int count = 10;
		
		int count1 = count;
		
		int tempcount = 0;
		
		int name = 1;
		
		if(raf.length()%count!=0) {
			
			tempcount = (int)raf.length()%count;
		}
		
		byte[]b = new byte[(int)raf.length()/count];
		
		while(true) {
			
			//切割刚好没有余下的字节
			if(tempcount==0) {
				
				RandomAccessFile tempRaf = new RandomAccessFile("Test2切割文件"+name+++".wmv", "rw");
				
				raf.read(b);
				
				tempRaf.write(b);
				
				rafAll.write(b);
				
				tempRaf.close();
				
				if(rafAll.length()==raf.length()) {
					
					break;
				}
				
				//切割有余下的字节
			}else {
				
				RandomAccessFile tempRaf = new RandomAccessFile("Test2切割文件"+name+++".wmv", "rw");
				
				if(count1==1) {
					
					byte[]b1 = new byte[(int)(raf.length()/count)+tempcount];

					raf.readFully(b1);
					
					tempRaf.write(b1);
					
					rafAll.write(b1);
				
					break;
					
				}else {
					
					raf.readFully(b);
					
					rafAll.write(b);
					
					tempRaf.write(b);
					
					count1--;
				}
				
				tempRaf.close();
			}
			
		}
		
		raf.close();
		
		rafAll.close();
	}
}
