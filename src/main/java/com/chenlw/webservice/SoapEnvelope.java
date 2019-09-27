package com.chenlw.webservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


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
@JacksonXmlRootElement(localName = "soap:Envelope")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoapEnvelope {

    public static void main(String[]args){
        try{
            SoapEnvelope soapEnvelope = new SoapEnvelope();
            SoapEnvelope.SoapBody soapBody = new SoapBody();
            SoapEnvelope.AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            SoapEnvelope.Return returnNode = new Return();
            returnNode.setCode(0);
            returnNode.setData("1");
            returnNode.setMsg("1212313");

            authenticationResponse.setReturnNode(returnNode);
            soapBody.setAuthenticationResponse(authenticationResponse);
            soapEnvelope.setSoapBody(soapBody);

            System.out.println(JackSonXmUtils.beanToXml(soapEnvelope));
        }catch (Exception e){
            System.out.println("异常："+e.getMessage());
        }


    }



    @JacksonXmlProperty(localName = "soap:Body")
    private SoapBody soapBody;



    @Data
    @JacksonXmlRootElement(localName = "soap:Body")
    public static class SoapBody{

        @JacksonXmlProperty(localName = "ns2:authenticationResponse")
        private AuthenticationResponse authenticationResponse;
    }

    @Data
    public static class AuthenticationResponse{

        @JacksonXmlProperty(localName = "return")
        private Return returnNode;
    }

    @Data
    public static class Return{

        private Integer code;
        private String data;
        private String msg;

    }

}
