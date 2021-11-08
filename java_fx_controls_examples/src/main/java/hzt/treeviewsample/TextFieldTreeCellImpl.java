package hzt.treeviewsample;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

final class TextFieldTreeCellImpl extends TreeCell<String> {

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
            textField = createTextField();
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

    private TextField createTextField() {
        var newTextField = new TextField(getString());
        newTextField.setOnKeyReleased(this::executeChange);
         return newTextField;
    }

    private void executeChange(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            commitEdit(textField.getText());
        }
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            cancelEdit();
        }
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}
