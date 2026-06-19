package johane.util;

import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import johane.annotation.MonController;
public class LoadingClass {
    public static boolean hasAnnotation(Class<?>classscanned)
    {
        return classscanned.isAnnotationPresent(MonController.class);
    }

    public static List<String> loadClassWithMyAnnotation(String packageName, String monAnnotation) {
        List<String> listeClasse = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(packageName).scan()) {
            ClassInfoList classesAvecAnnotation = scanResult.getClassesWithAnnotation(monAnnotation);
            for (ClassInfo classInfo : classesAvecAnnotation) {
                listeClasse.add(classInfo.getName());
            }
        }
        return listeClasse;
    }

    public static List<String> loadClassWithMyAnnotation(String monAnnotation) {
        List<String> listeClasse = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .scan()) {

            ClassInfoList classesAvecAnnotation = scanResult.getClassesWithAnnotation(monAnnotation);
            for (ClassInfo classInfo : classesAvecAnnotation) {
                listeClasse.add(classInfo.getName());
            }
        }
        return listeClasse;
    }

    public static List<String> loadAllClasses() {
        List<String> listeClasse = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .scan()) {

            ClassInfoList classesAvecAnnotation = scanResult.getAllClasses();
            for (ClassInfo classInfo : classesAvecAnnotation) {
                listeClasse.add(classInfo.getName());
            }
        }
        return listeClasse;
    }
}
