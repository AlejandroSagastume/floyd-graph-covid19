package com.io;

/**
 * Clase Floyd: Implementación del algoritmo de Floyd-Warshall
 * para encontrar los caminos más cortos entre todos los pares de vértices
 * en un grafo dirigido ponderado.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class Floyd {
    
    private double[][] distancias;
    private int[][] siguiente;
    private String[] ciudades;
    private int totalNodos;
    private static final double INFINITO = Grafo.getINFINITO();
    
    /**
     * Constructor: Inicializa el algoritmo de Floyd con un grafo dado.
     * 
     * @param grafo El grafo sobre el cual se ejecutará el algoritmo de Floyd
     * @pre grafo no debe ser nulo y debe contener al menos un nodo
     * @post Se calculan las distancias más cortas entre todos los pares de vértices
     */
    public Floyd(Grafo grafo) {
        this.totalNodos = grafo.obtenerTotalNodos();
        this.ciudades = new String[totalNodos];
        
        // Copiar nombres de ciudades
        for (int i = 0; i < totalNodos; i++) {
            ciudades[i] = grafo.obtenerCiudades().get(i);
        }
        
        // Inicializar matrices de distancias y siguiente
        this.distancias = new double[totalNodos][totalNodos];
        this.siguiente = new int[totalNodos][totalNodos];
        
        // Copiar la matriz de adyacencia del grafo
        double[][] matrizGrafo = grafo.obtenerMatrizAdyacencia();
        
        for (int i = 0; i < totalNodos; i++) {
            for (int j = 0; j < totalNodos; j++) {
                distancias[i][j] = matrizGrafo[i][j];
                
                // Inicializar siguiente
                if (i == j) {
                    siguiente[i][j] = -1; // No hay siguiente en la diagonal
                } else if (distancias[i][j] != INFINITO) {
                    siguiente[i][j] = j; // Hay conexión directa
                } else {
                    siguiente[i][j] = -1; // No hay conexión
                }
            }
        }
        
        // Ejecutar el algoritmo de Floyd-Warshall
        ejecutarFloyd();
    }
    
    /**
     * Ejecuta el algoritmo de Floyd-Warshall para calcular las distancias
     * más cortas entre todos los pares de vértices.
     * 
     * La complejidad temporal es O(n^3) donde n es el número de vértices.
     * 
     * @pre distancias y siguiente deben estar inicializadas
     * @post distancias contiene las distancias más cortas entre todos los pares
     *       siguiente contiene la información del próximo vértice en el camino más corto
     */
    private void ejecutarFloyd() {
        // k es el vértice intermedio
        for (int k = 0; k < totalNodos; k++) {
            // i es el vértice origen
            for (int i = 0; i < totalNodos; i++) {
                // j es el vértice destino
                for (int j = 0; j < totalNodos; j++) {
                    // Verificar si existe un camino más corto pasando por k
                    if (distancias[i][k] != INFINITO && distancias[k][j] != INFINITO) {
                        double nuevaDistancia = distancias[i][k] + distancias[k][j];
                        
                        if (nuevaDistancia < distancias[i][j]) {
                            distancias[i][j] = nuevaDistancia;
                            siguiente[i][j] = siguiente[i][k];
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Obtiene la distancia más corta entre dos ciudades.
     * 
     * @param ciudadOrigen Nombre de la ciudad origen
     * @param ciudadDestino Nombre de la ciudad destino
     * @return La distancia más corta entre las dos ciudades, o INFINITO si no existe camino
     * @throws IllegalArgumentException si alguna ciudad no existe en el grafo
     */
    public double obtenerDistancia(String ciudadOrigen, String ciudadDestino) {
        int i = obtenerIndice(ciudadOrigen);
        int j = obtenerIndice(ciudadDestino);
        
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Ciudad no existe en el grafo");
        }
        
        return distancias[i][j];
    }
    
    /**
     * Obtiene el camino más corto entre dos ciudades como una lista de nombres.
     * 
     * @param ciudadOrigen Nombre de la ciudad origen
     * @param ciudadDestino Nombre de la ciudad destino
     * @return Un arreglo de Strings con el camino desde origen hasta destino,
     *         o null si no existe camino
     * @throws IllegalArgumentException si alguna ciudad no existe en el grafo
     * 
     * @example Si el camino es: Mixco -> Antigua -> Escuintla
     *          retorna {"Mixco", "Antigua", "Escuintla"}
     */
    public String[] obtenerCamino(String ciudadOrigen, String ciudadDestino) {
        int i = obtenerIndice(ciudadOrigen);
        int j = obtenerIndice(ciudadDestino);
        
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Ciudad no existe en el grafo");
        }
        
        // Verificar si existe camino
        if (distancias[i][j] == INFINITO) {
            return null;
        }
        
        // Reconstruir el camino
        java.util.List<String> camino = new java.util.ArrayList<>();
        int actual = i;
        
        while (actual != -1 && actual != j) {
            camino.add(ciudades[actual]);
            actual = siguiente[actual][j];
        }
        
        if (actual == j) {
            camino.add(ciudades[j]);
        } else {
            return null; // No hay camino
        }
        
        return camino.toArray(new String[0]);
    }
    
    /**
     * Obtiene la matriz de distancias después de aplicar Floyd.
     * 
     * @return Una copia de la matriz de distancias más cortas
     */
    public double[][] obtenerMatrizDistancias() {
        double[][] copia = new double[totalNodos][totalNodos];
        for (int i = 0; i < totalNodos; i++) {
            for (int j = 0; j < totalNodos; j++) {
                copia[i][j] = distancias[i][j];
            }
        }
        return copia;
    }
    
    /**
     * Obtiene la matriz siguiente utilizada para reconstruir los caminos.
     * 
     * @return Una copia de la matriz siguiente
     */
    public int[][] obtenerMatrizSiguiente() {
        int[][] copia = new int[totalNodos][totalNodos];
        for (int i = 0; i < totalNodos; i++) {
            for (int j = 0; j < totalNodos; j++) {
                copia[i][j] = siguiente[i][j];
            }
        }
        return copia;
    }
    
    /**
     * Obtiene el índice de una ciudad en el arreglo de ciudades.
     * 
     * @param ciudad Nombre de la ciudad
     * @return El índice de la ciudad, o -1 si no existe
     */
    private int obtenerIndice(String ciudad) {
        for (int i = 0; i < totalNodos; i++) {
            if (ciudades[i].equals(ciudad)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Imprime la matriz de distancias más cortas de forma legible.
     */
    public void imprimirMatrizDistancias() {
        System.out.println("\n=== MATRIZ DE DISTANCIAS MAS CORTAS (FLOYD) ===");
        
        // Encabezado
        System.out.print("     ");
        for (String ciudad : ciudades) {
            System.out.printf("%15s", ciudad);
        }
        System.out.println();
        
        // Filas
        for (int i = 0; i < totalNodos; i++) {
            System.out.printf("%-15s", ciudades[i]);
            for (int j = 0; j < totalNodos; j++) {
                if (distancias[i][j] == INFINITO) {
                    System.out.printf("%15s", "∞");
                } else {
                    System.out.printf("%15.1f", distancias[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Imprime el camino más corto entre dos ciudades.
     * 
     * @param ciudadOrigen Nombre de la ciudad origen
     * @param ciudadDestino Nombre de la ciudad destino
     */
    public void imprimirCamino(String ciudadOrigen, String ciudadDestino) {
        String[] camino = obtenerCamino(ciudadOrigen, ciudadDestino);
        
        if (camino == null) {
            System.out.printf("\nNo existe camino de %s a %s%n", ciudadOrigen, ciudadDestino);
        } else {
            double distancia = obtenerDistancia(ciudadOrigen, ciudadDestino);
            System.out.printf("\nCamino de %s a %s:%n", ciudadOrigen, ciudadDestino);
            System.out.println("Ruta: " + String.join(" -> ", camino));
            System.out.printf("Distancia total: %.1f KM%n", distancia);
        }
    }
}