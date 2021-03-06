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
		// This checks that the program is being passed the valid number of inputs
		if(args.length!=2){
			System.out.println("Usage: java Game (words file) (number of guesses)");
			System.exit(1);
		}
		
		// Checks if the number of guesses is a valid integer
		for(int i=0;i<args[1].length();i++){
			char loopChar = args[1].charAt(i);

			if(!Character.isDigit(loopChar)){
				System.out.println("Guesses must be a valid integer");
				System.exit(2);
			}
		}
		
		// Check that the above integer is within a valid range specified below
		if(Integer.parseInt(args[1]) > 25 || Integer.parseInt(args[1]) < 3){
			System.out.println("Guesses must be between 3 and 25");
			System.exit(3);
		}

	}
	
	public static String cleanWord(String word){
		// This method returns a String stripped of all non-aphas
		String cleanedWord = "";
		
		for(int i=0;i<word.length();i++){
			if(Character.isAlphabetic(word.charAt(i))){
				cleanedWord += Character.toUpperCase(word.charAt(i));
			}
		}
		return cleanedWord;
	}
	
	public static ArrayList<String> readFile(String fileName){
		// This method reads all words from a file and creates an arraylist from them.
		// It also calculates and appends the lettercount of the largest word to the list
		BufferedReader reader = null;
		ArrayList<String> wordList = new ArrayList<String>();
		int letterCount = 0;
		
		try{
			reader = new BufferedReader(new FileReader(fileName));
		}catch(FileNotFoundException fnfe){
			System.out.println("Error on opening file data");
			System.exit(4);
		}
		
		boolean done = false;
		String inputLine = null;
		
		// This loop reads from the source file and builds the arrayList until 
		// the whole file has been processed
		while(!done){
			// Read one line at a time
			try{
				inputLine = reader.readLine();
			}catch(IOException ioe){
				System.out.println("I/O error");
				System.exit(5);
			}
			
			// If the line read above is not null then this branch+loop
			// breaks the line down into words and the cleanWord method removes
			// non-alphanumeric characters before adding it to the arraylist
			if(inputLine != null){

				String[] tempArray = inputLine.split(" +");
				String loopWord;
				
				for(int i=0; i<tempArray.length;i++){
					loopWord = cleanWord(tempArray[i]);
					
					wordList.add(loopWord);
					
					// This branch simply check each word against the
					// previous to see if it's bigger and assigns the
					// biggest letter count to letterCount
					if(loopWord.length()>letterCount){
						letterCount = loopWord.length();
					}
				}

			}else{
				done = true;
			}
		}
		try{
			reader.close();
		}catch(IOException ioe){
			System.out.println("Error closing file '"+fileName+"'");
			System.exit(6);
		}
		
		// Append the highest letter count to the last arrayList position
		wordList.add(Integer.toString(letterCount));
		
		return wordList;
	}

	public static char readLine(){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input;
		String cleanedInput = "";
		
		try{
			// Assigns the string entered at the console to the variable "input"
			input = reader.readLine();

			// This branch/loop strips any spaces from the input string
			if(input.length()>0){
				for(int i=0;i<input.length();i++){
					if(input.charAt(i)!=' '){
						cleanedInput += input.charAt(i);
						System.out.print(cleanedInput);
					}
				}
			}
			
			// If there is only one letter entered then it is returned as a char to be used as a valid guess
			if(cleanedInput.length()==1){
				if(Character.isAlphabetic(cleanedInput.charAt(0))){
					return Character.toUpperCase(cleanedInput.charAt(0));
				}
			}
		}catch(IOException ioe){
			System.out.println(ioe);
			System.exit(7);
		}
		
		// If the above code fails then an exclamation point is returned for error handling
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
		// This method returns a random word that is not smaller than the number of guesses
		// the user has chosen. This is to avoid games that are impossible to win.
		ArrayList<String> wordList = readFile(args[0]);
		
		// The last position in the arraylist is the size of the biggest word
		int maxLength = Integer.parseInt(wordList.get(wordList.size()-1));
		char[] wordToGuess;
		int guessCount = Integer.valueOf(args[1]);
		
		if(guessCount<maxLength){
			System.out.print("Thinking of a word");
			do{
				wordToGuess = wordList.get(returnRand(wordList.size()-1)).toCharArray();
				System.out.print(".");
				
			}while(wordToGuess.length>guessCount);
		}else{
			wordToGuess = wordList.get(returnRand(wordList.size()-1)).toCharArray();
		}

		
		
//		char[] wordToGuess = readFile( args[0]) .get(returnRand(readFile(args[0]).size()-1)) .toCharArray();

		
		return wordToGuess;
	}
	
	public static void main(String[] args) {
		// Call the method that deals with the args array exceptions
		dealWithArgs(args);
		
		// Declare and initialise the randomly selected word and it's length
		char[] wordToGuess = wordToGuess(args); 
		int wordLength = wordToGuess.length;;
		
		char[] displayChars = new char[wordLength];
		char guess = ' ';
		int guessCount;
		boolean gameOver, winMet, letterBad; 
		gameOver = winMet = letterBad = false;
		
		String spacer = multiString(String.valueOf((char)205), 28);
		String winLose;

		int count = 0;
		int success = 0;
		
			
		for(int i=0;i<wordLength;i++){displayChars[i]='*';}
		
		do{
			// This branch defines the win condition
			if(success>=wordLength){
				winMet = true;
				gameOver = true;
			}
			
			// Below is the formatted output 
			output.printf(multiString("\n", 50));

			// Top of Box
			output.printf(String.valueOf((char)201) + spacer + String.valueOf((char)187 )+"\n");
			
			output.printf("%s%28s%s\n", (String.valueOf((char)186)),"",(String.valueOf((char)186)));
			
			
			output.printf("%s%18s%11s\n", String.valueOf((char)186), "HANGMAN!", String.valueOf((char)186));
			output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
			output.printf("%s%s%s\n",String.valueOf((char)204),spacer,String.valueOf((char)185));

			output.printf("%s%s", String.valueOf((char)186), multiString(" ", (28-wordLength)/2));
			
			for(int j=0;j<wordLength;j++){
				output.printf("%s", displayChars[j]);
			}

			if(wordLength%2!=0){
				output.printf(" ");
			}

			output.printf("%s%s\n", multiString(" ", (28-wordLength)/2), String.valueOf((char)186));
			
//			output.printf(spacer + "\n");
			
			output.printf("%s%s%s\n", String.valueOf((char)204), spacer, String.valueOf((char)185));
			guessCount = Integer.parseInt(args[1])-count;
			if(guessCount>1){
				output.printf("%s%2s", String.valueOf((char)186), " ");
					
				output.printf("You have %2d guesses left", guessCount);
				output.printf("%2s%s\n", " ", String.valueOf((char)186));

				
			}else{
				output.printf("%s%2s", String.valueOf((char)186), "  ");
				output.printf("You have %2d guess left", guessCount);
				output.printf("%4s%s\n", " ", String.valueOf((char)186));

			}

			if(letterBad){
				if(guess=='!'){
					//output.printf("%s%7s", String.valueOf((char)186), " ");
					//output.printf("Invalid Guess!");
					//output.printf("%6s%s\n", " ", String.valueOf((char)186));
					output.printf("%s%s%s\n",String.valueOf((char)204),spacer,String.valueOf((char)185));
					output.printf("%s%7s", String.valueOf((char)186), " ");
					output.printf("Invalid Guess!");
					output.printf("%7s%s\n", " ", String.valueOf((char)186));
				}
			}else{
				output.printf("%s%s%s\n",String.valueOf((char)204),spacer,String.valueOf((char)185));
				output.printf("%s%7s", String.valueOf((char)186), " ");
				output.printf("Guess a letter");
				output.printf("%7s%s\n", " ", String.valueOf((char)186));
			}
			
			
			
			// Box Bottom
			output.printf("%s%s%s\n\n\n",String.valueOf((char)200),spacer,String.valueOf((char)188));
			output.printf(":");


			if(success!=wordLength){
				guess = readLine();
			}else{
				guess=' ';
			}
			
			// '!' is used as a special escape character to tell if the guess is a valid one, returned from readLine()
			
			if(guess!='!'){
				count++;
				boolean guessRight = false;
				
				for(int j=0;j<wordLength;j++){
					if(guess==wordToGuess[j] && displayChars[j] == '*' ){
						displayChars[j] = wordToGuess[j];
						success++;
						guessRight = true;
					}
				}
				count = guessRight?count-=1:count;
				letterBad = false;
			}else{
				letterBad = true;
			}
			
			if(count==Integer.parseInt(args[1])){
				gameOver = true;
			}

			
		}while(!gameOver);
		
		
		// If winMet then well done else hard luck
		winLose = winMet?"Well Done":"Hard Luck";

					// Clear console
					output.printf(multiString("\n", 50));
					
					// Top of Box - Start
					output.printf(String.valueOf((char)201) + spacer + String.valueOf((char)187 )+"\n");
					// Top of Box - End

					output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
					output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
			
					output.printf("%s%7sThe word was:%8s%s\n", String.valueOf((char)186), " ", " ", String.valueOf((char)186) );
			
					output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
					output.printf("%s%s", String.valueOf((char)186), multiString(" ", (28-wordLength)/2));

		// This outputs the 
		for(int j=0;j<wordLength;j++){
			output.printf("%s", wordToGuess[j]);
		}
		if(wordLength%2!=0){
			output.printf(" ");
		}
		
		
					output.printf("%s%s\n", multiString(" ", (28-wordLength)/2), String.valueOf((char)186));
			
					output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
					
					output.printf("%s%10s%s%10s\n", String.valueOf((char)186), " ", winLose, String.valueOf((char)186));
					output.printf("%s%28s%s\n", String.valueOf((char)186), "", String.valueOf((char)186));
					// Bottom of Box - Start
					output.printf("%s%s%s\n\n",String.valueOf((char)200),spacer,String.valueOf((char)188));
					// Bottom of Box - End

	
	}
}
