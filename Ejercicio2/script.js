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

// ESTRUCTURA DE DATOS UNION-FIND (para Kruskal) ---
class UnionFind { // Mantiene conjuntos disjuntos de nodos
    constructor(nodes) {
        this.parent = new Map(); // Mapa de padres para cada nodo
        nodes.forEach(node => this.parent.set(node.name, node.name)); // Inicialmente, cada nodo es su propio padre
    }

    // Encuentra el representante (raíz) del conjunto al que pertenece un nodo
    find(nodeName) {
        if (this.parent.get(nodeName) === nodeName) { // Es la raíz
            return nodeName;
        }
        // Compresión de ruta para optimizar
        const root = this.find(this.parent.get(nodeName)); // Recursión para encontrar la raíz
        this.parent.set(nodeName, root); // Actualiza el padre directo al root
        return root;
    }

    // Une los conjuntos de dos nodos
    union(nodeName1, nodeName2) {
        const root1 = this.find(nodeName1); // Encuentra la raíz del primer nodo
        const root2 = this.find(nodeName2); // Encuentra la raíz del segundo nodo
        if (root1 !== root2) {
            this.parent.set(root2, root1);
            return true; // Unión exitosa
        }
        return false; // Ya estaban en el mismo conjunto
    }
}



// LÓGICA DEL GRAFO 
function generarGrafoAleatorio() {
    const cantidadNodos = parseInt(nodeCountInput.value);
    const cantidadAristas = parseInt(edgeCountInput.value);
    
    nodes = [];
    edges = [];
    activeAlgorithm = null;
    
    for (let i = 0; i < cantidadNodos; i++) { // Crear nodos en posiciones aleatorias
        const x = Math.random() * (canvas.width - 60) + 30;
        const y = Math.random() * (canvas.height - 60) + 30;
        nodes.push(new Node('N' + i, x, y));
    }

    const edgeSet = new Set(); // Para evitar aristas duplicadas
    while (edges.length < cantidadAristas && edges.length < (cantidadNodos * (cantidadNodos - 1)) / 2) { // Máximo de aristas en un grafo simple
        let i = Math.floor(Math.random() * cantidadNodos);
        let j = Math.floor(Math.random() * cantidadNodos);
        if (i === j) continue;
        const edgeKey = i < j ? `${i}-${j}` : `${j}-${i}`;
        if (edgeSet.has(edgeKey)) continue;
        const weight = Math.floor(Math.random() * 20) + 1;
        edges.push(new Edge(nodes[i], nodes[j], weight));
        edgeSet.add(edgeKey);
    }
    
    resetGraphColors();
    drawGraph();
    logMessage('Grafo aleatorio generado.', true);
    btnRunPrim.disabled = false;
    btnRunKruskal.disabled = false;
    btnNext.disabled = true;
}

function resetGraphColors() { // Resetea los colores de nodos y aristas
    nodes.forEach(n => n.color = COLORS.NODE_DEFAULT);
    edges.forEach(e => e.color = COLORS.EDGE_DEFAULT);
}

// LÓGICA DE LOS ALGORITMOS (PASO A PASO) 

function prepararAlgoritmo(type) {
    if (nodes.length === 0) {
        logMessage("Error: No hay nodos en el grafo.", true);
        return;
    }
    resetGraphColors();
    activeAlgorithm = type;
    logMessage(`Iniciando algoritmo de ${type}...`, true);
    
    if (type === 'prim') {
        iniciarPrim();
    } 
    else if (type === 'kruskal') {
        iniciarKruskal();
    }

    btnRunPrim.disabled = true;
    btnRunKruskal.disabled = true;
    btnNext.disabled = false;
    drawGraph();
}

function siguientePaso() { // Ejecuta el siguiente paso del algoritmo activo
    if (activeAlgorithm === 'prim') {
        siguientePasoPrim();
    } 
    else if (activeAlgorithm === 'kruskal') {
        siguientePasoKruskal();
    }
}

