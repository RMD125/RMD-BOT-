package com.rmdbot;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Random;

public class SessionManager {
    private static final String SESSION_FILE = "sessions/session.json";
    private JSONObject sessionData;
    
    public boolean loadSession() {
        try {
            JSONParser parser = new JSONParser();
            sessionData = (JSONObject) parser.parse(new FileReader(SESSION_FILE));
            Logger.info("Session chargée pour: " + sessionData.get("phoneNumber"));
            return true;
        } catch (Exception e) {
            Logger.error("Erreur lors du chargement de la session: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveSession() {
        try {
            FileWriter file = new FileWriter(SESSION_FILE);
            file.write(sessionData.toJSONString());
            file.close();
            return true;
        } catch (Exception e) {
            Logger.error("Erreur lors de la sauvegarde de la session: " + e.getMessage());
            return false;
        }
    }
    
    public void createNewSession(String phoneNumber) {
        sessionData = new JSONObject();
        sessionData.put("clientID", "RMD-BOT-" + generateRandomString(8));
        sessionData.put("serverToken", generateRandomString(40));
        sessionData.put("clientToken", generateRandomString(40));
        sessionData.put("encKey", generateRandomString(40));
        sessionData.put("macKey", generateRandomString(40));
        sessionData.put("phoneNumber", phoneNumber);
        sessionData.put("createdAt", new Date().toString());
        sessionData.put("lastActive", new Date().toString());
        
        if (saveSession()) {
            Logger.info("Nouvelle session créée pour: " + phoneNumber);
        } else {
            Logger.error("Échec de la création de la session");
        }
    }
    
    public JSONObject getSessionData() {
        return sessionData;
    }
    
    public void updateLastActive() {
        try {
            sessionData.put("lastActive", new Date().toString());
            saveSession();
        } catch (Exception e) {
            Logger.error("Erreur lors de la mise à jour de lastActive: " + e.getMessage());
        }
    }
    
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return result.toString();
    }
}
