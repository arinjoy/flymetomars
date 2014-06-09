package flymetomars.business.model;

/**
 * Loosely-coupled Service Provider Interface (SPI) for Business Layer Models
 * which wish to type-safely indicate that they have the behavioral ability to
 * be transformed into layer-independent (and again loosely coupled) Data
 * Transfer Object (DTO) implementations (expresses loosely as an interface).
 * 
 * @author Lawrence Colman
 */
public interface TransferableModel<T> {
   
    /**
     * Transforms a supplicant model implementation object (as service provider
     * of this SPI) into a loosely-coupled transfer data-typed object (a DTO).
     * 
     * @return DTO of given type T (should be from common DTO interfaces)
     *          representing the data contained within this Model object
     */
    T toDTO();
    
}
