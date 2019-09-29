package com.chenlw.webservice.httpclient;

import com.chenlw.webservice.soap.SoapParseUtils;
import org.apache.axis.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author chenlw
 * @date 2019/09/27
 */
public class WebserviceHttpClientTester1 {

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
        String soapRes = callWebserviceUsingHttpClient(SERVICE_URL, requestData, ticket);
        if (!StringUtils.isEmpty(soapRes)) {
            Map<String, String> resultMap = SoapParseUtils.parse(soapRes);
            System.out.println(resultMap.toString());
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
    public static String callWebserviceUsingHttpClient(String wsdl, String requestData, String ticket) throws Exception {
        String soapResponse = null;
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        try {
            HttpPost httpPost = new HttpPost(wsdl);
            // 添加HTTP请求头
            httpPost.setHeader("ticket", ticket);

            // 设置连接参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setSocketTimeout(CLIENT_CONNECT_TIMEOUT)
                    .build();
            httpPost.setConfig(requestConfig);

            // 请求正文
            StringEntity stringEntity = new StringEntity(requestData, StandardCharsets.UTF_8.displayName());
            stringEntity.setContentType(ContentType.APPLICATION_XML.toString());
            httpPost.setEntity(stringEntity);
            // 发送请求
            CloseableHttpResponse response = client.execute(httpPost);
            int responseStatusCode = response.getStatusLine().getStatusCode();
            if (responseStatusCode != HttpStatus.SC_OK) {
                System.out.println("Webservice接口未能正确处理请求，HTTP响应状态码为：" + responseStatusCode);
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                soapResponse = EntityUtils.toString(entity, StandardCharsets.UTF_8.displayName());
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
            System.out.println("调用Webservice出错：" + e.getMessage());
            throw e;
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return soapResponse;
    }


}
