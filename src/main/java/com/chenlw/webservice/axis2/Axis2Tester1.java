package com.chenlw.webservice.axis2;


import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.NamedValue;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * AXIS2调用Webservice
 *
 * @author chenlw
 * @date 2019/10/02
 */
public class Axis2Tester1 {


    public static final String targetEndpoint = "http://192.168.174.1:9090/MyWebService/testWebService?wsdl";
    public static final String targetNamespace = "http://webservice.chenlw.com/";

    public static final String METHOD = "testWebService";


    public static void main(String[] args) {
        try {
            test1();
        } catch (Exception e) {
            System.out.println("异常：" + e.getMessage());
        }
    }

    public static void test1() throws Exception {
        //RPCServiceClient是RPC方式调用
        RPCServiceClient client = new RPCServiceClient();
        Options options = client.getOptions();
        //设置调用WebService的URL
        EndpointReference epf = new EndpointReference(targetEndpoint);
        options.setTo(epf);
        // 添加HTTP请求头
        List<NamedValue> headerList = new ArrayList<>();
        headerList.add(new NamedValue("ticket", "ticket"));
        options.setProperty(HTTPConstants.HTTP_HEADERS, headerList);
        // 设置超时
        options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, 10 * 1000);
        // 取消重复请求
        options.setProperty(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));

        QName qname = new QName(targetNamespace, METHOD);
        //指定调用的方法和传递参数数据，及设置返回值的类型
        Object[] result = client.invokeBlocking(qname, new Object[]{"param"}, new Class[]{String.class});
        System.out.println(result[0]);
    }


}
