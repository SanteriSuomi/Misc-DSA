import PQ


class Vertex:
    def __init__(self, data):
        self.data = data
        self.index = index


class Edge:
    def __init__(self, f, t):
        self.f = f
        self.t = t

    def get_reversed(self):
        # Return this edge but direction reversed
        return Edge(self.t, self.f)

    def __str__(self):
        return self.f + "->" + self.t


class WeightedEdge(Edge):
    def __init__(self, f, t, w):
        super().__init__(f, t)
        self.w = w

    def get_reversed(self):
        return WeightedEdge(self.t, self.f, self.w)

    def __lt__(self, other):
        # Less than operator
        return self.w < other.w

    def __str__(self):
        return f"{self.f} {self.w} -> {self.t}"


class Node:
    # Node class, primarily used in graph searching algorithms to reconstruct the path.
    def __init__(self, parent, value):
        self.parent = parent
        self.value = value


class DijkstraNode:
    # Node class for dijkstra's algorithm
    def __init__(self, vertex, distance):
        self.vertex = vertex
        self.distance = distance

    def __lt__(self, other):
        return self.distance < other.distance

    def __eq__(self, other):
        return self.distance == other.distance


class Graph:
    def __init__(self, vertices=None, edges: tuple = None):
        # Construct a new graph. Vertices = all possible vertices.
        # Edges = connections between vertices as tuples.
        if vertices is None:
            self._vertices = []
            self._edges = []
        else:
            self._vertices = vertices
            self._edges = [[] for _ in vertices]
            for t in edges:
                self.add_edge_using_vertices(t[0], t[1])

    def vertex_count(self):
        return len(self._vertices)

    def edge_count(self):
        return len(self._edges)

    def add_vertex(self, v):
        self._vertices.append(v)
        self._edges.append([])
        return self.add_vertex - 1  # Return index to the just added vertex

    def add_edge(self, e):
        self._edges[e.f].append(e)  # Add edge from -> to
        self._edges[e.t].append(e.get_reversed())  # Add edge to -> from

    def add_edge_using_indices(self, fi, ti):
        # Add edge using from -> to indices.
        self.add_edge(Edge(fi, ti))

    def add_edge_using_vertices(self, fv, tv):
        # Add edge using from -> to edges.
        self.add_edge_using_indices(self._vertices.index(fv), self._vertices.index(tv))

    def vertex_at(self, ind):
        return self._vertices[ind]

    def index_of(self, vertex):
        return self._vertices.index(vertex)

    def neighbours_of_index(self, ind):
        # Find all the vertices to which a vertex at index is connected to
        return list(map(self.vertex_at, [e.t for e in self._edges[ind]]))

    def neighbours_of_vertex(self, v):
        return self.neighbours_of_index(self.index_of(v))

    def edges_of_index(self, ind):
        return self._edges[ind]

    def edges_of_vertex(self, v):
        return self.edges_of_index(self.index_of(v))

    def __str__(self):
        # Get a string representation of the graph
        string = ""
        for i in range(self.vertex_count()):
            string += (
                str(self.vertex_at(i))
                + " -> "
                + str(self.neighbours_of_index(i))
                + "\n"
            )
        return string

    def bfs(self, from_vertex, to_vertex):
        frontier = []
        frontier.append(Node(None, from_vertex))
        explored = {from_vertex}
        while len(frontier) > 0:
            current_node = frontier.pop()
            if current_node.value == to_vertex:
                path = []
                current_path_node = current_node
                while not current_path_node.parent is None:
                    path.append(current_path_node.value)
                    current_path_node = current_path_node.parent
                path.append(current_path_node.value)
                return path[::-1]
            for child_vertex in self.neighbours_of_vertex(current_node.value):
                if child_vertex not in explored:
                    explored.add(child_vertex)
                    frontier.append(Node(current_node, child_vertex))
        return None


class WeighedGraph(Graph):
    def __init__(self, vertices=None, edges: tuple = None):
        if vertices is None:
            self._vertices = []
            self._edges = []
        else:
            self._vertices = vertices
            self._edges = [[] for _ in vertices]
            for t in edges:
                self.add_edge_using_vertices(t[0], t[1], t[2])

    def add_edge_using_indices(self, fi, ti, w=None):
        self.add_edge(WeightedEdge(fi, ti, w))

    def add_edge_using_vertices(self, fv, tv, w=None):
        self.add_edge_using_indices(
            self._vertices.index(fv), self._vertices.index(tv), w
        )

    def neighbours_of_index_with_weights(self, ind):
        # Return all the neighbours of vertex at provided index as (vertex, weight) tuples.
        vertex_weight_tuples = []
        for edge in self.edges_of_index(ind):
            vertex_weight_tuples.append((self.vertex_at(edge.t), edge.w))
        return vertex_weight_tuples

    def __str__(self):
        string = ""
        for i in range(self.vertex_count()):
            string += (
                str(self.vertex_at(i))
                + " -> "
                + str(self.neighbours_of_index_with_weights(i))
                + "\n"
            )
        return string

    def total_weight(self, path):
        # Returns the total weight of a given list of weighted edges
        return sum([e.w for e in path])

    def mst(self, start=0, pretty_print: bool = False):
        # Minimum spanning tree for this graph
        if start > (self.vertex_count() - 1) or start < 0:
            raise IndexError(
                "There is no MST for a vertex whose index starts outside the graph's indices."
            )
        path = []  # Final path
        pq = PQ.PQ()
        visited = [False] * self.vertex_count()  # Keep track of visited vertices

        def visit(ind):
            visited[ind] = True
            for edge in self.edges_of_index(ind):
                if not visited[edge.t]:
                    pq.push(edge)

        visit(start)
        while not pq.empty():
            current_edge = pq.pop()
            if not visited[current_edge.t]:
                pq.push(current_edge)
                path.append(current_edge)
                visit(current_edge.t)
        if pretty_print:
            for edge in path:
                print(f"{self.vertex_at(edge.f)} {edge.w} -> {self.vertex_at(edge.t)}")
            print(f"TOTAL WEIGHT: {self.total_weight(path)}")
        return path

    def dijkstra(self, root):
        first = self.index_of(root)
        distances = [None] * self.vertex_count()  # Distances to each vertex from start
        distances[first] = 0  # First vertex (root) is zero distance away
        path_map = {}  # How we got to each vertex
        pq = PQ.PQ()
        pq.push(DijkstraNode(first, 0))

        while not pq.empty():
            next_node = pq.pop()
            distance_next = distances[next_node.vertex]
            for edge in self.edges_of_index(next_node.vertex):
                distance_old = distances[edge.t]
                if distance_old is None or distance_old > edge.w + distance_next:
                    distances[edge.t] = edge.w + distance_next
                    path_map[edge.t] = edge
                    pq.push(DijkstraNode(edge.t, edge.w + distance_next))
        return path_map

    def path_map_to_path(self, path_map, start, end, pretty_print=False):
        # Convert path dictionary returned by dijkstra to a path from start to end
        start_ind = self.index_of(start)
        end_ind = self.index_of(end)
        if len(path_map) == 0:
            raise RuntimeError("Path map is empty.")
        path = []
        edge = path_map[end_ind]
        path.append(edge)
        while edge.f != start_ind:
            edge = path_map[edge.f]
            path.append(edge)
        path = path[::-1]  # Reverse
        if pretty_print:
            for edge in path:
                print(f"{self.vertex_at(edge.f)} {edge.w} -> {self.vertex_at(edge.t)}")
        return path


# Unweighted graph
# graph = Graph(
#     [
#         "Tampere",
#         "Helsinki",
#         "Turku",
#         "Espoo",
#         "Jyväskylä",
#         "Kuopio",
#         "Kajaani",
#         "Kuusamo",
#     ],
#     [
#         ("Tampere", "Helsinki"),
#         ("Tampere", "Turku"),
#         ("Turku", "Helsinki"),
#         ("Helsinki", "Espoo"),
#         ("Tampere", "Jyväskylä"),
#         ("Jyväskylä", "Kuopio"),
#         ("Kuopio", "Kajaani"),
#         ("Kajaani", "Kuusamo"),
#     ],
# )
# print(graph)
# print("\n")

# path = graph.bfs("Tampere", "Helsinki")
# print(path)

# path = graph.bfs("Tampere", "Helsinki")
# print(path)

# path = graph.bfs("Espoo", "Kuusamo")
# print(path)

# Weighted graph
graph = WeighedGraph(
    [
        "Tampere",
        "Helsinki",
        "Turku",
        "Espoo",
        "Jyväskylä",
        "Kuopio",
        "Kajaani",
        "Kuusamo",
    ],
    [  # Third element is weight, in this case distance in kilometers
        ("Tampere", "Helsinki", 160),
        ("Tampere", "Turku", 141),
        ("Turku", "Helsinki", 150),
        ("Helsinki", "Espoo", 16),
        ("Tampere", "Jyväskylä", 133),
        ("Jyväskylä", "Kuopio", 122),
        ("Kuopio", "Kajaani", 148),
        ("Kajaani", "Kuusamo", 205),
    ],
)
# print(graph)
# mst = graph.mst(pretty_print=True)
path_map = graph.dijkstra("Tampere")
path = graph.path_map_to_path(path_map, "Tampere", "Kuopio", pretty_print=True)