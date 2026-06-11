package com.sl.chat.ui;

import cn.hutool.core.util.IdUtil;
import com.sl.base.ui.component.ViewToolbar;
import com.sl.chat.tool.MyTool;
import com.sl.entity.AgentTool;
import com.sl.entity.AgentToolExample;
import com.sl.entity.User;
import com.sl.entity.UserAgent;
import com.sl.mapper.AgentToolMapper;
import com.sl.mapper.UserAgentMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import jakarta.annotation.security.PermitAll;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public AgentMgmtView(UserAgentMapper userAgentMapper, AgentToolMapper agentToolMapper, ChatMemoryProvider chatMemoryProvider) {
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

        MultiSelectComboBox<AgentTool> toolComboBox = new MultiSelectComboBox<>("选择工具");
        toolComboBox.setWidthFull();
        toolComboBox.setItems(getAvailableTools());
        toolComboBox.setItemLabelGenerator(AgentTool::getNameTool);

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

        MultiSelectComboBox<AgentTool> toolComboBox = new MultiSelectComboBox<>("选择工具");
        toolComboBox.setWidthFull();
        toolComboBox.setItems(getAvailableTools());
        toolComboBox.setItemLabelGenerator(AgentTool::getNameTool);

        // 获取当前Agent已选择的工具
        AgentToolExample example = new AgentToolExample();
        example.createCriteria().andIdAgentEqualTo(agent.getIdAgent());
        List<AgentTool> agentTools = agentToolMapper.selectByExample(example);
        toolComboBox.setValue(agentTools);

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
                             String systemPrompt, Set<AgentTool> selectedTools) {
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
                for (AgentTool toolName : selectedTools) {
                    agentToolMapper.deleteByPrimaryKey(agent.getIdAgent(), toolName.getNameTool());
                }
            }

            // 添加新的工具关联
            for (AgentTool tool : selectedTools) {
                tool.setIdAgent(agent.getIdAgent());
                agentToolMapper.insert(tool);
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

    private List<AgentTool> getAvailableTools() {
        List<AgentTool> tools = new ArrayList<>();
            
        try {
            // 使用 Spring 的 Resource 扫描器扫描 classpath 下带有@MyTool 注解的类
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
                
            // 获取 com.sl.chat.tool 包下的所有 class 文件
            org.springframework.core.io.Resource[] resources = resolver.getResources("classpath*:com/sl/chat/tool/**/*.class");

            for (org.springframework.core.io.Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        
                    // 检查是否有@MyTool 注解
                    if (metadataReader.getAnnotationMetadata().hasAnnotation("com.sl.chat.tool.MyTool")) {
                        String className = metadataReader.getClassMetadata().getClassName();
                            
                        // 加载类并获取注解信息
                        Class<?> clazz = Class.forName(className);
                        MyTool myTool = clazz.getAnnotation(MyTool.class);
                            
                        if (myTool != null && !myTool.name().isEmpty()) {
                            // 创建 AgentTool 实例
                            AgentTool agentTool = new AgentTool();
                            agentTool.setIdAgent(IdUtil.getSnowflakeNextIdStr());
                            agentTool.setNameTool(myTool.name());
                            agentTool.setNameToolClass(clazz.getName());
                                
                            tools.add(agentTool);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        return tools;
    }
}