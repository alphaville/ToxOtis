package org.opentox.toxotis.core.html;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class GoogleAnalytics {

    private static Properties properties = null;
    public static final String GOOGLE_ANALYTICS = getGoogleAnalytics();
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GoogleAnalytics.class);

    static {
        try {
            loadDefaultProperties();
        } catch (ToxOtisException ex) {
            Logger.getLogger(GoogleAnalytics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getGAaccount() {
        return GOOGLE_ANALYTICS;
    }

    public static String getGAjs() {
        return "<script type=\"text/javascript\">\n"
                + "var _gaq = _gaq || [];\n"
                + "_gaq.push(['_setAccount', '" + getGAaccount() + "']);\n"
                + "_gaq.push(['_trackPageview']);\n"
                + "(function() {\n"
                + "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n"
                + "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n"
                + "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n"
                + "})();"
                + "</script>";
    }

    private static String getGoogleAnalytics() {
        if (properties == null) {
            try {
                loadDefaultProperties();
            } catch (ToxOtisException ex) {
                Logger.getLogger(GoogleAnalytics.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return properties.getProperty("google-analytics");
    }

    public static Properties loadDefaultProperties() throws ToxOtisException {
        java.io.InputStream inStr = null;
        try {
            properties = new Properties();
            inStr = GoogleAnalytics.class.getClassLoader().getResourceAsStream("toxotis.properties");
            properties.load(inStr);
            properties.setProperty("log4j.useDefaultFile", "true");
        } catch (final IOException ex) {
            ex.printStackTrace();
            logger.warn("IOException while trying to access configuration file.", ex);
        } finally {
            if (inStr != null) {
                try {
                    inStr.close();
                } catch (IOException ex) {
                    logger.error(null, ex);
                }
            }
            if (properties != null) {
                return properties;
            } else {
                String message = "Could not load the standard properties hence could not use the standard "
                        + "logger - Using the console instead!";
                throw new ToxOtisException(message);
            }
        }
    }
}
