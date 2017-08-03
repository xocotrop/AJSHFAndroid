package br.com.irweb.ajshf.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Igor on 22/05/2017.
 */

public class Food implements Serializable {

    private static final long serialVersionUID = -4892961225387753734L;
    public int Id;
    public String Title;
    public boolean Enable;
    public String Description;
    public float Value;
    public Date Start;
    public Date End;
    public String Image;
    public List<ItemFoodCustomModel> Items;
    public boolean Custom;
}
