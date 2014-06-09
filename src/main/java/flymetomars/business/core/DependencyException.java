package flymetomars.business.core;

/**
 * Implementory sub-type of the AbstractDataIntegrityException custom exception.
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class DependencyException extends AbstractDataIntegrityException {
    
    /**
     * Default constructor
     */
    public DependencyException() {
        super();
    }

    /**
     * Message constructor
     * 
     * @param s String message detailing the reasons/solutions for this issue
     */
    public DependencyException(String s) {
        super(s);
    }

    /**
     * Message constructor with inner cause Exception/Error component
     * 
     * @param s String message detailing the reasons/solutions for this issue
     * @param throwable The Exception/Error instance to mark as causing this one
     */
    public DependencyException(String s, Throwable throwable) {
        super(s, throwable);
    }

    /**
     * Inner cause only constructor
     * 
     * @param throwable The Exception/Error instance to mark as causing this one
     */
    public DependencyException(Throwable throwable) {
        super(throwable);
    }
    
}
