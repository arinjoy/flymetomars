package flymetomars.presentation.resources;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Expertise;
import flymetomars.business.model.Location;
import org.restlet.resource.Get;
import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

/**
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 */
public class ExpertiseResource extends FreemarkerResource {

    private static final int HOME_EXPERTISE_INDEX = 2;

    private Expertise expertise;
    
    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("expertise.html.ftl");
        String expId = getRequestedExpertiseName(getRequest());
        try {
            expertise = entityLoader.loadExpertiseById(Long.parseLong(expId));
            super.doInit();
        } catch (Exception e) {
            final String msg = "Error loading expertise: " + expId;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedExpertiseName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_EXPERTISE_INDEX);
    }

    @Override
    protected boolean authorizeGet() {
        return true;
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        if (null == expertise) {
            exceptionHandler.displayErrorOnScreen(
                "Cannot find expertise with id: " + getRequestedExpertiseName(this.getRequest()));
        }
        dataModel.put("expertise", expertise);
        return super.doAuthenticatedGet(variant);
    }
    
}
