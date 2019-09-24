package test0311;

import java.net.Socket;

public class UserSocket {

	private Socket socket;
	private String name;
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserSocket [socket=" + socket + ", name=" + name + "]";
	}
	
	public UserSocket() {}
	public UserSocket(Socket socket, String name) {
		super();
		this.socket = socket;
		this.name = name;
	}
	
	
}
