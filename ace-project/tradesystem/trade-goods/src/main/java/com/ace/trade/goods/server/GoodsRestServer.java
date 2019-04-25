package com.ace.trade.goods.server;

import com.ace.trade.common.constants.TradeEnums;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class GoodsRestServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(TradeEnums.RestServerEnum.COUPON.getServerPort());
        ServletContextHandler springMvcHandler = new ServletContextHandler();
        springMvcHandler.setContextPath("/" + TradeEnums.RestServerEnum.COUPON.getContextPath());
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation("classpath:xml/spring-web-goods.xml");
        springMvcHandler.addEventListener(new ContextLoaderListener(context));
        springMvcHandler.addServlet(new ServletHolder(new DispatcherServlet(context)),"/*");
        server.setHandler(springMvcHandler);
        server.start();
        System.out.println(server.getState());
        server.join();
    }
}
