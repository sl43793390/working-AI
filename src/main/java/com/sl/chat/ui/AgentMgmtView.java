package com.sl.chat.ui;

import cn.hutool.core.util.IdUtil;
import com.sl.base.ui.component.ViewToolbar;
import com.sl.entity.User;
import com.sl.entity.UserAgent;
import com.sl.entity.AgentTool;
import com.sl.mapper.UserAgentMapper;
import com.sl.mapper.AgentToolMapper;
import com.sl.chat.tool.Tool;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.Resource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Route("agent-mgmt")
@PageTitle("Agent Management")
@Menu(order = 3, icon = "vaadin:medal", title = "Agent管理")
@PermitAll
public class AgentMgmtView extends Main {

    private final Tab agentChatTab;
    private UserAgentMapper userAgentMapper;
    
    private AgentToolMapper agentToolMapper;
    
    private User currentUser;

    // Agent管理标签页组件
    private TextField searchField;
    private Button searchBtn;
    private Button createAgentBtn;
    private Grid<UserAgent> agentGrid;
    private ListDataProvider<UserAgent> agentDataProvider;

    // Agent对话标签页组件
    private AgentConversationTab agentConversationTab;
    private TabSheet tabSheet;

    public AgentMgmtView(UserAgentMapper userAgentMapper,AgentToolMapper agentToolMapper) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser) {
            UI.getCurrent().navigate("login");
        }
        this.userAgentMapper = userAgentMapper;
        this.agentToolMapper = agentToolMapper;
        // 创建标签页
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        // 创建Agent管理标签页
        VerticalLayout agentMgmtLayout = createAgentMgmtLayout();
        Tab agentMgmtTab = new Tab("Agent管理");
        tabSheet.add(agentMgmtTab, agentMgmtLayout);

        // 创建Agent对话标签页
        agentConversationTab = new AgentConversationTab();
        agentChatTab = agentConversationTab.createTab();
        tabSheet.add(agentChatTab, agentConversationTab);

        // 设置主布局
        setSizeFull();
        addClassNames("agent-mgmt-view");
        add(new ViewToolbar("Agent管理"));
        add(tabSheet);

        // 加载数据
        refreshData();
    }

    private VerticalLayout createAgentMgmtLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);

        // 创建搜索区域
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setWidthFull();
        searchLayout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.END);

        searchField = new TextField("搜索Agent");
        searchField.setPlaceholder("输入Agent名称");
        searchField.setWidth("300px");

        searchBtn = new Button("搜索");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> searchData());

        createAgentBtn = new Button("创建Agent");
        createAgentBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createAgentBtn.addClickListener(e -> openCreateAgentDialog());

        searchLayout.add(searchField, searchBtn, createAgentBtn);
        searchLayout.expand(searchField);

        // 创建Agent表格
        agentGrid = new Grid<>(UserAgent.class, false);
        agentGrid.setSizeFull();
        agentGrid.addComponentColumn(agent -> {
            Button agentLink = new Button(agent.getNameAgent());
            agentLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            agentLink.getStyle().set("padding", "0px");
            agentLink.getStyle().set("margin", "0px");
            agentLink.addClickListener(e -> {
                // 切换到对话标签页并更新当前Agent
                agentConversationTab.updateCurrentAgent(agent);
                tabSheet.setSelectedTab(agentChatTab);
            });
            return agentLink;
        }).setHeader("Agent名称").setAutoWidth(true);
        agentGrid.addColumn(UserAgent::getCdDesc).setHeader("描述").setAutoWidth(true);
        agentGrid.addComponentColumn(agent -> {
            Button editBtn = new Button("编辑");
            editBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editBtn.addClickListener(e -> openEditAgentDialog(agent));
            return editBtn;
        }).setHeader("操作").setAutoWidth(true);

        agentGrid.addComponentColumn(agent -> {
            Button deleteBtn = new Button("删除");
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> confirmDeleteAgent(agent));
            return deleteBtn;
        }).setHeader("操作").setAutoWidth(true);

        layout.add(searchLayout, agentGrid);
        layout.expand(agentGrid);

        return layout;
    }

    private void refreshData() {
        List<UserAgent> agents = userAgentMapper.selectByExample(null);
        agentDataProvider = new ListDataProvider<>(agents);
        agentGrid.setItems(agentDataProvider);
    }

    private void searchData() {
        String keyword = searchField.getValue();
        if (keyword == null || keyword.trim().isEmpty()) {
            refreshData();
            return;
        }

        // 根据关键字搜索Agent
        List<UserAgent> filteredAgents = agentDataProvider.getItems()
                .stream()
                .filter(agent -> agent.getNameAgent().contains(keyword) || 
                               (agent.getCdDesc() != null && agent.getCdDesc().contains(keyword)))
                .collect(Collectors.toList());

        agentDataProvider = new ListDataProvider<>(filteredAgents);
        agentGrid.setItems(agentDataProvider);
    }

    private void openCreateAgentDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setWidth("50%");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        TextField nameField = new TextField("Agent名称");
        nameField.setWidthFull();

        TextArea descArea = new TextArea("描述");
        descArea.setWidthFull();

        TextArea systemPromptArea = new TextArea("System Prompt");
        systemPromptArea.setWidthFull();

        MultiSelectComboBox<String> toolComboBox = new MultiSelectComboBox<>("选择工具");
        toolComboBox.setWidthFull();
        toolComboBox.setItems(getAvailableTools());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button saveBtn = new Button("保存");
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> {
            if (saveAgent(null, nameField.getValue(), descArea.getValue(), 
                         systemPromptArea.getValue(), toolComboBox.getSelectedItems())) {
                dialog.close();
                refreshData();
            }
        });

        Button cancelBtn = new Button("取消");
        cancelBtn.addClickListener(e -> dialog.close());

        buttonLayout.add(saveBtn, cancelBtn);

        layout.add(nameField, descArea, systemPromptArea, toolComboBox, buttonLayout);
        dialog.add(layout);
        dialog.open();
    }

    private void openEditAgentDialog(UserAgent agent) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setWidth("50%");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        TextField nameField = new TextField("Agent名称");
        nameField.setWidthFull();
        nameField.setValue(agent.getNameAgent());

        TextArea descArea = new TextArea("描述");
        descArea.setWidthFull();
        if (agent.getCdDesc() != null) {
            descArea.setValue(agent.getCdDesc());
        }

        TextArea systemPromptArea = new TextArea("System Prompt");
        systemPromptArea.setWidthFull();
        if (agent.getSystemPrompt() != null) {
            systemPromptArea.setValue(agent.getSystemPrompt());
        }

        MultiSelectComboBox<String> toolComboBox = new MultiSelectComboBox<>("选择工具");
        toolComboBox.setWidthFull();
        toolComboBox.setItems(getAvailableTools());

        // 获取当前Agent已选择的工具
        List<AgentTool> agentTools = agentToolMapper.selectByExample(null);
        Set<String> selectedTools = agentTools.stream()
                .filter(at -> at.getIdAgent().equals(agent.getIdAgent()))
                .map(AgentTool::getNameTool)
                .collect(Collectors.toSet());
        toolComboBox.setValue(selectedTools);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button saveBtn = new Button("保存");
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> {
            if (saveAgent(agent, nameField.getValue(), descArea.getValue(), 
                         systemPromptArea.getValue(), toolComboBox.getSelectedItems())) {
                dialog.close();
                refreshData();
            }
        });

        Button cancelBtn = new Button("取消");
        cancelBtn.addClickListener(e -> dialog.close());

        buttonLayout.add(saveBtn, cancelBtn);

        layout.add(nameField, descArea, systemPromptArea, toolComboBox, buttonLayout);
        dialog.add(layout);
        dialog.open();
    }

    private boolean saveAgent(UserAgent existingAgent, String name, String desc, 
                             String systemPrompt, Set<String> selectedTools) {
        if (name == null || name.trim().isEmpty()) {
            // 显示错误提示
            return false;
        }

        UserAgent agent;
        if (existingAgent == null) {
            // 创建新Agent
            agent = new UserAgent();
            agent.setUserId(currentUser.getUserId());
            agent.setIdAgent(IdUtil.simpleUUID());
        } else {
            // 更新现有Agent
            agent = existingAgent;
        }

        agent.setNameAgent(name);
        agent.setCdDesc(desc);
        agent.setSystemPrompt(systemPrompt);

        try {
            if (existingAgent == null) {
                userAgentMapper.insert(agent);
            } else {
                userAgentMapper.updateByPrimaryKeySelective(agent);
            }

            // 保存工具关联信息
            if (existingAgent != null) {
                // 删除旧的工具关联
                // 这里应该根据实际的Mapper方法实现
            }

            // 添加新的工具关联
            for (String toolName : selectedTools) {
                AgentTool agentTool = new AgentTool();
                agentTool.setIdAgent(agent.getIdAgent());
                agentTool.setNameTool(toolName);
                agentToolMapper.insert(agentTool);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void confirmDeleteAgent(UserAgent agent) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("确认删除");
        dialog.setText("确定要删除Agent \"" + agent.getNameAgent() + "\" 吗？此操作不可撤销。");

        dialog.setConfirmText("确认");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> deleteAgent(agent));
        
        dialog.setCancelText("取消");
        dialog.setCancelable(true);
        dialog.open();
    }

    private void deleteAgent(UserAgent agent) {
        // 删除Agent及其关联的工具
        userAgentMapper.deleteByPrimaryKey(agent.getUserId(), agent.getIdAgent());
        refreshData();
    }

    private List<String> getAvailableTools() {
        // 扫描com.sl.chat.tool包下所有实现Tool接口的类
        List<String> tools = new ArrayList<>();
        
        try {
            // 获取项目根路径
            String classpath = System.getProperty("user.dir");
            Path toolPath = Paths.get(classpath, "src", "main", "java", "com", "sl", "chat", "tool");
            
            if (Files.exists(toolPath)) {
                Files.walk(toolPath)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString().replace(".java", "");
                            if (!"Tool".equals(fileName) && !"ToolExecutor".equals(fileName)) {
                                // 尝试加载类并检查是否实现了Tool接口
                                Class<?> clazz = Class.forName("com.sl.chat.tool." + fileName);
                                if (Tool.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                                    // 实例化并调用getName方法获取工具名称
                                    Tool toolInstance = (Tool) clazz.getDeclaredConstructor().newInstance();
                                    tools.add(toolInstance.getName());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 默认添加天气工具
        if (tools.isEmpty()) {
            tools.add("get_weather");
        }
        
        return tools;
    }
}