package org.openstreetmap.josm.plugins.kartaview.service;

import static java.time.temporal.ChronoUnit.DAYS;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.Config;
import org.openstreetmap.josm.plugins.kartaview.util.cnf.GuiConfig;
import org.openstreetmap.josm.tools.Logging;


/**
 * Helps log INFO messages on client side. Each instance of this object will result in a new logger file.
 *
 * @author laura.dumitru on 24/09/2020.
 */
public class ClientLogger {

    public static final String SLASH = "/";
    private static final String LOG_DIRECTORY_PATH =
            Preferences.main().getPluginsDirectory() + SLASH + GuiConfig.getInstance().getPluginShortName() + "/logs";

    private static final String LOG_EXTENSION = ".log";
    private static final String UNDERSCORE = "_";
    private static final int DATE_INDEX = 2;
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
                    final String fileBaseName = GuiConfig.getInstance().getPluginLogName() + UNDERSCORE + componentName;
                    final FileHandler fileHandler = new FileHandler(logDir.getPath() + SLASH + fileBaseName + UNDERSCORE
                            + LocalDate.now().toString() + LOG_EXTENSION, true);
                    fileHandler.setFormatter(new SimpleFormatter());
                    logger = Logger.getLogger(ClientLogger.class.getName() + fileBaseName);
                    logger.setUseParentHandlers(false);
                    logger.addHandler(fileHandler);
                    logger.setLevel(Level.INFO);
                }
            } catch (SecurityException | IOException e) {
                Logging.info("Failed to initialize client logger for KartaView plugin with component: " + componentName,
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
        Optional<LocalDate> result = Optional.empty();
        if (fileName.matches(
                GuiConfig.getInstance().getPluginLogName() + ANY_CHARACTERS + LOG_EXTENSION + ANY_CHARACTERS)) {
            final String[] nameComponents = fileName.split(UNDERSCORE);
            if (nameComponents.length > DATE_INDEX) {
                final String logDate = nameComponents[DATE_INDEX].split("\\.")[0];
                try {
                    result = Optional.of(LocalDate.parse(logDate));
                } catch (final DateTimeParseException e) {
                    Logging.info("Failed to extract date for file: " + fileName, e);
                }
            }
        }
        return result;
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