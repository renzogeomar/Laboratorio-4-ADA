// CONFIGURACIÓN Y ELEMENTOS DEL DOM 
const canvas = document.getElementById('graphCanvas'); // Canvas para dibujar el grafo
const ctx = canvas.getContext('2d'); // Contexto 2D del canvas
const btnGenerate = document.getElementById('btn-generate'); // Botón para generar grafo
const btnRunPrim = document.getElementById('btn-run-prim'); // Botón para iniciar Prim
const btnRunKruskal = document.getElementById('btn-run-kruskal'); // Botón para iniciar Kruskal
const btnNext = document.getElementById('btn-next'); // Botón para siguiente paso
const nodeCountInput = document.getElementById('nodeCount'); // Input para cantidad de nodos
const edgeCountInput = document.getElementById('edgeCount'); // Input para cantidad de aristas
const logMessages = document.getElementById('log-messages'); // Panel de logs

// ESTADO DEL GRAFO Y ALGORITMO 
let nodes = [];
let edges = [];
let activeAlgorithm = null; // 'prim' o 'kruskal'
let algorithmState = {}; // Objeto para mantener el estado del algoritmo activo

// COLORES PARA VISUALIZACIÓN 
const COLORS = {
    NODE_DEFAULT: '#007bff',
    NODE_MST: '#28a745',
    EDGE_DEFAULT: '#6c757d',
    EDGE_CANDIDATE: '#ffc107', // Usado en Prim
    EDGE_CONSIDERING: '#17a2b8', // Usado en Kruskal
    EDGE_MST: '#dc3545',
    EDGE_DISCARDED: '#e9ecef',
    TEXT: '#FFFFFF'
};

// CLASE NODE
class Node {
    constructor(name, x, y) {
        this.name = name; // Identificador del nodo
        this.x = x; // Posición x en el canvas
        this.y = y; // Posición y en el canvas
        this.color = COLORS.NODE_DEFAULT;
    }
}

// CLASE EDGE
class Edge {
    constructor(from, to, weight) {
        this.from = from; // Nodo de origen
        this.to = to; // Nodo de destino
        this.weight = weight;
        this.color = COLORS.EDGE_DEFAULT;
    }
}

