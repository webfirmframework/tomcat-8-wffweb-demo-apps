package com.wffwebdemo.wffwebdemoproject.page.template;

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
import com.wffwebdemo.wffwebdemoproject.page.layout.DashboardLayout;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class LoginTemplate extends Div implements ServerAsyncMethod {

    private String username;

    private String password;

    private DocumentModel documentModel;

    private Span msgSpan;

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
    private OnKeyUp lengthValidation = new OnKeyUp(null,
            new ServerAsyncMethod() {

                @Override
                public WffBMObject asyncMethod(WffBMObject wffBMObject,
                        Event event) {
                    if (wffBMObject != null) {
                        String fieldId = (String) wffBMObject
                                .getValue("fieldId");

                        if ("passwordId".equals(fieldId)) {
                            String fieldValue = (String) wffBMObject
                                    .getValue("fieldValue");

                            if (fieldValue.length() < 4) {
                                msgSpan.addInnerHtml(
                                        new NoTag(null, "Required min 4"));
                            } else {
                                msgSpan.removeAllChildren();
                            }

                        }
                    }

                    return null;
                }
            }, "return {fieldId:source.id, fieldValue:source.value};", null);

    private Form form;

    private void develop() {

        documentModel.getPageTitle().addInnerHtml(new NoTag(null, "Login"));

        form = new Form(this, new Target("https://webfirmframework.github.io"),
                new OnSubmit(
                        "event.preventDefault();/*to avoid form sumbission to target url*/ return true;/*will allow to invoke server side method*/",
                        this, null, null)) {
            {
                new NoTag(this, "Username : ");

                new Input(this, new Id("usernameId"), new Type(Type.TEXT),
                        new OnChange(
                                "/*client side validation may be done here*/ return true;",
                                LoginTemplate.this,
                                "return {fieldName:'username', fieldValue:source.value};",
                                null));

                msgSpan = new Span(this);

                new Br(this);
                new Br(this);

                new NoTag(this, "Password : ");

                new Input(this, new Id("passwordId"), lengthValidation,
                        new Type(Type.PASSWORD),
                        new OnChange(null, LoginTemplate.this,
                                "return {fieldName:'password', fieldValue:source.value};",
                                null));

                new Br(this);
                new Br(this);

                Input loginButton = new Input(this, new Type(Type.SUBMIT),
                        new Value("Login"),
                        new OnClick("return true;", LoginTemplate.this,
                                "return {fieldName:'loginButton', username:document.getElementById('usernameId').value, password:document.getElementById('passwordId').value};",
                                " if(jsObject == null) {return;} var status = jsObject.status; if (status === false) {alert(jsObject.statusMessage);}"));

                loginButton.addAttributes(getStyleToCenter());

            }
        };

    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        System.out.println("asyncMethod");

        if (wffBMObject != null) {
            System.out
                    .println("fieldName " + wffBMObject.getValue("fieldName"));
            if ("username".equals(wffBMObject.getValue("fieldName"))) {

                username = (String) wffBMObject.getValue("fieldValue");
                System.out.println("username " + username);

                if (username.length() < 4) {
                    msgSpan.addInnerHtml(
                            new NoTag(null, "Minimum 4 letters required"));
                } else {
                    msgSpan.removeAllChildren();
                }

            } else if ("password".equals(wffBMObject.getValue("fieldName"))) {

                password = (String) wffBMObject.getValue("fieldValue");
                System.out.println("password " + password);

            } else if ("loginButton".equals(wffBMObject.getValue("fieldName"))
                    || event.getSourceTag().equals(form)) {

                username = (String) wffBMObject.getValue("username");
                password = (String) wffBMObject.getValue("password");

                // check in the db if the username and password is write
                if ("demo".equals(username) && "demo".equals(password)) {

                    System.out.println("login success");
                    // parent.addInnerHtml(new NoTag(null, "login success"));
                    DashboardLayout dashboard = new DashboardLayout(
                            documentModel);
                    documentModel.getBody().addInnerHtml(dashboard);

                } else {

                    WffBMObject result = new WffBMObject();
                    result.put("status", BMValueType.BOOLEAN, false);
                    result.put("statusMessage", BMValueType.STRING,
                            "Username or password is incorrect");

                    return result;
                }

            }
        }

        return null;
    }

}
