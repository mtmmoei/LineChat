package Server;

import java.util.ArrayList;

public class UserInfo implements Comparable<UserInfo> {
	private String userID;
	private ArrayList<String> groupID=new ArrayList<String>();

	public UserInfo(String ID) {
		userID = ID.toLowerCase();
	}

	public ArrayList<String> getGroupList() {
		return groupID;
	}

	public String getUserID() {
		return userID;
	}
	public void addGroup(String gID){
		 groupID.add(gID);
	}
	public void deleteGroup(String gID){
		 groupID.remove(gID);
	}
	@Override
	public int compareTo(UserInfo o) {
		// TODO Auto-generated method stub
		if (userID.equals(o.getUserID().toLowerCase()))
			return 0;
		else {
			return 1;
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String list="";
		for(int i=0;i<groupID.size();i++){
			list+= groupID.get(i)+" ";
		}
		return list;
	}
}
