package com.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para la clase Grafo.
 * Prueba la funcionalidad de agregar nodos, agregar arcos y eliminar arcos.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class GrafoTest {
    
    private Grafo grafo;
    
    @BeforeEach
    public void setUp() {
        grafo = new Grafo();
    }
    
    // ============ PRUEBAS PARA AGREGAR NODOS ============
    
    @Test
    public void testAgregarNodoExitosamente() {
        assertTrue(grafo.agregarNodo("Mixco"));
        assertEquals(1, grafo.obtenerTotalNodos());
        assertTrue(grafo.existeCiudad("Mixco"));
    }
    
    @Test
    public void testAgregarMultiplesNodos() {
        assertTrue(grafo.agregarNodo("Mixco"));
        assertTrue(grafo.agregarNodo("Antigua"));
        assertTrue(grafo.agregarNodo("Escuintla"));
        assertEquals(3, grafo.obtenerTotalNodos());
    }
    
    @Test
    public void testAgregarNodoDuplicado() {
        grafo.agregarNodo("Mixco");
        assertFalse(grafo.agregarNodo("Mixco"));
        assertEquals(1, grafo.obtenerTotalNodos());
    }
    
    @Test
    public void testObtenerCiudades() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        assertEquals(2, grafo.obtenerCiudades().size());
        assertTrue(grafo.obtenerCiudades().contains("Mixco"));
        assertTrue(grafo.obtenerCiudades().contains("Antigua"));
    }
    
    @Test
    public void testExisteCiudad() {
        grafo.agregarNodo("Mixco");
        assertTrue(grafo.existeCiudad("Mixco"));
        assertFalse(grafo.existeCiudad("Escuintla"));
    }
    
    // ============ PRUEBAS PARA AGREGAR ARCOS ============
    
    @Test
    public void testAgregarArcoExitosamente() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        assertTrue(grafo.agregarArco("Mixco", "Antigua", 30));
        assertEquals(30, grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    @Test
    public void testAgregarArcoNegativo() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        assertFalse(grafo.agregarArco("Mixco", "Antigua", -10));
    }
    
    @Test
    public void testAgregarArcoCiudadNoExiste() {
        grafo.agregarNodo("Mixco");
        assertFalse(grafo.agregarArco("Mixco", "NoExiste", 30));
    }
    
    @Test
    public void testAgregarMultiplesArcos() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarNodo("Escuintla");
        
        assertTrue(grafo.agregarArco("Mixco", "Antigua", 30));
        assertTrue(grafo.agregarArco("Antigua", "Escuintla", 25));
        assertTrue(grafo.agregarArco("Mixco", "Escuintla", 50));
        
        assertEquals(30, grafo.obtenerDistancia("Mixco", "Antigua"));
        assertEquals(25, grafo.obtenerDistancia("Antigua", "Escuintla"));
        assertEquals(50, grafo.obtenerDistancia("Mixco", "Escuintla"));
    }
    
    @Test
    public void testAgregarArcoConPeso0() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        assertTrue(grafo.agregarArco("Mixco", "Antigua", 0));
        assertEquals(0, grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    @Test
    public void testActualizarArcoExistente() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarArco("Mixco", "Antigua", 30);
        grafo.agregarArco("Mixco", "Antigua", 45);
        assertEquals(45, grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    // ============ PRUEBAS PARA ELIMINAR ARCOS ============
    
    @Test
    public void testEliminarArcoExitosamente() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarArco("Mixco", "Antigua", 30);
        
        assertTrue(grafo.eliminarArco("Mixco", "Antigua"));
        assertEquals(Grafo.getINFINITO(), grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    @Test
    public void testEliminarArcoCiudadNoExiste() {
        grafo.agregarNodo("Mixco");
        assertFalse(grafo.eliminarArco("Mixco", "NoExiste"));
    }
    
    @Test
    public void testEliminarArcoYaEliminado() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarArco("Mixco", "Antigua", 30);
        
        // Primera eliminación debe retornar true
        assertTrue(grafo.eliminarArco("Mixco", "Antigua"));
        
        // Verificar que el arco definitivamente no existe
        assertEquals(Grafo.getINFINITO(), grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    @Test
    public void testEliminarArcoEntre() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarNodo("Escuintla");
        
        grafo.agregarArco("Mixco", "Antigua", 30);
        grafo.agregarArco("Antigua", "Escuintla", 25);
        grafo.agregarArco("Mixco", "Escuintla", 50);
        
        grafo.eliminarArco("Antigua", "Escuintla");
        
        assertEquals(30, grafo.obtenerDistancia("Mixco", "Antigua"));
        assertEquals(Grafo.getINFINITO(), grafo.obtenerDistancia("Antigua", "Escuintla"));
        assertEquals(50, grafo.obtenerDistancia("Mixco", "Escuintla"));
    }
    
    @Test
    public void testEliminarArcoMismaOrigen() {
        grafo.agregarNodo("Mixco");
        assertFalse(grafo.eliminarArco("Mixco", "Mixco"));
    }
    
    // ============ PRUEBAS PARA MATRIZ DE ADYACENCIA ============
    
    @Test
    public void testObtenerMatrizAdyacencia() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        grafo.agregarArco("Mixco", "Antigua", 30);
        
        double[][] matriz = grafo.obtenerMatrizAdyacencia();
        assertEquals(2, matriz.length);
        assertEquals(2, matriz[0].length);
        assertEquals(30, matriz[0][1]);
        assertEquals(0, matriz[0][0]);
    }
    
    @Test
    public void testObtenerDistanciaNoExiste() {
        grafo.agregarNodo("Mixco");
        grafo.agregarNodo("Antigua");
        assertEquals(Grafo.getINFINITO(), grafo.obtenerDistancia("Mixco", "Antigua"));
    }
    
    @Test
    public void testObtenerDistanciaCiudadNoExiste() {
        grafo.agregarNodo("Mixco");
        assertThrows(IllegalArgumentException.class, 
                     () -> grafo.obtenerDistancia("Mixco", "NoExiste"));
    }
}