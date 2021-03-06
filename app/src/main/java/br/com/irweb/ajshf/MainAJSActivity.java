package br.com.irweb.ajshf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.irweb.ajshf.Activity.LoginActivity;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import br.com.irweb.ajshf.Fragment.AboutFragment;
import br.com.irweb.ajshf.Fragment.AddressFragment;
import br.com.irweb.ajshf.Fragment.CartFragment;
import br.com.irweb.ajshf.Fragment.CloseOrderFragment;
import br.com.irweb.ajshf.Fragment.ListAddressFragment;
import br.com.irweb.ajshf.Fragment.MenuFragment;
import br.com.irweb.ajshf.Fragment.OrderOkFragment;

public class MainAJSActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView cartTotalView;
    private FloatingActionButton fab;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(MessageBus bus) {
        if (bus.className.equalsIgnoreCase(CartFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("fecharPedido")) {
                Fragment frag = CloseOrderFragment.newInstance();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.replace_fragment, frag, "closerOrder");
                transaction.addToBackStack("closeOrder");
                transaction.commit();

                mFirebaseAnalytics.setCurrentScreen(this, CloseOrderFragment.class.getName(), CloseOrderFragment.class.getName());
            }
        } else if (bus.className.equalsIgnoreCase(CloseOrderFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("pedidoFechado")) {
                Fragment frag = OrderOkFragment.newInstance(bus.additionalInfo);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.replace_fragment, frag, "finishOrder");
                transaction.commit();

                mFirebaseAnalytics.setCurrentScreen(this, OrderOkFragment.class.getName(), OrderOkFragment.class.getName());
                AJSHFApp.clearOrder();
                updateViewCart();
            }
        } else if (bus.className.equalsIgnoreCase(OrderOkFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("MenuInicial")) {

                Fragment frag = MenuFragment.newInstance();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.replace_fragment, frag, "menu");
                transaction.commit();

                mFirebaseAnalytics.setCurrentScreen(this, MenuFragment.class.getName(), MenuFragment.class.getName());
            }
        } else if (bus.className.equalsIgnoreCase(MenuFragment.class + "") || bus.className.equalsIgnoreCase(CloseOrderFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("tokenExpirado")) {
                executeLogout();
            }
        } else if (bus.className.equalsIgnoreCase(AddressFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("enderecoCadastrado")) {

                Fragment frag = MenuFragment.newInstance();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.replace_fragment, frag, "menu");
                transaction.commit();

                mFirebaseAnalytics.setCurrentScreen(this, MenuFragment.class.getName(), MenuFragment.class.getName());
            }
        } else if (bus.className.equalsIgnoreCase(ListAddressFragment.class + "")) {
            if (bus.message.equalsIgnoreCase("openAddress")) {

                openAddressFragment(bus.additionalInfo);
            }
            else if(bus.message.equalsIgnoreCase("addAddress")) {
                openAddressFragment(null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!VerifyIsLogged()) {
            return;
        }

        setContentView(R.layout.activity_main_ajs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCartFragment();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (AJSHFApp.getInstance().getAddressUser() != null && AJSHFApp.getInstance().getAddressUser().addresses != null && AJSHFApp.getInstance().getAddressUser().addresses.size() > 0) {
            MenuFragment menuFragment = MenuFragment.newInstance();

            transaction.add(R.id.replace_fragment, menuFragment, "menu");
            transaction.commit();
            mFirebaseAnalytics.setCurrentScreen(MainAJSActivity.this, MenuFragment.class.getName(), MenuFragment.class.getName());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Endereço");
            String str = String.format("Olá %s, tudo bem? Percebi que você não tem endereço cadastrado, e desta forma não podemos realizar entregas para você, mas mesmo assim você consegue fazer o pedido e ir retirar. Ou então crie um cadastro de endereço agora mesmo =)", AJSHFApp.getInstance().getUser().Name);
            builder.setMessage(str);
            builder.setCancelable(false);
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    MenuFragment menuFragment = MenuFragment.newInstance();

                    transaction.add(R.id.replace_fragment, menuFragment, "menu");
                    transaction.commit();

                    mFirebaseAnalytics.setCurrentScreen(MainAJSActivity.this, MenuFragment.class.getName(), MenuFragment.class.getName());
                }
            });
            builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddressFragment addressFragment = AddressFragment.newInstance();

                    transaction.add(R.id.replace_fragment, addressFragment, "address");
                    transaction.commit();

                    mFirebaseAnalytics.setCurrentScreen(MainAJSActivity.this, AddressFragment.class.getName(), AddressFragment.class.getName());
                }
            });
            builder.create().show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = navigationView.getHeaderView(0);
        TextView userName = (TextView) v.findViewById(R.id.user_name);
        TextView userMail = (TextView) v.findViewById(R.id.user_email);

        userName.setText(AJSHFApp.getInstance().getUser().Name + " " + AJSHFApp.getInstance().getUser().LastName);
        userMail.setText(AJSHFApp.getInstance().getUser().Email);

    }

    public void hideFAB() {
        fab.hide();
    }

    public void showFAB() {
        updateViewCart();

        if (AJSHFApp.getOrder().Items != null && AJSHFApp.getOrder().Items.size() > 0)
            fab.show();

    }

    public void updateViewCart() {
        Order order = AJSHFApp.getOrder();
        if (order != null && cartTotalView != null)
            cartTotalView.setText(String.format("R$ %s", order.TotalValue));

    }

    private boolean VerifyIsLogged() {
        UserAuthAJSHF user = AJSHFApp.getInstance().getUser();
        if (user == null) {
            ShowLoginActivity();

            finish();
            return false;
        }
        return true;
    }

    private void ShowLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);

        startActivity(it);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_aj, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View v = MenuItemCompat.getActionView(menuItem);
        cartTotalView = (TextView) v.findViewById(R.id.cart_total);

        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        updateViewCart();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_cart) {
            // Handle the camera action
            LoadCartFragment();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadCartFragment() {
        Fragment fragment = CartFragment.newIstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.replace_fragment, fragment, "cart");
        transaction.addToBackStack("cart");
        transaction.commit();

        mFirebaseAnalytics.setCurrentScreen(this, CartFragment.class.getName(), CartFragment.class.getName());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
            Fragment fragment = MenuFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.replace_fragment, fragment, "menu");
            transaction.commit();

            mFirebaseAnalytics.setCurrentScreen(this, MenuFragment.class.getName(), MenuFragment.class.getName());

        } else if (id == R.id.nav_address) {

            openListFragment();

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_logout) {
            executeLogout();
        } else if (id == R.id.nav_about) {
            Fragment fragment = AboutFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.replace_fragment, fragment, "about");
            transaction.commit();

            mFirebaseAnalytics.setCurrentScreen(this, AboutFragment.class.getName(), AboutFragment.class.getName());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openAddressFragment(String idAddress) {
        Fragment fragment = idAddress == null ? AddressFragment.newInstance() : AddressFragment.newInstance(idAddress);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.replace_fragment, fragment, "address");
        transaction.commit();

        mFirebaseAnalytics.setCurrentScreen(this, AddressFragment.class.getName(), AddressFragment.class.getName());
    }

    private void openListFragment() {
        Fragment fragment = ListAddressFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.replace_fragment, fragment, "listaddress");
        transaction.commit();

        mFirebaseAnalytics.setCurrentScreen(this, ListAddressFragment.class.getName(), ListAddressFragment.class.getName());
    }

    private void executeLogout() {
        AJSHFApp.getInstance().Logout();
        AJSHFApp.clearOrder();
        ShowLoginActivity();
        finish();
    }

}
