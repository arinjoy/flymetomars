package flymetomars.common.logic;


/**
 * Behavioral composed interface representing ability to execute some given
 * piece of JavaScript code within a particularly configured environment.
 * 
 * @author Lawrence Colman
 */
public interface JavaScriptEnvironment extends JavaScriptExecutor {
    
    /**
     * Loads a specified JavaScriot library into this JavaScript Environment for
     * later execution and/or structural use by `execute'd JavaScript fragments.
     * 
     * @param lib ScriptLibrary representing JavaScript for loading in environment
     */
    void loadLibrary(ScriptLibrary lib);
    
    /**
     * Contractual override of JavaScriptExecutor "execute" method.
     * This method now represents the execution of a given `code' String of
     * JavaScript within a SATEFUL JavaScriptEnvironment rather than stateless.
     * 
     * @param code String of valid JavaScript source code to be executed
     * @return The given object (typically null or a string) resulting from `code'
     * @throws
     */
    @Override
    Object execute(String code);
    
}
