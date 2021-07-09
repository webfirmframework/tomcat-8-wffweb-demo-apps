package com.wffwebdemo.minimalproductionsample.page;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.minimalproductionsample.AppSettings;
import com.wffwebdemo.minimalproductionsample.page.layout.IndexPageLayout;
import com.wffwebdemo.minimalproductionsample.page.model.DocumentModel;
import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;

public class IndexPage extends BrowserPage {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger
            .getLogger(IndexPage.class.getName());

    // this is a standard interval
    public static final int HEARTBEAT_TIME_MILLISECONDS = 25000;

    private static final int WS_RECONNECT_TIME = 1000;

    private DocumentModel documentModel;

    public IndexPage(DocumentModel documentModel) {
        this.documentModel = documentModel;
    }

    @Override
    public String webSocketUrl() {
        return ServerConstants.DOMAIN_WS_URL.concat(ServerConstants.CONTEXT_PATH
                .concat(ServerConstants.INDEX_PAGE_WS_URI));
    }

    // this is new since 3.0.1
    @Override
    protected void beforeRender() {
        // Write BrowserPage configurations code here.
        // The following code can also be written inside render() method but
        // writing here will be a nice separation of concern.
        super.setWebSocketHeartbeatInterval(HEARTBEAT_TIME_MILLISECONDS);
        super.setWebSocketReconnectInterval(WS_RECONNECT_TIME);
        super.setExecutor(AppSettings.CACHED_THREAD_POOL);
    }

    @Override
    public AbstractHtml render() {
        documentModel.setBrowserPage(this);
        // Here you can return layout template based on condition, Eg if the
        // user is logged in then return DashboardPageLayout otherwise
        // return LoginPageLayout
        return new IndexPageLayout(documentModel);
    }

    // this is new since 3.0.18
    @Override
    protected void onInitialClientPing(AbstractHtml rootTag) {
        IndexPageLayout layout = (IndexPageLayout) rootTag;
        //to build main div tags only if there is a client communication
        layout.buildMainDivTags();
    }

    public DocumentModel getDocumentModel() {
        return documentModel;
    }

}
