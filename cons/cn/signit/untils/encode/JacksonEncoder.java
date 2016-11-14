/*
 * Copyright 2015 Async-IO.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.signit.untils.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
*Json编码工具
* @ClassName JacksonEncoder
* @author ZhangHongdong
* @date 2015年3月23日-下午1:20:40
* @version 1.0.0
*/
public class JacksonEncoder{
	private final static Logger LOG = LoggerFactory.getLogger(JacksonEncoder.class);
    private final static ObjectMapper MAPPER = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true)
																																    		.setSerializationInclusion(Include.NON_NULL)
																																			.setSerializationInclusion(Include.NON_EMPTY)
																																			;
    
    
    public static String encodeAsString(Object obj){
    	String ret = null;
    	try {
			ret = MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			LOG.info(e.getMessage());
		}
    	return ret;
    }

    public static byte[] encodeAsBytes(Object obj){
    	byte[] ret = null;
    	try {
			ret = MAPPER.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			LOG.info(e.getMessage());
		}
    	return ret;
    }
    
    public static void main(String[] args) {
		Test t = new Test("zhd", 24);
		System.out.println(encodeAsString(t));
	}
    
    public static class Test{
    	private String userName;
    	private int age;
    	
		public Test() {
			super();
		}

		
		
		public Test(String userName, int age) {
			super();
			this.userName = userName;
			this.age = age;
		}



		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
    }
}
