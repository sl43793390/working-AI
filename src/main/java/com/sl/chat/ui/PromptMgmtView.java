package com.sl.chat.ui;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sl.base.ui.component.ViewToolbar;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.sl.entity.PromptCategory;
import com.sl.entity.User;
import com.sl.entity.UserPrompt;
import com.sl.mapper.PromptCategoryMapper;
import com.sl.mapper.UserPromptMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Route("prompt-list")
@PageTitle("Prompt List")
@Menu(order = 1, icon = "vaadin:align-justify", title = "Prompt 管理")
@PermitAll
public class PromptMgmtView extends Main {

    private UserPromptMapper userPromptMapper;
    private PromptCategoryMapper promptCategoryMapper;

    private TextField searchField;
    private ComboBox<PromptCategory> categoryComboBox;
    private Button searchBtn;
    private Button createCategoryBtn;
    private Button createPromptBtn; // 新增新建提示词按钮
    private Grid<UserPrompt> promptGrid;
    private User currentUser;
    private FlexLayout promptCardContainer;

    public PromptMgmtView(UserPromptMapper userPromptMapper, PromptCategoryMapper promptCategoryMapper) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
        }
        this.userPromptMapper = userPromptMapper;
        this.promptCategoryMapper = promptCategoryMapper;
        
        // 初始化UI组件
        initView();
        
        // 加载数据
        refreshData();
    }

    private void initView() {
        // 创建搜索区域
        createSearchArea();
        
        // 创建提示词卡片容器
        promptCardContainer = new FlexLayout();
        promptCardContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        promptCardContainer.setClassName("prompt-card-container");
        promptCardContainer.setWidthFull();
        
        // 设置主布局
        setSizeFull();
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        add(new ViewToolbar("Prompt 管理"));
        add(createSearchArea());
        add(promptCardContainer);
    }

    private HorizontalLayout createSearchArea() {
        // 搜索输入框
        searchField = new TextField();
        searchField.setPlaceholder("搜索提示词...");
        searchField.setWidth("300px");

        // 类别下拉框
        categoryComboBox = new ComboBox<>("类别");
        categoryComboBox.setPlaceholder("选择类别");
        categoryComboBox.setWidth("200px");

        // 搜索按钮
        searchBtn = new Button("搜索", new Icon(VaadinIcon.SEARCH));
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> refreshData());

        // 新建类别按钮
        createCategoryBtn = new Button("新建类别", new Icon(VaadinIcon.PLUS));
        createCategoryBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createCategoryBtn.addClickListener(e -> openCreateCategoryDialog());
        
        // 新建提示词按钮
        createPromptBtn = new Button("新建提示词", new Icon(VaadinIcon.PLUS_CIRCLE));
        createPromptBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createPromptBtn.addClickListener(e -> openCreatePromptDialog());

        HorizontalLayout searchLayout = new HorizontalLayout(searchField, categoryComboBox, searchBtn, createCategoryBtn, createPromptBtn);
        searchLayout.setAlignItems(HorizontalLayout.Alignment.END);
        searchLayout.setPadding(true);
        searchLayout.setSpacing(true);
        searchLayout.setWidthFull();
        return searchLayout;
    }

    private void refreshData() {
        // 清空现有内容
        promptCardContainer.removeAll();
        
        // 获取类别列表并更新下拉框
        QueryWrapper<PromptCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentUser.getUserId());
        List<PromptCategory> promptCategories = promptCategoryMapper.selectList(queryWrapper);
        categoryComboBox.setItems(promptCategories);
        categoryComboBox.setItemLabelGenerator(PromptCategory::getNameCategory);
        
        // 查询提示词数据
        List<UserPrompt> prompts = new ArrayList<>();
        
        // 根据搜索条件过滤数据
        String searchText = searchField.getValue();
        PromptCategory selectedCategory = categoryComboBox.getValue();

        //根据用户ID查询当前用户的所有提示词
        QueryWrapper<UserPrompt> queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("USER_ID", currentUser.getUserId());
        List<UserPrompt> allPrompts = userPromptMapper.selectList(queryWrapper2);
        for (UserPrompt prompt : allPrompts) {
            // 筛选当前用户的提示词
            if (!prompt.getUserId().equals(currentUser.getUserId())) {
                continue;
            }

            boolean matchesSearch = searchText == null || searchText.isEmpty() ||
                    prompt.getNamePrompt().contains(searchText);
            
            boolean matchesCategory = selectedCategory == null || 
                    (prompt.getCdCategory() != null && prompt.getCdCategory().equals(selectedCategory.getIdCategory()));
            
            if (matchesSearch && matchesCategory) {
                prompts.add(prompt);
            }
        }
        
        // 创建提示词卡片
        for (UserPrompt prompt : prompts) {
            promptCardContainer.add(createPromptCard(prompt));
        }
    }

    private Div createPromptCard(UserPrompt prompt) {
        // 卡片容器
        Div card = new Div();
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.Padding.SMALL,
                LumoUtility.Margin.SMALL,
                LumoUtility.Margin.MEDIUM
        );
        // 缩小卡片尺寸
        card.getStyle().set("width", "250px").set("height", "220px");
        card.getStyle().set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");
        
        // 卡片头部（标题栏）
        Div cardHeader = new Div();
        cardHeader.addClassNames(
                LumoUtility.Background.PRIMARY_10,
                LumoUtility.Padding.SMALL,
                LumoUtility.Margin.Bottom.SMALL,
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.BETWEEN,
                LumoUtility.AlignItems.CENTER
        );
        
        // 提示词名称
        H4 name = new H4(prompt.getNamePrompt() != null ? prompt.getNamePrompt() : "未命名");
        name.getStyle().set("margin-top", "0");
        name.getStyle().set("margin-bottom", "0");
        
        // 创建时间
        Paragraph createTime = new Paragraph();
        if (prompt.getDtCreate() != null) {
            // 格式化日期显示为 yyyy-MM-dd HH:mm:ss
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            createTime.setText(sdf.format(prompt.getDtCreate()));
        } else {
            createTime.setText("未知时间");
        }
        createTime.getStyle().set("margin-top", "0");
        createTime.getStyle().set("margin-bottom", "0");
        createTime.getStyle().set("font-size", "small");
        createTime.addClassNames(LumoUtility.TextColor.SECONDARY);
        
        // 操作按钮区域
        Div actions = new Div();
        actions.addClassNames(LumoUtility.Display.FLEX);
        
        // 编辑按钮
        Button editBtn = new Button(new Icon(VaadinIcon.EDIT));
        editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        editBtn.addClickListener(e -> openEditPromptDialog(prompt));
        
        // 删除按钮
        Button deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        deleteBtn.addClickListener(e -> {
            // 显示确认删除对话框
            showDeleteConfirmationDialog(prompt);
        });
        
        actions.add(editBtn, deleteBtn);
        
        // 组装头部
        VerticalLayout headerInfo = new VerticalLayout(name, createTime);
        headerInfo.setPadding(false);
        headerInfo.setSpacing(false);
        cardHeader.add(headerInfo, actions);
        
        // 卡片内容
        Div content = new Div();
        content.addClassNames(LumoUtility.Padding.SMALL);
        
        String contentText = prompt.getContent() != null ? prompt.getContent() : "";
        if (contentText.length() > 50) {
            contentText = contentText.substring(0, 50) + "...";
        }
        
        Paragraph contentPara = new Paragraph(contentText);
        contentPara.getStyle().set("white-space", "pre-wrap");
        contentPara.getStyle().set("word-break", "break-word");
        content.add(contentPara);
        
        // 卡片底部（复制按钮）
        Div cardFooter = new Div();
        cardFooter.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.END,
                LumoUtility.Padding.SMALL
        );
        
        Button copyBtn = new Button("复制", new Icon(VaadinIcon.COPY));
        copyBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        copyBtn.addClickListener(e -> {
//                 copyPrompt(prompt);//原来的功能是复制真跟个prompt，现在改为复制到剪贴板
//                refreshData();
            String jsCode = "navigator.clipboard.writeText('" + prompt.getContent() + "').then(function() {" +
                    "    console.log('Text copied to clipboard'); " +
                    "}, function(err) {" +
                    "    console.error('Could not copy text: ', err);" +
                    "});";

            Page page = UI.getCurrent().getPage();
            if (page != null) {
                page.executeJs(jsCode).then(String.class, success -> {
                    if (success == null) {
//                        Notification.show("复制失败，请使用编辑模式打开复制");
                    } else {
                        Notification.show("复制成功");
                    }
                });
            } else {
                Notification.show("Unable to access the current page.");
            }
            Notification.show("复制成功", 3000, Notification.Position.BOTTOM_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        cardFooter.add(copyBtn);
        
        // 组装整个卡片
        card.add(cardHeader, content, cardFooter);
        
        return card;
    }

    // 显示删除确认对话框
    private void showDeleteConfirmationDialog(UserPrompt prompt) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("确认删除");
        
        Paragraph message = new Paragraph("确定要删除提示词 \"" + 
            (prompt.getNamePrompt() != null ? prompt.getNamePrompt() : "未命名") + "\" 吗？");
        message.getStyle().set("margin", "var(--lumo-space-m)");
        
        VerticalLayout dialogLayout = new VerticalLayout(message);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        
        confirmDialog.add(dialogLayout);
        
        Button confirmButton = new Button("确认", e -> {
            userPromptMapper.deleteByPrimaryKey(prompt.getUserId(), prompt.getIdPrompt());
            refreshData();
            Notification.show("删除成功", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelButton = new Button("取消", e -> confirmDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        
        confirmDialog.getFooter().add(cancelButton, confirmButton);
        confirmDialog.open();
    }

    // 复制提示词
    private void copyPrompt(UserPrompt originalPrompt) {
        try {
            // 创建新的提示词对象
            UserPrompt newPrompt = new UserPrompt();
            newPrompt.setUserId(currentUser.getUserId());
            newPrompt.setIdPrompt(IdUtil.getSnowflakeNextIdStr());
            
            // 名称添加-copy后缀
            String newName = (originalPrompt.getNamePrompt() != null ? originalPrompt.getNamePrompt() : "未命名") + "-copy";
            newPrompt.setNamePrompt(newName);
            
            // 复制其他属性
            newPrompt.setCdCategory(originalPrompt.getCdCategory());
            newPrompt.setContent(originalPrompt.getContent());
            newPrompt.setDtCreate(new Timestamp(System.currentTimeMillis()));
            
            // 保存到数据库
            userPromptMapper.insert(newPrompt);
            
            // 刷新页面
            refreshData();
            
            Notification.show("复制成功", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("复制失败: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    
    // 打开新建提示词对话框
    private void openCreatePromptDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("新建提示词");
        
        // 提示词名称输入框，限制长度为32个字符
        TextField nameField = new TextField("提示词名称");
        nameField.setMaxLength(32);
        nameField.setWidthFull();
        nameField.setRequired(true);
        nameField.setErrorMessage("请输入提示词名称");

        // 类别选择下拉框
        ComboBox<PromptCategory> categoryField = new ComboBox<>("提示词类别");
        QueryWrapper<PromptCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentUser.getUserId());
        List<PromptCategory> promptCategories = promptCategoryMapper.selectList(queryWrapper);
        categoryField.setItems(promptCategories);
        categoryField.setItemLabelGenerator(PromptCategory::getNameCategory);
        categoryField.setWidthFull();
        categoryField.setRequired(true);
        categoryField.setErrorMessage("请选择提示词类别");

        // 内容输入区域，限制长度为16000个字符
        TextArea contentArea = new TextArea("提示词内容");
        contentArea.setMaxLength(16000);
        contentArea.setWidthFull();
        contentArea.setMinHeight("200px");
        contentArea.setRequired(true);
        contentArea.setErrorMessage("请输入提示词内容");

        // 按钮区域
        Button saveButton = new Button("保存", e -> {
            // 验证输入
            if (nameField.isEmpty()) {
                Notification.show("请输入提示词名称", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            if (categoryField.isEmpty()) {
                Notification.show("请选择提示词类别", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            if (contentArea.isEmpty()) {
                Notification.show("请输入提示词内容", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                // 创建新提示词
                UserPrompt prompt = new UserPrompt();
                prompt.setUserId(currentUser.getUserId());
                prompt.setIdPrompt(IdUtil.getSnowflakeNextIdStr());
                prompt.setNamePrompt(nameField.getValue());
                prompt.setCdCategory(categoryField.getValue().getNameCategory());
                prompt.setContent(contentArea.getValue());
                prompt.setDtCreate(new Timestamp(System.currentTimeMillis()));

                // 保存到数据库
                userPromptMapper.insert(prompt);

                Notification.show("提示词创建成功", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshData(); // 刷新数据
            } catch (Exception ex) {
                Notification.show("保存失败: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        Button cancelButton = new Button("取消", e -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // 添加组件到对话框
        VerticalLayout dialogLayout = new VerticalLayout(nameField, categoryField, contentArea);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setMargin(false);
        dialogLayout.setWidth("400px");
        dialogLayout.setMinWidth("400px");

        dialog.add(dialogLayout);

        // 设置对话框按钮
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();
    }

    private void openCreateCategoryDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("新建类别");

        // 类别名称输入框
        TextField categoryNameField = new TextField("类别名称");
        categoryNameField.setWidthFull();

        // 按钮区域
        Button saveButton = new Button("保存", e -> {
            String categoryName = categoryNameField.getValue();
            if (categoryName == null || categoryName.trim().isEmpty()) {
                Notification.show("请输入类别名称", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // 创建新类别
            PromptCategory category = new PromptCategory();
            category.setUserId(currentUser.getUserId());
            category.setIdCategory(UUID.randomUUID().toString());
            category.setNameCategory(categoryName.trim());

            promptCategoryMapper.insert(category);

            Notification.show("类别创建成功", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();
            refreshData(); // 刷新数据以更新下拉框
        });

        Button cancelButton = new Button("取消", e -> dialog.close());

        // 添加组件到对话框
        VerticalLayout dialogLayout = new VerticalLayout(categoryNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setMargin(false);
        dialogLayout.setWidth("400px");

        dialog.add(dialogLayout);

        // 设置对话框按钮
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();
    }

    private void openEditPromptDialog(UserPrompt prompt) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("编辑提示词");

        // 提示词名称输入框
        TextField nameField = new TextField("提示词名称");
        nameField.setValue(prompt.getNamePrompt() != null ? prompt.getNamePrompt() : "");
        nameField.setWidthFull();

        // 类别选择
        ComboBox<PromptCategory> categoryField = new ComboBox<>("类别");
        QueryWrapper<PromptCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", currentUser.getUserId());
        List<PromptCategory> promptCategories = promptCategoryMapper.selectList(queryWrapper);
        categoryField.setItems(promptCategories);
        categoryField.setItemLabelGenerator(PromptCategory::getNameCategory);
        
        // 设置当前类别
        if (prompt.getCdCategory() != null) {
            categoryField.setValue(promptCategories.stream().filter(category -> category.getIdCategory().equals(prompt.getCdCategory())).findFirst().orElse(null));
        }
        categoryField.setWidthFull();

        // 内容输入区域
        TextArea contentArea = new TextArea("提示词内容");
        contentArea.setValue(prompt.getContent() != null ? prompt.getContent() : "");
        contentArea.setWidthFull();
        contentArea.setMinHeight("200px");

        // 保存按钮事件
        Button saveButton = new Button("保存", e -> {
            // 更新提示词信息
            prompt.setNamePrompt(nameField.getValue());
            prompt.setCdCategory(categoryField.getValue() != null ? categoryField.getValue().getIdCategory() : null);
            prompt.setContent(contentArea.getValue());

            userPromptMapper.updateByPrimaryKey(prompt);

            Notification.show("保存成功", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            dialog.close();
            refreshData(); // 刷新数据
        });

        Button cancelButton = new Button("取消", e -> dialog.close());

        // 添加组件到对话框
        VerticalLayout dialogLayout = new VerticalLayout(nameField, categoryField, contentArea);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setMargin(false);
        dialogLayout.setWidth("400px");
        dialogLayout.setMinWidth("400px");

        dialog.add(dialogLayout);

        // 设置对话框按钮
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        dialog.open();
    }
}
