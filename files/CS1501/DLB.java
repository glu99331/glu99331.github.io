import java.util.*;
/**************************************************************************
 * ======================= DLB Starter Code: ============================ *                                                     
 *------------------------------------------------------------------------*
 * ========================= Description: =============================== *                                                          
 * -----------------------------------------------------------------------*
 * A De La Briandais Trie is a data structure that can be                 *
 * used to optimally search through items by encoding items               *
 * as paths rather than encoding objects in each node.                    *
 *------------------------------------------------------------------------*
 * Rather than using a R-way Trie, or a Ternary Search Trie,              *
 * both of which yield runtime akin to wRn, are suboptimal                *
 * due to their array-backed structure.                                   *
 * -----------------------------------------------------------------------*
 * A DLB is backed by a linked list, which allocates memory               *
 * as continguous blobs of data, and is resizing in nature.               *
 * -----------------------------------------------------------------------*
 * The runtime of a DLB is akin to wR, therefore, in considering          *
 * a RST, TST, or a DLB, consider sparse vs dense data.                   *
 * -----------------------------------------------------------------------*
 * We refer to data as sparse if it tends to be long and non-discrete.    *
 * - I.e., long and not randomized.                                       *
 *------------------------------------------------------------------------*
 * We refer to data as dense if it tends to be short, and randomized.     *
 * -----------------------------------------------------------------------*
 * For the purposes of this assignment, we are utilizing a DLB to         *
 * generate predictions from a dictionary, and the user's history.        *
 * -----------------------------------------------------------------------*
 * We can do so by utilizing a DLB, since the words from the              *
 * dictionary tend to be sparse.                                          *
 * - Therefore, rather than using an R-way Trie which will                *
 * waste a lot of memory overhead, a DLB will optimally preserve          *
 * a reasonable runtime.                                                  *
 *************************************************************************/

public class DLB
{
    /************************************************************************
     * ======================== Inner Node Class ========================== *
     * ---------------------------------------------------------------------*
     * Since the DLB is Linked-List backed, we're gonna have nodes to help  *
     * encode our paths.                                                    *
     * ---------------------------------------------------------------------*
     * Specifically, in a DLB, we know that each node will have:            *
     * I) Data (which are implicitly stored as keys)                        *
     * II) A sibling node                                                   *
     * III) A child node                                                    *
     *----------------------------------------------------------------------*
     * Therefore, we must define an inner class for a node.                 *
     ***********************************************************************/
    
    //Inner Node class
    class Node
    {
        //TODO: Fill in the code to create instance fields of the data that
        //each node should store.
        public Node()
        {
            //TODO: Fill in the corresponding code to instantiate a single node.
        }
    }
    
    /************************************************************************
     * ====================== Default Constructor: ======================== *       
     * -------------------------------------------------------------------- *
     * Our DLB needs some way method of initialization.                     *
     *                                                                      *
     * If the DLB is about to be created, we need to initialize its root.   *
     * - Just like in a Red-Black, AVL tree or a BST.                       *
     *                                                                      *
     * Since no words have been inserted, just set the root to null.        *
     * - We must therefore declare a field for the root node.               *
     ***********************************************************************/

     Node rootNode; //Declaration of rootNode.

     //Default constructor for DLB, don't need to pass anything in.
     public DLB()
     {
         rootNode = null;   //Initialize the rootNode to null, we don't have 
                            //anything in the DLB yet!
     }

    /************************************************************************
     * ========================== DLB Methods: ============================ *
     * -------------------------------------------------------------------- *
     * ============== Our DLB should have three main methods: ============= * 
     * ---------------------------------------------------------------------*
     * I) A method to add a word to the DLB.                                *
     * II) A method to search the DLB for a certain string.                 *
     * III) A method to build predictions from a given string.              *
     ***********************************************************************/ 
    
    /************************************************************************
     * =========================== Add Method ============================= *
     * -------------------------------------------------------------------- *
     * This method will serve to insert words into the DLB.                 *
     * - The function signature does not matter.                            *
     * - For testability, I would recommend using a boolean function.       *
     ***********************************************************************/
    
     public boolean add(String word)
     {
        //TO DO: Fill in the code to add a word to the DLB.

        /*******************************************************************
         * ========================== Hints: ============================= *
         * --------------------------------------------------------------- *
         * =========== You should consider the following cases: ========== *
         * I) What if the DLB is empty?                                    *
         * --------------------------------------------------------------- *
         * II) What if the DLB is not empty?                               *
         * --------------------------------------------------------------- *
         * ========= You can split Case II into several subcases: ======== *                   
         * --------------------------------------------------------------- *
         * I) When can I create a child node?                              *
         * --------------------------------------------------------------- *
         * II) When do I just need to traverse children until I get to a   *
         * place where I can create a child node?                          *
         * --------------------------------------------------------------- *
         * III) When can I create a sibling node?                          *
         * --------------------------------------------------------------- *
         * IV) When do I just need to traverse siblings until I get to a   *
         * place where I can crate a sibling node?                         *
         * --------------------------------------------------------------- *
         ******************************************************************/

        return false;
     }

    /************************************************************************
     * ===================== Testing the Add Function: ==================== *
     * -------------------------------------------------------------------- *
     * After you finish writing the add method, I would recommend testing   *
     * that it actually worked, refer to testDLB.java to see an             *
     * implementation of testing insertion.                                 *
     * -------------------------------------------------------------------- *
     * This yields a false positive, since we don't actually know if insert *
     * works until we finish search!                                        *
     ***********************************************************************/

     /***********************************************************************
     * ========================== Search Method =========================== *
     * -------------------------------------------------------------------- *
     * This method will serve to search for a given word in the DLB.        *
     * - The function signature does not matter.                            *
     * - For testability, I would recommend using a boolean function.       *
     ***********************************************************************/

     public boolean search(String word)
     {
         //TO DO: Fill in the code to search for a given word to the DLB.
         //This method should be fundamentally similar to the add method,
         //Rather than adding, you are merely traversing the DLB to determine 
         //whether or not a word is in the DLB

        /*******************************************************************
         * ========================== Hints: ============================= *
         * --------------------------------------------------------------- *
         * =========== You should consider the following cases: ========== *
         * I) When can I traverse children?                                *
         * --------------------------------------------------------------- *
         * II) When can I traverse siblings?                               *
         * --------------------------------------------------------------- *
         * III) How do I compare the characters I traverse to the String   *
         * that I pass in?                                                 *
         * --------------------------------------------------------------- *
         ******************************************************************/

         return false;
     }

     /***********************************************************************
     * =================== Testing the Search Function: =================== *
     * -------------------------------------------------------------------- *
     * After you finish writing the search method, I would recommend        *
     * testing that it actually worked, refer to testDLB.java to see an     *
     * implementation of testing search.                                    *
     * -------------------------------------------------------------------- *
     * ========= Reasons why search fails includes the following: ========= *                    
     * ---------------------------------------------------------------------*
     * I) Insert didn't work properly.                                      *
     * II) Search prematurely stopped.                                      *
     * III) Search dove too deep into the DLB                               *
     ***********************************************************************/


     /***********************************************************************
     * ==================== Build Predictions Method ====================== *
     * -------------------------------------------------------------------- *
     * This method will build up a list of predictions based on passed in   *
     * word.                                                                *
     * - Essentially you will be returning some data structure              *
     ***********************************************************************/

     public ArrayList<String> generate_predictions(String word)
     {
        //TO DO: Fill in the code to fill up some data structure with 
        //predictions based on a given word.

        //Note: This is a difficult function, don't surprised if it doesn't
        //work right away!

        /*******************************************************************
         * ========================== Hints: ============================= *
         * --------------------------------------------------------------- *
         * ===== You can break this function up into the following: ====== *
         * --------------------------------------------------------------- *
         * I) First, navigate to the node that the given word starts at.   *
         * --------------------------------------------------------------- *
         * II) Then fill in some data structure with predictions using the *
         * anchor point determined by navigation to that node.             *
         * --------------------------------------------------------------- *
         * You can split Case II into the following:                        *
         * I) How can I use the properties of trees & tries to traverse    *
         * through the DLB?                                                *
         * --------------------------------------------------------------- *
         * II) Once I've figured out what CS technique helps me, what are  *
         * some cases I can consider?                                      *
         ******************************************************************/
    
        return null;
     }
}