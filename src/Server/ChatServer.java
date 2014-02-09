package Server;

import java.net.*;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.*;

import Message.Message;

public class ChatServer implements Runnable {

	private ChatServerThread clients[] = new ChatServerThread[50];
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	private ServerSocket server = null;
	private Thread thread = null;
	private int clientCount = 0;
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	private Date time = new Date();

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

	private int findUser(String userID) {
		for (int i = 0; i < clientCount; i++)
			if (clients[i].getUserID() == userID)
				return i;
		return -1;
	}

	public synchronized void handle(String userID, int ID, String input) {
		StringTokenizer in = new StringTokenizer(input, " ");
		String command = in.nextToken();
		System.out.println(command);
		if (command.equals(".bye")) {
			clients[findClient(ID)].send(".bye");
			remove(ID);
		} else if (command.equals(".create")) {// .createGroup gID
			String gID = in.nextToken();
			Group g = new Group(gID);
			groups.add(g);
			g.joinGroup(userID);
			getUser(userID).addGroup(gID);
			System.out.println("create");
		} else if (command.equals(".join")) {// .joinGroup gID
			String gID = in.nextToken();
			Group g = getGroup(gID);
			g.joinGroup(userID);
			getUser(userID).addGroup(gID);
			clients[findClient(ID)].setGroupID(gID);
			System.out.println("join");
		} else if (command.equals(".leave")) {
			String gID = in.nextToken();
			Group g = getGroup(gID);
			g.leaveGroup(userID);
			getUser(userID).deleteGroup(gID);
			clients[findClient(ID)].setGroupID("");
			System.out.println("leave");
		} else if (command.equals(".enter")) {
			String gID = in.nextToken();
			Group g = getGroup(gID);
			g.enterGroup(userID);
			clients[findClient(ID)].setGroupID(gID);
			clients[findClient(ID)].send(g.getUnread(userID));
			System.out.println("enter");
		} else if (command.equals(".exit")) {
			String gID = in.nextToken();
			Group g = getGroup(gID);
			g.exitGroup(userID);
			clients[findClient(ID)].setGroupID("");
			System.out.println("exit");
		} else if (command.equals(".login")) {
			String uID = in.nextToken();
			UserInfo loginUser = new UserInfo(uID);
			if (users.contains(loginUser)) {
				String gListString = users.get(users.indexOf(uID))
						.getGroupList().toString();
				clients[findClient(ID)].send(".groupList " + gListString);
			} else {
				users.add(loginUser);
				clients[findClient(ID)].send(".newUser");
			}
			System.out.println("login");
		} else {
			String gID = clients[findClient(ID)].getGroupID();
			ArrayList<String> sendList = getGroup(gID).sendMsg(
					new Message(input, userID, dateFormat.format(time)));

			for (int i = 0; i < sendList.size(); i++)
				clients[findUser(sendList.get(i))].send(ID + ": " + input);
			System.out.println(ID + ": " + input);
		}
	}

	public UserInfo getUser(String uID) {
		return users.get(users.indexOf(new UserInfo(uID)));
	}

	public Group getGroup(String gID) {
		return groups.get(groups.indexOf(new Group(gID)));
	}

	public void sendTo() {

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