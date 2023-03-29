package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
/**
 * This is the AddPart class. This is the controller for the Add Part form.
 * @author Derek Brown
 */
public class AddPart implements Initializable {

    private static Part part;

    public RadioButton inHouseBtn;
    public RadioButton outsourcedBtn;

    public Label errorAlert;
    public Label inOrOut;

    public TextField partIdAdd;
    public TextField partNameAdd;
    public TextField partInvAdd;
    public TextField partPriceAdd;
    public TextField partMaxAdd;
    public TextField partMinAdd;
    public TextField partMachId;

    /**
     * Initializes the add part controller
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inHouseBtn.setSelected(true);
        part = new InHouse(00,"", -1, -1, -1, -2, -1);
        partIdAdd.setText(Integer.toString(Inventory.getAllParts().size() + 1));

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
     * and if they do, saves the part
     * @param actionEvent when the "Save" button is clicked
     * @throws IOException
     */
    public void savePart(ActionEvent actionEvent)  throws IOException {
        errorAlert.setText("");
        String errorsStr = "";

        Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save this part and return to main menu?", ButtonType.YES, ButtonType.NO);
        ButtonType response = removeAlert.showAndWait().orElse(ButtonType.NO);
        if (ButtonType.YES.equals(response)) {

            String name = "";
            if (validateStr(partNameAdd.getText()))
                try{name = partNameAdd.getText();} catch (Exception e) {
                    errorsStr += "Invalid Name\n";
                }
            if (name == "" || name.length() < 1) errorsStr += "Name cannot be empty\n";

            int inv = -1;
            if (validateInt(partInvAdd.getText()) && partInvAdd.getText() != null)
                try {inv = Integer.parseInt(partInvAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Stock\n";
                }

            double price = -1.0;
            if (validateDouble(partPriceAdd.getText()) && partPriceAdd.getText() != null)
                try {price = Double.parseDouble(partPriceAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Price\n";
                }
            if (price < 0) errorsStr += "Price must be greater than 0\n";

            int max = -1;
            if (validateInt(partMaxAdd.getText()))
                try{max = Integer.parseInt(partMaxAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Max\n";
                }
            if (max < 0) errorsStr += "Max must be greater than 0\n";

            int min = -2;
            if (validateInt(partMinAdd.getText()))
                try{min = Integer.parseInt(partMinAdd.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Min\n";
                }
            if (min < 0) errorsStr += "Min must be greater than 0\n";
            if (min > max) errorsStr += "Min cannot be greater than Max\n";
            if (inv > max) errorsStr += "Stock cannot be greater than Max\n";
            if (inv < min) errorsStr += "Stock cannot be less than Min\n";
            if (part instanceof InHouse){
                int machId = -1;
                if (validateInt(partMachId.getText()))
                    try{machId = Integer.parseInt(partMachId.getText());} catch (Exception e) {
                        errorsStr += "Invalid Machine ID\n";
                    }
                if (machId < 0) errorsStr += "Machine ID must be a number greater than 0\n";


                if (name == "" || inv < min || inv > max || price <= 0 || min < 0 || max < min || machId < 0){//
                    errorAlert.setText(errorsStr);
                }
                else {
                    part.setId(Inventory.getAllParts().size() + 1);
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMax(max);
                    part.setMin(min);
                    ((InHouse) part).setMachineId(machId);

                    Inventory.addPart(part);

                    toMain(actionEvent);
                }
            }
            else if (part instanceof Outsourced){
                String supplier = "";
                if (validateStr(partMachId.getText()))
                    try{supplier = partMachId.getText();} catch (Exception e) {
                        errorsStr += "Invalid Supplier\n";
                    }
                if (supplier == "" || supplier.length() < 1) errorsStr += "Invalid Supplier";

                if (name == "" || inv < min || inv > max || price <= 0 || min < 0 || max < min || supplier.length() < 1){//
                    errorAlert.setText(errorsStr);
                }
                else {

                    part.setId(Integer.parseInt(partIdAdd.getText()));
                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMax(max);
                    part.setMin(min);
                    ((Outsourced) part).setCompanyName(supplier);

                    Inventory.addPart(part);

                    toMain(actionEvent);
                }
            }
        }

    }
    /**
     * This method is activated when the radio button for "InHouse" is selected and changes the part to InHouse class
     */
    public void changeToInHouse() {
        part = new InHouse(00,"", -1, -1, -1, -2, -1);
        inHouseBtn.setSelected(true);
        outsourcedBtn.setSelected(false);
        inOrOut.setText("Machine ID");
    }
    /**
     * This method is activated when the radio button for "Outsourced" is selected and changes the part to Outsourced class
     */
    public void changeToOutsourced() {
        part = new Outsourced(00,"", -1, -1, -1, -2, "");
        outsourcedBtn.setSelected(true);
        inHouseBtn.setSelected(false);
        inOrOut.setText("Supplier");
    }
}
