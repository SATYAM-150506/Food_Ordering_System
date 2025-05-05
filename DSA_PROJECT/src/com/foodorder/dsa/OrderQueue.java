package com.foodorder.dsa;

//importing order from model
import com.foodorder.model.Order;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue implements Serializable {
    private static final long serialVersionUID=1L;

    private Queue<Order> orderQueue;

    public OrderQueue(){
        this.orderQueue=new LinkedList<>();
    }

    //add new order in queue

    public void enqueue(Order order){
        orderQueue.add(order);
        System.out.println("Order added to queue: " + order.getOrderId());
    }

    //remove first order from queue
    public Order dequeue(){
        if(orderQueue.isEmpty()){
            System.out.println("No orders to process.");
            return null;
        }
        Order processed=orderQueue.poll();
        System.out.println("Processing order: " + processed.getOrderId());
        return processed;
    }

    public Order peek(){
        return orderQueue.peek();
    }

    public  boolean isEmpty(){
        return orderQueue.isEmpty();
    }

    public int size(){
        return orderQueue.size();
    }

    public void displayQueue(){
        if(orderQueue.isEmpty()){
            System.out.println("No pending orders...");
            return ;
        }
        System.out.println("Pending Orders: ");
        for(Order order : orderQueue){
            System.out.println("- "+ order.getOrderId()+ " for "+ order.getUser().getName());
        }
    }
}
