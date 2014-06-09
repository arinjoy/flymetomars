package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.model.Person;
import flymetomars.business.services.PersonAlreadyExistsException;
import flymetomars.business.services.RegisterService;
import flymetomars.common.FMTMValidationException;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import flymetomars.presentation.webservices.util.FormHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class RegisterResource extends FreemarkerResource {
    
    private FormHandler formHandler;
    private RegisterService registerService;

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        dataModel = new HashMap<String, Object>();
        dataModel.put("title", "User Self Registration");
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("register.html.ftl");
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {

        String init = getRequest().getResourceRef().getQueryAsForm().getFirstValue("init");
        if (init != null && init.equals("true")) {
            formHandler.resetAllFields();
        }
        dataModel.put("userNameValue", formHandler.getFirstFieldValue("userName"));
        dataModel.put("titleValue", formHandler.getFirstFieldValue("title"));
        dataModel.put("firstNameValue", formHandler.getFirstFieldValue("firstName"));
        dataModel.put("lastNameValue", formHandler.getFirstFieldValue("lastName"));
        dataModel.put("emailValue", formHandler.getFirstFieldValue("email"));
        return super.doAuthenticatedGet(variant);
    }

    @Override
    @Post("html")
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        final boolean validFormValues = formHandler.saveFieldValues(entity);
        if (!validFormValues) {
            exceptionHandler.displayErrorOnScreen("Invalid User information.");
            final Map<String, List<String>> inputErrors = formHandler.getInputErrors();
            for (String key : inputErrors.keySet()) {
                errorMap.put(key + "Error", inputErrors.get(key).get(0));
            }
        }
        // validate the user name and email address, if they are not valid return to the input page
        final String username = formHandler.getFirstFieldValue("userName");
        try {
            Person newguy = this.registerService.register(username,
                formHandler.getFirstFieldValue("email"),
                formHandler.getFirstFieldValue("password"),
                formHandler.getFirstFieldValue("confirmPassword"),
                formHandler.getFirstFieldValue("firstName"),
                formHandler.getFirstFieldValue("lastName")
            );
            errorMap.put("errorMessage", "User #"+newguy.getId().toString()+" Created!");
            getResponse().redirectPermanent(getRequest().getRootRef().toString() + "/login?reg_success");
        } catch(DependencyException de) {
            errorMap.put("errorMessage",de.getMessage());
        } catch(PersonAlreadyExistsException ae) {
            errorMap.put("errorMessage",ae.getMessage());
        } catch(FMTMValidationException e) {
            errorMap.put("errorMessage",e.getMessage());
        } catch(IllegalArgumentException iae) {
            errorMap.put("errorMessage",iae.getMessage());
        } 
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            for (String key : errorMap.keySet()) {
                dataModel.put(key, errorMap.get(key));
            }
        }
        return get(variant);
    }

    @Override
    protected boolean authorizeGet() {
        // only availible to anonymous users who are self registering (if enabled) or administrators
        return (null == authenticatedUser);
    }

    public void setFormHandler(FormHandler formHandler) {
        this.formHandler = formHandler;
    }

    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }    

}
