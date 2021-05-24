package pl.kwl.jasper;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JasperMain {

    public static void main(String[] args) throws JRException, IOException {
        evaluateJasperExpression();
        evaluateJasperExpression();
        evaluateJasperExpression();
    }

    private static void evaluateJasperExpression() throws JRException, IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(contentWithImg.getBytes(StandardCharsets.UTF_8));
        JasperDesign jasperDesign = JRXmlLoader.load(stream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        DefaultJasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
		List<JRExpression> compiledExpression = JRExpressionCollector.collectExpressions(jasperReportsContext, jasperReport);
        int size = compiledExpression.size();
        JRExpression expression = compiledExpression.get(size - 1);

        JREvaluator evaluator = JasperCompileManager.loadEvaluator(jasperReport);
        Object jasperEvaluatedValue = evaluator.evaluate(expression);

        System.out.println("\nNext iteration");
        System.out.println(expression.getText());
        if (jasperEvaluatedValue != null) {
            System.out.println("My value is evaluated!! " + jasperEvaluatedValue);
        } else {
            System.out.println("Now I'm just some null... ");
        }
        stream.close();
    }

    private static final String contentWithImg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "        <jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"TO_JEST_NAZWA_SZABLONU\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"058c42f8-7e0c-4bfb-8d6e-dec7ae3dc2c5\">\n" +
            "            <parameter name=\"param\" class=\"java.lang.String\" />\n" +
            "            <queryString>\n" +
            "                <![CDATA[]]>\n" +
            "            </queryString>\n" +
            "            <title>\n" +
            "                <band height=\"93\" splitType=\"Stretch\">\n" +
            "                    <textField>\n" +
            "                        <reportElement x=\"0\" y=\"0\" width=\"550\" height=\"20\" uuid=\"197611c5-ead6-47fc-bc5d-f5187ac6588a\"/>\n" +
            "                        <textFieldExpression><![CDATA[$P{param}]]></textFieldExpression>\n" +
            "                    </textField>\n" +
            "                    <image>\n" +
            "                        <reportElement x=\"0\" y=\"20\" width=\"60\" height=\"40\" uuid=\"a1219e07-48d3-40b3-9603-2a97a817da87\"/>\n" +
            "                        <imageExpression><![CDATA[1 == 1 ? \"https://media.os.fressnapf.com/cms/2020/05/Ratgeber-Katze-Navigation-KItten_1200x527.jpg?t=cmsimg_920\" : \"http://fundacjawspomaganiawsi.pl/wp-content/uploads/2018/07/KCh-1014x576.jpg\"]]></imageExpression>\n" +
            "                    </image>\n" +
            "                </band>\n" +
            "            </title>\n" +
            "        </jasperReport>";
}
