package sumagoinfotech.ipt.task1.data;

import android.content.Context;
import sumagoinfotech.ipt.task1.helper.DbHelper;

public class UserBean{
    int id, profile_state;
    String email, user_name;
    DbHelper dbHelper;

    public UserBean(String email, String user_name, Context c){
        dbHelper = new DbHelper(c);
        this.email = email;
        this.user_name = user_name;
        this.id = dbHelper.getId(email, user_name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getProfile_state() {
        return profile_state;
    }

    public void setProfile_state(int profile_state) {
        this.profile_state = profile_state;
    }

}
