package CS2103;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * Option (a) is chosen as the method to write to file. After every operation 
 * except display will write the data into the file. 
 * Assumptions made:
 * (1) When user specified a fileName that already exist, the program will overwrite 
 * the file instead of update it.
 * (2) display and clear will still work as long as the user correct enter display or
 * clear as the first word. E.g display   123123 will work. 123123Display will not work.
 * 
 **/
public class TextBuddy {
	
	private static final String MESSAGE_NO_RESULTS_FOUND = "No results found.";
	private static final String SORT_MESSAGE = "Text Sorted!";
	private static final String NOTHING_TO_DISPLAY_MESSAGE = "%s is empty";
	private static final String ERROR_NULL_COMMANDTYPE = "Command type string cannot be null!";
	private static final String ERROR_DELETE = "Error indicating which line to delete.";
	private static final String ERROR_INVALID_COMMAND = "Error. Invalid command.";
	private static final String ERROR_UNRECOGNISED_COMMAND = "Unrecognised command.";
	private static final String ERROR_FILE_NAME_NOT_SPECIFIED = "Error. File name not specified.";
	private static final String ADD_MESSAGE = "added to %s: \"%s\"";
	private static final String DELETE_MESSAGE = "deleted from %s: \"%s\"";
	private static final String CLEAR_MESSAGE = "all content deleted from %s";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %s is ready for use";
	
	private ArrayList<String> m_addWords = new ArrayList<String>();
	private static Scanner sc = new Scanner(System.in);
	private File f = null;
	private String fileName = null;
	
	//Available Commands
	enum COMMAND_TYPE {
		ADD,DISPLAY,CLEAR,EXIT,DELETE,SORT,SEARCH,INVALID;
	};
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			throw new Error(ERROR_NULL_COMMANDTYPE);
		} else if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
		 	return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
		 	return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
		 	return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
		 	return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public static void main(String[] Args) throws IOException {	
		TextBuddy tb = new TextBuddy(Args);
		show(String.format(WELCOME_MESSAGE, tb.getFileName()));
		while(true) {
			System.out.print("command: ");
			String userCommand = sc.nextLine();
			String feedback = tb.executeCommand(userCommand);
			show(feedback);
		}
	}
	
	public String executeCommand(String userCommand) throws IOException {
		//Extract the first word that user types and determine the command
		COMMAND_TYPE commandType = determineCommandType(getFirstWord(userCommand));
		String userWords = removeFirstWord(userCommand);
		switch(commandType) {
			case ADD : 
				m_addWords.add(userWords);
				return doAdd();
			case DISPLAY : 
				doDisplay();
				break;
			case CLEAR : 
				return doClear();
			case DELETE :
				try {
					int num = Integer.parseInt(userWords);
					return doDelete(num);
				}catch (Exception e) {
					return ERROR_DELETE;
				}
			case EXIT :
				sc.close();
				System.exit(0);
				break;
			case INVALID :
				return ERROR_INVALID_COMMAND;
			default:
				return ERROR_UNRECOGNISED_COMMAND;
		}
		return "";	
	}

	
	//Main method for writing into file. Everytime doAdd is called, Everything in the 
	//arraylist of words are written and save into the file.
	public String doAdd() throws IOException{
		if(m_addWords.size() > 0) {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int j = 0; j < m_addWords.size(); j++) {
				String userWords = m_addWords.get(j);
				bw.write(userWords);
				bw.newLine();
			}

			bw.close();
			return String.format(ADD_MESSAGE,fileName, 
					m_addWords.get(m_addWords.size()-1));
		} 
		return "";
	}
	
	//doDelete deletes the corresponding line and call doAdd to update the file.
	public String doDelete(int line) throws IOException  {
		String userWords = m_addWords.get(line - 1);
		m_addWords.remove(line - 1);
		doAdd();
		return String.format(DELETE_MESSAGE, fileName,userWords);
	}
	
	//doClear clears the whole arraylist of words and call doAdd to update the file.
	public String doClear() throws IOException {
		m_addWords.clear();
		doAdd();
		return String.format(CLEAR_MESSAGE, fileName);
	}

	
	public void doDisplay() {
		if (m_addWords.isEmpty()) {
			System.out.println(String.format(NOTHING_TO_DISPLAY_MESSAGE,fileName));
		} else {
			for (int i = 0 ; i < m_addWords.size(); i++) {
				String userWords = m_addWords.get(i);
				System.out.println(i + 1 + ". " + userWords);
			} 
		}
	}


	private static String removeFirstWord(String userCommand) {
		return userCommand.replaceFirst(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String userWords = userCommand.trim().split("\\s+")[0];
		return userWords;
	}
	
	public int getLineCount() {
		return m_addWords.size();
	}

	public TextBuddy(String[] Args) {
		if (Args.length == 0) {
			System.out.println(ERROR_FILE_NAME_NOT_SPECIFIED);
			System.exit(0);
		} else {
			fileName = Args[0];
			f = new File(fileName);
		}
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void setFileName(String f) {
		this.fileName = f;
	}
	
	private static void show(String message) {
		System.out.println(message);
	}
	
	
}
