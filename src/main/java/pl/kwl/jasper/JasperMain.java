package pl.kwl.jasper;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JREvaluator;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillParameter;
import net.sf.jasperreports.engine.fill.JRFillVariable;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperMain {

    public static void main(String[] args) throws JRException, IOException {
        evaluateJasperExpression();
    }

    private static void evaluateJasperExpression() throws JRException, IOException {
        // load jrxml and compile report
        ByteArrayInputStream stream = new ByteArrayInputStream(contentWithImg.getBytes(StandardCharsets.UTF_8));
        JasperDesign jasperDesign = JRXmlLoader.load(stream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        stream.close();

        // prepare map of parameters provided by user
        Map<String, Object> map = new HashMap<>();
        map.put("param", "param");

        // fetch expression
        DefaultJasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
        List<JRExpression> compiledExpressions = JRExpressionCollector.collectExpressions(jasperReportsContext, jasperReport);
        int size = compiledExpressions.size();
        JRExpression expression = compiledExpressions.get(size - 1);

        // prepare evaluator
        JREvaluator evaluator = JasperCompileManager.loadEvaluator(jasperReport);

        // prepare maps for evaluator init
        Map<String, JRFillParameter> parametersMap = new HashMap<>();
        Map<String, JRFillField> fieldsMap = new HashMap<>();
        Map<String, JRFillVariable> variablesMap = new HashMap<>();
//        evaluator.init(parametersMap, fieldsMap, variablesMap, WhenResourceMissingTypeEnum.EMPTY, false);

        // evaluate expression
        // note: jeżeli w expression będzie param który ma wartość default, to zwróci tutaj null bo nie widzi nawet takiego default parametru
        Object jasperEvaluatedValue = evaluator.evaluate(expression);

        // save file
//        savePdf(jasperReport, map);

        // logging
        log(expression, jasperEvaluatedValue);
    }

    private static void savePdf(JasperReport jasperReport, Map<String, Object> parameters) throws JRException, IOException {
        JasperPrint resultJasper = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        OutputStream outStream = new FileOutputStream("/tmp/jreport.pdf");
        JasperExportManager.exportReportToPdfStream(resultJasper, outStream);
        outStream.close();
    }

    private static void log(JRExpression expression, Object jasperEvaluatedValue) {
        System.out.println(expression.getText());
        if (jasperEvaluatedValue != null) {
            System.out.println("My value is evaluated!! " + jasperEvaluatedValue);
        } else {
            System.out.println("Now I'm just some null... ");
        }
    }

    private static final String contentWithImg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "        <jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" name=\"TO_JEST_NAZWA_SZABLONU\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\" uuid=\"058c42f8-7e0c-4bfb-8d6e-dec7ae3dc2c5\">\n" +
            "            <parameter name=\"param\" class=\"java.lang.String\">\n" +
            "                <defaultValueExpression><![CDATA[\"default\"]]></defaultValueExpression>\n" +
            "            </parameter>" +
            "            <parameter name=\"param2\" class=\"java.lang.String\" isForPrompting=\"false\">\n" +
            "                <defaultValueExpression><![CDATA[\"test\"]]></defaultValueExpression>\n" +
            "            </parameter>" +
            "            <queryString>\n" +
            "                <![CDATA[]]>\n" +
            "            </queryString>\n" +
            "            <title>\n" +
            "                <band height=\"93\" splitType=\"Stretch\">\n" +
            "                    <textField>\n" +
            "                        <reportElement x=\"0\" y=\"0\" width=\"550\" height=\"20\" uuid=\"197611c5-ead6-47fc-bc5d-f5187ac6588a\"/>\n" +
            "                        <textFieldExpression><![CDATA[$P{param}]]></textFieldExpression>\n" +
            "                    </textField>\n" +
            "                    <textField>\n" +
            "                        <reportElement x=\"0\" y=\"20\" width=\"550\" height=\"20\" uuid=\"197611c5-ead6-47fc-bc5d-f5187ac6588b\"/>\n" +
            "                        <textFieldExpression><![CDATA[$P{param2}]]></textFieldExpression>\n" +
            "                    </textField>\n" +
            "                    <image>\n" +
            "                        <reportElement x=\"0\" y=\"40\" width=\"60\" height=\"40\" uuid=\"a1219e07-48d3-40b3-9603-2a97a817da87\"/>\n" +
            "                        <imageExpression><![CDATA[$P{param}.toString().equals(\"param\") ? \"https://dziendobry.tvn.pl/media/cache/content/imie-dla-kotki-jak-wybrac-oryginalne-imie-i-dobrze-dopasowac-je-do-kotki-jpg.jpg\" : \"https://mry.pl/app/uploads/2020/10/somojed.jpg\"]]></imageExpression>\n" +
            "                    </image>\n" +
            "                </band>\n" +
            "            </title>\n" +
            "        </jasperReport>";

}
