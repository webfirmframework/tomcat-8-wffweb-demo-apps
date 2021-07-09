package com.wffwebdemo.minimalproductionsample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;

public class AppSettings {
    
    public static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();
    
    static {
    	BrowserPageContext.INSTANCE.enableAutoClean(ServerConstants.SESSION_TIMEOUT_MILLISECONDS, CACHED_THREAD_POOL);
    }
    
}
