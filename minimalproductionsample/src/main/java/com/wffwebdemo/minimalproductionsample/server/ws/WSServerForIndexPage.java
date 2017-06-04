package com.wffwebdemo.minimalproductionsample.server.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
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
import com.webfirmframework.wffweb.server.page.WebSocketPushListener;
import com.webfirmframework.wffweb.server.page.action.BrowserPageAction;
import com.wffwebdemo.minimalproductionsample.server.constants.ServerConstants;
import com.wffwebdemo.minimalproductionsample.server.util.HeartBeatUtil;

/**
 * @ServerEndpoint gives the relative name for the end point This will be
 *                 accessed via
 *                 ws://localhost:8080/wffwebdemoproject/ws-for-wffweb.
 */
@ServerEndpoint(value = ServerConstants.INDEX_PAGE_WS_URI, configurator = WSServerForIndexPage.class)
@WebListener
public class WSServerForIndexPage extends Configurator
        implements ServletRequestListener {

    private static final Logger LOGGER = Logger
            .getLogger(WSServerForIndexPage.class.getName());

    private BrowserPage browserPage;

    private HttpSession httpSession;

    private long lastHeartbeatTime;

    private static final long HTTP_SESSION_HEARTBEAT_INVTERVAL = ServerConstants.SESSION_TIMEOUT_MILLISECONDS
            - (1000 * 60 * 2);

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
            HandshakeRequest request, HandshakeResponse response) {

        HttpSession httpSession = (HttpSession) request.getHttpSession();

        super.modifyHandshake(config, request, response);

        if (httpSession == null) {
            LOGGER.info("httpSession == null after modifyHandshake");
            httpSession = (HttpSession) request.getHttpSession();
        }

        if (httpSession == null) {
            LOGGER.info("httpSession == null");
            return;
        }

        config.getUserProperties().put("httpSession", httpSession);

        httpSession = (HttpSession) request.getHttpSession();
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
            HeartBeatUtil.ping(httpSession.getId());
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

        browserPage.addWebSocketPushListener(session.getId(),
                new WebSocketPushListener() {

                    @Override
                    public synchronized void push(ByteBuffer data) {
                        try {
                            session.getBasicRemote().sendBinary(data);
                        } catch (Throwable e) {
                            throw new PushFailedException(e.getMessage(), e);
                        }
                    }
                });

    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     */
    @OnMessage
    public void onMessage(byte[] message, Session session) {

        browserPage.webSocketMessaged(message);

        if (message.length == 0) {
            LOGGER.info("client ping message.length == 0");
            if (httpSession != null
                    && HTTP_SESSION_HEARTBEAT_INVTERVAL < (System
                            .currentTimeMillis() - lastHeartbeatTime)) {
                LOGGER.info("going to start httpsession hearbeat");
                HeartBeatUtil.ping(httpSession.getId());
                lastHeartbeatTime = System.currentTimeMillis();
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
                HeartBeatUtil.ping(httpSession.getId());
            }

            LOGGER.info("httpSession.setMaxInactiveInterval(60 * 30)");
        }

        LOGGER.info("Session " + session.getId() + " closed");
        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get("wffInstanceId");

        String instanceId = wffInstanceIds.get(0);
        BrowserPageContext.INSTANCE.webSocketClosed(instanceId,
                session.getId());
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
