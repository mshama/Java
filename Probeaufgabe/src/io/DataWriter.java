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
	 * writes fileContent to a CSV file (filename) using seperator
	 * @param fileContent
	 * @param filename
	 * @param seperator
	 */
	public static void write_CSV(ArrayList<String[]> fileContent, String filename, String seperator) {
		
		try {
			FileWriter writer = new FileWriter(filename);
		
			// loop on file content
			for(int i=0; i<fileContent.size(); i++){
				String[] lineContent = fileContent.get(i);
				// construct line string
				StringBuilder sb = new StringBuilder();
				for(int j=0; j<lineContent.length; j++){
					sb.append(lineContent[j]).append(seperator);
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
