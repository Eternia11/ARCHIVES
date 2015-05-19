package archives.archives;

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
	private String m_perormative = "";		// performative of the line
	private String m_caseID = "";			// caseID of the line
	private Date m_timestamp = null;		// timestamp of the line

	/**
	 * Default Constructor
	 * 
	 * @deprecated
	 */
	public Occurrence() {
	}

}
