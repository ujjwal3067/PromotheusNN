import java.util.Random; 
import java.util.ArrayList;


/**
 * --------------------------------------------------------------------------------------------------------------------------------------------------------
 * 	!NEURAL NETWORK 
 * --------------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class NN{
	
    
    private ArrayList<double[][]> WEIGHTS;
    private ArrayList<double[][]> WEIGHTS_DELTAS;
    private ArrayList<double[][]> BIASES;
    private ArrayList<double[][]> BIASES_DELTAS;
    private ArrayList<double[][]> LAYER_ACTIVATIONS;
    private ArrayList<double[][]> LAYER_INPUTS;
    private double LEARNING_RATE;
    private double BIAS_LEARNING_RATE;
    private int[] STRUCTURE; 
    private Random rand;


    //Constructor
    
    public NN(int[] NN_config){
        rand = new Random();
        this.LEARNING_RATE = 1;
        this.BIAS_LEARNING_RATE = 1;
        NN_INIT( NN_config, rand);
    }

    public NN(int[] NN_config, double learning_rate){
        rand = new Random();
        this.LEARNING_RATE = learning_rate;
        this.BIAS_LEARNING_RATE = learning_rate;
        NN_INIT(NN_config, rand);
    }
    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	!HELPER FUNCTIONS 
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 
     */
    
    private static final int maxCharsPerOutput = 350;

    //activation function for layers
    private static double activationFunction(double x){
        return (1/(1+Math.pow(Math.E, -x)));
    }
    
    //derivative of activation function
    private static double activationDerivative(double x){
        return (1/(Math.pow(Math.pow(Math.E, -x*.5)+Math.pow(Math.E, x*.5),2)));
    }
    
    //function to calculate the error
    private static double errorFunction(double expected, double actual){
        return 0.5 * Math.pow((expected - actual), 2);
    }
    
    //function to calculate the derivative of error
    private static double errorDerivative(double expected, double actual){
        return actual - expected;
    }
    private static double calcError(double[][] expected, double[][] actual){
        double error=0;
        if(expected.length != actual.length || expected[0].length != actual[0].length){
            throw new IllegalArgumentException("different dimensions for expected and actual data");
        }
        
        for(int row=0; row<expected.length; row++){
            for(int col=0; col<expected[0].length; col++){
                error += errorFunction(expected[row][col], actual[row][col]);
            }
        }
        return error;
    }
    
    //Displaying the inputed matrix to the console
    private static String displayConsole(double[][] matrix){
        return VECTOR.toStringOneLine(matrix, maxCharsPerOutput);
    }
    
    //Checking for convergance of error
    private static void checkError(double error){
        if(Double.isNaN(error)){
            throw new RuntimeException("calculate erorr convergance problem : change the learning rate ( smaller  learning rate ) ");
        }
    }
    private static double[][][] DATAmanipulation(double[][] data){
        double[][][] output = new double[data.length][data[0].length][1];
        
        for(int row = 0; row < data.length; row++){
           for(int col = 0; col < data[0].length; col++){
                output[row][col][0] = data[row][col];
            } 
        }
        
        return output;
    }

    public ArrayList<double[][]> getWeights(){
//    	ArrayList<double[][]>  copy_weights = new ArrayList<>(this.weights);
    	return this.WEIGHTS; 
    }
    
    public double getLearningRate(){
        return this.LEARNING_RATE;
    }

    public void setLearningRate(double lr){
        if(lr > 0){
            this.LEARNING_RATE = lr;
            this.BIAS_LEARNING_RATE = lr;
        }else{
            throw new IllegalArgumentException("invalied learning rate \n please enter positive learning rate"+lr);
        }
    }

    public Random getRandom(){
        return rand;
    }

    public void setRandom(Random r){
        if(Random.class.isInstance(r)){
            rand = r;
        }else{
            throw new IllegalArgumentException("ERROR : in setRandom");
        }
    }

    public int[] getStructure(){
        return this.STRUCTURE;
    }

    public void VALID_input_checker(double[][] INPUT, double[][] OUTPUT){
        
        if(INPUT.length != OUTPUT.length){
            throw new IllegalArgumentException("Size of input and output data doesn't match: " + INPUT.length + ", " + OUTPUT.length);
        }
        
        for(int i=0; i<INPUT.length; i++){
            if(INPUT[i].length != this.STRUCTURE[0]){
                throw new IllegalArgumentException("Input dimensions doens't match with NN input layer: " + i);
            } 
            
            if(OUTPUT[i].length != this.STRUCTURE[this.STRUCTURE.length-1]){
                throw new IllegalArgumentException("Output dimebnsions doesn't match with NN output layer: " + i);
            } 
        }
        
    }
    public double[][] shuffleData(double[][] DATA){
        Random r = new Random();
        
        double[] temp;
        
        for(int i=0; i<DATA.length; i++){
            int index = r.nextInt(DATA.length);
            temp = DATA[i];
            DATA[i] = DATA[index];
            DATA[index] = temp;
        }
        
        return DATA;
    }
   
   
    private void WEIGHTS_UPDATE(int trainingNum){
        for(int i=0; i<WEIGHTS.size(); i++){
        	WEIGHTS.set(i, VECTOR.add(WEIGHTS.get(i), VECTOR.multiply(this.LEARNING_RATE/trainingNum, this.WEIGHTS_DELTAS.get(i))));
        	this.WEIGHTS_DELTAS.set(i, new double[this.STRUCTURE[i+1]][this.STRUCTURE[i]]);
        }
    }
    private void WEIGHTS_UPDATE(int trainingNum, boolean normalized){
        if(normalized){
            for(int i=0; i<WEIGHTS.size(); i++){
            	WEIGHTS.set(i, VECTOR.add(WEIGHTS.get(i), VECTOR.multiply(this.LEARNING_RATE, VECTOR.normalize(this.WEIGHTS_DELTAS.get(i)))));
            	this.WEIGHTS_DELTAS.set(i, new double[this.STRUCTURE[i+1]][this.STRUCTURE[i]]);
            }
        }else{
        	WEIGHTS_UPDATE(trainingNum);
        }
    }

    private void BIAS_UPDATE(int trainingNum){
        for(int i=1; i<BIASES.size(); i++){
        	this.BIASES.set(i, VECTOR.add(this.BIASES.get(i), VECTOR.multiply(this.BIAS_LEARNING_RATE/trainingNum, this.BIASES_DELTAS.get(i))));
            this.BIASES_DELTAS.set(i, new double[this.STRUCTURE[i]][1]); 
        }
    }

    private void BIAS_UPDATE(int trainingNum, boolean normalized){
        if(normalized){
            for(int i=1; i<this.BIASES.size(); i++){
                this.BIASES.set(i, VECTOR.add(this.BIASES.get(i), VECTOR.multiply(this.BIAS_LEARNING_RATE, VECTOR.normalize(this.BIASES_DELTAS.get(i)))));
                this.BIASES_DELTAS.set(i, new double[this.STRUCTURE[i]][1]);
            }
        }else{
        	BIAS_UPDATE(trainingNum);
        }
    }


    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	 INITIALIZING NEURAL NETWORK
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    
    private void NN_INIT(int[] NN_config, Random rand){
        //initialize
        this.STRUCTURE				 = NN_config;
        this.LAYER_ACTIVATIONS 		= new ArrayList<double[][]>(NN_config.length);
        this.LAYER_INPUTS 			= new ArrayList<double[][]>(NN_config.length);
        this.WEIGHTS 				= new ArrayList<double[][]>(NN_config.length-1);
        this.WEIGHTS_DELTAS			= new ArrayList<double[][]>(NN_config.length-1);
        this.BIASES 				= new ArrayList<double[][]>(NN_config.length);
        this.BIASES_DELTAS 			= new ArrayList<double[][]>(NN_config.length);
        
        
        //set up structure
        for(int LAYER = 0; LAYER < NN_config.length; LAYER++){
            this.LAYER_ACTIVATIONS.add(VECTOR.randomizeArray( new double[NN_config[LAYER]][1], rand));
            
            if(LAYER != 0){
                this.LAYER_INPUTS.add(VECTOR.randomizeArray(new double[NN_config[LAYER]][1], rand));
                this.WEIGHTS.add(VECTOR.randomizeArray(new double[NN_config[LAYER]][NN_config[LAYER-1]], rand));
                this.WEIGHTS_DELTAS.add(new double[NN_config[LAYER]][NN_config[LAYER-1]]);
                this.BIASES.add(VECTOR.randomizeArray(new double[NN_config[LAYER]][1], rand));
                this.BIASES_DELTAS.add(new double[NN_config[LAYER]][1]);
            }else{
                this.BIASES.add(null);
                this.BIASES_DELTAS.add(null);
                this.LAYER_INPUTS.add(null);
            }
        }
    }

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	FORWARD PROPAGATION
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void FORWARD_PROPAGATION(double[][] input){
        
        this.LAYER_ACTIVATIONS.set(0, input);
        
        for(int LAYER=1; LAYER<this.STRUCTURE.length; LAYER++){
            this.LAYER_INPUTS.set(LAYER, VECTOR.add(VECTOR.multiply(this.WEIGHTS.get(LAYER-1), this.LAYER_ACTIVATIONS.get(LAYER-1)), this.BIASES.get(LAYER)));
            
            for(int ROW =0; ROW<this.LAYER_ACTIVATIONS.get(LAYER).length; ROW++){
                for(int col=0; col<this.LAYER_ACTIVATIONS.get(LAYER)[0].length; col++){
                    this.LAYER_ACTIVATIONS.get(LAYER)[ROW][col] = activationFunction(this.LAYER_INPUTS.get(LAYER)[ROW][col]);
                }
            }
        }
        
    } 

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	BACK PROPAGATION  : UPDATING WEIGHTS
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void BACKWORD_PROPAGATION(double[][] output){

        double[][] layerGradient = VECTOR.subtract(this.LAYER_ACTIVATIONS.get(this.STRUCTURE.length-1), output);
        for(int LAYER = this.STRUCTURE.length - 1; LAYER > 0; LAYER--){
            double[][] biasGradient = new double[this.BIASES.get(LAYER).length][1];
            for(int row = 0; row < biasGradient.length; row++){
                biasGradient[row][0] = layerGradient[row][0] *
                activationDerivative(this.LAYER_INPUTS.get(LAYER)[row][0]);
            }

            this.BIASES_DELTAS.set(LAYER, VECTOR.subtract(this.BIASES_DELTAS.get(LAYER), biasGradient));
            double[][] weightGradient = new double[WEIGHTS.get(LAYER - 1).length][this.WEIGHTS.get(LAYER - 1)[0].length];
            for(int toNeuron = 0; toNeuron < weightGradient.length; toNeuron++){
                for(int fromNeuron = 0; fromNeuron < weightGradient[0].length; fromNeuron++){
                    weightGradient[toNeuron][fromNeuron] = layerGradient[toNeuron][0] *
                    activationDerivative(this.LAYER_INPUTS.get(LAYER)[toNeuron][0]) * 
                    this.LAYER_ACTIVATIONS.get(LAYER - 1)[fromNeuron][0];
                }
            }
            this.WEIGHTS_DELTAS.set(LAYER - 1, VECTOR.subtract(this.WEIGHTS_DELTAS.get(LAYER - 1), weightGradient));
            double[][] activationGradient = new double[this.LAYER_ACTIVATIONS.get(LAYER - 1).length][this.LAYER_ACTIVATIONS.get(LAYER - 1)[0].length];
            for(int fromNeuron = 0; fromNeuron < activationGradient.length; fromNeuron++){
                for(int toNeuron = 0; toNeuron < this.STRUCTURE[LAYER]; toNeuron++){
                    activationGradient[fromNeuron][0] += layerGradient[toNeuron][0] *
                    activationDerivative(this.LAYER_INPUTS.get(LAYER)[toNeuron][0]) * 
                    this.WEIGHTS.get(LAYER - 1)[toNeuron][fromNeuron];
                }
            }
            layerGradient = activationGradient;
        }
        
        
    }
    
    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * NEURAL NET TRAINING
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */  
/**
 * 
 * @param _TRAIN_input - Data input for training purpose  type => double[][]
 * @param _TRAIN_output - Data output for  error check and updating weight  type => double[][]
 * @param iterations  - number of iteration for training
 * @param dataBatches  - number of Dataset batches to create for batch training
 * @param normalizedGradientDescent - true if only want to be concerned with normalized value of Gradient Descent
 */
    public void TRAIN(double[][] _TRAIN_input, double[][] _TRAIN_output, int iterations, int BATCHES, boolean normalizedGradientDescent){
        
        double[][][] TRAIN_input = DATAmanipulation(_TRAIN_input);
        double[][][] TRAIN_output = DATAmanipulation(_TRAIN_output);
        
        if(iterations<1){
            throw new IllegalArgumentException( iterations + " iterations is not valid input.... Please use iteration > 1");    
        }
        System.out.println("-------------------------"); 
        System.out.println("TRAINING in progress .  .  . \n Iterations =>  " + iterations);
        System.out.println("-------------------------"); 

        for(int i=0; i<iterations; i++){
            
            double errorTotal=0;
            for(int batch=0; batch< BATCHES; batch++){
                for(int j=batch*TRAIN_input.length/BATCHES; j<(batch+1)*TRAIN_input.length/BATCHES; j++){
                	//propagating errors
                	FORWARD_PROPAGATION(TRAIN_input[j]);
                    BACKWORD_PROPAGATION(TRAIN_output[j]);
                    
                    if(i%(iterations/10)==0){
                        errorTotal+=calcError(this.LAYER_ACTIVATIONS.get(this.LAYER_ACTIVATIONS.size()-1), TRAIN_output[j]);
                        checkError(errorTotal);
                    }
                    
                }
                if(batch < TRAIN_input.length%BATCHES){
                	FORWARD_PROPAGATION(TRAIN_input[TRAIN_input.length-1-batch]);
                    BACKWORD_PROPAGATION(TRAIN_output[TRAIN_output.length-1-batch]);
                    
                    if(i%(iterations/10)==0){
                        errorTotal+=calcError(this.LAYER_ACTIVATIONS.get(this.LAYER_ACTIVATIONS.size()-1), TRAIN_output[TRAIN_input.length-1-batch]);
                        checkError(errorTotal);
                    }
                    WEIGHTS_UPDATE(TRAIN_input.length/BATCHES+1, normalizedGradientDescent);
                    WEIGHTS_UPDATE(TRAIN_input.length/BATCHES+1, normalizedGradientDescent);
                }else{
                	WEIGHTS_UPDATE(TRAIN_input.length/BATCHES, normalizedGradientDescent);
                	BIAS_UPDATE(TRAIN_input.length/BATCHES, normalizedGradientDescent);
                }
            }

        }
    }
    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * TESTING NEURAL NETWORK
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    /**
     * 
     * @param _TEST_input : Data input for testing purpose type => double[][]
     * @param _TEST_output : Data output for error calculation ( groundtruth - predicted ) 
     */
    public void TEST(double[][] _TEST_input, double[][] _TEST_output){
        
        double[][][] TEST_input = DATAmanipulation(_TEST_input);
        double[][][] TEST_output = DATAmanipulation(_TEST_output);
        
        System.out.println("-------------------------"); 
        System.out.println("|   TESTING in progress |");
        System.out.println("-------------------------");         
        double CumulativeERROR = 0;
        for(int i=0; i<TEST_input.length; i++){
        	FORWARD_PROPAGATION(TEST_input[i]);
            double inputError = calcError(TEST_output[i], this.LAYER_ACTIVATIONS.get(this.LAYER_ACTIVATIONS.size()-1)); //calc error
            CumulativeERROR += inputError; 
            checkError(CumulativeERROR);checkError(inputError);   
            System.out.println("TEST ["+i+"]" + " -- ERROR: "+ inputError);System.out.println();
        }
        System.out.println(); 
        System.out.println("----------------------------------------------------------");
        System.out.println("[ AVERAGE ] error in testing : " + CumulativeERROR/TEST_input.length);
        System.out.println("----------------------------------------------------------");

    }

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	 PREDICT
     * !TODO : return the predicted vector instead of printing it .. 
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * @return 
     */
    public  ArrayList<double[][]> PREDICT(double[][] Input){
        double[][][] unseen_input = DATAmanipulation(Input);
//        double[][] output = {} ; 
        ArrayList<double[][]> output = new ArrayList<>();
 
//        double[] output = new double[5]; 
        
        System.out.println("--------------------");  
        System.out.println("|    PREDICITNG    |"); 
        System.out.println("--------------------");

        
        for(int i=0; i<unseen_input.length; i++){
        	FORWARD_PROPAGATION(unseen_input[i]);
            
            System.out.println("..prediction ["+i+ "]");
            if(unseen_input[i].length*unseen_input[i][0].length < maxCharsPerOutput){
                System.out.println("INPUT: "+ VECTOR.toStringOneLine(unseen_input[i]));
            }else{
                System.out.println("input: too long to display");
            }
            output.add(this.LAYER_ACTIVATIONS.get(this.LAYER_ACTIVATIONS.size()-1)) ;             
        }//loop
//        System.out.println();
		return output; 

    }


}


