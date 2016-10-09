package com.wffwebdemo.wffwebdemoproject.page.template.users;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.tag.html.Br;
import com.webfirmframework.wffweb.tag.html.attribute.Target;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnChange;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.event.mouse.OnClick;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formatting.B;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;

@SuppressWarnings("serial")
public class RegisterUserTempate extends Div implements ServerAsyncMethod {

    private static final Logger LOGGER = Logger
            .getLogger(RegisterUserTempate.class.getName());

    @SuppressWarnings("unused")
    private DocumentModel documentModel;

    private Button registerButton;

    public RegisterUserTempate(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;

        develop();
    }

    private void develop() {

        new Form(this, new Target("https://webfirmframework.github.io"),
                new OnSubmit(
                        "event.preventDefault(); /*to avoid form sumbission to target url*/ "
                                + "return true; /*will allow to invoke server side method*/",
                        this, null, null)) {
            {
                new NoTag(this, "Username : ");

                new Input(this, new Id("usernameId"), new Type(Type.TEXT),
                        new OnChange(
                                "/*client side validation may be done here*/ return true;",
                                RegisterUserTempate.this,
                                "return {fieldId:'usernameId', fieldValue:source.value};",
                                null));

                new Input(this, new Id("ageId"), new Type(Type.NUMBER));
            }
        };

        new Br(this);
        new Br(this);

        registerButton = new Button(this,
                new OnClick(null, RegisterUserTempate.this,
                        "return {fieldName:'regButton', "
                                + "username:document.getElementById('usernameId').value, "
                                + "ageId: parseInt(document.getElementById('ageId').value)}; ",
                        null)) {
            {
                new B(this) {
                    {
                        new NoTag(this, "Register");
                    }
                };
            }
        };

    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {

        if (registerButton.equals(event.getSourceTag())) {

            LOGGER.info("age type " + wffBMObject.getValueType("ageId"));
            LOGGER.info("age " + wffBMObject.getValue("ageId"));

        }

        return null;
    }

}
