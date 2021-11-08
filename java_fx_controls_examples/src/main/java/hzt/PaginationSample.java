package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaginationSample extends Application {

    private static final int INIT_PAGE_INDEX = 0;
    public static final int ITEMS_PER_PAGE = 8;
    private static final int NUMBER_OF_PAGES = 28;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(final Stage stage) throws Exception {
        Pagination pagination = new Pagination(NUMBER_OF_PAGES, INIT_PAGE_INDEX);
        pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(PaginationSample::createPage);

        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 10.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);
        Scene scene = new Scene(anchor);
        stage.setScene(scene);
        stage.setTitle("PaginationSample");
        stage.show();
    }

    private static VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex * ITEMS_PER_PAGE;
        for (int i = page; i < page + ITEMS_PER_PAGE; i++) {
            VBox element = new VBox();
            Hyperlink link = new Hyperlink("Item " + (i + 1));
            link.setVisited(true);
            Label text = new Label("Search results\nfor " + link.getText());
            element.getChildren().addAll(link, text);
            box.getChildren().add(element);
        }
        return box;
    }
}
