package model;

/**
 * This is the InHouse class. It extends the Part class.
 * @author Derek Brown
 */
public class InHouse extends Part{
    private int machineId;

    /**
     * constructor
     * @param id the id to set
     * @param name the name to set
     * @param price the price to set
     * @param stock the stock to set
     * @param min the min to set
     * @param max the max to set
     * @param machineId the machineId to set
     */
    public InHouse (int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);

    }

    /**
     *
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     *
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
