/**
 * 
 */
package test0311;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Topin@JSC
 *
 */
public class Server {

	public static String userName = null;

	public static void main(String[] args) throws IOException {
		new Server().init();
	}

	// 所有的客户端
	private List<UserSocket> clients = new ArrayList<>();

	public void init() throws IOException {
		ServerSocket serverSocket = new ServerSocket(8888); // 建立连接
		System.out.println("服务器启动:" + serverSocket);

		while (true) {
			Socket client = serverSocket.accept(); // 等待请求 - 只运行一次

			// 用户上线

			new Thread() {

				@Override
				public void run() {
					try {

						// 边读边写
						InputStream is = client.getInputStream()/* 客户端的输入流 */;
						byte[] bytes = new byte[1024];
						int lenght = -1;
						String name1 = userName;

						while ((lenght = is.read(bytes)) != -1) {

							String msk = new String(bytes, 0, lenght);

							if (name1 != null) {

								Pattern compile = Pattern.compile("^@(.)\\s+.*$");
								Matcher matcher = compile.matcher(msk);
								if (matcher.find()) { // 私聊
									String name = matcher.group(1);
									String ms1 = matcher.group(2);
									// 向客户端发送消息
									for (UserSocket socket : clients) {
										if (socket.getName().equals(name)) {
											try {
												OutputStream os = socket.getSocket().getOutputStream();
												os.write(ms1.getBytes("UTF-8"));
											} catch (Exception e) {
												// 用户下线
												clients.remove(socket);
												e.printStackTrace();
											}
										}
									}
								} else {
									// 发送的消息
									broadcast(name1 + " ： " + msk, clients);
								}
							} else {
								name1 = new String(bytes, 0, lenght);
								String mask = name1 + "上线了";
								System.out.println(mask);
								broadcast(mask, clients);
								clients.add(new UserSocket(client, name1));
							}
						}

						// 读取客户端的发送的消息

						// 阻塞:如果客户端没有发送,一直等待有消息为止, 如果客户端 - reset Exception 断开连接 客户端发送的消息

					} catch (Exception e) {
						// 用户下线
						clients.remove(client);
						e.printStackTrace();
					}
				}
				// 线程: "同时执行"

			}.start(); // 1. 线程开始(while - 进行下一次) 2.线程中代码执行
		}
	}

	/**
	 * 广播
	 * 
	 * @param msg
	 */
	public void broadcast(String msg, List<UserSocket> clients) {
		// 向客户端发送消息
		for (UserSocket socket : clients) {
			try {
				OutputStream os = socket.getSocket().getOutputStream();

				os.write(msg.getBytes("UTF-8"));
			} catch (Exception e) {
				// 用户下线
				clients.remove(socket);
				e.printStackTrace();
			}
		}
	}
}
