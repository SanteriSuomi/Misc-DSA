from abc import abstractmethod
from enum import Enum
import unittest


class MinimaxAI:
    def __init__(self, minimizer, maximizer, start_depth=0, max_depth=8):
        self._start_depth = start_depth
        self._max_depth = max_depth
        self._minimizer = minimizer
        self._maximizer = maximizer

    def start(self, current_board):
        maximizing = current_board.turn() == self._maximizer
        return self._minimax(
            current_board, maximizing, current_board.turn(), self._start_depth
        )

    def _minimax(
        self,
        current_board,
        maximizing,
        original_player,
        current_depth,
        alpha=float("-inf"),  # Best current maximizing move
        beta=float("inf"),  # Best current minimizing move
    ):
        # Minimax algorithm with alpha-beta pruning.
        if (
            current_board.get_is_win()
            or current_board.get_is_draw()
            or current_depth >= self._max_depth
        ):
            return (current_board.evaluate(original_player), -1)

        best_pos = -1
        if maximizing:  # Maximizer (tries to maximize result)
            for move in current_board.get_legal_moves():
                result_eval, _ = self._minimax(
                    current_board.move(move),
                    False,
                    original_player,
                    current_depth + 1,
                    alpha,
                    beta,
                )
                if result_eval > alpha:
                    alpha = result_eval
                    best_pos = move
                if (
                    beta <= alpha
                ):  # If beta (maximizer) is less than or equal to alpha, it means the best move on this branch has already been found, so we should stop the search.
                    break
            return alpha, best_pos
        else:  # Minimizer (tries to minimize result)
            best_pos = -1
            for move in current_board.get_legal_moves():
                result_eval, _ = self._minimax(
                    current_board.move(move),
                    True,
                    original_player,
                    current_depth + 1,
                    alpha,
                    beta,
                )
                if result_eval < beta:
                    beta = result_eval
                    best_pos = move
                if beta <= alpha:
                    break
            return beta, best_pos


class Piece:
    def opposite(self):
        # Get the opposite piece to determine whose turn it is.
        raise NotImplementedError("Needs to be implemented by subclasses of Piece")


class Board:
    @abstractmethod
    def turn(self):
        pass

    @abstractmethod
    def move(self, position):
        pass

    @abstractmethod
    def get_legal_moves(self):
        pass

    @abstractmethod
    def get_is_win(self):
        pass

    def get_is_draw(self):
        return not self.get_is_win() and len(self.get_legal_moves()) == 0

    @abstractmethod
    def evaluate(self, player):
        pass


class TTTPiece(Piece, Enum):
    X = "X"
    O = "O"
    E = " "  # Empty

    def opposite(self):
        if self == TTTPiece.X:
            return TTTPiece.O
        elif self == TTTPiece.O:
            return TTTPiece.X
        else:
            return TTTPiece.E

    def __str__(self) -> str:
        return self.value


class TTTBoard(Board):
    # Represent a tic-tac-toe board
    def __init__(self, board_positions=[TTTPiece.E] * 9, start_turn=TTTPiece.X):
        self.board_positions = board_positions
        self._current_turn = start_turn

    def turn(self):
        return self._current_turn

    def move(self, position):
        # Mark position as occupied by current player turn and return that
        # board state with different turn.
        temp_board_positions = self.board_positions.copy()
        temp_board_positions[position] = self._current_turn
        return TTTBoard(temp_board_positions, self._current_turn.opposite())

    def get_legal_moves(self):
        # Return a list of possible move positions (empty squares)
        return [
            i
            for i in range(len(self.board_positions))
            if self.board_positions[i] == TTTPiece.E
        ]

    def get_is_win(self):
        # Hardcoded win check (for the regular board size of 9)
        return (
            self.board_positions[0] == self.board_positions[1]
            and self.board_positions[0] == self.board_positions[2]
            and self.board_positions[0] != TTTPiece.E
            or self.board_positions[3] == self.board_positions[4]
            and self.board_positions[3] == self.board_positions[5]
            and self.board_positions[3] != TTTPiece.E
            or self.board_positions[6] == self.board_positions[7]
            and self.board_positions[6] == self.board_positions[8]
            and self.board_positions[6] != TTTPiece.E
            or self.board_positions[0] == self.board_positions[3]
            and self.board_positions[0] == self.board_positions[6]
            and self.board_positions[0] != TTTPiece.E
            or self.board_positions[1] == self.board_positions[4]
            and self.board_positions[1] == self.board_positions[7]
            and self.board_positions[1] != TTTPiece.E
            or self.board_positions[2] == self.board_positions[5]
            and self.board_positions[2] == self.board_positions[8]
            and self.board_positions[2] != TTTPiece.E
            or self.board_positions[0] == self.board_positions[4]
            and self.board_positions[0] == self.board_positions[8]
            and self.board_positions[0] != TTTPiece.E
            or self.board_positions[2] == self.board_positions[4]
            and self.board_positions[2] == self.board_positions[6]
            and self.board_positions[2] != TTTPiece.E
        )

    def evaluate(self, player):
        # Evaluate current board state if there is a winner and return either
        # minimizer(player, -1), or maximizer(AI, 1) or nothing (no win, 0)
        win = self.get_is_win()
        if win and self.turn() == player:
            return -1
        elif win and self.turn() != player:
            return 1
        else:
            return 0

    def __str__(self):
        return f"""{self.board_positions[0]}|{self.board_positions[1]}|{self.board_positions[2]}
-----
{self.board_positions[3]}|{self.board_positions[4]}|{self.board_positions[5]}
-----
{self.board_positions[6]}|{self.board_positions[7]}|{self.board_positions[8]}"""


class TTTMinimaxTest(unittest.TestCase):
    # Unit test for testing Tic-tac-toe with minimax
    def test_easy(self):
        # Must win in one move due
        board_positions = [
            TTTPiece.X,
            TTTPiece.O,
            TTTPiece.X,
            TTTPiece.X,
            TTTPiece.E,
            TTTPiece.O,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.O,
        ]
        ttt = TTTBoard(board_positions=board_positions)
        mm = MinimaxAI(minimizer=TTTPiece.O, maximizer=TTTPiece.X)
        position = mm.start(ttt)[1]
        self.assertEqual(position, 6)

    def test_medium(self):
        # Must block player O from winning
        board_positions = [
            TTTPiece.X,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.O,
            TTTPiece.E,
            TTTPiece.X,
            TTTPiece.O,
        ]
        ttt = TTTBoard(board_positions=board_positions)
        mm = MinimaxAI(minimizer=TTTPiece.O, maximizer=TTTPiece.X)
        position = mm.start(ttt)[1]
        self.assertEqual(position, 2)

    def test_hard(self):
        # Must find a position which wins in 2 moves
        board_positions = [
            TTTPiece.X,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.E,
            TTTPiece.O,
            TTTPiece.O,
            TTTPiece.X,
            TTTPiece.E,
        ]
        ttt = TTTBoard(board_positions=board_positions)
        mm = MinimaxAI(minimizer=TTTPiece.O, maximizer=TTTPiece.X)
        position = mm.start(ttt)[1]
        self.assertEqual(position, 1)


class TicTacToe:
    def __init__(self, board=TTTBoard()):
        self._board = board
        self._minimax = MinimaxAI(minimizer=TTTPiece.O, maximizer=TTTPiece.X)

    def _get_player_move(self):
        while True:
            move = int(input("Enter a legal move (1-9):")) - 1
            if move in self._board.get_legal_moves():
                return move

    def _test_state(self, win_message, draw_message):
        if self._board.get_is_win():
            print(win_message)
            return True
        elif self._board.get_is_draw():
            print(draw_message)
            return True

    def play(self):
        while True:
            computer_move = self._minimax.start(self._board)[1]
            self._board = self._board.move(computer_move)
            if self._test_state("Computer wins!", "Draw"):
                break
            print(self._board)
            player_move = self._get_player_move()
            self._board = self._board.move(player_move)
            if self._test_state("Human wins!", "Draw!"):
                break
            print(self._board)
        print(self._board)


# unittest.main()
ttt = TicTacToe()
ttt.play()