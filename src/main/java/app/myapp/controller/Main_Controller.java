package app.myapp.controller;

import app.myapp.Main;
import app.myapp.controller.component.Content_ListofPosts;
import app.myapp.controller.component.Content_ListofSpots;
import app.myapp.controller.component.ContentMainPost;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.StackedFontIcon;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class Main_Controller {
    // TODO
    // DATABASE: Reset per week and sample data

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
    Content_ListofSpots contentDynamic = new Content_ListofSpots();

    Content_ListofPosts contentListofPosts = new Content_ListofPosts();
    Spot_JoinedList spotJoinedList = new Spot_JoinedList();
    ContentMainPost contentMainPost;

    //METHODS
    public Main_Controller() throws IOException {
        UserData userData = UserData.getInstance();
        this.user = userData.getUser();
    }

    @FXML
    void initialize() throws IOException, SQLException, ParseException {
        //Set Username to the MenuBar
        menuUser.setText(user.getUsername());

        //Default to Home
        sessionHome();
    }

    /*******************
     * CURRENT SESSION *
     *******************/

    void sessionHome() throws IOException, SQLException, ParseException {
        txtSearch.clear();
        viewContent_Top();
        viewSpotJoinedList();
    }

    void sessionSelectedSpots(String spotID) throws IOException, SQLException, ParseException {
        txtSearch.clear();
        viewContent_PostList(spotID);
        viewSpotInfo(spotID);
    }

    void sessionSelectedPosts(String spotID, String postID) throws IOException, SQLException, ParseException {
        txtSearch.clear();
        viewContent_MainPost(spotID, postID);
        viewSpotInfo(spotID);
    }

    /******************
     *  Switch Panes  *
     ******************/

    void viewContent_MainPost(String spotID, String postID) throws IOException, SQLException, ParseException {
        txtSearch.clear();
        //CONTENT PANE: Main Post
        Query sql = new Query();

        contentMainPost = sql.getPostInfo(user, spotID, postID);

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

        contentMainPost.cMainPostUpBtn.setOnMousePressed(e->{
            try {
                sql.changeVote(user, contentMainPost, "up");
                user.updateData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentMainPost.cMainPostDownBtn.setOnMousePressed(e->{
            try {
                sql.changeVote(user, contentMainPost, "down");
                user.updateData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        if(!contentMainPost.cMainPostComments.getText().equals("0 COMMENTS")){
            ArrayList<CardComment> comments = sql.getComments(user,postID);

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
                comment.cComUpBtn.setOnMousePressed(e ->{
                    try {
                        sql.changeVote(user,comment,"up");
                        user.updateData();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });
                comment.cComDownBtn.setOnMousePressed(e ->{
                    try {
                        sql.changeVote(user,comment,"down");
                        user.updateData();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            contentMainPost.commentPane.getChildren().addAll(comments);
        }

        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentMainPost);

    }

    void viewContent_Top() throws IOException, SQLException, ParseException {
        //CONTENT PANE: Top spots and posts
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentDynamic);

        //Initial
        contentDynamic.cListSpotTitle.setText("Recommended for you");
        contentDynamic.sIcon.setIconLiteral("mdi2c-cards-heart");
        contentDynamic.searchText.setText("");

        contentDynamic.PostBtn.setTextFill(Paint.valueOf("#E0D3DE"));
        contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#DC4731"));
        getTopSpots();

        contentDynamic.PostBtn.setOnMousePressed(e->{
            contentDynamic.PostBtn.setTextFill(Paint.valueOf("#DC4731"));
            contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#E0D3DE"));
            try {
                getTopPosts();
            } catch (SQLException | IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });
        contentDynamic.SpotBtn.setOnMousePressed(e->{
            contentDynamic.PostBtn.setTextFill(Paint.valueOf("#E0D3DE"));
            contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#DC4731"));
            try {
                getTopSpots();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    void viewContent_Search() throws IOException, SQLException, ParseException {
        //CONTENT PANE: Searched spots and posts
        contentPane.getChildren().clear();
        contentPane.getChildren().add(contentDynamic);


        //Initial
        String text = txtSearch.getText();
        contentDynamic.cListSpotTitle.setText("Search results");
        contentDynamic.sIcon.setIconLiteral("mdi2a-arrow-right-bold-circle");
        contentDynamic.searchText.setText(text);

        contentDynamic.PostBtn.setTextFill(Paint.valueOf("#E0D3DE"));
        contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#DC4731"));
        getSearchSpots(text);

        contentDynamic.PostBtn.setOnMousePressed(e->{
            contentDynamic.PostBtn.setTextFill(Paint.valueOf("#DC4731"));
            contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#E0D3DE"));
            try {
                getSearchPosts(text);
            } catch (SQLException | IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        contentDynamic.SpotBtn.setOnMousePressed(e->{
            contentDynamic.PostBtn.setTextFill(Paint.valueOf("#E0D3DE"));
            contentDynamic.SpotBtn.setTextFill(Paint.valueOf("#DC4731"));
            try {
                getSearchSpots(text);
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });

    }

    void viewContent_PostList(String spotID) throws SQLException, IOException, ParseException {
        //CONTENT PANE: List of Posts

        contentListofPosts.cPostReturn.setOnMousePressed(e ->{
            try {
                sessionHome();
            } catch (IOException | SQLException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        contentListofPosts.cLatestBtn.setOnMousePressed(e->{
            try {
                getPosts(spotID,1);
                contentListofPosts.cPopularBtn.setTextFill(Paint.valueOf("#E0D3DE"));
                contentListofPosts.cLatestBtn.setTextFill(Paint.valueOf("#DC4731"));
            } catch (SQLException | IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });
        contentListofPosts.cPopularBtn.setOnMousePressed(e->{
            try {
                getPosts(spotID,0);
                contentListofPosts.cPopularBtn.setTextFill(Paint.valueOf("#DC4731"));
                contentListofPosts.cLatestBtn.setTextFill(Paint.valueOf("#E0D3DE"));
            } catch (SQLException | IOException | ParseException ex) {
                ex.printStackTrace();
            }
        });

        getPosts(spotID, 0);
        contentListofPosts.cPopularBtn.setTextFill(Paint.valueOf("#DC4731"));
        contentListofPosts.cLatestBtn.setTextFill(Paint.valueOf("#E0D3DE"));

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

        Spot_Info spotInfo = sql.getSpotInfo(user, spotID);

        //ACTIONS
        spotInfo.spotJoinBtn.setOnAction(e->{
            spotInfo.switchState();
            try {
                joinSpot(spotInfo.spotID);
                viewSpotInfo(spotInfo.spotID);
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });
        spotInfo.spotCPostBtn.setOnMousePressed(e->{
            try {
                addPost(spotID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        spotPane.getChildren().clear();
        spotPane.getChildren().add(spotInfo);

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
                    String spotID = sql.createSpots(user, name, desc, rules);
                    System.out.println("CREATED SPOT: " + name);
                    user.updateData();
                    sessionSelectedSpots(spotID);
                } catch (SQLException | IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }

    @FXML
    void onHome(MouseEvent event) throws IOException, SQLException, ParseException {
        sessionHome();
    }

    @FXML
    void search(MouseEvent event) throws IOException, SQLException, ParseException {
        if(!txtSearch.getText().isEmpty()){
            viewContent_Search();
            contentPane.requestFocus();
        }
    }

    @FXML
    void signout(ActionEvent event) {
        Stage mainStage = (Stage) this.btnAdd.getScene().getWindow();

        Stage loginStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/userLoginView.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        loginStage.getIcons().add(new Image(String.valueOf(Main.class.getResource("objects\\logo_ph.png"))));
        loginStage.setScene(scene);
        loginStage.setTitle("NightSpot");
        loginStage.setResizable(false);
        loginStage.resizableProperty().setValue(Boolean.FALSE);

        mainStage.close();
        loginStage.show();
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
                    String postID = sql.createPosts(user, spotID, title, htmlText);
                    System.out.println("CREATED POST: " + title);
                    sessionSelectedPosts(spotID,postID);
                } catch (SQLException | IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        dialog.showAndWait();
    }

    void joinSpot(String spotID) throws SQLException, IOException {
        sql.joinSpot(user, spotID);
        user.updateData();
    }

    void getPosts(String spotID, int z) throws SQLException, IOException, ParseException {
        //GET CURRENT DATABASE DATA
        ArrayList<CardPost> postCards = sql.getPosts(user, spotID,z);

        for (CardPost post: postCards) {
            post.cPostLink.setOnMousePressed(e -> {
                try {
                    sessionSelectedPosts(post.spotID, post.postID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });

            post.cPostCardUpBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "up");
                    user.updateData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            post.cPostCardDownBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "down");
                    user.updateData();
                } catch (SQLException ex) {
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

                    ArrayList<CardComment> comments = sql.getComments(user,contentMainPost.postID);

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
                        comment.cComUpBtn.setOnMousePressed(event ->{
                            try {
                                sql.changeVote(user,comment,"up");
                                user.updateData();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        });
                        comment.cComDownBtn.setOnMousePressed(event ->{
                            try {
                                sql.changeVote(user,comment,"down");
                                user.updateData();
                            } catch (SQLException ex) {
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



    void getTopSpots() throws IOException, SQLException {
        //GET CURRENT DATABASE DATA
        ArrayList<CardSpot> spotCards = sql.getTopSpots(user);

        for (CardSpot spot: spotCards) {
            spot.cardSpotJoinBtn.setOnMousePressed(e->{
                spot.switchState();
                try {
                    joinSpot(spot.spotID);
                    viewSpotJoinedList();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            });

            spot.cardSpotTitle.setOnMousePressed(e->{
                try {
                    sessionSelectedSpots(spot.spotID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SET UP THE PAGINATION
        int countSpots = spotCards.size();
        int noPage = Math.max(countSpots, 1);
        noPage = (int) Math.ceil((double) noPage / 5);

        contentDynamic.cPageSpots.setMaxPageIndicatorCount(3);
        contentDynamic.cPageSpots.setPageCount(noPage);

        System.out.println("POST COUNT: " + countSpots + " | PAGES: " + noPage);
        ArrayList<PageContainer> containers = new ArrayList<>();

        if(countSpots != 0){
            //PAGINATION VBOX LAYER
            int j = 0;

            //LOOP PER PAGE
            for (int i = 0; i < noPage; i++) {
                PageContainer container = new PageContainer();

                //LOOP THROUGH POSTS
                for (int x = 0; j < countSpots; ) {
                    container.getChildren().add(spotCards.get(j));
                    x++;
                    j++;
                    if(x == 5 || j == countSpots ){
                        containers.add(container);
                        //RESET PER CONTAINER
                        break;
                    }
                }
            }

            //SET CHILDREN OF THE PAGINATION
            contentDynamic.cPageSpots.setPageFactory(containers::get);

        }else {
            PageContainer empty = new PageContainer();
            contentDynamic.cPageSpots.setPageFactory((pageindex) -> {
                return empty;
            });
        }
    }

    void getTopPosts() throws SQLException, IOException, ParseException {
        //GET CURRENT DATABASE DATA
        ArrayList<CardPost> postCards = sql.getTopPosts(user);

        for (CardPost post: postCards) {
            post.cPostLink.setOnMousePressed(e -> {
                try {
                    sessionSelectedPosts(post.spotID, post.postID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });

            post.cPostCardUpBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "up");
                    user.updateData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            post.cPostCardDownBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "down");
                    user.updateData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SET UP THE PAGINATION
        int countPosts = postCards.size();
        int noPage = Math.max(countPosts, 1);
        noPage = (int) Math.ceil((double) noPage / 5);

        contentDynamic.cPageSpots.setMaxPageIndicatorCount(3);
        contentDynamic.cPageSpots.setPageCount(noPage);

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
            contentDynamic.cPageSpots.setPageFactory(containers::get);

        }else {
            PageContainer empty = new PageContainer();
            contentDynamic.cPageSpots.setPageFactory((pageindex) -> {
                return empty;
            });
        }



    }

    void getSearchSpots(String text) throws IOException, SQLException {
        //GET CURRENT DATABASE DATA
        ArrayList<CardSpot> spotCards = sql.getMatchedSpots(user,text);

        for (CardSpot spot: spotCards) {
            spot.cardSpotJoinBtn.setOnMousePressed(e->{
                spot.switchState();
                try {
                    joinSpot(spot.spotID);
                    viewSpotJoinedList();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            });

            spot.cardSpotTitle.setOnMousePressed(e->{
                try {
                    sessionSelectedSpots(spot.spotID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SET UP THE PAGINATION
        int countSpots = spotCards.size();
        int noPage = Math.max(countSpots, 1);
        noPage = (int) Math.ceil((double) noPage / 5);

        contentDynamic.cPageSpots.setMaxPageIndicatorCount(3);
        contentDynamic.cPageSpots.setPageCount(noPage);

        System.out.println("POST COUNT: " + countSpots + " | PAGES: " + noPage);
        ArrayList<PageContainer> containers = new ArrayList<>();

        if(countSpots != 0){
            //PAGINATION VBOX LAYER
            int j = 0;

            //LOOP PER PAGE
            for (int i = 0; i < noPage; i++) {
                PageContainer container = new PageContainer();

                //LOOP THROUGH POSTS
                for (int x = 0; j < countSpots; ) {
                    container.getChildren().add(spotCards.get(j));
                    x++;
                    j++;
                    if(x == 5 || j == countSpots ){
                        containers.add(container);
                        //RESET PER CONTAINER
                        break;
                    }
                }
            }

            //SET CHILDREN OF THE PAGINATION
            contentDynamic.cPageSpots.setPageFactory(containers::get);

        }else {
            PageContainer empty = new PageContainer();
            contentDynamic.cPageSpots.setPageFactory((pageindex) -> {
                return empty;
            });
        }

    }

    void getSearchPosts(String text) throws SQLException, IOException, ParseException {
        //GET CURRENT DATABASE DATA
        ArrayList<CardPost> postCards = sql.getMatchedPosts(user,text);

        for (CardPost post: postCards) {
            post.cPostLink.setOnMousePressed(e -> {
                try {
                    sessionSelectedPosts(post.spotID, post.postID);
                } catch (IOException | SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            });

            post.cPostCardUpBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "up");
                    user.updateData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            post.cPostCardDownBtn.setOnMousePressed(e->{
                try {
                    sql.changeVote(user, post, "down");
                    user.updateData();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        }

        //SET UP THE PAGINATION
        int countPosts = postCards.size();
        int noPage = Math.max(countPosts, 1);
        noPage = (int) Math.ceil((double) noPage / 5);

        contentDynamic.cPageSpots.setMaxPageIndicatorCount(3);
        contentDynamic.cPageSpots.setPageCount(noPage);

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
            contentDynamic.cPageSpots.setPageFactory(containers::get);

        }else {
            PageContainer empty = new PageContainer();
            contentDynamic.cPageSpots.setPageFactory((pageindex) -> {
                return empty;
            });
        }

    }
}


