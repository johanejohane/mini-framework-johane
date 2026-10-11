package johane.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import johane.util.Binder;
import johane.util.LoadingClass;
import johane.util.Mapping;
import johane.util.ModAndView;
import johane.util.UrlMethod;

public class FrontControllerServlet extends HttpServlet {

    Map<UrlMethod, Mapping> routesWithMethod;
    String viewPrefix;
    String viewSuffix;
    String annotationRest;
    ApplicationContext springContext;

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ServletException {
        super.init();
        routesWithMethod = (Map<UrlMethod, Mapping>) getServletContext().getAttribute("routesWithMethod");
        viewPrefix = (String) getServletContext().getAttribute("prefix");
        viewSuffix = (String) getServletContext().getAttribute("suffix");
        annotationRest = (String) getServletContext().getAttribute("annotationRest");
        springContext = (ApplicationContext) getServletContext().getAttribute("springContext");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String pathInfo = request.getRequestURI().substring(request.getContextPath().length()); // url sans le context path
        UrlMethod urlMethod = new UrlMethod(pathInfo, request.getMethod());

        if (LoadingClass.isARouteInsideMappingWithMethod(urlMethod, routesWithMethod)) { // cherche la route
            Mapping mapping = routesWithMethod.get(urlMethod);
            System.out.println("Route trouvée : " + urlMethod + " -> " + mapping);

            try {
                Object controller = mapping.getControllerClass().getDeclaredConstructor().newInstance(); // crée le controller
                Method controllerMethod = mapping.getMethod(); // récupère la méthode
                Class<?>[] parameterTypes = controllerMethod.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters[i] = populateParameter(controllerMethod, i, request, response, urlMethod);
                }
                Object result = controllerMethod.invoke(controller, parameters); // exécute le controller

                if (result == null) {
                    return;
                }
                if (result instanceof ModAndView mav) { // vue : valeurs -> request, puis forward
                    if (mav.getValues() != null) {
                        for (Map.Entry<String, Object> en : mav.getValues().entrySet()) {
                            request.setAttribute(en.getKey(), en.getValue());
                        }
                    }

                    if (mav.getView() != null && !mav.getView().isBlank()) {
                        String viewPath = viewPrefix + mav.getView() + viewSuffix;
                        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
                        dispatcher.forward(request, response);
                        return;
                    }

                    throw new ServletException("Aucune vue définie pour " + urlMethod);

                } else if (result instanceof String text) { // texte ou json
                    response.setContentType("text/plain;charset=UTF-8");
                    if (isRestMethod(controllerMethod)) {
                        response.setContentType("application/json;charset=UTF-8");
                    }
                    try (PrintWriter out = response.getWriter()) {
                        out.println(text);
                    }
                    return;
                } else { // objet : transformé en json
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.setContentType("application/json;charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        String json = objectMapper.writeValueAsString(result);
                        out.println(json);
                    }
                    return;
                }

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException("Impossible d'exécuter la méthode liée à " + urlMethod, e);
            }
        } else {
            response.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println("Aucune route trouvée pour l'URL : " + pathInfo);
                routesWithMethod.forEach((urlMethodKey, mapping) -> {
                    out.println(urlMethodKey + " -> " + mapping.getClassName() + "->" + mapping.getMethod().getName()
                            + "()");
                });
            }
        }
    }

    private Object populateParameter(Method controllerMethod, int index, HttpServletRequest request,
            HttpServletResponse response, UrlMethod urlMethod) throws ServletException {
        Class<?> paramType = controllerMethod.getParameterTypes()[index];
        String paramName = controllerMethod.getParameters()[index].getName();

        if (paramType.equals(ApplicationContext.class)) {
            return springContext;
        } else if (paramType.equals(HttpServletRequest.class)) {
            return request;
        } else if (paramType.equals(HttpServletResponse.class)) {
            return response;
        } else if (paramType.equals(String.class)) {
            return request.getParameter(paramName);
        } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(int.class)) return 0;
            } else {
                return Integer.valueOf(value);
            }
        } else if (paramType.equals(byte.class) || paramType.equals(Byte.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(byte.class)) return (byte) 0;
            } else {
                return Byte.valueOf(value);
            }
        } else if (paramType.equals(short.class) || paramType.equals(Short.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(short.class)) return (short) 0;
            } else {
                return Short.valueOf(value);
            }
        } else if (paramType.equals(long.class) || paramType.equals(Long.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(long.class)) return 0L;
            } else {
                return Long.valueOf(value);
            }
        } else if (paramType.equals(float.class) || paramType.equals(Float.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(float.class)) return 0F;
            } else {
                return Float.valueOf(value);
            }
        } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(double.class)) return 0D;
            } else {
                return Double.valueOf(value);
            }
        } else if (paramType.equals(boolean.class) || paramType.equals(Boolean.class)) {
            String value = request.getParameter(paramName);
            if (value == null) {
                if (paramType.equals(boolean.class)) return false;
            } else {
                return Boolean.valueOf(value);
            }
        } else if (paramType.equals(char.class) || paramType.equals(Character.class)) {
            String value = request.getParameter(paramName);
            if (value != null) {
                if (value.length() != 1)
                    throw new ServletException("Le paramètre doit contenir un seul caractère : " + value);
                return value.charAt(0);
            } else if (paramType.equals(char.class)) {
                return '\0';
            }
        } else if (paramType.equals(LocalDate.class)) {
            String value = request.getParameter(paramName);
            if (value != null) return LocalDate.parse(value);
        } else if (!paramType.isPrimitive()) {
            // tout autre type objet (ex: Employe) : on remplit ses setters avec les paramètres de la requête
            try {
                return Binder.bind(paramType, request);
            } catch (Exception e) {
                throw new ServletException("Erreur lors de la liaison des paramètres pour " + urlMethod, e);
            }
        }

        return null;
    }

    private boolean isRestMethod(Method method) throws ServletException {
        try {
            Class<? extends Annotation> restAnnotationClass = Class.forName(annotationRest)
                    .asSubclass(Annotation.class);
            return method.isAnnotationPresent(restAnnotationClass);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Annotation REST introuvable : " + annotationRest, e);
        }
    }
}