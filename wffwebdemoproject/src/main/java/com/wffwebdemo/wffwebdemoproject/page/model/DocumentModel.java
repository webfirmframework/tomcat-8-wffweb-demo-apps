package com.wffwebdemo.wffwebdemoproject.page.model;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.TitleTag;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;

public class DocumentModel {

    private Div bodyDiv;

    private TitleTag pageTitle;

    private HttpSession httpSession;
    
    private BrowserPage browserPage;

    public DocumentModel(BrowserPage browserPage) {
        this.browserPage = browserPage;
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

    public BrowserPage getBrowserPage() {
        return browserPage;
    }

    public void setBrowserPage(BrowserPage browserPage) {
        this.browserPage = browserPage;
    }

}
