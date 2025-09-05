#!/bin/bash

# Script d'installation de RMD-BOT
echo "Installation de RMD-BOT..."

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
mkdir -p lib sessions logs

# Téléchargement des dépendances
echo "Téléchargement des dépendances..."
if [ ! -f "lib/whatsapp-web.jar" ]; then
    wget -O lib/whatsapp-web.jar https://github.com/RMD125/RMD-BOT/releases/download/v1.0.0/whatsapp-web.jar
fi

if [ ! -f "lib/json-simple.jar" ]; then
    wget -O lib/json-simple.jar https://github.com/RMD125/RMD-BOT/releases/download/v1.0.0/json-simple.jar
fi

# Installation des dépendances Node.js
if [ -f "package.json" ]; then
    npm install
fi

# Compilation du code Java
echo "Compilation du code Java..."
javac -cp "lib/*" src/main/java/com/rmdbot/*.java -d build

# Copie des ressources
cp -r src/main/resources/* build/

# Création de la session si elle n'existe pas
if [ ! -f "sessions/session.json" ]; then
    echo "Création d'une nouvelle session..."
    cat > sessions/session.json << EOL
{
  "clientID": "RMD-BOT-\$(date +%s)",
  "serverToken": "\$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "clientToken": "\$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "encKey": "\$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "macKey": "\$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 40)",
  "phoneNumber": "+22896190934",
  "createdAt": "\$(date)",
  "lastActive": "\$(date)"
}
EOL
fi

# Création du fichier de config s'il n'existe pas
if [ ! -f "config.json" ]; then
    echo "Création du fichier de configuration..."
    cp config.example.json config.json
    echo "N'oubliez pas de modifier config.json avec vos paramètres!"
fi

echo "Installation terminée avec succès!"
