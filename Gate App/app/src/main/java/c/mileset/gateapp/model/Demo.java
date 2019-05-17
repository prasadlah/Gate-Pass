package c.mileset.gateapp.model;

public class Demo {

    private String name;
    private String imgUrl;

    public Demo() {
    }

    public Demo(String name, String imgUrl) {
        if(name.trim().equals("")){
            name = "no name";
        }
        this.name = name;
        this.imgUrl = imgUrl;
    }

//name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//image url
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
