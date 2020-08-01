public class DST 
{
    DST_Node root;
    //Node class:
    private static class DST_Node
    {
        private int data;
        private DST_Node left;
        private DST_Node right; 

        public DST_Node(int data)
        {
            this.data = data;
        }
    }    

    public DST()
    {
        root = null;
    }
    //Inserts into a DST based on the binary representation of the string:
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