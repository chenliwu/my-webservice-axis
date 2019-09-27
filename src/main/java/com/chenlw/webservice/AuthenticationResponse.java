package com.chenlw.webservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


/*
<ns2:authenticationResponse xmlns:ns2="http://auth.webservice.hy.com/">
	<return>
		<code>0</code>
		<data xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:string">1</data>
		<msg>成功</msg>
	</return>
</ns2:authenticationResponse>

 */

/**
 * @author chenlw
 * @date 2019/09/27
 */
@Data
@JacksonXmlRootElement(localName = "ns2:authenticationResponse")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationResponse {

    @JacksonXmlProperty(localName = "return")
    private Return returnNode;

    @Data
    public static class Return {

        private Integer code;
        private String data;
        private String msg;
    }

}
