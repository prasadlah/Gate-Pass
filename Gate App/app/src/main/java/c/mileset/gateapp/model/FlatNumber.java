package c.mileset.gateapp.model;

public class FlatNumber {
    private String f_id;
    private String flat_number;

    public FlatNumber() {
    }

    public FlatNumber(String f_id, String flat_number) {
        this.f_id = f_id;
        this.flat_number = flat_number;
    }

//flat id
    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

//flat number
    public String getFlat_number() {
        return flat_number;
    }

    public void setFlat_number(String flat_number) {
        this.flat_number = flat_number;
    }

    @Override
    public String toString() {
        return flat_number;
    }
}
