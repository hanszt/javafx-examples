package hzt.hyperlinkwebviewsample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HyperlinkWebViewSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/hyperlinksample/";

    static final String[] imageFiles = {
        "product.png",
        "education.png",
        "partners.png",
        "support.png"
    };
    static final String[] captions = {
        "Products",
        "Education",
        "Partners",
        "Support"
    };

    static final String[] urls = new String[]{
        "http://www.oracle.com/us/products/index.html",
        "http://education.oracle.com/",
        "http://www.oracle.com/partners/index.html",
        "http://www.oracle.com/us/support/index.html"
    };
    
    final ImageView selectedImage = new ImageView();
    final Hyperlink[] hyperlinks = new Hyperlink[captions.length];
    final Image[] images = new Image[imageFiles.length];   

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox);
        stage.setTitle("Hyperlink Sample");
        stage.setWidth(570);
        stage.setHeight(550);

        selectedImage.setLayoutX(100);
        selectedImage.setLayoutY(10);
        
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        for (int i = 0; i < captions.length; i++) {
            final Hyperlink hpl = hyperlinks[i] = new Hyperlink(captions[i]);
            final Image image = images[i] =
                new Image(getClass().getResourceAsStream(RELATIVE_RESOURCES_DIR + imageFiles[i]));
            hpl.setGraphic(new ImageView (image));
            hpl.setFont(Font.font("Arial", 14));
            final String url = urls[i];

            hpl.setOnAction(e -> webEngine.load(url));
        }
              
        HBox hbox = new HBox();
        hbox.getChildren().addAll(hyperlinks);
        vbox.getChildren().addAll(hbox, browser);
        VBox.setVgrow(browser, Priority.ALWAYS);
        
        stage.setScene(scene);
        stage.show();
    }
}
