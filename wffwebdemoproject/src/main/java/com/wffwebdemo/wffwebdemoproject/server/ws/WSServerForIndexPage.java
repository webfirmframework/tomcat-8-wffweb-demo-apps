package com.wffwebdemo.wffwebdemoproject.server.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnClose;
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

/**
 * @ServerEndpoint gives the relative name for the end point This will be
 *                 accessed via
 *                 ws://localhost:8080/wffwebdemoproject/ws-for-wffweb.
 */
@ServerEndpoint(value = "/ws-for-index-page", configurator = WSServerForIndexPage.class)
public class WSServerForIndexPage extends Configurator {

    private BrowserPage browserPage;

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
            HandshakeRequest request, HandshakeResponse response) {

        super.modifyHandshake(config, request, response);

        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("httpSession", httpSession);

        httpSession = (HttpSession) request.getHttpSession();
        System.out.println("modifyHandshake " + httpSession.getId());

    }

    /**
     * @OnOpen allows us to intercept the creation of a new session. The session
     *         class allows us to send data to the user. In the method onOpen,
     *         we'll let the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(final Session session, EndpointConfig config) {

        HttpSession httpSession = (HttpSession) config.getUserProperties()
                .get("httpSession");
        System.out.println("websocket session id " + session.getId()
                + " has opened a connection for httpsession id "
                + httpSession.getId());

        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get("wffInstanceId");

        String instanceId = wffInstanceIds.get(0);

        browserPage = BrowserPageContext.INSTANCE.getBrowserPage(instanceId);

        if (browserPage == null) {
            System.out.println("browserPage == null, read comments");

            // get browserPageBytes by instanceId, which is stored in
            // an external persistent storage like db.
            // try {
            // byte[] browserPageBytes = null;
            // ByteArrayInputStream bais = new
            // ByteArrayInputStream(browserPageBytes);
            // ObjectInputStream ois = new ObjectInputStream(bais);
            // browserPage = (BrowserPage) ois.readObject();
            // } catch (Exception e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            try {
                session.close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // get browserPage instance associated this this instance id
            // and add again, otherwise required to manually refresh the page
            // BrowserPageContext.INSTANCE.addBrowserPage(httpSession.getId(),
            // browserPage);

        } else {

            // try {
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // ObjectOutputStream oos = new ObjectOutputStream(baos);
            // oos.writeObject(browserPage);
            // byte[] browserPageBytes = baos.toByteArray();
            // save browserPageBytes to persistent storage like db so that
            // we can get it by instanceId

            // } catch (Exception e) {
            // e.printStackTrace();
            // }

            BrowserPageContext.INSTANCE.webSocketOpened(instanceId);
        }
        

        browserPage.setWebSocketPushListener(new WebSocketPushListener() {

            @Override
            public void push(byte[] message) {
                try {
                    session.getBasicRemote()
                            .sendBinary(ByteBuffer.wrap(message));
                    // asyncRemove will make exception if the click is made many
                    // times
                    // https://bz.apache.org/bugzilla/show_bug.cgi?id=56026
                    // session.getAsyncRemote().sendBinary(ByteBuffer.wrap(message));
                } catch (Exception e) {
                    e.printStackTrace();
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
        browserPage.websocketMessaged(message);
    }

    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " closed");
        List<String> wffInstanceIds = session.getRequestParameterMap()
                .get("wffInstanceId");

        String instanceId = wffInstanceIds.get(0);
        BrowserPageContext.INSTANCE.webSocketClosed(instanceId);
    }
}
