package flymetomars.presentation.webservices.auth;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static flymetomars.presentation.webservices.auth.UserAction.CREATE;
import static flymetomars.presentation.webservices.auth.UserAction.DELETE;
import static flymetomars.presentation.webservices.auth.UserAction.READ;
import static flymetomars.presentation.webservices.auth.UserAction.UPDATE;
import static flymetomars.presentation.webservices.auth.UserAction.values;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public final class AccessRights {
    /**
     * No access.
     */
	public static final AccessRights DENY_ALL =
        new AccessRights(new Boolean[]{false, false, false, false, false, false, false, false, false});

	/**
	 * Read only access.
	 */
    public static final AccessRights READ_ONLY =
        new AccessRights(new Boolean[]{false, true, false, false});

    /**
	 * Read and Delete only access.
	 */
    public static final AccessRights READ_AND_DELETE_ONLY =
        new AccessRights(new Boolean[]{false, true, false, true});

    /**
     * Create access.
     */
    public static final AccessRights CREATE_ONLY =
        new AccessRights(new Boolean[]{true, false, false, false});

    /**
     * CRUD access.
     */
    public static final AccessRights CRUD_ONLY =
        new AccessRights(new Boolean[]{true, true, true, true});

    private final Boolean[] accessRights;

    public AccessRights(Boolean[] accessRights) {
        if (null == accessRights) {
            throw new IllegalArgumentException("Access rights array cannot be empty.");
        }
        if (accessRights.length != values().length) {
            throw new IllegalArgumentException("Access rights array not of correct length: " + asList(accessRights));
        }
        this.accessRights = Arrays.copyOf(accessRights, accessRights.length);
    }

    public boolean canCreate() {
        return accessRights[CREATE.ordinal()];
    }

    public boolean canRead() {
        return accessRights[READ.ordinal()];
    }

    public boolean canUpdate() {
        return accessRights[UPDATE.ordinal()];
    }

    public boolean canDelete() {
        return accessRights[DELETE.ordinal()];
    }
    public boolean getAccessRight(UserAction action) {
        boolean result;
        switch (action) {
            case CREATE:
                result = canCreate();
                break;
            case READ:
                result = canRead();
                break;
            case UPDATE:
                result = canUpdate();
                break;
            case DELETE:
                result = canDelete();
                break;
            default:
                throw new IllegalArgumentException("Invalid user action: " + action.name());
        }
        return result;
    }
}
