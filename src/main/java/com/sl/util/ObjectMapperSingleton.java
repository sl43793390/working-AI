package com.sl.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapper 单例类
 * 用于全局共享一个 ObjectMapper 实例，避免重复创建开销
 */
public class ObjectMapperSingleton {

    // 私有静态实例，使用 volatile 确保多线程下的可见性
    private static volatile ObjectMapper mapper;

    // 私有构造函数，防止外部实例化
    private ObjectMapperSingleton() {
        // 防止通过反射创建实例
        if (mapper != null) {
            throw new RuntimeException("Use getInstance() method to get the instance of this class.");
        }
    }

    /**
     * 获取 ObjectMapper 实例（线程安全）
     * @return ObjectMapper 实例
     */
    public static ObjectMapper getInstance() {
        if (mapper == null) {
            synchronized (ObjectMapperSingleton.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                    // 可在此处添加自定义配置，例如：
                    // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }
        return mapper;
    }
}