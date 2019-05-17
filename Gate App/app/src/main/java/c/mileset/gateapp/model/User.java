package c.mileset.gateapp.model;

public class User {
    private String userId;
    private String name;
    private String mobileNumber;
    private String email;
    private String password;
    private int flag;

    public User() {
    }

    public User(String userId, String name, String mobileNumber, String email, String password, int flag) {
        this.userId = userId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.flag = flag;
    }

//user id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//mobile number
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

//email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//flag
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
