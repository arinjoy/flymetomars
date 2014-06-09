package flymetomars.presentation.webservices.auth;

import flymetomars.business.model.Mission;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Person;
import flymetomars.business.model.IdentifiableModel;

import static flymetomars.presentation.webservices.auth.UserAction.CREATE;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class ClassLevelAuthorizer {

    public synchronized boolean authorize(Person user, Class<? extends IdentifiableModel> clazz, UserAction action) {
        boolean decision = false;
        if (null == user) {
            decision = false;
        } else if (null != action && CREATE.equals(action)) {
            decision = true;
        }
        return decision;
    }

    public boolean authorize(Person user, Class<? extends IdentifiableModel> clazz, Mission mission, UserAction action) {
        boolean decision = false;
        if (null == user) {
            decision = false;
        } else if (null != action && CREATE.equals(action)) {
            if (Invitation.class.isAssignableFrom(clazz)) {
                decision = user.equals(mission.getCaptain());
            } else {
                decision = false;
            }
        }
        return decision;
    }
}
