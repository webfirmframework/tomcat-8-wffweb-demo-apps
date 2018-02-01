package com.wffwebdemo.minimalproductionsample.page.template;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.H2;
import com.webfirmframework.wffweb.tag.html.attribute.For;
import com.webfirmframework.wffweb.tag.html.attribute.Name;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.tag.html.attribute.event.form.OnSubmit;
import com.webfirmframework.wffweb.tag.html.attribute.global.ClassAttribute;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Button;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Form;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Input;
import com.webfirmframework.wffweb.tag.html.formsandinputs.Label;
import com.webfirmframework.wffweb.tag.html.html5.attribute.Placeholder;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.webfirmframework.wffweb.tag.repository.TagRepository;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.minimalproductionsample.page.model.DocumentModel;

@SuppressWarnings("serial")
public class SampleTemplate2 extends Div implements ServerAsyncMethod {

    private DocumentModel documentModel;

    public SampleTemplate2(DocumentModel documentModel) {
        super(null);
        this.documentModel = documentModel;
        develop();
    }

    private void develop() {
        changeTitle();
        
        final ClassAttribute classAttribute10 = new ClassAttribute("form-group");
        final ClassAttribute classAttribute13 = new ClassAttribute("form-control");

        new H2(this) {{
            new NoTag(this, "Vertical (basic) form");
        }};
        new Form(this,
            new OnSubmit(this)) {{
            new Div(this,
                classAttribute10) {{
                new Label(this,
                    new For("email")) {{
                    new NoTag(this, "Email:");
                }};
                new Input(this,
                    new Type("email"),
                    classAttribute13,
                    new Id("email"),
                    new Placeholder("Enter email"),
                    new Name("email"));
            }};
            new Div(this,
                classAttribute10) {{
                new Label(this,
                    new For("pwd")) {{
                    new NoTag(this, "Password:");
                }};
                new Input(this,
                    new Type("password"),
                    classAttribute13,
                    new Id("pwd"),
                    new Placeholder("Enter password"),
                    new Name("pwd"));
            }};
            new Div(this,
                new ClassAttribute("checkbox")) {{
                new Label(this) {{
                    new Input(this,
                        new Type("checkbox"),
                        new Name("remember"));
                    new NoTag(this, " Remember me");
                }};
            }};
            new Button(this,
                new Type("submit"),
                new ClassAttribute("btn btn-default")) {{
                new NoTag(this, "Submit");
            }};
        }};
    }
    
    private void changeTitle() {
        // getTagRepository() will give object only if the browserPage.render is returned
        TagRepository tagRepository = documentModel.getBrowserPage().getTagRepository();
        if (tagRepository != null) {
            AbstractHtml title = tagRepository.findTagById("windowTitleId");
            if (title != null) {
                title.addInnerHtml(new NoTag(null, "SampleTemplate2"));
            } 
        }
    }

    @Override
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {
        this.insertBefore(new SampleTemplate1(documentModel));
        this.getParent().removeChild(this);
        
        return null;
    }
}
