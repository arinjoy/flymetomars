package flymetomars.common.logic;

import flymetomars.common.NullArgumentException;

/**
 * Representative encapsulating class for JavaScript modules for use within the
 * JavaScriptEnvironment for providing native language components and features.
 * 
 * @author Lawrence Colman
 */
public class ScriptLibrary {
    
    private String name;
    private String code;
    
    
    /**
     * Public Library Constructor - takes a given library `name' and supplied
     * `code' codebase for implementation of some JavaScript library featureset.
     * 
     * @param name
     * @param code String of JavaScript code to initialize environment for library
     * @throws NullArgumentException thrown when either of `name' or `code' are null
     * @throws IllegalArgumentException thrown when `name' is an empty (zero-length) String
     */
    public ScriptLibrary(String name, String code) {
        if(null==name) {
            throw new NullArgumentException("ScriptLibrary cannot be created with a null name");
        }
        if(name.isEmpty()) {
            throw new IllegalArgumentException("ScriptLibrary cannot be created with an empty name");
        }
        this.name=name;
        if(null==code) {
            throw new NullArgumentException("ScriptLibrary cannot be created with null code specified");
        }
        this.code=code;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
