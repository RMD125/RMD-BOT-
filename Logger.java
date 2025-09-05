package com.rmdbot.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static final String LOG_DIR = "logs/";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static void log(String level, String message) {
        String timestamp = DATE_FORMAT.format(new Date());
        String logMessage = String.format("[%s] [%s] %s", timestamp, level.toUpperCase(), message);
        
        // Affichage console
        System.out.println(logMessage);
        
        // Écriture fichier
        try {
            // Assurer que le dossier logs existe
            java.io.File logDir = new java.io.File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            String logFile = LOG_DIR + "rmd-bot_" + FILE_FORMAT.format(new Date()) + ".log";
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(logMessage + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("❌ Erreur d'écriture dans le log: " + e.getMessage());
        }
    }
    
    public static void info(String message) {
        log("INFO", message);
    }
    
    public static void warning(String message) {
        log("WARNING", message);
    }
    
    public static void error(String message) {
        log("ERROR", message);
    }
    
    public static void debug(String message) {
        log("DEBUG", message);
    }
}
