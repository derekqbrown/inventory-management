package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * This is the AddProduct class. This is the controller for the Add Product form.
 * @author Derek Brown
 */
public class AddProduct implements Initializable {

    private Product product = new Product(-1, "", -1, -1, -1, -1);

    public TextField prodIdAdd;
    public TextField prodNameAdd;
    public TextField prodInvAdd;
    public TextField prodPriceAdd;
    public TextField prodMaxAdd;
    public TextField prodMinAdd;

    public Label errorAlert;
    public Label successAlert;
    public TextField searchParts;

    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TableView partsTable;

    public TableColumn prodIdCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public TableView productsTable; //holds parts associated with product

    /**
     * initialize the controller
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        prodIdAdd.setText(Integer.toString(Inventory.getAllProducts().size() + 1));
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(product.getAllAssociatedParts());

        successAlert.setText("");
        errorAlert.setText("");
    }
    /**
     * searchParts method searches and filters the tables based on part name or id
     */
    public void searchParts(){
        successAlert.setText("");
        errorAlert.setText("");
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for (Part part : Inventory.getAllParts()){
            if (validateStr(searchParts.getText())){
                if (part.getName().contains(searchParts.getText())){
                    filteredParts.add(part);
                }
                else if (Integer.toString(part.getId()).contains(searchParts.getText())){
                    filteredParts.add(part);
                }
            }
        }
        partsTable.setItems(filteredParts);
        if (filteredParts.size() < 1){
            errorAlert.setText("No matching parts were found");
        }
    }

    /**
     * Prompts the user to confirm cancellation. Asks the user if they want to cancel and return to the main menu.
     * @param actionEvent activated via the cancel button and passes to toMain method.
     * @throws IOException
     */
    public void cancelBtn(ActionEvent actionEvent) throws IOException{
        Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel and return to main menu?", ButtonType.YES, ButtonType.NO);
        ButtonType response = removeAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {

            toMain(actionEvent);
        }
    }
    /**
     * This method returns to the main menu
     * @param actionEvent activated via the save or cancel buttons
     * @throws IOException
     */
    public void toMain(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 1100, 450));
        stage.show();
    }
    /**
     * This method adds a part to the product's associated part list
     */
    public void addPartToProd() {
        successAlert.setText("");
        errorAlert.setText("");
        int selectedItem;
        try {
            selectedItem = ((Part) partsTable.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = -1;
        }

        if (selectedItem > -1){
            product.addAssociatedPart(Inventory.lookupPart(selectedItem));
        }
        else {
            errorAlert.setText("Part could not be added");
        }
    }
    /**
     * This method removes a part from the product's associated part list
     */
    public void removePart() {
        successAlert.setText("");
        errorAlert.setText("");
        Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove the associated part?", ButtonType.YES, ButtonType.NO);
        ButtonType response = removeAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            int selectedIndex;
            try {
                selectedIndex = productsTable.getSelectionModel().getSelectedIndex();
            } catch (Exception e) {
                selectedIndex = -1;
            }
            if (selectedIndex > -1) {
                product.deleteAssociatedPart(product.getAllAssociatedParts().get(selectedIndex));
                successAlert.setText("Successfully removed associated part.");
            } else {
                successAlert.setText("");
                errorAlert.setText("Part could not be removed");
            }
        }
    }
    /**
     * this method validates a string's characters
     * @param string the string to validate
     * @return true if all characters are valid
     */
    public boolean validateStr(String string){
        Pattern pattern = Pattern.compile("^[ a-zA-Z0-9]*$");
        if (pattern.matcher(string).find()){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * this method validates an integer value
     * @param string the string containing a possible integer value
     * @return true if the string is an integer
     */
    public boolean validateInt(String string){
        Pattern pattern = Pattern.compile("^[0-9]*$");
        if (pattern.matcher(string).find()){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * this method validates a double value
     * @param string the string containing a possible double value
     * @return true if the string is a double or an integer
     */
    public boolean validateDouble(String string){
        Pattern pattern = Pattern.compile("^[+]?([0-9]*.[0-9]+|[0-9]+)?$");
        if (pattern.matcher(string).find()){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * This method checks each of the textfields to ensure they have valid input,
     * and if they do, saves the product
     * @param actionEvent when the "Save" button is clicked
     * @throws IOException
     */
    public void saveProduct(ActionEvent actionEvent) throws IOException {

        successAlert.setText("");
        errorAlert.setText("");
        String errorsStr = "";

        Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save the product and return to main menu?", ButtonType.YES, ButtonType.NO);
        ButtonType response = removeAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {


            String name = "";
            if (validateStr(prodNameAdd.getText()))
                try{name = prodNameAdd.getText();} catch (Exception e) {
                    errorsStr += "Invalid Name\n";
                }
            if (name == "" || name.length() < 1) errorsStr += "Name cannot be empty\n";

            int inv = -1;
            if (validateInt(prodInvAdd.getText()) && prodInvAdd.getText() != null)
                try {inv = Integer.parseInt(prodInvAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Stock.\n";
                }

            double price = -1.0;
            if (validateDouble(prodPriceAdd.getText()) && prodPriceAdd.getText() != null)
                try {price = Double.parseDouble(prodPriceAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Price.\n";
                }
            if (price < 0) errorsStr += "Price must be greater than 0\n";

            int max = -1;
            if (validateInt(prodMaxAdd.getText()))
                try{max = Integer.parseInt(prodMaxAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Max.\n";
                }
            if (max < 0) errorsStr += "Max must be greater than 0\n";

            int min = -2;
            if (validateInt(prodMinAdd.getText()))
                try{min = Integer.parseInt(prodMinAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Min\n";
                }
            if (min < 0) errorsStr += "Min must be greater than 0\n";
            if (min > max) errorsStr += "Min cannot be greater than Max\n";
            if (inv > max) errorsStr += "Stock cannot be greater than Max\n";
            if (inv < min) errorsStr += "Stock cannot be less than Min\n";

            if (name == "" || inv < min || inv > max || price <= 0 || min < 0 || max < min){//
                errorAlert.setText(errorsStr + "Product could not be saved");
            }
            else {
                product.setId(Inventory.getAllProducts().size() + 1);
                product.setName(name);
                product.setStock(inv);
                product.setPrice(price);
                product.setMax(max);
                product.setMin(min);

                Inventory.addProduct(product);

                toMain(actionEvent);
            }
        }
    }
}
