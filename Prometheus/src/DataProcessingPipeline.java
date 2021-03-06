import java.util.ArrayList; 
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedReader ; 
import java.io.IOException;
import java.nio.charset.StandardCharsets ; 
import java.nio.file.Files ; 
import java.nio.file.Path ; 
import java.nio.file.Paths ; 
import java.util.List ;
import java.util.Scanner ; 
import java.util.stream.*;

import java.io.File; 
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;


import java.io.BufferedReader;
import java.io.FileNotFoundException ; 
import java.io.FileReader; 
import java.io.IOException; 
public class DataProcessingPipeline {
	
	
	
	public String Path = ""; 
	public double[][] dataset ; 
	public Scanner scanin = null ; 
	public String InputLine = ""; 
	public int Row_count = 0 ; 
	public int instances_num ; 
	public int features_num ; 
	public double[][] trainIn ; 
	public double[][] trainOut ; 
	
	//constructor
	public DataProcessingPipeline(String Path, int instances_num, int features_num) {
		this.instances_num = instances_num ; 
		this.features_num = features_num ; 
		this.dataset = new double[this.instances_num][this.features_num]; 	
		this.Path = Path ; 
		this.trainIn = new double[this.instances_num][this.features_num - 1 ]; 
		this.trainOut = new double[this.instances_num][1] ; 


	}
	
	//data processing pipeline
	public void DATA_Processing() {
		
		
		System.out.println(">> Loading dataset ... "); 
		
		try {
	
			BufferedReader br = new BufferedReader(new FileReader(this.Path)) ; 
			
			while((this.InputLine = br.readLine()) !=null) {
				
				String[] row = this.InputLine.split(","); 
				double[] doubleValuesRow = Arrays.stream(row).mapToDouble(Double::parseDouble).toArray() ; 
				
				for(int i = 0 ; i < doubleValuesRow.length ; i ++) {
					this.dataset[this.Row_count][i] = doubleValuesRow[i] ; 

				}
				this.Row_count++; 
			}
			

			/*
			 *-Filtering out Training input and output features
			 */
			for(int i = 0 ; i < this.dataset.length ; i++) {
				for(int j=0 ; j < this.dataset[0].length - 1 ; j++) {
					this.trainIn[i][j] = this.dataset[i][j]; 
				}
			}
			
			for(int i = 0 ; i < this.dataset.length ; i++) {
				this.trainOut[i][0] = this.dataset[i][this.dataset[0].length - 1];

			}
			
			 
	
		}
		catch(FileNotFoundException e ) {
			e.printStackTrace(); 
			System.out.println(" ERROR : file not found  on Path  : " + Path) ; 
		}catch(IOException e ) {
			e.printStackTrace();
			System.out.println("ERROR : Unable to read the file on Path : " + Path ) ; 
		}
		
	}// end of dataprocessing
	
	
	/*
	 * GETTERS
	 */
	public double[][] TRAIN_input(){
		return this.trainIn ; 
	}
	public double[][] TRAIN_output(){
		return this.trainOut ; 
	}
	
	/*
	 * PRINTING DETAILS of the DATASET
	 *-@param : 
	 *--num : number of instances to print out
	 */
	public void Details(int num) {
		
		
		for(int i = 0 ; i < num ; i++) {
			System.out.print("INPUT --> ");

			for(int j = 0 ; j < trainIn[0].length; j++) {
				System.out.print("     ");
				System.out.print(trainIn[i][j]);
			}
			System.out.print("       ");
			System.out.print("OUTPUT -> " + this.trainOut[i][0]);
			System.out.println("\n--------------------------------------------------------------------------------------------------"); 
		}


	}}
