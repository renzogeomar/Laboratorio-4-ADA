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

// ALGORITMO DE PRIM 
function iniciarPrim() {
    const startNode = nodes[0];
    
    algorithmState = { // Estado inicial del algoritmo
        visited: new Set([startNode]),
        mst: [],
        availableEdges: [],
        isFinished: false,
    };
    
    startNode.color = COLORS.NODE_MST;
    
    edges.forEach(edge => { // Añadir aristas conectadas al nodo inicial
        if (edge.from === startNode || edge.to === startNode) { // Conectada al nodo inicial
            algorithmState.availableEdges.push(edge); // Añadir a aristas disponibles
            edge.color = COLORS.EDGE_CANDIDATE;
        }
    });

    logMessage(`Empezando desde el nodo ${startNode.name}.`);
}

function siguientePasoPrim() {
    if (algorithmState.isFinished) return;

    let minEdge = null;
    let minWeight = Infinity;
    
    algorithmState.availableEdges.forEach(edge => { // Buscar la arista de menor peso que conecta un nodo visitado con uno no visitado
        const fromVisited = algorithmState.visited.has(edge.from); // Nodo de origen visitado
        const toVisited = algorithmState.visited.has(edge.to); // Nodo de destino visitado  
        if ((fromVisited && !toVisited) || (!fromVisited && toVisited)) { // Conecta un nodo visitado con uno no visitado
            if (edge.weight < minWeight) { // Encontrar la arista de menor peso
                minWeight = edge.weight;
                minEdge = edge;
            }
        }
    });

    if (minEdge === null) { // No hay aristas disponibles que conecten nodos visitados con no visitados
        algorithmState.isFinished = true;
        logMessage("No se encontraron más aristas. El grafo podría no ser conexo.");
        btnNext.disabled = true;
        drawGraph();
        return;
    }
    
    minEdge.color = COLORS.EDGE_MST;
    algorithmState.mst.push(minEdge);

    const newNode = algorithmState.visited.has(minEdge.from) ? minEdge.to : minEdge.from; // Nodo nuevo añadido al MST
    newNode.color = COLORS.NODE_MST;
    algorithmState.visited.add(newNode);
    
    logMessage(`Arista ${minEdge.from.name}-${minEdge.to.name} (Peso: ${minEdge.weight}) añadida.`);

    algorithmState.availableEdges = algorithmState.availableEdges.filter(e => e !== minEdge);
    
    edges.forEach(edge => { // Añadir nuevas aristas conectadas al nuevo nodo
        if ((edge.from === newNode && !algorithmState.visited.has(edge.to)) || 
            (edge.to === newNode && !algorithmState.visited.has(edge.from))) {
            algorithmState.availableEdges.push(edge);
            if(edge.color !== COLORS.EDGE_MST) edge.color = COLORS.EDGE_CANDIDATE;
        } // Conectada al nuevo nodo y al menos un extremo no visitado
    });

    drawGraph();
    
    if (algorithmState.visited.size === nodes.length) { // Todos los nodos han sido visitados
        algorithmState.isFinished = true;
        btnNext.disabled = true;
        const totalWeight = algorithmState.mst.reduce((sum, edge) => sum + edge.weight, 0);
        logMessage(`¡MST completo! Peso total: ${totalWeight}`);
    }
}

// ALGORITMO DE KRUSKAL 
function iniciarKruskal() {
    const sortedEdges = [...edges].sort((a, b) => a.weight - b.weight); // Ordenar aristas por peso ascendente
    
    algorithmState = { // Estado inicial del algoritmo
        mst: [],
        dsu: new UnionFind(nodes), // Estructura Union-Find para ciclos
        sortedEdges: sortedEdges,
        edgeIndex: 0,
        isFinished: false
    };
    
    logMessage('Aristas ordenadas por peso. Evaluando la primera arista.');
    // Pintar la primera arista como "en consideración"
    if (algorithmState.sortedEdges.length > 0) { // Si hay aristas
        algorithmState.sortedEdges[0].color = COLORS.EDGE_CONSIDERING;
    }
}

function siguientePasoKruskal() {
    if (algorithmState.isFinished) return;

    const edgeIndex = algorithmState.edgeIndex;
    if (edgeIndex >= algorithmState.sortedEdges.length) { // Todas las aristas han sido evaluadas
        algorithmState.isFinished = true;
        logMessage("Todas las aristas han sido evaluadas.");
        finalizarKruskal();
        return;
    }
    
    // Devolver el color de la arista anterior a su estado (si no fue MST)
    if (edgeIndex > 0) { // No es la primera arista
        const prevEdge = algorithmState.sortedEdges[edgeIndex - 1];
        if (prevEdge.color !== COLORS.EDGE_MST) { // Si no fue añadida al MST
            prevEdge.color = COLORS.EDGE_DISCARDED;
        }
    }

    const currentEdge = algorithmState.sortedEdges[edgeIndex]; // Arista actual a evaluar
    currentEdge.color = COLORS.EDGE_CONSIDERING;
    
    logMessage(`Evaluando arista ${currentEdge.from.name}-${currentEdge.to.name} (Peso: ${currentEdge.weight})...`);

    const root1 = algorithmState.dsu.find(currentEdge.from.name);
    const root2 = algorithmState.dsu.find(currentEdge.to.name);

    if (root1 !== root2) { // No forma ciclo
        logMessage('  -> No forma ciclo. Añadiendo al MST.');
        algorithmState.dsu.union(currentEdge.from.name, currentEdge.to.name);
        algorithmState.mst.push(currentEdge);
        currentEdge.color = COLORS.EDGE_MST;
        currentEdge.from.color = COLORS.NODE_MST;
        currentEdge.to.color = COLORS.NODE_MST;
    } else {
        logMessage('  -> Forma un ciclo. Descartando.');
        // Se coloreará como descartada en el siguiente paso
    }
    
    algorithmState.edgeIndex++;
    
    drawGraph();

    if (algorithmState.mst.length === nodes.length - 1) { // MST completo
        algorithmState.isFinished = true;
        logMessage("MST completo encontrado.");
        finalizarKruskal();
    }
}

function finalizarKruskal() {
    // Colorear las aristas no usadas como descartadas
    algorithmState.sortedEdges.forEach(e => {
        if(e.color !== COLORS.EDGE_MST) e.color = COLORS.EDGE_DEFAULT;
    }); // Resetear color de aristas no usadas
    const totalWeight = algorithmState.mst.reduce((sum, edge) => sum + edge.weight, 0);
    logMessage(`Peso total del MST: ${totalWeight}`);
    btnNext.disabled = true;
    drawGraph();
}


