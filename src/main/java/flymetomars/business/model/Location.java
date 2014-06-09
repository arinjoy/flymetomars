package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
//import flymetomars.common.validation.StringValidator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents any reachable location (street address, building,
 * house, shop in a mall, an empty field, the middle of the desert, etc) that
 * may be or become the host-site of a FlyMeToMars Mission under the PETSS.
 *
 * As a member of the model package, this class implements business logic.
 *
 * @author Apoorva Singh
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class Location extends IdentifiableModel<Location> implements TransferableModel<flymetomars.common.datatransfer.Location> {

    private String floor;
    private String streetNo;
    private String street;
    private String landmark;
    private String town;
    private String region;
    private String postcode;
    private String country;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.Location> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.Location>(
            flymetomars.common.datatransfer.Location.class, dtoFactory
    );

    /**
     * Default constructor which does initialization
     */
    public Location() {
        this.floor = null;
        this.streetNo = null;
        this.street = "";
        this.landmark = null;
        this.town = "";
        this.region = "";
        this.postcode = null;
        this.country = "";
    }
    
     /**
     * Copy constructor that takes a DTO to a Location model object
     * 
     * @param dto Data Transfer Object representing a Location
     */
    public Location(flymetomars.common.datatransfer.Location dto) {
        this();
        if(null!=dto.getFloor()) { this.setFloor(dto.getFloor()); }
        if(null!=dto.getStreetNo()) { this.setStreetNo(dto.getStreetNo()); }
        this.setStreet(dto.getStreet());
        if(null!=dto.getLandmark()) { this.setLandmark(dto.getLandmark()); }
        this.setTown(dto.getTown());
        this.setRegion(dto.getRegion());
        if(null!=dto.getPostcode()) { this.setPostcode(dto.getPostcode()); }
        this.setCountry(dto.getCountry());
        this.setId(dto.getId());
    }
    
    /**
     * To convert a Location model object to a dto
     */
    @Override
    public final flymetomars.common.datatransfer.Location toDTO() {
        return this.makeDTO();
    }
    private flymetomars.common.datatransfer.Location makeDTO() {
        flymetomars.common.datatransfer.Location result;
        try {
            result = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        if(null!=this.getFloor()) { result.setFloor(this.getFloor()); }
        if(null!=this.getStreetNo()) { result.setStreetNo(this.getStreetNo()); }
        result.setStreet(this.getStreet());
        if(null!=this.getLandmark()) { result.setLandmark(this.getLandmark()); }
        result.setTown(this.getTown());
        result.setRegion(this.getRegion());
        if(null!=this.getPostcode()) { result.setPostcode(this.getPostcode()); }
        result.setCountry(this.getCountry());
        result.setId(this.getId());
        return result;
    }

    /**
     * Method to get the floor value
     * @return value of floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Method to set the floor value
     * @param floor 
     */
    public final void setFloor(String floor) {
        if (null != floor) {
            /* Old Validation (non-JS):
            StringValidator.validateStringLengthInRange(floor, 1, 12);
            StringValidator.validateStringContainsOnly(floor, StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS + " -.:");
            */
            flymetomars.common.datatransfer.Location dto;//=this.toDTO();
            try {
                dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
            dto.setFloor(floor);
            logic.applyRules(this.getClass().getSimpleName(),"floor",dto);
        }
        this.floor = floor;
    }

    /**
     * Method to get the street number
     * @return the value of the street number
     */
    public String getStreetNo() {
        return streetNo;
    }

    /**
     * Method to set the street number
     * @param streetNo 
     */
    public final void setStreetNo(String streetNo) {
        if (null != streetNo) {
            /* Old Validation (non-JS):
            StringValidator.validateStringLengthInRange(streetNo, 1, 10);
            StringValidator.validateStringContainsOnly(streetNo, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + StringValidator.NUMBERS + " -./");
            */
            flymetomars.common.datatransfer.Location dto;//=this.toDTO();
            try {
                dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
            dto.setStreetNo(streetNo);
            logic.applyRules(this.getClass().getSimpleName(),"streetNo",dto);
        }
        this.streetNo = streetNo;
    }

    /**
     * Method to get the street name
     * @return the value containing the street name
     */
    public String getStreet() {
        return street;
    }

    /**
     * Method to set the street name
     * @param street 
     */
    public final void setStreet(String street) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(street, 5, 40);
        StringValidator.validateStringContainsOnly(street, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + StringValidator.NUMBERS + " -.',");
        */
        flymetomars.common.datatransfer.Location dto;//=this.toDTO();
        try {
            dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setStreet(street);
        logic.applyRules(this.getClass().getSimpleName(),"street",dto);
        this.street = street;
    }

    /**
     * Method to get the landmark description of the location
     * @return
     */
    public String getLandmark() {
        return landmark;
    }

    /**
     * Method to set the landmark description of the location
     * @param landmark
     */
    public final void setLandmark(String landmark) {
        if (null != landmark) {
            /* Old Validation (non-JS):
            StringValidator.validateStringLengthInRange(landmark, 1, 30);
            StringValidator.validateStringContainsOnly(landmark, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + StringValidator.NUMBERS + " -,.");
            */
            flymetomars.common.datatransfer.Location dto;//=this.toDTO();
            try {
                dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
            dto.setLandmark(landmark);
            logic.applyRules(this.getClass().getSimpleName(),"landmark",dto);
        }
        this.landmark = landmark;
    }

    /**
     * Method to get the town of the location
     * @return
     */
    public String getTown() {
        return town;
    }

     /**
     * Method to set the town of the location
     * @param town
     */
    public final void setTown(String town) {
        if (null == town) {
            throw new NullArgumentException("cannot set Location town field to null");
        }
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(town, 2, 30);
        StringValidator.validateStringContainsOnly(town, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + " -'."); 
        */
        flymetomars.common.datatransfer.Location dto;//=this.toDTO();
        try {
            dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setTown(town);
        logic.applyRules(this.getClass().getSimpleName(),"town",dto);
        this.town = town;
    }

    /**
     * Method to get the region of the location
     * @return
     */
    public String getRegion() {
        return region;
    }

     /**
     * Method to set the region of the location
     * @param region
     */
    public final void setRegion(String region) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(region, 2, 30);
        StringValidator.validateStringContainsOnly(region, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + " -,'");
        */
        flymetomars.common.datatransfer.Location dto;//=this.toDTO();
        try {
            dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setRegion(region);
        logic.applyRules(this.getClass().getSimpleName(),"region",dto);
        this.region = region;
    }

    /**
     * Method to get the postcode of the location
     * @return
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Method to set the postcode of the location
     * @param pcode
     */
    public final void setPostcode(String pcode) {
        if (null!=pcode) {
            /* Old Validation (non-JS):
            StringValidator.validateStringLengthInRange(pcode, 1, 12);
            StringValidator.validateStringContainsOnly(pcode, StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS + "-");
            */
            flymetomars.common.datatransfer.Location dto;//=this.toDTO();
            try {
                dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                throw new IllegalStateException(ex);
            }
            dto.setPostcode(pcode);
            logic.applyRules(this.getClass().getSimpleName(),"postcode",dto);
        }
        this.postcode = pcode;
    }

    /**
     * Method to get the country of the location
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * Method to set the country of the location
     * @param country
     */
    public final void setCountry(String country) {
        if (null == country) {
            throw new NullArgumentException("cannot set Location country field to null");
        }
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(country, 2, 32);
        StringValidator.validateStringContainsOnly(country, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + " (-)");
        */
        flymetomars.common.datatransfer.Location dto;//=this.toDTO();
        try {
            dto = dtoFactory.createDTO(flymetomars.common.datatransfer.Location.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setCountry(country);
        logic.applyRules(this.getClass().getSimpleName(),"country",dto);
        this.country = country;
    }
    
    @Override
    public boolean equals(Object obj) {
        try {
            return this.equalsModel((Location) obj);
        } catch (ClassCastException cc) {
            return super.equals(obj);
        }
    }

    @Override
    public boolean equalsModel(Location loc) {
        if (!this.floor.equals(loc.getFloor())) { return false; }
        if (!this.streetNo.equals(loc.getStreetNo())) { return false; }
        if (!this.street.equals(loc.getStreet())) { return false; }
        if (!this.landmark.equals(loc.getLandmark())) { return false; }
        if (!this.town.equals(loc.getTown())) { return false; }
        if (!this.region.equals(loc.getRegion())) { return false; }
        if (!this.postcode.equals(loc.getPostcode())) { return false; }
        if (!this.country.equals(loc.getCountry())) { return false; }
        return true;
    }
    
     @Override
    public int hashCode() {
        int hash = (2*2*2)-1,bits=2+((2*(2+2+1))*hash)+2;
        hash = bits * hash + (this.floor != null ? this.floor.hashCode() : 0);
        hash = bits * hash + (this.streetNo != null ? this.streetNo.hashCode() : 0);
        hash = bits * hash + (this.street != null ? this.street.hashCode() : 0);
        hash = bits * hash + (this.landmark != null ? this.landmark.hashCode() : 0);
        hash = bits * hash + (this.town != null ? this.town.hashCode() : 0);
        hash = bits * hash + (this.region != null ? this.region.hashCode() : 0);
        hash = bits * hash + (this.postcode != null ? this.postcode.hashCode() : 0);
        hash = bits * hash + (this.country != null ? this.country.hashCode() : 0);
        return hash;
    }

}
