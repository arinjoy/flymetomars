package flymetomars.presentation.webservices.auth;

import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.IdentifiableModel;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class AccessDecisionManager {
    private final ClassLevelAuthorizer classAuthorizer;
    private final ObjectLevelAuthorizer objectAuthorizer;

    public AccessDecisionManager(ClassLevelAuthorizer classAuthorizer,
                                         ObjectLevelAuthorizer objectAuthorizer) {
        this.classAuthorizer = classAuthorizer;
        this.objectAuthorizer = objectAuthorizer;
    }

    public boolean decide(Person user, IdentifiableModel resource, UserAction action) {
        return objectAuthorizer.authroize(user, resource, action);
    }

    public boolean decide(Person user, Class<? extends IdentifiableModel> clazz, UserAction action) {
        return classAuthorizer.authorize(user, clazz, action);
    }

    public boolean decide(Person user, Class<? extends IdentifiableModel> clazz, Mission mission, UserAction action) {
        return classAuthorizer.authorize(user, clazz, mission, action);
    }
}
