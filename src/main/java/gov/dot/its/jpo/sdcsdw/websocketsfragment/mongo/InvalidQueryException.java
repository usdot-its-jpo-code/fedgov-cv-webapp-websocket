/** LEGACY CODE
 * 
 * This was salvaged in part or in whole from the Legacy System. It will be heavily refactored or removed.
 */
package gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo;

public class InvalidQueryException extends Exception {

	private static final long serialVersionUID = 6876150938688013459L;

	public InvalidQueryException(String message) {
		super(message);
    }
	
	public InvalidQueryException(Throwable cause) {
		super(cause);
    }

    public InvalidQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
