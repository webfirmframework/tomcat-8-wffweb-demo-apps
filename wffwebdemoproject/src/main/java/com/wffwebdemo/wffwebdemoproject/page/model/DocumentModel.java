package com.wffwebdemo.wffwebdemoproject.page.model;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.TitleTag;

public class DocumentModel {

    private Body body;

    private TitleTag pageTitle;

    public DocumentModel() {
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public TitleTag getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(TitleTag pageTitle) {
        this.pageTitle = pageTitle;
    }

}
