package br.com.irweb.ajshf.Entities;

import java.util.List;

/**
 * Created by Igor on 25/05/2017.
 */

public class ItemMenuOrder {
    public int MenuId;
    public int Quantity;
    public boolean CustomMenu;
    public List<CustomItemMenuOrder> CustomItems;
}
