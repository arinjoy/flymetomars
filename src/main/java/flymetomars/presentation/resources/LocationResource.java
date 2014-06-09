package flymetomars.presentation.resources;

import flymetomars.business.core.EntityLoader;
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
 * @author Lawrence Colman
 */
public class LocationResource extends FreemarkerResource {

    private static final int HOME_LOCATION_INDEX = 2;

    private Location location;
    
    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("location.html.ftl");
        String locationId = getRequestedLocationName(getRequest());
        try {
            location = entityLoader.loadLocationById(Long.parseLong(locationId));
            super.doInit();
        } catch (DataAccessException e) {
            final String msg = "Error loading location: " + locationId;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedLocationName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_LOCATION_INDEX);
    }

    @Override
    protected boolean authorizeGet() {
        return true;
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        if (null == location) {
            exceptionHandler.displayErrorOnScreen(
                "Cannot find location with id: " + getRequestedLocationName(this.getRequest()));
        }
        dataModel.put("location", location);
        return super.doAuthenticatedGet(variant);
    }
    
}
