package test0311;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Chat extends JFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JTextArea recieveMsgText;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public Chat() throws UnknownHostException, IOException {
		final Socket socket = new Socket("192.168.6.88", 8888);
		OutputStream os = socket.getOutputStream();	
		while (true) {
			String name = JOptionPane.showInputDialog("请输入用户名 ");
			if(name!=null &&!name.trim().isEmpty()) {
				os.write(name.getBytes("UTF-8"));
				break;
			}
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 456, 408);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 420, 249);
		getContentPane().add(scrollPane);

		recieveMsgText = new JTextArea();
		scrollPane.setViewportView(recieveMsgText);

		JPanel panel = new JPanel();
		panel.setBounds(10, 269, 420, 91);
		getContentPane().add(panel);
		panel.setLayout(null);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 5, 420, 46);
		panel.add(textArea);

		JButton button = new JButton("\u53D1\u9001");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					OutputStream os = socket.getOutputStream();
					String text = textArea.getText();
					if (text.trim().isEmpty()) return;
					os.write(textArea.getText().getBytes("UTF-8"));
					textArea.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}
		});
		button.setBounds(311, 58, 93, 23);
		panel.add(button);

		// 读
		new Thread() {
			public void run() {
				try {
					// 一直读
					InputStream is = socket.getInputStream()/* 客户端的输入流 */;
					byte[] bytes = new byte[1024];
					int lenght = -1;
					// 读取客户端的发送的消息
					while ((lenght = is.read(bytes)) != -1) {
						String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
						String msg = new String(bytes, 0, lenght);
						recieveMsgText.append(now + "\t" + msg + "\n");
						recieveMsgText.setCaretPosition(recieveMsgText.getText().length());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void recieve(String name, String msg) {
		String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		recieveMsgText.append(now + "\t" + name + "\n" + msg + "\n\n");
	}
}
