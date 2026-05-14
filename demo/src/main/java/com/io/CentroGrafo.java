package com.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase CentroGrafo: Calcula el centro de un grafo dirigido ponderado
 * usando los resultados del algoritmo de Floyd-Warshall.
 * 
 * El centro de un grafo es el vértice con mínima excentricidad.
 * La excentricidad de un vértice es la máxima distancia más corta
 * desde ese vértice a cualquier otro vértice del grafo.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class CentroGrafo {
    
    private Floyd floyd;
    private Grafo grafo;
    private List<String> ciudades;
    private double[] excentricidades;
    private String centrografo;
    private int totalNodos;
    private static final double INFINITO = Grafo.getINFINITO();
    
    /**
     * Constructor: Calcula el centro del grafo a partir del algoritmo Floyd.
     * 
     * @param floyd Objeto Floyd que contiene las distancias más cortas calculadas
     * @param grafo Objeto Grafo para obtener los nombres de las ciudades
     * @pre floyd no debe ser nulo y debe haber ejecutado el algoritmo de Floyd
     * @post Se calculan las excentricidades de todos los vértices y se identifica el centro
     */
    public CentroGrafo(Floyd floyd, Grafo grafo) {
        this.floyd = floyd;
        this.grafo = grafo;
        this.totalNodos = floyd.obtenerMatrizDistancias().length;
        this.ciudades = new ArrayList<>(grafo.obtenerCiudades());
        this.excentricidades = new double[totalNodos];
        this.centrografo = null;
        
        calcularCentro();
    }
    
    /**
     * Constructor alternativo (compatibilidad hacia atrás)
     * @param floyd Objeto Floyd
     * @deprecated Usar constructor con parámetro Grafo
     */
    public CentroGrafo(Floyd floyd) {
        this(floyd, null);
    }
    
    /**
     * Calcula las excentricidades de todos los vértices del grafo
     * y determina el centro (vértice con mínima excentricidad).
     * 
     * La excentricidad de un vértice i es:
     * excentricidad(i) = max { distancia más corta de i a j para todo j }
     * 
     * @pre Floyd debe estar inicializado con distancias calculadas
     * @post excentricidades contiene la excentricidad de cada vértice
     *       centrografo contiene el nombre del vértice centro
     */
    private void calcularCentro() {
        double[][] matrizDistancias = floyd.obtenerMatrizDistancias();
        double minimaExcentricidad = INFINITO;
        
        for (int i = 0; i < totalNodos; i++) {
            double excentricidadMaxima = 0;
            boolean existeCamino = false;
            
            for (int j = 0; j < totalNodos; j++) {
                if (i != j && matrizDistancias[i][j] != INFINITO) {
                    existeCamino = true;
                    if (matrizDistancias[i][j] > excentricidadMaxima) {
                        excentricidadMaxima = matrizDistancias[i][j];
                    }
                }
            }
            
            if (existeCamino) {
                excentricidades[i] = excentricidadMaxima;
                
                if (excentricidadMaxima < minimaExcentricidad) {
                    minimaExcentricidad = excentricidadMaxima;
                    centrografo = ciudades.get(i);
                }
            } else {
                excentricidades[i] = INFINITO;
            }
        }
    }
    
    /**
     * Obtiene el nombre de la ciudad (centro del grafo).
     * 
     * @return El nombre de la ciudad que es centro del grafo,
     *         o null si no existe un centro válido
     */
    public String obtenerCentro() {
        return centrografo;
    }
    
    /**
     * Obtiene la excentricidad de una ciudad específica.
     * 
     * @param ciudad Nombre de la ciudad
     * @return La excentricidad de la ciudad, o INFINITO si no existe camino válido
     * @throws IllegalArgumentException si la ciudad no existe en el grafo
     */
    public double obtenerExcentricidad(String ciudad) {
        int indice = obtenerIndice(ciudad);
        if (indice == -1) {
            throw new IllegalArgumentException("Ciudad no existe en el grafo");
        }
        return excentricidades[indice];
    }
    
    /**
     * Obtiene el arreglo completo de excentricidades de todos los vértices.
     * 
     * @return Copia del arreglo de excentricidades
     */
    public double[] obtenerExcentricidades() {
        double[] copia = new double[totalNodos];
        for (int i = 0; i < totalNodos; i++) {
            copia[i] = excentricidades[i];
        }
        return copia;
    }
    
    /**
     * Obtiene el arreglo de ciudades ordenado por índice.
     * 
     * @return Arreglo de nombres de ciudades
     */
    public String[] obtenerCiudades() {
        return ciudades.toArray(new String[0]);
    }
    
    /**
     * Obtiene el índice de una ciudad en el arreglo de ciudades.
     * 
     * @param ciudad Nombre de la ciudad
     * @return El índice de la ciudad, o -1 si no existe
     */
    private int obtenerIndice(String ciudad) {
        for (int i = 0; i < ciudades.size(); i++) {
            if (ciudades.get(i).equals(ciudad)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Imprime el análisis completo de excentricidades y el centro del grafo.
     */
    public void imprimirAnalisisCentro() {
        System.out.println("\n=== ANÁLISIS DE CENTRO DEL GRAFO ===");
        System.out.println("\nExcentricidades de cada vértice:");
        System.out.printf("%-20s %s%n", "Ciudad", "Excentricidad");
        System.out.println("---------------------------------------");
        
        for (int i = 0; i < totalNodos; i++) {
            if (excentricidades[i] == INFINITO) {
                System.out.printf("%-20s %s%n", ciudades.get(i), "∞");
            } else {
                System.out.printf("%-20s %.1f%n", ciudades.get(i), excentricidades[i]);
            }
        }
        
        System.out.println("\n=== RESULTADO ===");
        if (centrografo != null) {
            System.out.printf("Centro del grafo: %s%n", centrografo);
            System.out.printf("Excentricidad mínima: %.1f KM%n", obtenerExcentricidad(centrografo));
        } else {
            System.out.println("No existe centro válido para este grafo");
        }
    }
    
    /**
     * Imprime un reporte resumido del centro del grafo.
     */
    public void imprimirReporteCentro() {
        System.out.println("\n=== REPORTE CENTRO DEL GRAFO ===");
        if (centrografo != null) {
            System.out.printf("Centro recomendado: %s%n", centrografo);
            System.out.printf("Distancia máxima desde el centro: %.1f KM%n", 
                            obtenerExcentricidad(centrografo));
            System.out.println("Ubicación recomendada para oficinas centrales de logística");
        } else {
            System.out.println("No se puede determinar el centro del grafo");
            System.out.println("El grafo puede no estar completamente conectado");
        }
    }
}