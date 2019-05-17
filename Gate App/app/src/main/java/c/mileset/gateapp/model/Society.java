package c.mileset.gateapp.model;

public class Society {
    private String s_id;
    private String s_name;

    public Society() {
    }

    public Society(String s_id, String s_name) {
        this.s_id = s_id;
        this.s_name = s_name;
    }

//s_id
    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

//s_name
    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    @Override
    public String toString() {
        return s_name;
    }
}
