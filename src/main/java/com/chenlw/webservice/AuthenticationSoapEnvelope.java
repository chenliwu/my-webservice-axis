package com.chenlw.webservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.experimental.Accessors;


/*
<?xml version='1.0' encoding='utf-8'?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	<soap:Body>
		<ns2:authenticationResponse xmlns:ns2="http://auth.webservice.hy.com/">
			<return>
				<code>0</code>
				<data xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1
				</data>
				<msg>成功</msg>
			</return>
		</ns2:authenticationResponse>
	</soap:Body>
</soap:Envelope>

 */
/**
 * @author chenlw
 * @date 2019/09/27
 */
@Data
@Accessors
@JacksonXmlRootElement(localName = "soap:Envelope")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationSoapEnvelope {

    private static String test_data = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "\t<soap:Body>\n" +
            "\t\t<ns2:authenticationResponse xmlns:ns2=\"http://auth.webservice.hy.com/\">\n" +
            "\t\t\t<return>\n" +
            "\t\t\t\t<code>0</code>\n" +
            "\t\t\t\t<data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">0</data>\n" +
            "\t\t\t\t<msg>成功</msg>\n" +
            "\t\t\t</return>\n" +
            "\t\t</ns2:authenticationResponse>\n" +
            "\t</soap:Body>\n" +
            "</soap:Envelope>";
    private static String test_data_1 = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<soap:Envelope>\n" +
            "  <soap:Body>\n" +
            "    <ns2:authenticationResponse>\n" +
            "      <return>\n" +
            "        <code>0</code>\n" +
            "        <data>1</data>\n" +
            "        <msg>1212313</msg>\n" +
            "      </return>\n" +
            "    </ns2:authenticationResponse>\n" +
            "  </soap:Body>\n" +
            "</soap:Envelope>";

    private static String test_data_2 = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns2:authenticationResponse xmlns:ns2=\"http://auth.webservice.hy.com/\"><return><code>0</code><data xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">3</data><msg>成功</msg></return></ns2:authenticationResponse></soap:Body></soap:Envelope>";
    private static String test_data_3 = "<?xml version='1.0' encoding='UTF-8'?><soap:Envelope><soap:Body><ns2:authenticationResponse><return><code>0</code><data>1</data><msg>1212313</msg></return></ns2:authenticationResponse></soap:Body></soap:Envelope>";



    public static void main(String[]args){
        try{
            testXmlToBean();
            System.out.println("---------------------");
            testBeanToXml();
        }catch (Exception e){
            System.out.println("异常："+e.getMessage());
        }
    }

    public static void testBeanToXml() {
        try {
            AuthenticationSoapEnvelope soapEnvelope = new AuthenticationSoapEnvelope();
            AuthenticationSoapEnvelope.SoapBody soapBody = new SoapBody();
            AuthenticationSoapEnvelope.AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            AuthenticationSoapEnvelope.Return returnNode = new Return();
            returnNode.setCode(0);
            returnNode.setData("1");
            returnNode.setMsg("1212313");

            authenticationResponse.setReturnNode(returnNode);
            soapBody.setAuthenticationResponse(authenticationResponse);
            soapEnvelope.setSoapBody(soapBody);

            System.out.println(JackSonXmUtils.beanToXml(soapEnvelope));
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
        }
    }


    public static void testXmlToBean() {
        try {
            String xmlHeader = "<?xml version='1.0' encoding='utf-8'?>";
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setDefaultUseWrapper(true);
            //自动忽略无法对应pojo的字段
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            AuthenticationSoapEnvelope authenticationSoapEnvelope = xmlMapper.readValue(xmlHeader + test_data, AuthenticationSoapEnvelope.class);
            if (authenticationSoapEnvelope != null) {
                System.out.println(authenticationSoapEnvelope.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("xml 转化成对象错误：" + e.getMessage());
        }

    }



    @JacksonXmlProperty(localName = "soap:Body")
    private SoapBody soapBody;



    @Data
    @JacksonXmlRootElement(localName = "soap:Body")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SoapBody{

        @JacksonXmlProperty(localName = "ns2:authenticationResponse")
        private AuthenticationResponse authenticationResponse;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AuthenticationResponse{

        @JacksonXmlProperty(localName = "return")
        private Return returnNode;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Return{

        private Integer code;
        private String data;
        private String msg;

    }

}
