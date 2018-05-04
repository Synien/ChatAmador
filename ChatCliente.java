package ChatAmador;

import java.util.*;
import java.io.*;
import java.net.*;

public class Cliente{
	
	private String host;
	private int port;
	
	public void Cliente(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public void exec() throws UnknownHostException, IOException{ //Enviar a mensagem para o servidor
		try (Socket cliente = new Socket(this.host, this.port);
			Scanner sc = new Scanner(System.in);
			PrintStream out = new PrintStream(cliente.getOutputStream())){
				System.out.println("Cliente conectado!");
				MessageReceiver msgr = new MessageReceiver(cliente.getInputStream());
				new Thread(msgr).start();
				
				while (sc.hasNextLine()){
					out = println(sc.nextLine());
				}
			}
	}
}

public class MessageReceiver implements Runnable { //Receber a mensagem do servidor
	private InputStream server;
	
	public MessageReceiver (InputStream server){
		this.server = server;
	}	
	
	public void exec2(){
		try(Scanner sc2 = new Scanner (this.server)){
			while (sc2.hasNextLine()){
				System.out.println(sc2.nextLine());
			}
		}
	}
}

public class ClientRun{
	public static void main (String[]args) throws UnknownHostException, IOException{
		new Cliente("127.0.0.1", 100).exec(); //host local
	}
}