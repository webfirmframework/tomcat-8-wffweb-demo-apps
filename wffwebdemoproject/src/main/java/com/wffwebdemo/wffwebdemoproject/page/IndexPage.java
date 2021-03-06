package com.wffwebdemo.wffwebdemoproject.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage implements Threaded {

    private static final long serialVersionUID = 1L;
    private final HttpSession httpSession;
    private IndexPageLayout indexPageLayout;

    @Override
    public String webSocketUrl() {
        return "ws://localhost:8080/wffwebdemoproject/ws-for-index-page";
    }

    public IndexPage(final HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public AbstractHtml render() {
        
        //it must be set to false if there is a anchor tag which opens new tab/window
        //otherwise the browserPage will be removed from the BrowserPageContext
        //when clicking on the link
        super.removeFromContext(false, BrowserPage.On.INIT_REMOVE_PREVIOUS);
        
        
        super.addServerMethod("testServerMethod", new CustomServerMethod());

        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession, this);
        return indexPageLayout;
    }

    private List<Thread> allActiveThreads;

    @Override
    public void startAllThreads() {

        if (allActiveThreads != null) {
            return;
        }

        allActiveThreads = new ArrayList<Thread>();
        final List<Runnable> allThreads = indexPageLayout.getAllThreads();
        for (final Runnable runnable : allThreads) {
            final Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.start();
            allActiveThreads.add(thread);
        }

    }

    @Override
    public void stopAllThreads() {

        if (allActiveThreads != null) {
            for (final Thread thread : allActiveThreads) {
                thread.interrupt();
            }
            allActiveThreads = null;
        }
    }

    @Override
    protected void removedFromContext() {
        super.removedFromContext();
        stopAllThreads();
    }
}
