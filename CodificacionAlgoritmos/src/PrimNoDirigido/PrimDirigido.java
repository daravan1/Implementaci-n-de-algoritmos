/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PrimNoDirigido;

/**
 *
 * @author darav
 */
import java.util.*;
import java.io.*;

class Edge {
    int src, dest, peso;

    Edge(int s, int d, int p) {
        src = s;
        dest = d;
        peso = p;
    }
}

public class PrimDirigido {
    int V;
    LinkedList<Edge>[] adj;

    PrimDirigido(int v) {
        V = v;
        adj = new LinkedList[V];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    void addEdge(int src, int dest, int peso) {
        adj[src].add(new Edge(src, dest, peso));
    }

    void primMST() {
        boolean[] inMST = new boolean[V];
        Edge[] result = new Edge[V];
        int[] key = new int[V];
        int[] parent = new int[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>(V, Comparator.comparingInt(o -> o.peso));

        key[0] = 0;
        pq.add(new Edge(-1, 0, 0)); // Iniciando desde el vértice 0

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int u = edge.dest;

            if (inMST[u]) continue;

            inMST[u] = true;

            if (edge.src != -1) {
                result[u] = edge;
            }

            for (Edge neighbor : adj[u]) {
                int v = neighbor.dest;
                int weight = neighbor.peso;

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    pq.add(new Edge(u, v, weight));
                    parent[v] = u;
                }
            }
        }

        guardarResultadoEnArchivo(result, "ResultadoPrimDirigido.txt");
    }

    void guardarResultadoEnArchivo(Edge[] result, String nombreArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 1; i < result.length; ++i) {
                if (result[i] != null) {
                    bw.write((result[i].src + 1) + "," + (result[i].dest + 1) + "," + result[i].peso);
                    bw.newLine();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void cargarGrafoDesdeArchivo(String nombreArchivo) {
        // Primera pasada para determinar el número máximo de vértices
        int maxVertice = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int src = Integer.parseInt(partes[0]);
                int dest = Integer.parseInt(partes[1]);
                maxVertice = Math.max(maxVertice, Math.max(src, dest));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Inicializar el grafo con el número correcto de vértices
        this.V = maxVertice;
        this.adj = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new LinkedList<>();
        }

        // Segunda pasada para leer las aristas
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int src = Integer.parseInt(partes[0]) - 1; // ajustando a índice base 0
                int dest = Integer.parseInt(partes[1]) - 1; // ajustando a índice base 0
                int peso = Integer.parseInt(partes[2]);
                addEdge(src, dest, peso);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PrimDirigido grafo = new PrimDirigido(0);

        String nombreArchivo = "Grafo30.txt"; // Nombre del archivo que contiene el grafo
        grafo.cargarGrafoDesdeArchivo(nombreArchivo);
        grafo.primMST();
        System.out.println("Archivo Creado");
    }
}
