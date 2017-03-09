package board;

import java.io.Serializable;
import java.util.ArrayList;

import sl.saveLoad;
import parser.Parser;
import pieces.*;
import moves.PieceMoves;
import controls.Controls;

public class ChessBoard implements Runnable, Serializable {

	private static ChessPiece [][] chessBoard;
	private static ChessBoard cb;
	private static King whiteK;
	private static King blackK;
	private static boolean turn;
	private static final int DIM = 8;
	private static final long serialVersionUID = 4650705520377743101L;
	private static final boolean white = true;
	private static final boolean black = false;
	
	//initialization of the table.
	private ChessBoard(){
		setTurn(white);
		chessBoard = new ChessPiece[8][8];
		
		for(int i = 0; i < DIM; ++i){
			ChessPiece [] r = chessBoard[i];
			switch(i){
			case 0:
				r[0] = new Rook(i, 0, black);
				r[1] = new Knight(i, 1, black);
				r[2] = new Bishop(i, 2, black);
				r[3] = new Queen(i, 3, black);
				r[4] = new King(i, 4, black);
				r[5] = new Bishop(i, 5, black);
				r[6] = new Knight(i, 6, black);
				r[7] = new Rook(i, 7, black);
				break;
			case 7:
				r[0] = new Rook(i, 0, white);
				r[1] = new Knight(i, 1, white);
				r[2] = new Bishop(i, 2, white);
				r[3] = new Queen(i, 3, white);
				r[4] = new King(i, 4, white);
				r[5] = new Bishop(i, 5, white);
				r[6] = new Knight(i, 6, white);
				r[7] = new Rook(i, 7, white);
				break;
			case 1:
				for(int j = 0; j < DIM; ++j)
					r[j] = new Pawn(i, j, black);
				break;
			case 6:
				for(int j = 0; j < DIM; ++j)
					r[j] = new Pawn(i, j, white);
				break;
			default:
				for(int j = 0; j < DIM; ++j)
					r[j] = new NoPiece(i, j);
				break;
			}
		}
		
		blackK = (King)chessBoard[0][4];
		whiteK = (King)chessBoard[7][4];
		
	}

	//her turdan sonra tahtayý döndürmek için
	//gerekli method
	public static void rotate(){
		ChessPiece rotateTemp;
		
		for(int i = 0; i < DIM/2; ++i){ 		// DIM/2 çünkü tahtanýn geri kalanýný iþlersek eski hale döner
			for(int j = 0; j < DIM; ++j){
				rotateTemp = chessBoard[i][j];
				chessBoard[i][j] = chessBoard[7-i][7-j];
				chessBoard[7-i][7-j] = rotateTemp;
				
				//changes coordinates according to rotation
				chessBoard[i][j].setXY(i, j);
				chessBoard[7-i][7-j].setXY(7-i, 7-j);
			}
		}
	}
	
	//her turdan sonra çaðrýlacak print methodu
	public static void printBoard(){
		System.out.println();
		
		if(whoseTurn() == black) //checks if table is rotated or not
			System.out.println("      H   G   F   E   D   C   B   A");
		else
			System.out.println("      A   B   C   D   E   F   G   H");
		
		System.out.println("    ---------------------------------");
		
		for(int i = 0; i < DIM; ++i){
			
			if(whoseTurn() == black)
				System.out.print("  " + (i+1) + " |");
			else
				System.out.print("  " + (8-i) + " |");
			
			for(int j = 0; j < DIM; ++j){
				System.out.print(" ");
				chessBoard[i][j].print();
				System.out.print(" ");
				System.out.print("|");
			}
			
			if(whoseTurn() == black)
				System.out.println(" " + (i+1) + "\n    ---------------------------------");
			else
				System.out.println(" " + (8-i) + "\n    ---------------------------------");
		}
		
		if(whoseTurn() == black)
			System.out.println("      H   G   F   E   D   C   B   A");
		else
			System.out.println("      A   B   C   D   E   F   G   H");
		
		System.out.println("\n  ************************************");
	}
	
	//kod kalabalýðýný azaltmak için
	public static boolean move(ChessPiece p, int x1, int y1, int x2, int y2){
		int moveStatus = -1;
		boolean ok = false;
		
		if(p.getClass() == Pawn.class){
			moveStatus = PieceMoves.canPawnMove(x1, y1, x2, y2, chessBoard);
		} else if(p.getClass() == Rook.class) {
			moveStatus = PieceMoves.canRookMove(x1, y1, x2, y2, chessBoard);
		} else if(p.getClass() == Knight.class) {
			moveStatus = PieceMoves.canKnightMove(x1, y1, x2, y2, chessBoard);
		} else if(p.getClass() == Bishop.class) {
			moveStatus = PieceMoves.canBishopMove(x1, y1, x2, y2, chessBoard);
		} else if(p.getClass() == Queen.class) {
			moveStatus = PieceMoves.canQueenMove(x1, y1, x2, y2, chessBoard);
		} else if(p.getClass() == King.class) {
			moveStatus = PieceMoves.canKingMove(x1, y1, x2, y2, chessBoard);
		}
		
		if(moveStatus == -1){
			System.err.println("invalid move!");
			ok = false;
		} else if(moveStatus == 0){
			PieceMoves.move(x1, y1, x2, y2, chessBoard);
			ok = true;
		} else if(moveStatus == 1){
			PieceMoves.eat(x1, y1, x2, y2, chessBoard);
			ok = true;
		}
		
		//looks if move you're going to make is creating an self check, which is invalid
		if(ok) {
			if(whoseTurn() == white){
				if(Controls.isCheck(whiteK, chessBoard)){
					System.err.println("invalid move!");

					if(moveStatus == 0){
						PieceMoves.move(x2, y2, x1, y1, chessBoard);
					} else if(moveStatus == 1){
						PieceMoves.revertEat(x1, y1, x2, y2, chessBoard);
					}

					ok = false;
				}
			} else {
				if(Controls.isCheck(blackK, chessBoard)){
					System.err.println("invalid move!");

					if(moveStatus == 0){
						PieceMoves.move(x2, y2, x1, y1, chessBoard);
					} else if(moveStatus == 1){
						PieceMoves.revertEat(x1, y1, x2, y2, chessBoard);
					}

					ok = false;
				}
			}
		}
		
		if(ok) {
			if(p.getClass() == Pawn.class && ((Pawn)p).getX() == 0)
				Controls.promote(p, chessBoard);

			else if(p.getClass() == Rook.class)
				((Rook)chessBoard[x2][y2]).setRookable(false);

			else if(p.getClass() == King.class)
				((King)chessBoard[x2][y2]).setRookable(false);
		}
		
		return ok;
	}
	
	//istenen hareketin geçerli olup olmadýðýný kontrol ettikten sonra
	//hareketi yapacak method
	private boolean makeMove(ArrayList<Integer> from, ArrayList<Integer> to){
		int x1 = from.get(0), 
			y1 = from.get(1), 
			x2 = to.get(0)	, 
			y2 = to.get(1)	;
		ChessPiece p = chessBoard[x1][y1];
		boolean canMove = false;
		
		if(p.getClass() == NoPiece.class){
			System.err.println("You cannot make a move with empty places");
			return canMove;
		}
		
		canMove = move(p, x1, y1, x2, y2);
		
		//controls if move is successful or not
		if(!canMove) 
			return false;
		
		return true;
	}
	
	//main runnable method
	@Override
	public void run() {
		int control = 0;
		boolean moveCont;
		Parser p = Parser.getInstance();
		ArrayList<ArrayList<Integer>> m;
		ArrayList<ArrayList<Integer>> mR = new ArrayList<ArrayList<Integer>>();
		mR.add(new ArrayList<Integer>());
		mR.add(new ArrayList<Integer>());
		
		//initializes the menu
		init(p, control);
		
		while(true){
			m = p.parse();
			control = m.get(0).get(0);
			
			//parse error check
			if(control == 99)
				continue;
			
			if(control == 10){
				if(turn == white)
					moveCont = Controls.castling(whiteK, m.get(1).get(0).intValue(), chessBoard);
				else
					moveCont = Controls.castling(blackK, m.get(1).get(0).intValue(), chessBoard);
			} 
			
			else if(control == 40){
				Controls.resign();
				break;
			}
			
			else if(control == 20){
				saveLoad.saveGame(chessBoard, whiteK, blackK, turn);
				System.out.println("SAVED");
				moveCont = false;
			}
			
			else if(whoseTurn() == black){ //mR is the coor for rotated table 
				p.resetParser(mR);
				mR.get(0).add(7 - m.get(0).get(0));
				mR.get(0).add(7 - m.get(0).get(1));
				mR.get(1).add(7 - m.get(1).get(0));
				mR.get(1).add(7 - m.get(1).get(1));
				moveCont = makeMove(mR.get(0), mR.get(1));
			}
			else moveCont = makeMove(m.get(0), m.get(1));
			
			if(!moveCont)
				continue;
			
			Controls.isCheckMate(chessBoard);
			
			//passes turn
			if(whoseTurn() == white)
				turn = black;
			else
				turn = white;
			
			rotate();
			printBoard();
			
		}  	//END OF SESSION
		
		cb = null;
		p.close();
	}
	
	private void init(Parser p, int control){
		System.out.println("WELCOME TO CHESS V1.3");
		System.out.println("1 - play\n2 - available commands\n3 - load game\n4 - quit");
		
		while(true){
			control = p.parseMenu().get(0).get(0);
			
			if(control == 99)
				continue;
			
			if(control == 101)
				break;
			
			else if(control == 102){
				System.out.println("- MOVING PIECES: \"D4 D3\"");
				System.out.println("- CASTLING: \"cast (L) or cast (R)\"");
				System.out.println("- PROMOTION: \"prom @ChessPiece\"");
				System.out.println("- RESIGNING: \"resign\"");
			} 
			
			else if(control == 30){
				chessBoard = null;
				cb = null;
				saveLoad.loadGame();
				
				if(chessBoard == null){
					System.err.println("LOAD ERROR, NEW GAME STARTING");
					ChessBoard.getInstance();
					break;
				} else {
					System.out.println("LOADED");
					break;
				}
			}
			
			else if(control == 100){
				p.close();
				System.exit(0);
			}
		}
		
		printBoard();
	}
	
	//sýranýn kimde olduðunu öðrenmek için
	public static boolean whoseTurn(){
		return turn;
	}

	//sadece bi tane board olmasý için getInstance() methodu
	public static ChessBoard getInstance(){
		if(cb == null)
			cb = new ChessBoard();
		return cb;
	}

	public static King getWhiteK() {
		return whiteK;
	}

	public static King getBlackK() {
		return blackK;
	}

	public static void setTurn(boolean turn) {
		ChessBoard.turn = turn;
	}
	
	public static void loadBoard(ChessPiece [][] cb, King w, King b, boolean turn){
		chessBoard = cb;
		whiteK = w;
		blackK = b;
		setTurn(turn);
	}
	
}	//end of class
