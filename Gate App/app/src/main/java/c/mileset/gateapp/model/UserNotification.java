package c.mileset.gateapp.model;

import com.google.firebase.firestore.Exclude;

public class UserNotification {
    private String notificationId;
    private String pass_id;
    private String reg_id;
    private String scan_time;
    private String in_entry_time;
    private int permission;

    public UserNotification() {
    }

    public UserNotification(String pass_id, String reg_id, String scan_time, String in_entry_time, int permission) {
        this.pass_id = pass_id;
        this.reg_id = reg_id;
        this.scan_time = scan_time;
        this.in_entry_time = in_entry_time;
        this.permission = permission;
    }

//notification id
    @Exclude
    public String getNotificationId() {
        return notificationId;
    }

    @Exclude
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    //
//gate pass id
    public String getPass_id() {
        return pass_id;
    }

    public void setPass_id(String pass_id) {
        this.pass_id = pass_id;
    }

//reg id
    public String getReg_id() {
        return reg_id;
    }

    public void setReg_id(String reg_id) {
        this.reg_id = reg_id;
    }

//scan time
    public String getScan_time() {
        return scan_time;
    }

    public void setScan_time(String scan_time) {
        this.scan_time = scan_time;
    }

//entry time
    public String getIn_entry_time() {
        return in_entry_time;
    }

    public void setIn_entry_time(String in_entry_time) {
        this.in_entry_time = in_entry_time;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
