class CentroGrafo:
    """
    Clase que calcula el centro de un grafo dirigido ponderado
    usando los resultados del algoritmo de Floyd-Warshall.
    
    El centro es el vértice con mínima excentricidad.
    Excentricidad = máxima distancia desde un vértice a cualquier otro.
    
    @author Alejandro Sagastume
    @version 1.0
    """
    
    def __init__(self, floyd_grafo):
        """
        Constructor: Calcula el centro del grafo.
        
        @param floyd_grafo: Objeto FloydGrafo con distancias calculadas
        @pre floyd_grafo debe tener Floyd ejecutado
        @post Se calculan excentricidades e identifica el centro
        """
        self.floyd_grafo = floyd_grafo
        self.ciudades = floyd_grafo.obtener_ciudades()
        self.excentricidades = {}
        self.centro = None
        self.calcular_centro()
    
    def calcular_centro(self):
        """
        Calcula las excentricidades y el centro del grafo.
        
        Excentricidad(i) = max { distancia más corta de i a j para todo j }
        Centro = vértice con mínima excentricidad
        
        @post excentricidades contiene todas las excentricidades
        @post centro contiene el nombre del vértice centro
        """
        minima_excentricidad = float('inf')
        
        for ciudad in self.ciudades:
            max_distancia = 0
            existe_camino = False
            
            for otra_ciudad in self.ciudades:
                if ciudad != otra_ciudad:
                    distancia = self.floyd_grafo.obtener_distancia(ciudad, otra_ciudad)
                    
                    if distancia != float('inf'):
                        existe_camino = True
                        if distancia > max_distancia:
                            max_distancia = distancia
            
            if existe_camino:
                self.excentricidades[ciudad] = max_distancia
                
                if max_distancia < minima_excentricidad:
                    minima_excentricidad = max_distancia
                    self.centro = ciudad
            else:
                self.excentricidades[ciudad] = float('inf')
    
    def obtener_centro(self):
        """
        Retorna el nombre del centro del grafo.
        
        @return Nombre de la ciudad centro, o None si no existe
        """
        return self.centro
    
    def obtener_excentricidad(self, ciudad):
        """
        Obtiene la excentricidad de una ciudad específica.
        
        @param ciudad: Nombre de la ciudad
        @return Excentricidad de la ciudad
        @throws ValueError: Si la ciudad no existe
        """
        if ciudad not in self.ciudades:
            raise ValueError(f"Ciudad no existe: {ciudad}")
        
        return self.excentricidades.get(ciudad, float('inf'))
    
    def obtener_excentricidades(self):
        """
        Retorna todas las excentricidades.
        
        @return Diccionario con excentricidades de todas las ciudades
        """
        return dict(self.excentricidades)
    
    def recalcular(self):
        """
        Recalcula el centro después de cambios en el grafo.
        
        @post Centro y excentricidades se actualizan
        """
        self.ciudades = self.floyd_grafo.obtener_ciudades()
        self.excentricidades = {}
        self.centro = None
        self.calcular_centro()
    
    def imprimir_analisis_centro(self):
        """
        Imprime el análisis completo de excentricidades y centro.
        """
        print("\n=== ANÁLISIS DE CENTRO DEL GRAFO ===")
        print("\nExcentricidades de cada vértice:")
        print(f"{'Ciudad':<20} {'Excentricidad':<20}")
        print("-" * 40)
        
        for ciudad in sorted(self.ciudades):
            exc = self.excentricidades.get(ciudad, float('inf'))
            if exc == float('inf'):
                print(f"{ciudad:<20} {'∞':<20}")
            else:
                print(f"{ciudad:<20} {exc:<20.1f}")
        
        print("\n=== RESULTADO ===")
        if self.centro:
            exc_centro = self.excentricidades[self.centro]
            print(f"Centro del grafo: {self.centro}")
            print(f"Excentricidad mínima: {exc_centro:.1f} KM")
            print("\nUbicación recomendada para oficinas centrales de logística")
        else:
            print("No existe centro válido para este grafo")
            print("El grafo puede no estar completamente conectado")