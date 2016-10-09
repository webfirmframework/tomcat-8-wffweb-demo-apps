package com.wffwebdemo.wffwebdemoproject.page.layout;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.formatting.B;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.ServerLogPage;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;
import com.wffwebdemo.wffwebdemoproject.page.template.users.ListUsersTempate;
import com.wffwebdemo.wffwebdemoproject.page.template.users.RegisterUserTempate;

@SuppressWarnings("serial")
public class DashboardLayout extends Div implements ServerAsyncMethod {

    private DocumentModel documentModel;

    private Button logoutButton;

    private Button listUsersButton;

    private Button registerUserButton;

    private ListUsersTempate listUsers;

    private RegisterUserTempate regUser;

    public DashboardLayout(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;

        develop();
    }

    private void develop() {
        documentModel.getPageTitle()
                .addInnerHtml(new NoTag(null, "User Dashboard"));

        logoutButton = new Button(this, new OnClick(this)) {
            {
                new NoTag(this, "Logout");
            }
        };

        listUsersButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "List Users");
                    }
                };
            }
        };

        registerUserButton = new Button(this, new OnClick(this)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Register User");
                    }
                };
            }
        };
    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        if (event.getSourceTag().equals(logoutButton)) {

            LoginTemplate loginTemplate = new LoginTemplate(documentModel);
            documentModel.getBodyDiv().addInnerHtml(loginTemplate);
            
            displayInServerLogPage("logoutButton clicked");
            
        } else if (event.getSourceTag().equals(listUsersButton)) {

            if (regUser != null) {
                removeChild(regUser);
                regUser = null;
            }

            if (listUsers == null) {
                listUsers = new ListUsersTempate(documentModel);

                appendChild(listUsers);
            }
            displayInServerLogPage("listUsersButton clicked");
        } else if (event.getSourceTag().equals(registerUserButton)) {

            if (listUsers != null) {
                removeChild(listUsers);
                listUsers = null;
            }

            if (regUser == null) {

                regUser = new RegisterUserTempate(documentModel);
                appendChild(regUser);

            }
            displayInServerLogPage("registerUserButton clicked");
        }

        return null;
    }
    
    private void displayInServerLogPage(String msg) {
        Object serverLogPageInstanceId = documentModel.getHttpSession()
                .getAttribute("serverLogPageInstanceId");
        if (serverLogPageInstanceId != null) {
            ServerLogPage serverLogPage = (ServerLogPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(serverLogPageInstanceId.toString());
            serverLogPage.log(msg);
        }
    }

}
