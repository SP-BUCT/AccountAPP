package com.example.zzyyff.flowerrecords;

public class class_Credit {
    int id;
    String name;
    String type;
    float balance;
    String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public class_Credit(int id,String name,String type,float balance,String remarks) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.remarks = remarks;
    }
}
