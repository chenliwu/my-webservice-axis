package com.chenlw.webservice.hq;

import com.chenlw.webservice.JackSonXmUtils;
import com.chenlw.webservice.soap.SoapParseUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
public class HqWebserviceHttpClientTester {

    public static final String SERVICE_URL = "http://192.168.0.193:9090/epp/services/WeixinServerForSendTouser";

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
            "      <web:returnNotificationSuccessfulResult>\n" +
            "         <!--Optional:-->\n" +
            "         <arg0>%s</arg0>\n" +
            "      </web:returnNotificationSuccessfulResult>\n" +
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

        Map<String,String> map = SoapParseUtils.parse(soapRes);
        System.out.println(map.toString());

        String jsonData = map.get("return");
        if(jsonData!=null && jsonData.length() > 0){
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonData);
            System.out.println(jsonNode.toString());

        }

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
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        try {
            HttpPost httpPost = new HttpPost(wsdl);
            // 添加HTTP请求头
            // httpPost.setHeader("ticket", ticket);

            // 设置连接参数
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setConnectionRequestTimeout(CLIENT_CONNECT_TIMEOUT)
                    .setSocketTimeout(CLIENT_CONNECT_TIMEOUT)
                    .build();
            httpPost.setConfig(requestConfig);

            // 请求正文
            StringEntity stringEntity = new StringEntity(requestData, StandardCharsets.UTF_8.displayName());
            //stringEntity.setContentType(ContentType.APPLICATION_XML.toString());
            stringEntity.setContentType("text/xml; charset=utf-8");
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
