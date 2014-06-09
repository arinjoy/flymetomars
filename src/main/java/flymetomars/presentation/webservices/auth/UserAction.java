package flymetomars.presentation.webservices.auth;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public enum UserAction {
    /**
     * The create action.
     */
    CREATE("Create"),
    /**
     * The read action.
     */
    READ("Read"),
    /**
     * The update action.
     */
    UPDATE("Update"),
    /**
     * The delete action.
     */
    DELETE("Delete");

    private final String name;

    UserAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User action: " + name;
    }
}
