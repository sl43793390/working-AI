package com.sl.base.ui.view;

import com.sl.entity.User;
import com.sl.service.UserService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {
//	@AnonymousAllowed, @PermitAll, @RolesAllowed.
//    public static final String LOGIN_PATH = "login";
	private static final Logger logger = LoggerFactory.getLogger(LoginView.class);
	
    public LoginView(@Autowired UserService userService) {

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("系统登录");
        i18n.getHeader().setDescription("提示：");
        i18n.getForm().setTitle("Working AI");
        i18n.setAdditionalInformation(null);
        i18n.getForm().setSubmit("登录");
        setI18n(i18n);
        setAction("login");
        
        setForgotPasswordButtonVisible(false);
        setOpened(true);

//        addLoginListener(new ComponentEventListener<LoginEvent>() {
//
//			private static final long serialVersionUID = 3541080283633713772L;
//
//			@Override
//			public void onComponentEvent(LoginEvent event) {
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                if (authentication != null) {
//                    logger.info("对用户[" + authentication.getName() + "]进行登录验证..验证通过.");
//                }else {
//                    logger.info("对用户[" + event.getUsername() + "]进行登录验证..验证未通过");
//                }
//				Notification notification = new Notification();
//				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
//                try {
//                    logger.debug("对用户[" + event.getUsername() + "]进行登录验证..验证开始");
//
//                    User user = userService.getUserById(event.getUsername());
//					if(user==null){
//						notification.setText("用户不存在");
//						notification.setPosition(Position.MIDDLE);
//						notification.setDuration(5000);
//						notification.open();
//						return;
//					}
//                    VaadinSession.getCurrent().setAttribute("user",user) ;
//                    VaadinSession.getCurrent().setAttribute("userName", event.getUsername());
//                    logger.info("用户{}登录成功", event.getUsername());
////						UI.getCurrent().getPage().setLocation("/menu");
////						event.getSource().getUI().ifPresent(ui -> ui.navigate("/menu"));
////                    UI.getCurrent().navigate(MainView.class);
//                    UI.getCurrent().navigate("chat");
////						getUI().ifPresent(ui -> ui.navigate("/leftview"));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//		});
    }

}