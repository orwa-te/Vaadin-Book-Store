package org.vaadin.example.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route("")
public class LoginView extends VerticalLayout {

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final Button loginButton = new Button("Login");
    private final Div errorMessage = new Div();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        errorMessage.getStyle().setColor("red");
        errorMessage.setVisible(false);

        loginButton.addClickListener(e -> authenticate());
        loginButton.addClickShortcut(Key.ENTER);

        add(new H1("Welcome"), username, password, loginButton, errorMessage);
    }

    private void authenticate() {
        if ("admin".equals(username.getValue()) && "password".equals(password.getValue())) {
            getUI().ifPresent(ui -> {
                ui.getSession().setAttribute("authenticated", true);
                ui.navigate("dashboard");
            });
        } else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setVisible(true);
        }
    }

//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        if (isAuthenticated()) {
//            event.forwardTo(DashboardView.class);
//        }
//    }

    private boolean isAuthenticated() {
        return getUI().map(ui -> {
            Boolean authenticated = (Boolean) ui.getSession().getAttribute("authenticated");
            return authenticated != null && authenticated;
        }).orElse(false);
    }
}