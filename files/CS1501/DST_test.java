import java.util.Scanner;
/*****************************************************************
* @author Gordon Lu                                              *
* @Email GOL6@pitt.edu                                           *
* @PSID 4191042                                                  *
*****************************************************************/
/***************************************************
* An implementation of a Digital Search Tree       *
* ================================================ *                                       
* A DST takes a non-comparison based approach to   *
* attempt to allow for faster inserts/searches on  *
* numerical data                                   * 
****************************************************/   

/*************************************************
* =============================================  *
* Tests the functionality of the insertion and   *
* search functions for a DST                     *
*************************************************/

public class DST_test 
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        DST dst = new DST();
        //test inserts
        dst.insert(4);
        dst.insert(3);
        dst.insert(2);
        dst.insert(6);
        dst.insert(5);
        //print results of searches:
        System.out.println(dst.search(3));
        System.out.println(dst.search(7));

    }    
}