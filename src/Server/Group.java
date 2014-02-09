package Server;

import java.util.ArrayList;
import java.util.Vector;

import Message.Message;

public class Group implements Comparable<Group> {
	private String groupID;
	private ArrayList<UserInGroup> userInGroups = new ArrayList<UserInGroup>();
	private ArrayList<Message> messages = new ArrayList<Message>();
	private int totalUser;
	private int lastStoreMsgNo;

	public Group(String gID) {
		groupID = gID.toLowerCase();
		totalUser = 0;
		lastStoreMsgNo = 0;
	}

	public String getGID() {
		return groupID;
	}

	public void joinGroup(String uID) {
		UserInGroup user1 = new UserInGroup(uID);
		userInGroups.add(user1);
		user1.setLastMsgNo(lastStoreMsgNo);
		totalUser++;
	}

	public void leaveGroup(String uID) {
		userInGroups.remove(new UserInGroup(uID));
		totalUser--;
	}

	public void enterGroup(String uID) {
		UserInGroup user = userInGroups.get(userInGroups
				.indexOf(new UserInGroup(uID)));
		user.setOnline(true);
	}

	public void exitGroup(String uID) {
		UserInGroup user = userInGroups.get(userInGroups
				.indexOf(new UserInGroup(uID)));
		user.setOnline(false);
	}

	public ArrayList<String> sendMsg(Message msg) {
		ArrayList<String> uList = new ArrayList<String>();
		int uCount = 0;
		lastStoreMsgNo++;
		for (int i = 0; i < userInGroups.size(); i++) {
			UserInGroup user = userInGroups.get(i);
			if (user.isOnline()) {
				uList.add(user.getUID());
				user.setLastMsgNo(lastStoreMsgNo);
				uCount++;
			}
		}
		if (uCount < userInGroups.size()) {
			messages.add(msg);
		}
		return uList;
	}

	public String getUnread(String uID) {
		UserInGroup user = getUser(uID);
		int msgNo = user.getLastMsgNo();
		String msg = "";
		for (int i = messages.size() - lastStoreMsgNo + msgNo; i < messages
				.size() - 1; i++) {
			msg += messages.get(i).toString() + "\n";
		}
		user.setLastMsgNo(lastStoreMsgNo);
		if (lastStoreMsgNo - msgNo == messages.size()) {
			int minMsgNo = lastStoreMsgNo;
			for (int i = 0; i < userInGroups.size(); i++) {

				if (userInGroups.get(i).getLastMsgNo() < minMsgNo)
					minMsgNo = userInGroups.get(i).getLastMsgNo();
			}
			if (lastStoreMsgNo - messages.size() < minMsgNo)
				for (int i = 0; i < messages.size()
						- (lastStoreMsgNo - minMsgNo); i++) {
					messages.remove(0);
				}
		}
		return msg;
	}

	public UserInGroup getUser(String uID) {
		return userInGroups.get(userInGroups.indexOf(new UserInGroup(uID)));
	}

	@Override
	public int compareTo(Group o) {
		// TODO Auto-generated method stub
		if (groupID.equals(o.getGID().toLowerCase()))
			return 0;
		else
			return -1;
	}
}
