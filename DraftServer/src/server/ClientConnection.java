package server;

public class ClientConnection{

	/**
	 * 
	 */
	public String host;
	public String address;
	public int port;
	
	public ClientConnection(String host, String add, int port){
		this.host = host;
		this.address = add;
		this.port = port;
	}
	
}
