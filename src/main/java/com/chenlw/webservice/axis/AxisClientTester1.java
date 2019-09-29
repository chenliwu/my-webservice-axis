package com.chenlw.webservice.axis;

import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;

import javax.xml.soap.MimeHeaders;

/**
 * @author chenlw
 * @date 2019/09/29
 */
public class AxisClientTester1 {

    public static final String SERVICE_URL = "http://10.0.36.240:9000/soap/webservice/authentication?wsdl";

    public static final String METHOD_NAME = "authentication";
    public static final String TICKET = "18627da2-1f40-48da-87b5-c4f4dec31089";

    public static void main(String[] args) {
        try {
            // testAxis1();
            testAxis2();
        } catch (Exception e) {
            System.out.println("错误：" + e.getMessage());
        }
    }


    /**
     * 通过axis方式调用webservice接口
     */
    public static void testAxis1() {
        try {
            // 指出service所在完整的URL
            String endpoint = SERVICE_URL;
            //所调用接口的方法method
            String method = "authentication";
            String ticket = "18627da2-1f40-48da-87b5-c4f4dec31089";


            // 创建一个服务(service)调用(call)
            Service service = new Service();
            // 通过service创建call对象
            Call call = (Call) service.createCall();

            // 设置service所在URL
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            // 设置方法名
            call.setOperationName(method);
            call.setUseSOAPAction(true);


            // 变量最好只是用String类型，其他类型会报错
            // 设置参数名 state  第二个参数表示String类型,第三个参数表示入参
            call.addParameter("userid", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("password", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            // 设置返回类型
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            // String path = targetNamespace + method;
            // call.setSOAPActionURI(path);
            // 此处为数组，有几个变量传几个变量
            Object jsonString = call.invoke(new Object[]{"100857", "1232132123"});
            //输出SOAP请求报文
            System.out.println("--SOAP Request: " + call.getMessageContext().getRequestMessage().getSOAPPartAsString());
            //输出SOAP返回报文
            System.out.println("--SOAP Response: " + call.getResponseMessage().getSOAPPartAsString());
            //输出返回信息
            System.out.println("result===" + jsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 通过axis方式调用webservice接口
     */
    public static void testAxis2() {
        try {

            // 创建一个服务(service)调用(call)
            Service service = new Service();
            // 通过service创建call对象
            Call call = (Call) service.createCall();

            // 设置service所在URL
            call.setTargetEndpointAddress(new java.net.URL(SERVICE_URL));
            // 设置方法名
            call.setOperationName(METHOD_NAME);
            call.setUseSOAPAction(true);


            // 变量最好只是用String类型，其他类型会报错
            // 设置参数名 state  第二个参数表示String类型,第三个参数表示入参
            call.addParameter("userid", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("password", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            // 设置返回类型
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            // 此处为数组，有几个变量传几个变量
            Message message = buildMessage(call, "100857", "1212323", TICKET);
            SOAPEnvelope soapEnvelope = call.invoke(message);
            Object jsonString = null;
            if (soapEnvelope != null) {
                jsonString = soapEnvelope.getBodyElements();
            }

            //输出SOAP请求报文
            System.out.println("--SOAP Request: " + call.getMessageContext().getRequestMessage().getSOAPPartAsString());
            //输出SOAP返回报文
            System.out.println("--SOAP Response: " + call.getResponseMessage().getSOAPPartAsString());
            //输出返回信息
            System.out.println("result===" + jsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Message buildMessage(Call call, String userid, String password, String ticket) throws Exception {
        MessageContext messageContext = call.getMessageContext();
        SOAPEnvelope env = new SOAPEnvelope(messageContext.getSOAPConstants(), messageContext.getSchemaVersion());
        //env.addBodyElement(new SOAPBodyElement("userid", userid));
        //env.addBodyElement(new SOAPBodyElement("password", password));
        Message message = new Message(env);
        MimeHeaders mimeHeaders = message.getMimeHeaders();
        if (mimeHeaders != null) {
            mimeHeaders.addHeader("ticket", ticket);
        }
        return message;
    }

}
