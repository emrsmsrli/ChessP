package controls;

import pieces.*;
import board.ChessBoard;
import moves.PieceMoves;
import parser.Parser;

public class Controls {

	public static boolean moveGenerator(ChessPiece [][] cb){


		/*
		 * 
		 * x = Math.min(Math.max(p.getX() + m, 0), 7); //clamp indexes to 0-7
		 * y = Math.min(Math.max(p.getY() + n, 0), 7);
		 * 
		 */

		//returns false if there is no valid moves
		ChessPiece p;
		int x, y;
		
		if(ChessBoard.whoseTurn() == true)
			ChessBoard.setTurn(false);
		else
			ChessBoard.setTurn(true);

		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				p = cb[i][j];
				if(p.isWhite() == ChessBoard.whoseTurn()){
					if(p.getClass() == NoPiece.class) 	//don't process empty places
						continue;

					else if(p.getClass() == King.class){ //process checked king's moves
						for(int m = -1; m <= 1; ++m){
							for(int n = -1; n <= 1; ++n){
								if(m == 0 && n == 0) 	//don't process the king itself
									continue;

								x = Math.min(Math.max(p.getX() + m, 0), 7);
								y = Math.min(Math.max(p.getY() + n, 0), 7);

								int move = PieceMoves.canKingMove(p.getX(), p.getY(), x, y, cb);

								if(move == 0 || move == 1)
									if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){ //if this is true, a valid move is found
										if(ChessBoard.whoseTurn() == true)
											ChessBoard.setTurn(false);
										else
											ChessBoard.setTurn(true);
										return true;
									}
							}
						}
					}

					else if(p.getClass() == Pawn.class){ //process pawns' moves
						x = p.getX() + 1;
						for(int m = -1; m <= 1; ++m){
							y = Math.min(Math.max(p.getY() + m, 0), 7);

							int move = PieceMoves.canPawnReverseMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move == 1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}
					}

					else if(p.getClass() == Knight.class){ //process knights' moves
						for(int m = -2; m <= 2; ++m){
							for(int n = -2; n <= 2; ++n){
								if(m == 0 || n == 0 || Math.abs(m) == Math.abs(n)) //continue if can't catch L move
									continue;


								x = Math.min(Math.max(p.getX() + m, 0), 7);
								y = Math.min(Math.max(p.getY() + n, 0), 7);

								int move = PieceMoves.canKnightMove(p.getX(), p.getY(), x, y, cb);

								if(move == 0 || move == 1)
									if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
										if(ChessBoard.whoseTurn() == true)
											ChessBoard.setTurn(false);
										else
											ChessBoard.setTurn(true);
										return true;
									}
							}
						}
					}

					else if(p.getClass() == Rook.class){ //process checked rooks' moves
						y = p.getY();

						for(int m = -7; m <= 7; ++m){ //vertical
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							int move = PieceMoves.canRookMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}

						x = p.getX();

						for(int m = -7; m <= 7; ++m){ //horizontal
							y = Math.min(Math.max(p.getY() + m, 0), 7);
							int move = PieceMoves.canRookMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}
					} 

					else if(p.getClass() == Bishop.class){ //process checked bishops' moves
						for(int m = -7; m <= 7; ++m){ //NE - SW
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							y = Math.min(Math.max(p.getY() - m, 0), 7);

							int move = PieceMoves.canBishopMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}

						for(int m = -7; m <= 7; ++m){ //NW - SE
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							y = Math.min(Math.max(p.getY() + m, 0), 7);

							int move = PieceMoves.canBishopMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move == 1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}
					}

					else if(p.getClass() == Queen.class){ //process checked queens' moves
						y = p.getY();

						for(int m = -7; m <= 7; ++m){ //vertical
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							int move = PieceMoves.canQueenMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move == 1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}

						x = p.getX();

						for(int m = -7; m <= 7; ++m){ //horizontal
							y = Math.min(Math.max(p.getY() + m, 0), 7);
							int move = PieceMoves.canQueenMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}

						for(int m = -7; m <= 7; ++m){ //NE - SW
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							y = Math.min(Math.max(p.getY() - m, 0), 7);

							int move = PieceMoves.canQueenMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}

						for(int m = -7; m <= 7; ++m){ //NW - SE
							x = Math.min(Math.max(p.getX() + m, 0), 7);
							y = Math.min(Math.max(p.getY() + m, 0), 7);

							int move = PieceMoves.canQueenMove(p.getX(), p.getY(), x, y, cb);

							if(move == 0 || move ==1)
								if(PieceMoves.moveControl(p.getX(), p.getY(), x, y, cb)){
									if(ChessBoard.whoseTurn() == true)
										ChessBoard.setTurn(false);
									else
										ChessBoard.setTurn(true);
									return true;
								}
						}
					}
				} // if not your piece, continue searching
				continue;
			}
		}
		
		//if cant found any valid moves, return
		if(ChessBoard.whoseTurn() == true)
			ChessBoard.setTurn(false);
		else
			ChessBoard.setTurn(true);

		return false;
	}

	public static void isCheckMate(ChessPiece [][] cb){
		//karþý oyuncuya þah çekildi mi ve mat-pat mý kontrol eder
		if(ChessBoard.whoseTurn() == true){
			if(Controls.isCheck(ChessBoard.getBlackK(), cb)){
				if(!moveGenerator(cb))
					checkMate();
				System.out.println("white checked to black!");
			} else {
				if(!moveGenerator(cb))
					staleMate();
			}
		} else {
			if(Controls.isCheck(ChessBoard.getWhiteK(), cb)){
				if(!moveGenerator(cb))
					checkMate();
				System.out.println("black checked to white!");
			} else {
				if(!moveGenerator(cb))
					staleMate();
			}
		}
	}

	public static boolean castling(King k, int pos, ChessPiece [][] cb) {

		boolean canCastle = false;

		if(isCheck(k, cb)){
			System.err.println("castling not allowed while checked!");
			return false;
		}

		if(k.isWhite() == true){
			switch(pos){
			case 1:
				if(cb[7][0].getClass() == Rook.class){
					if(k.isRookable() && ((Rook)cb[7][0]).isRookable()){
						if(PieceMoves.canRookMove(7, 0, 7, 3, cb) == 0){
							PieceMoves.move(7, 4, 7, 2, cb); //king
							if(!isCheck(k, cb)){
								PieceMoves.move(7, 0, 7, 3, cb); //rook
								canCastle = true;
							} else {
								PieceMoves.move(7, 2, 7, 4, cb); //king revert
								canCastle = false;
							}
						} else
							canCastle = false;
					} else 
						canCastle = false;
				} else
					canCastle = false;
				break;
			case 2:
				if(cb[7][7].getClass() == Rook.class){
					if(k.isRookable() && ((Rook)cb[7][7]).isRookable()){
						if(PieceMoves.canRookMove(7, 7, 7, 5, cb) == 0){
							PieceMoves.move(7, 4, 7, 6, cb); //king
							if(!isCheck(k, cb)){
								PieceMoves.move(7, 7, 7, 5, cb); //rook
								canCastle = true;
							} else {
								PieceMoves.move(7, 5, 7, 7, cb); //king revert
								canCastle = false;
							}
						} else
							canCastle = false;
					} else
						canCastle = false;
				} else
					canCastle = false;
				break;
			}//end of switch
		} //end of if
		else {
			switch(pos){
			case 1:
				if(cb[7][0].getClass() == Rook.class){
					if(k.isRookable() && ((Rook)cb[7][0]).isRookable()){
						if(PieceMoves.canRookMove(7, 0, 7, 2, cb) == 0){
							PieceMoves.move(7, 3, 7, 1, cb); //king
							if(!isCheck(k, cb)){
								PieceMoves.move(7, 0, 7, 2, cb); //rook
								canCastle = true;
							} else {
								PieceMoves.move(7, 1, 7, 3, cb); //king revert
								canCastle = false;
							}
						} else
							canCastle = false;
					} else
						canCastle = false;
				} else
					canCastle = false;
				break;
			case 2:
				if(cb[7][7].getClass() == Rook.class){
					if(k.isRookable() && ((Rook)cb[7][7]).isRookable()){
						if(PieceMoves.canRookMove(7, 7, 7, 4, cb) == 0){
							PieceMoves.move(7, 3, 7, 5, cb); //king
							if(!isCheck(k, cb)){
								PieceMoves.move(7, 7, 7, 4, cb); //rook
								canCastle = true;
							} else {
								System.err.println("castling not allowed!");
								PieceMoves.move(7, 5, 7, 3, cb); // king revert
								canCastle = false;
							}
						} else
							canCastle = false;
					} else
						canCastle = false;
				} else
					canCastle = false;
			}//end of switch
		}//end of else

		if(!canCastle){
			System.err.println("castling not allowed!");
			return false;
		}

		return true;
	}

	public static void resign() {
		if(ChessBoard.whoseTurn() == true){
			System.out.println("White resigned, black wins!");
		} else {
			System.out.println("Black resigned, white wins!");
		}
	}

	public static void promote(ChessPiece p, ChessPiece [][] cb){
		while(true){
			switch(Parser.getInstance().parseProm().get(0).get(0)){
			case 1:
				cb[p.getX()][p.getY()] = new Rook(p.getX(), p.getY(), p.isWhite()); 
				return;
			case 2:
				cb[p.getX()][p.getY()] = new Knight(p.getX(), p.getY(), p.isWhite()); 
				return;
			case 3:
				cb[p.getX()][p.getY()] = new Bishop(p.getX(), p.getY(), p.isWhite()); 
				return;
			case 4:
				cb[p.getX()][p.getY()] = new Queen(p.getX(), p.getY(), p.isWhite()); 
				return;
			default:
				continue;
			}
		}
	}

	public static void staleMate(){
		System.out.println("STALEMATE, draw!");
		ChessBoard.printBoard();
		System.exit(0);
	}

	public static void checkMate(){
		if(ChessBoard.whoseTurn() == false){
			System.out.println("CHECKMATE, black wins!");
		} else {
			System.out.println("CHECKMATE, white wins!");
		}
		ChessBoard.printBoard();
		System.exit(0);
	}

	public static boolean isCheck(King k, ChessPiece cb[][]){ //Yollanan þah ve koordinatlarý
		boolean colour = k.isWhite();
		int i = k.getX();
		int j = k.getY();

		for(int a=-1; a<2; a++){ // king to king
			for(int b=-1; b<2; b++){
				if(i+a < 8 && i+a > 0 && j+b < 8 && j+b > 0 && colour!=cb[i+a][j+b].isWhite() && cb[i+a][j+b].getClass() == King.class)
					return true;
			}	
		}

		if( i+2<8 && j+1<8 && cb[i+2][j+1].getClass()== Knight.class && cb[i+2][j+1].isWhite() != colour ) // At için olanlar
			return true;
		else if( i+2<8 && j-1>=0 && cb[i+2][j-1].getClass() == Knight.class && cb[i+2][j-1].isWhite() != colour  )
			return true;
		else if( i-2>=0 && j+1<8 && cb[i-2][j+1].getClass() == Knight.class && cb[i-2][j+1].isWhite() != colour  )
			return true;
		else if( i-2>=0 && j-1>=0 && cb[i-2][j-1].getClass() == Knight.class && cb[i-2][j-1].isWhite() != colour  )
			return true;
		else if( i+1<8 && j+2<8 &&  cb[i+1][j+2].getClass() == Knight.class && cb[i+1][j+2].isWhite() != colour  )
			return true;
		else if( i+1<8 && j-2>=0 && cb[i+1][j-2].getClass() == Knight.class && cb[i+1][j-2].isWhite() != colour )
			return true;
		else if ( i-1>=0 && j+2<8 && cb[i-1][j+2].getClass() == Knight.class && cb[i-1][j+2].isWhite() != colour )
			return true;
		else if ( i-1 >=0 && j-2>=0 && cb[i-1][j-2].getClass() == Knight.class && cb[i-1][j-2].isWhite() != colour )
			return true;

		if( i+1<8 && j-1>=0 && cb[i+1][j-1].isWhite() != colour && ( cb[i+1][j-1].getClass() == Pawn.class ) ) // piyon için
			return true;
		else if( i-1>=0 && j-1>=0 && cb[i-1][j-1].isWhite() != colour && ( cb[i-1][j-1].getClass() == Pawn.class ) )
			return true;

		while( i+1<8 && j+1<8 ) //çaprazda vezir veya fil kontrolü
		{
			if( cb[i+1][j+1].isWhite() != colour && ( cb[i+1][j+1].getClass() == Queen.class || cb[i+1][j+1].getClass() ==Bishop.class  ) )
				return true;			
			else if ( cb[i+1][j+1].getClass() != NoPiece.class   )
				break;
			i++;j++;
		}
		i = k.getX();
		j = k.getY();

		while( i+1<8 && j-1>=0 ) //çaprazda vezir veya fil kontrolü 2
		{
			if( cb[i+1][j-1].isWhite() != colour && ( cb[i+1][j-1].getClass() == Queen.class || cb[i+1][j-1].getClass() ==Bishop.class  ) )
				return true;			
			else if ( cb[i+1][j-1].getClass() != NoPiece.class   )
				break;
			i++;j--;
		}
		i = k.getX();
		j = k.getY();

		while( i-1>=0 && j+1<8 ) //çaprazda vezir veya fil kontrolü 3
		{
			if( cb[i-1][j+1].isWhite() != colour && ( cb[i-1][j+1].getClass() == Queen.class || cb[i-1][j+1].getClass() ==Bishop.class  ) )
				return true;			
			else if ( cb[i-1][j+1].getClass() != NoPiece.class   )
				break;
			i--;j++;
		}
		i = k.getX();
		j = k.getY();

		while( i-1>=0 && j-1>=0 ) // çaprazda vezir veya fil kontrolü 4
		{
			if( cb[i-1][j-1].isWhite() != colour && ( cb[i-1][j-1].getClass() == Queen.class || cb[i-1][j-1].getClass() == Bishop.class  ) )
				return true;			
			else if ( cb[i-1][j-1].getClass() != NoPiece.class   )
				break;
			i--;j--;
		}

		i = k.getX();
		j = k.getY();

		while( j-1>=0) // dikeyde kale veya vezir kontrolü
		{
			if( cb[i][j-1].isWhite() != colour && ( cb[i][j-1].getClass() == Queen.class || cb[i][j-1].getClass() == Rook.class  ) )
				return true;			
			else if ( cb[i][j-1].getClass() != NoPiece.class   )
				break;
			j--;
		}

		j = k.getY();

		while( j+1<8) // dikeyde kale veya vezir kontrolü 2
		{
			if( cb[i][j+1].isWhite() != colour && ( cb[i][j+1].getClass() == Queen.class || cb[i][j+1].getClass() == Rook.class  ) )
				return true;			
			else if ( cb[i][j+1].getClass() != NoPiece.class   )
				break;
			j++;
		}
		j = k.getY();

		while( i-1>=0) // yatayda kale veya vezir kontrolü
		{
			if( cb[i-1][j].isWhite() != colour && ( cb[i-1][j].getClass() == Queen.class || cb[i-1][j].getClass() == Rook.class  ) )
				return true;			
			else if ( cb[i-1][j].getClass() != NoPiece.class   )
				break;
			i--;
		}

		i = k.getX();

		while( i+1<8) // yatayda kale veya vezir kontrolü 2
		{
			if( cb[i+1][j].isWhite() != colour && ( cb[i+1][j].getClass() == Queen.class || cb[i+1][j].getClass() == Rook.class  ) )
				return true;			
			else if ( cb[i+1][j].getClass() != NoPiece.class   )
				break;
			i++;
		}

		return false;	//Hiçbiri þah çekmiyorsa false döndürüyor.
	}
}
