package br.com.irweb.ajshf.Entities;

import java.util.Date;
import java.util.List;

/**
 * Created by Igor on 22/05/2017.
 */

public class Food {

    public int Id;
    public String Title;
    public boolean Enable;
    public String Description;
    public double Value;
    public Date Start;
    public Date End;
    public String Image;
    public List<ItemFoodCustomModel> Items;
    public boolean Custom;
}
