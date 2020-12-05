import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;
/*****************************************************************
* @author Gordon Lu                                              *
* @Email GOL6@pitt.edu                                           *
* @PSID 4191042                                                  *
*****************************************************************/
@SuppressWarnings({"unchecked", "rawtypes"})
public class LSD_RadixSort 
{
    /***************************************************
    * An implementation of a LSD Radix Sort            *
    * ================================================ *                                        
    ****************************************************/   

    //Typically work with integers, so we'll need at most 10 buckets for each number (0 - 9)
    Queue<Integer>[] auxiliary_buckets = new Queue[10];
    int[] nums;

    public LSD_RadixSort(int[] nums)
    {
        this.nums = nums;
        for(int i = 0; i < auxiliary_buckets.length; i++)
        {
            auxiliary_buckets[i] = new LinkedList<Integer>(); //initialize each bucket from 0 to 9.
        }
    }

    public int[] sort()
    {
        //first we need to determine the number of passes:
        int max = 0, num_passes = 0;
        for(int i = 0; i < nums.length; i++) //handle first pass
        {   
            int digit = nums[i] % 10;
            auxiliary_buckets[digit].add(nums[i]);
            if(nums[i] > max)
            {
                max = nums[i];
            }
        }
        //determine num digits in max:
        int temp_max = max;
        while(temp_max != 0)
        {
            temp_max /= 10;
            num_passes++;
        }
        int digit = 0;
        //Now actually sort:
        do
        {
            //copy back into nums:
            //means we need a separate pointer for original array:
            int ptr = 0;
            for(int j = 0; j < auxiliary_buckets.length; j++)
            {
                //go through each respective bucket:
                while(auxiliary_buckets[j].size() > 0)
                {
                    nums[ptr++] = auxiliary_buckets[j].poll(); //remove head of queue, and store at nums[ptr++]
                }
            }
            //operate from lsd to msd
            for(int i = 0; i < nums.length; i++)
            {
                int curr_digit = (nums[i]/(int)Math.pow(10, digit))%(int)Math.pow(10, digit); //extract curr_digit
                //fill in bucket:
                auxiliary_buckets[curr_digit].add(nums[i]);
            }
            
            digit++;
        }while(num_passes-- > 0);
        return nums;

    }

    public static void main(String[] args)
    {
        //Test with sample data
        int[] radix_test = {12, 103, 212, 23, 3, 113, 121, 200};

        LSD_RadixSort sort = new LSD_RadixSort(radix_test);
        radix_test = sort.sort();
        System.out.println(Arrays.toString(radix_test));

    }
}