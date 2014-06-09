package flymetomars.presentation.resources;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

import java.util.HashMap;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class StaticResource extends FreemarkerResource {

    private static final int REQUEST_INDEX = 1;
    private static final String ERROR_PAGE = "error";
    private static final String INSUFFICIENT_ACCESS = "insufficient-access";

    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        Reference ref = getRequest().getResourceRef();
        String request = ref.getSegments().get(REQUEST_INDEX);
        if (ERROR_PAGE.equals(request)) {
            setContentTemplateName("error_page.html.ftl");
            Form query = ref.getQueryAsForm();
            dataModel.put("error_code", query.getFirstValue("error_code"));
            dataModel.put("message", query.getFirstValue("message"));
            title = "PODD - Error";
        } else if (INSUFFICIENT_ACCESS.equals(request)) {
            setContentTemplateName("insufficient_access.html.ftl");
            title = "PODD - Insufficient Access";
        }
        super.doInit();
    }

    @Override
    protected boolean authorizeGet() {
        // everyone has access to static pages
        return true;
    }

}
