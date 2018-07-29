package dev.gsitgithub.yarg.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.loaders.impl.GroovyDataLoader;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.Report;
import com.haulmont.yarg.structure.ReportBand;
import com.haulmont.yarg.structure.ReportOutputType;
import com.haulmont.yarg.structure.impl.BandBuilder;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportFieldFormatImpl;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;
import com.haulmont.yarg.structure.xml.impl.DefaultXmlReader;
import com.haulmont.yarg.util.groovy.DefaultScriptingImpl;

public class Groovy1TemplateMain {

	public static void main(String[] args) throws IOException {
		boolean xmltemplate = false;
		Report report = xmltemplate ? getReportObjectFromXmlTemplate():createReportObject();

        Reporting reporting = new Reporting();
        reporting.setFormatterFactory(new DefaultFormatterFactory());
        reporting.setLoaderFactory(
                new DefaultLoaderFactory().setGroovyDataLoader(new GroovyDataLoader(new DefaultScriptingImpl())));

        ReportOutputDocument reportOutputDocument = reporting.runReport(
                new RunParams(report), new FileOutputStream("./src/main/resources/reports/invoice.pdf"));

//		ReportOutputDocument reportOutputDocument = reporting.runReport(
//		        new RunParams(report), new FileOutputStream("./src/main/resources/reports/invoice.pdf"));
	}
	
	private static Report getReportObjectFromXmlTemplate() throws IOException {
		// TODO Auto-generated method stub
		Report report = new DefaultXmlReader()
				.parseXml(readFileToString(new File("./src/main/resources/templates/groovy/groovy-template.xml")));
		return report;
	}
	
	private static Report createReportObject() throws IOException {
		// TODO Auto-generated method stub
		ReportBuilder reportBuilder = new ReportBuilder();
        ReportTemplateBuilder reportTemplateBuilder = new ReportTemplateBuilder()
                .documentPath("./src/main/resources/templates/groovy/template.docx")
                .documentName("invoice.docx")
                .outputType(ReportOutputType.pdf)
                .readFileFromPath();
        reportBuilder.template(reportTemplateBuilder.build());
        BandBuilder bandBuilder = new BandBuilder();
        ReportBand main= bandBuilder.name("Main").query("Main", "return [\n" +
                "                              [\n" +
                "                               'invoiceNumber':99987,\n" +
                "                               'client' : 'Google Inc.',\n" +
                "                               'date' : new Date(),\n" +
                "                               'addLine1': '1600 Amphitheatre Pkwy',\n" +
                "                               'addLine2': 'Mountain View, USA',\n" +
                "                               'addLine3':'CA 94043',\n" +
                "                               'signature':'<html><body><b><font color=\"red\">Mr. Yarg</font></b></body></html>'\n" +
                "                            ]]", "groovy").build();


        bandBuilder = new BandBuilder();
        ReportBand items = bandBuilder.name("Items").query("Items", "return [\n" +
                "                                ['name':'Solar plasma', 'price' : 15000],\n" +
                "                                ['name':'Flying tables', 'price' : 13000],\n" +
                "                                ['name':'Black T-shirts', 'price' : 12000]\n" +
                "                            ]", "groovy").build();

        reportBuilder.band(main);
        reportBuilder.band(items);
        reportBuilder.format(new ReportFieldFormatImpl("Main.signature", "${html}"));

        Report report = reportBuilder.build();
        return report;
	}
}
