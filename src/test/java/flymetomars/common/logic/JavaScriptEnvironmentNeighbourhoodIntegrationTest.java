package flymetomars.common.logic;

import flymetomars.common.FMTMValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * "Neighbourhood" style Integration Test for testing the RhinoWrapper
 * implementation of the JavaScriptEnvironment interface of logic package.
 * 
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class JavaScriptEnvironmentNeighbourhoodIntegrationTest {

    private JavaScriptEnvironment script;
    
    @Before
    public void setUp() {
        if(null==script) { this.script=new RhinoWrapper(); }
    }
    
    @Test
    public void testLoadLibrarySuccess() {
        ScriptLibrary lib=new ScriptLibrary("TEST", "test=\"Testing\";");
        this.script.loadLibrary(lib);
        Assert.assertEquals("Testing", script.execute("test+'';"));
    }
    
    @Test
    public void testExecuteSuccess() {
        script.execute("var x=\"Res\";");
        Assert.assertEquals("Results", script.execute("x+'ults'"));
    }
    
    @Test
    public void testLoadLibraryAndExecuteSuccess() {
        String codes="var world=new Object() {\n";
        codes += "hello : function() { return 'Hello World'; }\n";
        codes += "};\n";
        ScriptLibrary lib=new ScriptLibrary("HelloWorld", codes);
        this.script.loadLibrary(lib);
        Assert.assertEquals("Hello World", script.execute("world.hello()"));
    }
    
    @Test(expected = FMTMValidationException.class)
    public void testExecuteAndPassToExceptionMarshalerFailure() {
        Object result=script.execute("new Array(1, 'testing!');");
        throw ExceptionMarshaler.unmarshal(result);
    }    
}
