package com.chenlw.webservice.axis;


import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

/**
 * @author chenlw 2019/09/28
 */
public class AxisClientTester {

    public static final String TARGET_ENDPOINT = "http://192.168.174.1:9090/Service/ServiceHello?wsdl";
    public static final String TARGET_NAMESPACE = "http://webservice.chenlw.com/";

    public static final String METHOD_NAME = "getValue";

    public static void main(String[] args) {
        try {
            test1();
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
        }
    }

    public static void test1() throws Exception {
        String name = "12123123";
        Service service = new Service();
        Call call = (Call) service.createCall();

        call.setTargetEndpointAddress(TARGET_ENDPOINT);
        // call.setOperation("getValue");
        call.setOperationName(new QName(TARGET_NAMESPACE, METHOD_NAME));

        // 入参，在设定参数时，不使用服务端定义的参数名，而是arg0~argN来定义，也不需制定namespaceURI
        call.addParameter("arg0", XMLType.SOAP_STRING, ParameterMode.IN);
        //设置参数名 state  第二个参数表示String类型,第三个参数表示入参
        //call.addParameter(new QName(TARGET_NAMESPACE, "arg0"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);


        // 设置HTTP请求头
        //MimeHeaders mimeHeaders = call.getMessageContext().getCurrentMessage().getMimeHeaders();
        //mimeHeaders.addHeader("ticket","1231232");

        // 设置返回类型
        //call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

        String result = (String) call.invoke(new Object[]{name});
        System.out.println("result:" + result);
    }

}
