import networkx as nx
from collections import defaultdict

class FloydGrafo:
    """
    Clase que implementa el algoritmo de Floyd-Warshall usando NetworkX.
    Calcula las rutas más cortas entre todos los pares de vértices.
    
    @author Alejandro Sagastume
    @version 1.0
    """
    
    def __init__(self, archivo_grafo):
        """
        Constructor: Inicializa el grafo y ejecuta el algoritmo Floyd.
        
        @param archivo_grafo: Ruta del archivo que contiene el grafo
        @pre El archivo debe existir y tener formato válido
        @post Se construye el grafo y se calcula Floyd
        """
        self.grafo = nx.DiGraph()
        self.distancias = {}
        self.caminos = defaultdict(lambda: defaultdict(list))
        self.leer_grafo(archivo_grafo)
        self.ejecutar_floyd()
    
    def leer_grafo(self, archivo):
        """
        Lee el archivo de grafo y construye la estructura.
        
        Formato del archivo:
        CiudadOrigen CiudadDestino Distancia
        
        @param archivo: Ruta del archivo
        @throws FileNotFoundError: Si el archivo no existe
        @throws ValueError: Si el formato es inválido
        """
        try:
            with open(archivo, 'r', encoding='utf-8') as f:
                numero_linea = 0
                for linea in f:
                    numero_linea += 1
                    linea = linea.strip()
                    
                    # Ignorar líneas vacías y comentarios
                    if not linea or linea.startswith('#'):
                        continue
                    
                    partes = linea.split()
                    
                    if len(partes) != 3:
                        raise ValueError(f"Formato inválido en línea {numero_linea}: se esperaban 3 campos")
                    
                    origen, destino, distancia_str = partes
                    
                    try:
                        distancia = float(distancia_str)
                    except ValueError:
                        raise ValueError(f"Distancia inválida en línea {numero_linea}: {distancia_str}")
                    
                    if distancia < 0:
                        raise ValueError(f"Distancia negativa en línea {numero_linea}")
                    
                    # Agregar nodos si no existen
                    if not self.grafo.has_node(origen):
                        self.grafo.add_node(origen)
                    if not self.grafo.has_node(destino):
                        self.grafo.add_node(destino)
                    
                    # Agregar arco
                    self.grafo.add_edge(origen, destino, weight=distancia)
            
            if self.grafo.number_of_nodes() == 0:
                raise ValueError("El archivo no contiene datos válidos")
            
            print(f"Grafo cargado exitosamente")
            print(f"Total de ciudades: {self.grafo.number_of_nodes()}")
            
        except FileNotFoundError:
            print(f"Error: Archivo no encontrado: {archivo}")
            raise
        except ValueError as e:
            print(f"Error en el formato: {e}")
            raise
    
    def ejecutar_floyd(self):
        """
        Ejecuta el algoritmo de Floyd-Warshall usando NetworkX.
        
        @post distancias contiene las distancias más cortas
        @post caminos contiene los caminos más cortos
        """
        try:
            # Usar el algoritmo de Floyd-Warshall de NetworkX
            self.distancias = dict(nx.all_pairs_dijkstra_path_length(self.grafo, weight='weight'))
            
            # Calcular caminos
            caminos_temp = dict(nx.all_pairs_dijkstra_path(self.grafo, weight='weight'))
            
            for origen in caminos_temp:
                for destino in caminos_temp[origen]:
                    self.caminos[origen][destino] = caminos_temp[origen][destino]
            
        except Exception as e:
            print(f"Error al ejecutar Floyd: {e}")
            raise
    
    def obtener_distancia(self, origen, destino):
        """
        Obtiene la distancia más corta entre dos ciudades.
        
        @param origen: Ciudad de origen
        @param destino: Ciudad de destino
        @return Distancia más corta, o infinito si no existe camino
        @throws ValueError: Si alguna ciudad no existe
        """
        if origen not in self.grafo:
            raise ValueError(f"Ciudad no existe en el grafo: {origen}")
        if destino not in self.grafo:
            raise ValueError(f"Ciudad no existe en el grafo: {destino}")
        
        if destino in self.distancias.get(origen, {}):
            return self.distancias[origen][destino]
        else:
            return float('inf')
    
    def obtener_camino(self, origen, destino):
        """
        Obtiene el camino más corto entre dos ciudades.
        
        @param origen: Ciudad de origen
        @param destino: Ciudad de destino
        @return Lista con las ciudades del camino, o None si no existe
        @throws ValueError: Si alguna ciudad no existe
        """
        if origen not in self.grafo:
            raise ValueError(f"Ciudad no existe en el grafo: {origen}")
        if destino not in self.grafo:
            raise ValueError(f"Ciudad no existe en el grafo: {destino}")
        
        if destino in self.caminos.get(origen, {}):
            return self.caminos[origen][destino]
        else:
            return None
    
    def obtener_ciudades(self):
        """
        Retorna la lista de todas las ciudades.
        
        @return List de nombres de ciudades
        """
        return list(self.grafo.nodes())
    
    def obtener_total_nodos(self):
        """
        Retorna el total de ciudades.
        
        @return Número de ciudades
        """
        return self.grafo.number_of_nodes()
    
    def existe_ciudad(self, ciudad):
        """
        Verifica si una ciudad existe en el grafo.
        
        @param ciudad: Nombre de la ciudad
        @return True si existe, False en caso contrario
        """
        return self.grafo.has_node(ciudad)
    
    def agregar_arco(self, origen, destino, distancia):
        """
        Agrega un nuevo arco al grafo.
        
        @param origen: Ciudad de origen
        @param destino: Ciudad de destino
        @param distancia: Distancia en KM
        @return True si se agregó, False en caso contrario
        """
        if not self.existe_ciudad(origen) or not self.existe_ciudad(destino):
            return False
        
        if distancia < 0:
            return False
        
        self.grafo.add_edge(origen, destino, weight=distancia)
        return True
    
    def eliminar_arco(self, origen, destino):
        """
        Elimina un arco del grafo.
        
        @param origen: Ciudad de origen
        @param destino: Ciudad de destino
        @return True si se eliminó, False en caso contrario
        """
        if not self.existe_ciudad(origen) or not self.existe_ciudad(destino):
            return False
        
        if self.grafo.has_edge(origen, destino):
            self.grafo.remove_edge(origen, destino)
            return True
        
        return False
    
    def obtener_matriz_adyacencia(self):
        """
        Retorna la matriz de adyacencia del grafo.
        
        @return Diccionario anidado representando la matriz
        """
        ciudades = sorted(self.grafo.nodes())
        matriz = {}
        
        for origen in ciudades:
            matriz[origen] = {}
            for destino in ciudades:
                if origen == destino:
                    matriz[origen][destino] = 0
                elif self.grafo.has_edge(origen, destino):
                    matriz[origen][destino] = self.grafo[origen][destino]['weight']
                else:
                    matriz[origen][destino] = float('inf')
        
        return matriz
    
    def imprimir_matriz_adyacencia(self):
        """
        Imprime la matriz de adyacencia de forma legible.
        """
        matriz = self.obtener_matriz_adyacencia()
        ciudades = sorted(self.grafo.nodes())
        
        print("\n" + "="*100)
        print("MATRIZ DE ADYACENCIA DEL GRAFO")
        print("="*100)
        
        ancho_columna = 12
        ancho_fila = 18
        
        # Mostrar en bloques de 8 columnas
        for col_start in range(0, len(ciudades), 8):
            col_end = min(col_start + 8, len(ciudades))
            
            print(f"\nBloque de columnas {col_start + 1} a {col_end}")
            print("-" * 100)
            
            # Encabezado
            print(f"{'Ciudad':<{ancho_fila}}", end="")
            for j in range(col_start, col_end):
                ciudad_corta = ciudades[j][:10]
                print(f"{ciudad_corta:>{ancho_columna}}", end="")
            print()
            
            print("-" * 100)
            
            # Filas
            for i in range(len(ciudades)):
                ciudad_fila = ciudades[i][:15]
                print(f"{ciudad_fila:<{ancho_fila}}", end="")
                
                for j in range(col_start, col_end):
                    valor = matriz[ciudades[i]][ciudades[j]]
                    if valor == float('inf'):
                        print(f"{'∞':>{ancho_columna}}", end="")
                    else:
                        print(f"{valor:>{ancho_columna}.0f}", end="")
                print()
        
        print("\n" + "="*100)
    
    def recalcular_floyd(self):
        """
        Recalcula el algoritmo de Floyd después de modificar el grafo.
        
        @post distancias y caminos se actualizan
        """
        self.ejecutar_floyd()