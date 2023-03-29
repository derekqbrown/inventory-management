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
 * MainMenu class for the main menu. This is the first menu when the program launches.
 * @author Derek Brown
 */
public class MainMenu implements Initializable {
    public TextField searchParts;
    public TextField searchProducts;
    public Label alertLabel;
    public Label successAlert;

    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TableView partsTable;

    public TableColumn prodIdCol;
    public TableColumn prodNameCol;
    public TableColumn prodInvCol;
    public TableColumn prodPriceCol;
    public TableView productsTable;


//    private static boolean firstInitialize = false;

    /**
     * initializes the main menu
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        if (firstInitialize == false){ //Uncomment this and the firstInitialize variable above if testing
//
//            /* dummy data for testing*/
////            Outsourced dummyPart;
////            dummyPart = new Outsourced(0, "DummyOutsource", 1.98, 10, 5, 50, "Nice");
////            Inventory.addPart(dummyPart); //had to fix a null pointer exception here
////            InHouse dummyPart2 = new InHouse(300, "Such cool Part", 14.99, 50, 31, 45, 10);
////            Inventory.addPart(dummyPart2);
////
////            Product dummyProd;
////            dummyProd = new Product(0, "DummyProduct", 15.67, 5, 0, 50);
////            dummyProd.addAssociatedPart(dummyPart);
////            Inventory.addProduct(dummyProd);
////            Product dummyProd2 = new Product(100, "This Is cool", 23.97, 80, 40, 100);
////            dummyProd2.addAssociatedPart(dummyPart);
////            dummyProd2.addAssociatedPart(dummyPart2);
////            Inventory.addProduct(dummyProd2);
//            /* end dummy data */
//
//            firstInitialize = true;
//        }
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(Inventory.getAllProducts());

        alertLabel.setText("");
        successAlert.setText("");
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
     * searches and filters the tables based on part name or id
     */
    public void searchParts(){
        alertLabel.setText("");
        successAlert.setText("");
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
            alertLabel.setText("No matching parts were found");
        }
    }
    /**
     *  searches and filters the tables based on product name or id
     */
    public void searchProducts(){
        alertLabel.setText("");
        successAlert.setText("");
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product product : Inventory.getAllProducts()){
            if (validateStr(searchProducts.getText())){
                if (product.getName().contains(searchProducts.getText())){
                    filteredProducts.add(product);
                }
                else if (Integer.toString(product.getId()).contains(searchProducts.getText())){
                    filteredProducts.add(product);
                }
            }
        }
        productsTable.setItems(filteredProducts);
        if (filteredProducts.size() < 1){
            alertLabel.setText("No matching products were found");
        }
    }

    /**
     * deletes a part from the inventory
     */
    public void deletePart() {
        alertLabel.setText("");
        successAlert.setText("");
        int selectedItem;
        try {
            selectedItem = ((Part) partsTable.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = -1;
        }
        if (selectedItem > -1) {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete the part?", ButtonType.YES, ButtonType.NO);

            ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(response)) {

                try{
                    Inventory.deletePart(Inventory.lookupPart(selectedItem));
                    searchParts();
                    alertLabel.setText("");
                    successAlert.setText("Successfully deleted part.");
                } catch (Exception e) {
                    alertLabel.setText("An error occurred. The part was not deleted");
                }

            /* this code checks if the part is associated with a product and denies deletion if so */
    //            if (selectedItem >= 0){
    //                ObservableList<Part> inventoryParts = Inventory.getAllParts();
    //                ObservableList<Product> inventoryProducts = Inventory.getAllProducts();
    //                boolean inProduct = false;
    //
    //                for (int i = 0; i <inventoryParts.size(); i++) {
    //                    Product tempProduct;
    //                    Part part = inventoryParts.get(i);
    //                    ObservableList<Part> tempPartList;
    //                    for (int k = 0; k < inventoryProducts.size(); k++) {
    //                        tempProduct = inventoryProducts.get(k);
    //                        tempPartList = tempProduct.getAllAssociatedParts();
    //                        inProduct = false;
    //                        for (int j = 0; j < tempPartList.size(); j++) {
    //                            if (tempPartList.get(j) == part) {
    //                                inProduct = true;
    //                                break;
    //                            }
    //                        }
    //                    }
    //                }
    //                if(!inProduct){
    //                    try{
    //                        Inventory.deletePart((Part) partsTable.getSelectionModel().getSelectedItem());
    //                    } catch (Exception e) {
    //
    //                    }
    //                    successAlert.setText("Part was successfully removed!");
    //                }
    //                else{
    //                    alertLabel.setText("Error! Deletion failed - part is associated with a product");
    //                }
    //            }
    //        }

            }
        }
        else {
            alertLabel.setText("Nothing selected. Select a part and try again");
        }

    }

    /**
     * deletes a product if no parts are associated with it
     */
    public void deleteProd() {
        alertLabel.setText("");
        successAlert.setText("");
        int selectedItem = -1;
        try {
            selectedItem = ((Product) productsTable.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = -1;

        }
        if (selectedItem > -1){
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete the product?", ButtonType.YES, ButtonType.NO);
            ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
            if (ButtonType.YES.equals(response)) {

                Product prodToDelete = Inventory.lookupProduct(selectedItem);
                if (prodToDelete.getAllAssociatedParts().size() < 1){
                    try {
                        Inventory.deleteProduct(prodToDelete);
                        searchProducts();
                        alertLabel.setText("");
                        successAlert.setText("Product was successfully removed!");
                    } catch (Exception e) {
                        alertLabel.setText("Error! Product could not be deleted");
                        successAlert.setText("");
                    }
                }
                else {
                    alertLabel.setText("There are parts associated with this product. To delete, please remove all associated parts");
                }


            }
        }
        else {
            alertLabel.setText("Nothing selected. Select a product and try again");
        }


    }

    /**
     * goes to Add product form
     * @param actionEvent when the "Add" button is clicked below the product table
     * @throws IOException
     */
    public void toAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Add Product");
        stage.setScene(new Scene(root, 800, 525));
        stage.show();
    }

    /**
     * goes to Modify Product form
     * @param actionEvent when the "Modify" button is clicked below the product table
     * @throws IOException
     */
    public void toModProduct(ActionEvent actionEvent) throws IOException {
        int selectedItem;
        try {
            selectedItem = ((Product) productsTable.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = -1;
        }
        if (selectedItem >= 0){
            ModProduct newMod = new ModProduct();
            newMod.setInvIndex(Inventory.getAllProducts().indexOf(Inventory.lookupProduct(selectedItem)));
            newMod.setModProduct(Inventory.lookupProduct(selectedItem));

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModProduct.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root, 800, 525));
            stage.show();
        }
        else{
            alertLabel.setText("Select a product from the table, then try again");
        }
    }
    /**
     * goes to Add part form
     * @param actionEvent when the "Add" button is clicked below the part table
     * @throws IOException
     */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPart.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Add Part");
        stage.setScene(new Scene(root, 525, 525));
        stage.show();
    }
    /**
     * goes to Modify Part form
     * @param actionEvent when the "Modify" button is clicked below the part table
     * @throws IOException
     */
    public void toModPart(ActionEvent actionEvent) throws IOException {

        int selectedItem;
        try {
            selectedItem = ((Part) partsTable.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = -1;
        }
        if (selectedItem >= 0) {
            ModPart newMod = new ModPart();

            newMod.setModPart(Inventory.lookupPart(selectedItem));
            newMod.setInvIndex(Inventory.getAllParts().indexOf(Inventory.lookupPart(selectedItem)));

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ModPart.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root, 525, 525));
            stage.show();
        }
        else{
            alertLabel.setText("Select a part from the table, then try again");
        }
    }

    /**
     * prompts the user to exit the program when the "Exit" button is clicked
     */
    public void exitProgram() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to close the program?", ButtonType.YES, ButtonType.NO);
        ButtonType response = exitAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {
            System.exit(0);
        }
    }
}
