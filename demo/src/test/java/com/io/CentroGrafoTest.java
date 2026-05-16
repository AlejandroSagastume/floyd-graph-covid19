package com.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para la clase CentroGrafo.
 * Prueba la funcionalidad de cálculo del centro del grafo.
 * 
 * @author Alejandro Sagastume
 * @version 1.0
 */
public class CentroGrafoTest {
    
    private Grafo grafo;
    private Floyd floyd;
    private CentroGrafo centroGrafo;
    
    @BeforeEach
    public void setUp() {
        // Crear grafo del ejemplo del PDF
        grafo = new Grafo();
        grafo.agregarNodo("a");
        grafo.agregarNodo("b");
        grafo.agregarNodo("c");
        grafo.agregarNodo("d");
        grafo.agregarNodo("e");
        
        grafo.agregarArco("a", "b", 1);
        grafo.agregarArco("b", "c", 2);
        grafo.agregarArco("b", "d", 1);
        grafo.agregarArco("c", "d", 3);
        grafo.agregarArco("d", "e", 5);
        grafo.agregarArco("e", "c", 4);
        
        floyd = new Floyd(grafo);
        centroGrafo = new CentroGrafo(floyd, grafo);
    }
    
    // ============ PRUEBAS PARA OBTENER CENTRO ============
    
    @Test
    public void testObtenerCentroNoNulo() {
        assertNotNull(centroGrafo.obtenerCentro());
    }
    
    @Test
    public void testCentroExisteEnGrafo() {
        String centro = centroGrafo.obtenerCentro();
        assertTrue(grafo.existeCiudad(centro));
    }
    
    // ============ PRUEBAS PARA EXCENTRICIDADES ============
    
    @Test
    public void testObtenerExcentricidad() {
        double excentricidad = centroGrafo.obtenerExcentricidad("a");
        assertTrue(excentricidad >= 0);
    }
    
    @Test
    public void testObtenerExcentricidadCiudadNoExiste() {
        assertThrows(IllegalArgumentException.class,() -> centroGrafo.obtenerExcentricidad("NoExiste"));
    }
    
    @Test
    public void testObtenerExcentricidades() {
        double[] excentricidades = centroGrafo.obtenerExcentricidades();
        assertNotNull(excentricidades);
        assertEquals(5, excentricidades.length);
    }
    
    // ============ PRUEBAS CON GRAFO SIMPLE ============
    
    @Test
    public void testCentroGrafoTriangulo() {
        Grafo grafoTriangulo = new Grafo();
        grafoTriangulo.agregarNodo("X");
        grafoTriangulo.agregarNodo("Y");
        grafoTriangulo.agregarNodo("Z");
        
        grafoTriangulo.agregarArco("X", "Y", 1);
        grafoTriangulo.agregarArco("Y", "Z", 1);
        grafoTriangulo.agregarArco("Z", "X", 1);
        
        Floyd floydTri = new Floyd(grafoTriangulo);
        CentroGrafo centrTri = new CentroGrafo(floydTri, grafoTriangulo);
        
        assertNotNull(centrTri.obtenerCentro());
    }
    
    // ============ PRUEBAS CON GRAFO DESCONEXO ============
    
    @Test
    public void testCentroGrafoDesconexo() {
        Grafo grafoDesconexo = new Grafo();
        grafoDesconexo.agregarNodo("A");
        grafoDesconexo.agregarNodo("B");
        grafoDesconexo.agregarNodo("C");
        
        grafoDesconexo.agregarArco("A", "B", 10);
        // C está desconectado
        
        Floyd floydDes = new Floyd(grafoDesconexo);
        CentroGrafo centrDes = new CentroGrafo(floydDes, grafoDesconexo);
        
        // El centro puede ser uno de los vértices conectados
        assertNotNull(centrDes.obtenerCentro());
    }
    
    // ============ PRUEBAS DE ARRAY DE CIUDADES ============
    
    @Test
    public void testObtenerCiudades() {
        String[] ciudades = centroGrafo.obtenerCiudades();
        assertNotNull(ciudades);
        assertEquals(5, ciudades.length);
    }
    
    // ============ PRUEBAS CON GRAFO DE DOS NODOS ============
    
    @Test
    public void testCentroGrafoDosNodos() {
        Grafo grafoDos = new Grafo();
        grafoDos.agregarNodo("C1");
        grafoDos.agregarNodo("C2");
        grafoDos.agregarArco("C1", "C2", 50);
        
        Floyd floydDos = new Floyd(grafoDos);
        CentroGrafo centrDos = new CentroGrafo(floydDos, grafoDos);
        
        assertNotNull(centrDos.obtenerCentro());
    }
    
    // ============ PRUEBAS DE EXCENTRICIDAD MINIMA ============
    
    @Test
    public void testCentroTieneExcentricidadMinima() {
        String centro = centroGrafo.obtenerCentro();
        double excCentro = centroGrafo.obtenerExcentricidad(centro);
        
        double[] excentricidades = centroGrafo.obtenerExcentricidades();
        
        // La excentricidad del centro debe ser menor o igual a todas las demás
        for (int i = 0; i < excentricidades.length; i++) {
            if (excentricidades[i] != Grafo.getINFINITO()) {
                assertTrue(excCentro <= excentricidades[i]);
            }
        }
    }
    
    // ============ PRUEBAS CON GRAFO LINEAL ============
    
    @Test
    public void testCentroGrafoLineal() {
        Grafo grafoLineal = new Grafo();
        grafoLineal.agregarNodo("V1");
        grafoLineal.agregarNodo("V2");
        grafoLineal.agregarNodo("V3");
        grafoLineal.agregarNodo("V4");
        grafoLineal.agregarNodo("V5");
        
        // Crear una línea: V1 -> V2 -> V3 -> V4 -> V5
        grafoLineal.agregarArco("V1", "V2", 1);
        grafoLineal.agregarArco("V2", "V3", 1);
        grafoLineal.agregarArco("V3", "V4", 1);
        grafoLineal.agregarArco("V4", "V5", 1);
        
        Floyd floydLineal = new Floyd(grafoLineal);
        CentroGrafo centrLineal = new CentroGrafo(floydLineal, grafoLineal);
        
        // El centro debe estar cerca del medio (V3)
        String centro = centrLineal.obtenerCentro();
        assertNotNull(centro);
    }
}