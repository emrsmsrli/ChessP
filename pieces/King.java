package pieces;

public class King extends ChessPiece {

	private static final long serialVersionUID = 4932480537573301283L;
	private boolean isRookable;
	
	public King(int x, int y, boolean isWhite) {
		super(x, y, isWhite);
		isRookable = true;
	}

	@Override
	public void print() {
		if(isWhite())
			System.out.print("K");
		else
			System.out.print("k");
	}

	public boolean isRookable() {
		return isRookable;
	}

	public void setRookable(boolean isRookable) {
		this.isRookable = isRookable;
	}

}
