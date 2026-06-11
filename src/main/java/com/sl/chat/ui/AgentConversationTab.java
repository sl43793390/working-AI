package com.sl.chat.ui;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sl.chat.model.ChatServiceGeneral;
import com.sl.config.ModelConfig;
import com.sl.entity.*;
import com.sl.mapper.AgentMemoryMapper;
import com.sl.service.AgentService;
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
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Agent对话标签页组件
 * 用于展示AI Agent对话界面，支持多会话管理、消息发送与接收、历史记录加载等功能
 */
public class AgentConversationTab extends VerticalLayout {

    /** Agent名称标签 */
    private H3 agentNameLabel;
    /** 当前选中的Agent实体 */
    private UserAgent currentAgent;
    /** 当前登录用户 */
    private User currentUser;
    /** LangChain4j Agent服务实例，用于处理AI对话 */
    private UntypedAgent currentAgentService;

    // 会话相关组件
    /** 左侧会话列表容器 */
    private VerticalLayout sessionsList;
    /** 消息列表组件，用于显示聊天消息 */
    private MessageList messageList;
    /** 消息输入框组件 */
    private MessageInput messageInput;
    /** 可滚动的聊天区域容器 */
    private Scroller chatScroller;
    /** 所有会话的列表 */
    private final List<ChatSession> chatSessions;
    /** 当前正在操作的会话 */
    private ChatSession currentSession;
    /** AI处理中的进度指示器 */
    private ProgressBar processingIndicator;
    /** 历史消息保留数量，默认为10条 */
    private Integer defaultMaxMessages = 10;
    /** 删除会话按钮 */
    private Button deleteSessionButton;

    // 数据访问组件
    /** RAG服务，用于获取Agent记忆数据 */
    private final RagService ragService;
    /** Agent服务，用于获取Agent配置和工具 */
    private AgentService agentService;
    /** Agent记忆数据访问Mapper */
    private final AgentMemoryMapper agentMemoryMapper;
    /** 聊天内存提供者 */
    private ChatMemoryProvider chatMemoryProvider;
    // AI服务组件
    /** LangChain4j聊天模型 */
    private final ChatModel openAiChatModel;

    /**
     * 构造函数
     * 从Spring容器中获取必要的Bean实例，初始化当前用户并创建UI布局
     */
    public AgentConversationTab() {
        this.ragService = ModelConfig.appcationContext.getBean(RagService.class);
        this.agentMemoryMapper = ModelConfig.appcationContext.getBean(AgentMemoryMapper.class);
        this.openAiChatModel = ModelConfig.appcationContext.getBean(ChatModel.class);
        this.chatMemoryProvider = ModelConfig.appcationContext.getBean(ChatMemoryProvider.class);
        this.agentService = ModelConfig.appcationContext.getBean(AgentService.class);
        this.currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        chatSessions = new ArrayList<>();
        initLayout();
    }

    /**
     * 初始化UI布局
     * 创建并配置所有UI组件，包括会话列表、消息列表、消息输入框、进度条等
     */
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

    /**
     * 更新当前选中的Agent
     * 当用户选择一个Agent时调用，初始化Agent服务并加载历史会话
     * @param agent 用户选择的Agent实体，如果为null则清空会话列表
     */
    public void updateCurrentAgent(UserAgent agent) {
        this.currentAgent = agent;
        if (agent != null) {
            agentNameLabel.setText("当前Agent: " + agent.getNameAgent());
            // 创建Ai服务组件
            List<AgentTool> agentTool = agentService.getAgentTool(agent.getIdAgent());
            Object[] array = agentService.transferTool(agentTool).toArray();
            currentAgentService = AgenticServices.agentBuilder()
                    .chatModel(openAiChatModel)
                    .description(agent.getCdDesc())
                    .userMessage(agent.getSystemPrompt())
                    .chatMemoryProvider(chatMemoryProvider)
                    .inputKey(String.class,"input")
                    .tools(array)
                    .returnType(String.class) // String is the default return type for untyped agents
                    .build();
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

    /**
     * 加载用户的历史会话
     * 从数据库查询当前用户的所有会话记录，并转换为UI组件显示
     */
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

    /**
     * 创建新会话
     * 先保存当前会话到数据库，然后创建新的空会话并切换到该会话
     */
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

    /**
     * 切换到指定会话
     * 保存当前会话后切换到目标会话，并更新消息列表显示
     * @param session 要切换到的目标会话
     */
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

    /**
     * 移除所有会话按钮的成功主题样式
     * 用于在切换会话时清除之前选中按钮的高亮状态
     */
    private void removeSuccessButtonTheme(){
        sessionsList.getChildren().forEach(component -> {
            if (component instanceof Button button1) {
                button1.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
            }
        });
    }

    /**
     * 保存当前会话到数据库
     * 将当前会话的消息序列化为JSON并存储到AgentMemory表中
     */
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

    /**
     * 删除当前会话
     * 弹出确认对话框，用户确认后删除会话数据并更新UI
     */
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

    /**
     * 处理用户提交的消息
     * 当用户在输入框中提交消息时调用，调用AI Agent处理消息并返回响应
     * @param event 消息提交事件，包含用户输入的文本内容
     */
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
        if (!currentSession.getMessages().isEmpty()){
            buf.append("###历史对话信息：").append(System.lineSeparator());
        }
        // 只保留最近10条历史消息
        if (currentSession.getMessages().size() > defaultMaxMessages){
            List<MessageListItem> messageListItems = currentSession.getMessages().subList(currentSession.getMessages().size() - defaultMaxMessages, currentSession.getMessages().size());
            messageListItems.forEach(message -> buf.append(message.getUserName()).append(":").append(message.getText()).append(System.lineSeparator()));

        }else{
            currentSession.getMessages().forEach(message -> buf.append(message.getUserName()).append(message.getText()).append(System.lineSeparator()));
        }
        if (!currentSession.getMessages().isEmpty()){
            buf.append("###请结合历史对话回答用户问题（如果没有，请忽略）：").append(System.lineSeparator());
        }

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
            // 使用基本的ChatService处理用户消息
//            ChatServiceGeneral chatService = AiServices.builder(ChatServiceGeneral.class)
//                    .build();
//            String response = chatService.chat(currentSession.getId(), userMessageText);
            buf.append(userMessageText);
            Object response = currentAgentService.invoke(Map.of("input", buf.toString()));
            // 在UI线程中更新界面
            MessageListItem aiMessage = new MessageListItem(response.toString(),Instant.now(),"AI Assistant");
            // 添加AI消息
            currentSession.addMessage(aiMessage);
            // 更新消息列表
            messageList.setItems(currentSession.getMessages());
            // 隐藏处理中提示
            processingIndicator.setVisible(false);
            messageInput.setEnabled(true);
            scrollToBottom();
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
            scrollToBottom();
        }
    }

    /**
     * 更新会话按钮的文本
     * 根据会话名称更新左侧会话列表中对应按钮的显示文本
     * @param session 要更新按钮的会话对象
     */
    private void updateSessionButton(ChatSession session) {
        // 找到对应的会话按钮并更新其文本
        int sessionIndex = chatSessions.indexOf(session);
        if (sessionIndex >= 0 && sessionIndex < sessionsList.getComponentCount()) {
            Button sessionButton = (Button) sessionsList.getComponentAt(sessionIndex);
            sessionButton.setText(session.getName());
        }
    }

    /**
     * 滚动到消息列表底部
     * 使用JavaScript延迟滚动，确保DOM更新完成后再执行滚动操作
     */
    private void scrollToBottom() {
        // 延迟执行滚动以确保DOM已更新
        chatScroller.getElement().executeJs(
                "setTimeout(function() { this.scrollTop = this.scrollHeight; }, 100);");
    }

    /**
     * 创建标签页
     * 用于在TabSheet中创建此视图的标签页
     * @return 包含"Agent对话"文本的Tab对象
     */
    public Tab createTab() {
        return new Tab("Agent对话");
    }

    /**
     * 获取当前选中的Agent
     * @return 当前正在使用的UserAgent对象
     */
    public UserAgent getCurrentAgent() {
        return currentAgent;
    }

    /**
     * 表示一个聊天会话
     * 内部类，用于封装单个会话的名称、ID和消息列表
     */
    private static class ChatSession {
        /** 会话名称 */
        private String name;
        /** 会话唯一标识符 */
        private final String id;
        /** 会话中的消息列表 */
        private final List<MessageListItem> messages;

        /**
         * 构造函数
         * @param name 会话名称
         * @param id 会话唯一标识符
         */
        public ChatSession(String name, String id) {
            this.name = name;
            this.id = id;
            this.messages = new ArrayList<>();
        }

        /**
         * 获取会话名称
         * @return 会话名称
         */
        public String getName() {
            return name;
        }

        /**
         * 设置会话名称
         * @param name 新的会话名称
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取会话ID
         * @return 会话唯一标识符
         */
        public String getId() {
            return id;
        }

        /**
         * 获取消息列表的副本
         * @return 消息列表的副本，防止外部直接修改内部状态
         */
        public List<MessageListItem> getMessages() {
            return new ArrayList<>(messages); // 返回副本以防止外部修改
        }

        /**
         * 添加消息到会话
         * @param message 要添加的消息
         */
        public void addMessage(MessageListItem message) {
            messages.add(message);
        }

        /**
         * 设置会话的消息列表
         * @param messages 新的消息列表
         */
        public void setMessages(List<MessageListItem> messages) {
            this.messages.clear();
            this.messages.addAll(messages);
        }
    }

}