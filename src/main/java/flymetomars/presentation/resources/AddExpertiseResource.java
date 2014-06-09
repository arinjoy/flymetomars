package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.model.Expertise;
import flymetomars.business.model.Location;
import flymetomars.common.FMTMValidationException;
import flymetomars.common.datatransfer.Expertise.ExpertiseLevel;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.presentation.webservices.auth.UserAction;
import flymetomars.presentation.webservices.util.FormHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * 
 * 
 * @author Arinjoy Biswas
 */

public class AddExpertiseResource extends FreemarkerResource {

    private Expertise expertise;
    private FormHandler formHandler;
    private EntitySaver entitySaver;
    private EntityUpdater entityUpdater;
    private EntityFactory entityFactory;
    
    private ExpertiseDAO expertiseDao;

    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("add_expertise.html.ftl");
        dataModel.put("title", "Add expertise");
        super.doInit();
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel.put("authenticatedUser", authenticatedUser);
        String init = getRequest().getResourceRef().getQueryAsForm().getFirstValue("init");
        if (init != null && init.equals("true")) {
            expertise = null;
            formHandler.resetAllFields();
        }
        dataModel.put("user", authenticatedUser);
        dataModel.put("nameValue", formHandler.getFirstFieldValue("name"));
        dataModel.put("descriptionValue", formHandler.getFirstFieldValue("description"));
        dataModel.put("level", formHandler.getFirstFieldValue("level"));
        return super.doAuthenticatedGet(variant);
    }

    @Override
    @Post("html")
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        final boolean validFormValues = formHandler.saveFieldValues(entity);
        if (!validFormValues) {
            exceptionHandler.displayErrorOnScreen("Invalid expertise information.");
            final Map<String, List<String>> inputErrors = formHandler.getInputErrors();
            for (String key : inputErrors.keySet()) {
                errorMap.put(key + "Error", inputErrors.get(key).get(0));
            }
        }

        if (errorMap.isEmpty()) {
            try {
                errorMap.putAll(createAndSaveExpertise(formHandler.getFirstFieldValue("name"),
                        formHandler.getFirstFieldValue("description"),
                        formHandler.getFirstFieldValue("level")));
            } catch (DependencyException ex) {
                Logger.getLogger(AddExpertiseResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (errorMap.isEmpty()) {
                getResponse().redirectPermanent(getRequest().getRootRef().toString() + "/expertise/" + expertise.getId());
            }
        }
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            dataModel.putAll(errorMap);
        }
        return get(variant);
    }

    private Map<String, String> createAndSaveExpertise(String name, String description, String level) 
                                                    throws DependencyException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
       
        try {
            expertise = entityFactory.createExpertise();
            expertise.setName(name);
            expertise.setDescription(description); 
            ExpertiseLevel expLevel = ExpertiseLevel.valueOf(level);
            expertise.setLevel(expLevel);      
        } catch (FMTMValidationException e) {
            errorMap.put("errorMessage", e.getMessage());
            return errorMap;
        }
        try {            
            flymetomars.common.datatransfer.Expertise expDTO = expertise.toDTO(); 
            expDTO.setHeldBy(authenticatedUser.toDTO());
            expertiseDao.save(expDTO); 
            expertise.setId(expDTO.getId());               
        } catch (Exception e) {
            errorMap.put("errorMessage", "Error saving expertise: " + e.getMessage());
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
    
    public void setEntityUpdater(EntityUpdater entityUpdater) {
        this.entityUpdater = entityUpdater;
    }
    
    public void setExpertiseDao(ExpertiseDAO expertiseDao) {
        this.expertiseDao = expertiseDao;
    }

}
