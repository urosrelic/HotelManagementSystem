package com.hotelmanagementsystem.main.java.objects;

public class Room {
    private int roomNum, status;
    private String roomType, bed;
    private double price;

    public Room(int roomNum, String roomType, String bed, double price, int status) {
        this.roomNum = roomNum;
        this.status = status;
        this.price = price;
        this.roomType = roomType;
        this.bed = bed;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public int getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getBed() {
        return bed;
    }


    @Override
    public String toString() {
        return "Room{" +
                "roomNum=" + roomNum +
                ", status=" + status +
                ", price=" + price +
                ", roomType='" + roomType + '\'' +
                ", bed='" + bed + '\'' +
                '}';
    }
}
