package hzt.treeviewsample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class TreeViewSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/treeviewsample/";

    private static final String SALES = "Sales Department";
    private static final String IT_SUPPORT = "IT Support";
    private static final String ACCOUNTS_DEPARTEMENT = "Accounts Department";

    private final Node rootIcon = new ImageView(new Image(getClass()
            .getResourceAsStream(RELATIVE_RESOURCES_DIR + "root.png")));

    private final Image depIcon = new Image(getClass()
            .getResourceAsStream(RELATIVE_RESOURCES_DIR + "department.png"));


    List<Employee> employees = List.of(
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
    TreeItem<String> rootNode = new TreeItem<>("MyCompany Human Resources", rootIcon);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        rootNode.setExpanded(true);
        for (Employee employee : employees) {
            TreeItem<String> empLeaf = new TreeItem<>(employee.getName());
            boolean found = false;
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(employee.getDepartment())) {
                    depNode.getChildren().add(empLeaf);
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(employee.getDepartment(), new ImageView(depIcon));
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

    private static final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private final ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Add Employee");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(this::addNewEmployee);
        }

        private void addNewEmployee(ActionEvent t) {
            TreeItem<String> newEmployee = new TreeItem<>("New Employee");
            getTreeItem().getChildren().add(newEmployee);
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                        setContextMenu(addMenu);
                    }
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(this::executeChange);

        }

        private void executeChange(KeyEvent t) {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

    public static class Employee {

        private final StringProperty name;
        private final StringProperty department;

        private Employee(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
        }

        public String getName() {
            return name.get();
        }

        public String getDepartment() {
            return department.get();
        }
    }
}