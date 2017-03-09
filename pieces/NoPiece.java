package pieces;

public class NoPiece extends ChessPiece {

	private static final long serialVersionUID = 3972595302781650413L;

	public NoPiece(int x, int y) {
		super(x, y);
	}

	@Override
	public void print() {
		System.out.print(" ");
	}

}
