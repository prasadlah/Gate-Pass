package c.mileset.gateapp.model;

public class Family extends Common {
    private String name, email, mobileNumber, relation, occupation, imgUrl;

    public Family() {
    }

    public Family(String name, String email, String mobileNumber, String relation, String occupation, String imgUrl) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.relation = relation;
        this.occupation = occupation;
        this.imgUrl = imgUrl;
    }

//name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//mobile number
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

//relation
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

//occupation
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

//image url
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
