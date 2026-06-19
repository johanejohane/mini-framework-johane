package johane.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    List<String> listeClasse = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        super.init();
        // packageName corrigé : "com.app" (le vrai package des contrôleurs)
        String packageName = "com.app";
        String monAnnotation = "johane.annotation.MonController";
        // appel corrigé : johane.util.LoadingClass (et non roro.util.LoadingClass)
        listeClasse = johane.util.LoadingClass.loadClassWithMyAnnotation(packageName, monAnnotation);
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
            throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String url = request.getRequestURL().toString();

        try (PrintWriter out = response.getWriter()) {
            out.println("URL interceptee : " + url);
            out.println("-Framework maison de Johane-");

            for (String classe : listeClasse) {
                out.println(classe);
            }
        }
    }
}