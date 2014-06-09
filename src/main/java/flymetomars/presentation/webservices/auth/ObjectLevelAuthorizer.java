package flymetomars.presentation.webservices.auth;

import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.IdentifiableModel;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class ObjectLevelAuthorizer {

    public synchronized boolean authroize(Person user, IdentifiableModel resource, UserAction action) {
        boolean decision = false;
        if (null == user) {
            decision = false;
        } else if (null == action) {
            decision = false;
        } else if (UserAction.READ.equals(action)) {
            decision = !(resource instanceof Person) || user.equals(resource);
        } else if (UserAction.UPDATE.equals(action) || UserAction.DELETE.equals(action)) {
            if (resource instanceof Mission) {
                decision = user.equals(((Mission) resource).getCaptain());
            } else if (resource instanceof Invitation) {
                decision = user.equals(((Invitation) resource).getCreator());
            } else if (resource instanceof Person) {
                decision = user.equals(resource);
            }
        } else if (UserAction.CREATE.equals(action)) {
            decision = false;
        }

        return decision;
    }
}
