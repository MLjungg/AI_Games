import java.util.*;

public class Player {
    static int iAmPlayer;
    static HashMap<String, Node> calculatedStates = new HashMap<String, Node>();
    static boolean searchInterupted = false;
    /**
     * Performs a move
     *
     * @param pState
     *            the current state of the board
     * @param pDue
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState pState, final Deadline pDue) {

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
        iAmPlayer =  pState.getNextPlayer();

        if (lNextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(pState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */


        searchInterupted = false;
        int index = 0;
        int depth = 1;
        while(true) {
            int score = Integer.MIN_VALUE;
            int tempScore = 0;
            int tempIndex = 0;
            //System.err.println(depth);
            for (int i = 0; i < lNextStates.size(); i++) {
                tempScore = alphaBeta(lNextStates.elementAt(i), depth, Integer.MIN_VALUE, Integer.MAX_VALUE, pState.getNextPlayer(), pDue);
                if (tempScore > score) {
                    score = tempScore;
                    tempIndex = i;
                }
            }
            // If we don't complete a whole iteration we are better off returning the previous value
            if (searchInterupted){
                break;
            }
            else{
                index = tempIndex;
                depth++;
            }
        }
        //System.err.println(index);
        return lNextStates.elementAt(index);
    }

    /*
     * and to check if it is a king, you would check if
     *
     *   (lBoard.At(23)&CELL_KING)
     */

    /**
     * This function counts the normal pieces as 1 point and the kings as 2 points.
     *
     */


    public static String createHashKey(GameState gameState){
        StringBuilder key = new StringBuilder();
        int row;
        int col;
        int cellValue;
        for (int cell = 0; cell < gameState.NUMBER_OF_SQUARES; cell++) {
            row = gameState.cellToRow(cell);
            col = gameState.cellToCol(cell);
            cellValue = gameState.get(row, col);
            key.append(cellValue);
        }

        return key.toString();

    }

    public static int evalFunction(GameState gameState, int player) {

        HashMap<String, Integer> scoreMapX = new HashMap<String, Integer>();
        HashMap<String, Integer> scoreMapO = new HashMap<String, Integer>();
        int row;
        int col;
        int cellValue;
        int something;
        int numberOfRed = 0;
        int numberOfWhite = 0;

        for (int cell = 0; cell < gameState.NUMBER_OF_SQUARES; cell++) {
            row = gameState.cellToRow(cell);
            col = gameState.cellToCol(cell);
            cellValue = gameState.get(row, col);
            if (cellValue == Constants.CELL_RED){
                numberOfRed += 1;
            }
            else if (cellValue == Constants.CELL_RED + Constants.CELL_KING){
                numberOfRed += 1.5;
            }
            else if (cellValue == Constants.CELL_WHITE){
                numberOfWhite += 1;
            }
            else if (cellValue == Constants.CELL_WHITE + Constants.CELL_KING){
                numberOfWhite += 1.5;
            }
        }
        int value;

        // player 1 is red
        if (iAmPlayer == 1) {
            value = numberOfRed - numberOfWhite;
        }
        else{
            value = numberOfWhite - numberOfRed;
        }

        return value;
    }

    public static int alphaBeta (GameState gameState, int depth, int alpha, int beta, int player, Deadline deadline){
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        int value;

        if (deadline.timeUntil() < 100000000/10){
            searchInterupted = true;
        }


        // Repeat stake check
        String key = createHashKey(gameState);
        if(calculatedStates.containsKey(key)){
            Node node = calculatedStates.get(key);
            if (node.depth > depth){
                //System.err.println("Tvilling");
                return node.value;
            }
        }

        if (depth == 0 || nextStates.size() == 0 || searchInterupted){
            value = evalFunction(gameState, player);
            return value;
        }
        else if (player == 1){
            value = Integer.MIN_VALUE;
            for (GameState nextState: nextStates) {
                value = Math.max(value, alphaBeta(nextState, depth-1, alpha, beta, 2, deadline));

                // Lägg till värdet
                Node node = new Node(value, depth);
                calculatedStates.put(key, node);

                alpha = Math.max(alpha, value);
                if (beta <= alpha){
                    break;
                }
            }
        }
        else{
            value = Integer.MAX_VALUE;
            for (GameState nextState:nextStates) {
                value = Math.min(value, alphaBeta(nextState, depth-1, alpha, beta, 1, deadline));

                // Lägg till värdet
                Node node = new Node(value, depth);
                calculatedStates.put(key, node);

                beta = Math.min(beta, value);
                if (beta <= alpha){
                    break;
                }
            }
        }

        return value;
    }

}
