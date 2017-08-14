package br.com.irweb.ajshf.Entities;

import java.util.Date;
import java.util.List;

/**
 * Created by Igor on 25/05/2017.
 */

public class Order {
    
    public float ValueDelivery;
    public boolean Pickup;
    public int DayOfWeek;
    public int Period;
    public int PaymentMethod;
    //troco
    public float ChangeOfMoney;
    public float TotalValue;
    public float TotalValueDiscount;
    public int PromotionId;
    public float ValueDiscountPromotion;
    public Date DeliveryDate;
    public Integer IdAddress;
    //public Address Address;
    public Integer IdNeighborhood;
    public List<ItemOrder> Items;
    public String Observation;
}
