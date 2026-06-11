package com.sl.chat.tool;

import cn.hutool.http.HttpUtil;
import dev.langchain4j.agent.tool.Tool;

import java.util.Map;

@MyTool(name = "HttpTool", description = "Http 远程调用工具类")
public class HttpTool {


    /**
     * 获取 Http 响应结果
     *请求方式get
     * @param url
     * @return
     */
    @Tool(name = "get请求")
    public static String get(String url) {
        return HttpUtil.get(url,5);
    }

    /**
     * 获取 Http 响应结果
     *请求方式get
     * @param url
     * @param paramMap
     * @return
     */
    @Tool(name = "get请求带参数")
    public static String get(String url, Map<String, Object> paramMap) {
        return HttpUtil.get(url, paramMap,5);
    }

    /**
     * 获取 Http 响应结果
     *请求方式post
     * @param url
     * @return
     */
    @Tool(name = "post请求带参数")
    public static String post(String url,Map<String, Object> paramMap) {
        return HttpUtil.post(url,paramMap,5);
    }

    /**
     * 获取 Http 响应结果
     *请求方式post
     * @param url
     * @return
     */
    @Tool(name = "post请求")
    public static String post(String url,String body) {
        return HttpUtil.post(url,body,5);
    }


}
