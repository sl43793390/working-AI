package com.sl.chat.memory;

import java.util.List;

public interface Memory {

    /**
     * 添加一条记忆
     */
    void add(String role, String content);

    /**
     * 获取所有记忆（按时间顺序）
     */
    List<MemoryMessage> getMessages();

    /**
     * 获取格式化后的记忆字符串（可直接拼入 prompt）
     */
    String format();

    /**
     * 清除所有记忆
     */
    void clear();

    /**
     * 设置最大保留的消息条数
     */
    void setMaxMessages(int maxMessages);

    /**
     * 获取当前记忆条数
     */
    int size();

    /**
     * 关闭资源（如数据库连接）
     */
    default void close() {
        // 默认空实现
    }
}