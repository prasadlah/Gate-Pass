package c.mileset.gateapp.model;

public class Vehicle {
    private String id, vehicleNumber;

    public Vehicle() {
    }

    public Vehicle(String id, String vehicleNumber) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
    }

//id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//vehicle number
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
