package com.wffwebdemo.wffwebdemoproject.page.layout;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import com.wffwebdemo.wffwebdemoproject.page.model.DocumentModel;
import com.wffwebdemo.wffwebdemoproject.page.template.LoginTemplate;

public class IndexPageLayout extends Html {

    private static final long serialVersionUID = 1L;

    private TitleTag pageTitle;

    public IndexPageLayout() {
        super(null);
        super.setPrependDocType(true);
        develop();
    }

    @SuppressWarnings("serial")
    private void develop() {

        new Head(this) {
            {
                pageTitle = new TitleTag(this) {
                    {
                        new NoTag(this);
                    }
                };
            }
        };

        new Body(this, new Style("background:lightgray")) {

            {
                DocumentModel documentModel = new DocumentModel();
                documentModel.setBody(this);
                documentModel.setPageTitle(pageTitle);
                appendChild(new LoginTemplate(documentModel));
            }

        };

    }

}
