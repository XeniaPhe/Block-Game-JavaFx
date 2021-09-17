import java.util.ArrayList;

    /*
        This class operates everything related to game and gameplay with the help of other entities,
        This class is the business logic layer of the project
        It communicates with UI layer and data access layer respectively and processes information
    */
public class Game 
{
    public static int currentLevel=1;
    public static int currentScore=0;
    public static int highestScore=0;
    public static int totalScore=0;
    public static ArrayList<Level> levels;
    public static int lastLevel=0;

    /*
    Game class is designed so that there are only static fields and methods
    So this disables creating Game objects
    */
    private Game()
    {
        
    }

    /*
    This method calls the LoadGame() method in the SaveLoadSystem class which loads the game data from text files
    App class making a call to SaveLoadSystem would have broken the N-Tier Architecture 
    */
    public static void LoadGame()
    {
        try
        {
            levels = SaveLoadSystem.LoadLevelData();
            String[] data = SaveLoadSystem.LoadGameData();

            currentLevel = Integer.parseInt(data[0]);
            totalScore = Integer.parseInt(data[1]);
            currentScore=0;
            highestScore = levels.get(currentLevel-1).highestScore;
        }
        catch(Exception ex)
        {
            System.out.println("Game hasn't been able to be loaded successfully due to:");
            System.out.println(ex.getMessage());
        }
    }

    /*
    This method calls the SaveGame() method in the SaveLoadSystem class which saves the game data to text files
    App class making a call to SaveLoadSystem would have broken the N-Tier Architecture 
    */
    public static void SaveGame()
    {
        try
        {
            SaveLoadSystem.SaveGameData(currentLevel,totalScore);
            SaveLoadSystem.SaveLevelData(levels);
        }
        catch(Exception ex)
        {
            System.out.println("Game hasn't been able to be saved successfully due to:");
            System.out.println(ex.getMessage());
        }
    }

    //This method loads the current level
    public static void LoadCurrentLevel()
    {
        LoadExistingLevel(currentLevel);
    }

    //This method loads the next level
    public static void LoadNextLevel()
    {
        LoadExistingLevel(++currentLevel);
    }

    //This method loads the level specified with int parameter level from levels list
    public static void LoadExistingLevel(int level)
    {

        if(level>levels.size() || level<0)
        {
            System.out.println("Invalid Level!");
            System.out.println(level);
            return;
        }
        if(!levels.get(level-1).unlocked)
        {
            System.out.println("Level is not unlocked!");
            return;
        }

        currentLevel=level;
        lastLevel=0;

        App.UpdateLevelLabel(level+"");
        
        LoadLevel(levels.get(currentLevel-1));
    } 
  
    //This method generates a random level and loads it
    public static void LoadRandomLevel()
    {
        Level level = Level.GenerateRandomLevel();

        lastLevel=currentLevel;
        currentLevel = levels.size()+1;

        App.UpdateLevelLabel("Random Level");

        LoadLevel(level);
    }

    //This is the general method to load a level,all the other level loading methods use that method
    private static void LoadLevel(Level level)
    {
        App.ResetBlocks();
        App.UpdateRestartLabel();
        currentScore=0;
        App.UpdateCurrentScoreLabel(currentScore);
        highestScore = level.highestScore;
        App.UpdateHighScoreLabel(highestScore);
        App.UpdateHitTextLabel();
        
        int blockSize = level.blocksData.size();
        int index= 0;
        BlockData blockData;

        for(int i = 0;i<blockSize;i++)
        {
            index = level.blocksData.get(i).index;
            blockData = level.blocksData.get(i);

            App.blocks[index].SetCurrentLevel(blockData.currentLevel);
        }
    }

    //This method checks if player finished the current level,and updates some values accordingly
    public static void CheckIfLevelOver()
    {
        boolean levelOver=true;

        for(BlockButton b:App.blocks)
        {
            if(b.GetCurrentLevel()<=1) continue;
            else
            {       
                levelOver=false;
                break;
            }
        }
        
        if(!levelOver)
            return;

        if(lastLevel==0)
        {
            UpdateHighScore();
            UpdateTotalScore();

            if(currentLevel==levels.size())
            {
                App.CloseGameWindow();
                App.ShowMainMenu();
            }
            else
            {
                levels.get(currentLevel).unlocked=true;      
                App.UpdateNextLevelLabel();
            }  
        }     
        else
        {
            currentLevel=lastLevel;
            lastLevel=0;
            App.CloseGameWindow();
            App.ShowMainMenu();
        }
    }

    //This method updates the high score of a level
    private static void UpdateHighScore()
    {
        levels.get(currentLevel-1).highestScore = (currentScore>highestScore)?currentScore:(highestScore==0)?currentScore:highestScore;
        App.UpdateHighScoreLabel(levels.get(currentLevel-1).highestScore);
    }

    //This method updates the total score of a level
    private static void UpdateTotalScore()
    {
        totalScore=0;

        for(Level level:levels)
        {
            totalScore+=level.highestScore;
        }
    }

    /*
    This method generates the hit text and updates it calling the String taking overload of UpdateHitTextLabel()
    method in the App class
    */
    public static void UpdateHitText(Coordinate[] coordinates)
    {
        int hitCount=1;
        String text = "Box: "+ coordinates[0].item1+","+coordinates[0].item2;

        for(int i=1;i<5;i++)
        {
            if(coordinates[i]==null)
                continue;
            hitCount++;
            text+=(" - Hit: " + coordinates[i].item1 + "," + coordinates[i].item2); 
        }

        int pointsEarned  = CalculatePoints(hitCount);
        currentScore+=pointsEarned;
        App.UpdateCurrentScoreLabel(currentScore);
        text+=(" (" + ((pointsEarned>0)? "+":"") + pointsEarned + " Points)");
        
        App.UpdateHitTextLabel(text);
    }

    //This method calculates the points earned from a move
    private static int CalculatePoints(int hitCount)
    {
        int points=0;

        if(hitCount==1)
            points=-3;
        else if(hitCount==2)
            points=-1;
        else if(hitCount==3)
            points=1;
        else if(hitCount==4)
            points=2;
        else if(hitCount==5)
            points=4;

        return points;
    }   

    //This method finds how many unlocked levels there are
    public static int GetUnlockedLevelCount()
    {
        int counter=0;
        for(Level level:levels)
        {
            if(level.unlocked)
                counter++;
        }

        return counter;
    }
}