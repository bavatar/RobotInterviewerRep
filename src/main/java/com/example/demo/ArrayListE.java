package com.example.demo;

import java.util.ArrayList;
import java.util.Date;

// Extends ArrayList with Tags
public class ArrayListE {
    public long userID;
    public ArrayList<Job> arrList;

    public ArrayListE() {
        userID = 0;
        Date interviewDateTime = new Date();
        arrList = new ArrayList<>();
    }
}

