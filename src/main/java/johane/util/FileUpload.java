package johane.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class FileUpload {
    private String fileName; // nom complet : "photo.png"
    private String name; // nom sans extension : "photo"
    private String extension; // extension en minuscules : "png"
    private String contentType; // type MIME : "image/png"
    private byte[] bytes; // contenu du fichier

    public FileUpload() {
    }

    public FileUpload(String fileName, String name, String extension, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.name = name;
        this.extension = extension;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    // Convertit une Part (partie multipart de la requête) en FileUpload.
    // Retourne null si aucun fichier n'a été choisi dans le formulaire.
    public static FileUpload convertToFileUpload(Part part) throws IOException {
        String submitted = part.getSubmittedFileName();
        if (submitted == null || part.getSize() == 0)
            return null;

        // On garde seulement le nom du fichier (on enlève "C:\..." ou "../" pour la sécurité)
        String fileName = submitted.substring(Math.max(submitted.lastIndexOf('/'), submitted.lastIndexOf('\\')) + 1);

        int dot = fileName.lastIndexOf('.');
        String name = dot > 0 ? fileName.substring(0, dot) : fileName;
        String extension = dot > 0 ? fileName.substring(dot + 1).toLowerCase() : "";

        byte[] bytes;
        try (InputStream in = part.getInputStream()) {
            bytes = in.readAllBytes();
        }
        return new FileUpload(fileName, name, extension, part.getContentType(), bytes);
    }

    // Vrai si le formulaire a été envoyé avec enctype="multipart/form-data"
    public static boolean isMultipart(HttpServletRequest request) {
        String ct = request.getContentType();
        return ct != null && ct.toLowerCase().startsWith("multipart/");
    }

    // Récupère tous les fichiers envoyés sous un même nom (input type="file" multiple)
    public static FileUpload[] fromAll(HttpServletRequest request, String name) throws IOException, ServletException {
        List<FileUpload> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals(name)) {
                FileUpload f = convertToFileUpload(part);
                if (f != null)
                    files.add(f);
            }
        }
        return files.toArray(new FileUpload[0]);
    }
}