package com.sl.chat.ui;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sl.chat.ChatServiceGeneral;
import com.sl.chat.agent.MyReActAgent;
import com.sl.chat.tool.Tool;
import com.sl.chat.tool.ToolExecutor;
import com.sl.config.ModelConfig;
import com.sl.entity.AgentMemory;
import com.sl.entity.ChatMessage;
import com.sl.entity.User;
import com.sl.entity.UserAgent;
import com.sl.mapper.AgentMemoryMapper;
import com.sl.service.RagService;
import com.sl.util.ObjectMapperSingleton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class AgentConversationTab extends VerticalLayout {

    private H3 agentNameLabel;
    private UserAgent currentAgent;
    private User currentUser;

    // 会话相关组件
    private VerticalLayout sessionsList;
    private MessageList messageList;
    private MessageInput messageInput;
    private Scroller chatScroller;
    private final List<ChatSession> chatSessions;
    private ChatSession currentSession;
    private ProgressBar processingIndicator;
    private Integer defaultMaxMessages = 10;
    // 删除会话按钮
    private Button deleteSessionButton;

    // 数据访问组件
    private final RagService ragService;
    private final AgentMemoryMapper agentMemoryMapper;

    // AI服务组件
    private final ChatModel openAiChatModel;

    private MyReActAgent reActAgent;
    private ToolExecutor toolExecutor;

    public AgentConversationTab() {
        this.ragService = ModelConfig.appcationContext.getBean(RagService.class);
        this.agentMemoryMapper = ModelConfig.appcationContext.getBean(AgentMemoryMapper.class);
        this.openAiChatModel = ModelConfig.appcationContext.getBean(ChatModel.class);
        this.currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        chatSessions = new ArrayList<>();
        initLayout();
    }

    private void initLayout() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // 创建Agent名称显示
        agentNameLabel = new H3("请选择一个Agent开始对话");
        agentNameLabel.setVisible(true);
        add(agentNameLabel);

        // 创建会话列表区域
        sessionsList = new VerticalLayout();
        sessionsList.setWidth("100%");
        sessionsList.setSpacing(true);

        // 创建消息列表区域
        messageList = new MessageList();
        messageList.setMarkdown(true);
        messageList.setSizeFull();

        // 创建可滚动的聊天区域
        chatScroller = new Scroller(messageList);
        chatScroller.setSizeFull();
        chatScroller.addClassNames(LumoUtility.Background.CONTRAST_5);

        // 创建消息输入区域
        messageInput = new MessageInput();
        messageInput.setHeight("100px");
        messageInput.addSubmitListener(this::onMessageSubmit);
        messageInput.setWidthFull();

        // 创建AI处理中进度条组件
        processingIndicator = new ProgressBar();
        processingIndicator.setIndeterminate(true);
        processingIndicator.setVisible(false);

        // 创建删除会话按钮
        deleteSessionButton = new Button("删除当前会话");
        deleteSessionButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteSessionButton.getStyle().set("margin-left", "20px");
        deleteSessionButton.addClickListener(e -> deleteCurrentSession());

        // 主聊天区域布局
        VerticalLayout chatLayout = new VerticalLayout(chatScroller, processingIndicator,deleteSessionButton, messageInput);
        chatLayout.setSizeFull();
        chatLayout.setSpacing(true);
        chatLayout.setPadding(false);
        chatLayout.expand(chatScroller);

        // 创建新建会话按钮
        Button newSessionButton = new Button("新建会话");
        newSessionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newSessionButton.setWidthFull();
        newSessionButton.addClickListener(e -> {
            removeSuccessButtonTheme();
            createNewSession();
        });

        // 左侧会话列表区域
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setWidth("260px");
        leftPanel.add(newSessionButton, sessionsList);
        leftPanel.setSpacing(true);
        leftPanel.addClassName(LumoUtility.Background.CONTRAST_10);

        // 主布局
        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, chatLayout);
        mainLayout.setHeight("95%");
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(false);
        mainLayout.setPadding(false);
        mainLayout.setFlexGrow(1, chatLayout);
        // 设置主视图属性
        setSizeFull();
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        add(mainLayout);
        expand(mainLayout);
    }

    public void updateCurrentAgent(UserAgent agent) {
        this.currentAgent = agent;
        if (agent != null) {
            agentNameLabel.setText("当前Agent: " + agent.getNameAgent());

            // 初始化Agent相关组件
            initializeAgentComponents();

            // 加载历史会话
            loadUserSessions();

            // 如果没有历史会话，则创建第一个会话
            if (chatSessions.isEmpty()) {
                createNewSession();
            }
        } else {
            agentNameLabel.setText("请选择一个Agent开始对话");
            // 清空会话列表
            sessionsList.removeAll();
            chatSessions.clear();
            messageList.setItems(new ArrayList<>());
        }
    }

    private void initializeAgentComponents() {
        // 初始化ReActAgent
        List<Tool> availableTools = getAvailableToolsForAgent();
        if (!availableTools.isEmpty()) {
            toolExecutor = new ToolExecutor(availableTools);
            reActAgent = new MyReActAgent(openAiChatModel, availableTools);
        }
    }

    private List<Tool> getAvailableToolsForAgent() {
        // 获取当前Agent可用的工具
        List<Tool> tools = new ArrayList<>();
        // 这里应该根据currentAgent查询数据库获取工具列表
        // 简化实现，返回所有工具
        try {
            // 示例：添加天气工具
            Tool weatherTool = new com.sl.chat.tool.WeatherTool();
            tools.add(weatherTool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tools;
    }

    private void loadUserSessions() {
        if (currentAgent == null || currentUser == null) {
            return;
        }

        sessionsList.removeAll();
        chatSessions.clear();

        // 查询当前用户和当前Agent的所有聊天记录
        List<AgentMemory> chatContents = ragService.getAgentMemoryByUserId(currentUser.getUserId());

        ObjectMapperSingleton.getInstance().setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));

        int i = 0;
        // 遍历聊天记录，创建会话按钮
        for (AgentMemory content : chatContents) {
            try {
                // 解析JSON内容
                List<ChatMessage> chatMessages = ObjectMapperSingleton.getInstance().readValue(content.getContent(), new TypeReference<List<ChatMessage>>() {});

                // 创建会话
                ChatSession session = new ChatSession(content.getNameChat() == null? "历史会话":content.getNameChat(), content.getSessionId());

                // 转换消息格式
                List<MessageListItem> messageItems = new ArrayList<>();
                for (ChatMessage chatMessage : chatMessages) {
                    MessageListItem item = new MessageListItem(
                            chatMessage.getText(),
                            DateUtil.toInstant(chatMessage.getTime()),
                            chatMessage.getUserName()
                    );
                    messageItems.add(item);
                }
                session.setMessages(messageItems);

                // 设置会话名称为第一条消息的前几个字符
                if (content.getNameChat() != null){
                    session.setName(content.getNameChat());
                } else if (!messageItems.isEmpty()) {
                    String firstMessage = messageItems.getFirst().getText();
                    String sessionName = firstMessage.length() > 8 ? firstMessage.substring(0, 8) : firstMessage;
                    session.setName(sessionName);
                } else {
                    session.setName("空会话");
                }

                // 添加到会话列表
                chatSessions.add(session);

                // 创建会话按钮
                Button sessionButton = new Button(session.getName());
                sessionButton.setWidthFull();
                sessionButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                sessionButton.addClickListener(e -> {
                    removeSuccessButtonTheme();
                    sessionButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                    switchToSession(session);
                });
                if (i == 0){
                    sessionButton.click();
                    i++;
                }
                sessionsList.add(sessionButton);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void createNewSession() {
        // 保存当前会话到数据库
        if (currentSession != null && !currentSession.getMessages().isEmpty()) {
            saveCurrentSession();
        }

        // 清空当前会话区域内容
        messageList.setItems(new ArrayList<>());

        // 创建新的会话
        String sessionId = UUID.randomUUID().toString();
        ChatSession session = new ChatSession("新会话", sessionId);
        chatSessions.add(session);

        // 创建会话列表项
        Button sessionItem = new Button(session.getName());
        sessionItem.setWidthFull();
        sessionItem.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        sessionItem.addClickListener(e ->{
            removeSuccessButtonTheme();
            sessionItem.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            switchToSession(session);
        });
        sessionItem.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        sessionsList.add(sessionItem);

        // 切换到新会话
        switchToSession(session);
        deleteSessionButton.setEnabled(true);
    }

    private void switchToSession(ChatSession session) {
        // 保存当前会话到数据库
        if (currentSession != null && !currentSession.getMessages().isEmpty()) {
            saveCurrentSession();
        }
        currentSession = session;
        messageList.setItems(session.getMessages());
        deleteSessionButton.setEnabled(true);
        // 滚动到最新消息
        scrollToBottom();
    }

    private void removeSuccessButtonTheme(){
        sessionsList.getChildren().forEach(component -> {
            if (component instanceof Button button1) {
                button1.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
            }
        });
    }

    private void saveCurrentSession() {
        if (currentSession == null || currentUser == null) {
            return;
        }

        try {
            ObjectMapperSingleton.getInstance().setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));

            List<ChatMessage> chatMessages = new ArrayList<>();

            // 将MessageListItem转换为ChatMessage
            for (MessageListItem item : currentSession.getMessages()) {
                if (item.getUserName().startsWith("AI")){
                    ChatMessage chatMessage = new ChatMessage(item.getText(), DateUtil.toLocalDateTime(item.getTime()), "AI");
                    chatMessages.add(chatMessage);
                }else {
                    ChatMessage chatMessage = new ChatMessage(item.getText(), DateUtil.toLocalDateTime(item.getTime()), "user");
                    chatMessages.add(chatMessage);
                }
            }

            String jsonChatMessage = ObjectMapperSingleton.getInstance().writeValueAsString(chatMessages);
            AgentMemory contentChat = new AgentMemory();
            contentChat.setUserId(currentUser.getUserId());
            contentChat.setSessionId(currentSession.getId());
            contentChat.setNameChat(currentSession.getName());
            contentChat.setContent(jsonChatMessage);

            // 检查会话是否已存在
            AgentMemory existingContent = agentMemoryMapper.selectByPrimaryKey(currentUser.getUserId(), currentSession.getId());
            if (existingContent != null) {
                // 更新现有会话
                agentMemoryMapper.updateByPrimaryKey(contentChat);
            } else {
                // 插入新会话
                agentMemoryMapper.insert(contentChat);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void deleteCurrentSession() {
        if (currentSession == null || currentUser == null) {
            return;
        }

        // 弹出确认对话框
        Dialog confirmDialog = new Dialog();
        confirmDialog.setWidth("25%");
        confirmDialog.setCloseOnEsc(false);
        confirmDialog.setCloseOnOutsideClick(false);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        Span message = new Span("确定要删除当前会话吗？此操作不可撤销。");
        Button confirmButton = new Button("确定", event -> {
            // 删除会话数据
            try {
                // 删除Agent记忆
                agentMemoryMapper.deleteByPrimaryKey(currentUser.getUserId(), currentSession.getId());

                // 从UI中移除会话
                int sessionIndex = chatSessions.indexOf(currentSession);
                if (sessionIndex >= 0) {
                    chatSessions.remove(sessionIndex);
                    sessionsList.remove(sessionsList.getComponentAt(sessionIndex));
                }

                // 清空当前会话
                currentSession = null;
                messageList.setItems(new ArrayList<>());
                deleteSessionButton.setEnabled(false);

                // 如果还有其他会话，切换到第一个会话
                if (!chatSessions.isEmpty()) {
                    switchToSession(chatSessions.getFirst());
                } else {
                    // 如果没有其他会话，创建一个新会话
                    createNewSession();
                }

                confirmDialog.close();
            } catch (Exception e) {
                e.printStackTrace();
                // 处理删除失败的情况
                Span errorMessage = new Span("删除会话失败: " + e.getMessage());
                dialogLayout.add(errorMessage);
            }
        });

        Button cancelButton = new Button("取消", event -> confirmDialog.close());
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setSpacing(true);

        dialogLayout.add(message, buttonLayout);
        confirmDialog.add(dialogLayout);
        confirmDialog.open();
        confirmDialog.setWidth("50%");
    }

    private void onMessageSubmit(MessageInput.SubmitEvent event) {
        if (currentSession == null || currentAgent == null) {
            return;
        }

        String userMessageText = event.getValue();
        if (userMessageText.trim().isEmpty()) {
            return;
        }

        // 更新会话名称为用户消息的前8个字
        if (currentSession.getMessages().isEmpty()) {
            String sessionName = userMessageText.length() > 8 ? userMessageText.substring(0, 8) : userMessageText;
            currentSession.setName(sessionName);
            updateSessionButton(currentSession);
        }
        // 将当前会话中的所有消息都添加到userMessageText中，提交到agent中
        StringBuffer buf = new StringBuffer();
        buf.append("###历史对话信息：").append(System.lineSeparator());
        // 只保留最近10条历史消息
        if (currentSession.getMessages().size() > defaultMaxMessages){
            List<MessageListItem> messageListItems = currentSession.getMessages().subList(currentSession.getMessages().size() - defaultMaxMessages, currentSession.getMessages().size());
            messageListItems.forEach(message -> buf.append(message.getUserName()).append(message.getText()).append(System.lineSeparator()));

        }else{
            currentSession.getMessages().forEach(message -> buf.append(message.getUserName()).append(message.getText()).append(System.lineSeparator()));
        }
        buf.append("###请结合历史对话回答用户问题：").append(System.lineSeparator());

        // 添加用户消息
        MessageListItem userMessage = new MessageListItem(
                userMessageText,
                Instant.now(),
                currentUser != null ? currentUser.getUserId() : "User"
        );
        currentSession.addMessage(userMessage);

        // 显示AI处理中进度条
        processingIndicator.setVisible(true);
        messageInput.setEnabled(false);

        // 在后台线程中处理AI响应
        try {
            String response;
            if (reActAgent != null) {
                // 使用ReActAgent处理用户消息
                response = reActAgent.run(buf +userMessageText);
            } else {
                // 使用基本的ChatService处理用户消息
                ChatServiceGeneral chatService = AiServices.builder(ChatServiceGeneral.class)
                        .build();
                response = chatService.chat(currentSession.getId(), userMessageText);
            }

            // 在UI线程中更新界面
            MessageListItem aiMessage = new MessageListItem(
                    response,
                    Instant.now(),
                    "AI Assistant"
            );
            // 添加AI消息
            currentSession.addMessage(aiMessage);
            // 更新消息列表
            messageList.setItems(currentSession.getMessages());
            // 隐藏处理中提示
            processingIndicator.setVisible(false);
            messageInput.setEnabled(true);
            // 滚动到底部
            scrollToBottom();
            // 保存会话
            saveCurrentSession();
        } catch (Exception e) {
            e.printStackTrace();
            // 确保在异常情况下也能恢复UI状态
            // 添加错误消息
            MessageListItem errorMessage = new MessageListItem(
                    "处理消息时发生错误: " + e.getMessage(),
                    Instant.now(),
                    "System"
            );
            currentSession.addMessage(errorMessage);
            messageList.setItems(currentSession.getMessages());

            // 隐藏处理中提示
            processingIndicator.setVisible(false);
            messageInput.setEnabled(true);

            // 滚动到底部
            scrollToBottom();
        }
    }

    private void updateSessionButton(ChatSession session) {
        // 找到对应的会话按钮并更新其文本
        int sessionIndex = chatSessions.indexOf(session);
        if (sessionIndex >= 0 && sessionIndex < sessionsList.getComponentCount()) {
            Button sessionButton = (Button) sessionsList.getComponentAt(sessionIndex);
            sessionButton.setText(session.getName());
        }
    }

    private void scrollToBottom() {
        // 延迟执行滚动以确保DOM已更新
        chatScroller.getElement().executeJs(
                "setTimeout(function() { this.scrollTop = this.scrollHeight; }, 100);");
    }

    public Tab createTab() {
        return new Tab("Agent对话");
    }

    public UserAgent getCurrentAgent() {
        return currentAgent;
    }

    /**
     * 表示一个聊天会话
     */
    private static class ChatSession {
        private String name;
        private final String id;
        private final List<MessageListItem> messages;

        public ChatSession(String name, String id) {
            this.name = name;
            this.id = id;
            this.messages = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public List<MessageListItem> getMessages() {
            return new ArrayList<>(messages); // 返回副本以防止外部修改
        }

        public void addMessage(MessageListItem message) {
            messages.add(message);
        }

        public void setMessages(List<MessageListItem> messages) {
            this.messages.clear();
            this.messages.addAll(messages);
        }
    }

}