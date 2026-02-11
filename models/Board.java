
package Programming.SER120.Chess.models;
/**
 * A Class representing a Board that can be used in chess games
 * SER 120
 */
public class Board {

	private int numRows;
	private int numCols;

	private String [][] boardData;

	/**
     * Constructor to initialize the  board with specific dimensions.
     * @param rows The number of ranks (rows) on the board.
     * @param cols The number of files (columns) on the board.
     */
	public Board(int rows, int cols){
		this.numRows = rows;
		this.numCols = cols;
		this.boardData = new String[this.numRows][numCols];
		initializeEmptyBoard();

	}
	
	// Helper method to fill the board with empty placeholders
    private void initializeEmptyBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boardData[i][j] = "--";
            }
        }
    }

	public void showBoard() {
	    System.out.println("\n--- Current Chess Board ---");

	    // 1. Top border (the "ceiling")
	    printHorizontalDivider();

	    for (int i = 0; i < numRows; i++) {
	        // 2. Print the row content
	        System.out.print("| ");
	        for (int j = 0; j < numCols; j++) {
	            System.out.print(boardData[i][j] + " | ");
	        }
	        System.out.println(); 

	        // 3. Print the horizontal divider after every row
	        printHorizontalDivider();
	    }
	}

	/**
	 * Helper method to print a consistent horizontal line based on column count
	 */
	private void printHorizontalDivider() {
	    // Each cell is 5 characters wide (| -- ) plus the final closing pipe
	    for (int k = 0; k < numCols; k++) {
	        System.out.print("-----");
	    }
	    System.out.println("-"); // Final dash to cap the last pipe
	}

	public boolean isSquare(){
		return numCols==numRows;
	}

	public static void main(String [] args){
		
	}


}