package com.example.heronation.mypage;

import java.util.ArrayList;

public class ExpandableGroup {
    public ArrayList<String> child;
    public String groupName;
    ExpandableGroup(String name){
        groupName = name;
        child = new ArrayList<String>();
    }
}
