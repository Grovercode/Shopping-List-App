package com.example.shoppylist.Model;

public class Grocery {
    private String name;
    private String quantity;
    private String dateitemAdded;
    private int id;

    public Grocery() {
    }

    public Grocery(String name, String quantity, String dateitemAdded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateitemAdded = dateitemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateitemAdded() {
        return dateitemAdded;
    }

    public void setDateitemAdded(String dateitemAdded) {
        this.dateitemAdded = dateitemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
