package pieces;

public class Knight extends ChessPiece {

	private static final long serialVersionUID = -7788434763966037941L;

	public Knight(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
	}

	@Override
	public void print() {
		if(isWhite())
			System.out.print("N");
		else
			System.out.print("n");
	}

}
