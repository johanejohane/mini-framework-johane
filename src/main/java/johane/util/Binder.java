package johane.util;

import java.lang.reflect.Method;
import java.time.LocalDate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part; // [AJOUT] pour lire les fichiers uploadés (ancienne ligne 6 : seul HttpServletRequest était importé)

public class Binder {

    public static Object bind(Class<?> clazz, HttpServletRequest request) throws Exception {
        return bind(clazz, request, "");
    }

    // [AJOUT] Version récursive. prefix = "" au premier niveau, "adresse." au niveau suivant, etc.
    // Exemple : formulaire <input name="adresse.ville"> -> employe.getAdresse().setVille(...)
    private static Object bind(Class<?> clazz, HttpServletRequest request, String prefix) throws Exception {
        Object obj = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getMethods()) {
            if (!method.getName().startsWith("set") || method.getParameterCount() != 1)
                continue;

            // nom de la propriété déduit du setter : setNom -> "nom"
            String prop = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);
            String fullName = prefix + prop; // nom complet du champ dans le formulaire
            Class<?> type = method.getParameterTypes()[0];

            if (isSimple(type)) {
                // Cas 1 : type simple (String, int, LocalDate, enum...) -> même logique qu'avant
                String value = request.getParameter(fullName);
                if (value == null || value.isBlank()) // [MODIF] isBlank() au lieu de isEmpty()
                    continue;
                method.invoke(obj, convert(value, type));

            } else if (type == FileUpload.class) {
                // Cas 2 [AJOUT] : un seul fichier uploadé
                if (!FileUpload.isMultipart(request))
                    continue;
                Part part = request.getPart(fullName);
                FileUpload file = part == null ? null : FileUpload.convertToFileUpload(part);
                if (file != null)
                    method.invoke(obj, file);

            } else if (type == FileUpload[].class) {
                // Cas 3 [AJOUT] : plusieurs fichiers uploadés
                if (!FileUpload.isMultipart(request))
                    continue;
                FileUpload[] files = FileUpload.fromAll(request, fullName);
                if (files.length > 0)
                    method.invoke(obj, (Object) files);

            } else if (hasParamStartingWith(request, fullName + ".")) {
                // Cas 4 [AJOUT] : objet imbriqué -> on le remplit récursivement
                Object nested = bind(type, request, fullName + ".");
                method.invoke(obj, nested);
            }
        }
        return obj;
    }

    // [AJOUT] Vrai s'il existe un paramètre (ou une part multipart) dont le nom commence par "start"
    private static boolean hasParamStartingWith(HttpServletRequest request, String start) throws Exception {
        for (String key : request.getParameterMap().keySet()) {
            if (key.startsWith(start))
                return true;
        }
        if (FileUpload.isMultipart(request)) {
            for (Part part : request.getParts()) {
                if (part.getName().startsWith(start))
                    return true;
            }
        }
        return false;
    }

    // [AJOUT] Vrai pour les types qu'on sait convertir directement depuis une String
    private static boolean isSimple(Class<?> t) {
        return t.isPrimitive()
                || t == String.class
                || Number.class.isAssignableFrom(t)
                || t == Boolean.class
                || t == Character.class
                || t == LocalDate.class
                || t.isEnum();
    }

    // Inchangé par rapport à johane (identique à roro)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object convert(String v, Class<?> t) {
        if (t == String.class)
            return v;
        if (t == int.class || t == Integer.class)
            return Integer.valueOf(v);
        if (t == byte.class || t == Byte.class)
            return Byte.valueOf(v);
        if (t == short.class || t == Short.class)
            return Short.valueOf(v);
        if (t == long.class || t == Long.class)
            return Long.valueOf(v);
        if (t == float.class || t == Float.class)
            return Float.valueOf(v);
        if (t == double.class || t == Double.class)
            return Double.valueOf(v);
        if (t == boolean.class || t == Boolean.class)
            return Boolean.valueOf(v);
        if (t == char.class || t == Character.class) {
            if (v.length() != 1)
                throw new IllegalArgumentException("Expected a single character: " + v);
            return v.charAt(0);
        }
        if (t == LocalDate.class)
            return LocalDate.parse(v);
        if (t.isEnum())
            return Enum.valueOf((Class<Enum>) t, v);
        throw new IllegalArgumentException("Unsupported type: " + t.getName());
    }
}