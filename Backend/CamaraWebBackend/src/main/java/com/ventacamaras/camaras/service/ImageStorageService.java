package com.ventacamaras.camaras.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageStorageService {
    @Value("${app.upload.dir:uploads}") private String uploadDir;
    private static final long MAX_SIZE = 5 * 1024 * 1024;
    private static final Set<String> TIPOS = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    public String guardar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) throw new IllegalArgumentException("Selecciona una imagen");
        if (archivo.getSize() > MAX_SIZE) throw new IllegalArgumentException("La imagen no puede superar 5 MB");
        if (!TIPOS.contains(archivo.getContentType())) throw new IllegalArgumentException("Formato de imagen no permitido");
        String original = archivo.getOriginalFilename() == null ? "imagen" : archivo.getOriginalFilename();
        String extension = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        String nombre = UUID.randomUUID() + extension;
        try {
            Path directorio = Files.createDirectories(Path.of(uploadDir).toAbsolutePath().normalize());
            archivo.transferTo(directorio.resolve(nombre));
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar la imagen");
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/").path(nombre).toUriString();
    }

    /** Elimina únicamente archivos administrados por esta aplicación. */
    public void eliminar(String url) {
        if (url == null || url.isBlank()) return;
        try {
            String path = URI.create(url).getPath();
            String prefijo = "/uploads/";
            if (path == null || !path.startsWith(prefijo)) return;

            String nombre = Path.of(path.substring(prefijo.length())).getFileName().toString();
            Path directorio = Path.of(uploadDir).toAbsolutePath().normalize();
            Path archivo = directorio.resolve(nombre).normalize();
            if (!archivo.startsWith(directorio)) {
                throw new IllegalArgumentException("Ruta de imagen no permitida");
            }
            Files.deleteIfExists(archivo);
        } catch (IllegalArgumentException ex) {
            // Las URLs externas solo se eliminan de la base de datos.
        } catch (IOException ex) {
            // Un archivo bloqueado o ya movido no debe impedir que se quite la
            // imagen del catálogo. Se registra para poder limpiar el volumen luego.
            log.warn("No se pudo eliminar el archivo local asociado a {}: {}", url, ex.getMessage());
        }
    }
}

