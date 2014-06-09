package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Location;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LocationEntity is responsible for representing strongly-typed and
 * strongly-coupled `Location' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class LocationEntity {
    
    private String floor;
    private String streetNo;
    private String street;
    private String landmark;
    private String town;
    private String region;
    private String postcode;
    private String country;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    /**
     * Default constructor
     */
    public LocationEntity() {}
    
    /**
     * Location DTO to Entity class conversion utility method
     * 
     * @param loc Location data transfer object (DTO) to convert to Entity
     * @return A Location Entity instance representing the DTO passed to method
     */
    public static LocationEntity fromDTO(Location loc) {
        if(null==loc) { return (LocationEntity)null; }
        LocationEntity result = new LocationEntity();
        result.setFloor(loc.getFloor());
        result.setStreetNo(loc.getStreetNo());
        result.setStreet(loc.getStreet());
        result.setLandmark(loc.getLandmark());
        result.setTown(loc.getTown());
        result.setRegion(loc.getRegion());
        result.setPostcode(loc.getPostcode());
        result.setCountry(loc.getCountry());
        result.setId(loc.getId());
        return result;
    }
    
    /**
     * Location Entity transfer encoding conversion utility method
     * 
     * @return A Location data transfer object (DTO) representing this Entity
     */
    public Location toDTO() {
        Location result;
        try {
            result=dtoFactory.createDTO(Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setFloor(this.getFloor());
        result.setStreetNo(this.getStreetNo());
        result.setStreet(this.getStreet());
        result.setLandmark(this.getLandmark());
        result.setTown(this.getTown());
        result.setRegion(this.getRegion());
        result.setPostcode(this.getPostcode());
        result.setCountry(this.getCountry());
        result.setId(this.getId());
        return result;
    }
    
    /**
     * Convenience method to populate this LocationEntity with values from another
     * 
     * @param loc The alternate LocationEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public LocationEntity copyValues(LocationEntity loc) {
        this.setFloor(loc.getFloor());
        this.setStreetNo(loc.getStreetNo());
        this.setStreet(loc.getStreet());
        this.setLandmark(loc.getLandmark());
        this.setTown(loc.getTown());
        this.setRegion(loc.getRegion());
        this.setPostcode(loc.getPostcode());
        this.setCountry(loc.getCountry());
        this.setId(loc.getId());
        return this;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String pcode) {
        this.postcode = pcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Property accessor for Entity objects encapsulating domain objects with ids
     * 
     * @return the unique Long Integer identifier code for this Entity instance
     */
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder(this.getClass().getCanonicalName());
        result.append('#');
        result.append(this.getId());
        return result.toString();
    }
    
}
