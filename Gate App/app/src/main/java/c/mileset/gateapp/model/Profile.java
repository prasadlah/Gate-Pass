package c.mileset.gateapp.model;

public class Profile{

    private String gender;
    private String adhar_number;
    private String image_url;

    public Profile() {
    }

    public Profile(String gender, String adhar_number, String image_url) {
        this.gender = gender;
        this.adhar_number = adhar_number;
        this.image_url = image_url;
    }

//gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//date of birth
    public String getAdhar_number() {
        return adhar_number;
    }

    public void setAdhar_number(String adhar_number) {
        this.adhar_number= adhar_number;
    }

//image url
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
