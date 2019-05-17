package c.mileset.gateapp.model;

public class Feedback extends Common {
    private String feedback;

    public Feedback() {
    }

    public Feedback(String feedback) {
        setId(getId());
        this.feedback = feedback;
    }

//feedback
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}