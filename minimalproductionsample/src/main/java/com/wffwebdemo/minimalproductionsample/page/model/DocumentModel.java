package com.wffwebdemo.minimalproductionsample.page.model;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;

public class DocumentModel {

    private HttpSession httpSession;

    private BrowserPage browserPage;

    public HttpSession getHttpSession() {
        return httpSession;
    }

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
