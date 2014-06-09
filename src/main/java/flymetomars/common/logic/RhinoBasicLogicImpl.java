package flymetomars.common.logic;

import flymetomars.common.datatransfer.Marshalable;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.validation.DateValidator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;


/**
 * Genericly-typed application interface for the execution of basic business
 * logic (BBL) against a domain model via a loosely-coupled data transfer object
 * 
 * @author Lawrence Colman
 * @param <T> Data Transfer Object (DTO) datatype of model to be checked
 * 
 * JavaDoc copied from interface
 */
public class RhinoBasicLogicImpl<T extends Marshalable> implements BasicLogic<T> {
    
    private DTOMarshaler<T> marshaler;
    private JavaScriptEnvironment runtime;
    
    /**
     * Runtime cache for RhinoWrapper instances - improves performance by 20-50%
     */
    private static Map<Class<? extends Marshalable>,JavaScriptEnvironment> envs=
        new HashMap<Class<? extends Marshalable>,JavaScriptEnvironment>();
    
    /**
     * Cache retrieval for genericised Marshalables and their relevant RhinoWrappers
     * 
     * @param clazz the DTO type relating to domain model object undergoing basic
     *                  business logic validation through this BasicLogic object
     * @return The relevant cached RhinoWrapper for this DTO type, or a new
     *          RhinoWrapper instance (that is also now cached for future calls)
     */
    private static JavaScriptEnvironment getExecutionEnvironment(Class<? extends Marshalable> clazz) {
        JavaScriptEnvironment result=envs.get(clazz);
        if(null==result) {
            result=new RhinoWrapper();
            envs.put(clazz, result);
            try {
                prepareRuntime(result);
            } catch(IOException ioe) {
                throw new IllegalStateException("Could not load all JavaScript Libraries", ioe);
            }
        }
        return result;
    }
    
    /**
     * Public constructor - optimized for cached dependency loading
     * 
     * @param clazz Instance of the runtime type implementing the `T' interface
     * @param dtoFactory a DTOFactory instance capable of instantiating more
     *                      objects implementing the `T' interface for DTOs
     */
    public RhinoBasicLogicImpl(Class<T> clazz, DTOFactory dtoFactory) {
        this.marshaler=new DTOMarshaler<T>(clazz, dtoFactory);
        this.runtime=RhinoBasicLogicImpl.getExecutionEnvironment(clazz);
        /*Old runtime initialization logic - comment line above and uncomment below to see performance contrast:
         this.runtime=new RhinoWrapper();
        try {
            prepareRuntime(this.runtime);
        } catch(IOException ioe) {
            throw new IllegalStateException("Could not load all JavaScript Libraries", ioe);
        }
         */
    }
    
    private static void prepareRuntime(JavaScriptEnvironment runtime) throws IOException {
        String[] libs=new String[]{"DateValidator", "NumberValidator", "StringValidator"};
        String pkg="validation";
        String ext=".js";
        for(String lib : libs) {
            StringBuilder ln=new StringBuilder(pkg);
            ln.append('/');  ln.append(lib);  ln.append(ext);
            ScriptLibrary code=new ScriptLibrary(lib, readJS(ln.toString()));
            runtime.loadLibrary(code);
        }
    }
    
    private static String readJS(String filename) throws IOException {
        ClassLoader cl=Thread.currentThread().getContextClassLoader();
        String filepath="./";
        filepath+=DateValidator.class.getPackage().getName().replace('.', '/');
        filepath=filepath.substring(0,1+filepath.lastIndexOf('/')).concat(filename);
        InputStream stream=cl.getResourceAsStream(filepath);
        StringWriter writer = new StringWriter();
        //IOUtils.copy(stream, writer, Charset.forName("UTF-8"));
        IOUtils.copy(stream, writer, "UTF-8");
        return writer.toString();
    }
    
    /**
     * Performs the application of basic business rules (bounds checking)
     * against a given data transfer object (DTO) of the `modelName' type
     * 
     * @param modelName Name of domain model type being checked against rules
     * @param fieldName String name of field with new value to be validated
     * @param dtoWithNewValue DTO object encapsulating new field value
     * @throws NullArgumentException thrown when any of the arguments passed to
     *          the method are null references for their respective types.
     * @throws ClassNotFoundException thrown when the supplied `modelName' does
     *          not relate to a registered validatable domain model class name
     */
    @Override
    public void applyRules(String domain, String field, T dto) {
        String model="var model = ";
        model+=marshaler.marshal(dto);
        this.prepare(domain);
        runtime.execute(model);
        String property=field.substring(1);
        property=Character.toUpperCase(field.charAt(0))+property;
        StringBuilder run=new StringBuilder(domain);
        run.append("Rules.validate");
        run.append(property);
        run.append("(model.");
        run.append(field);
        run.append(");");
        Object result=runtime.execute(run.toString());
        RuntimeException err=ExceptionMarshaler.unmarshal(result);
        runtime.execute("model = null;");  //clears host
        if(null!=err) { throw err; }
    }

    private void prepare(String domainModelType) {
        StringBuilder ruleset=new StringBuilder("logic/");
        ruleset.append(domainModelType);
        ruleset.append("Rules.js");
        try {
            runtime.loadLibrary(new ScriptLibrary("RULES", readJS(ruleset.toString())));
        } catch(IOException ioe) {
            throw new IllegalStateException("Could not load JavaScript Business Rules", ioe);
        } catch(UnsupportedOperationException uoe) {} //Already done RULES load
    }
    
}