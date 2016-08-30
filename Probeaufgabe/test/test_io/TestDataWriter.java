package test_io;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import io.DataReader;
import io.DataWriter;

public class TestDataWriter {

	@Test
	public void testWrite_CSV() {
		String filename = System.getProperty("user.dir") + "/res/test/test_writeCSV.csv";
		ArrayList<String[]> fileContent = new ArrayList<String[]>();
		fileContent.add(new String[]{"1","2","test"});
		DataWriter.write_CSV(fileContent, filename, ",");
		
		fileContent = DataReader.read_CSV(filename, ",");
		assertArrayEquals(new String[]{"1","2","test"}, fileContent.get(0));
	}

}
