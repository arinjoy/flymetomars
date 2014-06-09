package flymetomars.business.core;

/**
 * Custom (checked) Exception super-type for the Data Integrity checks implemented
 * within the various core operator classes of the business layer of FlyMeToMars
 * 
 * @author Lawrence Colman
 */
public abstract class AbstractDataIntegrityException extends Exception {

    /*package-private*/ AbstractDataIntegrityException() {
        super();
    }

    /*package-private*/ AbstractDataIntegrityException(String message) {
        super(message);
    }

    /*package-private*/ AbstractDataIntegrityException(String msg, Throwable t) {
        super(msg, t);
    }

    /*package-private*/ AbstractDataIntegrityException(Throwable cause) {
        super(cause);
    }
    
}
