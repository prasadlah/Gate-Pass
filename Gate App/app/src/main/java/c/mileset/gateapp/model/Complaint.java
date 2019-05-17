package c.mileset.gateapp.model;

import com.google.firebase.firestore.Exclude;

public class Complaint {
    private String c_id;
    private String subject;
    private String complaint;

    public Complaint() {
    }

    public Complaint(String c_id, String subject, String complaint) {
        this.c_id = c_id;
        this.subject = subject;
        this.complaint = complaint;
    }

//id
    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

//subject
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

//complaint
    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }
}
