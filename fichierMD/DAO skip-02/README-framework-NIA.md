# Framework NIA - Mini Web Framework

## 📋 Description du Projet

Framework NIA est un mini framework web Java basé sur le pattern **Front Controller**. C'est un framework servlet simple qui intercepte toutes les requêtes HTTP et les centralise dans un unique servlet (FrontControllerServlet).

**Version cible:** Java 17 (à modifier depuis Java 21)
**Packaging:** WAR (Web Application Archive)
**Build Tool:** Maven 3.6+

---

## 🏗️ Architecture du Projet

```
framework-NIA/
├── pom.xml                                      # Configuration Maven
├── src/
│   ├── main/
│   │   ├── java/nia/framework/
│   │   │   └── FrontControllerServlet.java     # Servlet principal
│   │   └── webapp/WEB-INF/
│   │       └── web.xml                         # Descripteur de déploiement
│   └── test/                                    # Tests (à ajouter)
└── target/                                      # Artefacts compilés
```

---

## 📦 Composants du Projet

### 1. **pom.xml** - Configuration Maven
Configuration pour le build du projet WAR avec dépendances servlets.

**Dépendances:**
- `javax.servlet-api` (v4.0.1) - API Servlet
- `javax.servlet.jsp-api` (v2.3.3) - API JSP

### 2. **FrontControllerServlet.java** - Servlet Principal
Intercepte toutes les requêtes HTTP (GET/POST) et affiche:
- L'URL de la requête
- La méthode HTTP utilisée

### 3. **web.xml** - Descripteur de Déploiement
Configure le servlet et mappe toutes les requêtes `/*` vers `FrontControllerServlet`.

---

## 🔧 Prérequis

- **Java 17** (ou supérieur)
- **Maven 3.6** ou plus récent
- **Tomcat 9+** ou autre serveur d'application compatible
- **Git** (optionnel, pour la gestion de version)

---

## ✅ Étapes pour Reproduire le Projet

### Étape 1: Créer la Structure du Projet

```bash
# Créer le répertoire du projet
mkdir -p framework-NIA
cd framework-NIA

# Créer l'arborescence Maven standard
mkdir -p src/main/java/nia/framework
mkdir -p src/main/webapp/WEB-INF
mkdir -p src/test/java
```

### Étape 2: Créer le fichier pom.xml

Créez le fichier `pom.xml` à la racine du projet:

```xml
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
        <!-- ⚠️ MODIFICATION 1: Changé de 21 à 17 pour votre version Java -->
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- API Servlet -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- API JSP -->
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
```

**Modification clé:** Les propriétés `maven.compiler.source` et `maven.compiler.target` sont passées de `21` à `17`.

### Étape 3: Créer FrontControllerServlet.java

Créez le fichier `src/main/java/nia/framework/FrontControllerServlet.java`:

```java
package nia.framework;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Front Controller Servlet
 * Intercepte toutes les requêtes HTTP et les centralise ici
 */
public class FrontControllerServlet extends HttpServlet {
    
    /**
     * Traite la requête HTTP (GET/POST)
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String url = req.getRequestURI();
        String method = req.getMethod();
        
        // Définir le type de contenu
        resp.setContentType("text/plain;charset=UTF-8");
        
        // Afficher les informations de la requête
        resp.getWriter().println("URL    : " + url);
        resp.getWriter().println("Method : " + method);
    }
    
    /**
     * Gère les requêtes GET
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Gère les requêtes POST
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
```

### Étape 4: Créer web.xml

Créez le fichier `src/main/webapp/WEB-INF/web.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- Déclaration du servlet -->
    <servlet>
        <servlet-name>FrontController</servlet-name>
        <servlet-class>nia.framework.FrontControllerServlet</servlet-class>
    </servlet>

    <!-- Mapping : toutes les requêtes vont au FrontController -->
    <servlet-mapping>
        <servlet-name>FrontController</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

</web-app>
```

### Étape 5: Compiler le Projet

```bash
# Se placer à la racine du projet (où se trouve pom.xml)
cd framework-NIA

# Nettoyer les anciens builds
mvn clean

# Compiler le projet
mvn compile

# Empaqueter en WAR
mvn package
```

Après exécution, un fichier `mini-framework-1.0-SNAPSHOT.war` sera généré dans le dossier `target/`.

### Étape 6: Déployer sur Tomcat

#### Option A: Déploiement manuel
1. Copier le fichier `.war` dans `$CATALINA_HOME/webapps/`
2. Redémarrer Tomcat
3. Accéder à: `http://localhost:8080/mini-framework-1.0-SNAPSHOT/`

#### Option B: Déploiement avec Maven (Maven Tomcat Plugin)

Ajouter le plugin dans `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <url>http://localhost:8080/manager/text</url>
        <server>TomcatServer</server>
        <path>/mini-framework</path>
    </configuration>
</plugin>
```

Puis exécuter:
```bash
mvn tomcat7:deploy
```

---

## 🔄 Modifications Requises pour Java 17

### 1. **pom.xml** (Modification principale)
```xml
<!-- AVANT (Java 21) -->
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>

<!-- APRÈS (Java 17) -->
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

### 2. **Vérifier la compatibilité Servlet API**
L'API Servlet 4.0.1 est compatible avec Java 17. Aucune modification requise.

### 3. **Vérifier Java 17 sur votre système**
```bash
java -version
javac -version
```

---

## 🧪 Tests

### Test 1: Via curl (en ligne de commande)

```bash
# Requête GET
curl http://localhost:8080/mini-framework-1.0-SNAPSHOT/test

# Requête POST
curl -X POST http://localhost:8080/mini-framework-1.0-SNAPSHOT/api/users
```

### Test 2: Via un navigateur
Accédez à: `http://localhost:8080/mini-framework-1.0-SNAPSHOT/`

**Résultat attendu:**
```
URL    : /mini-framework-1.0-SNAPSHOT/
Method : GET
```

---

## 📈 Évolutions Possibles

Le framework actuel est basique. Voici comment l'améliorer:

### 1. **Routage Dynamic**
```java
// Ajouter un système de routing basé sur des annotations
@Route("/users")
@Route("/products")
```

### 2. **Gestion des Contrôleurs**
```java
// Créer une interface Controller
public interface Controller {
    void handle(HttpServletRequest req, HttpServletResponse resp);
}
```

### 3. **Injection de Dépendances**
Intégrer un conteneur IoC simple (Spring Framework, Guice, etc.)

### 4. **Gestion des Vues (Template Engine)**
Intégrer:
- Thymeleaf
- Velocity
- FreeMarker

### 5. **ORM Database**
Ajouter:
- JPA/Hibernate
- MyBatis
- Jooq

### 6. **Gestion des Erreurs et Exceptions**
```java
// Créer un gestionnaire d'erreurs global
@Override
public void onError(HttpServletRequest req, HttpServletResponse resp) {
    // Traitement des erreurs
}
```

---

## 🔍 Dépannage

### Problème: "maven-compiler-plugin" n'a pas trouvé la classe
**Solution:** Vérifier que `pom.xml` pointe bien vers Java 17

### Problème: Erreur 404 en accédant à l'application
**Solution:** 
- Vérifier que Tomcat est démarré
- Vérifier le chemin de l'URL
- Vérifier que `web.xml` est correct

### Problème: "javax.servlet" introuvable
**Solution:** S'assurer que les dépendances Servlet sont correctement déclarées dans `pom.xml`

---

## 📚 Ressources Utiles

- [Documentation Maven](https://maven.apache.org/)
- [API Servlet 4.0](https://jakarta.ee/specifications/servlet/4.0/)
- [Apache Tomcat](https://tomcat.apache.org/)
- [Java 17 Documentation](https://docs.oracle.com/javase/17/)

---

## 📄 Licence

Ce projet est fourni à titre d'exemple éducatif.

---

## ✨ Résumé des Modifications pour Java 17

| Fichier | Ligne | Avant | Après |
|---------|-------|-------|-------|
| pom.xml | 13 | `<maven.compiler.source>21</maven.compiler.source>` | `<maven.compiler.source>17</maven.compiler.source>` |
| pom.xml | 14 | `<maven.compiler.target>21</maven.compiler.target>` | `<maven.compiler.target>17</maven.compiler.target>` |

**C'est tout!** Le reste du code est compatible avec Java 17.

---

**Dernière mise à jour:** 2026-06-16
