package server;


import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import sun.util.logging.PlatformLogger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Drew
 * Date: 12/4/13
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServletContextList implements ServletContextListener {
	//private static final Logger LOGGER = LoggerFactory.getLogger(ContextFinalizer.class);

    public void contextInitialized(ServletContextEvent sce) {
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        while(drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                DriverManager.deregisterDriver(d);
                //LOGGER.warn(String.format("Driver %s deregistered", d));
            } catch (SQLException ex) {
                //LOGGER.warn(String.format("Error deregistering driver %s", d), ex);
            }
        }
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            //logger.warn("SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
