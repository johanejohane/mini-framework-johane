# ⚡ Démarrage Rapide (10 minutes) - Framework NIA

**Vous êtes pressé?** Commencez ici! Ce guide condense tout en 10 étapes essentielles.

---

## 🎯 Avant de Commencer

```bash
# Vérifier Java 17
java -version  # Doit afficher 17.x.x

# Vérifier Maven
mvn -version   # Doit afficher 3.6.x ou plus
```

Si vous n'avez pas Java 17 ou Maven, installez-les d'abord!

---

## ⚡ 10 Étapes Rapides

### 1️⃣ Créer la structure du projet

```bash
mkdir -p ~/framework-NIA/src/main/java/nia/framework
mkdir -p ~/framework-NIA/src/main/webapp/WEB-INF
cd ~/framework-NIA
```

### 2️⃣ Créer pom.xml

```bash
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>nia.framework</groupId>
    <artifactId>mini-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging> 

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
EOF
```

### 3️⃣ Créer FrontControllerServlet.java

```bash
cat > src/main/java/nia/framework/FrontControllerServlet.java << 'EOF'
package nia.framework;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String url = req.getRequestURI();
        String method = req.getMethod();
        
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println("URL    : " + url);
        resp.getWriter().println("Method : " + method);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
EOF
```

### 4️⃣ Créer web.xml

```bash
cat > src/main/webapp/WEB-INF/web.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <servlet>
        <servlet-name>FrontController</servlet-name>
        <servlet-class>nia.framework.FrontControllerServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>FrontController</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

</web-app>
EOF
```

### 5️⃣ Compiler le projet

```bash
mvn clean compile
# Doit afficher: BUILD SUCCESS
```

### 6️⃣ Créer le WAR

```bash
mvn package
# Doit créer: target/mini-framework-1.0-SNAPSHOT.war
```

### 7️⃣ Vérifier le WAR

```bash
ls -lh target/*.war
# Vous devriez voir le fichier WAR
```

### 8️⃣ Copier dans Tomcat

```bash
# ⚠️ Remplacer /path/to/tomcat par votre chemin Tomcat
cp target/mini-framework-1.0-SNAPSHOT.war /path/to/tomcat/webapps/
```

### 9️⃣ Démarrer Tomcat

```bash
# ⚠️ Remplacer /path/to/tomcat par votre chemin Tomcat
/path/to/tomcat/bin/startup.sh

# Attendre 30-60 secondes
sleep 45

# Vérifier les logs
tail -f /path/to/tomcat/logs/catalina.out | grep -i "started"
```

### 🔟 Tester l'application

```bash
# Option 1: curl
curl http://localhost:8080/mini-framework-1.0-SNAPSHOT/

# Option 2: Navigateur
# Ouvrir: http://localhost:8080/mini-framework-1.0-SNAPSHOT/

# Résultat attendu:
# URL    : /mini-framework-1.0-SNAPSHOT/
# Method : GET
```

---

## ✅ Succès!

Si vous voyez:
```
URL    : /mini-framework-1.0-SNAPSHOT/
Method : GET
```

**Félicitations! 🎉 Votre Framework NIA est en marche!**

---

## 📍 Où Trouver Tomcat?

### Sur Linux/Mac
```bash
# Installer Tomcat
brew install tomcat  # macOS avec Homebrew

# Ou manuellement
# Télécharger depuis: https://tomcat.apache.org/download-9.cgi
# Extraire et pointer le chemin

# Trouver le chemin
which catalina.sh

# Ou chercher
find /opt -name "catalina.sh" 2>/dev/null
find /usr/local -name "catalina.sh" 2>/dev/null
```

### Sur Windows
```bash
# Télécharger depuis: https://tomcat.apache.org/download-9.cgi
# Extraire dans C:\apache-tomcat-9.0.x

# Démarrer Tomcat
C:\apache-tomcat-9.0.x\bin\startup.bat
```

---

## ⚠️ Modifications pour Java 17

✅ Vous avez déjà fait les modifications dans le **pom.xml** ci-dessus!

Les seules modifications requises:
```xml
<maven.compiler.source>17</maven.compiler.source>  ✅ Correctement défini
<maven.compiler.target>17</maven.compiler.target>  ✅ Correctement défini
```

---

## 🔧 Problèmes Courants

### "mvn: command not found"
```bash
# Installer Maven
# Ubuntu/Debian
sudo apt-get install maven

# macOS
brew install maven

# Ou télécharger manuellement
# https://maven.apache.org/download.cgi
```

### "java: command not found"
```bash
# Installer Java 17
# Ubuntu/Debian
sudo apt-get install openjdk-17-jdk

# macOS
brew install openjdk@17

# Vérifier
java -version  # Doit afficher 17.x.x
```

### "Erreur 404" lors de l'accès
```bash
# 1. Vérifier que Tomcat est démarré
ps aux | grep tomcat

# 2. Vérifier les logs
tail -f $CATALINA_HOME/logs/catalina.out

# 3. Attendre que le déploiement finisse (30-60 secondes)

# 4. Vérifier l'URL (exactement: /mini-framework-1.0-SNAPSHOT/)
```

### "Port 8080 already in use"
```bash
# Tuer le processus existant
lsof -i :8080
kill -9 <PID>

# Ou changer le port dans
# $CATALINA_HOME/conf/server.xml
```

---

## 📚 Documents Complémentaires

Pour plus de détails, consultez:

1. **README-framework-NIA.md** - Documentation complète du projet
2. **GUIDE-ETAPES.md** - Guide détaillé étape par étape
3. **FICHIERS-SOURCES.md** - Tous les fichiers source avec commentaires
4. **MODIFICATIONS-JAVA17.md** - Guide des modifications pour Java 17

---

## 🚀 Prochaines Étapes

Une fois le projet en marche:

1. **Ajouter du routage** - Intercepter les requêtes `/users`, `/products`, etc.
2. **Ajouter des contrôleurs** - Organiser le code avec des classes Controller
3. **Ajouter les vues** - Intégrer Thymeleaf ou JSP
4. **Ajouter la base de données** - Intégrer Hibernate ou JPA
5. **Ajouter les tests** - JUnit 5 et Mockito

---

## 🎓 Architecture du Projet

```
Framework NIA (Mini Web Framework)
│
├── Front Controller (Servlet unique)
│   └── Reçoit TOUTES les requêtes
│
├── Router (à ajouter)
│   └── Dirige vers le bon contrôleur
│
├── Controllers (à ajouter)
│   └── Traitent la logique métier
│
├── Views (à ajouter)
│   └── Renvoient le HTML/JSON
│
└── Models (à ajouter)
    └── Accès à la base de données
```

---

## ✨ Résumé en 30 secondes

1. `mkdir -p ~/framework-NIA/src/main/{java/nia/framework,webapp/WEB-INF}`
2. Créer **pom.xml** (copier le code ci-dessus)
3. Créer **FrontControllerServlet.java** (copier le code ci-dessus)
4. Créer **web.xml** (copier le code ci-dessus)
5. `mvn clean package`
6. Copier **mini-framework-1.0-SNAPSHOT.war** dans `$CATALINA_HOME/webapps/`
7. Démarrer Tomcat
8. Tester: `curl http://localhost:8080/mini-framework-1.0-SNAPSHOT/`

**Prêt? Commencez! 🚀**

---

**Besoin d'aide?** Consultez les autres documents .md fournis.
