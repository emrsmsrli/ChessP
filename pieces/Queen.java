package pieces;

public class Queen extends ChessPiece {

	private static final long serialVersionUID = 7853640908632511189L;

	public Queen(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
	}

	@Override
	public void print() {
		if(isWhite())
			System.out.print("Q");
		else
			System.out.print("q");
	}

}
