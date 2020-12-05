public class bad
{
    public static void main(String[] args)
    {
        int radius = 3;
        double area_of_circle = Math.PI * Math.pow(radius,2);
        //System.out.println(area_of_circle); //Too many decimal places!
        System.out.printf("%.3f\n", area_of_circle);    //Prints our variable up to 3 decimal places!
    }
}