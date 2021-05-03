# Solving constraint-satisfaction problems

import abc


class Constraint:
    variables: list

    def __init__(self, variables: list):
        self.variables = variables

    @abc.abstractclassmethod
    def is_satisfied(self, assignment):
        pass


class MapColoringConstraint(Constraint):
    # Override constraint class for the Australian map-coloring problem
    region1: str
    region2: str

    def __init__(self, region1, region2):
        variables = [region1, region2]
        Constraint.__init__(self, variables)
        self.region1 = region1
        self.region2 = region2

    def is_satisfied(self, assignment):
        if self.region1 not in assignment or self.region2 not in assignment:
            return True
        return assignment[self.region1] != assignment[self.region2]


class EightQueensConstraint(Constraint):
    # Override constraint class for the eight queens problem
    columns: list

    def __init__(self, columns):
        Constraint.__init__(self, columns)
        self.columns = columns

    def is_satisfied(self, assignment):
        # q1c = queen 1 column, q1r = queen 1 row
        for q1c, q1r in assignment.items():
            # queen 2 column
            for q2c in range(q1c + 1, len(self.columns) + 1):
                if q2c in assignment:
                    q2r = assignment[q2c]
                    # Same row? or same diagonal?
                    if q1r == q2r or (abs(q1r - q2r) == abs(q1c - q2c)):
                        return False
        return True


class CSP:
    # Framework for solving constraint satisfaction problems
    variables: list  # List of all variables
    domains: dict  # Map variables to list of possible values
    constraints: dict  # Map variables to list of constraints

    def __init__(self, variables: list, domains: dict):
        self.variables = variables
        self.domains = domains
        self.constraints = {}
        for v in variables:
            self.constraints[v] = []
            if v not in self.domains:
                raise LookupError("Every variable should have a domain assigned to it.")

    def add_constraint(self, constraint: Constraint):
        for v in constraint.variables:
            if v not in self.variables:
                raise LookupError("Variable in constraint not in CSP variables list.")
            else:
                self.constraints[v].append(constraint)

    def is_consistent(self, variable, assignment):
        for c in self.constraints[variable]:
            if not c.is_satisfied(assignment):
                return False
        return True

    def backtrack_search(self, assignment={}):
        if len(assignment) == len(self.variables):
            return assignment
        unassigned = [v for v in self.variables if v not in assignment]
        first = unassigned[0]
        for value in self.domains[first]:
            local = assignment.copy()
            local[first] = value
            if self.is_consistent(first, local):
                result = self.backtrack_search(local)
                if result is not None:
                    return result
        return None


if __name__ == "__main__":
    # Australian map-coloring problem
    variables = ["WA", "NT", "SA", "Q", "NSW", "V", "T"]  # Australian regions
    domains = {}
    for v in variables:
        domains[v] = [
            "red",
            "green",
            "blue",
        ]  # All regions have three possible colors (domains)
    csp = CSP(variables, domains)  # CSP solver itself
    csp.add_constraint(MapColoringConstraint("WA", "NT"))
    csp.add_constraint(MapColoringConstraint("WA", "SA"))
    csp.add_constraint(MapColoringConstraint("SA", "NT"))
    csp.add_constraint(MapColoringConstraint("Q", "NT"))
    csp.add_constraint(MapColoringConstraint("Q", "NSW"))
    csp.add_constraint(MapColoringConstraint("NSW", "SA"))
    csp.add_constraint(MapColoringConstraint("V", "SA"))
    csp.add_constraint(MapColoringConstraint("V", "NSW"))
    csp.add_constraint(MapColoringConstraint("V", "T"))
    solution = csp.backtrack_search()
    if solution is None:
        print("No solution!")
    else:
        print("Solution:", solution)

    columns = [1, 2, 3, 4, 5, 6, 7, 8]
    rows = {}
    for col in columns:
        rows[col] = [1, 2, 3, 4, 5, 6, 7, 8]
    csp = CSP(columns, rows)
    csp.add_constraint(EightQueensConstraint(columns))
    solution = csp.backtrack_search()
    if solution is None:
        print("No solution!")
    else:
        print("Solution:", solution)
