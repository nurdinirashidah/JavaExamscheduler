package com.ams;


import java.util.ArrayList;

/**
 * Created by shimon on 8/26/18.
 */
public class Student {
    private static int id;
    private int selfId;
    public String name;
    public ArrayList<String> subjects;

    public Student(String name) {
        this.name = name;
        this.selfId = id;
        subjects = new ArrayList<String>();
        id++;
    }

    public int getId() {
        return selfId;
    }
}
