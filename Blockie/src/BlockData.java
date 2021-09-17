
    /*
        This class holds the index(the index on game screen) and currentLevel properties of a BlockButton
        The reason why these data are not held directly inside the BlockButton class is because 
        Level class uses these two fields but it doesn't need Button properties so it would not be as efficient
        to load hundreds of buttons to memory when the game starts,so we decided to create this class with these two fields and 
        use it as an inner type inside the BlockButton class
    */

public class BlockData 
{
    public int index;
    public int currentLevel;

    public BlockData(int index,int currentLevel)
    {
        this.index=index;
        this.currentLevel=currentLevel;
    }
}