package com.foodorder.dsa;

import com.foodorder.model.MenuItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
public class MenuDSAStore implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer,MenuItem> menuMap;

    public MenuDSAStore(){
        menuMap=new HashMap<>();
    }

    public void addItem(MenuItem item){
        menuMap.put(item.getId(),item);
    }

    public void updateItem(int id,String newName,double newPrice){
        MenuItem item=menuMap.get(id);
        item.setName(newName);
        item.setPrice(newPrice);
    }

    public void removeItem(int id){
        menuMap.remove(id);
    }

    public MenuItem getItem(int id){
        return menuMap.get(id);
    }

    public Collection<MenuItem> getAllItems(){
        return menuMap.values();
    }

    public boolean containsItem(int id){
        return menuMap.containsKey(id);
    }

    public boolean isEmpty(){
        return menuMap.isEmpty();
    }

}
