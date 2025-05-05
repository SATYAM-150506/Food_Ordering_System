package com.foodorder.dsa;
import com.foodorder.model.Order;
import java.io.Serializable;
import java.util.Stack;
public class OrderHistoryStack implements Serializable {
    private static final long serialVersionUID=1L;

    private Stack<Order>  orderHistory;
     public OrderHistoryStack(){
         this.orderHistory=new Stack<>();
     }

     //add order to stack
    public void push(Order order){
         orderHistory.push(order);
         System.out.println("Order added to history: "+ order.getOrderId());
    }

    //pop order from stack
    public Order pop(){
         if(orderHistory.isEmpty()){
             System.out.println("No order history available.");
             return null;
         }
         Order lastOrder=orderHistory.pop();
        System.out.println("Remove last order: "+ lastOrder.getOrderId());
        return lastOrder;
    }

    public Order peek(){
         if(orderHistory.isEmpty()) return null;
         return orderHistory.peek();
    }

    public boolean isEmpty(){
         return orderHistory.isEmpty();
    }

    //display order history

    public void displayHistory(){
         if(orderHistory.isEmpty()){
             System.out.println("No past orders.");
             return;
         }
        System.out.println("Order History (Most recent first):");
         Stack<Order> temp=new Stack<>();
         while(!orderHistory.isEmpty()){
             Order a= orderHistory.pop();
             System.out.println("- "+ a.getOrderId() + "by"+a.getUser().getName());
             temp.push(a);
         }

         //restoring original stack
         while(!temp.isEmpty()){
             orderHistory.push(temp.pop());
         }
    }
    public int size(){
         return orderHistory.size();
    }
}
