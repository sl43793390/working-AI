package com.sl.chat.ui;

import cn.hutool.core.util.IdUtil;
import com.sl.entity.KnowledgeBase;
import com.sl.entity.User;
import com.sl.mapper.KnowledgeBaseFileMapper;
import com.sl.mapper.KnowledgeBaseMapper;
import com.sl.service.RagService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("rag-mgmt")
@PageTitle("Rag mgmt")
@Menu(order = 2, icon = "vaadin:database", title = "知识库管理")
@PermitAll
public class RagMgmtView extends Main {

    private KnowledgeBaseMapper knowledgeBaseMapper;
    private KnowledgeBaseFileMapper knowledgeBaseFileMapper;
    private RagService ragService;

    private TextField searchField;
    private Button searchBtn;
    private Button createBtn;
    private Grid<KnowledgeBase> knowledgeBaseGrid;
    private User currentUser;
    
    private TabSheet tabSheet;
    private Tab knowledgeBaseTab;
    private Tab fileManagementTab;
    
    // 文件管理相关组件
    private KnowledgeBase selectedKnowledgeBase;

    public RagMgmtView(KnowledgeBaseMapper knowledgeBaseMapper, 
                       KnowledgeBaseFileMapper knowledgeBaseFileMapper,
                       RagService ragService) {
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
        }
        this.knowledgeBaseMapper = knowledgeBaseMapper;
        this.knowledgeBaseFileMapper = knowledgeBaseFileMapper;
        this.ragService = ragService;
        
        // 初始化UI组件
        initView();
    }

    private void initView() {
        tabSheet = new TabSheet();
        
        // 创建知识库标签页
        knowledgeBaseTab = new Tab("知识库");
        tabSheet.add(knowledgeBaseTab, createKnowledgeBaseTab());
        
        // 创建文件管理标签页
        fileManagementTab = new Tab("文件管理");
        tabSheet.add(fileManagementTab, createFileManagementTab());
        
        // 默认选中知识库标签页
        tabSheet.setSelectedTab(knowledgeBaseTab);
        
        add(tabSheet);
    }
    
    private Div createKnowledgeBaseTab() {
        Div content = new Div();
        content.setSizeFull();
        
        // 创建工具栏
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setSpacing(true);
        toolbar.setPadding(true);
        
        searchField = new TextField();
        searchField.setPlaceholder("搜索知识库...");
        searchField.setWidth("300px");
        
        searchBtn = new Button("搜索", VaadinIcon.SEARCH.create());
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> searchKnowledgeBases());
        
        createBtn = new Button("新建", VaadinIcon.PLUS.create());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBtn.addClickListener(e -> openCreateDialog());
        
        toolbar.add(searchField, searchBtn, createBtn);
        toolbar.expand(searchField);
        
        // 创建知识库表格
        knowledgeBaseGrid = new Grid<>(KnowledgeBase.class, false);
        knowledgeBaseGrid.addColumn(KnowledgeBase::getNameBase).setHeader("名称").setAutoWidth(true);
        knowledgeBaseGrid.addColumn(KnowledgeBase::getDescBase).setHeader("描述").setAutoWidth(true);
        knowledgeBaseGrid.addColumn(KnowledgeBase::getDbType).setHeader("存储类型").setAutoWidth(true);
        knowledgeBaseGrid.addColumn(KnowledgeBase::getEmbeddingModel).setHeader("嵌入模型").setAutoWidth(true);
        knowledgeBaseGrid.addColumn(KnowledgeBase::getSearchType).setHeader("检索类型").setAutoWidth(true);
        knowledgeBaseGrid.addComponentColumn(knowledgeBase -> {
            HorizontalLayout actions = new HorizontalLayout();
            
            Button editBtn = new Button("修改", VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editBtn.addClickListener(e -> openEditDialog(knowledgeBase));
            
            Button deleteBtn = new Button("删除", VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> deleteKnowledgeBase(knowledgeBase));
            
            Button manageFilesBtn = new Button("管理文件", VaadinIcon.FOLDER_OPEN.create());
            manageFilesBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            manageFilesBtn.addClickListener(e -> {
                selectedKnowledgeBase = knowledgeBase;
                tabSheet.setSelectedTab(fileManagementTab);
            });
            
            actions.add(editBtn, deleteBtn, manageFilesBtn);
            return actions;
        }).setHeader("操作").setAutoWidth(true);
        
        knowledgeBaseGrid.setSizeFull();
        
        // 加载数据
        loadKnowledgeBases();
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        
        layout.add(toolbar, knowledgeBaseGrid);
        layout.expand(knowledgeBaseGrid);
        
        content.add(layout);
        return content;
    }
    
    private Div createFileManagementTab() {
        Div content = new Div();
        content.setSizeFull();
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        H2 placeholder = new H2("请选择一个知识库来管理文件");
        layout.add(placeholder);
        
        content.add(layout);
        return content;
    }
    
    private void loadKnowledgeBases() {
        List<KnowledgeBase> knowledgeBases = ragService.getKnowledgeBasesByUserId(currentUser.getUserId());
        knowledgeBaseGrid.setItems(knowledgeBases);
    }
    
    private void searchKnowledgeBases() {
        String keyword = searchField.getValue();
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<KnowledgeBase> knowledgeBases = ragService.searchKnowledgeBasesByName(currentUser.getUserId(), keyword);
            knowledgeBaseGrid.setItems(knowledgeBases);
        } else {
            loadKnowledgeBases();
        }
    }
    
    private void openCreateDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setDraggable(true);
        dialog.setHeaderTitle("创建知识库");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
        
        TextField nameField = new TextField("名称");
        nameField.setWidth("100%");
        
        TextField descField = new TextField("描述");
        descField.setWidth("100%");
        
        TextField dbTypeField = new TextField("存储类型");
        dbTypeField.setWidth("100%");
        
        TextField embeddingModelField = new TextField("嵌入模型");
        embeddingModelField.setWidth("100%");
        
        TextField searchTypeField = new TextField("检索类型");
        searchTypeField.setWidth("100%");
        
        layout.add(nameField, descField, dbTypeField, embeddingModelField, searchTypeField);
        
        Button saveButton = new Button("保存", e -> {
            KnowledgeBase knowledgeBase = new KnowledgeBase();
            knowledgeBase.setUserId(currentUser.getUserId());
            knowledgeBase.setNameBase(nameField.getValue());
            knowledgeBase.setDescBase(descField.getValue());
            knowledgeBase.setDbType(dbTypeField.getValue());
            knowledgeBase.setEmbeddingModel(embeddingModelField.getValue());
            knowledgeBase.setSearchType(searchTypeField.getValue());
            knowledgeBase.setIdBase(IdUtil.simpleUUID());
            
            if (ragService.createKnowledgeBase(knowledgeBase)) {
                Notification.show("创建成功", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                loadKnowledgeBases();
                dialog.close();
            } else {
                Notification.show("创建失败", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelButton = new Button("取消", e -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(false);
        
        layout.add(buttonLayout);
        dialog.add(layout);
        dialog.open();
    }
    
    private void openEditDialog(KnowledgeBase knowledgeBase) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setDraggable(true);
        dialog.setHeaderTitle("编辑知识库");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
        
        TextField nameField = new TextField("名称");
        nameField.setWidth("100%");
        nameField.setValue(knowledgeBase.getNameBase());
        nameField.setReadOnly(true); // 主键不能修改
        
        TextField descField = new TextField("描述");
        descField.setWidth("100%");
        descField.setValue(knowledgeBase.getDescBase() != null ? knowledgeBase.getDescBase() : "");
        
        TextField dbTypeField = new TextField("存储类型");
        dbTypeField.setWidth("100%");
        dbTypeField.setValue(knowledgeBase.getDbType() != null ? knowledgeBase.getDbType() : "");
        
        TextField embeddingModelField = new TextField("嵌入模型");
        embeddingModelField.setWidth("100%");
        embeddingModelField.setValue(knowledgeBase.getEmbeddingModel() != null ? knowledgeBase.getEmbeddingModel() : "");
        
        TextField searchTypeField = new TextField("检索类型");
        searchTypeField.setWidth("100%");
        searchTypeField.setValue(knowledgeBase.getSearchType() != null ? knowledgeBase.getSearchType() : "");
        
        layout.add(nameField, descField, dbTypeField, embeddingModelField, searchTypeField);
        
        Button saveButton = new Button("保存", e -> {
            knowledgeBase.setDescBase(descField.getValue());
            knowledgeBase.setDbType(dbTypeField.getValue());
            knowledgeBase.setEmbeddingModel(embeddingModelField.getValue());
            knowledgeBase.setSearchType(searchTypeField.getValue());
            
            if (ragService.updateKnowledgeBase(knowledgeBase)) {
                Notification.show("更新成功", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                loadKnowledgeBases();
                dialog.close();
            } else {
                Notification.show("更新失败", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelButton = new Button("取消", e -> dialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(false);
        
        layout.add(buttonLayout);
        dialog.add(layout);
        dialog.open();
    }
    
    private void deleteKnowledgeBase(KnowledgeBase knowledgeBase) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setModal(true);
        confirmDialog.setHeaderTitle("确认删除");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);
        
        layout.add(new Div("确定要删除知识库 \"" + knowledgeBase.getNameBase() + "\" 吗？"));
        
        Button confirmButton = new Button("确认", e -> {
            if (ragService.deleteKnowledgeBase(knowledgeBase.getUserId(), knowledgeBase.getNameBase())) {
                Notification.show("删除成功", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                loadKnowledgeBases();
                confirmDialog.close();
            } else {
                Notification.show("删除失败", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelButton = new Button("取消", e -> confirmDialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(false);
        
        layout.add(buttonLayout);
        confirmDialog.add(layout);
        confirmDialog.open();
    }
}
