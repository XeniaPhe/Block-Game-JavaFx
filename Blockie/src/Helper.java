    /*
        This class holds static general-use methods that are not directly related to any other class in the project
    */

public class Helper 
{
    //Just to prevent creating Helper objects,because it's not meant to be created
    private Helper()
    {

    }
    
    //Returns a random int between minimum and maximum values specified in the parameters(maximum included)
    public static int Random(int from,int to)
    {
        int randomValue = from + (int)(Math.random()*(from+to+1));

        return randomValue;
    }

}