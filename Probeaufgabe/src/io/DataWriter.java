package io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class writes data to external sources (not database)
 * @author Moustafa Shama
 *
 */
public class DataWriter {
	/**
	 * writes fileContent to a CSV file (filename) using separator
	 * @param fileContent content that we want to write to file
	 * @param filename name of the file that we want to save
	 * @param separator special character to separate between cells
	 */
	public static void write_CSV(ArrayList<String[]> fileContent, String filename, String separator) {
		
		try {
			FileWriter writer = new FileWriter(filename);
		
			// loop on file content
			for(int i=0; i<fileContent.size(); i++){
				String[] lineContent = fileContent.get(i);
				// construct line string
				StringBuilder sb = new StringBuilder();
				for(int j=0; j<lineContent.length; j++){
					sb.append(lineContent[j]).append(separator);
				}
				sb.append("\n");
				writer.append(sb.toString());
			}
			writer.flush();
	        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
