package archives.archives;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an occurrence of an activity in an archive (eq. a line of a log file)
 * 
 * @author Alan BENIER
 */
public class Occurrence {
	private Activity m_activity = null;		// activity of the line
	private Resource m_sender = null;		// sender of the line
	private Resource m_receiver = null;		// receiver of the line
	private String m_performative = "";		// performative of the line
	private String m_caseID = "";			// caseID of the line
	private Date m_timestamp = null;		// timestamp of the line

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Occurrence() {
	}

	/**
	 * Create an occurrence with all attributes set
	 * 
	 * @param activity activity of the line
	 * @param sender sender of the line
	 * @param receiver receiver of the line
	 * @param performative performative of the line
	 * @param caseID caseID of the line
	 * @param timestamp timestamp of the line
	 * @param dateFormat date format of the timestamp
	 * @throws OccurrenceException when cannot create an occurrence
	 */
	public Occurrence(Activity activity, Resource sender, Resource receiver, String performative, String caseID, String timestamp, String dateFormat) throws OccurrenceException {
		m_activity = activity;
		m_sender = sender;
		m_receiver = receiver;
		m_performative = performative;
		m_caseID = caseID;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			m_timestamp = sdf.parse(timestamp);
		} catch (ParseException e) {
			System.out.println("Error. Could not read the date at line "
					+ ". Please check out the log file.");
			e.printStackTrace();
			throw new OccurrenceException("Error during creation of the occurrence, please check out the date in the log file and the dateFormat in the source code.");
		}
	}

	/**
	 * Getter
	 * 
	 * @return the activity of the line
	 */
	public Activity get_activity() {
		return m_activity;
	}

	/**
	 * Getter
	 * 
	 * @return the sender of the line
	 */
	public Resource get_sender() {
		return m_sender;
	}

	/**
	 * Getter
	 * 
	 * @return the receiver of the line
	 */
	public Resource get_receiver() {
		return m_receiver;
	}

	/**
	 * Getter
	 * 
	 * @return the performative of the line
	 */
	public String get_performative() {
		return m_performative;
	}

	/**
	 * Getter
	 * 
	 * @return the caseId of the line
	 */
	public String get_caseID() {
		return m_caseID;
	}

	/**
	 * Getter
	 * 
	 * @return the timestamp of the line
	 */
	public Date get_timestamp() {
		return m_timestamp;
	}

	/**
	 * toString overload
	 */
	public String toString() {
		return "Occurrence<" + m_activity.get_name() + ", " + m_sender.get_name() + ", " + m_receiver.get_name()
				+ ", " + m_performative + ", " + m_caseID + ", " + m_timestamp.toString() + ">";
	}
}
