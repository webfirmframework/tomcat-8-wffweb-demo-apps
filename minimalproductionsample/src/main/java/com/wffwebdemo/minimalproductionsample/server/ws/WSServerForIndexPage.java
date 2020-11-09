package com.wffwebdemo.minimalproductionsample.server.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.webfirmframework.wffweb.PushFailedException;
import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.server.page.HeartbeatManager;
import com.webfirmframework.wffweb.server.page.PayloadProcessor;
import com.webfirmframework.wffweb.server.page.action.BrowserPageAction;
import com.webfirmframework.wffweb.util.ByteBufferUtil;
import com.wffwebdemo.minimalproductionsample.AppSettings;
import com.wffwebdemo.minimalproductionsample.page.IndexPage;
import com.wffwebdemo.minimalproductionsample.page.model.DocumentModel;
import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;

/**
 * @ServerEndpoint gives the relative name for the end point This will be
 *                 accessed via
 *                 ws://localhost:8080/minimalproductionsample/ws-for-index-page
 */
@ServerEndpoint(value = ServerConstants.INDEX_PAGE_WS_URI, configurator = WSServerForIndexPage.class)
@WebListener
public class WSServerForIndexPage extends Configurator
        implements ServletRequestListener {

    private static final Logger LOGGER = Logger
            .getLogger(WSServerForIndexPage.class.getName());

    private BrowserPage browserPage;

    private HttpSession httpSession;

    private PayloadProcessor payloadProcessor;

    private static final long HTTP_SESSION_HEARTBEAT_INTERVAL = ServerConstants.SESSION_TIMEOUT_MILLISECONDS
            - (1000 * 60 * 2);
    
    private volatile HeartbeatManager heartbeatManager;

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
            HandshakeRequest request, HandshakeResponse response) {

        final Map<String, List<String>> parameterMap = request
                .getParameterMap();

        HttpSession httpSession = null;

        List<String> wffInstanceIds = parameterMap
                .get(BrowserPage.WFF_INSTANCE_ID);
        String instanceId = wffInstanceIds.get(0);
        browserPage = BrowserPageContext.INSTANCE.webSocketOpened(instanceId);
        DocumentModel documentModel = null;

        if (browserPage instanceof IndexPage) {
            IndexPage indexPage = (IndexPage) browserPage;
            documentModel = indexPage.getDocumentModel();
            httpSession = documentModel.getHttpSession();
        }

        super.modifyHandshake(config, request, response);

        // in a worst case if the httpSession is null
        if (httpSession == null) {
            LOGGER.info(
                    "httpSession == null after modifyHandshake so httpSession = (HttpSession) request.getHttpSession()");
            httpSession = (HttpSession) request.getHttpSession();
        }

        if (httpSession == null) {
            LOGGER.info("httpSession == null");
            return;
        }

        config.getUserProperties().put("httpSession", httpSession);
        LOGGER.info("modifyHandshake " + httpSession.getId());

    }

    /**
     * @OnOpen allows us to intercept the creation of a new session. The session
     *         class allows us to send data to the user. In the method onOpen,
     *         we'll let the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(final Session session, EndpointConfig config) {

        LOGGER.info("onOpen");

        session.setMaxIdleTimeout(ServerConstants.SESSION_TIMEOUT_MILLISECONDS);

        httpSession = (HttpSession) config.getUserProperties()
                .get("httpSession");

        if (httpSession != null) {

            LOGGER.info("websocket session id " + session.getId()
                    + " has opened a connection for httpsession id "
                    + httpSession.getId());

            Object totalCons = httpSession.getAttribute("totalConnections");

            int totalConnections = 0;

            if (totalCons != null) {
                totalConnections = (int) totalCons;
            }

            totalConnections++;
            httpSession.setAttribute("totalConnections", totalConnections);

            // never to close the session on inactivity
            httpSession.setMaxInactiveInterval(-1);
            LOGGER.info("httpSession.setMaxInactiveInterval(-1)");
            
            final HeartbeatManager hbm = HeartbeatRunnable.HEARTBEAT_MANAGER_MAP.computeIfAbsent(httpSession.getId(),
                    k -> new HeartbeatManager(AppSettings.CACHED_THREAD_POOL,
                            HTTP_SESSION_HEARTBEAT_INTERVAL, new HeartbeatRunnable(httpSession.getId())));
            heartbeatManager = hbm;
            hbm.runAsync();
        }

        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get("wffInstanceId");

        String instanceId = wffInstanceIds.get(0);

        browserPage = BrowserPageContext.INSTANCE.webSocketOpened(instanceId);

        if (browserPage == null) {

            try {
                // or refresh the browser
                session.getBasicRemote().sendBinary(
                        BrowserPageAction.RELOAD.getActionByteBuffer());
                session.close();
                return;
            } catch (IOException e) {
                // NOP
            }

        }

        // Internally it may contain a volatile variable
        // so it's better to declare a dedicated variable before
        // addWebSocketPushListener.
        // If the maxBinaryMessageBufferSize is changed dynamically
        // then call getMaxBinaryMessageBufferSize method directly in
        // sliceIfRequired method as second argument.
        final int maxBinaryMessageBufferSize = session
                .getMaxBinaryMessageBufferSize();
        
        // NB: do not use browserPage.getPayloadProcessor it has bug
        // payloadProcessor = browserPage.getPayloadProcessor();
        payloadProcessor = new PayloadProcessor(browserPage);

        browserPage.addWebSocketPushListener(session.getId(), data -> {

            ByteBufferUtil.sliceIfRequired(data, maxBinaryMessageBufferSize,
                    (part, last) -> {

                        try {
                            session.getBasicRemote().sendBinary(part, last);
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE,
                                    "IOException while session.getBasicRemote().sendBinary(part, last)",
                                    e);
                            try {
                                session.close();
                            } catch (IOException e1) {
                                LOGGER.log(Level.SEVERE,
                                        "IOException while session.close()",
                                        e1);
                            }
                            throw new PushFailedException(e.getMessage(), e);
                        }

                        return !last;
                    });
        });

    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(ByteBuffer message, boolean last, Session session) {

        payloadProcessor.webSocketMessaged(message, last);

        if (last && message.capacity() == 0) {
            LOGGER.info("client ping message.length == 0");
            if (httpSession != null) {
                LOGGER.info("going to start httpsession hearbeat");
                HeartbeatManager hbm = heartbeatManager;
                if (hbm != null) {
                    hbm.runAsync();
                }
            }
        }
    }

    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        LOGGER.info("onClose");

        // how much time you want client for inactivity
        // may be it could be the same value given for
        // session timeout in web.xml file.
        // it's valid only when the browser is closed
        // because client will be trying to reconnect.
        // The value is in seconds.
        if (httpSession != null) {

            Object totalCons = httpSession.getAttribute("totalConnections");

            int totalConnections = 0;

            if (totalCons != null) {
                totalConnections = (int) totalCons;
                totalConnections--;
            }

            httpSession.setAttribute("totalConnections", totalConnections);

            if (totalConnections == 0) {
                httpSession.setMaxInactiveInterval(
                        ServerConstants.SESSION_TIMEOUT_SECONDS);
                HeartbeatManager hbm = heartbeatManager;
                if (hbm != null) {
                    hbm.runAsync();
                }
            }

            LOGGER.info("httpSession.setMaxInactiveInterval(60 * 30)");
        }

        LOGGER.info("Session " + session.getId() + " closed");
        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get("wffInstanceId");

        String instanceId = wffInstanceIds.get(0);
        BrowserPageContext.INSTANCE.webSocketClosed(instanceId,
                session.getId());
        payloadProcessor = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // NOP
        // log only if required
        // if (LOGGER.isLoggable(Level.WARNING)) {
        // LOGGER.log(Level.WARNING, throwable.getMessage());
        // }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        httpSession = ((HttpServletRequest) sre.getServletRequest())
                .getSession();
        LOGGER.info("requestDestroyed httpSession " + httpSession);

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        httpSession = ((HttpServletRequest) sre.getServletRequest())
                .getSession();
        LOGGER.info("requestInitialized httpSession " + httpSession);
    }
}
