package archives.log;

/**
 * Represents a line of a log file
 * 
 * @author Alan BENIER
 */
public class Trace {
	private String m_caseID;			// caseID of the case
	private String m_timestamp;			// timestamp of the case
	private String m_performative;		// performative of the case
	private String m_sender;			// sender of the case
	private String m_receiver;			// receiver of the case
	private String m_activity;			// activity of the case

	/**
	 * Create a case of a log
	 * 
	 * @param caseID caseID of the case
	 * @param timestamp timestamp of the case
	 * @param performative performative of the case
	 * @param sender sender of the case
	 * @param receiver receiver of the case
	 * @param activity activity of the case
	 */
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

	/**
	 * Getter
	 * 
	 * @return the caseID of the case
	 */
	public String getCaseID() {
		return m_caseID;
	}

	/**
	 * Getter
	 * 
	 * @return the timestamp of the case
	 */
	public String getTimestamp() {
		return m_timestamp;
	}

	/**
	 * Getter
	 * 
	 * @return the performative of the case
	 */
	public String getPerformative() {
		return m_performative;
	}

	/**
	 * Getter
	 * 
	 * @return the sender of the case
	 */
	public String getSender() {
		return m_sender;
	}

	/**
	 * Getter
	 * 
	 * @return the receiver of the case
	 */
	public String getReceiver() {
		return m_receiver;
	}

	/**
	 * Getter
	 * 
	 * @return the activity of the case
	 */
	public String getActivity() {
		return m_activity;
	}

	public String toString() {
		return "CaseID : " + m_caseID + ", TimeStamp : " + m_timestamp
				+ ", Performative : " + m_performative + ", Sender : " + m_sender
				+ ", Receiver : " + m_receiver + ", Activity : " + m_activity;
	}
}
