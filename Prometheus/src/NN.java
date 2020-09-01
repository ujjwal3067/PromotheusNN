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
    private ArrayList<double[][]> layerActivations;
    private ArrayList<double[][]> layerInputs;
    private Random rand;
    private double learningRate;
    private double biasLearningRate;
    private int[] structure; 

    //Constructor
    
    public NN(int[] NN_config){
        rand = new Random();
        learningRate = 1;
        biasLearningRate = 1;
        NN_INIT( NN_config, rand);
    }

    public NN(int[] NN_config, double learning_rate){
        rand = new Random();
        learningRate = learning_rate;
        biasLearningRate = learning_rate;
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
        return Matrix.toStringOneLine(matrix, maxCharsPerOutput);
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
        return learningRate;
    }

    public void setLearningRate(double lr){
        if(lr > 0){
            learningRate = lr;
            biasLearningRate = lr;
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
            throw new IllegalArgumentException("argument is not a Random object");
        }
    }

    public int[] getStructure(){
        return structure;
    }

    public void VALID_input_checker(double[][] INPUT, double[][] OUTPUT){
        
        if(INPUT.length != OUTPUT.length){
            throw new IllegalArgumentException("Size of input and output data doesn't match: " + INPUT.length + ", " + OUTPUT.length);
        }
        
        for(int i=0; i<INPUT.length; i++){
            if(INPUT[i].length != structure[0]){
                throw new IllegalArgumentException("Input dimensions doens't match with NN input layer: " + i);
            } 
            
            if(OUTPUT[i].length != structure[structure.length-1]){
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
   
   
    private void updateWeights(int trainingNum){
        for(int i=0; i<WEIGHTS.size(); i++){
        	WEIGHTS.set(i, Matrix.add(WEIGHTS.get(i), Matrix.multiply(learningRate/trainingNum, this.WEIGHTS_DELTAS.get(i))));
        	this.WEIGHTS_DELTAS.set(i, new double[structure[i+1]][structure[i]]);
        }
    }
    private void updateWeights(int trainingNum, boolean normalized){
        if(normalized){
            for(int i=0; i<WEIGHTS.size(); i++){
            	WEIGHTS.set(i, Matrix.add(WEIGHTS.get(i), Matrix.multiply(learningRate, Matrix.normalize(this.WEIGHTS_DELTAS.get(i)))));
            	this.WEIGHTS_DELTAS.set(i, new double[structure[i+1]][structure[i]]);
            }
        }else{
            updateWeights(trainingNum);
        }
    }

    private void updatebiases(int trainingNum){
        for(int i=1; i<BIASES.size(); i++){
        	this.BIASES.set(i, Matrix.add(this.BIASES.get(i), Matrix.multiply(biasLearningRate/trainingNum, this.BIASES_DELTAS.get(i))));
            this.BIASES_DELTAS.set(i, new double[structure[i]][1]); 
        }
    }

    private void updatebiases(int trainingNum, boolean normalized){
        if(normalized){
            for(int i=1; i<this.BIASES.size(); i++){
                this.BIASES.set(i, Matrix.add(this.BIASES.get(i), Matrix.multiply(biasLearningRate, Matrix.normalize(this.BIASES_DELTAS.get(i)))));
                this.BIASES_DELTAS.set(i, new double[structure[i]][1]);
            }
        }else{
            updatebiases(trainingNum);
        }
    }


    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	 INITIALIZING NEURAL NETWORK
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    
    private void NN_INIT(int[] NN_config, Random rand){
        //initialize
        structure = NN_config;
        layerActivations 		= new ArrayList<double[][]>(NN_config.length);
        layerInputs 			= new ArrayList<double[][]>(NN_config.length);
        this.WEIGHTS 			= new ArrayList<double[][]>(NN_config.length-1);
        this.WEIGHTS_DELTAS		= new ArrayList<double[][]>(NN_config.length-1);
        this.BIASES 			= new ArrayList<double[][]>(NN_config.length);
        this.BIASES_DELTAS 				= new ArrayList<double[][]>(NN_config.length);
        
        
        //set up structure
        for(int LAYER = 0; LAYER < NN_config.length; LAYER++){
            layerActivations.add(Matrix.randomizeArray( new double[NN_config[LAYER]][1], rand));
            
            if(LAYER != 0){
                layerInputs.add(Matrix.randomizeArray(new double[NN_config[LAYER]][1], rand));
                this.WEIGHTS.add(Matrix.randomizeArray(new double[NN_config[LAYER]][NN_config[LAYER-1]], rand));
                this.WEIGHTS_DELTAS.add(new double[NN_config[LAYER]][NN_config[LAYER-1]]);
                this.BIASES.add(Matrix.randomizeArray(new double[NN_config[LAYER]][1], rand));
                this.BIASES_DELTAS.add(new double[NN_config[LAYER]][1]);
            }else{
                this.BIASES.add(null);
                this.BIASES_DELTAS.add(null);
                layerInputs.add(null);
            }
        }
    }

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	FORWARD PROPAGATION
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void propagateForward(double[][] input){
        
        layerActivations.set(0, input);
        
        for(int layer=1; layer<structure.length; layer++){
            layerInputs.set(layer, Matrix.add(Matrix.multiply(this.WEIGHTS.get(layer-1), layerActivations.get(layer-1)), this.BIASES.get(layer)));
            
            for(int row=0; row<layerActivations.get(layer).length; row++){
                for(int col=0; col<layerActivations.get(layer)[0].length; col++){
                    layerActivations.get(layer)[row][col] = activationFunction(layerInputs.get(layer)[row][col]);
                }
            }
        }
        
    } 

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	BACK PROPAGATION  : UPDATING WEIGHTS
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    private void propagateBackward(double[][] output){

        double[][] layerGradient = Matrix.subtract(layerActivations.get(structure.length-1), output);
        for(int layer = structure.length - 1; layer > 0; layer--){
            double[][] biasGradient = new double[this.BIASES.get(layer).length][1];
            for(int row = 0; row < biasGradient.length; row++){
                biasGradient[row][0] = layerGradient[row][0] *
                activationDerivative(layerInputs.get(layer)[row][0]);
            }

            this.BIASES_DELTAS.set(layer, Matrix.subtract(this.BIASES_DELTAS.get(layer), biasGradient));
            double[][] weightGradient = new double[WEIGHTS.get(layer - 1).length][this.WEIGHTS.get(layer - 1)[0].length];
            for(int toNeuron = 0; toNeuron < weightGradient.length; toNeuron++){
                for(int fromNeuron = 0; fromNeuron < weightGradient[0].length; fromNeuron++){
                    weightGradient[toNeuron][fromNeuron] = layerGradient[toNeuron][0] *
                    activationDerivative(layerInputs.get(layer)[toNeuron][0]) * 
                    layerActivations.get(layer - 1)[fromNeuron][0];
                }
            }
            this.WEIGHTS_DELTAS.set(layer - 1, Matrix.subtract(this.WEIGHTS_DELTAS.get(layer - 1), weightGradient));
            double[][] activationGradient = new double[layerActivations.get(layer - 1).length][layerActivations.get(layer - 1)[0].length];
            for(int fromNeuron = 0; fromNeuron < activationGradient.length; fromNeuron++){
                for(int toNeuron = 0; toNeuron < structure[layer]; toNeuron++){
                    activationGradient[fromNeuron][0] += layerGradient[toNeuron][0] *
                    activationDerivative(layerInputs.get(layer)[toNeuron][0]) * 
                    this.WEIGHTS.get(layer - 1)[toNeuron][fromNeuron];
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
 * @param dataBatches  - number of dataset batches to create for batch training
 * @param normalizedGradientDescent - true if only want to be concerned with normalized value of Gradient Descent
 */
    public void train(double[][] _TRAIN_input, double[][] _TRAIN_output, int iterations, int BATCHES, boolean normalizedGradientDescent){
        
        double[][][] TRAIN_input = DATAmanipulation(_TRAIN_input);
        double[][][] TRAIN_output = DATAmanipulation(_TRAIN_output);
        
        if(iterations<1){
            throw new IllegalArgumentException( iterations + " iterations is not valid input.... Please use iteration > 1");    
        }
        
        System.out.println("...Starting Training for => " + iterations + " iterations...\n");
        for(int i=0; i<iterations; i++){
            
            double errorTotal=0;
   /**
    * Create batches
    */
            for(int batch=0; batch< BATCHES; batch++){
                for(int j=batch*TRAIN_input.length/BATCHES; j<(batch+1)*TRAIN_input.length/BATCHES; j++){
                	//propagating errors
                    propagateForward(TRAIN_input[j]);
                    propagateBackward(TRAIN_output[j]);
                    
                    if(i%(iterations/10)==0){
                        errorTotal+=calcError(layerActivations.get(layerActivations.size()-1), TRAIN_output[j]);
                        checkError(errorTotal);
                    }
                    
                }
                if(batch < TRAIN_input.length%BATCHES){
                    propagateForward(TRAIN_input[TRAIN_input.length-1-batch]);
                    propagateBackward(TRAIN_output[TRAIN_output.length-1-batch]);
                    
                    if(i%(iterations/10)==0){
                        errorTotal+=calcError(layerActivations.get(layerActivations.size()-1), TRAIN_output[TRAIN_input.length-1-batch]);
                        checkError(errorTotal);
                    }
                    updateWeights(TRAIN_input.length/BATCHES+1, normalizedGradientDescent);
                    updatebiases(TRAIN_input.length/BATCHES+1, normalizedGradientDescent);
                }else{
                    updateWeights(TRAIN_input.length/BATCHES, normalizedGradientDescent);
                    updatebiases(TRAIN_input.length/BATCHES, normalizedGradientDescent);
                }
            }
//            
//            if(i%(iterations/10)==0){
//                System.out.println("iteration: " + i); 
//                System.out.println("average error: " + errorTotal/TRAIN_input.length + "\n");
//            }
        }
    }
    
    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * TESTING NEURAL NETWORK
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void test(double[][] _TEST_input, double[][] _TEST_output){
        
        double[][][] TEST_input = DATAmanipulation(_TEST_input);
        double[][][] TEST_output = DATAmanipulation(_TEST_output);
        
        System.out.println("Starting TESTS on Testdataset... \n");        
        double CumulativeERROR = 0;
        for(int i=0; i<TEST_input.length; i++){
            propagateForward(TEST_input[i]);
            double inputError = calcError(TEST_output[i], layerActivations.get(layerActivations.size()-1)); //calc error
            CumulativeERROR += inputError; // add error to totalError
            
            checkError(CumulativeERROR);
            checkError(inputError);
            
            System.out.println("test "+i+"..............");// output stats
            System.out.println("error: "+ inputError);
            System.out.println();
        }
        
        System.out.println("average error: " + CumulativeERROR/TEST_input.length+"\n\n");
    }

    /**
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     * 	 PREDICT
     * !TODO : return the predicted vector instead of printing it .. 
     * --------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void predict(double[][] _unseen_input){
        double[][][] unseen_input = DATAmanipulation(_unseen_input);
        
        System.out.println("predicting... \n");
        for(int i=0; i<unseen_input.length; i++){
            propagateForward(unseen_input[i]);
            
            System.out.println("prediction "+i+"..............");
            if(unseen_input[i].length*unseen_input[i][0].length < maxCharsPerOutput){
                System.out.println("input: "+ Matrix.toStringOneLine(unseen_input[i]));
            }else{
                System.out.println("input: too long to display");
            }
            System.out.println("predicted output: "+ Matrix.toStringOneLine(layerActivations.get(layerActivations.size()-1)));
            System.out.println();
        }
        System.out.println();

    }


}


