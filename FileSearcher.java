
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/* Grader Please Ignore
 * pseudocode for coding preparation
 */
//read through files
//see words in files --> put word into the tree
//all words should be alphabetize (BST)
//a Node contains Word object and its pointer to its left and right child
//the node contains the word and its fileList


//run program from command line "directory"
//make directory File Object and use listFiles methods to get text files 1-5
//scanner to read line in textfile .nextLine() method to return current line
//split line by spaces and put in list of words
//create for loop to iterate through every letter in each word
//create new string to get rid of punctuation using isLetter method.
//make new Word object using Word class, set the word in Word class, add current filename to fileList
//put word into tree if word is not there
//if word already in tree, e.g. in file2, "the" already in file1, updating the word's fileList in the tree


//test run bug: empty tree
//solution: make tree a global variable ; should not make it a local one because local tree is being constructed not global
//commented out local one, run command line

//didn't print out file bug for "s" input
//before debugging, search word is blue which has an empty file list, because it is printing out the input word node that does not have filelist
//after, we use find method to find the real node in the tree, and we can ignore the node searchWord (because it does not have anything in it)
//we print the node that we find in the tree



public class FileSearcher {
	static String wordToFind;
	static Scanner scan = new Scanner (System.in);
	static String thisWord;
	static String theWord;
	static BinarySearchTree<Word> wordTree = new BinarySearchTree <Word>();
	
	
	//get user response method
	public static String getResponse(){
		System.out.println("Please enter a command (a,s, or q) >>");
		String response = scan.nextLine();
		return response;
	}
	
	
	//get Word method
	public static String getWord(){
		System.out.println("Word to find >>");
		wordToFind= scan.nextLine();
		return wordToFind;
	}
	

	//getTree method, take in directory and construct wordTree
	public static void getTree(File directory) throws FileNotFoundException{ //throws Exception necesssary?
		
		//Grader please ignore: not deleting for revision purposes
		//BinarySearchTree<Word> wordTree = new BinarySearchTree <Word>();	//create new tree global variable 
		
		File [] fileArray = directory.listFiles(); // make a File array File [] full of File Object
		
		//use scanner to access each file object e.g. file1.txt and file2.txt to access elements in the file
		//check whether element (word) is in the tree structure or not, if not populate the tree (insert (word))
		//when inserting word, keep track of what file it comes from 
		
		String filename;
		for (int i=0; i<fileArray.length;i++){
			
			if (!fileArray[i].isHidden()){
				filename = fileArray[i].getName();					//get name of file not file object
				Scanner reader = new Scanner(fileArray[i]); 		//Scanner constructor (File source)
				String[] words = reader.nextLine().split(" ");		//read text and split into array of words
				
				for (int j=0; j<words.length;j++){					//for loop check array of words
					String thisWord = words[j];
					String theWord ="";
					
					for (int z=0; z<thisWord.length();z++){
						
						if (Character.isLetter(thisWord.charAt(z))==true){
							theWord= theWord + thisWord.charAt(z);	//construct individual words without punctuation
						}
					}		
					
					Word findWord = new Word(theWord);		//create new Word object now?
						
				
					//check whether theWord element is in the tree already
					if (!wordTree.contains(findWord)){				//theWord not in tree
						
						findWord.setFileList(filename);
						wordTree.insert(findWord);
						
					}else{
						Word w = wordTree.find(findWord);
						w.setFileList(filename);
	
					}	
				}
			}
		}
	}
	
	
	//find word in Tree and print out fileList
	public static void wordToFind(String wordToFind){
		Word searchWord = new Word(wordToFind);
		Word nodeInTree = wordTree.find(searchWord);
		
		if (nodeInTree == null){
			System.out.println(wordToFind+ " is not found.");
		}else{
			System.out.println(nodeInTree);
		}
	}
	
	

	//main method
	public static void main(String[] args) throws IOException{
		File f= new File (args[0]); //get directory from command line, and make args[0] a File object: File (directory)
		getTree(f);					//construct tree using getTree method and directory as parameter
		
		boolean run = true;
		
		while (run == true){
			String response = getResponse();
			if (response.equals("a")){
				wordTree.printTree();
			}else if(response.equals("s")){
				wordToFind = getWord();
				wordToFind(wordToFind);
			}else{
				System.out.println("Goodbye!");
				break;
			}
			
		}

	}

}
