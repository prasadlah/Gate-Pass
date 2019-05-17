package c.mileset.gateapp.model;

import com.google.firebase.firestore.Exclude;

public class GatePass {
    private String id, name, email, mobile, visit_date, visit_time, pass_date, pass_time, expire, imgUrl, pass_code;

    public GatePass() {
    }

    public GatePass(String id, String name, String email, String mobile, String visit_date, String visit_time, String pass_date, String pass_time, String expire, String imgUrl, String pass_code) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.visit_date = visit_date;
        this.visit_time = visit_time;
        this.pass_date = pass_date;
        this.pass_time = pass_time;
        this.expire = expire;
        this.imgUrl = imgUrl;
        this.pass_code = pass_code;
    }

//id
    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
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

//mobile
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

//visit date
    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

//visit time
    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

//pass date
    public String getPass_date() {
        return pass_date;
    }

    public void setPass_date(String pass_date) {
        this.pass_date = pass_date;
    }

//pass time
    public String getPass_time() {
        return pass_time;
    }

    public void setPass_time(String pass_time) {
        this.pass_time = pass_time;
    }

//expire
    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

//image url
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

//pass code
    public String getPass_code() {
        return pass_code;
    }

    public void setPass_code(String pass_code) {
        this.pass_code = pass_code;
    }
}