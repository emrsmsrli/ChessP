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
	 * 0 ---> taþýma
	 * 1 ---> yeme
	 * -1 ---> not movable
	 */
	public static int canKingMove(int fx, int fy, int lx, int ly, ChessPiece cb[][]){
		piece=cb[fx][fy]; // Kolaylýk olsun diye gereksiz.
		if(ChessBoard.whoseTurn()!=piece.isWhite()){ // Baþkasýnýn taþýný oynatamasýn diye.
			return -1;
		}
		else{
			if(cb[lx][ly].getClass()==NoPiece.class &&
					((lx-fx==1 && ly==fy) || // aþþaðý..
							(lx==fx && ly-fy==1) || // saða..
							(fx-lx==1 && ly==fy) || // yukarý..
							(lx==fx && fy-ly==1) || // sola..
							(lx==fx+1 && ly==fy+1) || // sað alt..
							(lx==fx-1 && ly==fy-1) || // sol üst..
							(lx==fx-1 && ly==fy+1) || // sað üst..
							(lx==fx+1 && ly==fy-1))){ // sol alt..

				// yeni obje yaratmamak için pieceler taþýnýyor. bütün methodlar için ayný mantýk.
				return 0;
			}
			else if(cb[lx][ly].getClass()!=NoPiece.class  && 
					((lx-fx==1 && ly==fy) || // aþþaðý..
							(lx==fx && ly-fy==1) || // saða..
							(fx-lx==1 && ly==fy) || // yukarý..
							(lx==fx && fy-ly==1) || // sola..
							(lx==fx+1 && ly==fy+1) || // sað alt..
							(lx==fx-1 && ly==fy-1) || // sol üst..
							(lx==fx-1 && ly==fy+1) || // sað üst..
							(lx==fx+1 && ly==fy-1)) && // sol alt..
					cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){ // kendi taþýmý diye control.

				//taþ yemem iþlemi. yiyeceði taþýn üzerine overwrite edip eski yerini nopiece yapýyor. bütün methodlar için ayný mantýk.
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
				if(lx==fx && ly==fy+i){ // rook, bishop ve queen deki for,  if, else if, else mantýðý ayný.
					for(int k=1; k<=i; k++){ // tek tek oynattýðý hizadaki pieceleri kontrol etmesi için
						if(cb[fx][fy+k].getClass()==NoPiece.class){count++;}
						else break;
					}
					if(count==i){ // eðer önünde taþ yoksa sorunsuz taþýyor
						return 0;
					}
					else if(count==(i-1) && cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){ // eðer tam oynattýðý yerde taþ varsa ve onun renginde deðilse yeme iþlemi gerçekleþiyor.
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
			if(fx==6 && fx-lx<=2 && fx-lx>0){// ilk pozisyonda mý diye control ediyor. öyleyse 2 move izin veriyor.
				if(fx-lx==1 && (fy-ly==1 || ly-fy==1) && cb[lx][ly].getClass()!=NoPiece.class){
					return 1;
				}
				else if(fy==ly &&
						((fx-lx==1 && cb[lx][ly].getClass()==NoPiece.class) || 
								(fx-lx==2 && cb[lx][ly].getClass()==NoPiece.class && cb[lx+1][ly].getClass()==NoPiece.class))){
					
					return 0;
				}
				else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else if(fx-lx==1){//ilk pozisyonda deðilse en fazla 1 ileri gidebilir.
				if((fy-ly==1 || ly-fy==1) && 
						cb[lx][ly].getClass()!=NoPiece.class && 
						cb[lx][ly].isWhite()!=cb[fx][fy].isWhite()){

					return 1;
				}
				else if(fy==ly && cb[lx][ly].getClass()==NoPiece.class){
					return 0;
				}
				else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
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
			if(fx==1 && lx-fx<=2 && lx-fx>0){// ilk pozisyonda mý diye control ediyor. öyleyse 2 move izin veriyor.
				if(lx-fx==1 && (fy-ly==1 || ly-fy==1) && cb[lx][ly].getClass()!=NoPiece.class){
					return 1;
				}
				else if(fy==ly &&
						((lx-fx==1 && cb[lx][ly].getClass()==NoPiece.class) || 
								(lx-fx==2 && cb[lx][ly].getClass()==NoPiece.class && cb[lx+1][ly].getClass()==NoPiece.class))){

					return 0;
				}
				else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else if(lx-fx==1){//ilk pozisyonda deðilse en fazla 1 ileri gidebilir.
				if((ly-fy==1 || fy-ly==1) && 
						cb[lx][ly].getClass()!=NoPiece.class && 
						cb[fx][fy].isWhite()!=cb[lx][ly].isWhite()){

					return 1;
				}
				else if(ly==fy && cb[lx][ly].getClass()==NoPiece.class){
					return 0;
				}
				else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
					return -1;
				}
			}
			else{    //Diðer herhangi bir þeyde hata yazdýracak ve haraket ettirmeyecek.
				return -1;
			}
		}
	}
}