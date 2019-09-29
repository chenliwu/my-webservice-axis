package com.chenlw.webservice.httpclient;

import com.chenlw.webservice.AuthenticationSoapEnvelope;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.axis.utils.StringUtils;
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
public class WebserviceHttpClientTester {

    public static final String SERVICE_URL = "http://10.0.36.240:9000/soap/webservice/authentication?wsdl";

    /**
     * Webservice连接超时时间
     */
    public static final int CLIENT_CONNECT_TIMEOUT = 10 * 1000;

    public static void main(String[] args) {
        try {
            testHttpClient();
        } catch (Exception e) {
            System.out.println("错误：" + e.getMessage());
        }
    }


    public static void testHttpClient() throws Exception {
        // requestData可以直接用soapui中请求的数据
        String requestXml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:auth=\"http://auth.webservice.hy.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <auth:authentication>\n" +
                "         <userid>%s</userid>\n" +
                "         <password>%s</password>\n" +
                "      </auth:authentication>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        String userid = "100857";
        String password = "ia04agZTT0n1TZoiDUivNVqdk3m6RPjJJl9a/izRvt3Swp4RXihgrnbqrguFIrx+LwqoG+QOGRW3TyP3eNdqDg==";
        String ticket = "18627da2-1f40-48da-87b5-c4f4dec31089";
        String requestData = String.format(requestXml, userid, password);
        String res = callWebservice(SERVICE_URL, requestData, ticket);
        if (!StringUtils.isEmpty(requestData)) {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setDefaultUseWrapper(false);
            //自动忽略无法对应pojo的字段
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            AuthenticationSoapEnvelope authenticationSoapEnvelope = xmlMapper.readValue(res, AuthenticationSoapEnvelope.class);
            if (authenticationSoapEnvelope != null) {
                System.out.println(authenticationSoapEnvelope.toString());
            }
        }

    }

    /**
     * Http Client 调用Webservice接口
     * // 不要设置SOAPAction
     * //String soapAction = "authentication";
     * //postMethod.setRequestHeader("SOAPAction", soapAction);
     *
     * @param wsdl        Webservice接口
     * @param requestData 请求数据
     * @param ticket      ticket
     * @return 结果
     */
    public static String callWebservice(String wsdl, String requestData, String ticket) {
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
                InputStream is = postMethod.getResponseBodyAsStream();
                /**
                 * 获取的结果可以参考用soapUI调用时的返回值，
                 * 如果约定的返回值是XML,并不会像soapUI一样把xml用<![CDATA[]]>包含起来,要注意解析的方法,
                 */
                String resString = IOUtils.toString(is, StandardCharsets.UTF_8.displayName());
                System.out.println(resString);
            } else {
                System.out.println("调用Webservice出错;错误代码为：" + status);
            }
        } catch (Exception e) {
            System.out.println("调用Webservice出错：" + e.getMessage());
        }
        return null;
    }


}
