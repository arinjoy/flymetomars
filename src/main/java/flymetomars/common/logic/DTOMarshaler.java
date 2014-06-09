package flymetomars.common.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import flymetomars.common.datatransfer.Marshalable;
import flymetomars.common.factories.DTOFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface for the generic conversion of Data Transfer Objects (DTOs) into
 * JavaScriot Object Notation (JSON) serialised format for inter-language marshaling.
 * 
 * @param <T> Type of objects to be marshaled and unmarshaled within this instance
 * 
 * @author Apoorva Singh
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class DTOMarshaler<T extends Marshalable> {
    
    private Gson gson;
    private DTOFactory dtoFactory;
    private Class<T> clazz;  //dummy field to hold type reference as actual type
    
    /**
     * Public constructor taking dependency injected required componentry for
     * the conversion and construction of DTOs to and from JSON Strings.
     * 
     * @param classinstance the Class<T> of the required DTO type for marshaling
     * @param gsonConverter Gson object for performing JSON String operations
     * @param dtoFactory Data Transfer Object Factory for DTO instantiation
     */
    public DTOMarshaler(Class<T> classinstance, Gson gsonConverter, DTOFactory dtoFactory) {
        this.clazz=classinstance;
        this.gson = gsonConverter;
        this.dtoFactory = dtoFactory;
    }
    
    /*package-private*/ DTOMarshaler(Class<T> classinstance, DTOFactory dtoFactory) {
        this(classinstance,new GsonBuilder().setDateFormat("MMMM dd, yyyy HH:mm:ss").create(),dtoFactory);
    }
    
    /**
     * Encodes a DTO into a JSON String representing a seralised state of `obj'
     * 
     * @param obj The object of type <T> to be encoded and marshaled into JSON
     * @return JSON-encoded String representation of the given `obj' object
     */
    public String marshal(T obj) {
        return this.gson.toJson(obj);
    }
    
    /**
     * Decodes a JSON-encoded string into a strongly-typed DTO object instance
     * 
     * @param json JSON-encoded object of type <T> to be decoded and returned
     * @return An object of type <T> that is the JSON-decoded representation of `json'
     */
    public T unmarshal(String json) {
        T result;
        try {
            result=(T)this.dtoFactory.createDTO(this.clazz);
        } catch(ClassNotFoundException ex) {
            Logger.getLogger(DTOMarshaler.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        } catch(ClassCastException cce) {
            throw new IllegalStateException(cce);
        }
        result=(T)this.gson.fromJson(json, result.getClass());
        return result;
    }
    
}
