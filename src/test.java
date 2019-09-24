import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
		System.out.println(simpleDateFormat.format(date));
		
	}
}