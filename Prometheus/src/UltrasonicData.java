import java.util.ArrayList ; 
import java.io.* ; 

/**
 * 
 * @author ujjwal
 *
 *-A Datagenerator class of Ultrasonic Sensor
 *--Column1 => Distance detected by sensor1
 *--Column2 => distance detected by sensor2
 *--Column3 => distance detected by sensor3
 *--Column4 => Whether  object detected is obstacle or box to list
 *---- 0 => obstacle and 1 => box 
 *
 *First 3 column represents distance L of object from each sensor 1,2,3
 *
 *TODO ! Changed to label encoded outputs 
 *--navigation output 
 *--(1,0,0)-> L -> left Turn 
 *--(0,1,0)-> S -> go straight
 *--(0,0,1)-> R -> Right Turn
 *
 *
 *-New Output System
 *Changing outputs to labels to encoded values : 
 *left => 1 
 *straight => 2
 *right => 3
 *
 */
public class UltrasonicData {
	
	public ArrayList<double[]> Dataset ;
	public int row_eachCase  ; 
	
	/*Constructor*/
	public UltrasonicData(int row_eachCase) {
		//empty constructor -> nothing to initialize
		this.Dataset =  new ArrayList<double[]>() ; 
		this.row_eachCase = row_eachCase ;
	}
	
	public void datageneratorObstacleAvoidance(){
		/**
		 * @CASE_1 : object is straight ahead of the sensor at distance L  
		 */
		
		
		/**object is visible in all 3 sensors
		 *-10.0-18.0 units with step 4.0 units -> 50 measurements
		 */
		 double x_1 = (18.0-10.0)/50.0 ; 
		 double start_1 = 10.0 ; 
//		 System.out.println("\nx_1      => " + x_1); 
//		 System.out.println("start_1  => "+ start_1 +"\n");
		 
		 
		 for(int i = 0; i <this.row_eachCase ; i++) {
			 double[] data = new double[5] ; // # of features in one single instance = 4
			 
			 double value = (start_1 + (i*x_1));
			 for(int y = 0 ; y < data.length - 1 ; y++) {
				 data[y] = value ;
//				 System.out.println("data[" + y + "] -> "+ data[y]) ; 
				 
			 }
			 /**
			  *-Adding the output of the row and obstacle coding
			  *	-- 0 = obstacle
			  * --Turn Right : navigational output = 3
			  *  
			  */
			 data[3] = 0 ;  
			 data[data.length - 1] = 3 ; 
			 System.out.println("") ; 
			 this.Dataset.add(data) ; 
		 }
		 
		 /**
		  *-20-145.0 unity with step 25.0 unit -> 50 measurements
		  */
		 double x_2 = (145.0 - 20.0)/50.0; 
		 double start_2 = 20.0 ; 
		 for(int i = 0 ; i <this.row_eachCase ; i++) {
			 double[] data = new double[5] ; // # of features in one single instance = 4
			 
			 double value = (start_2  + (i*x_2)); 
			 for(int y = 0 ; y < data.length - 1 ; y++) {
				 data[y] = value ; 
			 }
			 /**
			  * -Adding the output of the row and obstacle coding
			  * -- 0 = obstacle 
			  * --Go straight : navigational output = 2
			  */
			 data[3] = 0 ;
			 data[data.length -1 ] = 2 ; 
			 System.out.println("");
			 this.Dataset.add(data); 
			 
		 }
		 
		 
		 
		 
		 /**
		  * @CASE_2 : Object is left side of the robot at distance  L 
		  * object is visible only in the left most sensor
		  */
		 
		 
		 /** -3.0-1.80 unit with step 5.9 unit _. 50 measurement
		  * Object is close to the left most sensor
		  */
		 
		 double x_3 = (18.0 - 3.0) /50.0 ; 
		 double start_3 = 3.0 ; 
		 for(int i = 0 ; i <this.row_eachCase ; i++) {
			 double[] data = new double[5] ; 
			 double value = (start_3 + (i * x_3)) ;
			 data[0] = value ; // value only for left sensor
			 for(int y = 1 ; y < data.length -1 ; y++) {
				 data[y] = 0 ; 
			 }
			 /**
			  * -Adding the output of the row and obstacle coding
			  * -- 0 = obstacle
			  * --Go Right : navigational output = 3
			  */
			 data[3] = 0 ;
			 data[data.length -1 ] = 3 ; 
			 System.out.println("") ; 
			 this.Dataset.add(data) ; 
		 }
		 
		 /**
		  *- 20.0 -70.0 unit with step 10.0 unit -> 50 measurements
		  *--Object is far from the leftmost sensor => L is large & not visible in other 2 sensors
		  */
		 double x_4 = (70.0 -20.0)/50.0 ; 
		 double start_4 = 20.0; 
		 for(int i = 0 ; i< this.row_eachCase ; i++) {
			 double[] data = new double[5] ; 
			 double value = ( start_4 + (i * x_4 ));
			 data[0] = value ; 
			 for(int y = 1 ; y < data.length -1 ; y++) {
				 data[y] = 0 ; 
			 }
			 /**
			  *-Adding the output of the row and obstacle coding
			  *-- 0 = obstacle
			  *--Go straight : navigational output = 3
			  */
			 data[3] =0 ; 
			 data[data.length -1 ] = 3 ; 
			 System.out.println("") ;
			 this.Dataset.add(data);
			 
			 
		 }
		 
		 /**
		  * @CASE_3 ; Object is right side of the robot at distance L 
		  * Object is visible only in the rightmost sensor
		  */
		 
		 /**
		  * 3.0-18.0 unit with step 5.0 unit -> 50 measurements
		  * --Object is close to the right most sensor 
		  */
		 double x_5 = (18.0-3.0)/50.0 ; 
		 double start_5 = 3.0 ; 
		 for(int i = 0 ; i < this.row_eachCase ; i++) {
			 double[] data = new double[5] ; 
			 double value = (start_5 + (i * x_5)) ; 
			 data[2] = value ; // value in the right most sensor i.e. index 2
			 for(int y = 0 ; y < 2 ; y++) {
				 data[y] = 0  ; 
			 }
			 /**
			  *-Adding the output of the row and obstcale coding
			  *--0 = obstacle
			  *--Go left : navigational output => 1
			  */
			 data[3] =0 ; 

			 data[data.length -1 ] = 1 ; 
			 System.out.println("") ; 
			 this.Dataset.add(data); 
			 
		 }
		 
		 /**
		  *-20.0-70.0 units with 10.0 unit steps -> 50 measurements
		  *Object is far from the rightmost senosr and it is not visible in other 2 sensor
		  */
		 
		 double x_6 = (70.0 - 20.0)/50.0 ; 
		 double start_6 = 20.0 ; 
		 for(int i = 0 ; i < this.row_eachCase ; i++) {
			 double[] data = new double[5] ; 
			 double value = ( start_6 + (i * x_6)) ; 
			 data[2] = value ; 
			 for(int y = 0 ; y < 2 ; y++) {
				 data[y] = 0 ; 
			 }
			 /**
			  *-Adding the output of the row and obstacle coding
			  *--0 = obstacle
			  *--Go Straight : navigational output => 2
			  */
			 data[3] =0 ; 

			 data[data.length -1 ] = 1 ; 
			 System.out.println("") ;
			 this.Dataset.add(data) ; 
		 }
		 

		
	}
	
	
	/**
	 * GETTER
	 */
	public ArrayList<double[]> getDataset(){
		return this.Dataset ; 
	}

}




























