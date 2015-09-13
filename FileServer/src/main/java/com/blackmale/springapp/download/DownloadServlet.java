package com.blackmale.springapp.download;

import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.annotation.WebServlet;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author kuldeep
 */

@WebServlet(urlPatterns = "/download/*", name="downloadServletHandler", asyncSupported = true)
@PropertySource("classpath:server.properties")
public class DownloadServlet extends HttpRequestHandlerServlet {

}
