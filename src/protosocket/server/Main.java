package protosocket.server;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MoServer server = new MoServer(Integer.parseInt("9090"), Integer.parseInt("5"));
		//System.out.println("�������շ������߳�");
		server.setUpHandlers("utf-8" );
		System.out.println("start receive thread");
		server.acceptConnections();
		//System.out.println("start server");
	 
	}

}
