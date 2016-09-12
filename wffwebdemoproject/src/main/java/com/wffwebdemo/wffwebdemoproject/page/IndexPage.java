package com.wffwebdemo.wffwebdemoproject.page;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;

    @Override
    public String webSocketUrl() {
        return "ws://localhost:8080/wffwebdemoproject/ws-for-index-page";
    }

    @Override
    public AbstractHtml render() {

        // here we should return the object IndexPageLayout

        return new IndexPageLayout();
    }

}
