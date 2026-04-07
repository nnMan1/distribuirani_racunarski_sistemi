package tcp_comunication;

public class Message {
	int srcId, destId;
	
	String tag;
	String msgBuffer;
	
	public Message(int s, int t, String msgType, String buff) {
		this.srcId = s;
		this.destId = t;
		this.tag = msgType;
		this.msgBuffer = buff;
	}
	
	
}
