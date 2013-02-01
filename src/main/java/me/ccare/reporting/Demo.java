package me.ccare.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.ccare.reporting.domain.Person;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

public class Demo {

	public static void main(final String[] args) {
		// Some model data that I want to merge into the template
		final Person person = new Person();
		person.setFirstName("Charles");
		person.setLastName("Care");
		person.setColour("Green");

		try {
			// 1) Load ODT file by filling Velocity template engine and cache it to the registry
			final InputStream in = Demo.class.getResourceAsStream("/template2.odt");
			final IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

			// 2) Create context Java model
			final IContext context = report.createContext();
			context.put("person", person);

			// 3) Generate report by merging Java model with the ODT
			final OutputStream out = new FileOutputStream(new File("out2.pdf"));
			//report.process(context, out);

			final Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
			report.convert(context, options, out);

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final XDocReportException e) {
			e.printStackTrace();
		}
	}
}
