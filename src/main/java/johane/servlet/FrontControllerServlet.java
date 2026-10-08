package johane.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String pathInfo = request.getRequestURI().substring(request.getContextPath().length()); //recupere url sans le context path
        UrlMethod urlMethod = new UrlMethod(pathInfo, request.getMethod());

        if (LoadingClass.isARouteInsideMappingWithMethod(urlMethod, routesWithMethod)) { //cherche route 
            Mapping mapping = routesWithMethod.get(urlMethod);
            System.out.println("Route trouvée : " + urlMethod + " -> " + mapping);

            try {
                Object controller = mapping.getControllerClass().getDeclaredConstructor().newInstance(); //cree le controller 
                Method controllerMethod = mapping.getMethod(); //recupere le controller 
                Class<?>[] parameterTypes = controllerMethod.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];
                            for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> paramType = parameterTypes[i];

                    if (paramType.equals(ApplicationContext.class)) {
                        parameters[i] = springContext;

                    } else if (paramType.equals(HttpServletRequest.class)) {     // ajouté
                        parameters[i] = request;

                    } else if (paramType.equals(HttpServletResponse.class)) {    // ajouté
                        parameters[i] = response;

                    } else if (paramType.equals(String.class)) {                 // ajouté
                        // le nom vient du nom du paramètre Java -> besoin de "-parameters" à la compilation
                        String paramName = controllerMethod.getParameters()[i].getName();
                        parameters[i] = request.getParameter(paramName);

                    } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) { // ajouté
                        String paramName = controllerMethod.getParameters()[i].getName();
                        String paramValue = request.getParameter(paramName);
                        int value = 0;                                           // 0 si le paramètre est absent
                        if (paramValue != null) {
                            value = Integer.parseInt(paramValue);
                        }
                        parameters[i] = value;
                    } else {
                        parameters[i] = null;
                    }
                }
                Object result = controllerMethod.invoke(controller, parameters); //execute le controller avec les parametres
                if (result==null) {
                    return;
                }
                if (result instanceof ModAndView mav) { //si veux afficher une vue, on recupere les valeurs et on les met dans le request, puis on forward vers la vue
                    if (mav.getValues() != null)
                    {for (Map.Entry<String, Object> en : mav.getValues().entrySet())
                    {
                        request.setAttribute(en.getKey(), en.getValue());
                    }}

                    if (mav.getView() != null && !mav.getView().isBlank()) {
                        String viewPath = viewPrefix + mav.getView() + viewSuffix;
                        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
                        dispatcher.forward(request, response);
                        return;
                    }

                    throw new ServletException("Aucune vue définie pour " + urlMethod);
                    
                } else if (result instanceof String text) {  //si le controller du json ou du text
                    response.setContentType("text/plain;charset=UTF-8");
                   /*  if(LoadingClass.hasAnnotation(mapping.getControllerClass(), annotationRest)) { //si le controller a l'annotation @Rest
                        response.setContentType("application/json;charset=UTF-8");
                    } */
                    if (isRestMethod(controllerMethod)) {
                        response.setContentType("application/json;charset=UTF-8");
                    }

                    try (PrintWriter out = response.getWriter()) { //affiche le text ou le json
                        out.println(text);
                    }
                    return;
                } else{ //si le controller retourne un objet, on le transforme en json
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.setContentType("application/json;charset=UTF-8"); //set le content type en json
                    try (PrintWriter out = response.getWriter()) { // affiche le json
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