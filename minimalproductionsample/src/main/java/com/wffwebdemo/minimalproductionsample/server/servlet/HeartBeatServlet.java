package com.wffwebdemo.minimalproductionsample.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * heart beat servlet
 */
@WebServlet({ "/heart-beat" })
public class HeartBeatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger
            .getLogger(HeartBeatServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HeartBeatServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        LOGGER.info("heat-beat request received");

        response.setContentType("text/html;charset=utf-8");

        try (OutputStream os = response.getOutputStream();) {

            request.getSession(false);
            os.write(new byte[0]);
            os.flush();
        }

    }

}
