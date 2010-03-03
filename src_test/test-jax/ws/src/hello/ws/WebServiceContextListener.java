package hello.ws;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebServiceContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {

        // disable JAX-WS capture stack trace in SOAP fault messages
        Properties sysProps = System.getProperties();
        sysProps.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");

        }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
