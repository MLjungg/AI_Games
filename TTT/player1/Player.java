import java.util.*;
import java.util.HashMap;

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
            tempScore = alphaBeta(nextStates.elementAt(i), 2, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer());
            if (tempScore > score){
                score = tempScore;
                index = i;
            }
        }

        //alphaBeta(nextStates.elementAt(0), 3, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getNextPlayer());
        //score = evalFunction(nextStates.elementAt(1), gameState.getNextPlayer());
        return nextStates.elementAt(index);
    }

    /**
     * +100 for EACH 3-in-a-line for computer.
     * +10 for EACH two-in-a-line (with a empty cell) for computer.
     * +1 for EACH one-in-a-line (with two empty cells) for computer.
     * Negative scores for opponent, i.e., -100, -10, -1 for EACH opponent's 3-in-a-line, 2-in-a-line and 1-in-a-line.
     * 0 otherwise (empty lines or lines with both computer's and opponent's seeds).
     * @param gameState, player
     * @return
     */

    public static int evalFunction(GameState gameState, int player){

        HashMap<String, Integer> scoreMapX = new HashMap<String, Integer>();
        HashMap<String, Integer> scoreMapO = new HashMap<String, Integer>();
        int row;
        int col;
        int cellValue;

        // Build dict that keeps count of number of X/O in same row/col/diag
        for (int cell = 0; cell < gameState.CELL_COUNT; cell++) {
            row = gameState.cellToRow(cell);
            col = gameState.cellToCol(cell);
            cellValue = gameState.at(row,col);

            if(cellValue == Constants.CELL_X) {
                if (!scoreMapX.containsKey("row" + row)) {
                    scoreMapX.put("row" + row, 1);
                } else {
                    increment(scoreMapX, "row" + row);
                }
                if (!scoreMapX.containsKey("col" + col)) {
                    scoreMapX.put("col" + col, 1);
                }
                else {
                    increment(scoreMapX, "col" + col);
                }
                if (row == col){
                    if (!scoreMapX.containsKey("diag1")) {
                        scoreMapX.put("diag1", 1);
                    } else {
                        increment(scoreMapX, "diag1");
                    }
                }
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && col == gameState.BOARD_SIZE - 1 - i){
                        if (!scoreMapX.containsKey("diag2")) {
                            scoreMapX.put("diag2", 1);
                        } else {
                            increment(scoreMapX, "diag2");
                        }
                    }
                }

            }
            else if(cellValue == Constants.CELL_O) {
                if (!scoreMapO.containsKey("row" + row)) {
                    scoreMapO.put("row" + row, 1);
                } else {
                    increment(scoreMapO, "row" + row);
                }
                if (!scoreMapO.containsKey("col"+col)) {
                    scoreMapO.put("col" + col, 1);
                }
                else {
                    increment(scoreMapO, "col" + col);
                }
                if (row == col){
                    if (!scoreMapO.containsKey("diag1")) {
                        scoreMapO.put("diag1", 1);
                    } else {
                        increment(scoreMapO, "diag1");
                    }
                }
                for (int i = 0; i < gameState.BOARD_SIZE; i++) {
                    if (row == i && col == gameState.BOARD_SIZE - 1 - i){
                        if (!scoreMapO.containsKey("diag2")) {
                            scoreMapO.put("diag2", 1);
                        } else {
                            increment(scoreMapO, "diag2");
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