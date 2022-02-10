package app.myapp.controller;

import app.myapp.Main;
import app.myapp.controller.component.Content_ListofPosts;
import app.myapp.controller.component.Content_ListofSpots;
import app.myapp.controller.component.Content_MainPost;
import app.myapp.controller.component.element.*;
import app.myapp.controller.component.element.dialog.Spot;
import app.myapp.database.Query;
import app.myapp.model.user.data.User;
import app.myapp.model.user.data.UserData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.StackedFontIcon;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Main_Controller {
    // TODO
    // DialogPane for Add Spots, Posts, Comments, and Reply.
    // Add Edit/Remove Buttons on Posts, Comments, and Reply. + Dialog Panes for Warning

    Image logo = new Image(String.valueOf(Main.class.getResource("objects\\logo_ph.png")));

    @FXML
    private StackedFontIcon btnAdd;
    @FXML
    private StackedFontIcon btnSearch;
    @FXML
    private MenuButton menuUser;
    @FXML
    private TextField txtSearch;

    @FXML
    private HBox contentPane;
    @FXML
    private HBox spotPane;

    //STATE
    User user;
    Query sql = new Query();

    //OBJECTS
    Content_ListofSpots contentListofSpots = new Content_ListofSpots();
    Spot_JoinedList spotJoinedList = new Spot_JoinedList();
    Content_ListofPosts contentListofPosts = new Content_ListofPosts();

    //METHODS

    //OBJECTS-TEMP
    Content_MainPost contentMainPost = new Content_MainPost();

    CardComment cardComment = new CardComment();
    CardReply cardReply = new CardReply();

    public Main_Controller() throws IOException {
        UserData userData = UserData.getInstance();
        this.user = userData.getUser();
    }

    @FXML
    void initialize() throws IOException, SQLException {
        //Set Username to the MenuBar
        menuUser.setText(user.getUsername());

        //Default to Home
        sessionHome();

        //TODO
        //JOIN BUTTON ON HOME SPOT LIST AND SPOT INFO
        //POST DATABASE ADD
        //CREATE POST METHOD
    }

    /******************
     *  Switch Panes  *
     ******************/

    void sessionHome() throws IOException, SQLException {
        viewContent_SpotList();
        viewSpotJoinedList();
    }

    void sessionSelectedSpots(String spotID) throws IOException, SQLException {
        viewContent_PostList(spotID);
        viewSpotInfo(spotID);
    }


    /******************
     *  Switch Panes  *
     ******************/

    void viewContent_MainPost() throws IOException {
        //CONTENT PANE: Main Post
        contentPane.getChildren().add(contentMainPost);
    }

    void viewContent_SpotList() throws IOException, SQLException {
        //CONTENT PANE: User's List of Spots

        //GET CURRENT DATABASE DATA
        Query sql = new Query();
        ArrayList<CardSpot> spotCards = sql.getTopSpots();

        for (CardSpot spot: spotCards) {
            spot.cardSpotJoinBtn.setOnMousePressed(e->{
                System.out.println( "Join " + spot.cardSpotTitle.getText());
                //HERE
            });
            spot.cardSpotTitle.setOnMousePressed(e->{
                System.out.println(spot.cardSpotTitle.getText());
                try {
                    sessionSelectedSpots(spot.spotID);
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SETUP THE PAGINATION
        contentListofSpots.cPageSpots.setMaxPageIndicatorCount(1);
        contentListofSpots.cPageSpots.setPageCount(1);

        //PAGINATION VBOX LAYER
        PageContainer pageContainer = new PageContainer();
        pageContainer.getChildren().addAll(spotCards);

        //SET CHILDREN OF THE PAGINATION
        contentListofSpots.cPageSpots.setPageFactory((pageIndex) -> {
            return pageContainer;
        });

        if(!contentPane.getChildren().contains(contentListofSpots)){
            contentPane.getChildren().clear();
            contentPane.getChildren().add(contentListofSpots);
        }
    }

    void viewContent_PostList(String spotID) throws SQLException, IOException {
        //CONTENT PANE: List of Posts

        //GET CURRENT DATABASE DATA
        Query sql = new Query();

        ArrayList<CardPost> postCards = sql.getTopPosts(spotID);

        //SET UP THE PAGINATION
        int noPage = Math.max(postCards.size(), 1);
        noPage = (int) Math.ceil((double) noPage / 5);
        contentListofPosts.cPagePosts.setMaxPageIndicatorCount(3);
        contentListofPosts.cPagePosts.setPageCount(noPage);

        if(postCards.size() != 0){
            //PAGINATION VBOX LAYER
            ArrayList<PageContainer> containers = new ArrayList<>();
            int j = 0;
            for (int i = 0; i < noPage; i++) {
                PageContainer container = new PageContainer();
                for (int x = 0; j < postCards.size(); j++) {
                    container.getChildren().add(postCards.get(j));
                    x++;
                    if(x == 5){
                        containers.add(container);
                        break;
                    }
                }
            }

            //SET CHILDREN OF THE PAGINATION
            contentListofPosts.cPagePosts.setPageFactory(containers::get);
        }


        if(!contentPane.getChildren().contains(contentListofPosts)){
            contentPane.getChildren().clear();
            contentPane.getChildren().add(contentListofPosts);
        }
    }

    void viewSpotJoinedList() throws IOException, SQLException {
        //SPOT PANE: Spot Lists
        spotJoinedList.paneList.getChildren().clear();

        //GET CURRENT DATABASE DATA
        Query sql = new Query();
        Map<String,String> spots = sql.getJoinedSpots(user);

        ArrayList<MySpotsLabel> jSpots = new ArrayList<>();

        for (Map.Entry<String, String> pair : spots.entrySet()) {
            MySpotsLabel jSpot = new MySpotsLabel(pair.getKey(),pair.getValue());
            jSpot.mySpotText.setOnMousePressed(e->{
                System.out.println(jSpot.mySpotText.getText());
                try {
                    sessionSelectedSpots(jSpot.spotID);
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }
            });
            jSpots.add(jSpot);
            System.out.println(pair.getValue());
        }

        spotJoinedList.paneList.getChildren().addAll(jSpots);

        if(!spotPane.getChildren().contains(spotJoinedList)){
            spotPane.getChildren().clear();
            spotPane.getChildren().add(spotJoinedList);
        }
    }

    void viewSpotInfo(String spotID) throws IOException, SQLException {
        //SPOT PANE: Spot Information

        //GET CURRENT DATABASE DATA
        Query sql = new Query();

        Spot_Info spotInfo = sql.getSpotInfo(spotID);

        spotInfo.spotJoinBtn.setOnAction(e->{
            System.out.println( "Join " + spotInfo.spotName);
            //HERE
        });
        spotInfo.spotCPostBtn.setOnMousePressed(e->{
            System.out.println( "Create Posts ");
            //HERE
        });

        if(!spotPane.getChildren().contains(spotInfo)){
            spotPane.getChildren().clear();
            spotPane.getChildren().add(spotInfo);
        }
    }

    /*******************
     *  Button Events  *
     *******************/

    @FXML
    void addSpot(MouseEvent event) throws IOException {
        Spot addSpotDialog = new Spot();

        Dialog<String> dialog = new Dialog<>();
        dialog.setDialogPane(addSpotDialog);
        dialog.setTitle("NightSpot");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(logo);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, e -> {
            String name = addSpotDialog.txtName.getText();
            String desc = addSpotDialog.txtDesc.getText();
            String rules = addSpotDialog.txtRules.getText();

            if( name.length() > 50 || name.isEmpty()
                    ||  desc.length() > 100 || desc.isEmpty()
                    ||  rules.length() > 500 || rules.isEmpty()
            ){
                e.consume();
            }else{
                //METHOD
                try {
                    sql.createSpots(user, name, desc, rules);
                    System.out.println("CREATED SPOT: " + name);
                    user.updateData();
                    viewSpotJoinedList();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }

    @FXML
    void onHome(MouseEvent event) throws IOException, SQLException {
        sessionHome();
    }

    @FXML
    void search(MouseEvent event) throws IOException {

    }

    /*************
     *  Methods  *
     *************/


}


