# 🚀 Guide Étape par Étape - Framework NIA

Ce guide vous accompagne de A à Z pour reproduire et exécuter le projet Framework NIA sur Java 17.

---

## 📋 Checklist Prérequis

Avant de commencer, vérifiez que vous avez:

- [ ] Java 17 installé
- [ ] Maven 3.6+ installé
- [ ] Un serveur d'application (Tomcat 9+)
- [ ] Un IDE (IntelliJ IDEA, Eclipse, VS Code) - optionnel
- [ ] Git installé - optionnel

### Vérifier les installations

```bash
# Vérifier Java 17
java -version
# Résultat attendu: java version "17.x.x"

# Vérifier Maven
mvn -version
# Résultat attendu: Apache Maven 3.6.x

# Vérifier Git (optionnel)
git --version
# Résultat attendu: git version x.x.x
```

---

## 📁 ÉTAPE 1: Créer la Structure du Projet

### 1.1 Créer le dossier principal

```bash
# Créer le répertoire du projet
mkdir -p ~/projects/framework-NIA
cd ~/projects/framework-NIA

# Créer la structure Maven standard
mkdir -p src/main/java/nia/framework
mkdir -p src/main/webapp/WEB-INF
mkdir -p src/test/java
mkdir -p src/test/resources
```

**Résultat:**
```
framework-NIA/
├── src/
│   ├── main/
│   │   ├── java/nia/framework/
│   │   └── webapp/WEB-INF/
│   └── test/
│       ├── java/
│       └── resources/
```

### 1.2 Initialiser Git (optionnel)

```bash
git init
touch .gitignore
```

Ajouter au `.gitignore`:
```
target/
.idea/
*.iml
.vscode/
*.swp
*.swo
*~
.DS_Store
```

---

## 🔧 ÉTAPE 2: Créer et Configurer pom.xml

### 2.1 Créer le fichier pom.xml

À la racine du projet (`framework-NIA/`), créez le fichier `pom.xml`:

```bash
touch pom.xml
```

### 2.2 Ajouter le contenu du pom.xml

Copier-coller le contenu suivant dans le fichier `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- Modèle du POM Maven 4.0.0 -->
    <modelVersion>4.0.0</modelVersion>

    <!-- Coordonnées du projet -->
    <groupId>nia.framework</groupId>
    <artifactId>mini-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
    
    <!-- Type de packaging: WAR (Web Application Archive) -->
    <packaging>war</packaging>

    <!-- Propriétés de compilation -->
    <properties>
        <!-- ⭐ IMPORTANT: Java 17 (à adapter selon votre version) -->
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <!-- Dépendances du projet -->
    <dependencies>
        
        <!-- 1. API Servlet 4.0 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <!-- "provided": Tomcat fournira cette API au runtime -->
            <scope>provided</scope>
        </dependency>

        <!-- 2. API JSP (pour les vues JSP - optionnel) -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
        
    </dependencies>

    <!-- Configuration du build -->
    <build>
        <plugins>
            <!-- Plugin WAR pour empaqueter l'application -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <!-- Permettre de construire sans web.xml -->
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

### 2.3 Vérifier la structure

```bash
ls -la
# Vous devriez voir: pom.xml et le dossier src/
```

---

## 💾 ÉTAPE 3: Créer le Servlet FrontControllerServlet.java

### 3.1 Créer le fichier

```bash
touch src/main/java/nia/framework/FrontControllerServlet.java
```

### 3.2 Ajouter le code

Copier-coller dans le fichier:

```java
package nia.framework;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Front Controller Servlet
 * 
 * Pattern: Front Controller
 * Rôle: Intercepter TOUTES les requêtes HTTP et les centraliser
 * 
 * Exemple:
 * - GET /users → FrontControllerServlet
 * - POST /products → FrontControllerServlet
 * - GET /api/orders → FrontControllerServlet
 */
public class FrontControllerServlet extends HttpServlet {
    
    /**
     * Méthode commune pour traiter les requêtes
     * 
     * @param req  - L'objet requête HTTP
     * @param resp - L'objet réponse HTTP
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Récupérer les informations de la requête
        String url = req.getRequestURI();      // Ex: /mini-framework/users
        String method = req.getMethod();        // Ex: GET, POST, PUT, DELETE
        
        // Définir le type de contenu de la réponse
        resp.setContentType("text/plain;charset=UTF-8");
        
        // Afficher les informations
        resp.getWriter().println("===== FRONT CONTROLLER =====");
        resp.getWriter().println("URL    : " + url);
        resp.getWriter().println("Method : " + method);
        resp.getWriter().println("=============================");
    }
    
    /**
     * Traiter les requêtes GET
     * Appelé quand on accède via le navigateur ou curl -X GET
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Traiter les requêtes POST
     * Appelé quand on envoie des données via un formulaire
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    /**
     * 📌 Améliorations futures:
     * - Ajouter doDelete() et doPut()
     * - Implémenter un système de routage
     * - Ajouter un logger
     * - Gérer les contrôleurs
     */
}
```

### 3.3 Vérifier la création

```bash
cat src/main/java/nia/framework/FrontControllerServlet.java
# Vous devriez voir le code du servlet
```

---

## 🗂️ ÉTAPE 4: Créer le Descripteur de Déploiement web.xml

### 4.1 Créer le fichier

```bash
touch src/main/webapp/WEB-INF/web.xml
```

### 4.2 Ajouter le contenu

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- 
        Descripteur de déploiement pour l'application web
        Définit comment le serveur d'application doit traiter les requêtes
    -->

    <!-- Déclaration du servlet FrontController -->
    <servlet>
        <!-- Nom interne du servlet (pour le mapping) -->
        <servlet-name>FrontController</servlet-name>
        
        <!-- Classe Java à charger (chemin complet) -->
        <servlet-class>nia.framework.FrontControllerServlet</servlet-class>
        
        <!-- Charge le servlet au démarrage (optionnel) -->
        <load-on-startup>1</load-on-startup>
    </servlet>

    <!-- Mapping: Association entre URL et servlet -->
    <servlet-mapping>
        <!-- Doit correspondre au servlet-name ci-dessus -->
        <servlet-name>FrontController</servlet-name>
        
        <!-- Pattern d'URL: /* = TOUTES les requêtes -->
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

    <!-- Configuration optionnelle de session -->
    <session-config>
        <tracking-mode>COOKIE</tracking-mode>
    </session-config>

</web-app>
```

---

## ✅ ÉTAPE 5: Compiler et Construire le Projet

### 5.1 Vérifier la structure du projet

```bash
tree src/
# Ou simplement:
find src/ -type f
```

Structure attendue:
```
src/main/java/nia/framework/FrontControllerServlet.java
src/main/webapp/WEB-INF/web.xml
```

### 5.2 Nettoyer les anciens builds (si applicable)

```bash
mvn clean
```

### 5.3 Compiler le code Java

```bash
mvn compile
```

**Résultat attendu:**
```
[INFO] BUILD SUCCESS
[INFO] -------------------------------------------------------
[INFO] Total time: x.xxx s
```

### 5.4 Construire le fichier WAR

```bash
mvn package
```

**Résultat attendu:**
```
[INFO] Building war: /chemin/vers/target/mini-framework-1.0-SNAPSHOT.war
[INFO] BUILD SUCCESS
```

### 5.5 Vérifier l'artefact WAR

```bash
ls -lh target/*.war
# Vous devriez voir: mini-framework-1.0-SNAPSHOT.war

# Voir le contenu du WAR
unzip -l target/mini-framework-1.0-SNAPSHOT.war | head -20
```

---

## 🚀 ÉTAPE 6: Déployer sur Tomcat

### 6.1 Configuration de Tomcat

#### Vérifier l'installation de Tomcat

```bash
# Variable d'environnement CATALINA_HOME
echo $CATALINA_HOME
# Ou sur Windows: echo %CATALINA_HOME%

# Si non défini, l'ajouter
export CATALINA_HOME=/path/to/apache-tomcat-9.x
```

#### Vérifier que Tomcat est dans PATH

```bash
which catalina.sh
# Ou: where catalina.bat (Windows)
```

### 6.2 Copier le WAR dans Tomcat

```bash
# Copier le WAR généré
cp target/mini-framework-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/

# Vérifier
ls -la $CATALINA_HOME/webapps/ | grep mini-framework
```

### 6.3 Démarrer Tomcat

```bash
# Démarrer Tomcat
$CATALINA_HOME/bin/startup.sh
# Ou sur Windows: %CATALINA_HOME%\bin\startup.bat

# Vérifier que le service est démarré
ps aux | grep tomcat
```

### 6.4 Attendre le déploiement (30-60 secondes)

```bash
# Vérifier les logs
tail -f $CATALINA_HOME/logs/catalina.out
# Ou Windows: type %CATALINA_HOME%\logs\catalina.log

# Chercher "Started" ou "Startup in X ms"
```

---

## 🧪 ÉTAPE 7: Tester l'Application

### 7.1 Test via Navigateur

1. Ouvrir votre navigateur
2. Accédez à: `http://localhost:8080/mini-framework-1.0-SNAPSHOT/`
3. Vous devriez voir:
```
===== FRONT CONTROLLER =====
URL    : /mini-framework-1.0-SNAPSHOT/
Method : GET
=============================
```

### 7.2 Test via cURL (Terminal)

```bash
# Test GET
curl http://localhost:8080/mini-framework-1.0-SNAPSHOT/
# Résultat attendu: affiche les informations

# Test GET avec un chemin
curl http://localhost:8080/mini-framework-1.0-SNAPSHOT/users
# URL: /mini-framework-1.0-SNAPSHOT/users

# Test POST
curl -X POST http://localhost:8080/mini-framework-1.0-SNAPSHOT/api/create
# Method: POST

# Test POST avec données
curl -X POST -d "nom=John&age=25" \
  http://localhost:8080/mini-framework-1.0-SNAPSHOT/user/save
```

### 7.3 Test via Postman

1. Ouvrir Postman
2. Créer une nouvelle requête:
   - **Method:** GET
   - **URL:** `http://localhost:8080/mini-framework-1.0-SNAPSHOT/test`
3. Cliquer sur "Send"
4. Vérifier la réponse

---

## ❌ Dépannage Courant

### Problème 1: "java: command not found"
```bash
# ✅ Solution: Installer Java 17 ou ajouter à PATH
export JAVA_HOME=/path/to/java-17
export PATH=$JAVA_HOME/bin:$PATH
java -version
```

### Problème 2: "mvn: command not found"
```bash
# ✅ Solution: Installer Maven ou ajouter à PATH
export M2_HOME=/path/to/apache-maven
export PATH=$M2_HOME/bin:$PATH
mvn -version
```

### Problème 3: "Erreur 404" lors de l'accès
```
✅ Solutions:
1. Vérifier que Tomcat est démarré: ps aux | grep tomcat
2. Vérifier le chemin de l'URL (case sensitive)
3. Attendre 30-60 secondes pour le déploiement
4. Vérifier les logs: tail -f $CATALINA_HOME/logs/catalina.out
```

### Problème 4: "Port 8080 already in use"
```bash
# ✅ Solution: Tuer le processus Tomcat existant
# Linux/Mac:
lsof -i :8080
kill -9 <PID>

# Ou modifier le port dans: $CATALINA_HOME/conf/server.xml
# Chercher <Connector port="8080"> et changer le port
```

### Problème 5: "BUILD FAILURE" lors du mvn compile
```bash
# ✅ Solutions:
1. Vérifier que Java 17 est bien configuré
2. Nettoyer le cache: mvn clean
3. Vérifier pom.xml syntaxe
4. Vérifier que les sources sont dans le bon répertoire
```

### Problème 6: Logs vides, servlet ne s'affiche pas
```bash
# ✅ Solutions:
1. Vérifier le nom du servlet dans web.xml
2. Vérifier le chemin complet: nia.framework.FrontControllerServlet
3. Vérifier que le .war contient les .class compilés
   unzip -l target/mini-framework-1.0-SNAPSHOT.war | grep FrontController
```

---

## 📝 Fichiers de Configuration Finaux

### Structure complète du projet

```
framework-NIA/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── nia/framework/
│   │   │       └── FrontControllerServlet.java
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── web.xml
│   └── test/
│       └── java/
└── target/
    ├── classes/
    │   └── nia/framework/FrontControllerServlet.class
    └── mini-framework-1.0-SNAPSHOT.war
```

---

## 🎯 Prochaines Étapes (Améliorations)

Maintenant que le framework de base fonctionne, vous pouvez:

1. **Ajouter le routage** - Implémenter un système pour mapper les URLs
2. **Ajouter les contrôleurs** - Créer une interface Controller
3. **Ajouter les vues** - Intégrer Thymeleaf ou FreeMarker
4. **Ajouter l'injection de dépendances** - Utiliser Spring IoC
5. **Ajouter l'ORM** - Intégrer Hibernate ou JPA
6. **Ajouter la validation** - Jakarta Bean Validation
7. **Ajouter les tests** - JUnit 5 et Mockito

---

## 🔗 Commandes Utiles

```bash
# Compilation et test
mvn clean compile         # Compiler
mvn clean package         # Compiler + empaqueter
mvn clean install         # Compiler + installer localement

# Déploiement
mvn deploy               # Déployer sur un serveur distant

# Tomcat
$CATALINA_HOME/bin/startup.sh    # Démarrer Tomcat
$CATALINA_HOME/bin/shutdown.sh   # Arrêter Tomcat

# Vérification
curl -X GET http://localhost:8080/mini-framework-1.0-SNAPSHOT/
curl -X POST http://localhost:8080/mini-framework-1.0-SNAPSHOT/test
```

---

## 📞 Aide et Support

Si vous rencontrez des problèmes:

1. **Logs Tomcat:** `$CATALINA_HOME/logs/catalina.out`
2. **Logs Maven:** Ajouter `-X` à la commande
3. **Vérifier les ports:** `lsof -i :8080`
4. **Vérifier Java:** `java -version`

---

**Bonne chance avec votre projet! 🚀**
