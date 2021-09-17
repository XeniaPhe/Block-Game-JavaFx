import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

    /*
        This class extends from Button class and represents a button(block) inside the game
        The only constructor sets the click event of the button itself
        A block's icon,its current level,whether it's destroyable or not are all determined
        by call of the methods inside this class
    */


public class BlockButton extends Button
{
    
    private Image icon;
    private ImageView view;
    private BlockData block;

    public BlockButton(int x,int y,int index,int currentLevel)
    {
        super();
        this.setMinSize(x, y);
        this.setMaxSize(x, y);
        block = new BlockData(index, currentLevel);       
        view = new ImageView();
        view.setFitWidth(52);
        view.setFitHeight(54);      
        SetCurrentLevel(currentLevel);      
    } 

    //This method sets the click event of the button according to its level
    private void SetClickEvent()
    {
        if(block.currentLevel<=1)
            this.setOnAction(null);
        else
            this.setOnAction(e->OnButtonClick());
    }

    //Click event of the button,it checks 
    private void OnButtonClick()
    {
        Coordinate[] coordinates = new Coordinate[5];

        coordinates[0]=GetCoordinates();

        GetHit();

        if(CheckIfIndexValid(block.index-10))
            coordinates[1]=App.blocks[block.index-10].TryHit();
        if(CheckIfIndexValid(block.index+10))
            coordinates[2]=App.blocks[block.index+10].TryHit();
        if(CheckIfIndexValid(block.index-1))
            coordinates[3]=App.blocks[block.index-1].TryHit();
        if(CheckIfIndexValid(block.index+1))
            coordinates[4]=App.blocks[block.index+1].TryHit();

        Game.UpdateHitText(coordinates);
        Game.CheckIfLevelOver();
    } 

    //This is called from an adjacent block,it calls GetHit() method of the block if the conditions are met
    public Coordinate TryHit()
    {
        if(block.currentLevel<=1) return null;
        else
        {
            GetHit();
            return GetCoordinates();
        }
    }

    //Decreases the current level of the object and sets its image accordingly
    private void GetHit()
    {
        block.currentLevel--;
        UpdateIcon();
        SetClickEvent();
    }

    //Checks if a number is within 0-99,it's used to determine whether the index is valid
    private boolean CheckIfIndexValid(int index)
    {
        return (index>=0 && index<=99) ? true :false;
    }

    //Converts the index of the block to game coordinates
    private Coordinate GetCoordinates()
    {
        Coordinate coordinate = new Coordinate(block.index/10,block.index%10);

        return coordinate;
    }

    //Updates the icon of the block
    private void UpdateIcon()
    {
        switch(block.currentLevel)
        {
            default:
            case 0:
                icon = new Image("Resources/Blocks/Wall.Png");
            break;
            case 1:
                icon = new Image("Resources/Blocks/Empty.Png");
            break;
            case 2:
                icon = new Image("Resources/Blocks/Mirror.Png");
            break;
            case 3:
                icon = new Image("Resources/Blocks/Wood.Png");
            break;
        }

        view.setImage(icon);
        this.setGraphic(view);
    }

    //Sets the current level of the block,then calls UpdateIcon() and SetClickEvent() methods
    public void SetCurrentLevel(int level)
    {
        if(level<0 || level>3)
            level=0;

        block.currentLevel=level;
        UpdateIcon();
        SetClickEvent();
    }

    //Retrieves the current level of the block
    public int GetCurrentLevel()
    {
        return block.currentLevel;
    }

    //Gets the index of the block
    public int GetIndex()
    {
        return block.index;
    }
    
}