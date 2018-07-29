package dev.gsitgithub.yarg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.loaders.impl.JsonDataLoader;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.Report;
import com.haulmont.yarg.structure.ReportBand;
import com.haulmont.yarg.structure.ReportOutputType;
import com.haulmont.yarg.structure.impl.BandBuilder;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Main m = new Main();
		
		File g = new File("./src/main/resources/reports/Letter.docx");
		if (!g.exists())
			g.createNewFile();
		
		String json = m.getJsonData();
		ReportOutputDocument doc = m.generateReportDocument(json);
	}

	/** STEP: 4
	 * define the Reporting object, which is responsible for inserting the data
	 * into the template and generate the final document:
	 */
	private ReportOutputDocument generateReportDocument(String json) throws IOException {
		// TODO Auto-generated method stub
		Reporting reporting = new Reporting();
		reporting.setFormatterFactory(new DefaultFormatterFactory());
		reporting.setLoaderFactory(new DefaultLoaderFactory().setJsonDataLoader(new JsonDataLoader()));

//		response.setContentType(
//		 "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//		reporting.runReport(new RunParams(getYargReportObject()).param("param1", json), response.getOutputStream());
		ReportOutputDocument outputDocument = reporting
				.runReport(new RunParams(getYargReportObject()).param("param1", json),
						new FileOutputStream("./src/main/resources/reports/Letter.docx"));
		return outputDocument;
	}

	/** STEP: 3
	 */
	private Report getYargReportObject() throws IOException {
		ReportBuilder reportBuilder = new ReportBuilder();

		// ReportTemplateBuilder loads our previously defined Letter.docx template 
		// by specifying the path, name, and output-type of the document.
		ReportTemplateBuilder rtb = getReportTemplate();
		reportBuilder.template(rtb.build());

		ReportBand rb = getReportBandFromJsonData();
		reportBuilder.band(rb);

//		ReportBuilder class is going to be responsible for the creation of the report, grouping up the template and the data.
		Report report = reportBuilder.build();
		return report;
	}

	/** STEP: 1
	 * @return
	 * @throws IOException
	 */
	private ReportTemplateBuilder getReportTemplate() throws IOException {
		ReportTemplateBuilder reportTemplateBuilder = null;
			reportTemplateBuilder = new ReportTemplateBuilder().documentPath("./src/main/resources/Letter.docx")
					.documentName("Letter.docx").outputType(ReportOutputType.docx).readFileFromPath();
		return reportTemplateBuilder;
	}

	/** STEP: 2
	 * define a BandBuilder in order to create a ReportBand, which is the abstraction that YARG uses for the groups
	 * of data that we defined earlier in the template document
	 * @return
	 */
	private ReportBand getReportBandFromJsonData() {
		// TODO Auto-generated method stub
		BandBuilder bandBuilder = new BandBuilder();
		ReportBand main = bandBuilder.name("Main").query("Main", "parameter=param1 $.main", "json").build();
		return main;
	}

	private String getJsonData() throws IOException {
		String json = FileUtils.readFileToString(new File("./src/main/resources/Data.json"));
		return json;
	}
}
