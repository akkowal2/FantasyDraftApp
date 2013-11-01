package connection;

public class RunConnection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connect = new Connection();
		
		connect.printData();
		
		connect.addData("This is my data");
		
		connect.printData();

	}

}
