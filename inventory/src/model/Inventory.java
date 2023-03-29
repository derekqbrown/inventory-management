package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Inventory class.
 * @author Derek Brown
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * adds part to inventory
     * @param newPart part to add
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * adds product to inventory
     * @param newProduct product to add
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * lookup a part by id
     * @param partId id of part to lookup
     * @return part with matching id
     */
    public static Part lookupPart(int partId){
        for (int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getId() == partId){
                return allParts.get(i);
            }
        }
        return null;
    }
    /**
     * lookup a product by id
     * @param productId id of product to lookup
     * @return product matching the id
     */
    public static Product lookupProduct(int productId){
        for (int i = 0; i < allProducts.size(); i++){
            if (allProducts.get(i).getId() == productId){
                return allProducts.get(i);
            }
        }
        return null;
    }

    /**
     * lookup parts by name
     * @param partName name of part to lookup
     * @return list of parts with that name
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> filteredPartList = FXCollections.observableArrayList();
        for (int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getName() == partName){
                filteredPartList.add(allParts.get(i));
            }
        }
        return filteredPartList;
    }

    /**
     * lookup products by name
     * @param productName name of product to lookup
     * @return list of products with that name
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> filteredProductList = FXCollections.observableArrayList();
        for (int i = 0; i < allProducts.size(); i++){
            if (allProducts.get(i).getName() == productName){
                filteredProductList.add(allProducts.get(i));
            }
        }
        return filteredProductList;
    }

    /**
     * updates part
     * RUNTIME ERROR - I fixed a logical issue here. It would cause an issue because a InHouse object requires a Machine ID, whereas the Outsourced object requires a Company Name. I had to find a way to identify which type of Part was being passed in here. I fixed it by using instanceof to apply the appropriate setters for each class.
     * @param index the index of the part to update
     * @param selectedPart part with updated values
     */
    public static void updatePart(int index, Part selectedPart){
        if (selectedPart instanceof Outsourced) {
            if (allParts.get(index) instanceof Outsourced){
                ((Outsourced) allParts.get(index)).setCompanyName(((Outsourced) selectedPart).getCompanyName());
                allParts.get(index).setId(selectedPart.getId());
                allParts.get(index).setName(selectedPart.getName());
                allParts.get(index).setPrice(selectedPart.getPrice());
                allParts.get(index).setStock(selectedPart.getStock());
                allParts.get(index).setMax(selectedPart.getMax());
                allParts.get(index).setMin(selectedPart.getMin());
            }
            else {
                allParts.add(selectedPart);
                allParts.remove(index);
            }
        }

        else if (selectedPart instanceof InHouse){
            if (!(allParts.get(index) instanceof InHouse)) {
                allParts.add(selectedPart);
                allParts.remove(index);
            }
            else {
                ((InHouse) allParts.get(index)).setMachineId(((InHouse) selectedPart).getMachineId());
                allParts.get(index).setId(selectedPart.getId());
                allParts.get(index).setName(selectedPart.getName());
                allParts.get(index).setPrice(selectedPart.getPrice());
                allParts.get(index).setStock(selectedPart.getStock());
                allParts.get(index).setMax(selectedPart.getMax());
                allParts.get(index).setMin(selectedPart.getMin());
            }
        }
    }

    /**
     * updates product
     * @param index index of product to update
     * @param newProduct product with updated values
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.get(index).setId(newProduct.getId());
        allProducts.get(index).setName(newProduct.getName());
        allProducts.get(index).setPrice(newProduct.getPrice());
        allProducts.get(index).setStock(newProduct.getStock());
        allProducts.get(index).setMax(newProduct.getMax());
        allProducts.get(index).setMin(newProduct.getMin());

    }

    /**
     * delete part from inventory
     * @param selectedPart the part to delete
     * @return true if the part was deleted
     */
    public static boolean deletePart(Part selectedPart){
        for (int i = 0; i < allParts.size(); i++){
            if (allParts.get(i).getId() == selectedPart.getId()){
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * delete product from inventory
     * @param selectedProduct the product to delete
     * @return true if the product was deleted
     */
    public static boolean deleteProduct(Product selectedProduct){
        for (int i = 0; i < allProducts.size(); i++){
            if (allProducts.get(i).getId() == selectedProduct.getId()){
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return list of all parts in the inventory
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @return list of all products in the inventory
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
