import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;


    /*
        This class is the presentation layer of the project,it handles UI generation,transitions and events,
        it manages everything related to UI according to information received from Game class or button events in the UI.

        First the game data is loaded from files,then all three different stages are loaded to memory in initialization methods,
        then the main menu stage is shown to the user so the game is ready to run its functions.

        Initialization methods are only executed once so that UI controls(nodes) are only created once and handed to memory afterwards.
        This speeds up the transitions between different scenes,that might not be a good practice since it can cause memory problems
        for larger datas but it actually helps reduce transition times and does not still cause a problem for this particular game.
    */
 
public class App extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }


    //Stages
    private Stage quitRequestPopup;
    private Stage levelsMenu;
    private static Stage mainMenu;
    private static Stage gameWindow;


    //Main Menu Controls
    private static Label totalScoreLabel;
    private static Label lastOpenedLevelLabel;


    //Levels Menu Controls
    private static TilePane levelsCenterPane;
    private Label totalLevelCountLabel;
    private Label unlockedLevelCountLabel;
    private Label backToMainMenuLabel;


    //Game Controls
    private static TilePane gameCenterPane;

    private static Label levelLabel;
    private static Label currentScoreLabel;
    private static Label highScoreLabel;
    private static Label restartLabel;
    private static Label nextLevelLabel;
    private static Label hitTextLabel;


    //Cached Fields
    public static BlockButton[] blocks;
    private final String TITLE="Blockie";


    @Override
    public void start(Stage stage)
    {
        mainMenu=stage;          
        Game.LoadGame();
        InitializeMainMenu();
        InitializeGameWindow();
        InitializeLevelsMenu();
        InitializeQuitPopup();

        ShowMainMenu();
    }


    /*
    This method initializes the main menu stage so that it's ready to use whenever the stage's show() method is called
    This is called only once
    */
    private void InitializeMainMenu()
    {
        mainMenu.setTitle(TITLE);
        mainMenu.setOnCloseRequest(e->
        {
            e.consume();
            ShowQuitPopup();
        });

        String sidePaneColour = "-fx-background-color: #DF8F64";
        String buttonColour = "-fx-background-color: #922F35";

        BorderPane mainPane= new BorderPane();
        mainPane.setPrefSize(616, 666);

        HBox topPane = new HBox();
        topPane.setStyle("-fx-background-color: #A59D7A");
        topPane.setPrefSize(616, 33);
        topPane.setAlignment(Pos.CENTER);

        VBox centerPane = new VBox();
        centerPane.setStyle("-fx-background-color: #E0D5CC");
        centerPane.setPrefSize(166, 633);
        centerPane.setPadding(new Insets(125,11,100,11));
        centerPane.setSpacing(30);

        TilePane leftPane = new TilePane();
        leftPane.setStyle(sidePaneColour);
        leftPane.setPrefSize(225, 633);

        TilePane rightPane = new TilePane();
        rightPane.setStyle(sidePaneColour);
        rightPane.setPrefSize(225, 633);

        mainPane.setTop(topPane);
        mainPane.setCenter(centerPane);
        mainPane.setLeft(leftPane);
        mainPane.setRight(rightPane);

        lastOpenedLevelLabel = new Label();
        lastOpenedLevelLabel.setText("Current Level : " + Game.currentLevel);
        lastOpenedLevelLabel.setPadding(new Insets(0,22,0,22));
        
        Button playButton = new Button();
        playButton.setPrefSize(144, 85);
        playButton.setText("PLAY");
        playButton.setStyle(buttonColour);
        playButton.setOnAction(e->
        {
            mainMenu.close();
            ShowGameWindow();
            Game.LoadCurrentLevel();
        });

        Button playRandomButton = new Button();
        playRandomButton.setPrefSize(144, 85);
        playRandomButton.setText("Generate Random");
        playRandomButton.setStyle(buttonColour);
        playRandomButton.setOnAction(e->
        {
            mainMenu.close();
            ShowGameWindow();
            Game.LoadRandomLevel();
        });
        
        Button levelsButton = new Button();
        levelsButton.setPrefSize(144, 85);
        levelsButton.setText("LEVELS");
        levelsButton.setStyle(buttonColour);
        levelsButton.setOnAction(e->
        {
            mainMenu.close();
            ShowLevelsMenu();
        });

        Button quitButton = new Button();
        quitButton.setPrefSize(144, 85);
        quitButton.setText("QUIT");
        quitButton.setStyle(buttonColour);
        quitButton.setOnAction(e->
        {
            ShowQuitPopup();
        });

        totalScoreLabel = new Label();
        totalScoreLabel.setText("Total Score : " + Game.totalScore);
        totalScoreLabel.setAlignment(Pos.CENTER);

        centerPane.getChildren().addAll(lastOpenedLevelLabel,playButton,playRandomButton,levelsButton,quitButton);

        topPane.getChildren().add(totalScoreLabel);

        Image[] images = new Image[4];

        images[0] = new Image("Resources/Blocks/Wall.Png");
        images[1] = new Image("Resources/Blocks/Empty.Png");
        images[2] = new Image("Resources/Blocks/Mirror.Png");
        images[3] = new Image("Resources/Blocks/Wood.Png");

        ImageView[] views=new ImageView[60];//60 is an arbitrary value

        int edge=0;

        for(int i = 0;i<30;i++)
        {
            edge = Helper.Random(20, 30);//These are also arbitrary values

            ImageView view = new ImageView();
            view.setFitWidth(edge);
            view.setFitHeight(edge);
            view.setImage(images[Helper.Random(0, 3)]);
            views[i]=view;
            leftPane.getChildren().add(views[i]);
        }
        for(int i = 30;i<60;i++)
        {
            edge = Helper.Random(20, 30);//These are also arbitrary values

            ImageView view = new ImageView();
            view.setFitWidth(edge);
            view.setFitHeight(edge);
            view.setImage(images[Helper.Random(0, 3)]);
            views[i]=view;
            rightPane.getChildren().add(views[i]);
        }

        Scene mainScene = new Scene(mainPane,616,666);

        mainMenu.setScene(mainScene);
        mainMenu.setResizable(false);
    }

    /*
    This method initializes the levels menu stage so that it's ready to use whenever the stage's show() method is called
    This is called only once
    */
    private void InitializeLevelsMenu()
    {
        levelsMenu= new Stage();
        levelsMenu.setTitle(TITLE);
        levelsMenu.setOnCloseRequest(e->
        {
            levelsMenu.close();
            ShowMainMenu();
        });

        BorderPane mainPane = new BorderPane();
        mainPane.setPrefSize(616, 666);

        HBox topPane = new HBox();
        topPane.setPrefSize(616, 33);
        topPane.setPadding(new Insets(10,0,10,0));
        topPane.setStyle("-fx-background-color: #86257F");

        levelsCenterPane = new TilePane();
        levelsCenterPane.setPrefSize(616, 608);
        levelsCenterPane.setPadding(new Insets(32,4,32,10));
        levelsCenterPane.setVgap(10);
        levelsCenterPane.setHgap(10);
        levelsCenterPane.setStyle("-fx-background-color: #6780E8");

        BorderPane bottomPane = new BorderPane();
        bottomPane.setPrefSize(616, 25);
        bottomPane.setPadding(new Insets(3,0,3,0));
        bottomPane.setStyle("-fx-background-color: #86257F");

        HBox bottomLeftPane = new HBox();
        HBox bottomRightPane = new HBox();
        HBox bottomCenterPane = new HBox();

        bottomPane.setLeft(bottomLeftPane);
        bottomPane.setRight(bottomRightPane);
        bottomPane.setCenter(bottomCenterPane);

        mainPane.setTop(topPane);
        mainPane.setCenter(levelsCenterPane);
        mainPane.setBottom(bottomPane);

        backToMainMenuLabel = new Label();
        backToMainMenuLabel.setText("<===");
        backToMainMenuLabel.setAlignment(Pos.BASELINE_LEFT);
        backToMainMenuLabel.setOnMouseClicked(e->
        {
            levelsMenu.close();
            ShowMainMenu();
        });

        topPane.getChildren().add(backToMainMenuLabel);

        unlockedLevelCountLabel = new Label();
        unlockedLevelCountLabel.setText("Unlocked Levels : " + Game.GetUnlockedLevelCount());
        unlockedLevelCountLabel.setAlignment(Pos.CENTER_LEFT);

        totalLevelCountLabel = new Label();
        totalLevelCountLabel.setText("Total Levels : " + Game.levels.size());
        totalLevelCountLabel.setAlignment(Pos.CENTER_RIGHT);

        bottomLeftPane.getChildren().add(unlockedLevelCountLabel);
        bottomRightPane.getChildren().add(totalLevelCountLabel);

        LoadLevelsButtons();

        Scene mainScene =new Scene(mainPane,616,666);

        levelsMenu.setScene(mainScene);
        levelsMenu.setResizable(false);
    }

    /*
    This method initializes the levels game stage so that it's ready to use whenever the stage's show() method is called
    This is called only once
    */
    private void InitializeGameWindow()
    {
        gameWindow=new Stage();
        gameWindow.setTitle(TITLE);
        gameWindow.setOnCloseRequest(e->
        {
            if(Game.lastLevel!=0)
            {
                Game.currentLevel=Game.lastLevel;
                Game.lastLevel=0;
            }
            gameWindow.close();
            ShowMainMenu();
        });

        BorderPane topPane= new BorderPane();
        topPane.setMinHeight(25);       
        topPane.setStyle("-fx-background-color: #A4B5D0");
        
        HBox topLeftPane = new HBox();
        HBox topCenterPane = new HBox();
        HBox topRightPane= new HBox();

        topPane.setLeft(topLeftPane);
        topLeftPane.setAlignment(Pos.CENTER_LEFT);
        topPane.setCenter(topCenterPane);
        topCenterPane.setAlignment(Pos.CENTER);
        topPane.setRight(topRightPane);
        topRightPane.setAlignment(Pos.CENTER_RIGHT);

        levelLabel = new Label("Level#-");
        currentScoreLabel =  new Label("0");
        highScoreLabel =  new Label("Highest Score: 0");
        restartLabel = new Label("Restart");
        UpdateRestartLabel();
        
        topLeftPane.getChildren().add(levelLabel);
        topCenterPane.getChildren().add(currentScoreLabel);
        topRightPane.getChildren().add(highScoreLabel);

        BorderPane bottomPane =new BorderPane();
        bottomPane.setMinHeight(25);
        bottomPane.setPadding(new Insets(5,0,5,0));
        bottomPane.setStyle("-fx-background-color: #A4B5D0");

        HBox bottomLeftPane = new HBox();
        bottomLeftPane.setMinWidth(283);
        HBox bottomCenterPane = new HBox();
        bottomCenterPane.setMinWidth(50);
        HBox bottomRightPane = new HBox();
        bottomRightPane.setMinHeight(283);

        bottomPane.setLeft(bottomLeftPane);
        bottomPane.setCenter(bottomCenterPane);
        bottomPane.setRight(bottomRightPane);

        hitTextLabel= new Label();
        UpdateHitTextLabel();
        nextLevelLabel = new Label();

        bottomLeftPane.getChildren().add(hitTextLabel);
        bottomRightPane.getChildren().add(nextLevelLabel);
        bottomCenterPane.getChildren().add(restartLabel);

        gameCenterPane= new TilePane();
        gameCenterPane.setMaxWidth(616);
        gameCenterPane.setPadding(new Insets(5,5,5,5));
        gameCenterPane.setHgap(10);
        gameCenterPane.setVgap(10);
        gameCenterPane.setStyle("-fx-background-color: #454545");

        BorderPane mainPane= new BorderPane();
        mainPane.setTop(topPane);
        mainPane.setBottom(bottomPane);
        mainPane.setCenter(gameCenterPane);

        ResetBlocks();

        Scene gameScene = new Scene(mainPane,616,666);
            
        gameWindow.setScene(gameScene);
        gameWindow.setResizable(false);
    }

   /*
    This method initializes the levels quit popup stage so that it's ready to use whenever the stage's show() method is called
    This is called only once
    */
    private void InitializeQuitPopup()
    {
        quitRequestPopup = new Stage();
        quitRequestPopup.setTitle("Quit");
        quitRequestPopup.initModality(Modality.APPLICATION_MODAL);
        quitRequestPopup.setOnCloseRequest(e->quitRequestPopup.close());

        BorderPane mainPane = new BorderPane();
        mainPane.setPrefSize(300, 150);

        HBox topPane = new HBox();
        topPane.setPrefSize(300, 70);
        

        HBox bottomPane = new HBox();
        bottomPane.setPrefSize(300, 80);
        bottomPane.setSpacing(80);
        bottomPane.setPadding(new Insets(10,30,10,30));

        Label question = new Label();
        question.setText("Are you sure you want to quit?");
        question.setPadding(new Insets(20,60,20,60));

        topPane.getChildren().add(question);

        Button yesButton = new Button();
        yesButton.setPrefSize(80, 40);
        yesButton.setText("YES");
        yesButton.setTextAlignment(TextAlignment.CENTER);
        yesButton.setOnAction(e->
        {
            Game.SaveGame();
            mainMenu.close();
            quitRequestPopup.close();
        });

        Button noButton =new Button();
        noButton.setText("NO");
        noButton.setPrefSize(80, 40);
        noButton.setTextAlignment(TextAlignment.CENTER);
        noButton.setOnAction(e->quitRequestPopup.close());

        bottomPane.getChildren().addAll(yesButton,noButton);

        mainPane.setTop(topPane);
        mainPane.setBottom(bottomPane);

        Scene mainScene = new Scene(mainPane,300,150);
        quitRequestPopup.setScene(mainScene);
        quitRequestPopup.setResizable(false);
    }

    /*
    This method shows the main menu stage after it's initialized
    A call to this method might result in an error in case the main menu stage is not initialized
    */
    public static void ShowMainMenu()
    {
        lastOpenedLevelLabel.setText("Current Level : " + Game.currentLevel);
        totalScoreLabel.setText("Total Score : " + Game.totalScore);
        mainMenu.show();
    }

    /*
    This method shows the levels menu stage after it's initialized
    A call to this method might result in an error in case the levels menu stage is not initialized
    */
    private void ShowLevelsMenu()
    {
        unlockedLevelCountLabel.setText("Unlocked Levels : " + Game.GetUnlockedLevelCount());
        totalLevelCountLabel.setText("Total Levels : " + Game.levels.size());
        LoadLevelsButtons();
        levelsMenu.show();
    }

    //This method is called from Game class and closes the game window when last level is finished
    public static void CloseGameWindow()
    {
        gameWindow.close();
    }
    
    /*
    This method shows the game window stage after it's initialized
    A call to this method might result in an error in case the game window stage is not initialized
    */
    private void ShowGameWindow()
    {
        ResetBlocks();
        gameWindow.show();
    }

    /*
    This method shows the quit popup stage after it's initialized
    A call to this method might result in an error in case the quit popup stage is not initialized
    */
    private void ShowQuitPopup()
    {
        quitRequestPopup.showAndWait();
    }

    /*
    This method resets all the blocks in the game to default blocks(Wall type block)
    So that whenever a new level is loaded,blocks will not overwrite each other
    This is also used in initialization method since the codes are same
    */
    public static void ResetBlocks()
    {
        gameCenterPane.getChildren().clear();
        
        blocks = new BlockButton[100];

        for(int i=0;i<100;i++)
        {
            BlockButton block = new BlockButton(51,51,i,0);
            blocks[i]=block;
            gameCenterPane.getChildren().add(blocks[i]);
        }
    }

    
    //This method loads all the level button in the levels stage so that they're kept updated when further progress is made   
    private void LoadLevelsButtons()
    {
        levelsCenterPane.getChildren().clear();

        for(int i =0;i<Game.levels.size();i++)
        {
            Button button = new Button();
            button.setPrefSize(110, 100);
            button.setStyle("-fx-background-color: #70B381");
            String text = "Level " + (i+1) + '\n' + (Game.levels.get(i).unlocked?"Unlocked":"Locked") + '\n' + "High Score : " + Game.levels.get(i).highestScore;
            button.setText(text);    

            int level = i+1;

            if(Game.levels.get(i).unlocked)
            {
                button.setOnAction(e->
                {
                levelsMenu.close();
                ShowGameWindow();
                Game.LoadExistingLevel(level);
                });
            }
            
            levelsCenterPane.getChildren().add(button);
        }
    }

    /*
        These update methods are used to update UI controls'(nodes') text fields whenever a game object is updated,
        Despite most of the game data were int fields which are passed by value,we could find a way to pass references
        to UI control's(nodes') text fields but that would be unreal due to our N-Tier approach,so we just decided to pass
        data between logic tier and UI tier.
    */


    //This method updates level text in the game window
    public static void UpdateLevelLabel(String newLevel)
    {
        levelLabel.setText("Level#" + newLevel);
    }

    //This method updates current score text in the game window
    public static void UpdateCurrentScoreLabel(int score)
    {
        currentScoreLabel.setText("Score: " + score);
    }

    //This method updates high score text in the game window
    public static void UpdateHighScoreLabel(int score)
    {
        highScoreLabel.setText("Highest Score: "+score);
    }

    //This method updates restart label's text and click event properties according to the lastLevel field in the Game class
    public static void UpdateRestartLabel()
    {
        if(Game.lastLevel==0)
            {
                restartLabel.setOnMouseClicked(e->
                {
                    if(!nextLevelLabel.getText().isEmpty())
                        UpdateNextLevelLabel();
                    Game.LoadExistingLevel(Game.currentLevel);
                });
                
                restartLabel.setText("Restart");
            }
            else
            {
                restartLabel.setOnMouseClicked(e->
                {
                    int lastLevel = Game.lastLevel;
                    Game.LoadRandomLevel();
                    Game.lastLevel=lastLevel;
                });
                restartLabel.setText("Regenerate");
            }      
    }

    //This method updates next level label's text and click event properties according to its current text
    public static void UpdateNextLevelLabel()
    {
        if(nextLevelLabel.getText().isEmpty())
        {
            nextLevelLabel.setText("Next Level >>");
            nextLevelLabel.setOnMouseClicked(e->
            {
            Game.LoadNextLevel();
            UpdateNextLevelLabel();
            });
        }
        else
        {
            nextLevelLabel.setText("");
            nextLevelLabel.setOnMouseClicked(null);
        }
    }

    /*
    This method updates hit text in the game window
    It's called whenever a level starts
    */
    public static void UpdateHitTextLabel()
    {
        hitTextLabel.setText("Good Luck!");
    }

    /*
    This overload updates hit text in the game window
    It's called whener player makes a click
    */
    public static void UpdateHitTextLabel(String text)
    {
        hitTextLabel.setText(text);
    }
}