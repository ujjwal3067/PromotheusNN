---
title: Prometheus
created: '2020-09-01T13:18:00.215Z'
modified: '2020-09-01T18:48:42.618Z'
---

# Prometheus
## NN Team Code:

- Creating a Neural Network Node : 

  -  use : `NN NODE = new NN(structure)` ---Where Structure is int[] array to specify the Neural Network structure. 
  - NN class constructor takes : 
    - param : 
      - **NN_config**  type =>`int[]`
      - **learning Rate** ( *Default = 1*) => `double`
  - For example : 
      - `int[] Structure = {7, 7, 7,7,1}`  will create a a Neural Network Node with &  Neurons in each layer , including input layer and 1 neuron in  output layer. 
  -  use : `		NN.VALID_input_checker(Training inputs, Traingin outputs)` for validating the structure of input dataset for before feeding it to `TRAIN()` function. 
  - use : `NN.TRAIN(trainIn, trainOut, iteration , batches, normlization)` for training the Neural Netowrk Node. 
      - param : 
        - `trainIn`       : Data input for training purpose 
        **type** => `double[][]`
        - `trainOut`      : Data output for error check and updating weights
        **type** => `double[][]`
        - `iteration`     : number of iteration for training
        **type** => `int`
        - `batches`       : number of dataset batches to create for batch training
        **type** => `int`
        - `normalization` : True for normalized value of gradient
        **type** => ` boolean`
   - use : `NN.TEST( testIn, testOut)` for testing the Neural Network node.
      - param : 
        - `testIn` : Data input for testing purpose
        **type** => `double[][]`
        - `testOut` : Data output for calculating the error rate
        **type** => `double[][]`
  - use : `NN.PREDICT(Unseen data` to predict the output of new data instance. 
      - param : 
        - ` Unseen_Data` :  Unseen data instance
        **type** => `doulbe[][]`
      - output : 
        -  return type => `ArrayList<double[][]>`
          *note : can be changed to `double[][]` if needed*


  NOTE : Each NN class instance saves it's own stats for Neural Network structure. Each NN node can have custom structure `(# layers, #n neurons)`. 
  
  Outputs from different NN nodes can be used as inputs for other already trained NN nodes.
  NN class makes this trivial buy matching the data strucutre for input and output of a NN nodes. `( input => double[][] and output => doulbe[][])`




### Major Internals Methods of NN class : 
- `NN.FORWARD_PROPAGATION(input)` : used for forward propagation in the neural network
  - **input**  param type => `double[][]`
- `NN.BACKWARD_PROPAGATION( output)` : used for backward progagtion in the neural network and updating weights based on error rate. 
  -  **output** param type => `double[][]`
- `NN.NN_INIT(NN_config , random)` : used for Initializing the Neural Network
  - param type : 
    - **NN_config** type => `int[]`
    - **rand** type => `Random`

## Vector Class Representation : 
- `VECTOR class` : represents Matrix class for some matrix algebra calculation
  - Available Methods : 
    - `transpose`
    - `multiply`
    - ` getRow`
    - `getCol`
    -  `dotProduct`
    - `add`
    - `subtract`
    - `normalize`
    - `randomizeArray`

## UltraSonic Data representation : 
- A Data Generator class for Ultrasonic Sensor
    - `Column1` => Distance detected by Sensor1
    - `Column2` => Distance detected by Sensor2
    - `Column3` => Distance detecetd by  Sensor3
    - `Column4` => Navigational output : 
        - `1`  => *Turn left* 
        - `2`  => *Go Straight* 
        - `3`  => *Turn Right*
- Note  : `Assumptions` :  There are three sensor in front of the robot and obstacle is visible in one sensor at a time ( for ease of data creation )
- ### Cases : 
  - `Case 1` : Object is straight  ahead of the sensor at distance L 
    Object is visible in all 3 sensors ( Object is close to robot's all 3 sensors)
    `10.0-18.0` units with step  4.0 units -> 50 measurements
    `Navigational output` : 3 - *Turn Right* 

  - `Case 2` : Object is straight  ahead of the sensor at distance L 
    Object is visible in all 3 sensors( Object is far from robot's all 3 sensors)
    `20.0-145.0` units with step  25.0 units -> 50 measurements
    `Navigational output` : 2 - *Go Straight*  

  - `Case 3` : Object is left side of the robot at distance L 
  Object is visible only in the left most sensor ( Object is close to robot's leftmost sensors)
  `3.0-18.0` unit with step 5.0 unit -> 50 measurements
  `Navigational output` : 3 - *Turn Right* 

   - `Case 4` : Object is left side of the robot at distance L 
  Object is visible only in the left most sensor ( Object is far from robo's leftmost sensors')
  `20.0-70.0` unit with step 10.0 unit -> 50 measurements
  `Navigational output` : 2- *Go Straight* 

  - `Case 5` : Object is right side of the robot at distance L
  Object is visible only in the rightmost sensor ( Object is close to the  robot's rightmost sensor)
  `3.0-18.0` unit with step 5.0 unit -> 50 measurements
  `Navigational ouput` : 1 - *Turn Left*

  - `Case 6` : Object is right side of the robot at distance L
  Object is visible only in the rightmost sensor ( Object is far from the  robot's rightmost sensor)
  `20.0-70.0` unit with step 5.0 unit -> 50 measurements
  `Navigational ouput` : 2 - *Go Straight*
  













