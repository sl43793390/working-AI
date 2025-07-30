package com.sl.base.ui.view;

import com.sl.entity.User;
import com.sl.service.UserManageMentProcess;
import com.sl.service.UserService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login",autoLayout = false)
public class LoginView extends Main implements BeforeEnterObserver {
    private static final Logger logger = LoggerFactory.getLogger(LoginView.class);

    private final LoginForm login;

    public LoginView(UserService userService, UserManageMentProcess userManageMentProcess,
                     AuthenticationContext authenticationContext) {
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        setSizeFull();
        login = new LoginForm();
        login.setAction("login");
        add(login);
        setClassName(LumoUtility.Background.SUCCESS);
        login.addLoginListener(event -> {

            try {
                logger.debug("对用户[" + event.getUsername() + "]进行登录验证..验证开始");

                User user = userService.getUserById(event.getUsername());
                if(user==null){
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setText("用户不存在");
                    notification.setPosition(Position.MIDDLE);
                    notification.setDuration(5000);
                    notification.open();
                    return;
                }

                // 验证密码（这里应该使用加密后的密码进行比较）
                if (!user.getPassword().equals(userManageMentProcess.getPassword(event.getPassword()))) {
                    Notification notification = new Notification();
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setText("密码错误");
                    notification.setPosition(Position.MIDDLE);
                    notification.setDuration(5000);
                    notification.open();
                    return;
                }
                // 手动认证，创建一个未认证的token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(event.getUsername(), event.getPassword());
                // 使用AuthenticationManager进行认证
//                    Authentication authentication = authenticationManager.authenticate(authToken);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
                SecurityContextHolder.getContext().setAuthentication(authToken);
//                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                    authentication.setAuthenticated(true);

                VaadinSession.getCurrent().setAttribute("user",user) ;
                VaadinSession.getCurrent().setAttribute("userName", event.getUsername());
                logger.info("用户{}登录成功", event.getUsername());

                // 登录成功后跳转到主页面
                UI.getCurrent().navigate("chat");
            } catch (Exception e) {
                logger.error("登录过程中发生错误", e);
                Notification notification = new Notification();
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setText("登录过程发生错误: " + e.getMessage());
                notification.setPosition(Position.MIDDLE);
                notification.setDuration(5000);
                notification.open();
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}