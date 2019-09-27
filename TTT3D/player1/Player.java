import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        int score = 0;
        int tempScore = 0;
        int index = 0;
        for (int i = 0; i < nextStates.size(); i++) {
            tempScore = alphaBeta(nextStates.elementAt(i), 3, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer());
            if (tempScore > score) {
                score = tempScore;
                index = i;
            }
        }
        return nextStates.elementAt(index);
    }

    public static int evalFunction(GameState gameState, int player) {

        HashMap<String, Integer> scoreMapX = new HashMap<String, Integer>();
        HashMap<String, Integer> scoreMapO = new HashMap<String, Integer>();
        int row;
        int col;
        int layer;
        int cellValue;

        // Build dict that keeps count of number of X/O in same row/col/diag
        for (int cell = 0; cell < gameState.CELL_COUNT; cell++) {
            row = gameState.cellToRow(cell);
            col = gameState.cellToCol(cell);
            layer = gameState.cellToLay(cell);
            cellValue = gameState.at(row, col, layer);

            if (cellValue == Constants.CELL_X) {
                // Code below takes care of the 4 layers, treating them as four 2d tictactoes.
                if (!scoreMapX.containsKey("row" + row + "layer" + layer)) {
                    scoreMapX.put("row" + row + "layer" + layer, 1);
                } else {
                    increment(scoreMapX, "row" + row + "layer" + layer);
                }
                if (!scoreMapX.containsKey("col" + col + "layer" + layer)) {
                    scoreMapX.put("col" + col + "layer" + layer, 1);
                } else {
                    increment(scoreMapX, "col" + col + "layer" + layer);
                }
                if (row == col) {
                    if (!scoreMapX.containsKey("diag1 layer" + layer)) {
                        scoreMapX.put("diag1 layer" + layer, 1);
                    } else {
                        increment(scoreMapX, "diag1 layer" + layer);
                    }
                }
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && col == gameState.BOARD_SIZE - 1 - i) {
                        if (!scoreMapX.containsKey("diag2 layer" + layer)) {
                            scoreMapX.put("diag2 layer" + layer, 1);
                        } else {
                            increment(scoreMapX, "diag2 layer" + layer);
                        }
                    }
                }

                // New dimension, "z", horizontically
                if (!scoreMapX.containsKey("row" + row + "col" + col)) {
                    scoreMapX.put("row" + row + "col" + col, 1);
                } else {
                    increment(scoreMapX, "row" + row + "col" + col);
                }

                // Diagonally col is fixed
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && layer == i) {
                        if (!scoreMapX.containsKey("diag col" + col)) {
                            scoreMapX.put("diag col" + col, 1);
                        } else {
                            increment(scoreMapX, "diag col" + col);
                        }
                    }
                }

                // Diagonally row is fixed
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && layer == i) {
                        if (!scoreMapX.containsKey("diag row" + row)) {
                            scoreMapX.put("diag row" + row, 1);
                        } else {
                            increment(scoreMapX, "diag row" + row);
                        }
                    }
                }

                // One hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && row == i && layer == i) {
                        if (!scoreMapX.containsKey("diag3")) {
                            scoreMapX.put("diag3", 1);
                        } else {
                            increment(scoreMapX, "diag3");
                        }
                    }
                }
                // Second hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == gameState.BOARD_SIZE - i - 1 && row == i && layer == i) {
                        if (!scoreMapX.containsKey("diag4")) {
                            scoreMapX.put("diag4", 1);
                        } else {
                            increment(scoreMapX, "diag4");
                        }
                    }
                }
                // Third hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == gameState.BOARD_SIZE - i - 1 && row == gameState.BOARD_SIZE - i - 1 && layer == i) {
                        if (!scoreMapX.containsKey("diag5")) {
                            scoreMapX.put("diag5", 1);
                        } else {
                            increment(scoreMapX, "diag5");
                        }
                    }
                }
                // Forth hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && row == gameState.BOARD_SIZE - i - 1 && layer == gameState.BOARD_SIZE - i - 1) {
                        if (!scoreMapX.containsKey("diag6")) {
                            scoreMapX.put("diag6", 1);
                        } else {
                            increment(scoreMapX, "diag6");
                        }
                    }
                }



            } else if (cellValue == Constants.CELL_O) {
                // Code below takes care of the 4 layers, treating them as four 2d tictactoes.
                if (!scoreMapO.containsKey("row" + row + "layer" + layer)) {
                    scoreMapO.put("row" + row + "layer" + layer, 1);
                } else {
                    increment(scoreMapO, "row" + row + "layer" + layer);
                }
                if (!scoreMapO.containsKey("col" + col + "layer" + layer)) {
                    scoreMapO.put("col" + col + "layer" + layer, 1);
                } else {
                    increment(scoreMapO, "col" + col + "layer" + layer);
                }
                if (row == col) {
                    if (!scoreMapO.containsKey("diag1 layer" + layer)) {
                        scoreMapO.put("diag1 layer" + layer, 1);
                    } else {
                        increment(scoreMapO, "diag1 layer" + layer);
                    }
                }
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && col == gameState.BOARD_SIZE - 1 - i) {
                        if (!scoreMapO.containsKey("diag2 layer" + layer)) {
                            scoreMapO.put("diag2 layer" + layer, 1);
                        } else {
                            increment(scoreMapO, "diag2 layer" + layer);
                        }
                    }
                }

                // Z dimension horisontically
                if (!scoreMapO.containsKey("row" + row + "col" + col)) {
                    scoreMapO.put("row" + row + "col" + col, 1);
                } else {
                    increment(scoreMapO, "row" + row + "col" + col);
                }

                // Diagonal col is fixed
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && layer == i) {
                        if (!scoreMapO.containsKey("diag col" + col)) {
                            scoreMapO.put("diag col" + col, 1);
                        } else {
                            increment(scoreMapO, "diag col" + col);
                        }
                    }
                }

                // Diagonal row is fixed
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && layer == i) {
                        if (!scoreMapO.containsKey("diag row" + row)) {
                            scoreMapO.put("diag row" + row, 1);
                        } else {
                            increment(scoreMapO, "diag row" + row);
                        }
                    }
                }
                // First hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && row == i && layer == i) {
                        if (!scoreMapO.containsKey("diag3")) {
                            scoreMapO.put("diag3", 1);
                        } else {
                            increment(scoreMapO, "diag3");
                        }
                    }
                }
                // Second hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == gameState.BOARD_SIZE - i - 1 && row == i && layer == i) {
                        if (!scoreMapO.containsKey("diag4")) {
                            scoreMapO.put("diag4", 1);
                        } else {
                            increment(scoreMapO, "diag4");
                        }
                    }
                }
                // Third hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == gameState.BOARD_SIZE - i - 1 && row == gameState.BOARD_SIZE - i - 1 && layer == i) {
                        if (!scoreMapO.containsKey("diag5")) {
                            scoreMapO.put("diag5", 1);
                        } else {
                            increment(scoreMapO, "diag5");
                        }
                    }
                }
                // Forth hard diagonal
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (col == i && row == gameState.BOARD_SIZE - i - 1 && layer == gameState.BOARD_SIZE - i - 1) {
                        if (!scoreMapO.containsKey("diag6")) {
                            scoreMapO.put("diag6", 1);
                        } else {
                            increment(scoreMapO, "diag6");
                        }
                    }
                }
            }
        }

        // Count
        int valueX;
        int valueO;
        int value;
        valueX = countValue(scoreMapX, scoreMapO);
        valueO = countValue(scoreMapO, scoreMapX);

        value = valueX - valueO;

        //System.err.println("X matris:");
        //printHashMap(scoreMapX);
        //System.err.println("O matris:");
        //printHashMap(scoreMapO);
        //System.err.println("Värdet: " + value);

        return value;
    }


    public static int alphaBeta (GameState gameState, int depth, int alpha, int beta, int player){
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        int value;
        if (depth == 0 || nextStates.size() == 0){
            value = evalFunction(gameState, player);
            return value;
        }
        else if (player == 1){
            value = Integer.MIN_VALUE;
            for (GameState nextState: nextStates) {
                value = Math.max(value, alphaBeta(nextState, depth-1, alpha, beta, 2));
                alpha = Math.max(alpha, value);
                if (beta <= alpha){
                    break;
                }
            }
        }
        else{
            value = Integer.MAX_VALUE;
            for (GameState nextState:nextStates) {
                value = Math.min(value, alphaBeta(nextState, depth-1, alpha, beta, 1));
                beta = Math.min(beta, value);
                if (beta <= alpha){
                    break;
                }
            }
        }

        return value;
        }

    public static<K> void increment(Map<K, Integer> map, K key) {
        map.putIfAbsent(key, 0);
        map.put(key, map.get(key) + 1);
    }

    public static<K> void printHashMap(Map<K, Integer> map){
        System.err.println(Arrays.asList(map));
    }

    // This function calculate the score of the board. If a line is occupied with both "X" and "O" it is worth 0.
    public static<K> int countValue(Map<K, Integer> map1, Map<K, Integer> map2) {
        int functionValue = 0;
        for (K key : map1.keySet()) {
            if(!map2.containsKey(key)) {
                functionValue += Math.pow(10, map1.get(key));
            }
        }
        return functionValue;
    }
}
