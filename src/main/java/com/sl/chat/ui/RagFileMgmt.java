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

public class RagFileMgmt extends Composite<Div> {
    
    private RagService ragService;
    private User currentUser;
    private KnowledgeBase selectedKnowledgeBase;
    
    private H3 kbNameLabel;
    private Hr hr;
    private Grid<KnowledgeBaseFile> fileGrid;
    
    public RagFileMgmt(RagService ragService, KnowledgeBase selectedKnowledgeBase) {
        this.ragService = ragService;
        this.selectedKnowledgeBase = selectedKnowledgeBase;
        
        currentUser = (User) VaadinSession.getCurrent().getAttribute("user");
        if (null == currentUser){
            UI.getCurrent().navigate("login");
        }
        
        // 初始化UI组件
        initView();
    }
    
    private void initView() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        
        // 知识库名称标签
        kbNameLabel = new H3();
        kbNameLabel.setVisible(false);
        
        // 分割线
        hr = new Hr();
        hr.setVisible(false);
        
        // 文件上传区域
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setDropLabel(new Span("点击或拖拽文件到此处上传"));
        upload.setWidth("100%");
        
        // 文件列表表格
        fileGrid = new Grid<>(KnowledgeBaseFile.class, false);
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
            deleteBtn.addClickListener(e -> deleteFile(file));
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
                    refreshFileGrid();
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
        
        getContent().add(layout);
        
        // 更新显示信息
        updateDisplay();
        refreshFileGrid();
    }
    
    public void updateSelectedKnowledgeBase(KnowledgeBase knowledgeBase) {
        this.selectedKnowledgeBase = knowledgeBase;
        updateDisplay();
        refreshFileGrid();
    }
    
    private void updateDisplay() {
        if (selectedKnowledgeBase != null) {
            kbNameLabel.setText("知识库: " + selectedKnowledgeBase.getNameBase());
            kbNameLabel.setVisible(true);
            hr.setVisible(true);
        } else {
            kbNameLabel.setVisible(false);
            hr.setVisible(false);
            if (fileGrid != null) {
                fileGrid.setItems(List.of());
            }
        }
    }
    
    private void refreshFileGrid() {
        if (selectedKnowledgeBase != null && fileGrid != null) {
            List<KnowledgeBaseFile> files = ragService.getFilesByKnowledgeBaseId(selectedKnowledgeBase.getIdBase());
            fileGrid.setItems(files);
        }
    }

    private void deleteFile(KnowledgeBaseFile file) {
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
                    refreshFileGrid();
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
}