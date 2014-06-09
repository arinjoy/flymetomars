package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.common.FMTMValidationException;
import flymetomars.presentation.webservices.auth.UserAction;
import flymetomars.presentation.webservices.util.FormHandler;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class CreateMissionResource extends FreemarkerResource {

    private Mission mission;
    private FormHandler formHandler;
    private EntitySaver entitySaver;
    private EntityFactory entityFactory;

    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("create_mission.html.ftl");
        dataModel.put("title", "Create mission");
        super.doInit();
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel.put("authenticatedUser", authenticatedUser);
        String init = getRequest().getResourceRef().getQueryAsForm().getFirstValue("init");
        if (init != null && init.equals("true")) {
            mission = null;
            formHandler.resetAllFields();
        }
        dataModel.put("missionNameValue", formHandler.getFirstFieldValue("missionName"));
        dataModel.put("timeValue", formHandler.getFirstFieldValue("time"));
        dataModel.put("locationValue", formHandler.getFirstFieldValue("location"));
        dataModel.put("descriptionValue", formHandler.getFirstFieldValue("description"));
        
        // to display the list of available locations
        List<Location> availableLocations=entityLoader.loadAllLocations();
        dataModel.put("availableLocations", availableLocations);
        
        return super.doAuthenticatedGet(variant);
    }

    @Override
    @Post("html")
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        final boolean validFormValues = formHandler.saveFieldValues(entity);
        if (!validFormValues) {
            exceptionHandler.displayErrorOnScreen("Invalid mission information.");
            final Map<String, List<String>> inputErrors = formHandler.getInputErrors();
            for (String key : inputErrors.keySet()) {
                errorMap.put(key + "Error", inputErrors.get(key).get(0));
            }
        }

        if (errorMap.isEmpty()) {
            try {
                errorMap.putAll(createAndSaveMission(formHandler.getFirstFieldValue("missionName"),
                        formHandler.getFirstFieldValue("time"),
                        formHandler.getFirstFieldValue("location"),
                        formHandler.getFirstFieldValue("description")));
            } catch (DependencyException ex) {
                Logger.getLogger(CreateMissionResource.class.getName()).log(Level.CONFIG, "Invalid dependencies while making ission", ex);
                errorMap.put("errorMessage",ex.getMessage());
            }
            if (errorMap.isEmpty()) {
                getResponse().redirectPermanent(getRequest().getRootRef().toString() + "/mission/" + mission.getId());
            }
        }
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            dataModel.putAll(errorMap);
        }
        return get(variant);
    }

    private Map<String, String> createAndSaveMission(String name, String time, String locationId, String description) throws DependencyException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        Long locId;
        try{
            locId = Long.parseLong(locationId);
        }catch(Exception e){
            errorMap.put("errorMessage", "The location id must be entered!");
            return errorMap;
        }
        
        Location location = entityLoader.loadLocationById(locId);

        if (location == null) {
            errorMap.put("errorMessage", "The location id does not exist. Check your input!");
        } else {
            try {
                mission = entityFactory.createMission(authenticatedUser, location);
                mission.setName(name);
                mission.setDescription(description);
            } catch (FMTMValidationException e) {
                errorMap.put("errorMessage", e.getMessage());
                return errorMap;
            }
            try {
                // change the format (and html description in create_mission.html.ftl) to suit your code
                DateFormat format = new SimpleDateFormat("dd/mm/yyyy, HH a");
                mission.setTime(format.parse(time));
                entitySaver.saveMission(mission);
            } catch (ParseException e) {
                errorMap.put("timeError", "Wrong date format: " + time + ": " + e.getMessage());
            } catch (DataAccessException e) {
                errorMap.put("saveError", "Error saving mission: " + e.getMessage());
            }
        }

        return errorMap;
    }

    @Override
    protected boolean authorizeGet() {
        return manager.decide(authenticatedUser, Mission.class, UserAction.CREATE);
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
