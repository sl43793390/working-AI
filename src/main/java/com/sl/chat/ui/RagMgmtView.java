package com.sl.chat.ui;

import cn.hutool.core.util.IdUtil;
import com.sl.entity.KnowledgeBase;
import com.sl.entity.KnowledgeBaseFile;
import com.sl.entity.User;
import com.sl.service.RagService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.StartedEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("rag-mgmt")
@PageTitle("Rag mgmt")
@Menu(order = 2, icon = "vaadin:database", title = "知识库管理")
@PermitAll
public class RagMgmtView extends Composite<Div> {

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

    public RagMgmtView(RagService ragService) {
        this.ragService = ragService;
        
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
        }
        
        // 初始化UI组件
        initView();
    }

    private void initView() {
        tabSheet = new TabSheet();
        tabSheet.setHeight("800px");
        // 创建知识库标签页
        knowledgeBaseTab = new Tab("知识库");
        tabSheet.add(knowledgeBaseTab, createKnowledgeBaseTab());
        
        // 创建文件管理标签页
        fileManagementTab = new Tab("文件管理");
        tabSheet.add(fileManagementTab, createFileManagementTab());
        
        // 默认选中知识库标签页
        tabSheet.setSelectedTab(knowledgeBaseTab);
        tabSheet.addSelectedChangeListener(event -> {
            if (event.getSelectedTab() == fileManagementTab) {
                if (selectedKnowledgeBase == null){
                    Notification.show("请选择一个知识库，点击管理文件进入", 3000, Notification.Position.MIDDLE);
                    tabSheet.setSelectedTab(knowledgeBaseTab);
                }
            }
        });
        
        getContent().add(tabSheet);
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
        
        searchBtn = new Button("搜索", new Icon(VaadinIcon.SEARCH));
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e -> searchKnowledgeBases());
        
        createBtn = new Button("新建", new Icon(VaadinIcon.PLUS));
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
            
            Button editBtn = new Button("修改", new Icon(VaadinIcon.EDIT));
            editBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            editBtn.addClickListener(e -> openEditDialog(knowledgeBase));
            
            Button deleteBtn = new Button("删除", new Icon(VaadinIcon.TRASH));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> deleteKnowledgeBase(knowledgeBase));
            
            Button manageFilesBtn = new Button("管理文件", new Icon(VaadinIcon.FOLDER_OPEN));
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
        layout.setPadding(true);
        layout.setSpacing(true);
        
        // 知识库名称标签
        H3 kbNameLabel = new H3();
        kbNameLabel.setVisible(false);
        
        // 分割线
        Hr hr = new Hr();
        hr.setVisible(false);
        
        // 文件上传区域
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropLabel(new Span("点击或拖拽文件到此处上传"));
        upload.setWidth("100%");
        
        // 文件列表表格
        Grid<KnowledgeBaseFile> fileGrid = new Grid<>(KnowledgeBaseFile.class, false);
        fileGrid.addColumn(KnowledgeBaseFile::getFileName).setHeader("文件名").setAutoWidth(true);
        fileGrid.addColumn(KnowledgeBaseFile::getFileSize).setHeader("大小").setAutoWidth(true);
        fileGrid.addColumn(file -> {
            if (file.getDtUpload() != null) {
                return file.getDtUpload().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            return "";
        }).setHeader("上传时间").setAutoWidth(true);
        fileGrid.addComponentColumn(file -> {
            Button deleteBtn = new Button("删除");
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            deleteBtn.addClickListener(e -> deleteFile(file, fileGrid));
            return deleteBtn;
        }).setHeader("操作").setAutoWidth(true);
        fileGrid.setSizeFull();
        
        // 添加上传完成监听器
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            
            try {
                // 获取配置的存储路径
                String basePath = System.getProperty("knowledge.base.path", "d:/tmp/knowledge");
                File baseDir = new File(basePath);
                if (!baseDir.exists()) {
                    baseDir.mkdirs();
                }
                
                // 创建用户目录
                File userDir = new File(baseDir, currentUser.getUserId());
                if (!userDir.exists()) {
                    userDir.mkdirs();
                }
                
                // 创建知识库目录
                File kbDir = new File(userDir, selectedKnowledgeBase.getIdBase());
                if (!kbDir.exists()) {
                    kbDir.mkdirs();
                }
                
                // 保存文件到磁盘
                File savedFile = new File(kbDir, fileName);
                try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                    inputStream.transferTo(fos);
                }
                
                // 保存文件信息到数据库
                KnowledgeBaseFile kbFile = new KnowledgeBaseFile();
                kbFile.setIdBase(selectedKnowledgeBase.getIdBase());
                kbFile.setFileName(fileName);
                kbFile.setFilePath(savedFile.getAbsolutePath());
                kbFile.setFileSize(String.format("%.2f KB", savedFile.length() / 1024.0));
                kbFile.setDtUpload(new java.util.Date());
                
                if (ragService.addFileToKnowledgeBase(kbFile)) {
                    Notification.show("文件上传成功", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    refreshFileGrid(fileGrid);
                } else {
                    Notification.show("文件信息保存失败", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    // 删除已保存的文件
                    savedFile.delete();
                }
            } catch (IOException e) {
                Notification.show("文件上传失败: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        layout.add(kbNameLabel, hr, upload, fileGrid);
        layout.expand(fileGrid);
        
        content.add(layout);
        
        // 监听标签页切换事件
        tabSheet.addSelectedChangeListener(e -> {
            if (e.getSelectedTab() == fileManagementTab) {
                if (selectedKnowledgeBase != null) {
                    kbNameLabel.setText("知识库: " + selectedKnowledgeBase.getNameBase());
                    kbNameLabel.setVisible(true);
                    hr.setVisible(true);
                    refreshFileGrid(fileGrid);
                } else {
                    kbNameLabel.setVisible(false);
                    hr.setVisible(false);
                    fileGrid.setItems(List.of());
                }
            }
        });
        
        return content;
    }

    private void refreshFileGrid(Grid<KnowledgeBaseFile> fileGrid) {
        if (selectedKnowledgeBase != null) {
            List<KnowledgeBaseFile> files = ragService.getFilesByKnowledgeBaseId(selectedKnowledgeBase.getIdBase());
            fileGrid.setItems(files);
        }
    }

    private void deleteFile(KnowledgeBaseFile file, Grid<KnowledgeBaseFile> fileGrid) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("确认删除");
        
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        
        layout.add(new Span("确定要删除文件 \"" + file.getFileName() + "\" 吗？"));
        
        Button confirmBtn = new Button("确认", e -> {
            try {
                // 从磁盘删除文件
                File diskFile = new File(file.getFilePath());
                if (diskFile.exists()) {
                    diskFile.delete();
                }
                
                // 从数据库删除记录
                // 注意：这里需要修改为根据文件ID删除，而不是知识库ID
                if (ragService.deleteFileFromKnowledgeBase(file.getIdBase(),file.getFileName())) {
                    Notification.show("文件删除成功", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    refreshFileGrid(fileGrid);
                } else {
                    Notification.show("文件删除失败", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (Exception ex) {
                Notification.show("文件删除异常: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            confirmDialog.close();
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelBtn = new Button("取消", e -> confirmDialog.close());
        
        HorizontalLayout buttonLayout = new HorizontalLayout(confirmBtn, cancelBtn);
        buttonLayout.setSpacing(true);
        
        layout.add(buttonLayout);
        confirmDialog.add(layout);
        confirmDialog.open();
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
        dialog.setWidth("50%");
        
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
        dialog.setWidth("50%");
        
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
        confirmDialog.setWidth("50%");
        
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