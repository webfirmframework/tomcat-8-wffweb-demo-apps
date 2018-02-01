package com.wffwebdemo.minimalproductionsample.server.constants;

public final class ServerConstants {

    public static final String DOMAIN_URL;

    public static final String DOMAIN_WS_URL;

    public static final String ORIGIN_DOMAIN_URL;

    public static final int SESSION_TIMEOUT;

    public static final int SESSION_TIMEOUT_SECONDS;

    public static final int SESSION_TIMEOUT_MILLISECONDS;

    private static final String LOCAL_MACHINE_IP = "localhost";

    public static final String LOCAL_MACHINE_PORT;

    public static final String INDEX_PAGE_WS_URI = "/ws-for-index-page";

    /**
     * must be null initially
     */
    public static String CONTEXT_PATH = null;

    static {

        String domainUrlFromEnv = System.getenv("DOMAIN_URL");
        String domainWsUrlFromEnv = System.getenv("DOMAIN_WS_URL");
        String originDomainUrlFromEnv = System.getenv("ORIGIN_DOMAIN_URL");
        String sessionTimeoutFromEnv = System.getenv("SESSION_TIMEOUT");

        String webPort = System.getenv("PORT");
        LOCAL_MACHINE_PORT = webPort != null && !webPort.isEmpty() ? webPort
                : "8080";

        DOMAIN_URL = domainUrlFromEnv != null ? domainUrlFromEnv
                : "http://" + LOCAL_MACHINE_IP + ":" + LOCAL_MACHINE_PORT;

        DOMAIN_WS_URL = domainWsUrlFromEnv != null ? domainWsUrlFromEnv
                : "ws://" + LOCAL_MACHINE_IP + ":" + LOCAL_MACHINE_PORT;

        ORIGIN_DOMAIN_URL = originDomainUrlFromEnv != null
                ? originDomainUrlFromEnv
                : "http://" + LOCAL_MACHINE_IP + ":" + LOCAL_MACHINE_PORT;

        SESSION_TIMEOUT = sessionTimeoutFromEnv != null
                ? Integer.parseInt(sessionTimeoutFromEnv) : 5;

        SESSION_TIMEOUT_MILLISECONDS = SESSION_TIMEOUT * 1000 * 60;
        SESSION_TIMEOUT_SECONDS = SESSION_TIMEOUT * 60;
    }

}
