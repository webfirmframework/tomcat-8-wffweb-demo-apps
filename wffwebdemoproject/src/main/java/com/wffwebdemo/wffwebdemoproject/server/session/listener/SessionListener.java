package com.wffwebdemo.wffwebdemoproject.server.session.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        System.out.println("SessionListener.sessionCreated()");
        // NOP
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        System.out.println("SessionListener.sessionDestroyed()");
        BrowserPageContext.INSTANCE
                .httpSessionClosed(sessionEvent.getSession().getId());
    }

}
