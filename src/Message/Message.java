package Message;

import java.security.Timestamp;

public class Message {
	private String text;
	private String clientID;
	private String time;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public Message(String text, String clientID, String time) {
		this.text = text;
		this.clientID = clientID;
		this.time = time;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String msg = time.toString() + " " + clientID + " : " + text;
		return msg;
	}
}
