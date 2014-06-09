package flymetomars.presentation.webservices.templating;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.IOException;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class FreemarkerTemplateLoader {
    private Configuration config;

    public FreemarkerTemplateLoader(String templateDir) {
    	TemplateLoader templateLoader = new ClassTemplateLoader(getClass(), templateDir);
	    config = new Configuration();
	    config.setTemplateLoader(templateLoader);
	    config.setObjectWrapper(new DefaultObjectWrapper());
    }

    public Template getTemplate(String templateName) throws IOException {
        return config.getTemplate(templateName, "UTF-8");
    }
}
