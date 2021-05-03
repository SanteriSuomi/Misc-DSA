import heapq


class PQ:
    # Wrapper around the heappush and heappop functions to maintain a binary heap priority queue
    def __init__(self, initial=None):
        if initial is None:
            self._container = []
        else:
            self._container = [initial]

    def empty(self):
        return len(self._container) == 0

    def push(self, item):
        heapq.heappush(self._container, item)

    def pop(self):
        return heapq.heappop(self._container)