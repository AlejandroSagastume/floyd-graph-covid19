package com.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase Grafo: Implementación de un grafo dirigido ponderado
 * usando Matriz de Adyacencia.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class Grafo {
    
    private Map<String, Integer> indices;
    private List<String> ciudades;
    private double[][] matrizAdyacencia;
    private int totalNodos;
    private static final double INFINITO = Double.MAX_VALUE;
    
    /**
     * Constructor: Inicializa un grafo vacío.
     */
    public Grafo() {
        this.indices = new HashMap<>();
        this.ciudades = new ArrayList<>();
        this.totalNodos = 0;
        this.matrizAdyacencia = new double[0][0];
    }
    
    /**
     * Agrega un nodo (ciudad) al grafo.
     * 
     * @param ciudad Nombre de la ciudad a agregar
     * @return true si se agregó exitosamente, false si ya existe
     * @pre ciudad no debe ser nula ni vacía
     * @post La matriz de adyacencia se expande y la ciudad queda agregada
     */
    public boolean agregarNodo(String ciudad) {
        if (indices.containsKey(ciudad)) {
            return false;
        }
        
        double[][] nuevaMatriz = new double[totalNodos + 1][totalNodos + 1];
        
        for (int i = 0; i < totalNodos; i++) {
            for (int j = 0; j < totalNodos; j++) {
                nuevaMatriz[i][j] = matrizAdyacencia[i][j];
            }
        }
        
        for (int i = 0; i <= totalNodos; i++) {
            nuevaMatriz[totalNodos][i] = INFINITO;
            nuevaMatriz[i][totalNodos] = INFINITO;
        }
        
        nuevaMatriz[totalNodos][totalNodos] = 0;
        
        indices.put(ciudad, totalNodos);
        ciudades.add(ciudad);
        matrizAdyacencia = nuevaMatriz;
        totalNodos++;
        
        return true;
    }
    
    /**
     * Agrega un arco dirigido entre dos ciudades con peso.
     * 
     * @param origen Ciudad origen
     * @param destino Ciudad destino
     * @param distancia Distancia/peso del arco en KM
     * @return true si se agregó exitosamente
     * @pre origen y destino deben existir en el grafo
     * @pre distancia debe ser mayor o igual a cero
     * @post Se establece la conexión entre las dos ciudades
     */
    public boolean agregarArco(String origen, String destino, double distancia) {
        if (!indices.containsKey(origen) || !indices.containsKey(destino)) {
            return false;
        }
        
        if (distancia < 0) {
            return false;
        }
        
        int i = indices.get(origen);
        int j = indices.get(destino);
        
        matrizAdyacencia[i][j] = distancia;
        return true;
    }
    
    /**
     * Elimina un arco entre dos ciudades.
     * 
     * @param origen Ciudad origen
     * @param destino Ciudad destino
     * @return true si se eliminó exitosamente
     * @pre origen y destino deben existir en el grafo
     * @post El arco se marca como no existente (INFINITO)
     */
    public boolean eliminarArco(String origen, String destino) {
        if (!indices.containsKey(origen) || !indices.containsKey(destino)) {
            return false;
        }
        
        int i = indices.get(origen);
        int j = indices.get(destino);
        
        if (i == j) {
            return false;
        }
        
        matrizAdyacencia[i][j] = INFINITO;
        return true;
    }
    
    /**
     * Obtiene el peso (distancia) entre dos ciudades.
     * 
     * @param origen Ciudad origen
     * @param destino Ciudad destino
     * @return Distancia, o INFINITO si no existe arco directo
     * @throws IllegalArgumentException si alguna ciudad no existe
     */
    public double obtenerDistancia(String origen, String destino) {
        if (!indices.containsKey(origen) || !indices.containsKey(destino)) {
            throw new IllegalArgumentException("Ciudad no existe en el grafo");
        }
        
        int i = indices.get(origen);
        int j = indices.get(destino);
        return matrizAdyacencia[i][j];
    }
    
    /**
     * Retorna la matriz de adyacencia.
     * 
     * @return Copia de la matriz de adyacencia
     */
    public double[][] obtenerMatrizAdyacencia() {
        double[][] copia = new double[totalNodos][totalNodos];
        for (int i = 0; i < totalNodos; i++) {
            for (int j = 0; j < totalNodos; j++) {
                copia[i][j] = matrizAdyacencia[i][j];
            }
        }
        return copia;
    }
    
    /**
     * Retorna la lista de ciudades en el grafo.
     * 
     * @return Lista de nombres de ciudades
     */
    public List<String> obtenerCiudades() {
        return new ArrayList<>(ciudades);
    }
    
    /**
     * Obtiene el número total de nodos en el grafo.
     * 
     * @return Total de nodos
     */
    public int obtenerTotalNodos() {
        return totalNodos;
    }
    
    /**
     * Verifica si una ciudad existe en el grafo.
     * 
     * @param ciudad Nombre de la ciudad
     * @return true si existe
     */
    public boolean existeCiudad(String ciudad) {
        return indices.containsKey(ciudad);
    }
    
    /**
     * Imprime la matriz de adyacencia de forma legible.
     */
    public void imprimirMatrizAdyacencia() {
        System.out.println("\n=== MATRIZ DE ADYACENCIA ===");
        
        System.out.print("     ");
        for (String ciudad : ciudades) {
            System.out.printf("%10s", ciudad);
        }
        System.out.println();
        
        for (int i = 0; i < totalNodos; i++) {
            System.out.printf("%-10s", ciudades.get(i));
            for (int j = 0; j < totalNodos; j++) {
                if (matrizAdyacencia[i][j] == INFINITO) {
                    System.out.printf("%10s", "∞");
                } else {
                    System.out.printf("%10.0f", matrizAdyacencia[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Obtiene la constante INFINITO.
     * 
     * @return Valor de INFINITO
     */
    public static double getINFINITO() {
        return INFINITO;
    }
}