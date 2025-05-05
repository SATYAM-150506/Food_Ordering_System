package com.foodorder.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID=1L;

    private int orderId;
    private User user;
    private List<MenuItem> items;
    private double total;
    private LocalDateTime time;
    private double discount;
    private double finalAmount;


    public Order( int orderId,User user,List<MenuItem> items,double total,LocalDateTime time,double discount,double finalAmount){
        this.orderId=orderId;
        this.user=user;
        this.items=items;
        this.total=total;
        this.time=time;
        this.discount = discount;
        this.finalAmount = finalAmount;
    }

    public int getOrderId(){
        return orderId;
    }

    public  User getUser(){
        return user;
    }

    public List<MenuItem> getItems(){
        return items;
    }

    public double getTotal(){
        return  total;
    }

    public LocalDateTime getTime(){
        return time;
    }
    public double getDiscount() {
        return discount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }


    //setters
    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public void setUser(User user){
        this.user=user;
    }

    public void setItems(List<MenuItem> items){
        this.items=items;
    }

    public void setTotal(double total){
        this.total=total;
    }

    public void setTime(LocalDateTime time){
            this.time=time;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Order Id: ").append(orderId).append("\n");
        sb.append("User: ").append(user.getName()).append("\n");
        sb.append("Items:\n");
        for(MenuItem item : items){
            sb.append("  - ").append(item.toString()).append("\n");
        }
        sb.append("Total: $").append(total).append("\n");
        sb.append("Time: ").append(time.toString()).append("\n");
        sb.append("Discount: ₹").append(String.format("%.2f", discount)).append("\n");
        sb.append("Final Amount: ₹").append(String.format("%.2f", finalAmount)).append("\n");
        return sb.toString();
    }
}
