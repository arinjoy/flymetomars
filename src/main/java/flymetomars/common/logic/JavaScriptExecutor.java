package flymetomars.common.logic;

/**
 * Behavioral interface representing the ability of an implementor to execute
 * arbitrarily given pieces of JavaScript code within some stateless environment
 * 
 * @author Lawrence Colman
 */
public interface JavaScriptExecutor {
    
    /**
     * Principle JavaScript/ECMAScript execution method allowing for the running
     * of a given `code' String of JavaScript within stateless execution environment
     * 
     * @param code String of valid JavaScript source code to be executed
     * @return The given object (typically null or a string) resulting from `code'
     * @throws
     */
    Object execute(String code);
    
}
