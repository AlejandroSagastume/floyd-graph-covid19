package com.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para la clase Floyd.
 * Prueba la funcionalidad del algoritmo de Floyd-Warshall.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class FloydTest {
    
    private Grafo grafo;
    private Floyd floyd;
    
    @BeforeEach
    public void setUp() {
        grafo = new Grafo();
        // Crear un grafo de prueba con 4 ciudades
        grafo.agregarNodo("A");
        grafo.agregarNodo("B");
        grafo.agregarNodo("C");
        grafo.agregarNodo("D");
        
        // Agregar arcos
        grafo.agregarArco("A", "B", 1);
        grafo.agregarArco("A", "C", 4);
        grafo.agregarArco("B", "C", 2);
        grafo.agregarArco("B", "D", 5);
        grafo.agregarArco("C", "D", 1);
        
        floyd = new Floyd(grafo);
    }
    
    // ============ PRUEBAS PARA DISTANCIAS MAS CORTAS ============
    
    @Test
    public void testObtenerDistanciaDirecta() {
        assertEquals(1, floyd.obtenerDistancia("A", "B"));
    }
    
    @Test
    public void testObtenerDistanciaIndirecta() {
        // Camino más corto de A a D: A->B->C->D = 1+2+1 = 4
        assertEquals(4, floyd.obtenerDistancia("A", "D"));
    }
    
    @Test
    public void testObtenerDistanciaMismaOrigen() {
        assertEquals(0, floyd.obtenerDistancia("A", "A"));
    }
    
    @Test
    public void testObtenerDistanciaNoExiste() {
        assertEquals(Grafo.getINFINITO(), floyd.obtenerDistancia("D", "A"));
    }
    
    @Test
    public void testObtenerDistanciaCiudadNoExiste() {
        assertThrows(IllegalArgumentException.class,() -> floyd.obtenerDistancia("A", "NoExiste"));
    }
    
    // ============ PRUEBAS PARA CAMINOS ============
    
    @Test
    public void testObtenerCaminoDirecto() {
        String[] camino = floyd.obtenerCamino("A", "B");
        assertNotNull(camino);
        assertEquals(2, camino.length);
        assertEquals("A", camino[0]);
        assertEquals("B", camino[1]);
    }
    
    @Test
    public void testObtenerCaminoIndirecto() {
        String[] camino = floyd.obtenerCamino("A", "D");
        assertNotNull(camino);
        // Camino más corto: A->B->C->D
        assertEquals(4, camino.length);
        assertEquals("A", camino[0]);
        assertEquals("D", camino[camino.length - 1]);
    }
    
    @Test
    public void testObtenerCaminoNoExiste() {
        String[] camino = floyd.obtenerCamino("D", "A");
        assertNull(camino);
    }
    
    @Test
    public void testObtenerCaminoCiudadNoExiste() {
        assertThrows(IllegalArgumentException.class,() -> floyd.obtenerCamino("A", "NoExiste"));
    }
    
    // ============ PRUEBAS PARA MATRICES ============
    
    @Test
    public void testObtenerMatrizDistancias() {
        double[][] matriz = floyd.obtenerMatrizDistancias();
        assertNotNull(matriz);
        assertEquals(4, matriz.length);
        assertEquals(4, matriz[0].length);
    }
    
    @Test
    public void testObtenerMatrizSiguiente() {
        int[][] matriz = floyd.obtenerMatrizSiguiente();
        assertNotNull(matriz);
        assertEquals(4, matriz.length);
        assertEquals(4, matriz[0].length);
    }
    
    // ============ PRUEBAS CON GRAFO COMPLETO ============
    
    @Test
    public void testFloydGrafoConexo() {
        Grafo grafoConexo = new Grafo();
        grafoConexo.agregarNodo("Mixco");
        grafoConexo.agregarNodo("Antigua");
        grafoConexo.agregarNodo("Escuintla");
        
        grafoConexo.agregarArco("Mixco", "Antigua", 30);
        grafoConexo.agregarArco("Antigua", "Escuintla", 25);
        grafoConexo.agregarArco("Escuintla", "Mixco", 40);
        
        Floyd floydConexo = new Floyd(grafoConexo);
        
        assertEquals(30, floydConexo.obtenerDistancia("Mixco", "Antigua"));
        assertEquals(55, floydConexo.obtenerDistancia("Mixco", "Escuintla"));
    }
    
    @Test
    public void testFloydOptimizaCaminos() {
        Grafo grafo2 = new Grafo();
        grafo2.agregarNodo("X");
        grafo2.agregarNodo("Y");
        grafo2.agregarNodo("Z");
        
        // Ruta directa X->Z = 100
        grafo2.agregarArco("X", "Z", 100);
        // Ruta indirecta X->Y->Z = 30+50 = 80 (más corta)
        grafo2.agregarArco("X", "Y", 30);
        grafo2.agregarArco("Y", "Z", 50);
        
        Floyd floyd2 = new Floyd(grafo2);
        
        // Floyd debe elegir la ruta más corta
        assertEquals(80, floyd2.obtenerDistancia("X", "Z"));
        String[] camino = floyd2.obtenerCamino("X", "Z");
        assertEquals(3, camino.length);
        assertEquals("Y", camino[1]);
    }
    
    @Test
    public void testFloydCicloCiudades() {
        Grafo grafoCiclo = new Grafo();
        grafoCiclo.agregarNodo("P1");
        grafoCiclo.agregarNodo("P2");
        grafoCiclo.agregarNodo("P3");
        
        grafoCiclo.agregarArco("P1", "P2", 10);
        grafoCiclo.agregarArco("P2", "P3", 20);
        grafoCiclo.agregarArco("P3", "P1", 30);
        
        Floyd floydCiclo = new Floyd(grafoCiclo);
        
        // P1 -> P3: P1->P2->P3 = 30 (no directo)
        assertEquals(30, floydCiclo.obtenerDistancia("P1", "P3"));
    }
}