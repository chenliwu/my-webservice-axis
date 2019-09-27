package com.chenlw.webservice;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.InputStream;

/**
 * JackSon  xml转化工具类
 *
 * @author chenlw 2019/08/09
 */
public class JackSonXmUtils {

    public static XmlMapper xmlMapper = new XmlMapper();

    /**
     * 对象转化成XML
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String beanToXml(Object object) throws Exception {
        // 格式化
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 设置xml头
        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
        // 字段为null，自动忽略，不再序列化
        // xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // XML标签名:使用骆驼命名的属性名，
        // xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        // 设置转换模式
        // xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        String xml = xmlMapper.writeValueAsString(object);
        return xml;
    }


    /**
     * xml转化成对象
     *
     * @param xmlInputStream
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T xmlToBean(InputStream xmlInputStream, Class<T> cls) throws Exception {
        T obj = xmlMapper.readValue(xmlInputStream, cls);
        return obj;
    }

    /**
     * xml转化成对象
     *
     * @param xml
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T xmlToBean(String xml, Class<T> cls) throws Exception {
        T obj = xmlMapper.readValue(xml, cls);
        return obj;
    }

}
