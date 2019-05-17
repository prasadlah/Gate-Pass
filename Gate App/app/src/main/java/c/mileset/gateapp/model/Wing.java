package c.mileset.gateapp.model;

public class Wing extends Society {
    private String w_id;
    private String wing;

    public Wing() {
    }

    public Wing(String w_id, String wing) {
        this.w_id = w_id;
        this.wing = wing;
    }

//w_id
    public String getW_id() {
        return w_id;
    }

    public void setW_id(String w_id) {
        this.w_id = w_id;
    }

//wing name
    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    @Override
    public String toString() {
        return wing;
    }
}
