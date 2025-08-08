package com.sl.chat.agent;

import com.sl.chat.memory.InMemoryMemory;
import com.sl.chat.tool.Tool;
import com.sl.chat.tool.ToolExecutor;
import dev.langchain4j.model.chat.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReActAgent {
    private static final Logger logger = LoggerFactory.getLogger(ReActAgent.class);

    private ChatModel llm;
    private ToolExecutor toolExecutor;
    private List<Tool> tools;
    private InMemoryMemory memory;
    private int maxIterations = 5;

    public ReActAgent(ChatModel llm, List<Tool> tools) {
        this.llm = llm;
        this.tools = tools;
        this.toolExecutor = new ToolExecutor(tools);
        this.memory = new InMemoryMemory(10); // 初始化记忆
    }

    public String run(String input) {
        logger.info("Agent started with input: {}", input);
        memory.add("user", input);

        StringBuilder fullPrompt = new StringBuilder();
        buildToolPrompt(fullPrompt);

        fullPrompt.append("Begin!\n")
                  .append("Question: ").append(input).append("\n");

        for (int i = 0; i < maxIterations; i++) {
            logger.debug("Starting iteration {}/{}", i + 1, maxIterations);
            String response = llm.chat(fullPrompt.toString());
            System.out.println("LLM Response: " + response);

            // 检查是否有工具调用
            if (hasToolCall(response)) {
                ToolCall toolCall = extractToolCall(response);
                if (toolCall != null) {
                    String toolName = toolCall.getFunction().getName();
                    String args = toolCall.getFunction().getArguments();

                    logger.info("Tool selected: {} with args: {}", toolName, args);

                    String observation = toolExecutor.execute(toolCall);
                    logger.info("Tool executed. Observation: {}", observation);

                    // 记录到记忆
                    memory.add("assistant", "Action: " + toolName + ", Input: " + args);
                    memory.add("system", "Observation: " + observation);

                    // 更新 prompt
                    fullPrompt.append("Thought: I need to use a tool.\n")
                              .append("Action: ").append(toolName).append("\n")
                              .append("Action Input: ").append(args).append("\n")
                              .append("Observation: ").append(observation).append("\n");
                } else {
                    String finalAnswer = response;
                    logger.info("Final answer generated: {}", finalAnswer);
                    memory.add("assistant", "Final Answer: " + finalAnswer);
                    return finalAnswer;
                }
            } else {
                String finalAnswer = response;
                logger.info("Final answer generated: {}", finalAnswer);
                memory.add("assistant", "Final Answer: " + finalAnswer);
                return finalAnswer;
            }
        }

        String fallback = "I couldn't find a solution within " + maxIterations + " steps.";
        logger.warn("Max iterations reached. Returning fallback.");
        memory.add("assistant", fallback);
        return fallback;
    }

    /**
     * 检查响应中是否包含工具调用
     * @param response 模型响应
     * @return 如果包含工具调用返回true，否则返回false
     */
    private boolean hasToolCall(String response) {
        // 检查响应中是否包含Action关键词
        return response.contains("Action:") && response.contains("Action Input:");
    }

    /**
     * 从模型响应中提取工具调用信息
     * @param response 模型响应
     * @return ToolCall对象，如果未找到则返回null
     */
    private ToolCall extractToolCall(String response) {
        try {
            // 使用正则表达式提取Action和Action Input
            Pattern actionPattern = Pattern.compile("Action:\\s*(\\w+)");
            Pattern inputPattern = Pattern.compile("Action Input:\\s*(\\{.*\\})");
            
            Matcher actionMatcher = actionPattern.matcher(response);
            Matcher inputMatcher = inputPattern.matcher(response);
            
            if (actionMatcher.find() && inputMatcher.find()) {
                String toolName = actionMatcher.group(1);
                String arguments = inputMatcher.group(1);
                
                // 创建ToolCall对象
                ToolCall toolCall = new ToolCall();
                toolCall.setType("function");
                FunctionCall functionCall = new FunctionCall(toolName, arguments);
                toolCall.setFunction(functionCall);
                
                return toolCall;
            }
        } catch (Exception e) {
            logger.error("Error extracting tool call from response: {}", e.getMessage());
        }
        return null;
    }

    private void buildToolPrompt(StringBuilder prompt) {
        prompt.append("Answer the following question. You can use tools.\n");
        prompt.append("Available tools:\n");

        for (Tool tool : tools) {
            prompt.append("- ").append(tool.getName())
                  .append(": ").append(tool.getDescription())
                  .append("\n  Parameters: ").append(tool.getParametersSchema())
                  .append("\n");
        }

        prompt.append("\nUse the following format:\n")
              .append("Thought: you should always think about what to do\n")
              .append("Action: the action to take, should be one of [")
              .append(String.join(", ", tools.stream().map(Tool::getName).toArray(String[]::new)))
              .append("]\n")
              .append("Action Input: the input to the action in JSON format\n")
              .append("Observation: the result of the action\n")
              .append("... (Thought/Action/Observation can repeat)\n")
              .append("Thought: I now know the final answer\n")
              .append("Final Answer: the final answer to the original input question\n\n");
    }

    // 获取记忆内容（可用于调试或上下文增强）
    public InMemoryMemory getMemory() {
        return memory;
    }
}