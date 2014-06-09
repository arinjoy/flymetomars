package flymetomars.presentation.webservices.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class FormHandler {
    private static final Logger LOGGER = Logger.getLogger(FormHandler.class);

    DiskFileItemFactory fileItemFactory;
    private Map<String, List<String>> formValues;
    private Map<String, List<String>> inputErrors;


    public FormHandler() {
        fileItemFactory = new DiskFileItemFactory();
        formValues = new HashMap<String, List<String>>();
    }

    public String getFirstFieldValue(String fieldName) {
        String value = "";
        List<String> list = formValues.get(fieldName);
        if (null != list && list.size() > 0) {
            value = list.get(0);
        }
        return value;
    }

    /**
     * Save values from the form.
     * The currently selected file is not added to the map of files until it is request by calling addFile()
     * @param entity
     * @return
     */
    public boolean saveFieldValues(Representation entity) {
    	return saveMultipart(entity);
    }

    private boolean saveMultipart(Representation entity) {
        RestletFileUpload upload = new RestletFileUpload(fileItemFactory);
        // re-initalises formValues, but remembers all files which have already been added to the map
        boolean valid = true;
        formValues = new HashMap<String, List<String>>();
        inputErrors = new HashMap<String, List<String>>();
        try {
            for (FileItem item : upload.parseRepresentation(entity)) {
                String name = item.getFieldName();

                if (item.isFormField()) {
                    try {
                        addFieldValue(name, item.getString("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        if (! addFieldValue(name, item.getString()) ) {
                            addErrorMsg(item, name);
                            valid = false;
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            valid = false;
            LOGGER.error("Error parsing form. ", e);
        }
        return valid;
    }

    private void addErrorMsg(FileItem item, String name) {
        String encodingError = "encodingError";
        String msg = "Value " + item.getString() + " for attribute " + name + " cannot be encoded ";

        List<String> list = inputErrors.get(encodingError);
        if (null == list) {
            list = new ArrayList<String>();
        }
        list.add(msg);
        inputErrors.put(encodingError, list);
    }

    /**
     * Adds the field with the given value.
     * If the value is clean, then return true
     * @param name
     * @param value
     * @return
     */
    private boolean addFieldValue(String name, String value) {

        // handle form fields; add the entered value to the list and values
        List<String> list = formValues.get(name);
        if (null == list) {
            list = new ArrayList<String>();
        }

        list.add(value);
        formValues.put(name, list);

        return true;
    }

    public Map<String, List<String>> getInputErrors() {
        return inputErrors;
    }

    public void resetAllFields() {
        formValues = new HashMap<String, List<String>>();
        inputErrors = new HashMap<String, List<String>>();
    }
}
