/*****************************************************************
* @author Gordon Lu                                              *
* @Email GOL6@pitt.edu                                           *
* @PSID 4191042                                                  *
*****************************************************************/
@SuppressWarnings({"unchecked", "rawtypes"})
public class DST 
{
    /***************************************************
    * An implementation of a Digital Search Tree       *
    * ================================================ *                                       
    * A DST takes a non-comparison based approach to   *
    * attempt to allow for faster inserts/searches on  *
    * numerical data                                   * 
    ****************************************************/   
    
    /**************************************************
     * <~-------------Global Variables:------------~> *
     * =============================================  *
     * root - Needed to provide base point to         *
     * insert and search for elements (need a place)  *
     * to start.                                      *
     * ============================================== *
     *************************************************/

    DST_Node root;
    /*******************************************
     * Inner node class:
     * <ul>
     * <li>Contains reference to left child</li> 
     * <li>Contains reference to right child</li>
     * <li>The data stored with each node</li>
     * </ul>
     *******************************************/
    private static class DST_Node
    {
        private int data; //number stored in each node
        private DST_Node left; //go left if curr bit is 0
        private DST_Node right; //go right if curr bit is 1
        /**************************************
         * Constructor to simply add a new 
         * node with a particular value.
         * 
         * @param data represents value node will 
         * be initialized with.
         *************************************/
        public DST_Node(int data)
        {
            this.data = data;
        }
    }    
    /***********************************
     * Initializes an empty DST
     * 
     **********************************/
    public DST()
    {
        root = null;
    }
    /**********************************************************************************
     * This method will iteratively insert into a DST based on the binary 
     * representation of the passed in number:
     * 
     * <p> Prior to going ahead and trying to insert a key into the DST, we need to 
     * actually determine how many bits we need to inspect! </p>
     * 
     * <p> There are several cases to consider for in insertion:</p>
     * <p><b>Base Case</b>: If nothing has been inserted, val becomes stored at root.</p>
     * <p><b>Case I</b>: Traverse to left if current bit is 0</p>
     * <p><b>Case II</b>: Traverse to right if current bit is 1</p>
     * <p><b>Case III</b>: Current bit is 0, but nothing on left, so create left child, and 
     * make val on left</p>
     * <p><b>Case IV</b>: Current bit is 1, but nothing on right, so create right child, and 
     * make val on right</p>
     * <p><b>Case V</b>: Value is already in DST, unneccesary to overwrite, so just exit.</p>
     * @param val represents the integer we want to add into the DST.
     **********************************************************************************/
    public void insert(int val)
    {
        if(root == null)
        {
            root = new DST_Node(val);
        }
        else
        {
            DST_Node curr = root;
            int num_bits = 0;
            int temp_val = val;
            while(temp_val != 0) //determine how many bits there are:
            {
                temp_val >>= 1;
                num_bits++;
            }
            for(int curr_msb = num_bits-1; curr_msb >= 0; curr_msb--) //shamt decreases as we want to look at bits from MSB to LSB.
            {
                //case 1: go left
                if(val != curr.data && ((val >> curr_msb) & 0x1) == 0x0 && curr.left != null)
                {
                    //navigate to left child:
                    curr = curr.left;
                }
                //case 2: go right
                else if(val != curr.data && ((val >> curr_msb) & 0x1) != 0x0 && curr.right != null)
                {
                    //navigate to right child
                    curr = curr.right;
                }
                //case 3: make left 
                else if(val != curr.data && ((val >> curr_msb) & 0x1) == 0x0 && curr.left == null)
                {
                    //create left child and store there
                    curr.left = new DST_Node(val);
                    return;
                }
                //case 4: make right
                else if(val != curr.data && ((val >> curr_msb) & 0x1) != 0x0 && curr.right == null)
                {
                    //create right child and store there
                    curr.right = new DST_Node(val);
                    return;
                }
                //case 5: already in DST
                else if(val == curr.data)
                {
                    return; //found
                }
            }
        }
    }
    //Search in DST based on the binary representation of the string:
    /**********************************************************************************
     * This method will iteratively search in a DST for a passed-in number based on 
     * its binary representation
     * 
     * <p> Prior to going ahead and trying to search a key into the DST, we need to 
     * actually determine how many bits we need to inspect! </p>
     * 
     * <p> There are several cases to consider for in searching for a key:</p>
     * <p><b>Base Case</b>: Peek at the root, if it's the value we want, we're good.</p>
     * <p><b>Case I</b>: Traverse to left if current bit is 0</p>
     * <p><b>Case II</b>: Traverse to right if current bit is 1</p>
     * <p><b>Case III</b>: Value is at current node, so we found it!</p>
     * @param val represents the integer we want to search for in the DST.
     **********************************************************************************/
    public boolean search(int val)
    {
        if(root.data == val)
        {
            return true;
        }
        else
        {
            DST_Node curr = root;
            int num_bits = 0;
            int temp_val = val;
            while(temp_val != 0) //determine how many bits there are:
            {
                temp_val >>= 1;
                num_bits++;
            }
            for(int curr_msb = num_bits-1; curr_msb >= 0; curr_msb--)
            {
                //case 1: go left
                if(val != curr.data && ((val >> curr_msb) & 0x1) == 0x0 && curr.left != null)
                {
                    //navigate to left child:
                    curr = curr.left;
                }
                //case 2: go right
                else if(val != curr.data && ((val >> curr_msb) & 0x1) != 0x0 && curr.right != null)
                {
                    //navigate to right child
                    curr = curr.right;
                }
                //case 3: val found 
                else if(val == curr.data)
                {
                    return true; //found
                }
            }
        }
        return false;
    }
}