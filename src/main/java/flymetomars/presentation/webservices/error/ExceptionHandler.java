package flymetomars.presentation.webservices.error;

import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class ExceptionHandler {
    private static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class);
    private Map<String, Object> dataModel;

    public void setDataModel(Map<String, Object> dataModel) {
        this.dataModel = dataModel;
    }

    public void handleException(Exception e, boolean stackTrace, String label, String message) {
        displayErrorOnScreen(label, message);
        logError(e, stackTrace, message);
    }

    public void handleException(Exception e, boolean stackTrace, String message) {
        displayErrorOnScreen(message);
        logError(e, stackTrace, message);
    }

    private void logError(Exception e, boolean stackTrace, String message) {
        if (stackTrace) {
            LOGGER.error(message, e);
        } else {
            LOGGER.error(message + e.getMessage());
        }
    }

    public void displayErrorOnScreen(String location, String error) {
        dataModel.put(location, error);
    }
    public void displayErrorOnScreen(String error) {
        dataModel.put("errorMessage", error);
    }
}
