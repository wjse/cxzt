package com.k66.cxzt.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class HttpSender {

    public static JSONObject send(HttpMethod method){
        method.addRequestHeader("Content-Type" , "application/json");
        HttpClient client = new HttpClient();
        BufferedReader reader = null;
        try{
            client.executeMethod(method);
            InputStream stream = method.getResponseBodyAsStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String tmp;
            StringBuffer buffer = new StringBuffer();
            while((tmp = reader.readLine()) != null){
                buffer.append(tmp);
            }
            return JSONObject.parseObject(buffer.toString());
        }catch (IOException e){
            log.error("Connection error." , e);
            throw new RuntimeException(e);
        }finally {
            if(null != reader){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Close the BufferedReader error." , e);
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
