package KruskalNoDirigido;

import java.util.*;
import java.io.*;

class Edge implements Comparable<Edge> {
    int src, dest, peso;

    Edge(int src, int dest, int peso) {
        this.src = src;
        this.dest = dest;
        this.peso = peso;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.peso, other.peso);
    }
}

class Subconjunto {
    int padre, rango;
}

public class KruskalNoDirigido {
    int V, E;
    Edge[] edges;

    KruskalNoDirigido(int v, int e) {
        V = v;
        E = e;
        edges = new Edge[E];
        for (int i = 0; i < e; ++i) {
            edges[i] = new Edge(0, 0, 0);
        }
    }

    int buscar(Subconjunto[] subconjuntos, int i) {
        if (subconjuntos[i].padre != i) {
            subconjuntos[i].padre = buscar(subconjuntos, subconjuntos[i].padre);
        }
        return subconjuntos[i].padre;
    }

    void union(Subconjunto[] subconjuntos, int x, int y) {
        int xroot = buscar(subconjuntos, x);
        int yroot = buscar(subconjuntos, y);

        if (subconjuntos[xroot].rango < subconjuntos[yroot].rango) {
            subconjuntos[xroot].padre = yroot;
        } else if (subconjuntos[xroot].rango > subconjuntos[yroot].rango) {
            subconjuntos[yroot].padre = xroot;
        } else {
            subconjuntos[yroot].padre = xroot;
            subconjuntos[xroot].rango++;
        }
    }

    void kruskalMST() {
        Edge[] result = new Edge[V];
        int e = 0;
        for (int i = 0; i < V; ++i) {
            result[i] = new Edge(0, 0, 0);
        }

        // Ordenar todas las aristas
        Arrays.sort(edges);

        Subconjunto[] subconjuntos = new Subconjunto[V];
        for (int v = 0; v < V; ++v) {
            subconjuntos[v] = new Subconjunto();
            subconjuntos[v].padre = v;
            subconjuntos[v].rango = 0;
        }

        int i = 0;
        while (e < V - 1) {
            Edge nextEdge = edges[i++];
            int x = buscar(subconjuntos, nextEdge.src);
            int y = buscar(subconjuntos, nextEdge.dest);

            if (x != y) {
                result[e++] = nextEdge;
                union(subconjuntos, x, y);
            }
        }

        guardarResultadoEnArchivo(result, e, "ResultadoKruskalNoDirigido.txt");
    }

    void guardarResultadoEnArchivo(Edge[] result, int e, String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < e; ++i) {
                bw.write((result[i].src + 1) + "," + (result[i].dest + 1) + "," + result[i].peso);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void cargarGrafoDesdeArchivo(String nombreArchivo) {
        List<Edge> edgeList = new ArrayList<>();
        int maxVertice = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int src = Integer.parseInt(partes[0]) - 1; // ajustando a índice base 0
                int dest = Integer.parseInt(partes[1]) - 1; // ajustando a índice base 0
                int peso = Integer.parseInt(partes[2]);
                edgeList.add(new Edge(src, dest, peso));
                maxVertice = Math.max(maxVertice, Math.max(src, dest));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.V = maxVertice + 1;
        this.E = edgeList.size();
        this.edges = new Edge[E];
        for (int i = 0; i < E; i++) {
            edges[i] = edgeList.get(i);
        }
    }

    public static void main(String[] args) {
        KruskalNoDirigido grafo = new KruskalNoDirigido(0, 0);

        String nombreArchivo = "Grafo50.txt"; // Nombre del archivo que contiene el grafo
        grafo.cargarGrafoDesdeArchivo(nombreArchivo);
        grafo.kruskalMST();
        System.out.println("Archivo Creado");
    }
}
