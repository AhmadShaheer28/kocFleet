package com.kocfleet.model;

public class ExcelCellModel {
    private String value;
    private String color;

    public ExcelCellModel(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public ExcelCellModel() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
