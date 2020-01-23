import java.util.Scanner;

public class userInput
{
    public static void main(String[] args)
    {
        //Invoke the scanner (Create a Scanner object)!
        Scanner kbd = new Scanner(System.in);
        //Prompt the user to enter their name!
        System.out.println("Hey there! Nice to meet you! What's your name? ");
        //Fetch the line that the user inputted, and store it into a variable!
        String name = kbd.nextLine();
    }
}