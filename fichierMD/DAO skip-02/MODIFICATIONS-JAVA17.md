# ⚙️ Modifications pour Java 17 - Framework NIA

Ce document détaille EXACTEMENT quelles modifications faire pour que le projet fonctionne sur Java 17.

---

## 📊 Vue d'ensemble des Modifications

| Fichier | Nombre de modifications | Facilité |
|---------|------------------------|----------|
| `pom.xml` | **2 lignes** | ✅ Très facile |
| `FrontControllerServlet.java` | **0 modification** | ✅ Aucune |
| `web.xml` | **0 modification** | ✅ Aucune |
| **TOTAL** | **2 lignes à modifier** | ✅ Très simple |

---

## 🔴 MODIFICATION 1: pom.xml - Compiler Source

### Avant (Java 21 - Configuration originale)

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
```

### Après (Java 17 - Configuration adaptée)

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
```

**Détail:**
- **Fichier:** `pom.xml` (à la racine du projet)
- **Ligne approximative:** 13
- **Signification:** Indique à Maven que le code source est écrit en Java 17

---

## 🔴 MODIFICATION 2: pom.xml - Compiler Target

### Avant (Java 21 - Configuration originale)

```xml
<properties>
    <maven.compiler.target>21</maven.compiler.target>
```

### Après (Java 17 - Configuration adaptée)

```xml
<properties>
    <maven.compiler.target>17</maven.compiler.target>
```

**Détail:**
- **Fichier:** `pom.xml` (à la racine du projet)
- **Ligne approximative:** 14
- **Signification:** Indique à Maven de compiler le code pour Java 17

---

## ✅ FICHIERS SANS MODIFICATION REQUISE

### FrontControllerServlet.java
```java
// ✅ AUCUNE MODIFICATION REQUISE
// Le code Java 17 est 100% compatible avec le code écrit pour Java 21
// Les imports javax.servlet.* sont compatibles avec Java 17

package nia.framework;

import java.io.IOException;
import javax.servlet.ServletException;          // ✅ Compatible Java 17
import javax.servlet.http.HttpServlet;          // ✅ Compatible Java 17
import javax.servlet.http.HttpServletRequest;   // ✅ Compatible Java 17
import javax.servlet.http.HttpServletResponse;  // ✅ Compatible Java 17
```

**Pourquoi?**
- Le code Java n'utilise que des API standard
- La Servlet API 4.0.1 fonctionne parfaitement avec Java 17
- Aucune nouvelle syntaxe Java 21 n'est utilisée (records, sealed classes, etc.)

---

### web.xml
```xml
<!-- ✅ AUCUNE MODIFICATION REQUISE -->
<!-- Le format XML du web.xml reste le même pour Java 17 -->
<!-- Les versions de DTD et schéma ne changent pas -->
```

**Pourquoi?**
- `web-app_3_1.xsd` est compatible avec Java 17
- Les namespaces XML ne changent pas
- Aucune configuration Java 17 spécifique n'est requise

---

## 🔍 Vérification Complète du pom.xml

Avant de commencer, voici le pom.xml COMPLET avec les modifications:

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
        <!-- 🔴 MODIFICATION 1: Changé de 21 à 17 -->
        <maven.compiler.source>17</maven.compiler.source>
        
        <!-- 🔴 MODIFICATION 2: Changé de 21 à 17 -->
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

---

## ⚠️ Dépendances - Vérification de Compatibilité

Pour chaque dépendance dans `pom.xml`, voici la compatibilité avec Java 17:

### javax.servlet-api (4.0.1)
```
Nom: javax.servlet-api
Version: 4.0.1
Java 17 Compatible: ✅ OUI (100%)
Java 21 Compatible: ✅ OUI (100%)
Changement requis: ❌ NON
```

### javax.servlet.jsp-api (2.3.3)
```
Nom: javax.servlet.jsp-api
Version: 2.3.3
Java 17 Compatible: ✅ OUI (100%)
Java 21 Compatible: ✅ OUI (100%)
Changement requis: ❌ NON
```

### maven-war-plugin (3.4.0)
```
Nom: maven-war-plugin
Version: 3.4.0
Java 17 Compatible: ✅ OUI (100%)
Java 21 Compatible: ✅ OUI (100%)
Changement requis: ❌ NON
```

---

## 🎯 Étapes Exactes pour Modifier

### Étape 1: Ouvrir le fichier pom.xml

```bash
# Avec nano
nano pom.xml

# Ou avec vim
vim pom.xml

# Ou avec un éditeur graphique
code pom.xml  # VS Code
gedit pom.xml # GNOME Text Editor
```

### Étape 2: Localiser les lignes à modifier

Chercher les lignes:
```
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

### Étape 3: Remplacer 21 par 17

```
<maven.compiler.source>17</maven.compiler.source>  ← Changer 21 en 17
<maven.compiler.target>17</maven.compiler.target>  ← Changer 21 en 17
```

### Étape 4: Sauvegarder le fichier

```bash
# Dans nano: Ctrl+O, puis Entrée, puis Ctrl+X
# Dans vim: :wq
```

### Étape 5: Vérifier la modification

```bash
grep "maven.compiler" pom.xml
# Résultat attendu:
# <maven.compiler.source>17</maven.compiler.source>
# <maven.compiler.target>17</maven.compiler.target>
```

---

## 🔧 Configuration Java 17 sur votre Machine

### Vérifier que Java 17 est installé

```bash
java -version
```

**Résultat attendu:**
```
java version "17.x.x" (ou plus récent)
Java(TM) SE Runtime Environment (build 17.x.x)
Java HotSpot(TM) 64-Bit Server VM (build 17.x.x)
```

### Si Java 17 n'est pas installé

#### Sur Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

#### Sur macOS
```bash
# Avec Homebrew
brew install openjdk@17

# Configurer le PATH
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH=$JAVA_HOME/bin:$PATH
```

#### Sur Windows
```bash
# Télécharger depuis: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
# Installer et ajouter au PATH
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

### Vérifier Maven 3.6+

```bash
mvn -version
```

**Résultat attendu:**
```
Apache Maven 3.6.x (ou plus récent)
Java version: 17.x.x
```

---

## 🧪 Test de Compilation après Modification

Une fois les modifications faites:

```bash
# 1. Nettoyer
mvn clean

# 2. Compiler - DOIT être avec Java 17
mvn compile

# Résultat attendu:
# [INFO] BUILD SUCCESS

# 3. Vérifier les classes compilées
ls -la target/classes/nia/framework/
# Vous devriez voir: FrontControllerServlet.class
```

---

## 📋 Checklist de Modification

- [ ] Ouvrir `pom.xml`
- [ ] Trouver `<maven.compiler.source>21</maven.compiler.source>`
- [ ] Remplacer par `<maven.compiler.source>17</maven.compiler.source>`
- [ ] Trouver `<maven.compiler.target>21</maven.compiler.target>`
- [ ] Remplacer par `<maven.compiler.target>17</maven.compiler.target>`
- [ ] Sauvegarder le fichier
- [ ] Vérifier: `grep "maven.compiler" pom.xml`
- [ ] Exécuter: `mvn clean compile`
- [ ] Vérifier le succès du build

---

## ❌ Erreurs Courantes et Solutions

### Erreur 1: "source version 21 is not supported"

```
[ERROR] source version 21 is not supported
```

**Cause:** Maven essaie de compiler pour Java 21 alors que vous n'avez que Java 17

**Solution:**
```bash
# Vérifier que les modifications ont été sauvegardées
grep "maven.compiler" pom.xml

# Si toujours 21, refaire la modification et sauvegarder
```

---

### Erreur 2: "target version 17 is not supported"

```
[ERROR] target version 17 is not supported
```

**Cause:** Vous n'avez probablement pas Java 17 sur votre système

**Solution:**
```bash
java -version
# Si moins de 17, installer Java 17

# Vérifier le JAVA_HOME
echo $JAVA_HOME
# Doit pointer vers Java 17+
```

---

### Erreur 3: "BUILD FAILURE" après modification

```
[ERROR] BUILD FAILURE
```

**Solution:**
1. Vérifier les modifications dans `pom.xml`
2. Vérifier que Java 17 est le défaut: `java -version`
3. Nettoyer: `mvn clean`
4. Réessayer: `mvn compile`

---

## 📚 Syntaxe Java 17 vs Java 21

### ❌ Java 21 Features (À ÉVITER avec Java 17)

Ces fonctionnalités de Java 21 **ne fonctionneront pas** avec Java 17:

```java
// ❌ Sealed Classes (Java 17 limited, fully in 21)
public sealed class Animal permits Dog, Cat { }

// ❌ Record Patterns (Java 21)
if (obj instanceof Point(int x, int y)) { }

// ❌ Virtual Threads (Java 21)
Thread.ofVirtual().start(() -> { });

// ❌ String Templates (preview in 21)
String msg = STR."Hello \{name}";
```

### ✅ Ce que vous POUVEZ utiliser avec Java 17

```java
// ✅ Records (Java 16, stable in 17)
public record Point(int x, int y) { }

// ✅ Pattern Matching (Java 16, Java 17)
if (obj instanceof String str) { }

// ✅ Sealed Classes (preview in 17, stable in 20)
public sealed class Animal permits Dog, Cat { }

// ✅ Var keyword (Java 10)
var name = "John";

// ✅ Text Blocks (Java 15+)
String sql = """
    SELECT * FROM users
    WHERE id = ?
    """;
```

---

## 🎓 Résumé

**Pour adapter Framework NIA à Java 17:**

1. **Seules 2 lignes du pom.xml doivent être modifiées**
   - Changer `21` en `17` pour `maven.compiler.source`
   - Changer `21` en `17` pour `maven.compiler.target`

2. **Aucune modification du code Java requise**
   - `FrontControllerServlet.java` reste exactement pareil
   - `web.xml` reste exactement pareil

3. **Vérifier que Java 17 est installé**
   - `java -version` doit afficher 17.x.x

4. **Compiler et tester**
   - `mvn clean compile` doit réussir
   - `mvn package` doit générer le WAR

**C'est tout! Le projet est alors compatible Java 17. 🚀**

---

## 🔗 Ressources Utiles

- [Oracle Java 17 Documentation](https://docs.oracle.com/javase/17/)
- [Maven Compiler Plugin](https://maven.apache.org/plugins/maven-compiler-plugin/)
- [Java 17 Release Notes](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Servlet API 4.0 Documentation](https://javaee.github.io/servlet-spec/)

---

**Dernière mise à jour:** 2026-06-16
**Auteur:** Framework NIA Team
