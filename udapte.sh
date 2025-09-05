#!/bin/bash

# Script de mise à jour automatique de RMD-BOT
echo "╭━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╮"
echo "┃      MISE À JOUR DE RMD-BOT    ┃"
echo "╰━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━╯"

# Sauvegarde de la session actuelle
if [ -f "sessions/session.json" ]; then
    echo "📦 Sauvegarde de la session..."
    cp sessions/session.json sessions/session.backup.$(date +%Y%m%d_%H%M%S).json
fi

# Mise à jour depuis GitHub
echo "🔄 Récupération des dernières modifications..."
git pull origin main

# Réinstallation des dépendances
echo "📦 Mise à jour des dépendances..."
./scripts/install.sh

# Restauration de la session si nécessaire
if [ ! -f "sessions/session.json" ] && [ $(ls sessions/session.backup.*.json 2>/dev/null | wc -l) -gt 0 ]; then
    echo "🔄 Restauration de la session..."
    latest_backup=$(ls -t sessions/session.backup.*.json | head -1)
    cp "$latest_backup" sessions/session.json
fi

echo "✅ Mise à jour terminée!"
echo "🔄 Redémarrage du bot..."
./scripts/start.sh
