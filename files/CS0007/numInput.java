import java.util.Scanner;

public class numInput
{
    public static void main(String[] args)
    {
        //Invoke the scanner (Create a Scanner object)!
        Scanner kbd = new Scanner(System.in);
        //Prompt the user to enter an Integer!
        System.out.println("Enter an integer: ");
        //Fetch the line that the user inputted, parse it as an Integer, then store it into a variable!
        int number = Integer.parseInt(kbd.nextLine());
        //or you can do this:
        //int number = kbd.nextInt();
    }
}