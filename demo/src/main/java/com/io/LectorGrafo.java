package com.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase LectorGrafo: Lee un archivo de texto que representa un grafo dirigido
 * y construye la estructura de datos correspondiente.
 * 
 * Formato del archivo:
 * CiudadOrigen CiudadDestino Distancia
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class LectorGrafo {
    
    private String rutaArchivo;
    private Grafo grafo;
    
    /**
     * Constructor: Inicializa el lector con la ruta del archivo.
     * 
     * @param rutaArchivo Ruta del archivo a leer
     * @pre rutaArchivo no debe ser nulo ni vacío
     */
    public LectorGrafo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.grafo = new Grafo();
    }
    
    /**
     * Lee el archivo y construye el grafo.
     * 
     * @return El grafo construido con los datos del archivo
     * @throws IOException si ocurre un error al leer el archivo
     * @throws IllegalArgumentException si el formato del archivo es inválido
     * @post Se retorna un grafo completo con nodos y arcos
     */
    public Grafo leerGrafo() throws IOException {
        try (BufferedReader lector = new BufferedReader(new FileReader("demo/src/main/resources/grafo.txt"))) {
            String linea;
            Set<String> ciudadesAgregadas = new HashSet<>();
            int numeroLinea = 0;
            
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();
                
                // Ignorar líneas vacías y comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                
                // Dividir la línea por espacios
                String[] partes = linea.split("\\s+");
                
                if (partes.length != 3) {
                    throw new IllegalArgumentException(
                            String.format("Formato inválido en línea %d: se esperaban 3 campos", numeroLinea));
                }
                
                String ciudadOrigen = partes[0];
                String ciudadDestino = partes[1];
                double distancia;
                
                // Validar que la distancia sea un número válido
                try {
                    distancia = Double.parseDouble(partes[2]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            String.format("Distancia inválida en línea %d: %s", numeroLinea, partes[2]));
                }
                
                // Validar distancia no negativa
                if (distancia < 0) {
                    throw new IllegalArgumentException(
                            String.format("Distancia negativa en línea %d", numeroLinea));
                }
                
                // Agregar ciudades si no existen
                if (!ciudadesAgregadas.contains(ciudadOrigen)) {
                    grafo.agregarNodo(ciudadOrigen);
                    ciudadesAgregadas.add(ciudadOrigen);
                }
                
                if (!ciudadesAgregadas.contains(ciudadDestino)) {
                    grafo.agregarNodo(ciudadDestino);
                    ciudadesAgregadas.add(ciudadDestino);
                }
                
                // Agregar arco
                if (!grafo.agregarArco(ciudadOrigen, ciudadDestino, distancia)) {
                    throw new IllegalArgumentException(
                            String.format("Error al agregar arco en línea %d", numeroLinea));
                }
            }
            
            if (grafo.obtenerTotalNodos() == 0) {
                throw new IllegalArgumentException("El archivo no contiene datos válidos");
            }
        }
        
        return grafo;
    }
    
    /**
     * Retorna el grafo construido.
     * 
     * @return El grafo
     */
    public Grafo obtenerGrafo() {
        return grafo;
    }
    
    /**
     * Imprime un reporte del grafo cargado.
     */
    public void imprimirReporte() {
        System.out.println("\n=== REPORTE DEL GRAFO ===");
        System.out.printf("Total de ciudades: %d%n", grafo.obtenerTotalNodos());
        System.out.println("Ciudades: " + grafo.obtenerCiudades());
    }
}