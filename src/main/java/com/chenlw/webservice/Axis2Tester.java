package com.chenlw.webservice;

import org.apache.axiom.mime.Header;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.NamedValue;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenlw
 * @date 2019/09/27
 */
public class Axis2Tester {


    public static final String targetEndpoint = "http://10.0.36.240:9000/soap/webservice/authentication?wsdl";
    public static final String targetNamespace = "http://auth.webservice.hy.com/";

    public static final String METHOD = "authentication";


    public static void main(String[] args) {
        try {
            // test2();
            test3();
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
        }
    }


    public static String send(String paramData) {
        System.out.println("WebService发送请求......");
        try {
            RPCServiceClient client = new RPCServiceClient();
            Options options = client.getOptions();
            options.setTo(new EndpointReference(targetEndpoint));
            // 毫秒单位
            options.setTimeOutInMilliSeconds(1000 * 60 * 5);
            options.setAction(METHOD);
            Object[] response = client.invokeBlocking(new QName(targetNamespace, METHOD), new Object[]{paramData}, new Class[]{String.class});
            String results = (String) response[0];
            System.out.println("WebService请求返回结果: " + response);
            return results;
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static void test2() {
        String url = targetEndpoint;
        String methodString = METHOD;
        String ticket = "18627da2-1f40-48da-87b5-c4f4dec31089";
        try {
            ServiceClient serviceClient = new ServiceClient();
            Options options = new Options();

            // 添加EndpointReference
            EndpointReference endpointReference = new EndpointReference(url);
            options.setTo(endpointReference);
            options.setAction(targetNamespace + METHOD);
            options.setExceptionToBeThrownOnSOAPFault(false);


            // 设置超时
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, 10 * 1000);
            // 取消重复请求
            options.setProperty(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
            serviceClient.setOptions(options);
            // 添加HTTP请求头
            List<NamedValue> headerList = new ArrayList<>();
            headerList.add(new NamedValue("ticket",ticket));
            options.setProperty(HTTPConstants.HTTP_HEADERS,headerList);


            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNamespace = fac.createOMNamespace(targetNamespace, "unqualified");
            OMElement method = fac.createOMElement(methodString, omNamespace);

            // 添加请求参数
            OMElement userId = fac.createOMElement("userid", omNamespace);
            userId.addChild(fac.createOMText(userId, "100857"));

            OMElement password = fac.createOMElement("password", omNamespace);
            password.addChild(fac.createOMText(password, "100857"));
            method.addChild(userId);
            method.addChild(password);


            OMElement res = serviceClient.sendReceive(method);
            String result = res.getFirstElement().getText();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("异常：" + e.getMessage());
        }
    }


    public static void test3() {
        String ticket = "18627da2-1f40-48da-87b5-c4f4dec31089";
        try {
            EndpointReference endpointReference = new EndpointReference(targetEndpoint);
            //创建一个OMFactory
            OMFactory factory = OMAbstractFactory.getOMFactory();
            //指定命名空间
            OMNamespace namespace = factory.createOMNamespace(targetNamespace, "sequence");
            //创建method对象，方法名 为getMobileCodeInfo
            OMElement method = factory.createOMElement(METHOD, namespace);

            //方法参数
            OMElement userId = factory.createOMElement("userid", namespace);
            //方法参数
            OMElement password = factory.createOMElement("password", namespace);

            //封装参数
            //设定参数的值1
            userId.addChild(factory.createOMText(userId, "100857"));
            method.addChild(userId);
            //设定参数的值2
            password.addChild(factory.createOMText(password, "2132132123132"));
            method.addChild(password);

            //请求参数设置
            ServiceClient sender = new ServiceClient();
            Options options = new Options();
            options.setAction(targetNamespace + METHOD);
            options.setTo(endpointReference);
            // 这句很关键，解决异常org.apache.axis2.AxisFault: Unmarshalling Error: 意外的元素
            options.setProperty(org.apache.axis2.Constants.Configuration.DISABLE_SOAP_ACTION, true);

            // 添加HTTP请求头
            List<NamedValue> headerList = new ArrayList<>();
            headerList.add(new NamedValue("ticket",ticket));
            options.setProperty(HTTPConstants.HTTP_HEADERS,headerList);
            // 设置超时
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, 10 * 1000);
            // 取消重复请求
            options.setProperty(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
            sender.setOptions(options);



            OMElement result = sender.sendReceive(method);
            Iterator iterator = result.getChildElements();
            while (iterator.hasNext()){
                System.out.println(iterator.next());
            }
            System.out.println( result.getText());

            System.out.println(result.getFirstElement().getText());
        } catch (AxisFault ex) {
            ex.printStackTrace();
        }
    }


}
