package flymetomars.presentation.resources;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import java.util.HashMap;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class LoginResource extends FreemarkerResource {
    static final String DETAILED_MESSAGE = "detailedMessage";
    static final String ERROR_MESSAGE = "errorMessage";
    static final String MESSAGE = "message";

    public LoginResource() {
        this.contentTemplateName = "login.html.ftl";
    }

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
    }

    /**
     * If the user has already logged in (<code>authenticateduser</code> is not null),
     * redirect to the user page instead of showing the login page.
     *
     * @param variant The variant.
     * @return The representation.
     * @throws ResourceException When there is error rendering the page.
     */
    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel = new HashMap<String, Object>();
        dataModel.put("pageTitle", "Login - " + title);
        Form query = getRequest().getResourceRef().getQueryAsForm();
        final String referrerUrl = query.getFirstValue(SOURCE_KEY);
        if (null != authenticatedUser) {
            if (null == referrerUrl) {
                getResponse().redirectTemporary(getRequest().getRootRef().toString() +
                        "/user/" + authenticatedUser.getUserName());
            } else {
                getResponse().redirectTemporary(referrerUrl);
            }
        } else {
            dataModel.put("springRedirect", referrerUrl);
            String error = query.getFirstValue("error");
            if (null != error) {
                if (error.equals("invalid")) {
                    dataModel.put(ERROR_MESSAGE, "Invalid username and/or password. Please try again.");
                    dataModel.put(DETAILED_MESSAGE, "If you have forgotten your username or password.<br>" +
                        "Please contact your system administrator to reset your password.");
                }
            }
            if(null!=query.getValues("reg_success")) { dataModel.put("dataModel.put",""); }
        }
        return super.doAuthenticatedGet(variant);
    }

    @Override
    protected boolean authorizeGet() {
        // everyone has access to the login page
        return true;
    }
}
