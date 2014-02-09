package Server;

import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class ChatServer implements Runnable {

	private ChatServerThread clients[] = new ChatServerThread[50];
	private ArrayList<GroupUser> group;
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;

	public ChatServer(int port) {
		try {
			System.out
					.println("Binding to port " + port + ", please wait  ...");
			server = new ServerSocket(port);
			System.out.println("Server started: " + server);
			start();
		} catch (IOException ioe) {
			System.out.println("Can not bind to port " + port + ": "
					+ ioe.getMessage());
		}
	}

	public void run() {
		while (thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(server.accept());
			} catch (IOException ioe) {
				System.out.println("Server accept error: " + ioe);
				stop();
			}
		}
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			thread.stop();
			thread = null;
		}
	}

	private int findClient(int ID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getID() == ID)
				return i;
		return -1;
	}

	public synchronized void handle(String userID, int ID, String input) {
		StringTokenizer in = new StringTokenizer(input," ");
		String command = in.nextToken();
		if (command.equals(".bye")) {
			clients[findClient(ID)].send(".bye");
			remove(ID);
		}
		else if (command.equals(".create")){//.createGroup gID
			GroupUser g = new GroupUser(in.nextToken());
			group.add(g);
			g.joinGroup(userID);
		}
		else if (command.equals(".join")){//.joinGroup gID
			String gID = in.nextToken();
			GroupUser g = group.get(group.indexOf(new GroupUser(gID)));
			g.joinGroup(userID);
		}
		else if ( command.equals(".leave")){
			String gID = in.nextToken();
			GroupUser g = group.get(group.indexOf(new GroupUser(gID)));
			g.leaveGroup(userID);
		}
		else if ( command.equals(".enter")){
			String gID = in.nextToken();
			GroupUser g = group.get(group.indexOf(new GroupUser(gID)));
			g.enterGroup(userID);
		}
		else if ( command.equals(".exit")){
			String gID = in.nextToken();
			GroupUser g = group.get(group.indexOf(new GroupUser(gID)));
			g.enterGroup(userID);
		}
		else
			
			for (int i = 0; i < clientCount; i++)
				clients[i].send(ID + ": " + input);
	}

	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if (pos >= 0) {
			ChatServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if (pos < clientCount - 1)
				for (int i = pos + 1; i < clientCount; i++)
					clients[i - 1] = clients[i];
			clientCount--;
			try {
				toTerminate.close();
			} catch (IOException ioe) {
				System.out.println("Error closing thread: " + ioe);
			}
			toTerminate.stop();
		}
	}

	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("Client accepted: " + socket);
			clients[clientCount] = new ChatServerThread(this, socket);
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			} catch (IOException ioe) {
				System.out.println("Error opening thread: " + ioe);
			}
		} else
			System.out.println("Client refused: maximum " + clients.length
					+ " reached.");
	}

	public static void main(String args[]) {
		ChatServer server = null;
		// if (args.length != 1)
		// System.out.println("Usage: java ChatServer port");
		// else
		// server = new ChatServer(Integer.parseInt(args[0]));
		server = new ChatServer(Integer.parseInt("5555"));
	}
}