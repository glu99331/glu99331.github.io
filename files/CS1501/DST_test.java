import java.util.Scanner;
//Tests inserts/search fine
public class DST_test 
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        DST dst = new DST();

        dst.insert(4);
        dst.insert(3);
        dst.insert(2);
        dst.insert(6);
        dst.insert(5);

        System.out.println(dst.search(3));
        System.out.println(dst.search(7));

    }    
}