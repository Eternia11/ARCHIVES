/* Author : Alan BENIER */

package archives.log;

// represent a line of a log
public class Trace {
	private String m_caseID;
	private String m_timestamp;
	private String m_performative;
	private String m_sender;
	private String m_receiver;
	private String m_activity;

	public Trace(String caseID, String timestamp, String performative,
			String sender, String receiver, String activity) {
		super();
		this.m_caseID = caseID;
		this.m_timestamp = timestamp;
		this.m_performative = performative;
		this.m_sender = sender;
		this.m_receiver = receiver;
		this.m_activity = activity;
	}

	public String getCaseID() {
		return m_caseID;
	}

	public String getTimestamp() {
		return m_timestamp;
	}

	public String getPerformative() {
		return m_performative;
	}

	public String getSender() {
		return m_sender;
	}

	public String getReceiver() {
		return m_receiver;
	}

	public String getActivity() {
		return m_activity;
	}

	public String toString() {
		return "CaseID : " + m_caseID + ", TimeStamp : " + m_timestamp
				+ ", Performative : " + m_performative + ", Sender : " + m_sender
				+ ", Receiver : " + m_receiver + ", Activity : " + m_activity;
	}
}
