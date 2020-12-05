/*****************************************************************
* @author Gordon Lu                                              *
* @Email GOL6@pitt.edu                                           *
* @PSID 4191042                                                  *
*****************************************************************/
@SuppressWarnings({"unchecked", "rawtypes"})
public class RwayTrie<T>
{
    /***************************************************
    * An implementation of an Rway Trie                *
    * ================================================ *                                       
    * An Rway Trie is an array-based implementation of *
    * a large branching factor trie.                   *
    *                                                  *
    * The number of branches per node is contingent on *
    * the size of the alphabet of interest.            *
    ***************************************************/

    /**************************************************
     * <~-------------Global Variables:------------~> *
     * =============================================  *
     * root - Needed to provide base point to         *
     * insert and search for elements (need a place)  *
     * to start.                                      *
     *                                                *
     * RADIX - Defines the size of the alphabet of    *
     * interest.                                      *
     * - Essential to define the number of branches   *
     * per node.                                      *
     * ============================================== *
     *************************************************/
    private static int RADIX; //size of alphabet
    private Rway_Node<T> root;
    /*******************************************
     * Inner node class:
     * <ul>
     * <li>Contains array of branches based on
     * alphabet size</li> 
     * <li>The data stored with each node</li>
     * </ul>
     *******************************************/
    private static class Rway_Node<T>
    {
        private Rway_Node<T>[] next;
        private T data;
        /**************************************
         * Constructor to initialize a Node to
         * have RADIX number of branches.
         *************************************/
        public Rway_Node()
        {
            next = new Rway_Node[RADIX]; 
        }
    }    
    /***********************************
     * Initializes an empty Rway trie
     * with a given RADIX.
     **********************************/
    public RwayTrie(int R)
    {
        root = null;
        RADIX = R; 
    }

    /**********************************************************************************
     * This method will iteratively insert into an R way Trie based on the String key, 
     * and at the end of the key, inserts a node with a given value.
     * 
     * <p> There are several cases to consider for in insertion:</p>
     * <p><b>Base Case</b>: If nothing has been inserted, insert vertically!</p>
     * <p><b>Case I</b>: Check if the branch to take is null, if so, create it!</p>
     * <p><b>Case II</b>: Check if the branch to take is not-null, if so, traverse to it</p>
     * <p><b>Case III</b>: At the end of the String, so create the branch, and store the
     * value there!</p>
     * @param key represents the Key to add into the R way Trie alongside the given Value
     * @param val represents the Value to add with the given Key into the R way Trie
     **********************************************************************************/
    public void insert(String key, T val)
    {
        if(root == null)
        {
            Rway_Node<T> curr = root;
            for(int i = 0; i < key.length(); i++)
            {
                if(root == null)
                {
                    root = curr = new Rway_Node<T>();
                }
                if(i == key.length() - 1)
                {
                    curr.next[key.charAt(i)] = new Rway_Node<T>();
                    curr = curr.next[key.charAt(i)];
                    curr.data = val;
                    return;
                }
                else
                {
                    curr.next[key.charAt(i)] = new Rway_Node<T>();
                    // curr.next[key.charAt(i)].data = val;
                    curr = curr.next[key.charAt(i)];
                    
                }
            }
        }
        else
        {
            Rway_Node<T> curr = root;
            for(int i = 0; i < key.length(); i++)
            {
                if(curr.next[key.charAt(i)] != null && i != key.length() - 1)
                {
                    curr = curr.next[key.charAt(i)];
                }
                else if(curr.next[key.charAt(i)] == null && i != key.length() - 1)
                {
                    curr.next[key.charAt(i)] = new Rway_Node<T>();
                    // curr.next[key.charAt(i)].data = val;
                    curr = curr.next[key.charAt(i)];
                }
                else if(i == key.length() - 1)
                {
                    curr.next[key.charAt(i)] = new Rway_Node<T>();
                    curr = curr.next[key.charAt(i)];
                    curr.data = val;
                    return;
                }

            }
        }
    }
    /**********************************************************************************
     * This method will iteratively search in an R way Trie for a given String key.
     * 
     * <p> There are several cases to consider for in searching for a key:</p>
     * <p><b>Case I</b>: Check if the branch to take is not-null, if so, traverse to it</p>
     * <p><b>Case II</b>: At the end of the String, so find the associated value with the
     * key. 
     * @param key represents the Key to search for in the R way Trie.
     * @return whether or not the key exists in the R way Trie
     **********************************************************************************/
    public boolean search(String key)
    {
        Rway_Node<T> curr = root;
        T theData = null;
        for(int i = 0; i < key.length(); i++)
        {
            if(curr.next[key.charAt(i)] != null && i != key.length() - 1)
            {
                curr = curr.next[key.charAt(i)];
            }
            else if(i == key.length() - 1)
            {
                curr = curr.next[key.charAt(i)];
                theData = curr.data;
                //curr.data = val;
            }

        }
        if(theData != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}