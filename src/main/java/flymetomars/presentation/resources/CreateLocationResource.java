package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Location;
import flymetomars.common.FMTMValidationException;
import flymetomars.presentation.webservices.auth.UserAction;
import flymetomars.presentation.webservices.util.FormHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;

/**
 * 
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class CreateLocationResource extends FreemarkerResource {

    private Location location;
    private FormHandler formHandler;
    private EntitySaver entitySaver;
    private EntityFactory entityFactory;

    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("create_location.html.ftl");
        dataModel.put("title", "Create location");
        super.doInit();
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel.put("authenticatedUser", authenticatedUser);
        String init = getRequest().getResourceRef().getQueryAsForm().getFirstValue("init");
        if (init != null && init.equals("true")) {
            location = null;
            formHandler.resetAllFields();
        }
        String floor=formHandler.getFirstFieldValue("floor");
        if(floor.isEmpty()) { floor=null; }
        String streetNo=formHandler.getFirstFieldValue("streetNo");
        if(streetNo.isEmpty()) { streetNo=null; }
        String landmark=formHandler.getFirstFieldValue("landmark");
        if(landmark.isEmpty()) { landmark=null; }
        String postcode=formHandler.getFirstFieldValue("postcode");
        if(postcode.isEmpty()) { postcode=null; }
        dataModel.put("floorValue", floor);
        dataModel.put("streetNoValue", streetNo);
        dataModel.put("streetValue", formHandler.getFirstFieldValue("street"));
        dataModel.put("landmarkValue", landmark);
        dataModel.put("townValue", formHandler.getFirstFieldValue("town"));
        dataModel.put("regionValue", formHandler.getFirstFieldValue("region"));
        dataModel.put("postcodeValue", postcode);
        dataModel.put("countryValue", formHandler.getFirstFieldValue("country"));
        return super.doAuthenticatedGet(variant);
    }

    @Override
    @Post("html")
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        final boolean validFormValues = formHandler.saveFieldValues(entity);
        if (!validFormValues) {
            exceptionHandler.displayErrorOnScreen("Invalid location information.");
            final Map<String, List<String>> inputErrors = formHandler.getInputErrors();
            for (String key : inputErrors.keySet()) {
                errorMap.put(key + "Error", inputErrors.get(key).get(0));
            }
        }

        if (errorMap.isEmpty()) {
            try {
                errorMap.putAll(createAndSaveLocation(formHandler.getFirstFieldValue("floor"),
                    formHandler.getFirstFieldValue("streetNo"),
                    formHandler.getFirstFieldValue("street"),
                    formHandler.getFirstFieldValue("landmark"),
                    formHandler.getFirstFieldValue("town"),
                    formHandler.getFirstFieldValue("region"),
                    formHandler.getFirstFieldValue("postcode"),
                    formHandler.getFirstFieldValue("country")));
            } catch (DependencyException ex) {
                Logger.getLogger(CreateLocationResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (errorMap.isEmpty()) {
                getResponse().redirectPermanent(getRequest().getRootRef().toString() + "/location/" + location.getId());
            }
        }
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            dataModel.putAll(errorMap);
        }
        return get(variant);
    }

    private Map<String, String> createAndSaveLocation(String floor, String streetNo, String street, String landmark,
                                                        String town, String region, String postcode, String country) throws DependencyException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
       
        try {
            location = entityFactory.createLocation();
            if(!floor.isEmpty()) { location.setFloor(floor); }
            if(!streetNo.isEmpty()) { location.setStreetNo(streetNo); }
            location.setStreet(street);
            if(!landmark.isEmpty()) { location.setLandmark(landmark); }
            location.setTown(town);
            location.setRegion(region);
            if(!postcode.isEmpty()) { location.setPostcode(postcode); }
            location.setCountry(country);
        } catch (FMTMValidationException e) {
            errorMap.put("errorMessage", e.getMessage());
            return errorMap;
        }
        try {  
            entitySaver.saveLocation(location);
        } catch (DataAccessException e) {
            errorMap.put("saveError", "Error saving location: " + e.getMessage());
        }
        return errorMap;
    }

    @Override
    protected boolean authorizeGet() {
        return manager.decide(authenticatedUser, Location.class, UserAction.CREATE);
    }

    public void setFormHandler(FormHandler formHandler) {
        this.formHandler = formHandler;
    }

    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void setEntitySaver(EntitySaver entitySaver) {
        this.entitySaver = entitySaver;
    }

}
