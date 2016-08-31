package test_models;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import exceptions.DependencyException;
import exceptions.DuplicateItemException;
import exceptions.ItemNotSavedException;
import exceptions.NoItemWasFoundException;
import exceptions.ParameterFormatException;
import models.Model;
import models.ProductGroup;

public class TestProductGroup {

	@Test
	public void testSelect() {
		// test that something exits
		try {
			ArrayList<ProductGroup> result = ProductGroup.select("Description", "test");
			assert("test".equals(result.get(0).getDescription()));
		} catch (NoItemWasFoundException e) {
			e.printStackTrace();
		}
		
				
		// test that something does not exit and that an exception is thrown
		try {
			ProductGroup.select("Description", "test1");			
		} catch (NoItemWasFoundException e) {
			assert(true);
		}
	}

	@Test
	public void testSave() throws DuplicateItemException, DependencyException {
		// test insert
		ProductGroup pg = new ProductGroup(1,2,"test1");
		assert(!pg.isDatabaseRecord());
		pg.save();
		assert(pg.isDatabaseRecord());
		
		// test update
		pg.setDescription("test3");
		pg.save();
		
		try {
			ArrayList<ProductGroup> result = ProductGroup.select("Description", "test3");
			assert("test3".equals(result.get(0).getDescription()));
			assert(1 == result.get(0).getProductGroupID());
		} catch (NoItemWasFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() throws ItemNotSavedException, SQLException, DependencyException {
		try {
			ArrayList<ProductGroup> result = ProductGroup.select("Description", "test3");
			result.get(0).delete();
			assert(!result.get(0).isDatabaseRecord());
		} catch (NoItemWasFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSetVariables() throws ParameterFormatException {
		ProductGroup pg = new ProductGroup();
		pg.setVariables(new String[] {"1","2","test1"});
		
		assertArrayEquals(new String[]{"1","2","test1"}, pg.getData());
	}
	
	@Test
	public void testModelSetVariables() throws ParameterFormatException {
		Model pg = new ProductGroup();
		pg.setVariables(new String[] {"1","2","test1"});
		
		assertArrayEquals(new String[]{"1","2","test1"}, pg.getData());
	}

}
