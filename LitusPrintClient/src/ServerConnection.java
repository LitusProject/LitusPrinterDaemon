import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String id;

	public ServerConnection(Socket socket, String id) {
		this.id = id;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendGreeting() {
		try {
			out.write("CONNECT " + id + "\n");
			out.flush();
		} catch (Exception e) {

		}

	}

	public void startListening() {

		while (true) {
			try {
				String s = in.readLine();
				if (s != null) {
					try {

						Ticket ticket = Ticket.fromJson(s);

						if (ticket.getType() == 1) {
							Printer.getInstance().printWaitingTicket(ticket);
						} else if (ticket.getType() == 2) {
							Printer.getInstance().printCollectTicket(ticket);
						} else if (ticket.getType() == 3) {
							Printer.getInstance().printBillTicket(ticket);
						}

					} catch (Exception e) {
						System.out.println("> Error: " + e.getMessage());
						System.out.println("> Received string: '"+s+"'");
						System.out.println("> Server does not use the LPS protocol, disconnecting ...");
						e.printStackTrace();
						socket.close();
					}
				}
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}