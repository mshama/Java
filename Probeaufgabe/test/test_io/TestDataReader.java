package test_io;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import io.DataReader;

public class TestDataReader {

	@Test
	public void testRead_CSV() {
		String filename = System.getProperty("user.dir") + "/res/test/test_readCSV.csv";;
		ArrayList<String[]> fileContent = DataReader.read_CSV(filename, ",");
		
		assertArrayEquals(new String[]{"1","2","abc"}, fileContent.get(0));
	}

}
