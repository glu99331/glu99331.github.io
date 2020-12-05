import java.io.*;
/*****************************************************************
* @author Gordon Lu                                              *
* @Email GOL6@pitt.edu                                           *
* @PSID 4191042                                                  *
*****************************************************************/

/***************************************************
* An implementation of an Rway Trie                *
* ================================================ *                                       
* An Rway Trie is an array-based implementation of *
* a large branching factor trie.                   *
*                                                  *
* The number of branches per node is contingent on *
* the size of the alphabet of interest.            *
***************************************************/

/*************************************************
* =============================================  *
* Tests the functionality of the insertion and   *
* search functions for an Rway Trie              *
*************************************************/
public class Rway_test 
{
    public static void main(String[] args) throws IOException
    {
        RwayTrie<Integer> rway = new RwayTrie<Integer>(256);//ASCII
        BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
        int i = 0;
        while(br.ready())
        {
            rway.insert(br.readLine(), i++);
        }
        br = new BufferedReader(new FileReader("dictionary.txt"));
        int j = 0;
        while(br.ready())
        {
            if(rway.search(br.readLine()))
            {
                j++;
            }
        }
        System.out.println((i==j)); //test if counts are equal

    }    
}