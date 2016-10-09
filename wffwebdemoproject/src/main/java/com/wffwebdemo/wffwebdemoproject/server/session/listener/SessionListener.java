package com.wffwebdemo.wffwebdemoproject.server.session.listener;

import java.util.logging.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.wffwebdemo.wffwebdemoproject.page.IndexPage;

@WebListener
public class SessionListener implements HttpSessionListener {

    private static final Logger LOGGER = Logger
            .getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        LOGGER.info("SessionListener.sessionCreated()");
        // NOP
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        LOGGER.info("SessionListener.sessionDestroyed()");

        HttpSession session = sessionEvent.getSession();
        Object attrValue = session.getAttribute("indexPageInstanceId");

        if (attrValue != null) {
            String indexPageInstanceId = attrValue.toString();
            IndexPage indexPage = (IndexPage) BrowserPageContext.INSTANCE
                    .getBrowserPage(indexPageInstanceId);

            if (indexPage != null) {
                indexPage.stopAllThreads();
            }

        }

        BrowserPageContext.INSTANCE.httpSessionClosed(session.getId());
    }

}
