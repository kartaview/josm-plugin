package org.openstreetmap.josm.plugins.kartaview.service;

import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.tools.Logging;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static java.time.temporal.ChronoUnit.DAYS;


/**
 * Helps log INFO messages on client side. Each instance of this object will result in a new logger file.
 *
 * @author laura.dumitru on 24/09/2020.
 */
public class ClientLogger {

    public static final String SLASH = "/";
    private static final String LOG_DIRECTORY_PATH =
            Preferences.main().getPluginsDirectory() + SLASH + GuiConfig.getInstance().getPluginShortName() + "/logs";
    private static final String OPENSTREETVIEW_PLUGIN = "openstreetview_plugin";
    private static final String LOG_EXTENSION = ".log";
    private static final String UNDERSCORE = "_";
    private static final int DATE_INDEX = 3;
    private static final String ANY_CHARACTERS = ".*";
    private Logger logger = null;

    public ClientLogger(final String componentName) {
        if (Config.getInstance().isDebugLoggingEnabled()) {
            try {
                final File logDir = new File(LOG_DIRECTORY_PATH);
                if (!logDir.exists() && !logDir.mkdirs()) {
                    throw new IOException();
                } else {
                    cleanupOldLogs(logDir);
                    final FileHandler fileHandler = new FileHandler(
                            logDir.getPath() + SLASH + OPENSTREETVIEW_PLUGIN + UNDERSCORE + componentName + UNDERSCORE
                                    + LocalDate.now().toString() + LOG_EXTENSION, true);
                    fileHandler.setFormatter(new SimpleFormatter());
                    logger = Logger.getLogger(ClientLogger.class.getName() + componentName);
                    logger.setUseParentHandlers(false);
                    logger.addHandler(fileHandler);
                    logger.setLevel(Level.INFO);
                }

            } catch (SecurityException | IOException e) {
                Logging.info(
                        "Failed to initialize client logger for openstreetview plugin with component: " + componentName,
                        e);
            }
        }
    }

    private void cleanupOldLogs(final File directory) {
        Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> DAYS.between(extractDate(file.getName()).orElse(LocalDate.now()), LocalDate.now()) > 7)
                .forEach(File::delete);
    }

    private Optional<LocalDate> extractDate(final String fileName) {
        if (fileName.matches(OPENSTREETVIEW_PLUGIN + ANY_CHARACTERS + LOG_EXTENSION + ANY_CHARACTERS)) {
            final String[] nameComponents = fileName.split(UNDERSCORE);
            final String logDate = nameComponents[DATE_INDEX].split("\\.")[0];
            return Optional.of(LocalDate.parse(logDate));
        }
        return Optional.empty();
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