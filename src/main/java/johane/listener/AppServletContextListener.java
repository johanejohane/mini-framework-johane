package johane.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap; 
import org.springframework.context.ApplicationContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import johane.util.LoadingClass;
import johane.util.Mapping;
import johane.util.MySession;
import johane.util.UrlMethod; 
public class AppServletContextListener implements ServletContextListener {

    String packageName;
    String viewPrefix;
    String viewSuffix;
    String annotationRest;
    Map<UrlMethod, Mapping> toutesLesRoutes;
    Map<String, MySession> mySessionsById; 
    String userSessionKey;
    String roleSessionKey;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[INIT] Tomcat démarre l'application. Lancement du scan des routes...");

        try {
            ApplicationContext springContext = org.springframework.web.context.support.WebApplicationContextUtils
            .getRequiredWebApplicationContext(sce.getServletContext());

            userSessionKey = requiredInitParam(sce, "auth.user-session-key");
            roleSessionKey = requiredInitParam(sce, "auth.role-session-key");

            mySessionsById = new ConcurrentHashMap<>();

            toutesLesRoutes = new HashMap<>();
            Properties prop = new Properties();
            try (InputStream input = LoadingClass.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new RuntimeException("[ERREUR] Impossible de trouver le fichier config.properties.");
                }
                prop.load(input);

                packageName = prop.getProperty("app.package");

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la lecture de config.properties", e);
            }

            LoadingClass.loadUrlMappingsWithMethod(packageName, toutesLesRoutes);
            viewPrefix = sce.getServletContext().getInitParameter("view.prefix");
            viewSuffix = sce.getServletContext().getInitParameter("view.suffix");

            annotationRest = prop.getProperty("annotation.rest");

            sce.getServletContext().setAttribute("routesWithMethod", toutesLesRoutes);
            sce.getServletContext().setAttribute("prefix", viewPrefix);
            sce.getServletContext().setAttribute("suffix", viewSuffix);
            sce.getServletContext().setAttribute("springContext", springContext);
            sce.getServletContext().setAttribute("annotationRest", annotationRest);
            sce.getServletContext().setAttribute("userSessionKey", userSessionKey);
            sce.getServletContext().setAttribute("roleSessionKey", roleSessionKey);
            sce.getServletContext().setAttribute("sessions", mySessionsById); 

            System.out.println("[SUCCESS] Scan terminé avec succès. " + toutesLesRoutes.size() + " routes chargées.");

        } catch (IllegalStateException e) {
            System.err.println("[ERREUR CRITIQUE DÉMARRAGE] " + e.getMessage());
            throw new RuntimeException("Déploiement impossible → " + e.getMessage(), e);
        }
    }

    
    private String requiredInitParam(ServletContextEvent sce, String name) {
        String value = sce.getServletContext().getInitParameter(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Paramètre obligatoire absent du web.xml : " + name);
        }
        return value;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[SHUTDOWN] L'application s'arrête.");
    }
}