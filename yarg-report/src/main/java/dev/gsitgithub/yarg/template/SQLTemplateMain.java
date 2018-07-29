package dev.gsitgithub.yarg.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import static org.apache.commons.io.FileUtils.readFileToString;

import com.haulmont.yarg.formatters.factory.DefaultFormatterFactory;
import com.haulmont.yarg.loaders.factory.DefaultLoaderFactory;
import com.haulmont.yarg.loaders.impl.SqlDataLoader;
import com.haulmont.yarg.reporting.ReportOutputDocument;
import com.haulmont.yarg.reporting.Reporting;
import com.haulmont.yarg.reporting.RunParams;
import com.haulmont.yarg.structure.Report;
import com.haulmont.yarg.structure.ReportBand;
import com.haulmont.yarg.structure.ReportOutputType;
import com.haulmont.yarg.structure.impl.BandBuilder;
import com.haulmont.yarg.structure.impl.ReportBuilder;
import com.haulmont.yarg.structure.impl.ReportTemplateBuilder;
import com.haulmont.yarg.structure.xml.impl.DefaultXmlReader;

import dev.gsitgithub.db.TestDatabase;

public class SQLTemplateMain {
	
	public static void main(String[] args) throws Exception {
		TestDatabase testDatabase = new TestDatabase();
		testDatabase.setUpDatabase();
		
		try {
            Connection connection = testDatabase.getDs().getConnection();
            try {
                connection.createStatement().executeUpdate("drop table store;");
            } catch (SQLException e) {
                //ignore
            }
            try {
                connection.createStatement().executeUpdate("drop table book;");
            } catch (SQLException e) {
                //ignore
            }

            connection.createStatement().executeUpdate("create table store (id integer, name varchar(200), address varchar(200));");
            connection.createStatement().executeUpdate("create table book(id integer, name varchar(200), author varchar(200), price decimal, store_id integer);");

            connection.createStatement().executeUpdate("insert into store values(1, 'Main store', 'Some street');");
            connection.createStatement().executeUpdate("insert into store values(2, 'Secondary store', 'Another street');");

            connection.createStatement().executeUpdate("insert into book values(1, 'Concurrency in practice', 'Brian Goetz', 10.0, 1);");
            connection.createStatement().executeUpdate("insert into book values(2, 'Concurrency in practice', 'Brian Goetz', 10.0, 1);");
            connection.createStatement().executeUpdate("insert into book values(2, 'Concurrency in practice', 'Brian Goetz', 10.0, 1);");
            connection.createStatement().executeUpdate("insert into book values(3, 'Effective Java', 'Josh Bloch', 20.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(4, 'Effective Java', 'Josh Bloch', 20.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(4, 'Effective Java', 'Josh Bloch', 20.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(5, 'Effective Java', 'Josh Bloch', 20.0, 1);");
            connection.createStatement().executeUpdate("insert into book values(5, 'Effective Java', 'Josh Bloch', 20.0, 1);");
            connection.createStatement().executeUpdate("insert into book values(6, 'Concurrency in practice', 'Brian Goetz', 10.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(7, 'Refactoring', 'Martin Fowler', 15.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(8, 'Refactoring', 'Martin Fowler', 15.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(8, 'Refactoring', 'Martin Fowler', 15.0, 2);");
            connection.createStatement().executeUpdate("insert into book values(9, 'Refactoring', 'Martin Fowler', 15.0, 1);");

            connection.commit();

            boolean xmltemplate = true;
    		Report report = xmltemplate ? getReportObjectFromXmlTemplate():createReportObject();
    		
    		Reporting reporting = new Reporting();
    		reporting.setFormatterFactory(new DefaultFormatterFactory());
    		reporting.setLoaderFactory(
    		        new DefaultLoaderFactory().setSqlDataLoader(new SqlDataLoader(testDatabase.getDs())));

    		File outputFile = new File("./src/main/resources/reports/bookstore.xlsx");
    		if (!outputFile.exists()) outputFile.createNewFile();
    		ReportOutputDocument reportOutputDocument = reporting.runReport(
    		        new RunParams(report), new FileOutputStream(outputFile));
            
            
//            Report report = new DefaultXmlReader().parseXml(FileUtils.readFileToString(new File("./modules/core/test/sample/bookstore/bookstore.xml")));
//            System.out.println();
//
//            Reporting reporting = new Reporting();
//            reporting.setFormatterFactory(new DefaultFormatterFactory());
//            reporting.setLoaderFactory(new DefaultLoaderFactory()
//                    .setGroovyDataLoader(new GroovyDataLoader(new DefaultScriptingImpl()))
//                    .setSqlDataLoader(new SqlDataLoader(testDatabase.getDs())));
//            ReportOutputDocument reportOutputDocument = reporting.runReport(new RunParams(report), new FileOutputStream("./result/sample/bookstore.xls"));
        } finally {
            testDatabase.stop();
        }
	}
	
	private static Report getReportObjectFromXmlTemplate() throws IOException {
		// TODO Auto-generated method stub
		Report report = new DefaultXmlReader()
				.parseXml(readFileToString(new File("./src/main/resources/templates/sql/sql-template.xml")));
		return report;
	}
	private static Report createReportObject() throws IOException {
		// TODO Auto-generated method stub
		ReportBuilder reportBuilder = new ReportBuilder();
		ReportTemplateBuilder reportTemplateBuilder = new ReportTemplateBuilder()
		        .documentPath("./src/main/resource/templates/sql/template.xls")
		        .documentName("bookstore.xls")
		        .outputType(ReportOutputType.xls)
		        .readFileFromPath();
		reportBuilder.template(reportTemplateBuilder.build());
		BandBuilder bandBuilder = new BandBuilder();
		ReportBand staff= bandBuilder.name("Staff")
		        .query("Staff", "select name, surname, position from staff", "sql")
		        .build();
		reportBuilder.band(staff);
		Report report = reportBuilder.build();
		return report;
	}

}
