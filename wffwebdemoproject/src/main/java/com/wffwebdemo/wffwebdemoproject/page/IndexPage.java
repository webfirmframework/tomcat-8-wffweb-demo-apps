package com.wffwebdemo.wffwebdemoproject.page;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.attribute.event.ServerAsyncMethod;
import com.webfirmframework.wffweb.wffbm.data.BMValueType;
import com.webfirmframework.wffweb.wffbm.data.WffBMObject;
import com.wffwebdemo.wffwebdemoproject.page.layout.IndexPageLayout;

public class IndexPage extends BrowserPage implements Threaded, ServerAsyncMethod {

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
        
        super.addServerMethod("testServerMethod", this);

        // here we should return the object IndexPageLayout

        indexPageLayout = new IndexPageLayout(httpSession);
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
    public WffBMObject asyncMethod(WffBMObject wffBMObject, Event event) {
        
        System.out.println(
                "serverAsyncMethod invoked " + event.getServerMethodName()+" wffBMObject "+wffBMObject);
        if (wffBMObject != null) {
            System.out.println("wffBMObject "+wffBMObject.getValue("somekey"));
        }
        
        WffBMObject bmObject = new WffBMObject();
        bmObject.put("serverKey", BMValueType.STRING, "server value");
        
        return bmObject;
    }

}
