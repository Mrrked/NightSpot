package app.myapp.controller;

import app.myapp.Main;
import app.myapp.controller.component.Content_ListofPosts;
import app.myapp.controller.component.Content_ListofSpots;
import app.myapp.controller.component.Content_MainPost;
import app.myapp.controller.component.element.*;
import app.myapp.controller.component.element.dialog.Comment;
import app.myapp.controller.component.element.dialog.Post;
import app.myapp.controller.component.element.dialog.Reply;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class Main_Controller {
    // TODO
    // IMITATE SPOTIFY'S TREND FEED: Daily, Monthly, Yearly

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
    Content_ListofPosts contentListofPosts = contentListofPosts = new Content_ListofPosts();
    Spot_JoinedList spotJoinedList = new Spot_JoinedList();
    Content_MainPost contentMainPost;

    //METHODS

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
    }

    /*******************
     * CURRENT SESSION *
     *******************/

    void sessionHome() throws IOException, SQLException {
        viewContent_SpotList();
        viewSpotJoinedList();
    }

    void sessionSelectedSpots(String spotID) throws IOException, SQLException, ParseException {
        viewContent_PostList(spotID);
        viewSpotInfo(spotID);
    }

    void sessionSelectedPosts(String spotID, String postID) throws IOException, SQLException, ParseException {
        viewContent_MainPost(spotID, postID);
        viewSpotInfo(spotID);
    }

    /******************
     *  Switch Panes  *
     ******************/

    void viewContent_MainPost(String spotID, String postID) throws IOException, SQLException, ParseException {
        //CONTENT PANE: Main Post
        Query sql = new Query();

        contentMainPost = sql.getPostInfo(spotID, postID);

        contentMainPost.cPostReturn.setOnMousePressed(e ->{
            try {
                sessionSelectedSpots(spotID);
            } catch (IOException | SQLException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        contentMainPost.cMainPostAddCommentBtn.setOnMousePressed(e -> {
            try {
                addComment();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        if(!contentMainPost.cMainPostComments.getText().equals("0 COMMENTS")){
            ArrayList<CardComment> comments = sql.getComments(postID);

            for (CardComment comment: comments) {
                comment.cComShowBtn.setOnMousePressed(e -> {
                    try {
                        showReply(comment);
                    } catch (SQLException | IOException | ParseException ex) {
                        ex.printStackTrace();
                    }
                });
                comment.cComReplyBtn.setOnMousePressed(e ->{
                    try {
                        addReply(comment);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            contentMainPost.commentPane.getChildren().addAll(comments);
        }

        contentPane.getChildren().clear();
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
                try {
                    sessionSelectedSpots(spot.spotID);
                } catch (IOException | SQLException | ParseException ex) {
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

    void viewContent_PostList(String spotID) throws SQLException, IOException, ParseException {
        //CONTENT PANE: List of Posts

        contentListofPosts.cPostReturn.setOnMousePressed(e ->{
            try {
                sessionHome();
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });

        //GET CURRENT DATABASE DATA
        Query sql = new Query();

        ArrayList<CardPost> postCards = sql.getTopPosts(spotID);

        for (CardPost post: postCards) {
            post.cPostLink.setOnMousePressed(e -> {
                try {
                    sessionSelectedPosts(post.spotID, post.postID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SET UP THE PAGINATION
        int countPosts = postCards.size();
        int noPage = Math.max(countPosts, 1);
        noPage = (int) Math.ceil((double) noPage / 5);

        contentListofPosts.cPagePosts.setMaxPageIndicatorCount(3);
        contentListofPosts.cPagePosts.setPageCount(noPage);

        System.out.println("POST COUNT: " + countPosts + " | PAGES: " + noPage);
        ArrayList<PageContainer> containers = new ArrayList<>();

        if(countPosts != 0){
            //PAGINATION VBOX LAYER
            int j = 0;

            //LOOP PER PAGE
            for (int i = 0; i < noPage; i++) {
                PageContainer container = new PageContainer();

                //LOOP THROUGH POSTS
                for (int x = 0; j < countPosts; ) {
                    container.getChildren().add(postCards.get(j));
                    x++;
                    j++;
                    if(x == 5 || j == countPosts ){
                        containers.add(container);
                        //RESET PER CONTAINER
                        break;
                    }
                }
            }

            //SET CHILDREN OF THE PAGINATION
            contentListofPosts.cPagePosts.setPageFactory(containers::get);

        }else {
            PageContainer empty = new PageContainer();
            contentListofPosts.cPagePosts.setPageFactory((pageindex) -> {
                return empty;
            });
        }

        if(!contentPane.getChildren().contains(contentListofPosts)){
            contentPane.getChildren().clear();
            contentPane.getChildren().add(contentListofPosts);
        }
    }

    void viewSpotJoinedList() throws IOException, SQLException {
        //SPOT PANE: Spot Lists
        spotJoinedList.paneList.getChildren().clear();

        if(user.getCountJoinedSpots() != 0){
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
                    } catch (IOException | SQLException | ParseException ex) {
                        ex.printStackTrace();
                    }
                });
                jSpots.add(jSpot);
            }

            spotJoinedList.paneList.getChildren().addAll(jSpots);
        }

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
            System.out.println( "Join " + spotInfo.spotName.getText());
            joinSpot(spotInfo.spotID);
            //HERE
        });
        spotInfo.spotCPostBtn.setOnMousePressed(e->{
            System.out.println( "Create Posts ");
            try {
                addPost(spotID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

    void addPost(String spotID) throws IOException {
        Post addPostDialog = new Post();

        Dialog<String> dialog = new Dialog<>();
        dialog.setDialogPane(addPostDialog);
        dialog.setTitle("NightSpot");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(logo);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, e -> {
            String title = addPostDialog.txtName.getText();
            String htmlText = addPostDialog.postEditor.getHtmlText();

            if( title.length() > 60 || title.isEmpty() || addPostDialog.postEditor.getHtmlText().isEmpty() || addPostDialog.postEditor.getHtmlText().isBlank() ){
                e.consume();
            }else{
                //METHOD
                try {
                    sql.createPosts(user, spotID, title, htmlText);
                    System.out.println("CREATED POST: " + title);
                    sessionSelectedSpots(spotID);
                } catch (SQLException | IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }

    void joinSpot(String spotID){

    }
    void showReply(CardComment comment) throws SQLException, IOException, ParseException {
        if(!comment.cComRepliesCount.getText().equals("0 replies")){
            if(comment.cComShowBtn.getText().equals("SHOW")){
                comment.cComShowBtn.setText("HIDE");

                ArrayList<CardReply> replies = sql.getReplies(comment.commentID);
                comment.replyPane.getChildren().addAll(replies);

            }else if(comment.cComShowBtn.getText().equals("HIDE")){
                comment.cComShowBtn.setText("SHOW");

                comment.replyPane.getChildren().clear();
            }
        }
    }

    void addReply(CardComment comment) throws IOException {
        Reply addReplyDialog = new Reply();

        Dialog<String> dialog = new Dialog<>();
        dialog.setDialogPane(addReplyDialog);
        dialog.setTitle("NightSpot");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(logo);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, e -> {
            String message = addReplyDialog.txtMessage.getText();
            if( message.length() > 75 || message.isEmpty()){
                e.consume();
            }else{
                //METHOD
                try {
                    sql.createReplies(user, message, comment);
                    System.out.println("CREATED REPLY: ");

                    String []lastcount = comment.cComRepliesCount.getText().split(" ");
                    int count = Integer.parseInt(lastcount[0]);
                    count++;
                    comment.cComRepliesCount.setText(count + " replies");

                    if(comment.cComShowBtn.getText().equals("HIDE")){
                        comment.replyPane.getChildren().clear();
                        ArrayList<CardReply> replies = sql.getReplies(comment.commentID);
                        comment.replyPane.getChildren().addAll(replies);
                    }


                } catch (SQLException | IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }

    void addComment() throws IOException {
        Comment addCommentDialog = new Comment();

        Dialog<String> dialog = new Dialog<>();
        dialog.setDialogPane(addCommentDialog);
        dialog.setTitle("NightSpot");

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(logo);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, e -> {
            String message = addCommentDialog.txtMessage.getText();

            if( message.length() > 550 || message.isEmpty()){
                e.consume();
            }else{
                //METHOD
                try {
                    sql.createComments(user, message, contentMainPost.postID, contentMainPost.spotID);
                    System.out.println("CREATED COMMENT: ");
                    //

                    String []lastcount = contentMainPost.cMainPostComments.getText().split(" ");
                    int count = Integer.parseInt(lastcount[0]);
                    count++;
                    contentMainPost.cMainPostComments.setText(count + " COMMENTS");

                    ArrayList<CardComment> comments = sql.getComments(contentMainPost.postID);

                    contentMainPost.commentPane.getChildren().clear();

                    for (CardComment comment: comments) {
                        comment.cComShowBtn.setOnMousePressed(event -> {
                            try {
                                showReply(comment);
                            } catch (SQLException | IOException | ParseException ex) {
                                ex.printStackTrace();
                            }
                        });
                        comment.cComReplyBtn.setOnMousePressed(event ->{
                            try {
                                addReply(comment);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }

                    contentMainPost.commentPane.getChildren().addAll(comments);
                } catch (SQLException | IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }


}


