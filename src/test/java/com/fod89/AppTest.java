package com.fod89;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fod89.exception.NoFuelException;
import com.opencsv.CSVReader;
import com.rf.core.RestEngine;

/**
 * 
 * @author fod
 *
 */
public class AppTest {
	
	private static final Logger logger = Logger.getLogger(AppTest.class);
	private String jsonApi;
	private String inputCsv;
	private String[] inputHeaders;
	
	@BeforeSuite
	private static void init(){
		System.out.println("init called");
	}
	
	@BeforeTest
	@Parameters({"jsonApi","inputCsv"})
	private void beforeTest(String jsonApi,String inputCsv){
		logger.info("lodded jsonApi:"+jsonApi);
		logger.info("lodded inputCsv:"+inputCsv);
		this.jsonApi = jsonApi;
		this.inputCsv = inputCsv;
	}
   
	@Test(dataProvider="dp")
	private void test(Object[] columns){
		//columns = (Object[]) columns[0];
		HashMap<String, Object> inputData = new HashMap<>();
		for(int i=0;i<columns.length;i++){
			inputData.put(inputHeaders[i], columns[i]);
		}
		logger.info("=========Test Executes============");
		logger.info(inputData);
		RestEngine.instance(jsonApi).with(inputData).start();
	}
	
	@DataProvider(name="dp")
	private Object[][] getInputDataFromCSV() throws Exception{
		CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(inputCsv));
            inputHeaders = reader.readNext();
            for(int i=0;i<inputHeaders.length;i++){
               inputHeaders[i] = "IN_"+inputHeaders[i];	
            }
            List<String[]> lines =  reader.readAll();
            int size = lines.size();
            Object[][] data = new Object[size][1];
            for(int i=0;i<size;i++){
            	data[i][0]= lines.get(i);
            }
            
            if(size==0){
            	throw new NoFuelException("no test data in csv file!!");
            }
            return data;
        } finally  {
            if(reader!=null)
            	reader.close();
        }
	
	}

	@AfterSuite
	private static void exit(){
		System.out.println("exit called");
	}
}
