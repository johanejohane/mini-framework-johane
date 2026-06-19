# 📄 Fichiers Sources Complets - Framework NIA

Tous les fichiers source du projet prêts à copier-coller.

---

## 📋 Table des Matières

1. [pom.xml](#pomxml)
2. [FrontControllerServlet.java](#frontcontrollerservletjava)
3. [web.xml](#webxml)
4. [.gitignore](#gitignore)
5. [Autres fichiers optionnels](#autres-fichiers-optionnels)

---

## pom.xml

**Emplacement:** `framework-NIA/pom.xml`

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
```

**Points clés pour Java 17:**
- `<maven.compiler.source>17</maven.compiler.source>` ✅ DOIT être à 17
- `<maven.compiler.target>17</maven.compiler.target>` ✅ DOIT être à 17
- Les dépendances Servlet 4.0.1 et JSP 2.3.3 sont compatibles avec Java 17

---

## FrontControllerServlet.java

**Emplacement:** `framework-NIA/src/main/java/nia/framework/FrontControllerServlet.java`

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
 * Le pattern Front Controller centralise le traitement de toutes les requêtes HTTP
 * dans un unique servlet. Cela permet un contrôle centralisé du flux.
 * 
 * Exemple d'utilisation:
 * - GET /users      → FrontControllerServlet → Contrôle l'affichage
 * - POST /users     → FrontControllerServlet → Traitement de création
 * - GET /products   → FrontControllerServlet → Contrôle l'affichage
 * 
 * @author Framework NIA
 * @version 1.0
 */
public class FrontControllerServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Traite les requêtes HTTP (GET/POST/etc)
     * 
     * Cette méthode est appelée par doGet() et doPost() pour centraliser
     * la logique de traitement.
     *
     * @param req  - Objet HttpServletRequest contenant les infos de la requête
     * @param resp - Objet HttpServletResponse pour envoyer la réponse
     * @throws ServletException - En cas d'erreur servlet
     * @throws IOException      - En cas d'erreur I/O
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // ========== Récupération des informations ==========
        
        String url = req.getRequestURI();          // Ex: /mini-framework/users/123
        String method = req.getMethod();            // Ex: GET, POST, PUT, DELETE
        String contextPath = req.getContextPath();  // Ex: /mini-framework
        String servletPath = req.getServletPath();  // Ex: /
        String pathInfo = req.getPathInfo();        // Ex: users/123
        String queryString = req.getQueryString();  // Ex: id=1&name=john
        
        // ========== Configuration de la réponse ==========
        
        resp.setContentType("text/plain;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        // ========== Génération de la réponse ==========
        
        resp.getWriter().println("╔════════════════════════════════════════╗");
        resp.getWriter().println("║      FRONT CONTROLLER SERVLET          ║");
        resp.getWriter().println("╚════════════════════════════════════════╝");
        resp.getWriter().println("");
        resp.getWriter().println("📝 INFOS REQUÊTE:");
        resp.getWriter().println("   URL complet       : " + url);
        resp.getWriter().println("   Context Path     : " + contextPath);
        resp.getWriter().println("   Servlet Path     : " + servletPath);
        resp.getWriter().println("   Path Info        : " + pathInfo);
        resp.getWriter().println("   Méthode HTTP     : " + method);
        resp.getWriter().println("   Query String     : " + queryString);
        resp.getWriter().println("");
        resp.getWriter().println("✅ Requête reçue et traitée avec succès!");
        resp.getWriter().println("");
    }
    
    /**
     * Traite les requêtes GET
     * 
     * Appelé lors de:
     * - Accès direct via le navigateur
     * - Requête curl: curl http://localhost:8080/...
     * - Lien hypertexte
     *
     * @param req  - Requête HTTP
     * @param resp - Réponse HTTP
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    /**
     * Traite les requêtes POST
     * 
     * Appelé lors de:
     * - Soumission de formulaires HTML
     * - Requête curl: curl -X POST http://localhost:8080/...
     * - Envoi de données en body
     *
     * @param req  - Requête HTTP
     * @param resp - Réponse HTTP
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
    
    /**
     * 🔄 Possibilités d'amélioration:
     * 
     * 1. Ajouter doPut() et doDelete() pour REST API
     * 2. Ajouter un système de routage URL
     * 3. Intégrer un conteneur IoC (Injection de dépendances)
     * 4. Ajouter des contrôleurs (Controller Pattern)
     * 5. Ajouter la gestion des erreurs (Exception Handling)
     * 6. Ajouter un système de logging
     * 7. Ajouter une couche de vue (JSP, Thymeleaf, etc.)
     * 8. Ajouter l'accès à la base de données (ORM)
     */
}
```

**Hérite de:**
- `HttpServlet` - Classe parent Servlet

**Méthodes implémentées:**
- `doGet()` - Traite les requêtes GET
- `doPost()` - Traite les requêtes POST
- `processRequest()` - Logique commune

**Modifications pour Java 17:**
- Aucune modification spécifique requise ✅
- Compatible avec Java 17+

---

## web.xml

**Emplacement:** `framework-NIA/src/main/webapp/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
         http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- 
        ═══════════════════════════════════════════════════════════════
        Descripteur de Déploiement - web.xml
        
        Ce fichier configure le serveur d'application (Tomcat, etc.)
        sur comment traiter les requêtes HTTP.
        ═══════════════════════════════════════════════════════════════
    -->

    <!-- ─────────────────────────────────────────────────────────────
         1. DÉCLARATION DU SERVLET
         ───────────────────────────────────────────────────────────── -->
    
    <servlet>
        <!-- Nom interne du servlet (utilisé dans le mapping) -->
        <servlet-name>FrontController</servlet-name>
        
        <!-- Chemin complet de la classe du servlet -->
        <servlet-class>nia.framework.FrontControllerServlet</servlet-class>
        
        <!-- 
            load-on-startup:
            - Valeur > 0: Charger le servlet au démarrage (optionnel)
            - La valeur indique l'ordre de chargement (1 = premier)
        -->
        <load-on-startup>1</load-on-startup>
        
        <!-- Configuration optionnelle des paramètres d'initialisation -->
        <init-param>
            <param-name>debug</param-name>
            <param-value>true</param-value>
        </init-param>
    </servlet>

    <!-- ─────────────────────────────────────────────────────────────
         2. MAPPING URL → SERVLET
         
         Définit quelles URLs sont traitées par quel servlet
         ───────────────────────────────────────────────────────────── -->
    
    <servlet-mapping>
        <!-- Doit correspondre au servlet-name ci-dessus -->
        <servlet-name>FrontController</servlet-name>
        
        <!-- 
            url-pattern: Le pattern d'URL
            
            Exemples:
            - /* = TOUTES les requêtes (ce qu'on utilise ici)
            - /api/* = Seulement les requêtes commençant par /api
            - *.do = Seulement les URL terminant par .do
            - /admin = Seulement la requête /admin exactement
        -->
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

    <!-- ─────────────────────────────────────────────────────────────
         3. CONFIGURATION DE SESSION (optionnel)
         ───────────────────────────────────────────────────────────── -->
    
    <session-config>
        <!-- Méthode de suivi de session: COOKIE -->
        <tracking-mode>COOKIE</tracking-mode>
        
        <!-- Durée de vie de la session en minutes -->
        <cookie-config>
            <http-only>true</http-only>
            <secure>false</secure>
        </cookie-config>
    </session-config>

    <!-- ─────────────────────────────────────────────────────────────
         4. PAGES D'ERREUR (optionnel)
         ───────────────────────────────────────────────────────────── -->
    
    <!-- Rediriger les erreurs 404 vers une page personnalisée (si elle existe)
    <error-page>
        <error-code>404</error-code>
        <location>/error/404.jsp</location>
    </error-page>
    
    <error-page>
        <error-code>500</error-code>
        <location>/error/500.jsp</location>
    </error-page>
    -->

    <!-- ─────────────────────────────────────────────────────────────
         5. WELCOME FILES (optionnel)
         ───────────────────────────────────────────────────────────── -->
    
    <!-- Les fichiers affichés par défaut lors d'un accès à un répertoire
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
    -->

</web-app>
```

**Points clés:**

| Élément | Rôle | Valeur |
|---------|------|--------|
| `<servlet-name>` | Identifiant interne | `FrontController` |
| `<servlet-class>` | Chemin complet Java | `nia.framework.FrontControllerServlet` |
| `<url-pattern>` | Les URLs à traiter | `/*` = toutes |
| `<load-on-startup>` | Chargement au démarrage | `1` = oui |

---

## .gitignore

**Emplacement:** `framework-NIA/.gitignore`

```
# ═══════════════════════════════════════════════════════════════
# GIT IGNORE - Framework NIA
# ═══════════════════════════════════════════════════════════════

# ───────────────────────────────────────────────────────────────
# 1. Répertoires Maven
# ───────────────────────────────────────────────────────────────
target/
.m2/
repository/

# ───────────────────────────────────────────────────────────────
# 2. IDE - IntelliJ IDEA
# ───────────────────────────────────────────────────────────────
.idea/
*.iml
*.iws
*.ipr
out/

# ───────────────────────────────────────────────────────────────
# 3. IDE - Eclipse
# ───────────────────────────────────────────────────────────────
.classpath
.project
.settings/
bin/

# ───────────────────────────────────────────────────────────────
# 4. IDE - VS Code
# ───────────────────────────────────────────────────────────────
.vscode/
.history/

# ───────────────────────────────────────────────────────────────
# 5. IDE - NetBeans
# ───────────────────────────────────────────────────────────────
nbproject/
*.nbattrs

# ───────────────────────────────────────────────────────────────
# 6. Fichiers temporaires et OS
# ───────────────────────────────────────────────────────────────
*.swp
*.swo
*~
.DS_Store
Thumbs.db

# ───────────────────────────────────────────────────────────────
# 7. Logs
# ───────────────────────────────────────────────────────────────
*.log
logs/

# ───────────────────────────────────────────────────────────────
# 8. Fichiers de build
# ───────────────────────────────────────────────────────────────
*.war
*.ear
*.class

# ───────────────────────────────────────────────────────────────
# 9. Fichiers de configuration personnels
# ───────────────────────────────────────────────────────────────
.env
local.properties
custom.properties

# ───────────────────────────────────────────────────────────────
# 10. Archive
# ───────────────────────────────────────────────────────────────
*.zip
*.tar.gz
```

---

## Autres Fichiers Optionnels

### README.md

**Emplacement:** `framework-NIA/README.md`

```markdown
# Framework NIA

Un mini framework web Java utilisant le pattern Front Controller.

## Démarrage rapide

```bash
mvn clean package
# Copier le WAR dans Tomcat
cp target/mini-framework-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
# Accéder à http://localhost:8080/mini-framework-1.0-SNAPSHOT/
```

## Prérequis

- Java 17+
- Maven 3.6+
- Tomcat 9+

## Structure du Projet

```
framework-NIA/
├── pom.xml
├── src/main/java/nia/framework/
│   └── FrontControllerServlet.java
└── src/main/webapp/WEB-INF/
    └── web.xml
```

## Licence

Licence MIT
```

---

### pom.xml Avancé (avec dépendances supplémentaires)

Si vous voulez ajouter des dépendances plus tard:

```xml
<!-- Ajouter après </dependencies> existantes: -->

<!-- Logging avec SLF4J -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.7</version>
</dependency>

<!-- JSON Processing -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
```

---

## ✅ Checklist de Création

- [ ] Créer dossier `framework-NIA/`
- [ ] Créer structure Maven (`src/main/java`, `src/main/webapp/WEB-INF`)
- [ ] Créer `pom.xml`
- [ ] Créer `FrontControllerServlet.java`
- [ ] Créer `web.xml`
- [ ] Créer `.gitignore` (optionnel)
- [ ] Exécuter `mvn compile`
- [ ] Exécuter `mvn package`
- [ ] Copier WAR dans Tomcat
- [ ] Tester via navigateur

---

## 📞 Problèmes Courants

### Erreur: "Cannot resolve symbol 'HttpServlet'"
→ Vérifier que `javax.servlet-api` est dans `pom.xml`

### Erreur: "ClassNotFoundException"
→ Vérifier le chemin complet: `nia.framework.FrontControllerServlet`

### Erreur 404 au démarrage
→ Attendre 30-60 secondes pour le déploiement

---

**Tous les fichiers sont prêts à copier-coller! 🚀**
