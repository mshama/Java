package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class is responsible for reading data from external sources
 * 
 * @author Moustafa Shama
 *
 */
public class DataReader {

	/**
	 * this function reads data from CSV file (filename) using seperator
	 * parameter
	 * 
	 * @param filename the name of the file that we want to read
	 * @param separator a special character to separate the cells
	 * @return returns an ArrayList of string array. each string array represents a line in the file
	 */
	public static ArrayList<String[]> read_CSV(String filename, String separator) {
		BufferedReader br = null;
		ArrayList<String[]> fileContent = null;
		try {

			br = new BufferedReader(new FileReader(filename));
			String line = "";
			fileContent = new ArrayList<String[]>();
			while ((line = br.readLine()) != null) {
				// split the line using separator
				String[] lineContent = line.split(separator + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // to avoid commas inside of quotes
				
				fileContent.add(lineContent);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return fileContent;

	}
}
