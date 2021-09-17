import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

    /*
        This class is the data access layer of the project,it handles save and load affairs of the project
        This communicates with logic layer(Game class) and retrieves/receives data from/to it respectively 
    */

public class SaveLoadSystem 
{
    //This prevents creating SaveLoadSystem objects 
    private SaveLoadSystem()
    {

    }

    //Loads the game data(total score and current level) from files
    public static String[] LoadGameData() throws IOException
    {
        File dataFile = new File("src/Resources/Data/GameData.txt");
    
        Scanner scanner = new Scanner(dataFile);
        String data = scanner.nextLine();
        scanner.close();
        
        String[] gameData=Split(data);

        return gameData;
    }
    
    //Loads all the level data from files
    public static ArrayList<Level> LoadLevelData() throws IOException
    {    

        File[] levelFiles = new File("src/Resources/Data/Levels").listFiles();

        ArrayList<Level> levels = new ArrayList<>();

        Scanner scanner;

        for(File file:levelFiles)
        {            
            scanner =  new Scanner(file);                     
            String data = scanner.nextLine();

            String[] temp=Split(data);
            boolean unlocked = (temp[0].equals("true"))?true:false;
            int highScore = Integer.parseInt(temp[1]);

            ArrayList<BlockData> blocksData= new ArrayList<>();
            
            while(scanner.hasNextLine())
            {
                data = scanner.nextLine();
                temp = Split(data);
                BlockData blockData = new BlockData(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
                blocksData.add(blockData);          
            }

            scanner.close();

            Level level = new Level(blocksData, unlocked, highScore);
            levels.add(level);
        }

        return levels;
    }

    //Saves the game data to files
    public static void SaveGameData(int currentLevel,int totalScore) throws IOException
    {
        File dataFile = new File("src/Resources/Data/GameData.txt");
        PrintWriter writer = new PrintWriter(dataFile);

        writer.print(currentLevel+"-"+totalScore);
        writer.close();
    }

    //Saves all the level data to files
    public static void SaveLevelData(ArrayList<Level> levels) throws IOException
    {
        int counter=0;
        //This for loop aims to narrow down the levels that might have been updated
        for(Level level:levels)
        {
            if(level.unlocked)
                counter++;
        }

        File[] levelFiles = new File("src/Resources/Data/Levels").listFiles();
        PrintWriter writer;

        for(int i=0;i<counter;i++)
        {
            writer = new PrintWriter(levelFiles[i]);
            String updatedData = levels.get(i).GetStringLevelData();
            writer.print(updatedData);
            writer.close();
        }
    }

    /*
        There was a problem in which sometimes two dashes were printed instead of one,and we couldn't figure out
        what was causing that,so we ended up creating this non-practical solution,but on the bright side it works :)
    */
    private static String[] Split(String data)
    {
        String[] returnValue;
        int counter=0;
        
        for(int i=0;i<data.length();i++)
        {
            if(data.charAt(i)=='-')
                counter++;
        }

        String splitter = "";

        for(int i=0;i<counter;i++)
        {
            splitter+="-";
        }

        returnValue=data.split(splitter);

       return returnValue;
    }

}