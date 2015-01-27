package pt.utl.ist.marc.iso2709;

/**
 * <p>
 * <code>ParseDirectoryException</code> is thrown when an error occurs while
 * parsing the directory of a MARC record.
 * </p>
 * 
 * @author Bas Peters - <a href="mailto:mail@bpeters.com">mail@bpeters.com</a>
 * @version 0.2
 */
public class ParseDirectoryException extends RuntimeException {

    /**
     * <p>
     * Creates an <code>Exception</code> indicating that an error occured while
     * parsing the directory.
     * </p>
     * 
     * @param reason
     *            the reason why the exception is thrown
     */
    public ParseDirectoryException(String reason) {
        super(new StringBuffer().append("Invalid directory: ").append(reason).append(".").toString());
    }

}

// End of ParseDirectoryException.java
