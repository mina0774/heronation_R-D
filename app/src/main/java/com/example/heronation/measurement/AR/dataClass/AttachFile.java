package com.example.heronation.measurement.AR.dataClass;

public class AttachFile {
    private String temp_name;
    private String real_name;

    public  AttachFile(String temp_name, String real_name){
        this.temp_name=temp_name;
        this.real_name=real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setTemp_name(String temp_name) {
        this.temp_name = temp_name;
    }

    public String getTemp_name() {
        return temp_name;
    }
}
