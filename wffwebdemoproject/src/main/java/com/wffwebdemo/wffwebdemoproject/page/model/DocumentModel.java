package com.wffwebdemo.wffwebdemoproject.page.model;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

public class DocumentModel {

    private Div bodyDiv;

    private TitleTag pageTitle;

    private HttpSession httpSession;

    public DocumentModel() {
    }

    public Div getBodyDiv() {
        return bodyDiv;
    }

    public void setBodyDiv(Div bodyDiv) {
        this.bodyDiv = bodyDiv;
    }

    public TitleTag getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(TitleTag pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * @return the httpSession
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * @param httpSession
     *            the httpSession to set
     */
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

}
