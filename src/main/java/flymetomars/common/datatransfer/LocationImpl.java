package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;

/**
 * This class represents Location domain objects for inter-layer transfer
 *
 * @author Apoorva Singh
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * 
 * JavaDoc copied from interface.
 */
public class LocationImpl extends SeriablizableEntityImpl<Location> implements EqualableStructure<Location>, Location {

    private String floor;
    private String streetNo;
    private String street;
    private String landmark;
    private String town;
    private String region;
    private String postcode;
    private String country;

    /**
     * default constructor
     */
    public LocationImpl() {}

    /**
     * Property accessor for `floor' component of Location
     * 
     * @return String name/number of floor within building currently represented
     */
    @Override
    public final String getFloor() {
        return floor;
    }

    /**
     * Property mutator for `floor' component of Location
     *
     * @param floor String name/number of floor within building to be represented
     */
    @Override
    public void setFloor(String floor) {
        //if(null==floor) { throw new NullArgumentException("A location cannot have a null floor"); }
        this.floor = floor;
    }

    /**
     * Property accessor for `streetNo' component of Location
     * 
     * @return String number of address within the street
     */
    @Override
    public final String getStreetNo() {
        return streetNo;
    }

    /**
     * Property mutator for `streetNo' component of Location
     * 
     * @param streetNo String number of address within the street
     */
    @Override
    public void setStreetNo(String streetNo) {
        //if(null==streetNo) { throw new NullArgumentException("A location cannot have a null street no"); }
        this.streetNo = streetNo;
    }

    /**
     * Property accessor for `street' component of Location
     * 
     * @return String name of street currently represented in Location
     */
    @Override
    public final String getStreet() {
        return street;
    }

    /**
     * Property mutator for `street' component of Location
     *
     * @param street String name of street to be represented in Location
     */
    @Override
    public void setStreet(String street) {
        if(null==street) { throw new NullArgumentException("A location cannot have a null street"); }
        this.street = street;
    }

    /**
     * Property accessor for `landmark' component of Location
     * 
     * @return String description/text describing nearby landmark
     */
    @Override
    public final String getLandmark() {
        return landmark;
    }

    /**
     * Property mutator for `landmark' component of Location
     * 
     * @param landmark String description/text describing nearby landmark
     */
    @Override
    public void setLandmark(String landmark) {
        //if(null==landmark) { throw new NullArgumentException("A location cannot have a null landmark"); }
        this.landmark = landmark;
    }

    /**
     * Property accessor for `town' component of Location
     * 
     * @return String name of town/city/suburb currently represented
     */
    @Override
    public final String getTown() {
        return town;
    }

    /**
     * Property mutator for `town' component of Location
     * 
     * @param town String name of town/city/suburb to be represented
     */
    @Override
    public void setTown(String town) {
         if(null==town) { throw new NullArgumentException("A location cannot have a null town"); }
        this.town = town;
    }

    /**
     * Property accessor for `region' component of Location
     * 
     * @return String name of region currently represented
     */
    @Override
    public final String getRegion() {
        return region;
    }

    /**
     * Property mutator for `region' component of Location
     * 
     * @param region String name of region to be represented
     */
    @Override
    public void setRegion(String region) {
        if(null==region) { throw new NullArgumentException("A location cannot have a null region"); }
        this.region = region;
    }

    /**
     * Property accessor for `postCode' component of Location
     * 
     * @return String postCode of region currently represented
     */
    @Override
    public final String getPostcode() {
        return postcode;
    }

    /**
     * Property mutator for `postCode' component of Location
     * 
     * @param pcode String postCode of region to be represented
     */
    @Override
    public void setPostcode(String pcode) {
        //if(null==pcode) { throw new NullArgumentException("A location cannot have a null post code"); }
        this.postcode = pcode;
    }

    /**
     * Property accessor for `country' component of Location
     * 
     * @return String name of country currently represented
     */
    @Override
    public final String getCountry() {
        return country;
    }

    /**
     * Property mutator for `country' component of Location
     * 
     * @param country String name of country to be represented
     */
    @Override
    public void setCountry(String country) {
        if (null == country) {
            throw new NullArgumentException("cannot set Location country field to null");
        }
        this.country = country;
    }

    /**
     * 
     * 
     * @param loc
     * @return 
     */
    @Override
    public boolean equalsDTO(Location loc) {
        if (nullEquals(this.getFloor(),loc.getFloor())) { return false; }
        if (nullEquals(this.getStreetNo(), loc.getStreetNo())) { return false; }
        if (nullEquals(this.getStreet(), loc.getStreet())) { return false; }
        if (nullEquals(this.getLandmark(), loc.getLandmark())) { return false; }
        if (nullEquals(this.getTown(), loc.getTown())) { return false; }
        if (nullEquals(this.getRegion(), loc.getRegion())) { return false; }
        if (nullEquals(this.getPostcode(), loc.getPostcode())) { return false; }
        if (nullEquals(this.getCountry(), loc.getCountry())) { return false; }
        return true;
    }
    private static boolean nullEquals(String str,String oth) {return str!=null?null==oth:str.equals(oth);}
    
    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = (1+2)*2, bits=(2+1)*2*2*2*2*2;
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
    
    /**
     * Standard Java Object.equals(Object) method signature override for sorting
     * 
     * @param obj Object to be checked for equality with this object
     * @return boolean value indicating if the representations contained within
     * this object and obj are effectively equivalent (thus equal for sorting).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.equalsDTO((Location)obj);
    }

}
