package pieces;

public class Rook extends ChessPiece {
	
	private static final long serialVersionUID = 4429837851013541887L;
	private boolean isRookable;

	public Rook(int x, int y, boolean isWhite){
		super(x, y, isWhite);
		isRookable = true;
	}
	
	@Override
	public void print() {
		if(isWhite())
			System.out.print("R");
		else
			System.out.print("r");
	}

	public boolean isRookable() {
		return isRookable;
	}

	public void setRookable(boolean isRookable) {
		this.isRookable = isRookable;
	}

}
