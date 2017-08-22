package br.com.irweb.ajshf.Service;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.Entities.ItemOrder;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import br.com.irweb.ajshf.MainAJSActivity;

/**
 * Created by Igor on 01/08/2017.
 */

public class CartService {

    private UserAuthAJSHF user;
    private Order order;
    private Context context;

    public CartService(Context context) {
        order = AJSHFApp.getOrder();
        user = AJSHFApp.getInstance().getUser();
        this.context = context;
    }

    private void calcTotal() {
        float total = 0f;
        for (ItemOrder item :
                order.Items) {
            total += item.Value * item.Quantity;
        }
        order.TotalValue = total;
    }

    public void addItemOrder(Food food, int quantity) throws Exception {
        if (food == null) {
            throw new Exception("Food is null");
        }

        Order order = AJSHFApp.getOrder();
        if (order.Items == null) {
            order.Items = new ArrayList<ItemOrder>();
        }

        ItemOrder item = createItemOrder(food, quantity);

        ItemOrder found = getItemOrder(item.MenuId);

        if (found != null) {
            found.Quantity += item.Quantity;
        } else {
            order.Items.add(item);
        }
        calcTotal();
        notifyCartChanged();
    }

    private ItemOrder getItemOrder(int idFood) {
        for (ItemOrder item :
                order.Items) {
            if (item.MenuId == idFood)
                return item;
        }
        return null;
    }

    public void notifyCartChanged() {
        if (context instanceof MainAJSActivity) {
            ((MainAJSActivity) context).updateViewCart();
        }
    }

    public void removeItemOrder(int id) {
        Order order = AJSHFApp.getOrder();
        if (order.Items != null) {
            for (ItemOrder item :
                    order.Items) {
                if (item.MenuId == id) {
                    order.Items.remove(item);
                    break;
                }
            }
        }
        calcTotal();
        notifyCartChanged();
    }

    @NonNull
    private ItemOrder createItemOrder(Food food, int quantity) {
        ItemOrder item = new ItemOrder();
        item.CustomMenu = food.Custom;
        item.MenuId = food.Id;
        item.Name = food.Title;
        item.Quantity = quantity;
        item.Value = food.Value;
        if (food.Custom) {
            //custom items
        }
        return item;
    }
}
