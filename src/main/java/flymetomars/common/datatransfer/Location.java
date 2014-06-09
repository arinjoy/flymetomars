package flymetomars.common.datatransfer;

/**
 * This class represents Location domain objects for inter-layer transfer
 *
 * @author Apoorva Singh
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface Location extends SeriablizableEntity<Location> {

    /**
     * Property accessor for `floor' component of Location
     * 
     * @return String name/number of floor within building currently represented
     */
    String getFloor();

    /**
     * Property mutator for `floor' component of Location
     *
     * @param floor String name/number of floor within building to be represented
     */
    void setFloor(String floor);

    /**
     * Property accessor for `streetNo' component of Location
     * 
     * @return String number of address within the street
     */
    String getStreetNo();

    /**
     * Property mutator for `streetNo' component of Location
     * 
     * @param streetNo String number of address within the street
     */
    void setStreetNo(String streetNo);

    /**
     * Property accessor for `street' component of Location
     * 
     * @return String name of street currently represented in Location
     */
    String getStreet();

    /**
     * Property mutator for `street' component of Location
     *
     * @param street String name of street to be represented in Location
     */
    void setStreet(String street);

    /**
     * Property accessor for `landmark' component of Location
     * 
     * @return String description/text describing nearby landmark
     */
    String getLandmark();

    /**
     * Property mutator for `landmark' component of Location
     * 
     * @param landmark String description/text describing nearby landmark
     */
    void setLandmark(String landmark);

    /**
     * Property accessor for `town' component of Location
     * 
     * @return String name of town/city/suburb currently represented
     */
    String getTown();

    /**
     * Property mutator for `town' component of Location
     * 
     * @param town String name of town/city/suburb to be represented
     */
    void setTown(String town);

    /**
     * Property accessor for `region' component of Location
     * 
     * @return String name of region currently represented
     */
    String getRegion();

    /**
     * Property mutator for `region' component of Location
     * 
     * @param region String name of region to be represented
     */
    void setRegion(String region);

    /**
     * Property accessor for `postCode' component of Location
     * 
     * @return String postCode of region currently represented
     */
    String getPostcode();

    /**
     * Property mutator for `postCode' component of Location
     * 
     * @param pcode String postCode of region to be represented
     */
    void setPostcode(String pcode);

    /**
     * Property accessor for `country' component of Location
     * 
     * @return String name of country currently represented
     */
    String getCountry();

    /**
     * Property mutator for `country' component of Location
     * 
     * @param country String name of country to be represented
     */
    void setCountry(String country);

}
