package pieces;

import java.io.Serializable;

public abstract class ChessPiece implements Serializable{
	private static final long serialVersionUID = 4768278586264639690L;
	private boolean isWhite;
	private int x;
	private int y;
	
	public ChessPiece(int x, int y){
		setXY(x, y);
	}
	
	public ChessPiece(int x, int y, boolean isWhite){
		setXY(x, y);
		setWhite(isWhite);
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public abstract void print();
	
}
