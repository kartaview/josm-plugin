package org.openstreetmap.josm.plugins.openstreetcam.service;

import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.plugins.openstreetcam.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Logging;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Helps log INFO messages on client side. Each instance of this object will result in a new logger file.
 *
 * @author laura.dumitru on 24/09/2020.
 */
public class ClientLogger {

    private static final String LOGS = "/logs";
    private static final String OPENSTREETVIEW_PLUGIN = "/openstreetview-plugin";
    public static final String LOG_EXTENSION = ".log";
    private Logger logger = null;

    public ClientLogger(final String componentName) {
        try {
            final File logDir = new File(
                    Preferences.main().getPluginsDirectory() + "/" + GuiConfig.getInstance().getPluginShortName()
                            + LOGS);
            if (!logDir.exists() && !logDir.mkdirs()) {
                throw new IOException();
            } else {
                final FileHandler fileHandler = new FileHandler(
                        logDir.getPath() + OPENSTREETVIEW_PLUGIN + "-" + componentName + LocalDate.now().toString()
                                + LOG_EXTENSION, true);
                fileHandler.setFormatter(new SimpleFormatter());
                logger = Logger.getLogger(ClientLogger.class.getName() + componentName);
                logger.setUseParentHandlers(false);
                logger.addHandler(fileHandler);
                logger.setLevel(Level.INFO);
            }
        } catch (SecurityException | IOException e) {
            Logging.info(
                    "Failed to initialize client logger for openstreetview plugin with component: " + componentName, e);
        }
    }

    public void log(final String message, final Throwable e) {
        if (Objects.nonNull(logger)) {
            if (Objects.nonNull(e)) {
                logger.log(Level.INFO, message, e);
            } else {
                logger.log(Level.INFO, message);
            }
        }
    }
}