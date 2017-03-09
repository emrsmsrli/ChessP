package pieces;

public class Pawn extends ChessPiece {

	private static final long serialVersionUID = 6316881718077195703L;

	public Pawn(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
	}
	
	@Override
	public void print() {
		if(isWhite())
			System.out.print("P");
		else
			System.out.print("p");

	}

}
