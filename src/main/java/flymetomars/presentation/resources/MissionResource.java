package flymetomars.presentation.resources;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Mission;
import java.util.HashMap;
import java.util.List;
import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class MissionResource extends FreemarkerResource {

    private static final int HOME_MISSION_INDEX = 2;

    private Mission mission;
    
    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("mission.html.ftl");
        String missionId = getRequestedMissionName(getRequest());
        try {
            mission = entityLoader.loadMissionById(Long.parseLong(missionId));
            super.doInit();
        } catch (DataAccessException e) {
            final String msg = "Error loading mission: " + missionId;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedMissionName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_MISSION_INDEX);
    }

    @Override
    protected boolean authorizeGet() {
        return true;
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        if (null == mission) {
            exceptionHandler.displayErrorOnScreen(
                "Cannot find mission with id: " + getRequestedMissionName(this.getRequest()));
        }
        dataModel.put("mission", mission);
        return super.doAuthenticatedGet(variant);
    }
    
}
