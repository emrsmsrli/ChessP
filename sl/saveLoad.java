package sl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import board.ChessBoard;

import java.io.IOException;

import pieces.ChessPiece;
import pieces.King;

public class saveLoad {
	
	public static void saveGame(ChessPiece [][] cb, King w, King b, boolean turn) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("save.bin")));
			out.writeObject(cb);
			out.writeObject(w);
			out.writeObject(b);
			out.writeObject(turn);
			out.close();
		} catch (IOException e) {
			System.err.println("I/O Error");
		}
	}
	
	public static void loadGame() {
		File f = new File("save.bin");
		
		if(!f.exists())
			return;
		
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			ChessPiece [][] cb = (ChessPiece [][])in.readObject();
			King w = (King)in.readObject();
			King b = (King)in.readObject();
			boolean turn =(boolean)in.readObject();
			ChessBoard.loadBoard(cb, w, b, turn);
			in.close();
		} catch (IOException e) {
			System.err.println("I/O Error");
		} catch (ClassNotFoundException e) {
			System.err.println("Class not recognized");
		}
	}

}
