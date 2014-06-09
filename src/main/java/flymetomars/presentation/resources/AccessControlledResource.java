package flymetomars.presentation.resources;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Person;
import org.apache.log4j.Logger;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.springframework.dao.DataAccessException;
import flymetomars.presentation.webservices.auth.AccessDecisionManager;

import java.security.Principal;
import java.util.List;
import org.apache.log4j.Priority;

import static org.restlet.data.Status.CLIENT_ERROR_FORBIDDEN;
import static org.restlet.data.Status.CLIENT_ERROR_UNAUTHORIZED;
import static org.restlet.data.Status.SERVER_ERROR_INTERNAL;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public abstract class AccessControlledResource extends ServerResource {
    static final String SOURCE_KEY = "source";

    protected final Logger logger = Logger.getLogger(getClass());

    protected EntityLoader entityLoader;
    protected AccessDecisionManager manager;
    protected Person authenticatedUser;

    public void setManager(AccessDecisionManager manager) {
        this.manager = manager;
    }

    public void setEntityLoader(EntityLoader entityLoader) {
        this.entityLoader = entityLoader;
    }

    @Override
    public void doInit() throws ResourceException {
        final List<Principal> principals = getRequest().getClientInfo().getPrincipals();
        if (null != principals && !principals.isEmpty()) {
            String authenticatedUserName = principals.get(0).getName();
            //String authenticatedUserName = principals.iterator().next().getName();
            try {
                if (null == authenticatedUser || !authenticatedUser.getUserName().equals(authenticatedUserName)) {
                    authenticatedUser = entityLoader.loadPersonByUserName(authenticatedUserName);
                }
            } catch (DataAccessException e) {
                Logger.getLogger(getClass()).log(Priority.WARN, "Error loading authenticated user from DB",e);
                authenticatedUser = null;
            }
        } else {
            authenticatedUser = null;
        }
        super.doInit();
    }

    @Get
    @Override
    public final Representation get(Variant variant) throws ResourceException {
        try {
            preGetAuthorization();
            if (!authorizeGet()) {
                return doUnauthenticatedGet(variant);
            } else {
                return doAuthenticatedGet(variant);
            }
        } catch (Exception e) {
            logger.error("Internal error: ", e);
            return redirectToErrorPage(SERVER_ERROR_INTERNAL, "Internal error: " + e.getMessage() + ".");
        }
    }

    @Post
    @Override
    public final Representation post(Representation entity, Variant variant) throws ResourceException {
        prePostAuthorization(entity);
        if (!authorizeGet()) {
            return doUnauthenticatedGet(variant);
        } else {
            return doAuthenticatedPost(entity, variant);
        }
    }

    protected abstract Representation doAuthenticatedGet(Variant variant) throws ResourceException;

    protected Representation doUnauthenticatedGet(Variant variant) throws ResourceException {
        return redirectErrorStatus(CLIENT_ERROR_UNAUTHORIZED);
    }

    protected abstract boolean authorizeGet();

    protected abstract void preGetAuthorization();

    protected void prePostAuthorization(Representation entity) {
    }

    protected Representation doAuthenticatedPost(Representation entity, Variant variant)
        throws ResourceException {
        return redirectErrorStatus(CLIENT_ERROR_FORBIDDEN);
    }

    protected Representation redirectToErrorPage(Status errorCode, String message) {
        getResponse().redirectTemporary(getRequest().getRootRef().toString() +
            "/error?error_code=" + errorCode.toString() + "&message=" + message);
        return new StringRepresentation(errorCode.toString() + " \n" + message);
    }

    protected Representation redirectErrorStatus(Status status) {
        getResponse().setStatus(status);
        return getResponseEntity();
    }
}