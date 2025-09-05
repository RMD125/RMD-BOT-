package com.rmdbot;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final String VERSION = "RMD-V5";
    private static final String PHONE_NUMBER = "+22896190934";
    private static SessionManager sessionManager;
    private static WhatsAppConnector whatsappConnector;
    private static CommandHandler commandHandler;
    
    public static void main(String[] args) {
        displayBanner();
        
        // Initialisation du logger
        Logger.info("Démarrage de RMD-BOT v" + VERSION);
        
        // Initialisation du gestionnaire de session
        sessionManager = new SessionManager();
        
        // Chargement de la session
        if (sessionManager.loadSession()) {
            Logger.info("Session persistante chargée avec succès");
            
            // Connexion à WhatsApp
            whatsappConnector = new WhatsAppConnector(sessionManager);
            if (whatsappConnector.connect()) {
                Logger.info("Connexion à WhatsApp établie");
                
                // Initialisation du gestionnaire de commandes
                commandHandler = new CommandHandler(whatsappConnector);
                commandHandler.start();
                
                // Maintenance de la session
                keepSessionAlive();
            } else {
                Logger.error("Échec de la connexion à WhatsApp");
            }
        } else {
            Logger.error("Échec du chargement de la session");
            Logger.info("Création d'une nouvelle session...");
            sessionManager.createNewSession(PHONE_NUMBER);
            
            // Redémarrer après création de session
            if (sessionManager.loadSession()) {
                Logger.info("Nouvelle session créée, redémarrage...");
                main(args); // Redémarrage récursif
            }
        }
    }
    
    private static void displayBanner() {
        System.out.println("╭━《* 𝚁𝙼𝙳-𝙱𝙾𝚃 * 》━┈⊷");
        System.out.println("┃★┃•Mode: 𝙿𝚁𝙸𝚅𝙴́");
        System.out.println("┃★┃•Commandes: 150+");
        System.out.println("┃★┃•Version: " + VERSION);
        System.out.println("┃★┃•Créateur: 𝚁𝙼𝙳𝟷𝟸𝟻");
        System.out.println("┃★┃•Numéro: " + PHONE_NUMBER);
        System.out.println("┃★┃•RAM: ██████████ 95%");
        System.out.println("┃★┃•Speed: 0.8ms");
        System.out.println("╚═══════════════════╝");
        System.out.println("");
        System.out.println("🔒 𝙲𝙾𝙽𝙽𝙴𝚇𝙸𝙾𝙽 𝚂𝙴́𝙲𝚄𝚁𝙸𝚂𝙴́𝙴 🔒");
        System.out.println("🔗 𝚂𝙴𝚂𝚂𝙸𝙾𝙽 𝙿𝙴𝚁𝙿𝙴𝚃𝚄𝙴𝙻𝙻𝙴");
        System.out.println("📞 " + PHONE_NUMBER);
        System.out.println("");
    }
    
    private static void keepSessionAlive() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Logger.info("Maintenance de session active - " + new Date());
                if (whatsappConnector != null) {
                    whatsappConnector.sendKeepAlive();
                }
                if (sessionManager != null) {
                    sessionManager.updateLastActive();
                }
            }
        }, 0, 300000); // Toutes les 5 minutes
    }
}
