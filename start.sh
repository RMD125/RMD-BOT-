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
    echo "❌ Session non trouvée. Exécutez d'abord ./scripts/install.sh"
    exit 1
fi

# Vérification de Java
if ! command -v java &> /dev/null; then
    echo "❌ Java n'est pas installé"
    echo "📦 Installation de Java..."
    pkg install openjdk-17 -y
fi

# Vérification de la compilation
if [ ! -d "build" ] || [ -z "$(ls -A build 2>/dev/null)" ]; then
    echo "🔄 Compilation nécessaire..."
    ./scripts/install.sh
fi

# Affichage des informations de session
echo "📋 Informations de session:"
SESSION_INFO=$(grep -o '"clientID": "[^"]*\|"createdAt": "[^"]*' sessions/session.json | head -2)
echo "   - $(echo "$SESSION_INFO" | head -1 | sed 's/"//g')"
echo "   - $(echo "$SESSION_INFO" | tail -1 | sed 's/"//g')"
echo ""

echo "🚀 Démarrage de RMD-BOT..."
echo "   Press Ctrl+C pour arrêter"
echo ""

# Démarrage de l'application Java
java -cp "build:lib/*" com.rmdbot.Main

# Journalisation
echo "$(date): RMD-BOT arrêté" >> logs/runtime.log
