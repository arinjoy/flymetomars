package flymetomars.common.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Encapsulation class provider of the JavaScriptEnvironment interface via a
 * Mozilla Rhino based back-end JavaScript engine for code evaluation.
 * 
 * @author Lawrence Colman
 */
public class RhinoWrapper implements JavaScriptExecutor, JavaScriptEnvironment {
   
    private static Map<Thread,Context> threadLocal=
        new ConcurrentHashMap<Thread, Context>();
    ;
    private void registerThreadLocals() {
        threadLocal.put(Thread.currentThread(), Context.enter());
    }
    
    private Scriptable env;
    
    /**
     * Map to keep track of the (uniquely-named) JavaScript libraries loaded in
     */
    private Map<String,ScriptLibrary> libraries;
    
    private void ensureLocalInit() {
        if(!threadLocal.containsKey(Thread.currentThread())) {
            registerThreadLocals();
        }
    }
    
    private Context host() {
        ensureLocalInit();
        return threadLocal.get(Thread.currentThread());
    }
    
    /**
     * Public constructor to initialize fields with Rhino and Map instances
     */
    public RhinoWrapper() {
        registerThreadLocals();
        this.env=host().initStandardObjects();
        this.libraries=new HashMap<String,ScriptLibrary>();
    }
    
    @Override
    /**
     * Runs a particular bit of code (one line or many) within environment
     * 
     * @param code String of the JavaScript code to execute inside Rhino instance
     */
    public synchronized Object execute(String code) {
        Object result=host().evaluateString(env, code, "INPUT", 1, null);
        if(result==Context.getUndefinedValue()) { return null; }
        return result;
    }

    @Override
    /**
     * Loads a named JavaScript codebase component into the environment
     * 
     * @param lib ScriptLibrary custom object instance representing library to load
     * @throws UnsupportedOperationException thrown when a ScriptLibrary object
     *          with the same name has already been loaded into this wrapper.
     */
    public void loadLibrary(ScriptLibrary lib) {
        if(null!=this.libraries.get(lib.getName())) {
            StringBuilder msg=new StringBuilder("A ScriptLibrary named \"");
            msg.append(lib.getName());
            msg.append("\" has already been loaded by this environment");
            throw new UnsupportedOperationException(msg.toString());
        }
        host().evaluateString(env, lib.getCode(), lib.getName(), 1, null);
        this.libraries.put(lib.getName(), lib);
    }

}
