package com.wffwebdemo.wffwebdemoproject.page.template;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.Value;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnChange;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.event.keyboard.OnKeyUp;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Span;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.BMValueType;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.ServerLogPage;
import com.wffwebdemo.wffwebdemoproject.page.layout.DashboardLayout;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class LoginTemplate extends Div implements ServerAsyncMethod {

    private static final Logger LOGGER = Logger
            .getLogger(LoginTemplate.class.getName());

    private String username;

    private String password;

    private DocumentModel documentModel;

    private Span msgSpan;
    
    private OnKeyUp passwordOnKeyUp;
    
    private OnChange usernameOnChange;
    
    private OnChange passwordOnChange;
    
    private OnClick loginButtonOnClick;

    public LoginTemplate(DocumentModel documentModel) {
        super(null, new Style(
                "top:50%;left:50%;width:600px;background:lightblue;position:absolute;z-index:15;margin: 0px 0px 0px -155px;"));
        this.documentModel = documentModel;

        develop();
    }

    private Style getStyleToCenter() {
        return new Style("margin: 0px 0px 0px 124px;");
    }
    
    // validation at server side
    /**
     * validates if fieldValue contains minimum 4 characters
     * 
     * @param wffBMObject
     */
    private void validateLength(WffBMObject wffBMObject) {

        String fieldValue = (String) wffBMObject.getValue("fieldValue");

        if (fieldValue.length() < 4) {
            msgSpan.addInnerHtml(new NoTag(null, "Required min 4"));
        } else {
            msgSpan.removeAllChildren();
        }
    }

    private Form form;

    private void develop() {

        documentModel.getPageTitle().addInnerHtml(new NoTag(null, "Login"));
        
        passwordOnKeyUp = new OnKeyUp(null, this,
                "return {fieldId:source.id, fieldValue:source.value};", null);

        usernameOnChange = new OnChange(
                "/*client side validation may be done here*/ return true;",
                LoginTemplate.this,
                "return {fieldName:'username', fieldValue:source.value};",
                null);

        passwordOnChange = new OnChange(null, LoginTemplate.this,
                "return {fieldName:'password', fieldValue:source.value};",
                null);
        
        loginButtonOnClick = new OnClick("return true;", LoginTemplate.this,
                "return {fieldName:'loginButton', username:document.getElementById('usernameId').value, password:document.getElementById('passwordId').value};",
                " if(jsObject == null) {return;} var status = jsObject.status; if (status === false) {alert(jsObject.statusMessage);}");
        

        form = new Form(this, new Target("https://webfirmframework.github.io"),
                new OnSubmit(
                        "event.preventDefault();/*to avoid form sumbission to target url*/ return true;/*will allow to invoke server side method*/",
                        this, null, null)) {
            {
                new NoTag(this, "Username : ");

                new Input(this, new Id("usernameId"), new Type(Type.TEXT),
                        usernameOnChange);

                msgSpan = new Span(this);

                new Br(this);
                new Br(this);

                new NoTag(this, "Password : ");

                new Input(this, new Id("passwordId"), passwordOnKeyUp,
                        new Type(Type.PASSWORD),
                        passwordOnChange);

                new Br(this);
                new Br(this);

                Input loginButton = new Input(this, new Type(Type.SUBMIT),
                        new Value("Login"),
                        loginButtonOnClick);

                loginButton.addAttributes(getStyleToCenter());

            }
        };

    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        LOGGER.info("asyncMethod");

        displayInServerLogPage("asyncMethod");

        if (wffBMObject != null) {
            
            if (usernameOnChange.equals(event.getSourceAttribute())) {

                username = (String) wffBMObject.getValue("fieldValue");
                LOGGER.info("username " + username);

                displayInServerLogPage("username " + username);

                validateLength(wffBMObject);

            } else if (passwordOnChange.equals(event.getSourceAttribute())) {

                password = (String) wffBMObject.getValue("fieldValue");
                LOGGER.info("password onchange " + password);
                displayInServerLogPage("password onchange " + password);

            } else if (loginButtonOnClick.equals(event.getSourceAttribute())
                    || form.equals(event.getSourceTag())) {

                username = (String) wffBMObject.getValue("username");
                password = (String) wffBMObject.getValue("password");

                // check in the db if the username and password is write
                if ("demo".equals(username) && "demo".equals(password)) {

                    LOGGER.info("login success");
                    displayInServerLogPage("login success");
                    // parent.addInnerHtml(new NoTag(null, "login success"));
                    DashboardLayout dashboard = new DashboardLayout(
                            documentModel);
                    documentModel.getBodyDiv().addInnerHtml(dashboard);

                } else {

                    WffBMObject result = new WffBMObject();
                    result.put("status", BMValueType.BOOLEAN, false);
                    result.put("statusMessage", BMValueType.STRING,
                            "Username or password is incorrect");

                    return result;
                }

            } else if (passwordOnKeyUp.equals(event.getSourceAttribute())) {
                validateLength(wffBMObject);
                LOGGER.info("password onchange " + "password onkeyup " + wffBMObject.getValue("fieldValue"));
                displayInServerLogPage("password onkeyup " + wffBMObject.getValue("fieldValue"));
            }
        }

        return null;
    }

    private void displayInServerLogPage(String msg) {
        Object serverLogPageInstanceId = documentModel.getHttpSession()
                .getAttribute("serverLogPageInstanceId");
        if (serverLogPageInstanceId != null) {
            ServerLogPage serverLogPage = (ServerLogPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(serverLogPageInstanceId.toString());
            if (serverLogPage != null) {
                serverLogPage.log(msg);
            }
        }
    }

}
