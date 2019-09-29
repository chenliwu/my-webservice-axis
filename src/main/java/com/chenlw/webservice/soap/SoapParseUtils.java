package com.chenlw.webservice.soap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SOAP XML报文解析
 *
 * @author chenlw
 * @date 2019/09/29
 */
public class SoapParseUtils {

    /**
     * 将SOAP响应报文转化成Map
     *
     * @param soap SOAP响应报文
     * @return Map
     * @throws DocumentException
     */
    public static Map<String,String> parse(String soap) throws DocumentException {
        //报文转成doc对象
        Document doc = DocumentHelper.parseText(soap);
        //获取根元素，准备递归解析这个XML树
        Element root = doc.getRootElement();
        Map<String, String> resultMap = new HashMap<>();
        getCode(root, resultMap);
        return resultMap;
    }

    private static void getCode(Element root, Map<String, String> resultMap) {
        if (root.elements() != null) {
            //如果当前跟节点有子节点，找到子节点
            List<Element> list = root.elements();
            for (Element e : list) {
                //遍历每个节点
                if (e.elements().size() > 0) {
                    //当前节点不为空的话，递归遍历子节点；
                    getCode(e, resultMap);
                }
                if (e.elements().size() == 0) {
                    //如果为叶子节点，那么直接把名字和值放入map
                    resultMap.put(e.getName(), e.getTextTrim());
                }
            }
        }
    }

}
