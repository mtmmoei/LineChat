package Server;

import java.io.*;
import java.util.ArrayList;

public class UserInfo implements Comparable<String> {
	// private ArrayList<String> messageOut;
	private String userID;
	private int lastMsgNo;
	private boolean online;

	public UserInfo(String uID) {
		userID = uID.toLowerCase();
		online = false;
	}

	public int getLastMsgNo() {
		return lastMsgNo;
	}

	public int setLastMsgNo(int msgNo) {
		int oldNo = lastMsgNo;
		lastMsgNo = msgNo;
		return oldNo;
	}

	@Override
	public int compareTo(String o) {
		// TODO Auto-generated method stub
		if (userID.equals(o.toLowerCase()))
			return 1;
		else
			return 0;
	}

	public void setOnline(boolean b) {
		online = b;
	}
}	
