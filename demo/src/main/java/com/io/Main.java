package com.io;

import java.io.IOException;
import java.util.Scanner;

/**
 * Clase Main: Programa principal que implementa el menú interactivo
 * para la gestión de rutas más cortas en un grafo dirigido ponderado
 * usando el algoritmo de Floyd-Warshall y cálculo del centro del grafo.
 * 
 * El programa permite:
 * 1. Consultar la ruta más corta entre dos ciudades
 * 2. Identificar el centro del grafo
 * 3. Modificar dinámicamente el grafo (agregar/eliminar arcos)
 * 4. Salir del programa
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class Main {
    
    private Grafo grafo;
    private Floyd floyd;
    private CentroGrafo centroGrafo;
    private Scanner scanner;
    
    /**
     * Constructor: Inicializa el programa principal.
     * 
     * @pre El archivo grafo.txt debe existir en src/main/resources/
     * @post Se carga el grafo y se calcula Floyd y el centro
     */
    public Main() {
        this.scanner = new Scanner(System.in);
        cargarGrafo();
    }
    
    /**
     * Carga el grafo desde el archivo grafo.txt.
     * Si hay error, termina el programa.
     * 
     * @post grafo, floyd y centroGrafo están inicializados
     */
    private void cargarGrafo() {
        try {
            // Obtener la ruta absoluta del archivo
            String rutaArchivo = System.getProperty("user.dir") + "\\src\\main\\resources\\grafo.txt";
            
            java.io.File archivo = new java.io.File(rutaArchivo);
            if (!archivo.exists()) {
                throw new IOException("Archivo no encontrado en: " + rutaArchivo);
            }
            
            LectorGrafo lector = new LectorGrafo(rutaArchivo);
            this.grafo = lector.leerGrafo();
            
            // Calcular Floyd y centro
            recalcularFloydYCentro();
            
            System.out.println("\nGrafo cargado exitosamente.");
            System.out.printf("Total de ciudades: %d%n", grafo.obtenerTotalNodos());
            
            // Mostrar matriz de adyacencia bien formateada
            imprimirMatrizAdyacenciaFormato();
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el formato del archivo: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Imprime la matriz de adyacencia en formato bien presentado en bloques.
     */
    private void imprimirMatrizAdyacenciaFormato() {
        double[][] matriz = grafo.obtenerMatrizAdyacencia();
        java.util.List<String> ciudades = grafo.obtenerCiudades();
        int n = ciudades.size();
        
        System.out.println("\n" + "=".repeat(100));
        System.out.println("MATRIZ DE ADYACENCIA DEL GRAFO");
        System.out.println("=".repeat(100));
        
        int anchoColumna = 12;
        int anchoFila = 18;
        
        // Mostrar la matriz en bloques de 8 columnas para que quepa en pantalla
        for (int colStart = 0; colStart < n; colStart += 8) {
            int colEnd = Math.min(colStart + 8, n);
            
            System.out.println("\nBloque de columnas " + (colStart + 1) + " a " + colEnd);
            System.out.println("-".repeat(100));
            
            // Encabezado de bloque
            System.out.print(String.format("%-" + anchoFila + "s", "Ciudad"));
            for (int j = colStart; j < colEnd; j++) {
                String nombreCorto = ciudades.get(j).length() > 10 ? ciudades.get(j).substring(0, 10) : ciudades.get(j);
                System.out.print(String.format("%" + anchoColumna + "s", nombreCorto));
            }
            System.out.println();
            
            // Línea separadora
            System.out.println("-".repeat(100));
            
            // Filas
            for (int i = 0; i < n; i++) {
                String nombreFila = ciudades.get(i).length() > 15 ? ciudades.get(i).substring(0, 15) : ciudades.get(i);
                System.out.print(String.format("%-" + anchoFila + "s", nombreFila));
                
                for (int j = colStart; j < colEnd; j++) {
                    if (matriz[i][j] == Grafo.getINFINITO()) {
                        System.out.print(String.format("%" + anchoColumna + "s", "∞"));
                    } else {
                        System.out.print(String.format("%" + anchoColumna + ".0f", matriz[i][j]));
                    }
                }
                System.out.println();
            }
        }
        
        System.out.println("\n" + "=".repeat(100));
    }
    
    /**
     * Recalcula el algoritmo de Floyd y el centro del grafo.
     * Se utiliza después de modificar el grafo.
     * 
     * @post floyd y centroGrafo se actualizan con los datos más recientes
     */
    private void recalcularFloydYCentro() {
        this.floyd = new Floyd(grafo);
        this.centroGrafo = new CentroGrafo(floyd, grafo);
    }
    
    /**
     * Ejecuta el programa principal con el menú interactivo.
     */
    public void ejecutar() {
        int opcion;
        
        do {
            mostrarMenu();
            opcion = obtenerOpcion();
            
            switch (opcion) {
                case 1:
                    consultarRutaMasCorta();
                    break;
                case 2:
                    mostrarCentroGrafo();
                    break;
                case 3:
                    modificarGrafo();
                    break;
                case 4:
                    System.out.println("\nPrograma finalizado.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
            
        } while (opcion != 4);
        
        scanner.close();
    }
    
    /**
     * Muestra el menú de opciones disponibles.
     */
    private void mostrarMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          MENU PRINCIPAL - FLOYD GRAFO COVID19");
        System.out.println("=".repeat(60));
        System.out.println("1. Consultar ruta mas corta entre dos ciudades");
        System.out.println("2. Mostrar el centro del grafo");
        System.out.println("3. Modificar el grafo (agregar/eliminar arcos)");
        System.out.println("4. Salir");
        System.out.println("=".repeat(60));
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Obtiene y valida la opción ingresada por el usuario.
     * 
     * @return Número de opción válida
     */
    private int obtenerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Consulta la ruta más corta entre dos ciudades.
     * 
     * @post Se muestra la ruta y la distancia total
     */
    private void consultarRutaMasCorta() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         CONSULTAR RUTA MAS CORTA");
        System.out.println("=".repeat(60));
        
        System.out.print("Ingrese la ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        System.out.print("Ingrese la ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        // Validar que las ciudades existan
        if (!grafo.existeCiudad(origen)) {
            System.out.println("Error: La ciudad origen '" + origen + "' no existe en el grafo.");
            return;
        }
        
        if (!grafo.existeCiudad(destino)) {
            System.out.println("Error: La ciudad destino '" + destino + "' no existe en el grafo.");
            return;
        }
        
        // Mostrar ruta
        floyd.imprimirCamino(origen, destino);
    }
    
    /**
     * Muestra el análisis del centro del grafo.
     */
    private void mostrarCentroGrafo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("         INFORMACION DEL CENTRO DEL GRAFO");
        System.out.println("=".repeat(60));
        centroGrafo.imprimirAnalisisCentro();
    }
    
    /**
     * Permite modificar el grafo agregando o eliminando arcos.
     * 
     * @post El grafo se actualiza y se recalculan Floyd y el centro
     */
    private void modificarGrafo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              MODIFICAR EL GRAFO");
        System.out.println("=".repeat(60));
        System.out.println("1. Agregar un nuevo arco");
        System.out.println("2. Eliminar un arco");
        System.out.println("3. Volver al menú principal");
        System.out.println("=".repeat(60));
        System.out.print("Seleccione una opción: ");
        
        int opcion = obtenerOpcion();
        
        switch (opcion) {
            case 1:
                agregarArco();
                break;
            case 2:
                eliminarArco();
                break;
            case 3:
                System.out.println("Volviendo al menú principal...");
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }
    
    /**
     * Agrega un nuevo arco al grafo.
     * 
     * @post Se agrega el arco y se recalculan Floyd y el centro
     */
    private void agregarArco() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("             AGREGAR NUEVO ARCO");
        System.out.println("=".repeat(60));
        
        System.out.print("Ingrese la ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        System.out.print("Ingrese la ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        // Validar existencia de ciudades
        if (!grafo.existeCiudad(origen)) {
            System.out.println("Error: La ciudad origen '" + origen + "' no existe en el grafo.");
            System.out.println("Ciudades disponibles: " + grafo.obtenerCiudades());
            return;
        }
        
        if (!grafo.existeCiudad(destino)) {
            System.out.println("Error: La ciudad destino '" + destino + "' no existe en el grafo.");
            System.out.println("Ciudades disponibles: " + grafo.obtenerCiudades());
            return;
        }
        
        System.out.print("Ingrese la distancia en KM: ");
        try {
            double distancia = Double.parseDouble(scanner.nextLine().trim());
            
            if (distancia < 0) {
                System.out.println("Error: La distancia no puede ser negativa.");
                return;
            }
            
            if (grafo.agregarArco(origen, destino, distancia)) {
                System.out.printf("\nArco agregado: %s → %s (%.1f KM)%n", origen, destino, distancia);
                
                // Recalcular Floyd y centro
                recalcularFloydYCentro();
                System.out.println("Floyd y centro del grafo recalculados.");
                
            } else {
                System.out.println("Error al agregar el arco.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese una distancia válida (número).");
        }
    }
    
    /**
     * Elimina un arco del grafo.
     * 
     * @post Se elimina el arco y se recalculan Floyd y el centro
     */
    private void eliminarArco() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              ELIMINAR ARCO");
        System.out.println("=".repeat(60));
        
        System.out.print("Ingrese la ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        System.out.print("Ingrese la ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        // Validar existencia de ciudades
        if (!grafo.existeCiudad(origen)) {
            System.out.println("Error: La ciudad origen '" + origen + "' no existe en el grafo.");
            return;
        }
        
        if (!grafo.existeCiudad(destino)) {
            System.out.println("Error: La ciudad destino '" + destino + "' no existe en el grafo.");
            return;
        }
        
        if (grafo.eliminarArco(origen, destino)) {
            System.out.printf("\nArco eliminado: %s → %s%n", origen, destino);
            
            // Recalcular Floyd y centro
            recalcularFloydYCentro();
            System.out.println("Floyd y centro del grafo recalculados.");
            
        } else {
            System.out.println("Error al eliminar el arco. Verifique que exista.");
        }
    }
    
    /**
     * Método principal: Punto de entrada del programa.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Main programa = new Main();
        programa.ejecutar();
    }
}