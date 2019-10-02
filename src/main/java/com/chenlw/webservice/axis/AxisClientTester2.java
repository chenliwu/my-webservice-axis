package com.chenlw.webservice.axis;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

/**
 * @author chenlw
 * @date 2019/10/02
 */
public class AxisClientTester2 {

    public static final String SERVICE_URL = "http://192.168.174.1:9090/MyWebService/testWebService?wsdl";
    public static final String TARGET_NAMESPACE = "http://webservice.chenlw.com/";
    public static final String METHOD_NAME = "testWebService";

    public static void main(String[] args) {
        try {
            testAxis1();
        } catch (Exception e) {
            System.out.println("错误：" + e.getMessage());
        }
    }


    /**
     * 通过axis方式调用webservice接口
     */
    public static void testAxis1() {
        try {

            // 创建一个服务(service)调用(call)
            Service service = new Service();
            // 通过service创建call对象
            Call call = (Call) service.createCall();

            // 设置service所在URL
            call.setTargetEndpointAddress(new java.net.URL(SERVICE_URL));
            // 设置方法名，需要设定TARGET_NAMESPACE，否则可能会报错的。
            // call.setOperationName(METHOD_NAME);
            call.setOperationName(new QName(TARGET_NAMESPACE, METHOD_NAME));
            // call.setUseSOAPAction(true);

            // 变量最好只是用String类型，其他类型会报错
            // 入参，在设定参数时，不使用服务端定义的参数名，而是arg0~argN来定义，也不需制定namespaceURI
            call.addParameter("arg0", XMLType.SOAP_STRING, ParameterMode.IN);
            // 设置参数名 state  第二个参数表示String类型,第三个参数表示入参
            // call.addParameter("param", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            // 设置返回类型
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            // 此处为数组，有几个变量传几个变量
            Object jsonString = call.invoke(new Object[]{"param"});
            // 输出SOAP请求报文
            System.out.println("--SOAP Request: " + call.getMessageContext().getRequestMessage().getSOAPPartAsString());
            // 输出SOAP返回报文
            System.out.println("--SOAP Response: " + call.getResponseMessage().getSOAPPartAsString());
            // 输出返回信息
            System.out.println("result===" + jsonString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
