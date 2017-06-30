package br.com.irweb.ajshf.Entities;

import java.util.List;

/**
 * Created by Igor on 28/06/2017.
 */

public class ItemOrder {
    public int MenuId;
    public int Quantity;
    public boolean CustomMenu;
    public List<CustomItemOrder> CustomItems;
    public float Value;
    public String Name;
}
