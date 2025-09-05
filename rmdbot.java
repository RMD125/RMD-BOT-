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
        
        // Initialisation du gestionnaire de session
        sessionManager = new SessionManager();
        
        // Chargement de la session
        if (sessionManager.loadSession()) {
            System.out.println("✅ Session persistante chargée avec succès!");
            
            // Connexion à WhatsApp
            whatsappConnector = new WhatsAppConnector(sessionManager);
            if (whatsappConnector.connect()) {
                System.out.println("✅ Connexion à WhatsApp établie!");
                
                // Initialisation du gestionnaire de commandes
                commandHandler = new CommandHandler(whatsappConnector);
                commandHandler.start();
                
                // Maintenance de la session
                keepSessionAlive();
            } else {
                System.out.println("❌ Échec de la connexion à WhatsApp.");
            }
        } else {
            System.out.println("❌ Échec du chargement de la session.");
            System.out.println("Création d'une nouvelle session...");
            sessionManager.createNewSession(PHONE_NUMBER);
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
                System.out.println("🔄 Session active - " + new Date());
                whatsappConnector.sendKeepAlive();
                sessionManager.updateLastActive();
            }
        }, 0, 300000); // Toutes les 5 minutes
    }
}
