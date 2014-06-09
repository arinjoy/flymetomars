package flymetomars.presentation.resources;

import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import java.util.HashMap;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class IndexResource extends FreemarkerResource {

    public IndexResource() {
        setContentTemplateName("index.html.ftl");
    }

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel = new HashMap<String, Object>();
        dataModel.put("pageTitle", "Index - " + title);
        return super.doAuthenticatedGet(variant);
    }

    @Override
    protected boolean authorizeGet() {
        // eveyrone has access to the index page
        return null!=authenticatedUser;
    }
}