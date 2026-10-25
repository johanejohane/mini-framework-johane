package johane.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Session "maison" donnée au controller quand il déclare un paramètre MySession.
 * Elle contient une copie des attributs de la HttpSession courante : le controller
 * la lit / la modifie, puis le FrontControllerServlet recopie les changements
 * dans la vraie HttpSession (voir FrontControllerServlet.flushSessions).
 */
public class MySession {

    private Map<String, Object> sessionData;

    public MySession() {
        this.sessionData = new LinkedHashMap<>();
    }

    public MySession(Map<String, Object> sessionData) {
        this.sessionData = sessionData == null ? new LinkedHashMap<>() : sessionData;
    }

    public Map<String, Object> getSessionData() {
        return sessionData;
    }

    public void setSessionData(Map<String, Object> sessionData) {
        this.sessionData = sessionData == null ? new LinkedHashMap<>() : sessionData;
    }

    public Object getAttribute(String key) {
        return sessionData.get(key);
    }

    public void setAttribute(String key, Object value) {
        sessionData.put(key, value);
    }

    public void removeAttribute(String key) {
        sessionData.remove(key);
    }

    /** Vide la session : à la fin de la requête, tous les attributs de la HttpSession sont supprimés. */
    public void destroy() {
        sessionData.clear();
    }

    // ---- petits ajouts par rapport au strict minimum ----

    public boolean hasAttribute(String key) {
        return sessionData.containsKey(key);
    }

    public Set<String> getAttributeNames() {
        return sessionData.keySet();
    }
}