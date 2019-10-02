package com.chenlw.webservice.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author chenlw
 * @date 2019/09/27
 */
public class WebserviceHttpClientTester3 {


    public static final String SERVICE_URL = "http://192.168.174.1:9090/MyWebService/testWebService?wsdl";

    /**
     * Webservice连接超时时间
     */
    public static final int CLIENT_CONNECT_TIMEOUT = 10 * 1000;

    /**
     * 直接用soapui中请求的数据
     */
    public static final String requestXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.chenlw.com/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <web:testWebService>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>%s</arg0>\n" +
            "      </web:testWebService>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>";

    public static void main(String[] args) {
        try {
            testHttpClient();
        } catch (Exception e) {
            System.out.println("错误：" + e.getMessage());
        }
    }


    public static void testHttpClient() throws Exception {
        String param = "param";
        String ticket = "ticket";
        String requestData = String.format(requestXml, param);
        String soapRes = callWebserviceUsingHttpClient(SERVICE_URL, requestData, ticket);
        System.out.println("result:" + soapRes);
    }

    /**
     * Http Client 调用Webservice接口
     *
     * @param wsdl        Webservice接口
     * @param requestData 请求数据
     * @param ticket      ticket
     * @return 结果
     */
    public static String callWebserviceUsingHttpClient(String wsdl, String requestData, String ticket) throws Exception {
        String soapResponse = null;
        try {
            // requestData可以直接用soapUi中请求的数据，注意<![CDATA[]]>的使用
            PostMethod postMethod = new PostMethod(wsdl);
            byte[] bytes = requestData.getBytes(StandardCharsets.UTF_8.displayName());
            InputStream in = new ByteArrayInputStream(bytes, 0, bytes.length);
            RequestEntity requestEntity = new InputStreamRequestEntity(in, "text/xml; charset=utf-8");
            postMethod.setRequestEntity(requestEntity);
            // 添加HTTP请求头
            postMethod.setRequestHeader("ticket", ticket);
            HttpClient client = new HttpClient();
            // 设置超时
            client.getHttpConnectionManager().getParams().setConnectionTimeout(CLIENT_CONNECT_TIMEOUT);
            client.getHttpConnectionManager().getParams().setSoTimeout(CLIENT_CONNECT_TIMEOUT);
            //
            int status = client.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                // 成功
                // 获取的结果可以参考用soapUI调用时的返回值，
                // 如果约定的返回值是XML,并不会像soapUI一样把xml用<![CDATA[]]>包含起来,要注意解析的方法,
                InputStream is = postMethod.getResponseBodyAsStream();
                soapResponse = IOUtils.toString(is, StandardCharsets.UTF_8.displayName());
            } else {
                System.out.println("调用Webservice出错;错误代码为：" + status);
            }
        } catch (Exception e) {
            System.out.println("调用Webservice出错：" + e.getMessage());
            throw e;
        }
        return soapResponse;
    }


}
