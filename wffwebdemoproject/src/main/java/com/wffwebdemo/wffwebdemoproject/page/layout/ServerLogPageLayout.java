package com.wffwebdemo.wffwebdemoproject.page.layout;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.metainfo.Head;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;

@SuppressWarnings("serial")
public class ServerLogPageLayout extends Html {

    private Body body;

    public ServerLogPageLayout() {
        super(null);
        super.setPrependDocType(true);
        develop();
    }

    private void develop() {

        new Head(this) {
            {
                new TitleTag(this) {
                    {
                        new NoTag(this, "Server Log");
                    }
                };
            }
        };

        body = new Body(this, new Style("background:white"));

    }

    
    public void log(final String msg) {
        Div logDiv = new Div(null) {
            {
                new NoTag(this, msg);
            }
        };
        if (body.getChildren().size() > 25) {
            body.addInnerHtml(logDiv);
        } else {
            body.appendChild(logDiv);
        }
    }

}
