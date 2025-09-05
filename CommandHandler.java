package com.rmdbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private WhatsAppConnector whatsappConnector;
    private Map<String, Command> commands;
    private String prefix;
    
    public CommandHandler(WhatsAppConnector whatsappConnector) {
        this.whatsappConnector = whatsappConnector;
        this.commands = new HashMap<>();
        this.prefix = "!";
        loadCommands();
    }
    
    private void loadCommands() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray commandsArray = (JSONArray) parser.parse(new FileReader("src/main/resources/commands.json"));
            
            for (Object obj : commandsArray) {
                JSONObject cmdObj = (JSONObject) obj;
                String name = (String) cmdObj.get("name");
                String description = (String) cmdObj.get("description");
                String usage = (String) cmdObj.get("usage");
                String category = (String) cmdObj.get("category");
                
                commands.put(name, new Command(name, description, usage, category));
            }
            
            Logger.info("✅ " + commands.size() + " commandes chargées");
        } catch (Exception e) {
            Logger.error("❌ Erreur lors du chargement des commandes: " + e.getMessage());
        }
    }
    
    public void start() {
        Logger.info("Gestionnaire de commandes démarré");
        // Simulation de l'écoute des messages
        simulateMessageListening();
    }
    
    private void simulateMessageListening() {
        Thread listenerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    // Simulation de réception de message
                    simulateIncomingMessage();
                } catch (InterruptedException e) {
                    Logger.error("Gestionnaire de messages interrompu: " + e.getMessage());
                    break;
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }
    
    private void simulateIncomingMessage() {
        // Simulation de messages entrants pour tester
        String[] testMessages = {
            "!help",
            "!info", 
            "!ping",
            "!menu"
        };
        
        String testMessage = testMessages[(int) (Math.random() * testMessages.length)];
        String testSender = "test-user";
        
        processMessage(testMessage, testSender);
    }
    
    public void processMessage(String message, String sender) {
        if (message.startsWith(prefix)) {
            String[] parts = message.substring(1).split(" ", 2);
            String commandName = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";
            
            if (commands.containsKey(commandName)) {
                executeCommand(commandName, args, sender);
            } else {
                sendMessage(sender, "❌ Commande inconnue. Tapez `!help` pour la liste des commandes.");
            }
        }
    }
    
    private void executeCommand(String commandName, String args, String sender) {
        Command command = commands.get(commandName);
        Logger.info("Exécution commande: " + commandName + " par " + sender);
        
        switch (commandName) {
            case "help":
                showHelp(sender);
                break;
            case "info":
                sendMessage(sender, "🤖 RMD-BOT vRMD-V5\nCréé par RMD125\n📞 Session: +22896190934");
                break;
            case "ping":
                sendMessage(sender, "🏓 Pong! Bot actif et opérationnel");
                break;
            case "menu":
                showMenu(sender);
                break;
            default:
                sendMessage(sender, "⚙️ Commande en cours de développement: " + commandName);
                break;
        }
    }
    
    private void showHelp(String sender) {
        StringBuilder helpMessage = new StringBuilder("📋 **Commandes Disponibles**\n\n");
        
        helpMessage.append("🤖 **Général**\n");
        helpMessage.append("• `!help` - Affiche cette aide\n");
        helpMessage.append("• `!info` - Informations du bot\n");
        helpMessage.append("• `!ping` - Test de réponse\n");
        helpMessage.append("• `!menu` - Menu principal\n\n");
        
        helpMessage.append("🎮 **Fun**\n");
        helpMessage.append("• `!joke` - Blague aléatoire\n");
        helpMessage.append("• `!quote` - Citation inspirante\n");
        helpMessage.append("• `!fact` - Fait intéressant\n\n");
        
        helpMessage.append("🛠️ **Outils**\n");
        helpMessage.append("• `!sticker` - Crée un sticker\n");
        helpMessage.append("• `!tts` - Text-to-Speech\n");
        helpMessage.append("• `!translate` - Traduction\n\n");
        
        helpMessage.append("🔍 **Recherche**\n");
        helpMessage.append("• `!google` - Recherche Google\n");
        helpMessage.append("• `!yt` - Recherche YouTube\n");
        helpMessage.append("• `!weather` - Météo\n");
        
        sendMessage(sender, helpMessage.toString());
    }
    
    private void showMenu(String sender) {
        String menu = "╭━━《 📱 MENU RMD-BOT 》━━╮\n" +
                     "┃\n" +
                     "┃ 🤖 **GÉNÉRAL**\n" +
                     "┃ ├── !help - Aide\n" +
                     "┃ ├── !info - Infos bot\n" +
                     "┃ ├── !ping - Test\n" +
                     "┃ └── !menu - Ce menu\n" +
                     "┃\n" +
                     "┃ 🎮 **FUN**\n" +
                     "┃ ├── !joke - Blague\n" +
                     "┃ ├── !quote - Citation\n" +
                     "┃ └── !fact - Fait\n" +
                     "┃\n" +
                     "┃ 🛠️ **OUTILS**\n" +
                     "┃ ├── !sticker - Sticker\n" +
                     "┃ ├── !tts - Vocal\n" +
                     "┃ └── !translate - Traduction\n" +
                     "┃\n" +
                     "┃ 🔍 **RECHERCHE**\n" +
                     "┃ ├── !google - Google\n" +
                     "┃ ├── !yt - YouTube\n" +
                     "┃ └── !weather - Météo\n" +
                     "┃\n" +
                     "╰━━━━━━━━━━━━━━━━━━━━╯";
        
        sendMessage(sender, menu);
    }
    
    private void sendMessage(String recipient, String message) {
        if (whatsappConnector != null) {
            whatsappConnector.sendMessage(recipient, message);
        }
    }
    
    private class Command {
        String name;
        String description;
        String usage;
        String category;
        
        Command(String name, String description, String usage, String category) {
            this.name = name;
            this.description = description;
            this.usage = usage;
            this.category = category;
        }
    }
}
