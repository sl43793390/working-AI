package com.sl.chat.ui;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.base.ui.component.ViewToolbar;
import com.sl.chat.ChatServiceGeneral;
import com.sl.config.ModelConfig;
import com.sl.entity.ChatContent;
import com.sl.entity.ChatMessage;
import com.sl.entity.KnowledgeBase;
import com.sl.entity.User;
import com.sl.mapper.ChatContentMapper;
import com.sl.service.RagService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Route("chat")
@PageTitle("AI Chat")
@Menu(order = 0, icon = "vaadin:chat", title = "AI聊天")
@PermitAll
public class ChatView extends Main implements BeforeEnterObserver {

    private final VerticalLayout sessionsList;
    private final MessageList messageList;
    private final MessageInput messageInput;
    private final Scroller chatScroller;
    private final List<ChatSession> chatSessions;
    private final RagService ragService;
    private final ComboBox<KnowledgeBase> knowledgeBaseComboBox;
    private ChatSession currentSession;

    // 添加文件上传组件
    private final Upload upload;
    private User currentUser;
    // 添加AI处理中的进度条组件
    private ProgressBar processingIndicator;
    private ChatServiceGeneral chatService;
    private EmbeddingModel embeddingModel;

    @Autowired
    public ChatView(ChatServiceGeneral chatService, RagService ragService, EmbeddingModel embeddingModel) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
        }
        this.ragService = ragService;
        this.chatService = chatService;
        this.embeddingModel = embeddingModel;
        // 创建会话列表区域
        sessionsList = new VerticalLayout();
        sessionsList.addClassNames(LumoUtility.Padding.SMALL);
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

        // 创建文件上传组件
        upload = new Upload();
        upload.setAcceptedFileTypes("*/*");
        upload.setMaxFiles(1);
        upload.setMaxFileSize(1024 * 1024 * 10);
        upload.setHeight("65px");
        upload.setDropLabel(new Span("上传文件"));

        // 创建消息输入区域
        messageInput = new MessageInput();
        messageInput.setHeight("100px");
        messageInput.addSubmitListener(this::onMessageSubmit);
        messageInput.setWidthFull();

        // 创建AI处理中进度条组件
        processingIndicator = new ProgressBar();
        processingIndicator.setIndeterminate(true);
        processingIndicator.setVisible(false);

        // 创建左侧会话区域
        VerticalLayout leftPanel = new VerticalLayout();
        leftPanel.setWidth("260px");
        leftPanel.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.SMALL);
        leftPanel.addClassName(LumoUtility.Background.CONTRAST_10);

//        上传文件按钮、选择知识库、删除会话按钮
        HorizontalLayout uploadLayout = new HorizontalLayout();
        uploadLayout.add(upload);
        knowledgeBaseComboBox = new ComboBox<>("选择知识库");
        List<KnowledgeBase> basesByUserId = ragService.getKnowledgeBasesByUserId(currentUser.getUserId());
        knowledgeBaseComboBox.setItems(basesByUserId);
        knowledgeBaseComboBox.setItemLabelGenerator(KnowledgeBase::getNameBase);
        uploadLayout.add(knowledgeBaseComboBox);
        
        Button deleteSessionButton = new Button("删除会话");
        deleteSessionButton.getStyle().set("margin-top", "30px");
        deleteSessionButton.addClickListener(e -> openDeleteSessionDialog());
        uploadLayout.add(deleteSessionButton);

        // 创建右侧聊天区域
        VerticalLayout rightPanel = new VerticalLayout(chatScroller, processingIndicator,uploadLayout, messageInput);
        rightPanel.setSizeFull();
        rightPanel.setSpacing(true);
        rightPanel.setPadding(false);

        // 创建新建会话按钮
        Button newSessionButton = new Button("New Chat");
        newSessionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newSessionButton.setWidthFull();
        newSessionButton.addClickListener(e -> {
            removeSuccessButtonTheme();
            createNewSession();
        });
        leftPanel.add(newSessionButton);

        // 添加会话列表
        leftPanel.add(sessionsList);

        // 创建主布局
        HorizontalLayout mainLayout = new HorizontalLayout(leftPanel, rightPanel);
        mainLayout.setHeight("95%");
        mainLayout.setSpacing(false);
        mainLayout.setPadding(false);
        mainLayout.setFlexGrow(1, rightPanel);

        // 初始化会话数据
        chatSessions = new ArrayList<>();
        
        // 加载用户的历史会话
        loadUserSessions();
        
        // 如果没有历史会话，则创建第一个会话
        if (chatSessions.isEmpty()) {
            createNewSession();
        }

        // 设置主视图属性
        setSizeFull();
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        add(new ViewToolbar("AI Chat"));
        add(mainLayout);
    }

    private void loadUserSessions() {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
            return;
        }
        sessionsList.removeAll();
        // 查询当前用户的所有聊天记录
        List<ChatContent> chatContents = ragService.getChatContentByUserId(currentUser.getUserId());
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));
        int i = 0;
        // 遍历聊天记录，创建会话按钮
        for (ChatContent content : chatContents) {
            try {
                // 解析JSON内容
                List<ChatMessage> chatMessages = objectMapper.readValue(content.getContent(), new TypeReference<List<ChatMessage>>() {});
                
                // 创建会话
                ChatSession session = new ChatSession(content.getNameChat() == null? "history session":content.getNameChat(), content.getSessionId());
                
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
                }else if (!messageItems.isEmpty()) {
                    String firstMessage = messageItems.get(0).getText();
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
                    sessionButton.addClassNames(LumoUtility.Background.CONTRAST_20);
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

    private void openDeleteSessionDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("300px");
        dialog.setHeight("150px");
        dialog.setModal(true);
        dialog.setHeaderTitle("删除会话");
        Button confirmBtn = new Button("确定", e -> {
            // 删除会话
            if (currentSession != null) {
                ragService.deleteChatContent(currentUser.getUserId(), currentSession.getId());
                //重新加载会话
                loadUserSessions();
                dialog.close();
                Notification.show("会话删除成功").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });
        Button cancelButton = new Button("取消", e -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmBtn, cancelButton);
        buttonLayout.setSpacing(true);
        dialog.add(buttonLayout);
        dialog.open();
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
        ChatSession session = new ChatSession("New Session", sessionId);
        chatSessions.add(session);

        // 创建会话列表项
        Button sessionItem = new Button(session.getName());
        sessionItem.setWidthFull();
        sessionItem.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        sessionItem.addClickListener(e ->{
            removeSuccessButtonTheme();
            sessionItem.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            sessionItem.addClassNames(LumoUtility.Background.CONTRAST_20);
            switchToSession(session);
        });
        sessionItem.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        sessionItem.addClassNames(LumoUtility.Background.CONTRAST_20);
        sessionsList.add(sessionItem);

        // 切换到新会话
        switchToSession( session);
    }

    private void switchToSession(ChatSession session) {
        // 保存当前会话到数据库
        if (currentSession != null && !currentSession.getMessages().isEmpty()) {
            saveCurrentSession();
        }
        currentSession = session;
        messageList.setItems(session.getMessages());
        // 滚动到最新消息
        scrollToBottom();
    }

    /**
     * 提交消息
     * @param event
     */
    private void onMessageSubmit(MessageInput.SubmitEvent event) {
        if (currentSession == null) {
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
            
            // 更新会话按钮显示文本
            updateSessionButton(currentSession);
        }

        // 添加用户消息
        MessageListItem userMessage = new MessageListItem(
                userMessageText,
                Instant.now(),
                currentUser.getUserId()
        );
        currentSession.addMessage(userMessage);

        // 显示AI处理中进度条
        processingIndicator.setVisible(true);
        messageInput.setEnabled(false);
        //判断用户是否勾选知识库，如果勾选则添加使用embedding模型嵌入用户输入内容，使用embeddingStore查询相似内容，将返回相似结果和用户问题一并提交给大模型
        if (knowledgeBaseComboBox.getValue() != null){
            Embedding queryEmbedding = embeddingModel.embed(userMessageText.trim()).content();
            EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                    .queryEmbedding(queryEmbedding)
                    .maxResults(3)
                    .build();
            KnowledgeBase knowledgeBase = knowledgeBaseComboBox.getValue();
            List<EmbeddingMatch<TextSegment>> matches = ModelConfig.milvusEmbeddingStore(knowledgeBase.getNameCollection(),1024).search(embeddingSearchRequest).matches();
            EmbeddingMatch<TextSegment> embeddingMatch = matches.get(0);

            StringBuilder userInputText =new StringBuilder();
            userInputText.append("根据以下内容回答用户问题：,如果可以找到答案就尽量参考引文回答问题，如果无法找到答案就回答：当前问题未在知识库中找到答案。");
            userInputText.append("用户问题：\n");
            userInputText.append(userMessageText);
            userInputText.append("知识库内容：\n");
            userInputText.append(embeddingMatch.embedded().text());
            String response = chatService.chat(currentSession.getId(), userInputText.toString());

            MessageListItem aiMessage = new MessageListItem(
                    response,
                    Instant.now(),
                    "AI Assistant"
            );

            // 在UI线程中更新界面
//                getUI().ifPresent(ui -> ui.access(() -> {
            // 移除进度条并添加AI消息
            currentSession.addMessage(aiMessage);

            // 更新消息列表
            messageList.setItems(currentSession.getMessages());

            // 隐藏处理中提示
            processingIndicator.setVisible(false);
            messageInput.setEnabled(true);

            // 滚动到底部
            scrollToBottom();
            return;
//            System.out.println(embeddingMatch.score()); // 0.8144287765026093
//            System.out.println(embeddingMatch.embedded().text()); // I like football.
        }
        
        // 用户未选择知识库
            try {
                // 模拟AI处理时间（实际应用中这里会调用OpenAIChatModel.chat()）
                String response = chatService.chat(currentSession.getId(), userMessageText);

                MessageListItem aiMessage = new MessageListItem(
                        response,
                        Instant.now(),
                        "AI Assistant"
                );
                
                // 在UI线程中更新界面
//                getUI().ifPresent(ui -> ui.access(() -> {
                    // 移除进度条并添加AI消息
                    currentSession.addMessage(aiMessage);
                    
                    // 更新消息列表
                    messageList.setItems(currentSession.getMessages());
                    
                    // 隐藏处理中提示
                    processingIndicator.setVisible(false);
                    messageInput.setEnabled(true);
                    
                    // 滚动到底部
                    scrollToBottom();
//                }));
            } catch (Exception e) {
                e.printStackTrace();
                // 确保在异常情况下也能恢复UI状态
                getUI().ifPresent(ui -> ui.access(() -> {
                    // 移除进度条
                    processingIndicator.setVisible(false);
                    messageInput.setEnabled(true);
                }));
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
    private void removeSuccessButtonTheme(){
        sessionsList.getChildren().forEach(component -> {
            if (component instanceof Button button1) {
                button1.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
                button1.removeClassName(LumoUtility.Background.CONTRAST_20);
            }
        });
    }

    private void saveCurrentSession() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")));

            List<ChatMessage> chatMessages = new ArrayList<>();
            
            // 将MessageListItem转换为ChatMessage
            for (MessageListItem item : currentSession.getMessages()) {
                ChatMessage chatMessage = new ChatMessage(item.getText(), DateUtil.toLocalDateTime(item.getTime()), item.getUserName());
                chatMessages.add(chatMessage);
            }
            String jsonChatMessage = objectMapper.writeValueAsString(chatMessages);
            ChatContent contentChat = new ChatContent();
            contentChat.setUserId(currentUser.getUserId());
            contentChat.setSessionId(currentSession.getId());
            contentChat.setNameChat(currentSession.getName());
            contentChat.setContent(jsonChatMessage);

            QueryWrapper<ChatContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("session_id", contentChat.getSessionId());
            ragService.deleteChatContent(currentUser.getUserId(),contentChat.getSessionId());
            ragService.insertChatContent(contentChat);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom() {
        // 延迟执行滚动以确保DOM已更新
        chatScroller.getElement().executeJs(
                "setTimeout(function() { this.scrollTop = this.scrollHeight; }, 100);");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        User user1 = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == user1){
            UI.getCurrent().navigate("login");
        }
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