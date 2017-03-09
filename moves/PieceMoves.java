package moves;

import board.ChessBoard;
import controls.Controls;
import pieces.ChessPiece;
import pieces.NoPiece;

public class PieceMoves{
	private static ChessPiece temp;
	private static ChessPiece piece;
	private static ChessPiece eatTemp; 

	public static void move(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		temp = cb[fx][fy]; 
		cb[fx][fy]=cb[lx][ly];
		cb[lx][ly]= temp ;
		cb[fx][fy].setXY(fx,fy);
		cb[lx][ly].setXY(lx, ly);
	}

	public static void eat(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		eatTemp = cb[lx][ly];
		cb[lx][ly] = cb[fx][fy];
		cb[fx][fy] = new NoPiece(fx,fy);
		cb[lx][ly].setXY(lx, ly);
	}
	
	public static void revertEat(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		cb[fx][fy] = cb[lx][ly];
		cb[lx][ly] = eatTemp;
		cb[fx][fy].setXY(fx, fy);
		cb[lx][ly].setXY(lx, ly);
	}

	public static boolean moveControl(int fx, int fy, int lx, int ly, ChessPiece [][] cb){
		
		ChessPiece t;

		//first assume the piece is moved
		t = cb[lx][ly];
		cb[lx][ly] = cb[fx][fy];
		cb[fx][fy] = new NoPiece(fx,fy);
		cb[lx][ly].setXY(lx, ly);

		//check if still in the same status
		if(ChessBoard.whoseTurn()){
			if(!Controls.isCheck(ChessBoard.getWhiteK(), cb)){
				cb[fx][fy] = cb[lx][ly];
				cb[lx][ly] = t;
				cb[fx][fy].setXY(fx, fy);
				cb[lx][ly].setXY(lx, ly);
				return true;
			}

			//remember to move back to original state
			cb[fx][fy] = cb[lx][ly];
			cb[lx][ly] = t;
			cb[fx][fy].setXY(fx, fy);
			cb[lx][ly].setXY(lx, ly);
		} else {
			if(!Controls.isCheck(ChessBoard.getBlackK(), cb)){
				cb[fx][fy] = cb[lx][ly];
				cb[lx][ly] = t;
				cb[fx][fy].setXY(fx, fy);
				cb[lx][ly].setXY(lx, ly);
				return true;
			}

			cb[fx][fy] = cb[lx][ly];
			cb[lx][ly] = t;
			cb[fx][fy].setXY(fx, fy);
			cb[lx][ly].setXY(lx, ly);
		}
		
		return false;
	}
	
	/*
	 * 0 ---> ta��ma
	 * 1 ---> yeme
	 * -1 ---> not movable
	 */
	public static int canKingMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy]; // Kolayl�k olsun diye gereksiz.
		if(ChessBoard.whoseTurn()!=piece.isWhite()){ // Ba�kas�n�n ta��n� oynatamas�n diye.
			return -1;
		}
		else{
			if(cb[lx][ly].getClass()==NoPiece.class &&
					((lx-fx==1 && ly==fy) || // a��a��..
							(lx==fx && ly-fy==1) || // sa�a..
							(fx-lx==1 && ly==fy) || // yukar�..
							(lx==fx && fy-ly==1) || // sola..
							(lx==fx+1 && ly==fy+1) || // sa� alt..
							(lx==fx-1 && ly==fy-1) || // sol �st..
							(lx==fx-1 && ly==fy+1) || // sa� �st..
							(lx==fx+1 && ly==fy-1))){ // sol alt..

				// yeni obje yaratmamak i�in pieceler ta��n�yor. b�t�n methodlar i�in ayn� mant�k.
				return 0;
			}
			else if(cb[lx][ly].getClass()!=NoPiece.class  && 
					((lx-fx==1 && ly==fy) || // a��a��..
							(lx==fx && ly-fy==1) || // sa�a..
							(fx-lx==1 && ly==fy) || // yukar�..
							(lx==fx && fy-ly==1) || // sola..
							(lx==fx+1 && ly==fy+1) || // sa� alt..
							(lx==fx-1 && ly==fy-1) || // sol �st..
							(lx==fx-1 && ly==fy+1) || // sa� �st..
							(lx==fx+1 && ly==fy-1)) && // sol alt..
					cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){ // kendi ta��m� diye control.

				//ta� yemem i�lemi. yiyece�i ta��n �zerine overwrite edip eski yerini nopiece yap�yor. b�t�n methodlar i�in ayn� mant�k.
				return 1;
			}
			else{
				return -1;
			}
		}
	}

	public static int canQueenMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			int count=0;
			int i=0;
			if(lx==fx){
				i=Math.abs(ly-fy);
				if(lx==fx && ly==fy+i){ // rook, bishop ve queen deki for,  if, else if, else mant��� ayn�.
					for(int k=1; k<=i; k++){ // tek tek oynatt��� hizadaki pieceleri kontrol etmesi i�in
						if(cb[fx][fy+k].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){ // e�er �n�nde ta� yoksa sorunsuz ta��yor
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){ // e�er tam oynatt��� yerde ta� varsa ve onun renginde de�ilse yeme i�lemi ger�ekle�iyor.
						return 1;
					}
					else{
						return -1;
					}
				}
				else if(lx==fx && ly==fy-i){
					for(int k=1; k<=i; k++){
						if(cb[fx][fy-k].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else{
					return -1;
				}
			}
			else if(fy==ly){
				i=Math.abs(lx-fx);
				if(lx==fx+i && ly==fy){
					for(int k=1; k<=i; k++){
						if(cb[fx+k][fy].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else if(lx==fx-i && ly==fy){
					for(int k=1; k<=i; k++){
						if(cb[fx-k][fy].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else{
					return -1;
				}
			}
			i=Math.abs(lx-fx);
			if(lx==fx+i && ly==fy+i){
				for(int k=1; k<=i; k++){	
					if(cb[fx+k][fy+k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx-i && ly==fy+i){
				for(int k=1; k<=i; k++){	
					if(cb[fx-k][fy+k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx+i && ly==fy-i){
				for(int k=1; k<=i; k++){	
					if(cb[fx+k][fy-k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx-i && ly==fy-i){
				i=Math.abs(lx-fx);
				for(int k=1; k<=i; k++){	
					if(cb[fx-k][fy-k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}	
			}
			else{
				return -1;
			}
		}
	}

	public static int canRookMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			int count=0;
			int i=0;
			if(lx==fx){
				i=Math.abs(ly-fy);
				if(lx==fx && ly==fy+i){
					for(int k=1; k<=i; k++){
						if(cb[fx][fy+k].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else if(lx==fx && ly==fy-i){
					for(int k=1; k<=i; k++){
						if(cb[fx][fy-k].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else{
					return -1;
				}
			}
			else if(fy==ly){
				i=Math.abs(lx-fx);
				if(lx==fx+i && ly==fy){
					for(int k=1; k<=i; k++){
						if(cb[fx+k][fy].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else if(lx==fx-i && ly==fy){
					for(int k=1; k<=i; k++){
						if(cb[fx-k][fy].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
						return 1;
					}
					else{
						return -1;
					}
				}
				else{
					return -1;
				}
			}
			else{
				return -1;
			}
		}
	}

	public static int canKnightMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			if(cb[lx][ly].getClass()==NoPiece.class && 
					((lx==fx+1 && ly==fy+2) || 
							(lx==fx+1 && ly==fy-2) || 
							(lx==fx-1 && ly==fy-2) || 
							(lx==fx-1 && ly==fy+2) ||
							(lx==fx+2 && ly==fy+1) ||
							(lx==fx-2 && ly==fy+1) ||
							(lx==fx+2 && ly==fy-1) ||
							(lx==fx-2 && ly==fy-1))){

				return 0;
			}
			else if(cb[lx][ly].getClass()!=NoPiece.class && 
					((lx==fx+1 && ly==fy+2) || 
							(lx==fx+1 && ly==fy-2) || 
							(lx==fx-1 && ly==fy-2) || 
							(lx==fx-1 && ly==fy+2) ||
							(lx==fx+2 && ly==fy+1) ||
							(lx==fx-2 && ly==fy+1) ||
							(lx==fx+2 && ly==fy-1) ||
							(lx==fx-2 && ly==fy-1)) && 
					cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){

				return 1;
			}
			else{
				return -1;
			}
		}
	}

	public static int canBishopMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			int count=0;
			int i=Math.abs(lx-fx);
			if(lx==fx+i && ly==fy+i){
				for(int k=1; k<=i; k++){	
					if(cb[fx+k][fy+k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx-i && ly==fy+i){
				for(int k=1; k<=i; k++){	
					if(cb[fx-k][fy+k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx+i && ly==fy-i){
				for(int k=1; k<=i; k++){	
					if(cb[fx+k][fy-k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}
			}
			else if(lx==fx-i && ly==fy-i){
				for(int k=1; k<=i; k++){	
					if(cb[fx-k][fy-k].getClass()==NoPiece.class){count++;}
					else break;
				}
				if(count==i){
					return 0;
				}
				else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){
					return 1;
				}
				else{
					return -1;
				}	
			}
			else{
				return -1;
			}
		}
	}

	public static int canPawnMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			if(fx==6 && fx-lx<=2 && fx-lx>0){// ilk pozisyonda m� diye control ediyor. �yleyse 2 move izin veriyor.
				if(fx-lx==1 && (fy-ly==1 || ly-fy==1) && cb[lx][ly].getClass()!=NoPiece.class){
					return 1;
				}
				else if(fy==ly &&
						((fx-lx==1 && cb[lx][ly].getClass()==NoPiece.class) || 
								(fx-lx==2 && cb[lx][ly].getClass()==NoPiece.class && cb[lx+1][ly].getClass()==NoPiece.class))){
					
					return 0;
				}
				else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else if(fx-lx==1){//ilk pozisyonda de�ilse en fazla 1 ileri gidebilir.
				if((fy-ly==1 || ly-fy==1) && 
						cb[lx][ly].getClass()!=NoPiece.class && 
						cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){

					return 1;
				}
				else if(fy==ly && cb[lx][ly].getClass()==NoPiece.class){
					return 0;
				}
				else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
				return -1;
			}
		}
	}
	
	public static int canPawnReverseMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy];
		if(ChessBoard.whoseTurn()!=piece.isWhite()){
			return -1;
		}
		else{
			if(fx==1 && lx-fx<=2 && lx-fx>0){// ilk pozisyonda m� diye control ediyor. �yleyse 2 move izin veriyor.
				if(lx-fx==1 && (fy-ly==1 || ly-fy==1) && cb[lx][ly].getClass()!=NoPiece.class){
					return 1;
				}
				else if(fy==ly &&
						((lx-fx==1 && cb[lx][ly].getClass()==NoPiece.class) || 
								(lx-fx==2 && cb[lx][ly].getClass()==NoPiece.class && cb[lx+1][ly].getClass()==NoPiece.class))){

					return 0;
				}
				else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else if(lx-fx==1){//ilk pozisyonda de�ilse en fazla 1 ileri gidebilir.
				if((ly-fy==1 || fy-ly==1) && 
						cb[lx][ly].getClass()!=NoPiece.class && 
						cb[fx][fy].isWhite()!=cb[lx][ly].isWhite()){

					return 1;
				}
				else if(ly==fy && cb[lx][ly].getClass()==NoPiece.class){
					return 0;
				}
				else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else{    //Di�er herhangi bir �eyde hata yazd�racak ve haraket ettirmeyecek.
				return -1;
			}
		}
	}
}