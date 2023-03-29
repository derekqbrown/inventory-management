package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


/**
 * This is the ModPart class. This is the controller for the Modify Part form.
 * @author Derek Brown
 */
public class ModPart implements Initializable {

    private int partId;

    private static int invIndex;
    private static Part part;

    public RadioButton inHouse;
    public RadioButton outsourced;
    public Label errorAlert;

    public TextField partIdMod;
    public TextField partNameMod;
    public TextField partInvMod;
    public TextField partPriceMod;
    public TextField partMaxMod;
    public TextField partMinMod;
    public TextField partMachId;

    public Label inOrOut;

    /**
     * Initializes the modify part controller
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (part instanceof InHouse){
            inHouse.setSelected(true);
            outsourced.setSelected(false);
            inOrOut.setText("In-house");
            partMachId.setText(Integer.toString(((InHouse) part).getMachineId()));
        }
        else{
            outsourced.setSelected(true);
            inHouse.setSelected(false);
            inOrOut.setText("Supplier");
            partMachId.setText(((Outsourced) part).getCompanyName());
        }

        partIdMod.setText(Integer.toString(part.getId()));
        partNameMod.setText(part.getName());
        partInvMod.setText(Integer.toString(part.getStock()));
        partPriceMod.setText(Double.toString(part.getPrice()));
        partMaxMod.setText(Integer.toString(part.getMax()));
        partMinMod.setText(Integer.toString(part.getMin()));

        partId = Integer.parseInt(partIdMod.getText());

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
            if (validateStr(partNameMod.getText()))
                try{name = partNameMod.getText();} catch (Exception e) {
                    errorsStr += "Invalid Name\n";
                }
            if (name == "" || name.length() < 1) errorsStr += "Name cannot be empty\n";

            int inv = -1;
            if (validateInt(partInvMod.getText()) && partInvMod.getText() != null)
                try {inv = Integer.parseInt(partInvMod.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Stock\n";
                }

            double price = -1.0;
            if (validateDouble(partPriceMod.getText()) && partPriceMod.getText() != null)
                try {price = Double.parseDouble(partPriceMod.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Price\n";
                }
            if (price < 0) errorsStr += "Price must be greater than 0\n";

            int max = -1;
            if (validateInt(partMaxMod.getText()))
                try{max = Integer.parseInt(partMaxMod.getText());} catch (NumberFormatException e) {
                    errorsStr += "Invalid value for Max\n";
                }
            if (max < 0) errorsStr += "Max must be greater than 0\n";

            int min = -2;
            if (validateInt(partMinMod.getText()))
                try{min = Integer.parseInt(partMinMod.getText());} catch (NumberFormatException e) {
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

                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMax(max);
                    part.setMin(min);
                    ((InHouse) part).setMachineId(machId);

                    Inventory.updatePart(getInvIndex(), part);

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

                    part.setName(name);
                    part.setStock(inv);
                    part.setPrice(price);
                    part.setMax(max);
                    part.setMin(min);
                    ((Outsourced) part).setCompanyName(supplier);

                    Inventory.updatePart(getInvIndex(), part);

                    toMain(actionEvent);
                }
            }
        }
    }

    /**
     * This method is activated when the radio button for "InHouse" is selected and changes the part to InHouse class
     */
    public void changeToInHouse() {
        part = new InHouse(partId,"", -1, -1, -1, -2, -1);
        inHouse.setSelected(true);
        outsourced.setSelected(false);
        inOrOut.setText("Machine ID");
    }
    /**
     * This method is activated when the radio button for "Outsourced" is selected and changes the part to Outsourced class
     */
    public void changeToOutsourced() {
        part = new Outsourced(partId,"", -1, -1, -1, -2, "");
        outsourced.setSelected(true);
        inHouse.setSelected(false);
        inOrOut.setText("Supplier");
    }

    /**
     *
     * @return the index for the current part being modified
     */
    public int getInvIndex() {
        return invIndex;
    }

    /**
     *
     * @param invIndex the index for the part being modified
     */
    public void setInvIndex(int invIndex) {
        this.invIndex = invIndex;
    }

    /**
     * sets the part being modified and allows the textfields to be pre-filled
     * @param part part selected to be modified
     */
    public static void setModPart(Part part) {
        ModPart.part = part;
    }

}
