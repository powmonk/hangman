import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class Game {
	private static PrintStream output = new PrintStream(System.out);

	public static int returnRand(int range){
		int random = (int)(Math.random()*range);
		return random;
	}
	
	public static void dealWithArgs(String[] args){
		if(args.length!=2){
			System.out.println("Usage: java Game (words file) (number of guesses)");
			System.exit(1);
		}
		
		if(Integer.parseInt(args[1]) > 25 || Integer.parseInt(args[1]) < 3){
			System.out.println("Guesses must be between 3 and 25");
			System.exit(6);
		}
		
		for(int i=0;i<args[1].length();i++){
			if(!Character.isDigit(args[1].charAt(i))){
				System.out.println("Please enter number of guesses as an integer");
				System.exit(2);
			}
		}
	}
	
	public static String cleanWord(String word){
		String cleanedWord = "";
		
		for(int i=0;i<word.length();i++){
			if(Character.isAlphabetic(word.charAt(i))){
				cleanedWord += Character.toUpperCase(word.charAt(i));
			}
		}
		return cleanedWord;
	}
	
	public static ArrayList<String> readFile(String fileName){
		BufferedReader reader = null;
		ArrayList<String> wordList = new ArrayList<String>();
		int letterCount = 0;
		
		try{
			reader = new BufferedReader(new FileReader(fileName));
		}catch(FileNotFoundException fnfe){
			System.out.println("Error on opening file data");
			System.exit(3);
		}
		
		boolean done = false;
		String inputLine = null;
		
		while(!done){
			try{
				inputLine = reader.readLine();
			}catch(IOException ioe){
				System.out.println("I/O error");
				System.exit(3);
			}
			if(inputLine == null){
				done = true;
			}else{
				String[] tempArray = inputLine.split(" +");
				String loopWord;
				
				for(int i=0; i<tempArray.length;i++){
					loopWord = cleanWord(tempArray[i]);
					
					wordList.add(loopWord);

					if(loopWord.length()>letterCount){
						letterCount = loopWord.length();
					}
				}
			}
		}
		try{
			reader.close();
		}catch(IOException ioe){
			System.out.println("Error closing file '"+fileName+"'");
			System.exit(4);
		}
		
		wordList.add(Integer.toString(letterCount));
		
		return wordList;
	}

	public static char readLine(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input;
		
		try{
			input = reader.readLine();
			System.out.println(input.length());
			if(input.length()==1){
				if(Character.isAlphabetic(input.charAt(0))){
					return Character.toUpperCase(input.charAt(0));
				}
			}
		}catch(IOException ioe){
			System.out.println(ioe);
			System.exit(4);
		}
		return '!';
	}
	
	public static String multiString(String rtnStr, int rows){
		String returnString = "";
		for(int i=0;i<rows;i++){
			returnString += rtnStr;
		}
		return returnString;
	}
	
	public static char[] wordToGuess(String[] args){
		ArrayList<String> wordList = readFile(args[0]);
		
		int maxLength = Integer.parseInt(wordList.get(wordList.size()-1));
		char[] wordToGuess;
		int guessCount = Integer.valueOf(args[1]);
		
		if(guessCount<maxLength){
			System.out.print("Thinking of a word");
			do{
				wordToGuess = wordList.get(returnRand(readFile(args[0]).size()-1)).toCharArray();
				System.out.print(".");
				
			}while(wordToGuess.length>guessCount);
		}else{
			wordToGuess = wordList.get(returnRand(readFile(args[0]).size()-1)).toCharArray();
		}

		
		
//		char[] wordToGuess = readFile( args[0]) .get(returnRand(readFile(args[0]).size()-1)) .toCharArray();

		
		return wordToGuess;
	}
	
	public static void main(String[] args) {
		dealWithArgs(args);
		char[] wordToGuess; 
		int wordLength;
		
		wordToGuess = wordToGuess(args);
		wordLength = wordToGuess.length;

		
		char[] displayChars = new char[wordLength];
		char guess = ' ';
		int guessCount;
		boolean gameOver, winMet, letterBad; 
		gameOver = winMet = letterBad = false;

		int count = 0;
		int success = 0;
		
			
		for(int i=0;i<wordLength;i++){displayChars[i]='*';}
		
		do{
			if(success==wordLength){
				winMet = true;
				gameOver = true;
			}

			output.printf(multiString("\n", 50));		
			output.printf("\n");
			output.printf("%s\n\n", "HANGMAN! ");

			for(int j=0;j<wordLength;j++){
				output.printf("%s", displayChars[j]);
			}
			output.printf("\n\n");
			guessCount = Integer.parseInt(args[1])-count;
			if(guessCount>1){
				output.printf("%sYou have %d guesses left\n", "", guessCount);
				
			}else{
				output.printf("You have 1 guess left\n");
			}

			if(letterBad){
				if(guess=='!'){
					output.printf("%s(Guesses must be 1 (and only 1!) letter from the alphabet!)\n", "");
				}
			}else{
				output.printf("\n");
			}
			
			output.printf("%sGuess a letter","");
			output.printf(multiString("\n", 2));		
			output.printf(":");


			if(success!=wordLength){
				guess = readLine();
			}else{
				guess=' ';
			}

			if(guess!='!'){
				count++;
				
				for(int j=0;j<wordLength;j++){
					if(guess==wordToGuess[j]){
						displayChars[j] = wordToGuess[j];
						success++;
					}
				}
				letterBad = false;
			}else{
				letterBad = true;
			}
			
			if(count==Integer.parseInt(args[1])){
				gameOver = true;
			}

			
		}while(!gameOver);
		
		if(winMet){
			output.printf(multiString("\n", 50));
			output.printf("The word was: ");
			for(int j=0;j<wordLength;j++){
				output.printf("%s", wordToGuess[j]);
			}
			output.printf("\n\nWell Done");

		}else{
			output.printf(multiString("\n", 50));
			output.printf("The word was: ");
			for(int j=0;j<wordLength;j++){
				output.printf("%s", wordToGuess[j]);
			}
			output.printf("\n\nHard Luck\n");
		}
	}
}
