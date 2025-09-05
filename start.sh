#!/bin/bash

# Script de démarrage de RMD-BOT
clear
echo "╭━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╮"
echo "┃        DÉMARRAGE DE RMD-BOT    ┃"
echo "╰━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╯"
echo "🔗 Connexion avec le numéro: +22896190934"
echo ""

# Vérification de la session
if [ ! -f "sessions/session.json" ]; then
    echo "❌ Session non trouvée. Exécutez d'abord install.sh"
    exit 1
fi

# Vérification de Java
if ! command -v java &> /dev/null; then
    echo "❌ Java n'est pas installé"
    exit 1
fi

# Affichage des informations de session
echo "📋 Informations de session:"
echo "   - Client ID: $(grep -o '"clientID": "[^"]*' sessions/session.json | grep -o '[^"]*$' | head -1)"
echo "   - Créée le: $(grep -o '"createdAt": "[^"]*' sessions/session.json | grep -o '[^"]*$' | head -1)"
echo ""

echo "🚀 Démarrage de RMD-BOT..."
echo "   Press Ctrl+C pour arrêter"
echo ""

# Démarrage de l'application Java
java -cp "build:lib/*" com.rmdbot.Main

# Journalisation
echo "$(date): RMD-BOT arrêté" >> logs/runtime.log
