/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DijkstraNoDirigido;

/**
 *
 * @author darav
 */

import java.io.*;
import java.util.*;


class Edge {
    int src, dest, weight;
    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}

class Graph {
    private int V;
    private LinkedList<Edge>[] adjList;

    public Graph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; ++i)
            adjList[i] = new LinkedList<>();
    }

    public void addEdge(int src, int dest, int weight) {
        adjList[src].add(new Edge(src, dest, weight));
        adjList[dest].add(new Edge(dest, src, weight)); // Añadir la arista en ambas direcciones porque es no dirigido
    }

    public LinkedList<Edge>[] getAdjList() {
        return adjList;
    }
    
    public int getV() {
        return V;
    }

    public void cargarGrafoDesdeArchivo(String nombreArchivo) {
        int maxVertice = 0;

        // Primera pasada para determinar el número máximo de vértices
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
        adjList = new LinkedList[V];
        for (int i = 0; i < V; ++i)
            adjList[i] = new LinkedList<>();

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

    public void guardarResultadosEnArchivo(int[] distancias) {
        String nombreArchivo = "ResultadoDijkstraNoDirigido.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (int i = 0; i < distancias.length; i++) {
                if (i != 0) { // Saltar el nodo origen
                    writer.write("1," + (i + 1) + "," + distancias[i] + "\n");
                }
            }
            System.out.println("Archivo Creado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Dijkstra {
    public static int[] dijkstra(Graph graph, int src) {
        int V = graph.getV();
        int[] dist = new int[V];
        boolean[] sptSet = new boolean[V];

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(V, Comparator.comparingInt(o -> dist[o]));
        pq.add(src);

        while (!pq.isEmpty()) {
            int u = pq.poll();
            sptSet[u] = true;

            for (Edge edge : graph.getAdjList()[u]) {
                if (!sptSet[edge.dest] && dist[u] != Integer.MAX_VALUE && dist[u] + edge.weight < dist[edge.dest]) {
                    dist[edge.dest] = dist[u] + edge.weight;
                    pq.add(edge.dest);
                }
            }
        }

        return dist;
    }
}

public class DijkstraNoDirigido {
    public static void main(String[] args) {
        // Define el nombre del archivo de entrada
        String nombreArchivo = "Grafo50.txt";

        Graph graph = new Graph(0);
        graph.cargarGrafoDesdeArchivo(nombreArchivo);

        int[] distancias = Dijkstra.dijkstra(graph, 0); // Suponiendo que el origen es el nodo 1 (índice 0)
        graph.guardarResultadosEnArchivo(distancias);
    }
}
