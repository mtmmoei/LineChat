package Server;

import java.io.*;
import java.util.ArrayList;

public class UserInfo implements Comparable<UserInfo> {
	// private ArrayList<String> messageOut;
	private String userID;
	private int lastMsgNo;
	private boolean online;

	public UserInfo(String uID) {
		userID = uID.toLowerCase();
		online = false;
	}
	
	public String getUID(){
		return userID;
	}

	public int getLastMsgNo() {
		return lastMsgNo;
	}

	public int setLastMsgNo(int msgNo) {
		int oldNo = lastMsgNo;
		lastMsgNo = msgNo;
		return oldNo;
	}

	public void setOnline(boolean b) {
		online = b;
	}

	@Override
	public int compareTo(UserInfo o) {
		// TODO Auto-generated method stub
		if (userID.equals(o.getUID().toLowerCase()))
			return 1;
		else
			return 0;
	}
}	
