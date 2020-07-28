import java.io.*;
public class testDLB
{
    public static void main(String[] args)
    {
        BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
        DLB dictionaryTrie = new DLBTrie();

        int total_dictionary_words = 0, words_in_dlb = 0;

        while(br.ready())
        {
            String word = br.readLine();
            total_dictionary_words++;
            if(dictionaryTrie.insert(word))
            {
                words_in_dlb++;
            }
        }
        System.out.println("Passed: " + words_in_dlb + "\nFailed: " + (total_dictionary_words - words_in_dlb) + "\nTotal: " + total_dictionary_words);
        //Should be:
        //Passed: 126328
        //Failed: 0
        //Total: 126328

        //You are free to expand upon this tester as you feel.
    }
}