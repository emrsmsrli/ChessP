package parser;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/* KONTROL KODLARI */

/* 
 * OLAY KODLARI:
 * 
 * -parse() 	returns
 * 10: castling
 * 20: save
 * 40: resign
 * 99: error codes
 * 
 * -parseMenu() returns
 * 30: load
 * 100: quit
 * 101: play
 * 102: avail commands
 */

/* PROMOTION KODLARI
 * 1: rook
 * 2: knight
 * 3: bishop
 * 4: queen
 */

/* CASTLING KODLARI
 * 1: left
 * 2: right
 */

public class Parser {
	
	//fieldlar. gereksiz.
	private Scanner s;
	private String str;
	private ArrayList<ArrayList<Integer>> coor = new ArrayList<ArrayList<Integer>>();
	private static Parser p;
	
	//constructor iki boyutlu array'i hazýrlýyor 
	//ve yeni scanner oluþturuyor.
	private Parser(){
		coor.add(new ArrayList<Integer>());
		coor.add(new ArrayList<Integer>());
		s = new Scanner(System.in);
	}
	
	//promotion komutunu parse eder
	//bir piyonun tahtanýn diðer ucuna gitmesiyle tetikle
	public ArrayList<ArrayList<Integer>> parseProm() {
		resetParser(coor);
		System.out.print("promotion command: ");
		try {
			str = s.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("EOF");
			System.exit(-1);
		}
		
		if(str.substring(0, 4).equals("prom") && 
				Character.isWhitespace(str.charAt(4))) {
			switch(str.substring(5, str.length()).toLowerCase()){
			case "rook":
				coor.get(0).add(1);
				break;
			case "knight":
				coor.get(0).add(2);
				break;
			case "bishop":
				coor.get(0).add(3);
				break;
			case "queen":
				coor.get(0).add(4);
				break;
			default:
				System.err.println("invalid input - \"prom @ChessPiece\"");
				resetParser(coor);
				coor.get(0).add(99);
			}
		} else {
			System.err.println("invalid input - \"prom @ChessPiece\"");
			resetParser(coor);
			coor.get(0).add(99);
		}
		
		return coor;
	}
	
	//parse the initial menu
	public ArrayList<ArrayList<Integer>> parseMenu() {
		resetParser(coor);
		System.out.print("command: ");
		try {
			str = s.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("EOF");
			System.exit(-1);
		}
		
		//parse initial menu options
		if(str.length() == 1 && Character.isDigit(str.charAt(0))){
			int i = Integer.parseInt(str);
			switch(i){
			case 1:
				coor.get(0).add(101);
				break;
			case 2:
				coor.get(0).add(102);
				break;
			case 3:
				coor.get(0).add(30);
				return coor;
			case 4:
				coor.get(0).add(100);
				break;
			default:
				System.err.println("invalid input - select 1 to 3");
				resetParser(coor);
				coor.get(0).add(99);
				break;
			}
			return coor;
		}
		System.err.println("invalid input - enter a number of length 1");
		resetParser(coor);
		coor.get(0).add(99);
		return coor;
	}
	
	//giriþleri parse eden method.
	//iki boyutlu arrayde gidiþ ve dönüþ koordinatlarý dönderir
	public ArrayList<ArrayList<Integer>> parse(){
		resetParser(coor);
		System.out.print("command: ");
		try {
			str = s.nextLine();
		} catch (NoSuchElementException e) {
			System.err.println("EOF");
			System.exit(-1);
		}
		
		//giriþin tam olarak NUMARA-HARF-BOÞLUK-NUMARA-HARF þeklinde
		//olmasý için uzunluk ve tür kontrolleri
		if(str.length() < 4){
			System.err.println("invalid input - move string length min 5");
			resetParser(coor);
			coor.get(0).add(99);
			return coor;
		}
		
		//pes etme komutunu parse eder
		//olay kodu 30.
		if(str.equalsIgnoreCase("resign")){
			coor.get(0).add(40);
			return coor;
		}
		
		else if(str.equalsIgnoreCase("save")){
			coor.get(0).add(20);
			return coor;
		}
		
		//rok komutunu parse eder
		//olay kodu 10.
		//TODO: short O-O, long O-O-O
		else if(str.substring(0, 4).equalsIgnoreCase("cast") && 
				Character.isWhitespace(str.charAt(4)) 	&& 
				Character.isAlphabetic(str.charAt(5))	){
			
			coor.get(0).add(10);
			switch(str.charAt(5)){
			case 'L':
			case 'l':
				coor.get(1).add(1);
				break;
			case 'R':
			case 'r':
				coor.get(1).add(2);
				break;
			default:
				System.err.println("invalid input - \"cast (L)\" or \"cast (R)\"");
				resetParser(coor);
				coor.get(0).add(99);
			}
			return coor;
		}
		
		else if(Character.isAlphabetic(str.charAt(0))      	  && 
				Character.isDigit(str.charAt(1)) && 
				Character.isWhitespace(str.charAt(2)) && 
				Character.isAlphabetic(str.charAt(3)) 	  && 
				Character.isDigit(str.charAt(4)) ){
			
			//switch ifadeleri harflerin iki boyutlu array'de karþýlýk gelen 
			//rakamlara çevrilmesi için yazýldý. SOURCE için

			//(8-..) çünkü oyun tahtasý sol üstte 8 ve A'dan baþlýyor
			coor.get(0).add(8 - Integer.parseInt(str.substring(1, 2)));
			coor.get(1).add(8 - Integer.parseInt(str.substring(4, 5)));
			
			/*FROM*/
			switch(str.charAt(0)){
			case 'A':
			case 'a':
				coor.get(0).add(0);;
				break;
			case 'B':
			case 'b':
				coor.get(0).add(1);;
				break;
			case 'C':
			case 'c':
				coor.get(0).add(2);
				break;
			case 'D':
			case 'd':
				coor.get(0).add(3);
				break;
			case 'E':
			case 'e':
				coor.get(0).add(4);
				break;
			case 'F':
			case 'f':
				coor.get(0).add(5);
				break;
			case 'G':
			case 'g':
				coor.get(0).add(6);
				break;
			case 'H':
			case 'h':
				coor.get(0).add(7);
				break;
			default:
				System.err.println("invalid input in SOURCE - valid chars: A to H");
				resetParser(coor);
				coor.get(0).add(99);
			}
			
	/***********************************************************/
			
			//ayný switch ifadesi DESTINATION için
			
			/*TO*/
			switch(str.charAt(3)){
			case 'A':
			case 'a':
				coor.get(1).add(0);
				break;
			case 'B':
			case 'b':
				coor.get(1).add(1);
				break;
			case 'C':
			case 'c':
				coor.get(1).add(2);
				break;
			case 'D':
			case 'd':
				coor.get(1).add(3);
				break;
			case 'E':
			case 'e':
				coor.get(1).add(4);
				break;
			case 'F':
			case 'f':
				coor.get(1).add(5);
				break;
			case 'G':
			case 'g':
				coor.get(1).add(6);
				break;
			case 'H':
			case 'h':
				coor.get(1).add(7);
				break;
			default:
				System.err.println("invalid input in DESTINATION - valid chars: A to H");
				resetParser(coor);
				coor.get(0).add(99);
				break;
			}
			
			if( (coor.get(0).get(0) < 0 || coor.get(0).get(0) > 7) || 
				(coor.get(1).get(0) < 0 || coor.get(1).get(0) > 7) ){
				System.err.println("invalid input - numbers cannot exceed 8 or go below 0");
				resetParser(coor);
				coor.get(0).add(99);
			}
			
			else if(coor.get(0).size() == 2 && 
					coor.get(0).get(0) == coor.get(1).get(0) && 
					coor.get(0).get(1) == coor.get(1).get(1) ){
				System.err.println("invalid input - you cannot enter same coordinates");
				resetParser(coor);
				coor.get(0).add(99);
			}
			
		} else {
			System.err.println("invalid input - you can move a piece (C#_C#),\nmake a promotion (prom @ChessPiece),\nor castling cast (L) or (cast (R)),\nor (resign)");
			resetParser(coor);
			coor.get(0).add(99);
		}

		return coor;
	}

	//tekrar parse yapýlmasý için array'i temizleyen method
	public void resetParser(ArrayList<ArrayList<Integer>> coor){
		coor.get(0).clear();
		coor.get(1).clear();
	}
	
	//sadece bi tane parser objesi oluþturmak için 
	//getInstance() methodu 
	public static Parser getInstance(){
		if(p == null)
			p = new Parser();
		return p;
	}
	
	public void close(){
		s.close();
	}
	
} //Parser class'ýnýn bitiþi.
