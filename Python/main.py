from floyd_grafo import FloydGrafo
from centro_grafo import CentroGrafo

class Main:
    """
    Clase principal: Programa interactivo para gestionar rutas y centro del grafo.
    
    Permite:
    1. Consultar la ruta más corta entre dos ciudades
    2. Identificar el centro del grafo
    3. Modificar dinámicamente el grafo
    4. Salir del programa
    
    @author Alejandro Sagastume
    @version 1.0
    """
    
    def __init__(self, archivo_grafo="grafo.txt"):
        """
        Constructor: Inicializa el programa y carga el grafo.
        
        @param archivo_grafo: Ruta del archivo del grafo
        """
        try:
            self.floyd_grafo = FloydGrafo(archivo_grafo)
            self.centro_grafo = CentroGrafo(self.floyd_grafo)
            self.imprimir_matriz_adyacencia()
        except Exception as e:
            print(f"Error al cargar el grafo: {e}")
            exit(1)
    
    def imprimir_matriz_adyacencia(self):
        """
        Imprime la matriz de adyacencia al cargar.
        """
        self.floyd_grafo.imprimir_matriz_adyacencia()
    
    def recalcular(self):
        """
        Recalcula Floyd y Centro después de modificar el grafo.
        """
        self.floyd_grafo.recalcular_floyd()
        self.centro_grafo.recalcular()
    
    def ejecutar(self):
        """
        Ejecuta el programa principal con el menú interactivo.
        """
        while True:
            self.mostrar_menu()
            opcion = self.obtener_opcion()
            
            if opcion == 1:
                self.consultar_ruta_mas_corta()
            elif opcion == 2:
                self.mostrar_centro_grafo()
            elif opcion == 3:
                self.modificar_grafo()
            elif opcion == 4:
                print("\nPrograma finalizado.")
                break
            else:
                print("Opción inválida. Intente nuevamente.")
    
    def mostrar_menu(self):
        """
        Muestra el menú de opciones disponibles.
        """
        print("\n" + "="*60)
        print("          MENU PRINCIPAL - FLOYD GRAFO COVID19")
        print("="*60)
        print("1. Consultar ruta mas corta entre dos ciudades")
        print("2. Mostrar el centro del grafo")
        print("3. Modificar el grafo (agregar/eliminar arcos)")
        print("4. Salir")
        print("="*60)
        print("Seleccione una opción: ", end="")
    
    def obtener_opcion(self):
        """
        Obtiene y valida la opción ingresada por el usuario.
        
        @return Número de opción válida
        """
        try:
            return int(input())
        except ValueError:
            return -1
    
    def consultar_ruta_mas_corta(self):
        """
        Consulta la ruta más corta entre dos ciudades.
        """
        print("\n" + "="*60)
        print("         CONSULTAR RUTA MAS CORTA")
        print("="*60)
        
        origen = input("Ingrese la ciudad origen: ").strip()
        destino = input("Ingrese la ciudad destino: ").strip()
        
        try:
            if not self.floyd_grafo.existe_ciudad(origen):
                print(f"Error: La ciudad origen '{origen}' no existe en el grafo.")
                return
            
            if not self.floyd_grafo.existe_ciudad(destino):
                print(f"Error: La ciudad destino '{destino}' no existe en el grafo.")
                return
            
            camino = self.floyd_grafo.obtener_camino(origen, destino)
            distancia = self.floyd_grafo.obtener_distancia(origen, destino)
            
            if camino is None:
                print(f"\nError: No existe camino de {origen} a {destino}")
            else:
                print(f"\nCamino de {origen} a {destino}:")
                print("Ruta: " + " -> ".join(camino))
                print(f"Distancia total: {distancia:.1f} KM")
        
        except ValueError as e:
            print(f"Error: {e}")
    
    def mostrar_centro_grafo(self):
        """
        Muestra el análisis del centro del grafo.
        """
        print("\n" + "="*60)
        print("         INFORMACION DEL CENTRO DEL GRAFO")
        print("="*60)
        self.centro_grafo.imprimir_analisis_centro()
    
    def modificar_grafo(self):
        """
        Permite modificar el grafo agregando o eliminando arcos.
        """
        print("\n" + "="*60)
        print("              MODIFICAR EL GRAFO")
        print("="*60)
        print("1. Agregar un nuevo arco")
        print("2. Eliminar un arco")
        print("3. Volver al menú principal")
        print("="*60)
        print("Seleccione una opción: ", end="")
        
        opcion = self.obtener_opcion()
        
        if opcion == 1:
            self.agregar_arco()
        elif opcion == 2:
            self.eliminar_arco()
        elif opcion == 3:
            print("Volviendo al menú principal...")
        else:
            print("Opción inválida.")
    
    def agregar_arco(self):
        """
        Agrega un nuevo arco al grafo.
        """
        print("\n" + "="*60)
        print("             AGREGAR NUEVO ARCO")
        print("="*60)
        
        origen = input("Ingrese la ciudad origen: ").strip()
        destino = input("Ingrese la ciudad destino: ").strip()
        
        if not self.floyd_grafo.existe_ciudad(origen):
            print(f"Error: La ciudad origen '{origen}' no existe.")
            print(f"Ciudades disponibles: {self.floyd_grafo.obtener_ciudades()}")
            return
        
        if not self.floyd_grafo.existe_ciudad(destino):
            print(f"Error: La ciudad destino '{destino}' no existe.")
            print(f"Ciudades disponibles: {self.floyd_grafo.obtener_ciudades()}")
            return
        
        try:
            distancia = float(input("Ingrese la distancia en KM: "))
            
            if distancia < 0:
                print("Error: La distancia no puede ser negativa.")
                return
            
            if self.floyd_grafo.agregar_arco(origen, destino, distancia):
                print(f"\nArco agregado: {origen} → {destino} ({distancia:.1f} KM)")
                self.recalcular()
                print("Floyd y centro del grafo recalculados.")
            else:
                print("Error al agregar el arco.")
        
        except ValueError:
            print("Error: Ingrese una distancia válida (número).")
    
    def eliminar_arco(self):
        """
        Elimina un arco del grafo.
        """
        print("\n" + "="*60)
        print("              ELIMINAR ARCO")
        print("="*60)
        
        origen = input("Ingrese la ciudad origen: ").strip()
        destino = input("Ingrese la ciudad destino: ").strip()
        
        if not self.floyd_grafo.existe_ciudad(origen):
            print(f"Error: La ciudad origen '{origen}' no existe.")
            return
        
        if not self.floyd_grafo.existe_ciudad(destino):
            print(f"Error: La ciudad destino '{destino}' no existe.")
            return
        
        if self.floyd_grafo.eliminar_arco(origen, destino):
            print(f"\nArco eliminado: {origen} → {destino}")
            self.recalcular()
            print("Floyd y centro del grafo recalculados.")
        else:
            print("Error al eliminar el arco. Verifique que exista.")


if __name__ == "__main__":
    programa = Main("grafo.txt")
    programa.ejecutar()