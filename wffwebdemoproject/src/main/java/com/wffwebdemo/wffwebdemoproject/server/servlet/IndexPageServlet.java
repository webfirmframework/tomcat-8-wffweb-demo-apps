package com.wffwebdemo.wffwebdemoproject.server.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.webfirmframework.wffweb.server.page.BrowserPage;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.wffwebdemo.wffwebdemoproject.page.IndexPage;

/**
 * Servlet implementation class HomePageServlet
 */
@WebServlet("/index")
public class IndexPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexPageServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        try (OutputStream os = response.getOutputStream();) {

            HttpSession session = request.getSession();

            String instanceId = (String) session
                    .getAttribute("indexPageInstanceId");

            BrowserPage browserPage = null;
            if (instanceId != null) {

                browserPage = BrowserPageContext.INSTANCE
                        .getBrowserPage(instanceId);

                // if the server is restarted browserPage could be null here
                // so you could save this instance to db after addBrowserPage
                // method
                // and retried from db using browserPage.getInstanceId()

            }

            if (browserPage == null) {
                browserPage = new IndexPage();
                BrowserPageContext.INSTANCE.addBrowserPage(session.getId(),
                        browserPage);
                session.setAttribute("indexPageInstanceId",
                        browserPage.getInstanceId());
            }

            browserPage.toOutputStream(os, "UTF-8");
        }

    }

}
