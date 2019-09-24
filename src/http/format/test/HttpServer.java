package http.format.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Topin@JSC
 *
 */
public class HttpServer {

	public static void main(String[] args) throws IOException {
		// 1. TCP服务器
		ServerSocket serverSocket = new ServerSocket(8888);
		// 2. 当前线程不中断 就一直运行
		while (!Thread.currentThread().isInterrupted()) {
			// 3. 接收一个新的客户端
			Socket socket = serverSocket.accept();
			// 4. 读取消息
			InputStream is = socket.getInputStream();
			byte[] bytes = new byte[1024 * 1024 * 8];
			int length = is.read(bytes);
			String requestLine = new String(bytes, 0, length, "UTF-8").split("\r\n")[0].split("\\s+")[1];

			Date date = new Date();
			String content = "无匹配的日期格式";
			if (requestLine.contains("/dateformat?pattern=yyyy-MM-dd HH:mm:ss&zoneId=-02")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("pattern=yyyy-MM-dd HH:mm:ss&zoneId=-02");
				content = simpleDateFormat.format(date);
			} else if (requestLine.contains("/dateformat?pattern=yyyy-MM-dd:mm ss&zoneId=+02")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("pattern=yyyy-MM-dd:mm ss&zoneId=+02");
				content = simpleDateFormat.format(date);
			} else if (requestLine.contains("/dateformat?pattern=yyyy-MM-dd HH:mm:ss")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("pattern=yyyy-MM-dd HH:mm:ss");
				content = simpleDateFormat.format(date);
			} else if (requestLine.contains("/dateformat/yyyy-MM-dd HH:mm:ss?zoneId=+02")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss?zoneId=+02");
				content = simpleDateFormat.format(date);
			} else if (requestLine.contains("/dateformat/yyyy-MM-dd HH:mm:ss")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				content = simpleDateFormat.format(date);
			} else if (requestLine.contains("/dateformat/yyyy-MM-dd yyyy-MM-dd")) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
				content = simpleDateFormat.format(date);
			}
			// 5. 响应消息
			bytes = content.getBytes("UTF-8");
			StringBuffer http = new StringBuffer();
			// 请求行: HTTP/1.1 200 OK
			http.append("HTTP/1.1 200 OK\r\n");
			// 内容类型: Content-Type:
			http.append("Content-Type: text/html;charset=UTF-8\r\n");
			// 内容长度: Content-Length:
			http.append("Content-Length: ").append(bytes.length).append("\r\n");
			// 空行
			http.append("\r\n");
			// 内容
			http.append(content);

			OutputStream os = socket.getOutputStream();
			os.write(http.toString().getBytes("UTF-8"));
			os.flush();

			socket.close();
		}
	}
}