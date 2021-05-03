import enum
import typing
import random
import collections
import time
import PQ


class Cell(enum.Enum):
    EMPTY = " "
    BLOCKED = "X"
    START = "S"
    GOAL = "G"
    PATH = "*"


class MazeLocation(typing.NamedTuple):
    row: int
    column: int


class Node:
    cost = 1.0
    start: MazeLocation  # This node
    _goal: MazeLocation  # Goal node

    def __init__(self, start, goal):
        self.start = start
        self._goal = goal

    def heuristic(self):
        # Manhattan Distance (best admissible heuristic for mazes, as no diagonal movement permitted)
        x = abs(self.start.column - self._goal.column)
        y = abs(self.start.row - self._goal.row)
        return x + y

    def __lt__(self, other):
        return (self.cost + self.heuristic()) < (other.cost + other.heuristic())


class Maze:
    _rows: int
    _columns: int
    _sparseness: float
    _start: MazeLocation
    _goal: MazeLocation

    _matrix: list[list[Cell]]

    def __init__(
        self, rows, columns, sparseness, start: MazeLocation, goal: MazeLocation
    ):
        self._rows = rows
        self._columns = columns
        self._sparseness = sparseness
        self._start = start
        self._goal = goal
        self._matrix = [[Cell.EMPTY for _ in range(columns)] for _ in range(rows)]
        self._fill(self._matrix, rows, columns, sparseness)
        self._matrix[start.row][start.column] = Cell.START
        self._matrix[goal.row][goal.column] = Cell.GOAL

    def _fill(self, matrix, rows, columns, sparseness):
        for row in range(rows):
            for column in range(columns):
                if random.uniform(0, 1.0) < sparseness:
                    matrix[row][column] = Cell.BLOCKED

    def __str__(self):
        output = ""
        for row in self._matrix:
            output += "".join([c.value for c in row]) + "\n"
        return output

    def _is_goal(self, location: MazeLocation):
        return location.row == self._goal.row and location.column == self._goal.column

    def _get_neighbours(self, location: MazeLocation):
        # Get all available neighbours of given location (Cell.EMPTY)
        neighbours = []
        if (
            location.row + 1 < self._rows
            and self._matrix[location.row + 1][location.column] != Cell.BLOCKED
        ):
            neighbours.append(MazeLocation(location.row + 1, location.column))
        if (
            location.row - 1 >= 0
            and self._matrix[location.row - 1][location.column] != Cell.BLOCKED
        ):
            neighbours.append(MazeLocation(location.row - 1, location.column))
        if (
            location.column + 1 < self._columns
            and self._matrix[location.row][location.column + 1] != Cell.BLOCKED
        ):
            neighbours.append(MazeLocation(location.row, location.column + 1))
        if (
            location.column - 1 >= 0
            and self._matrix[location.row][location.column - 1] != Cell.BLOCKED
        ):
            neighbours.append(MazeLocation(location.row, location.column - 1))
        return neighbours

    def dfs_find_goal(self):
        frontier = [self._start]  # Not yet searched
        explored = {self._start}  # Already explored
        while len(frontier) > 0:
            current = frontier.pop()
            if self._is_goal(current):
                return current
            for neighbour in self._get_neighbours(current):
                if not neighbour in explored:
                    frontier.append(neighbour)
                    explored.add(neighbour)
        return None

    def bfs_find_goal(self):
        frontier = collections.deque([self._start])  # Not yet searched
        explored = {self._start}  # Already explored
        while len(frontier) > 0:
            current = frontier.popleft()
            if self._is_goal(current):
                return current
            for neighbour in self._get_neighbours(current):
                if not neighbour in explored:
                    frontier.append(neighbour)
                    explored.add(neighbour)
        return None

    def astar_find_goal(self):
        start_node = Node(self._start, self._goal)
        frontier = PQ(start_node)  # Not yet searched
        explored = {start_node: 0.0}  # Already explored
        while not frontier.empty():
            current = frontier.pop()
            if self._is_goal(current.start):
                return current.start
            for neighbour in self._get_neighbours(current.start):
                new_cost = current.cost + 1
                if not neighbour in explored or explored[neighbour] > new_cost:
                    frontier.push(Node(neighbour, self._goal))
                    explored[neighbour] = new_cost
        return None


maze = Maze(20, 15, 0.25, MazeLocation(0, 0), MazeLocation(19, 14))
print(maze)
start_time = time.time()
print(maze.dfs_find_goal())
print("DFS took", time.time() - start_time, "to run")
start_time = time.time()
print(maze.bfs_find_goal())
print("BFS took", time.time() - start_time, "to run")
start_time = time.time()
print(maze.astar_find_goal())
print("AStar took", time.time() - start_time, "to run")