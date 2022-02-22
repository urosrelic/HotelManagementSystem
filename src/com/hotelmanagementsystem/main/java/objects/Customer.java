package com.hotelmanagementsystem.main.java.objects;

public class Customer {
    private String name, email, mobileNum, address, checkInDate, roomType, bed, checkOutDate, isPaid;
    private int id, roomNum, daysStaying;
    private double pricePerDay, totalAmount;

    public Customer(int id, String name, String email, String mobileNum, String address, String checkInDate,
                      int roomNum, String roomType,
                      String bed, double pricePerDay, int daysStaying, double totalAmount,
                      String checkOutDate, String isPaid) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
        this.address = address;
        this.checkInDate = checkInDate;
        this.roomType = roomType;
        this.bed = bed;
        this.checkOutDate = checkOutDate;
        this.roomNum = roomNum;
        this.pricePerDay = pricePerDay;
        this.daysStaying = daysStaying;
        this.totalAmount = totalAmount;
        this.isPaid = isPaid;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getDaysStaying() {
        return daysStaying;
    }

    public void setDaysStaying(int daysStaying) {
        this.daysStaying = daysStaying;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNum='" + mobileNum + '\'' +
                ", address='" + address + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", roomType='" + roomType + '\'' +
                ", bed='" + bed + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", roomNum=" + roomNum +
                ", pricePerDay=" + pricePerDay +
                ", daysStaying=" + daysStaying +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
