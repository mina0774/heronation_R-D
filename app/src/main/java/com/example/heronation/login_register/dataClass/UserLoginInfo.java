package com.example.heronation.login_register.dataClass;

public class UserLoginInfo {
    public String access_token;
    public String token_type;
    public String refresh_token;
    public String member_id;
    public String member_name;
    public Integer expires_in;

    public UserLoginInfo(String access_token, String token_type, String refresh_token, String member_id, String member_name, Integer expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.member_id = member_id;
        this.member_name = member_name;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }


    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }


}
