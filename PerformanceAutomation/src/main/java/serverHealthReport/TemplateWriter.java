package serverHealthReport;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class TemplateWriter {

	VelocityEngine velocityEngine = new VelocityEngine();

	TemplateWriter() throws Exception {
		velocityEngine.init();
	}

	public String getTemplate(String templateLocation, HashMap<String, String> parameters) throws IOException {
		VelocityContext context = new VelocityContext();

		for (String parameter : parameters.keySet()) {
			context.put(parameter, parameters.get(parameter));
		}

		try {
			Template template = velocityEngine.getTemplate(templateLocation);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			return writer.toString();
		} catch (Exception e) {
			File file = new File("TemplateWriterError.log");
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.flush();
			fileWriter.write(e.getMessage());
			fileWriter.close();
			return "";
		}
	}
}
