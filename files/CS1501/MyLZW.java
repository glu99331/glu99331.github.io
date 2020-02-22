import java.util.*;

/******************************************************************
* @author: [Your Name]                                            *
* @email: yourusername@pitt.edu                                   *
*                                                                 *
* Table of Contents:                                              *
* 1) Project Overview                                             *
* 2) Description LZW.java                                         *
* 3) Description of BinaryStdIn.java                              *
* 4) Description of BinaryStdOut.java                             *
* 6) Description of TST.java                                      *
* 7) Description of Queue.java                                    * 
* 8) Implementation of Hybrid LZW Compression                     *
*                                                                 *
******************************************************************/

/*<~---------------------------------@documentation, @section: Project Overview:------------------------------------------~>*/
/*************************************************************************************************************************
  * <p><b>Author:</b> Your name </p>
  * <p>1) Implement LZW variable-width code words (dynamically increase the size of codewords as dictionary fills up)
  * </p>                                                                                                             
  * 
  * <p>2) Once the dictionary is filled up, the algorithm can perform one of the following options:  
  *  <ul>                   
  * <li> Stop adding patterns </li>                                                                                       
  * <li> Continue compression with only patterns encountered </li>                                                           
  * <li> Reset codebook to find new patterns                 </li>  </ul> </p>                                                                                                                                                                                  
  * <p>The code provided by the Textbook authors (Robert Sedgewick and Kevin Wayne)  
  * <ul><li>Simply continues to use patterns that have already been added to the codebook</li></ul> </p>                                                          
  *
  *
  * <p><b>Purpose/Goal of the Project: </b>                                                                                    
  * 
  * <p>1) Modify the LZW source code provided by Robert Sedgewick and Kevin Wayne, to utilize           
  * variable-width codewords, and optionally reset the codebook under <b><i>CERTAIN CONDITIONS  </b></i>                           
  * <p>2) Once the said changes have been implemented, compare the performance of Hybrid LZW Compression code to the   
  * initial LZW code, and juxtapose the Hybrid LZW Compression code with a widely used Compression application of    
  * choice. </p>                                                                                                          
  *            
*************************************************************************************************************************/    

public class MyLZW
{
        /*<~-------------------------------------------ANSI_COLOR_CODES------------------------------------------------------>   
        *  The purpose of declaring the ANSI Color Codes is to aesthetically print out some usage box when the user does not *
        *  enter the correct arguments.                                                                                      *
        *                                                                                                                    *
        *  Sample Usage: System.out.println(ANSI_RED + "Usage of Arguments is like the following: " + ANSI_RESET + "\n" +);  *
        *                                                                                                                    *
        *  The variables are global to ensure when a function uses any of the ANSI color codes, there won't be a             *
        *  problem with the scope of the program                                                                             *
        *                                                                                                                    *
        <~-------------------------------------------ANSI_COLOR_CODES------------------------------------------------------>*/ 
        //You should probably comment these out once you're done debugging your code!
        static final String ANSI_RESET = "\u001B[0m";
        static final String ANSI_BLACK = "\u001B[30m";
        static final String ANSI_RED = "\u001B[31m";
        static final String ANSI_GREEN = "\u001B[32m";
        static final String ANSI_YELLOW = "\u001B[33m";
        static final String ANSI_BLUE = "\u001B[34m";
        static final String ANSI_PURPLE = "\u001B[35m";
        static final String ANSI_CYAN = "\u001B[36m";
        static final String ANSI_WHITE = "\u001B[37m";
        static final String ANSI_UNDERLINE = "\u001B[4m";

        /*<~-----------------------------------------------Globals----------------------------------------------------------->   
        * Purpose: Each global in this section is necessary for LZW compression to properly function...                      *
        *                                                                                                                    *
        * ASCII_LENGTH: Refers to the ASCII alphabet, each character maps to a value from 0 to 255.                          *
        *                                                                                                                    *
        * MIN_CODEWORD_WIDTH: Refers to the minimum codeword width, as described in the project description                  *                                                                                                                   
        *                                                                                                                    *
        * MAX_CODEWORD_WIDTH: Refers to the maximum codeword width, if the codeword width is currently at this width, and    * 
        * there are still codewords to add to the codebook, the code will be required to perform a reset...                  *                                                                                                 
        *                                                                                                                    *
        * MAX_THRESHOLD: Refers to the threshold in which to reset ratios, codeword width, and codebook width. Will only     *
        * occur during monitor mode. If the ratio between initialRatio and currentRatio exceed this threshold, the program   *
        * will perform a reset, along with a reset of ratios.                                                                *                                                    
        *                                                                                                                    *
        * VW: Refers to the initial codeword width: It will begin at 9, and continually expand if the codebook does not have *
        * sufficient capacity, until it reaches the max of 16.                                                               *
        *                                                                                                                    *
        * L: Refers to the number of codewords, calculated as 2^W, this is necessary to determine the instances in which the *
        * the codebook needs to be resized, in addition to the conditions on which to reset, monitor, and set the maximum    *
        * capacity of the String Array to store the codewords for expansion..                                                *                                                                                                                                       
        *                                                                                                                    *
        * mode: Indicates the type of mode the User seeks to invoke on files.                                                *                                                                                                                    
        *                                                                                                                    *
        * compressionFlag: Indicates the type of mode the User entered during compression.                                   *
        *                                                                                                                    *                                                                                                                                                                                          
        <~-----------------------------------------------Globals----------------------------------------------------------->*/ 

        static final int ASCII_LENGTH = 256;        // number of input chars
        static final int MIN_CODEWORD_WIDTH = 9;    // min codeword width
        static final int MAX_CODEWORD_WIDTH = 16;   // max codeword width
        static final double MAX_THRESHOLD = 1.1;    // threshold to determine reset 

        static int VW = MIN_CODEWORD_WIDTH;         // codeword width initially begins at 9
        static int L = (int)Math.pow(2, VW);        // number of codewords = 2^W
        
        static String mode;                         // indicates the mode the user enters
        static char compressionFlag;                // during expansion, will determine what the user entered during compression...

        //You should probably get rid of the comments below out once you' understood what each of the files does!

        /* 
        <~---------------------------------@documentation, @section: LZW.java Overview:------------------------------------~>
        *  Compilation:  javac LZW.java                                                                                     *
        *  Execution:    java LZW - < input.txt   (compress)                                                                *
        *  Execution:    java LZW + < input.txt   (expand)                                                                  *
        *  Dependencies: BinaryStdIn.java BinaryStdOut.java                                                                 *
        *  - Utilizes BinaryIn.java and BinaryOut.java in order to shift bits and store output in file                      *
        *                                                                                                                   *
        *  Compress or expand binary input from standard input using LZW.                                                   *
        *                                                                                                                   *
        <~---------------------------------@documentation, @section: LZW.java Methods:-------------------------------------~>
        * compress:                                                                                                         *           
        * 1) Compresses using FIXED-WIDTH codewords...                                                                      *                                                                                                                  
        * 2) Takes in the bytes from input as a string ~> input = BinaryStdIn.readString()                                  *                                                                                                                                                                                                     
        * 3) Fills a TST with the codewords ~> Making it the codebook                                                       *                                                           
        * 4) Initializes a variable to indicate the EOF, since R + 1 -> would exceed the ASCII length...                    *                                                                                               
        * 5) Loop through the input, and add codewords to the ST until there are no more characters to add from the input   *                                                                                                               
        * 6) Afterwords, Write r-bit integer to standard output, optimally write if word aligned, otherwise write one at a  *
        * time.                                                                                                             *                                                                                                                  
        *                                                                                                                   *
        * expand:                                                                                                           *
        * 1) Since, number of codewords has already been predetermined following compression, use a String[], rather than   *                                                                                                                  
        * TST. Initialize it to length of 2^W (I.e., L ~> String[] st = new String[L]);                                     *                                                                                                                                                                                                                                    
        * 2) Fill the String[] with all (0 to 255) ASCII characters.                                                        *                                                                                                                   
        * 3) Read the expanded message from BinaryStdIn, and put it into the String[]                                       *                                                                         
        * 4) Write out the codeword to StdOut until there isn't anything left to write...                                   *                                                                               
        *                                                                                                                   *                                                                                                                 
        <~******************************************************************************************************************~>
        /*
    
        <~******************BinaryStdIn.java*******************~>
        *-------------------Basic-Synposis----------------------*
        *  Compilation:  javac BinaryStdIn.java                 *
        *  Execution:    java BinaryStdIn < input > output      *
        *                                                       *        
        *  Supports reading binary data from standard input.    *
        *                                                       *
        *  Sample Execution of BinaryStdIn.java:                *
        *  - java BinaryStdIn < input.jpg > output.jpg          *
        *  Sample diff input.jpg output.jpg                     *
        *                                                       *
        *-------------------------------------------------------*
        *---------------Purpose-of-BinaryStd.java---------------*
        * Provides methods for reading in bits from standard    *
        * input, as one of the following...                     *
        *-------------------------------------------------------*                                                      
        * 1) One bit at a time (as a boolean)                   *
        * 2) 8 bits at a time (as a byte or a char)             *
        * 3) 16 bits at a time (as a short)                     *
        * 4) 32 bits at a time (as an int or a float)           *
        * 5) 64 bits at a time (as a double or a long)          *
        *                                                       *
        *-------------------------------------------------------*
        *                                                       *                                                      
        *----Assumptions regarding in-memory representation:----*    
        * All primitives are assumed to be represented using the*
        * standard Java representation, i.e., big-endian, (the  *
        * significant byte first)                               *
        *-------------------------------------------------------* 
        *                                                       *                                                     
        *---------------Notes-Regarding-Client-Usage------------*
        * When implementing BinaryStdIn, the client should not  *
        * mix up System.in with StdIn, doing so will result in  *
        * unexpected behavior!                                  *
        *                                                       *
        *-------------------------------------------------------*
        *                                                       *
        *--------------------Method-Analysis--------------------*
        * This part serves to analyze each of the methods in    *
        * BinaryStdIn.java.                                     *
        *                                                       *
        *                  <----fillBuffer--->                  *
        * Description: Attempts to fill buffer with 8 bits from *
        * Standard Input...                                     *
        *                  <------close------>                  *
        * Description: Closes the associated input stream       *
        *                  <-----isEmpty----->                  *
        * Description: Determines if standard input is empty!   *
        *                  <----The-Read-Methods-->             *
        * Description: Will take in n bits, and return an n bit *
        * data type, based on data passed in!                   *
        * Example: readString will ready bytes of data, and     *
        * will return a String                                  *
        <~*****************************************************~>

        <~---------------------------------@documentation, @section: BinaryStdOut.java Overview:------------------------------------~>
        *  Compilation:  javac BinaryStdOut.java                                                                                     *
        *  Execution:    java BinaryStdOut                                                                                           *
        *                                                                                                                            *
        *  Write binary data to standard output, either one 1-bit boolean, one 8-bit char, one 32-bit int, one 64-bit double, one    *
        *  32-bit float, or one 64-bit long at a time.                                                                               *
        *                                                                                                                            *
        *  The bytes written are not aligned.                                                                                        *
        *                                                                                                                            *
        <~---------------------------------@documentation, @section: BinaryStdOut.java Description:---------------------------------~>
        * Provides methods for converting primitives to sequences of bits (bitstreams) and writing them to standard output.          *
        *                                                                                                                            *                                                                                                                           
        * Operates under the assumption of Big-Endian (MSB first)                                                                    *                                                       
        *                                                                                                                            *
        * Upon completion of writing bits, user must flush the output stream                                                         *                                                                                                                           
        *                                                                                                                            *
        * The client should avoid intermixing StdOut and BinaryStdOut, as StdOut processes character streams, while Binary StdOut    *                                                                                                                           
        * will process bit/bytestreams                                                                                               *
        *                                                                                                                            *
        <~---------------------------------@documentation, @section: BinaryStdOut.java Methods:-------------------------------------~>
        * write Methods:                                                                                                             *                                                                        
        * 1) Generally take in n bits/bytes and writes it out to standard output.                                                    *                                                                       
        *                                                                                                                            *
        * clearBuffer:                                                                                                               *                                                                                                                                  
        * 1) Writes out any remaining bits in buffer to standard output, padding with 0s (similar to zero extending)                 *                                                                                                                          
        *                                                                                                                            *
        * flush:                                                                                                                     *      
        * 1) Flushes standard output, padding 0s if number of bits written so far is not byte-aligned.                               *
        *                                                                                                                            *
        * close:                                                                                                                     *       
        * 1) Flush and close standard output. Once standard output is closed, access to write bits is prohibited.                    *                                                                                                                                                                                                    
        *                                                                                                                            *
        <~**************************************************************************************************************************~>                                                                                                                         

        <~---------------------------------------@documentation, @section: TST.java Overview:---------------------------------------~>
        * Dependencies: StdIn.java, Queue.java                                                                                       *
        *                                                                                                                            *
        * Purpose: During compression, we cannot take advantage of perfect hashing to generate a fixed size array. Since the string  *
        * that will have substring called on will contain relatively sparse data, it makes the data a perfect candidate to use       *
        * a Ternary Search Trie (TST).                                                                                               *                                                                                
        *                                                                                                                            *                                                                                                                           
        <~**************************************************************************************************************************~>

        <~-------------------------------------@documentation, @section: HybridTQueue.java Overview:--------------------------------~>
        * Description:                                                                                                               *
        * A generic queue, implemented using a linked list.                                                                          *                                                 
        *                                                                                                                            *
        <~**************************************************************************************************************************~>
    */

    //How could I create a substring method to speed up the runtime of our current method?
    //HINT: String Pattern Matching: Aho-Corasick, Rabin-Karp, KMP, or Boyer Moore. 
    //Additional Hint: You could also try to modify the current String class and take more of a runtime specific approach, rather than the current String class approach
    //which prioritizes memory instead.
    public static void compress(String compressionMode) { 
        //Since we're going to execute compression then expansion, we need to send the mode the user writes to the StdOut buffer, otherwise, we don't know
        //what the user entered on the next iteration...
        BinaryStdOut.write(compressionMode, 8); //Write out compressionMode to BinaryStdOut so during expansion, we can determine what the user entered during compression!
        String input = BinaryStdIn.readString();    //Read in bytes as a String....
        //We need to use some sort of sparse data structure to store our codewords, since we don't know the length of the file ahead of time...
        HybridTST<Integer> st = initializeHybridTSTCodeBook();  //TST to store codewords: It's the codebook!
        //Insert 0-255 chars into the TST...
        int code = ASCII_LENGTH+1;  // R is codeword for EOF
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), VW);      // Print s's encoding.

            int t = s.length();      
            //This condition was provided..     
            if (code < L && t < input.length())    // Add s to symbol table, and 2^W is less than or equal to the codeword
            {
                st.put(input.substring(0, t + 1), code++);  //add new codeword to the TST
            }
            //This condition is clearly not correct: under what correct conditions would I need to resize 
            else if(VW != MAX_CODEWORD_WIDTH))
            {
                //What do I need to do with my codebook and the number of codewords when I need to resize?
            }
            //Under what conditions do I need to reset the codebook?
            else if(compressionMode.equals("r"))
            {
                //This line is for debugging: It will print out when you get into this block of code!
                System.err.println(ANSI_RED + "Resetting " + ANSI_RESET + ANSI_BLUE+ "CodeBook" + ANSI_RESET);
                //What do I need to do with my codeword length and total number of codewords?
               
            }
            else if(compressionMode.equals("m")) //Under what conditions will I need to perform monitor mode?
            {
                //I also need to reset the codebook under some condition, which is dependent on some ratios, so how do I calculate those ratios, and how do they play in?
            }
           
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(ASCII_LENGTH, VW);   //Write r-bit integer to standard output, optimally write if word aligned, otherwise write one at a time.
        BinaryStdOut.close();
    } 

    //You must maintain dynamicity and sychronicity across both expansion and compression! 
    //If expansion doesn't work, it could still mean compression is wrong!!
    //Inherited from LZW.java
    static void expand() {
        //read in the 8 bits we previous wrote to BinaryStdIn
        compressionFlag = BinaryStdIn.readChar(8);       
        //We can take advantage of perfect hashing since we determined the total number of codewords from compression!
        String[] st = initializeArraySTCodeBook();
        int i = ASCII_LENGTH + 1; // next available codeword value

        // initialize symbol table with all 1-character strings
        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(VW);
        if (codeword == ASCII_LENGTH) return;           // expanded message is empty string
        String val = st[codeword];

        while (true) {
            //This condition should be the same thing as from compression, we just want to maintain synchronicity!
            //This condition is clearly not correct: under what correct conditions would I need to resize 
            if(VW != MAX_CODEWORD_WIDTH))
            {
                //What do I need to do with my codebook and the number of codewords when I need to resize?
                //What do we know about arrays and their size?
                //HINT: We didn't need to do this in compression, since TSTs are dynamically resizing!
            }
            //This condition should be the same thing as from compression, just for synchronicity!
            //Under what conditions do I need to reset the codebook?
            else if(compressionMode.equals("r"))
            {
                //This line is for debugging: It will print out when you get into this block of code!
                System.err.println(ANSI_RED + "Resetting " + ANSI_RESET + ANSI_BLUE+ "CodeBook" + ANSI_RESET);
                //What do I need to do with my codeword length and total number of codewords?
               
            }
            //This condition should be the same thing as from compression, just for synchronicity!
            //Under what conditions will I need to perform monitor mode?
            else if(compressionMode.equals("m")) 
            {
                //I also need to reset the codebook under some condition, which is dependent on some ratios, so how do I calculate those ratios, and how do they play in?
            }
            //We have to do all the writes after resizing and correctly resetting, otherwise, we'll be writing out the wrong values!!!
            BinaryStdOut.write(val);        //Write out codeword to standard output
            codeword = BinaryStdIn.readInt(VW);  //Read the written codeword
            if (codeword == ASCII_LENGTH) break;       //If the value is equal to the max value of the ASCII value (256), break...
            String s = st[codeword];       
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            
            if (i < L) {              // i corresponds to the next codeword's value???
                st[i++] = val + s.charAt(0);
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) throws Exception
    {
        /*************************************************************************************************************************** 
        * 3 Compression Modes:                                                                                                     *
        * 1) Do Nothing mode: Do nothing and continue to use the full codebook (implemented by LZW.java)                           *
        *                                                                                                                          *
        * 2) Reset mode: Reset dictionary back to initial state, so new codewords can be added.                                    *
        * - Make sure both compression and expansion are both in sync!                                                             * 
        *                                                                                                                          *         
        * 3) Monitor mode: Initially do nothing, but begin monitoring the compression ratio whenever the codebook is filled.       *
        * - Compression Ratio -> (uncompressed data size) / (compressed data size)                                                 *
        * - If compression ratio > 1.1 -> reset dictionary back to initial state                                                   *
        * - Ratio of Ratios -> [old/new] -> old is ratio last recorded when program last filled the codebook, new is current ratio *
        * - Make sure to coordinate code for BOTH compression and expansion!!!                                                     * 
        ***************************************************************************************************************************/

        /*****************************************************************************************************************
        * Simple try-catch block to ensure user input matches criteria of project description...                         *
        * - If there is any exception, ignore it, and display what the user should enter in as valid input!              *
        *                                                                                                                *
        *****************************************************************************************************************/
        try
        {
             /*********************************************************************************************************************
             * If the user enters - followed by one of the characters "n", "r", or "m", perform the desired mode.
             * 1) "- n" indicates that the user wants to perform compression with Do Nothing mode...
             * 2) "- r" indicates that the user wants to perform compression with Reset mode...
             * 3) "- m" indicates that the user wants to perform compression with Monitor mode...
             *********************************************************************************************************************/
            
             if(args[0].equals("-") && args[1].equals("n") && args.length == 2) //Do Nothing mode
             {
                /******************************************************************************************************************
                * Initialize mode to what the user entered as the second argument. In this case, it is the compression mode,      *
                * which is Do Nothing mode.                                                                                       *
                *                                                                                                                 *
                * Then pass in the mode, so the compress function can perform the correct functionality based on the compression  *
                * mode entered!                                                                                                   *
                *                                                                                                                 *
                * Note: Compressing an already compressed file will degrade the file content.                                     *
                *                                                                                                                 *
                ******************************************************************************************************************/
                mode = args[1];
                compress(mode); 
             }
             else if(args[0].equals("-") && args[1].equals("r") && args.length == 2) //Reset mode
             {
                /******************************************************************************************************************
                * Initialize mode to what the user entered as the second argument. In this case, it is the compression mode,      *
                * which is Reset mode.                                                                                            *  
                *                                                                                                                 *
                * Then pass in the mode, so the compress function can perform the correct functionality based on the compression  *
                * mode entered!                                                                                                   *
                *                                                                                                                 *
                * Note: Compressing an already compressed file will degrade the file content.                                     *
                *                                                                                                                 *
                ******************************************************************************************************************/
                mode = args[1];
                compress(mode);
             }
             else if(args[0].equals("-") && args[1].equals("m") && args.length == 2) //Monitor mode
             {
                /******************************************************************************************************************
                * Initialize mode to what the user entered as the second argument. In this case, it is the compression mode,      *
                * which is Monitor mode.                                                                                          *
                *                                                                                                                 *
                * Then pass in the mode, so the compress function can perform the correct functionality based on the compression  *
                * mode entered!                                                                                                   *
                *                                                                                                                 *
                * Note: Compressing an already compressed file will degrade the file content.                                     *
                *                                                                                                                 *
                ******************************************************************************************************************/
                mode = args[1]; 
                compress(mode);
             }
             else if(args[0].equals("+") && args.length == 1) //Expansion
             {
                /******************************************************************************************************************
                * If the user enters a "+" as the first argument, this indicates that the user wants to expand a Compressed File. *
                *                                                                                                                 *
                * Note: Expansion will not be effective on the original file contents.                                            *
                *                                                                                                                 *
                ******************************************************************************************************************/
                expand();
             }
             else
             {
                /******************************************************************************************************************
                * Otherwise, assume that the user has entered the wrong input, and display two boxes:                             *
                *                                                                                                                 *
                * ErrorBox: Lists the correct way to run the MyLZW program...                                                     *
                *                                                                                                                 *
                * SampleBox: Outputs a sample execution of the MyLZW program...                                                   *
                * - It will also output suggestions of commands that may be helpful to pair along with the MyLZW program.         *
                *                                                                                                                 *
                ******************************************************************************************************************/
                errorBox();
                sampleBox();             
             }
        }
        catch(Exception e)
        {
            /******************************************************************************************************************
            * Otherwise, assume that an Exception has occured in the middle of running one of the compression modes or        * 
            * running expansion on a compressed file...                                                                       *
            *                                                                                                                 *
            * ErrorBox: Lists the correct way to run the MyLZW program...                                                     *
            *                                                                                                                 *
            * SampleBox: Outputs a sample execution of the MyLZW program...                                                   *
            * - It will also output suggestions of commands that may be helpful to pair along with the MyLZW program.         *
            *                                                                                                                 *
            ******************************************************************************************************************/
            errorBox();
            sampleBox();
        }

    }

    //Please only use these for testing: Do not submit your code with these error boxes.
    //Additionally, these ANSI colors are only compatible on Macs.

    static void errorBox()
    {
        System.err.println(ANSI_GREEN+"<~-------------------------------------------------------------------------------------------------------------------------------------------------------~>" + ANSI_RESET);
        System.err.print("\t" + ANSI_RED + ANSI_UNDERLINE + "USAGE:" + ANSI_RESET + ANSI_GREEN + " java " + ANSI_RESET);
        System.err.print( ANSI_RED + "MyLZW" + ANSI_RESET);
        System.err.print(ANSI_PURPLE + " [" + ANSI_BLUE + "COMPRESSION" + ANSI_RED + "/" + ANSI_GREEN + "EXPANSION" + ANSI_PURPLE + "]" + ANSI_RESET);
        System.err.print(ANSI_CYAN + " [" + ANSI_PURPLE + "MODE" + ANSI_CYAN + "]" + ANSI_RESET);
        System.err.print(ANSI_GREEN + " [" + ANSI_YELLOW + "INPUT REDIRECTION" + ANSI_GREEN + "]" + ANSI_RESET);
        System.err.print(ANSI_RED + " [" + ANSI_BLUE + "INPUT FILE" + ANSI_RED + "]" + ANSI_RESET);
        System.err.print(ANSI_GREEN + " [" + ANSI_YELLOW + "OUTPUT REDIRECTION" + ANSI_GREEN + "]" + ANSI_RESET);
        System.err.print(ANSI_BLUE + " [" + ANSI_RED + "INPUT FILE" + ANSI_BLUE + "]" + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_BLUE + ANSI_UNDERLINE + "DESCRIPTION:" + ANSI_RESET + ANSI_YELLOW + "\n\tImplement LZW variable-width code words " + ANSI_RED +  "(dynamically increase the size of codewords as dictionary fills up)");
        
        System.err.println();
        System.err.print("\n\t" + ANSI_CYAN + ANSI_UNDERLINE + "MyLZW:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_RED + "'MyLZW' is the program that will produce a hybrid LZW compression algorithm..." + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_GREEN + ANSI_UNDERLINE  + "COMPRESSION" + ANSI_RED + "/" + ANSI_BLUE + "EXPANSION:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_GREEN + "'-' indicates COMPRESSION on the user input...");
        System.err.print("\n\t" + ANSI_BLUE + "'+' indicates EXPANSION on the user input...");

        System.err.println();
        System.err.print("\n\t" + ANSI_PURPLE + ANSI_UNDERLINE + "MODE:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_GREEN + "'n' indicates to use the DO NOTHING mode with Compression..." + ANSI_RESET);
        System.err.print("\n\t" + ANSI_RED + "'r' indicates to use the RESET mode with Compression..." + ANSI_RESET);
        System.err.print("\n\t" + ANSI_BLUE + "'m' indicates to use the MONITOR mode with Compression..." + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_YELLOW + ANSI_UNDERLINE + "INPUT REDIRECTION:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_CYAN + "'<' will read the contents of the specified file..." + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_BLUE + ANSI_UNDERLINE + "INPUT FILE:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_GREEN + "'filename.type'" +  ANSI_RED + " will be the file that StdIn will read from..." + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_YELLOW + ANSI_UNDERLINE + "OUTPUT REDIRECTION:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_CYAN + "'>' will redirect the contents of the specified file, after performing operations from running the java file..." + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_RED + ANSI_UNDERLINE + "OUTPUT FILE:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_GREEN + "'filename.type'" +  ANSI_RED + " will be the file that will be created with the contents from StdOut..." + ANSI_RESET);

        System.err.println("\n" + ANSI_GREEN + "<~------------------------------------------------------------------------------------------------------------------------------------------------------~>" + ANSI_RESET);

    }
    static void sampleBox()
    {
        System.err.print("\t" + ANSI_RED + ANSI_UNDERLINE + "SAMPLE EXECUTION:" + ANSI_RESET);
        System.err.print("\n\t" + ANSI_GREEN + "The following statement will perform a Do Nothing Mode Compression on a File named " + ANSI_CYAN + "'foo.txt'" + ANSI_GREEN + " and redirect the output of compression to " + ANSI_RED + "'foo.lzw'" + ANSI_RESET);
        
        System.err.print("\n\t\t\t\t\t\t" + ANSI_GREEN + "java " + ANSI_RESET);
        System.err.print(ANSI_RED + "MyLZW " + ANSI_RESET);
        System.err.print(ANSI_BLUE + "- " + ANSI_RESET);
        System.err.print(ANSI_PURPLE + "r " + ANSI_RESET);
        System.err.print(ANSI_YELLOW + "< " + ANSI_RESET);
        System.err.print(ANSI_BLUE + "foo.txt " + ANSI_RESET);
        System.err.print(ANSI_YELLOW + "> " + ANSI_RESET);
        System.err.print(ANSI_RED + "foo.lzw " + ANSI_RESET);

        System.err.println();
        System.err.print("\n\t" + ANSI_GREEN + "The following statement will perform a expansion on the file, " +  ANSI_RED + "'foo.lzw'" + ANSI_GREEN + " and will expand the compressed file\n\tsuch that its size should equal " + ANSI_CYAN + "'foo.txt'" + " it will then send the expanded file to a new file named " + ANSI_PURPLE + "'foo2.txt'" + ANSI_RESET);
        
        System.err.print("\n\t\t\t\t\t\t" + ANSI_GREEN + "java " + ANSI_RESET);
        System.err.print(ANSI_RED + "MyLZW " + ANSI_RESET);
        System.err.print(ANSI_BLUE + "+ " + ANSI_RESET);
        System.err.print(ANSI_YELLOW + "< " + ANSI_RESET);
        System.err.print(ANSI_BLUE + "foo.lzw " + ANSI_RESET);
        System.err.print(ANSI_YELLOW + "> " + ANSI_RESET);
        System.err.print(ANSI_RED + "foo2.txt " + ANSI_RESET);

        System.err.println(ANSI_RED + "\n\tRunning the command, " + ANSI_GREEN + "'diff " + ANSI_CYAN + "foo.txt " + ANSI_PURPLE + "foo2.txt" + ANSI_GREEN + "'" + ANSI_RED + " will ensure contents are identical." + ANSI_RESET);
        System.err.println(ANSI_GREEN + "<~------------------------------------------------------------------------------------------------------------------------------------------------------~>" + ANSI_RESET);    }
    }

}