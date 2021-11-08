package hzt.treeviewsample;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class TreeViewSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/treeviewsample/";

    private static final String SALES = "Sales Department";
    private static final String IT_SUPPORT = "IT Support";
    private static final String ACCOUNTS_DEPARTEMENT = "Accounts Department";

    private final Node rootIcon = Optional.ofNullable(getClass()
            .getResourceAsStream(RELATIVE_RESOURCES_DIR + "root.png"))
            .map(Image::new)
            .map(ImageView::new)
            .orElse(new ImageView());

    private final Image depIcon = Optional.ofNullable(getClass()
                    .getResourceAsStream(RELATIVE_RESOURCES_DIR + "department.png"))
            .map(Image::new)
            .orElseThrow();


    private final List<Employee> employees = List.of(
            new Employee("Ethan Williams", SALES),
            new Employee("Emma Jones", SALES),
            new Employee("Michael Brown", SALES),
            new Employee("Anna Black", SALES),
            new Employee("Rodger York", SALES),
            new Employee("Susan Collins", SALES),
            new Employee("Mike Graham", IT_SUPPORT),
            new Employee("Judy Mayer", IT_SUPPORT),
            new Employee("Gregory Smith", IT_SUPPORT),
            new Employee("Jacob Smith", ACCOUNTS_DEPARTEMENT),
            new Employee("Isabella Johnson", ACCOUNTS_DEPARTEMENT));

    private final TreeItem<String> rootNode = new TreeItem<>("MyCompany Human Resources", rootIcon);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        rootNode.setExpanded(true);
        for (Employee employee : employees) {
            TreeItem<String> empLeaf = new TreeItem<>(employee.name());
            boolean found = false;
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(employee.department())) {
                    depNode.getChildren().add(empLeaf);
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(employee.department(), new ImageView(depIcon));
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(empLeaf);
            }
        }

        stage.setTitle("Tree View Sample");
        VBox box = new VBox();
        final Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        TreeView<String> treeView = new TreeView<>(rootNode);
        treeView.setShowRoot(true);
        treeView.setEditable(true);
        treeView.setCellFactory(p -> new TextFieldTreeCellImpl());

        box.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }

    private record Employee(String name, String department) {
    }
}
