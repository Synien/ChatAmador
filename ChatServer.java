package ChatAmador;

import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
	
	private int port;
	private List<Socket> clientes;
	
	public Server(int port){
		this.port = port;
		this.clientes = new ArrayList<>();
	}
	
	public void exec() throws IOException {
		try(ServerSocket server = new ServerSocket (this.port)){
			System.out.println("Port aberto.");
			
			while (true){
				Socket cliente = server.accept();
				System.out.println("Conexão com " + cliente.getInetAddress().getHostAddress());
				
				this.clientes.add(cliente);
				
				MessageTreat mt = new MessageTreat(cliente, this);
				new Thread(mt).start();
			}
		}
	}
	
	public void messageDist(Socket clientSent, String msg){
		for (Socket cliente : this.clientes){
			if(!cliente.equals(clientSent)){
				try {
					PrintStream ps = new PrintStream(cliente.getOutputStream);
					ps.println(msg);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}

public class MessageTreat implements Runnable {
	
	private Socket cliente;
	private Server server;
	
	public MessageTreat(Socket cliente, Server server){
		this.cliente = cliente;
		this.server = server;
	}
	
	public void run(){
		try(Scanner s = new Scanner (this.cliente.getInputStream())){
			while(s.hasNextLine()){
				server.messageDist(this.cliente, s.nextLine());
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}

public class ServerRun{
	public static void main (String[]args) throws Exception {
		new Server(100).exec();
	}
}