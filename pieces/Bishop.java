package pieces;

public class Bishop extends ChessPiece {

	private static final long serialVersionUID = -4853470504090408653L;

	public Bishop(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
	}

	@Override
	public void print() {
		if(isWhite())
			System.out.print("B");
		else
			System.out.print("b");
	}

}
