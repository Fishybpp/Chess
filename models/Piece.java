package Programming.SER120.Chess.models;

public class Piece {
	//Variables
	private String[][] pos = new String[8][8];
	private String currentType;
	private String[] promotionType = {"Queen", "Rook", "Knight", "Bishop"};
	private String team;
	private String currentPos;
	private int posRowIndex;
	private int posColIndex;
	private char row = 'A';

	//Constructor
	public Piece() {
		intitalizePos();
		currentType = "King";
	}

	//Methods
	

	private void intitalizePos() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pos[i][j] = row  + Integer.toString((j+1));
			}
			row++;
		}
		currentPos = "A1";
	}

	private void setPosition(String position) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (pos[i][j].equals(position)){
					posRowIndex = i;
					posColIndex = j;
				}
			}
		}
		currentPos = pos[posRowIndex][posColIndex];
	}

	public void movePiece(String position) throws IllegalArgumentException {
		switch(currentType){
			case "King" -> {
				int colDiff = Math.abs(currentPos.charAt(0) - position.charAt(0));
				int rowDiff = Math.abs(currentPos.charAt(1) - position.charAt(1));
				if (colDiff <= 1 && rowDiff <= 1) {
				    setPosition(position);
				}
				else {
					throw new IllegalArgumentException("The King cannot possibly do that Brotato");
				}
			}
            default -> {
            	setPosition(position);
            }
        };
	}

	public String getPosition() {
		return currentPos;
	}

	public String getType() {
		return currentType;
	}

	public int getRowIndex(){
		return posRowIndex;
	}
	public int getColIndex(){
		return posColIndex;
	}

	public void setType(String type) {
		for (int i = 0; i < 8; i++) {
			if (promotionType[i].equals(type)){
				currentType = promotionType[i];
				return;
			}
		}
		throw new RuntimeException("Cannot Find Piece Type");
	}
}