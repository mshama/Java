package test_core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.DataController;
import exceptions.DependencyException;
import exceptions.ParameterFormatException;

public class TestDataController {

	@Test
	public void testReadData() throws ParameterFormatException {
		DataController dc = new DataController();
		String filename = System.getProperty("user.dir") + "/res/test/test_readCSV.csv";
		ArrayList<String[]> result = dc.readDataFile("ProductGroup", filename);
		assertArrayEquals(new String[]{"4","2","test1"}, result.get(1));
	}

	@Test
	public void testAddData() throws ParameterFormatException {
		DataController dc = new DataController();
		String filename = System.getProperty("user.dir") + "/res/test/test_readCSV.csv";
		dc.readDataFile("ProductGroup", filename);
		dc.addData(new String[]{"8","2","test8"});
		assert(dc.isModified());
		
		ArrayList<String[]> data = dc.getData();
		assertArrayEquals(new String[]{"8","2","test8"}, data.get(data.size()-1));
	}

	@Test
	public void testUpdateData() throws ParameterFormatException {
		DataController dc = new DataController();
		String filename = System.getProperty("user.dir") + "/res/test/test_readCSV.csv";
		dc.readDataFile("ProductGroup", filename);
		dc.updateData(new String[]{"4","test4"}, 1);
		assert(dc.isModified());
		
		ArrayList<String[]> data = dc.getData();
		assertArrayEquals(new String[]{"5","4","test4"}, data.get(2));
		
	}

	@Test
	public void testSaveData() throws DependencyException, ParameterFormatException {
		DataController dc = new DataController();
		String filename = System.getProperty("user.dir") + "/res/test/test_readCSV.csv";
		dc.readDataFile("ProductGroup", filename);
		
		assert(dc.isModified());
		
		dc.saveData();
		
		assert(!dc.isModified());
		
		dc.updateData(new String[]{"4","test4"}, 1);
		assert(dc.isModified());
		
		dc.saveData();
		
		assert(!dc.isModified());
		
		dc.addData(new String[]{"8","2","test8"});
		assert(dc.isModified());
		
		dc.saveData();
		
		assert(!dc.isModified());
	}

}
