import java.util.ArrayList;

    /*
        This class represents a level in the game,it holds an arraylist of blockdata(the blocks in the level),
        a boolean unlocked which represents whether the level is unlocked or not,and an int highestScore 
        which holds the highest score of the level
    */

public class Level 
{
    public boolean unlocked=false;
    public int highestScore=0;
    public ArrayList<BlockData> blocksData;

    public Level(ArrayList<BlockData> blocksData,boolean unlocked,int highestScore)
    {
        this.unlocked=unlocked;
        this.blocksData=blocksData;
        this.highestScore=highestScore;
    }

    
    //This retrieves the level data as a string for saving purposes
    public String GetStringLevelData()
    {
        String data=unlocked+"-"+highestScore+'\n';

        for(BlockData blockData:blocksData)
        {
            data+=(blockData.index+"-"+blockData.currentLevel+'\n');
        }
        data=data.substring(0,data.length()-1);

        return data;
    }


    //This method generates a random level and returns it to the caller
    public static Level GenerateRandomLevel()
    {
        boolean unlocked = true;
        int highScore = 0;

        ArrayList<BlockData> blocksData = new ArrayList<>();

        int blockLevel =0;
        for(int i=0;i<100;i++)
        {
            blockLevel =Helper.Random(0, 3);
            if(blockLevel==0)
                continue;
        BlockData blockData = new BlockData(i, blockLevel);
        blocksData.add(blockData);
        }
        
        return new Level(blocksData, unlocked, highScore);
    }
}