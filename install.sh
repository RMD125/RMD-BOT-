#!/bin/bash

# Script d'installation de RMD-BOT
echo "=========================================="
echo "        INSTALLATION DE RMD-BOT"
echo "=========================================="
echo "Numéro de session: +22896190934"
echo ""

# Vérification Java
if ! command -v java &> /dev/null; then
    echo "Java n'est pas installé. Installation..."
    pkg install openjdk-17 -y
fi

# Vérification des outils
for tool in git node npm wget curl; do
    if ! command -v $tool &> /dev/null; then
        echo "$tool n'est pas installé. Installation..."
        pkg install $tool -y
    fi
done

# Création des dossiers
mkdir -p lib sessions logs backups build

# Téléchargement des dépendances
echo "Téléchargement des dépendances..."
if [ ! -f "lib/whatsapp-web.jar" ]; then
    echo "Téléchargement de whatsapp-web.jar..."
    wget -O lib/whatsapp-web.jar https://github.com/RMD125/RMD-BOT/releases/download/v1.0.0/whatsapp-web.jar
fi

if [ ! -f "lib/json-simple.jar" ]; then
    echo "Téléchargement de json-simple.jar..."
    wget -O lib/json-simple.jar https://github.com/RMD125/RMD-BOT/releases/download/v1.0.0/json-simple.jar
fi

# Installation des dépendances Node.js
if [ -f "package.json" ]; then
    npm install
fi

# Compilation du code Java
echo "Compilation du code Java..."
javac -cp "lib/*" src/main/java/com/rmdbot/*.java src/main/java/com/rmdbot/utilities/*.java -d build

# Copie des ressources
cp -r src/main/resources/* build/ 2>/dev/null || true

# Création de la session si elle n'existe pas
if [ ! -f "sessions/session.json" ]; then
    echo "Création d'une nouvelle session..."
    cat > sessions/session.json << EOL
{
  "clientID": "RMD-BOT-$(date +%s)",
  "serverToken": "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "clientToken": "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "encKey": "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "macKey": "$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "phoneNumber": "+22896190934",
  "createdAt": "$(date)",
  "lastActive": "$(date)"
}
EOL
    echo "✅ Session créée"
fi

# Création du fichier de config s'il n'existe pas
if [ ! -f "config.json" ] && [ -f "config.example.json" ]; then
    echo "Création du fichier de configuration..."
    cp config.example.json config.json
    echo "✅ Fichier config.json créé"
    echo "ℹ️  N'oubliez pas de modifier config.json avec vos paramètres!"
fi

echo ""
echo "=========================================="
echo "       INSTALLATION TERMINÉE!"
echo "=========================================="
echo "Prochaines étapes:"
echo "1. Éditez config.json si nécessaire"
echo "2. Lancez le bot: ./scripts/start.sh"
echo "3. Pour mettre à jour: ./scripts/update.sh"
echo "=========================================="
