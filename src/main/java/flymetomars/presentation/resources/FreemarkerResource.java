package flymetomars.presentation.resources;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import flymetomars.presentation.webservices.error.ExceptionHandler;
import flymetomars.presentation.webservices.templating.FreemarkerTemplateLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.restlet.data.Status.CLIENT_ERROR_FORBIDDEN;
import static org.restlet.data.Status.SERVER_ERROR_INTERNAL;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public abstract class FreemarkerResource extends AccessControlledResource {

    private static final String MAIN_TEMPLATE_NAME = "base_page.html.ftl";
    protected String title = "Fly me to Mars";

    protected String baseUrl;
    // set to make the selected tab appear highlighted
    protected String contentTemplateName;

    protected Map<String, Object> dataModel;
    protected ExceptionHandler exceptionHandler;

    protected FreemarkerTemplateLoader templateLoader;

    public void setTemplateLoader(FreemarkerTemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public String getContentTemplateName() {
        return contentTemplateName;
    }

    public void setContentTemplateName(String contentTemplateName) {
        this.contentTemplateName = contentTemplateName;
    }

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
    }

    @Override
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        try {
            variant.setMediaType(MediaType.TEXT_HTML);
            if (null == dataModel.get("pageTitle")) {
                dataModel.put("pageTitle", title);
            }
            dataModel.put("baseUrl", baseUrl);
            dataModel.put("contentTemplate", getContentTemplateName());
            // pass the current user, so we can display their details
            dataModel.put("user", authenticatedUser);
            dataModel.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
            final Template template = loadTemplate(MAIN_TEMPLATE_NAME);
            return new TemplateRepresentation(template, dataModel, variant.getMediaType());
        } catch (RuntimeException e) {
            logger.error("Internal error: ", e);
            redirectToErrorPage(SERVER_ERROR_INTERNAL, "Internal error: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Internal error: ", e);
            redirectToErrorPage(SERVER_ERROR_INTERNAL, "Internal error: " + e.getMessage());
        }
        return new StringRepresentation("Error page here.");
    }

    @Override
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        // not all url's support post, if not redirect to an error page
        return redirectToErrorPage(CLIENT_ERROR_FORBIDDEN, "POST not supported for this url.");
    }

    @Override
    protected Representation doUnauthenticatedGet(Variant variant) throws ResourceException {
        // if the user does not have access rights and they are not logged in,
        // ask them to login if they are logged in and do not have access rights,
        // ask them to logout and login with higher access rights
        if (null == authenticatedUser) {
            getResponse().redirectTemporary(getRequest().getRootRef().toString() + "/login?" +
                    SOURCE_KEY + "=" + getRequest().getResourceRef().toString());
        } else {
            getResponse().redirectTemporary(getRequest().getRootRef().toString() + "/insufficient-access");
        }
        // we should not actually get here as we always redirect
        return new StringRepresentation("Access is unauthroized.");
    }

    @Override
    protected Representation head(Variant variant) throws ResourceException {
        Representation rep = new EmptyRepresentation();
        rep.setMediaType(variant.getMediaType());
        return rep;
    }

    protected Template loadTemplate(String templateName) throws ResourceException {
        try {
            return templateLoader.getTemplate(templateName);
        } catch (IOException e) {
            throw new ResourceException(SERVER_ERROR_INTERNAL, "Cannot load template: " + templateName, e);
        }
    }

    @Override
    protected void preGetAuthorization() {
        if (null == baseUrl) {
            baseUrl = getRequest().getRootRef().toString();
        }
    }

    /**
     * Common method for error reporting in subclasses that handle file attachments
     *
     * @param objectErrorMsgs
     */
    protected void displayFileErrorMessages(Map<String, List<String>> objectErrorMsgs) {
        List<String> list;
        list = objectErrorMsgs.get("file");
        if (null != list && !list.isEmpty()) {
            exceptionHandler.displayErrorOnScreen("fileErrorMessage", list.get(0));
        }
        list = objectErrorMsgs.get("file_description");
        if (null != list && !list.isEmpty()) {
            exceptionHandler.displayErrorOnScreen("fileDescriptionError", list.get(0));
        }
    }

    @Override
    protected void doCatch(Throwable throwable) {
        exceptionHandler.displayErrorOnScreen("An error occurred while processing your request: " +
                throwable.getMessage());
        logger.error("Unhandled exception.", throwable);
    }
}
