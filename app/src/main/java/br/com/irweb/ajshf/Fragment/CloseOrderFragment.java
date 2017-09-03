package br.com.irweb.ajshf.Fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.FreightService;
import br.com.irweb.ajshf.API.Service.OrderService;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.AddressUserAJSHF;
import br.com.irweb.ajshf.Entities.Freight;
import br.com.irweb.ajshf.Entities.ItemOrder;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.PaymentMethod;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOrderFragment extends Fragment {

    //region var global
    private RadioButton radioManha;
    private RadioButton radioTarde;
    private RadioButton radioNoite;
    private RadioGroup radioGroup;
    private Spinner spinnerAddress;
    private TextView textPeriodo;
    private TextView txtTxEntrega;
    private Spinner spinnerMethodPayment;
    private EditText editTextChangeMoney;
    private EditText editTextObservation;
    private CheckBox checkBoxPickup;
    private Button btnFinishOrder;
    private TextView textPickup;
    private ImageView imgPickupDelivery;
    private Button btnDate;
    private TextView txtDateSelected;
    private Calendar mCalendar;
    private TextView textLblAddress;
    private List<Freight> freights;

    private Order mOrder;
    private UserAuthAJSHF user;
    private AddressUserAJSHF addressUserAJSHF;

    private OrderService orderService;
    private FreightService freightService;
    private AlertDialog loadingDialog;
    private AlertDialog loadingDialogFreight;

    private static boolean ThreadFreightRunning = false;
    private int AddressSelected = 0;

    private Handler handler;

    private double totalFreight = 0;
    //endregion

    public CloseOrderFragment() {
        // Required empty public constructor
    }

    public static CloseOrderFragment newInstance() {
        CloseOrderFragment closeOrderFragment = new CloseOrderFragment();

        return closeOrderFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mOrder = AJSHFApp.getOrder();
        user = AJSHFApp.getInstance().getUser();
        addressUserAJSHF = AJSHFApp.getInstance().getAddressUser();

        View v = inflater.inflate(R.layout.fragment_close_order, container, false);

        initViews(v);
        initAdapter();

        setupButtonFinish();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlertDialog();
        createAlertDialogFreight();

        handler = new Handler();

    }

    private void runFreight() {
        if (AddressSelected == 0) {
            int position = spinnerAddress.getSelectedItemPosition();
            int idAddress = addressUserAJSHF.addresses.get(position).IdAddress;
            mOrder.IdAddress = idAddress;
        }
        //ta dando pau isso

        new TaskFreigh().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Estamos preparando a sua encomenda =). Aguarde mais um instante.");

        loadingDialog = builder.create();
    }

    private void createAlertDialogFreight() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Efetuando o calculo do frete");

        loadingDialogFreight = builder.create();
    }

    private void setupButtonFinish() {

        final Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        Calendar endCalendar = Calendar.getInstance();

        final Calendar preSelected = Calendar.getInstance();
        preSelected.add(Calendar.DAY_OF_MONTH, 1);


        final MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        final MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH) + 21);

        endCalendar.setTimeInMillis(maxDate.getDateInMillis());

        final SparseArray<MonthAdapter.CalendarDay> disabledDays = new SparseArray<>();

        while (now.before(endCalendar)) {
            if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                int key = Utils.formatDisabledDayForKey(now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                disabledDays.put(key, new MonthAdapter.CalendarDay(now));
            }
            now.add(Calendar.DATE, 1);
        }

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                mCalendar = Calendar.getInstance();
                                mCalendar.set(year, monthOfYear, dayOfMonth);

                                txtDateSelected.setText(String.format("%s/%s/%s", dayOfMonth, monthOfYear + 1, year));

                                mOrder.DeliveryDate = mCalendar.getTime();
                                Log.d("data", mCalendar.getTime() + "");
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setPreselectedDate(preSelected.get(Calendar.YEAR),
                                preSelected.get(Calendar.MONTH),
                                preSelected.get(Calendar.DAY_OF_MONTH))
                        .setDateRange(minDate, maxDate)
                        .setDoneText("Aplicar")
                        .setDisabledDays(disabledDays)
                        .setCancelText("Cancelar");

                cdp.show(getChildFragmentManager(), "picker");

            }
        });

        btnFinishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Observation = editTextObservation.getText().toString();
                if (mOrder.PaymentMethod == 0) {
                    if (!editTextChangeMoney.getText().toString().isEmpty()) {
                        mOrder.ChangeOfMoney = Float.valueOf(editTextChangeMoney.getText().toString());
                    }
                } else {
                    mOrder.ChangeOfMoney = 0f;
                }
                if (!validateOrder()) {
                    return;
                }
                new Tasks(getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        radioTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 1;
                textPeriodo.setText("Entre 13h30 - 18h00");
                updateFreight();
            }
        });

        radioManha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 0;
                textPeriodo.setText("Entre 9h - 12h");
                updateFreight();
            }
        });

        radioNoite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 2;
                textPeriodo.setText("Após as 18h");
                updateFreight();
            }
        });

        checkBoxPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOrder.Pickup = isChecked;
                if (isChecked) {
                    textPickup.setVisibility(View.VISIBLE);
                    mOrder.IdAddress = null;
                    mOrder.IdNeighborhood = null;
                    imgPickupDelivery.setImageResource(R.drawable.if_backpack_icon_1741326);
                    spinnerAddress.setVisibility(View.GONE);
                    mOrder.IdNeighborhood = null;
                    mOrder.IdAddress = null;
                    textLblAddress.setVisibility(View.GONE);
                    txtTxEntrega.setVisibility(View.INVISIBLE);
                    radioNoite.setVisibility(View.INVISIBLE);

                    if(radioNoite.isChecked()){
                        radioManha.setChecked(true);
                    }

                } else {
                    textPickup.setVisibility(View.INVISIBLE);
                    mOrder.IdAddress = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdAddress;
                    mOrder.IdNeighborhood = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdNeighborhood;
                    imgPickupDelivery.setImageResource(R.drawable.if_truck_1054949);
                    spinnerAddress.setVisibility(View.VISIBLE);
                    mOrder.IdAddress = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdAddress;
                    mOrder.IdNeighborhood = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdNeighborhood;
                    textLblAddress.setVisibility(View.VISIBLE);
                    txtTxEntrega.setVisibility(View.VISIBLE);
                    radioNoite.setVisibility(View.VISIBLE);
                }
                updateFreight();
            }
        });

        spinnerMethodPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOrder.PaymentMethod = position;
                if (position == 0) {
                    editTextChangeMoney.setVisibility(View.VISIBLE);
                } else {
                    editTextChangeMoney.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOrder.IdAddress = addressUserAJSHF.addresses.get(position).IdAddress;
                mOrder.IdNeighborhood = addressUserAJSHF.addresses.get(position).IdNeighborhood;
//                mOrder.IdCity = addressUserAJSHF.addresses.get(position).

                //Fazer aqui a parte de ver o frete se precisa disparar a thread, se já está carregado em memoria este frete
                if (AddressSelected == 0) {
                    AddressSelected = mOrder.IdAddress;
                } else if (!ThreadFreightRunning && AddressSelected != mOrder.IdAddress) {
                    AddressSelected = mOrder.IdAddress;


                    boolean foundFreight = false;
                    if (freights != null && freights.size() > 0) {
                        for (Freight f :
                                freights) {
                            if (f.IdAddress == AddressSelected) {
                                foundFreight = true;
                                break;
                            }
                        }
                        if (!foundFreight) {
                            runFreight();
                        } else {
                            updateFreight();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private boolean validateOrder() {
        boolean error = false;
        if (mOrder.Items == null || mOrder.Items.size() == 0) {
            Toast.makeText(getContext(), "Nenhum Item adicionado", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (mOrder.PaymentMethod == 0) {
            if (mOrder.ChangeOfMoney <= 0) {
                Toast.makeText(getContext(), "Qual o valor para troco?", Toast.LENGTH_SHORT).show();
                error = true;
            } else if (mOrder.ChangeOfMoney < mOrder.TotalValue + totalFreight) {
                double totalOrderWithFreight = mOrder.TotalValue + totalFreight;
                Toast.makeText(getContext(), String.format("Valor para troco menor que o Total. (R$ %.2f)", totalOrderWithFreight), Toast.LENGTH_SHORT).show();
                error = true;
            }
        }
        if (!mOrder.Pickup && (mOrder.IdAddress == null || mOrder.IdNeighborhood == null)) {
            error = true;
            Toast.makeText(getContext(), "Precisa ser selecionado o endereço", Toast.LENGTH_SHORT).show();
        }
        if (mOrder.DeliveryDate == null) {
            error = true;

            String textPickup = "entrega";

            if (mOrder.Pickup)
                textPickup = "retirada";

            Toast.makeText(getContext(), "Qual seria o dia da " + textPickup + "?", Toast.LENGTH_SHORT).show();
        }

        return !error;

    }

    private void initAdapter() {
        String[] adrs = getAddresses();
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, adrs);
        spinnerAddress.setAdapter(adapter);

        ArrayAdapter adapterPayment = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, PaymentMethod.METHODS);
        spinnerMethodPayment.setAdapter(adapterPayment);
    }

    private String[] getAddresses() {
        List<String> list = new ArrayList<>();
        for (Address address :
                addressUserAJSHF.addresses) {
            list.add(String.format("%s, %s - %s", address.Address, address.Number, address.Neighborhood));
        }
        String[] listStr = new String[list.size()];
        return list.toArray(listStr);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderService = new OrderService(getContext());
        freightService = new FreightService(getContext());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runFreight();
            }
        }, 500);

    }

    private void hideDialog() {
        loadingDialog.dismiss();
    }

    private void finalizeOk(String orderId) {
        MessageBus bus = new MessageBus();
        bus.className = CloseOrderFragment.class + "";
        bus.message = "pedidoFechado";
        bus.additionalInfo = orderId;
        EventBus.getDefault().post(bus);
    }

    private void initViews(View v) {
        textLblAddress = (TextView) v.findViewById(R.id.lbl_address);
        txtDateSelected = (TextView) v.findViewById(R.id.date_selected);
        radioManha = (RadioButton) v.findViewById(R.id.radio_manha);
        radioTarde = (RadioButton) v.findViewById(R.id.radio_tarde);
        radioNoite = (RadioButton) v.findViewById(R.id.radio_noite);
        spinnerAddress = (Spinner) v.findViewById(R.id.select_address);
        spinnerMethodPayment = (Spinner) v.findViewById(R.id.select_method_payment);
        editTextChangeMoney = (EditText) v.findViewById(R.id.change_money);
        editTextObservation = (EditText) v.findViewById(R.id.text_observation);
        checkBoxPickup = (CheckBox) v.findViewById(R.id.pickup);
        btnFinishOrder = (Button) v.findViewById(R.id.btn_finish_order);
        textPickup = (TextView) v.findViewById(R.id.text_pickup);
        textPeriodo = (TextView) v.findViewById(R.id.lbl_periodo_entrega);
        txtTxEntrega = (TextView) v.findViewById(R.id.lbl_taxa_entrega);
        radioGroup = (RadioGroup) v.findViewById(R.id.group);
        imgPickupDelivery = (ImageView) v.findViewById(R.id.image_pickup_delivery);
        btnDate = (Button) v.findViewById(R.id.btn_date);
    }

    private class TaskFreigh extends AsyncTask<Void, Void, Void> {
        private boolean error = false;
        private String messageError;
        private int mError = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ThreadFreightRunning = true;
            loadingDialogFreight.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingDialogFreight.hide();
            updateFreight();
            ThreadFreightRunning = false;
            if(error){
                Toast.makeText(getContext(), "Erro ao calcular o frete", Toast.LENGTH_SHORT).show();
                if(mError == -1){
                    MessageBus bus = new MessageBus();
                    bus.className = CloseOrderFragment.class + "";
                    bus.message = "tokenExpirado";
                    EventBus.getDefault().post(bus);
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mOrder.IdAddress != null) {
                try {

                    int quantity = getQuantityItemsInOrder(0);

                    List<Freight> _freights = freightService.getFreights(mOrder.IdAddress, quantity);
                    if (_freights != null) {
                        if (freights == null) {
                            freights = _freights;
                        } else {
                            boolean found = false;
                            for (int o = 0; o < freights.size(); o++) {
                                for (int i = 0; i < _freights.size(); i++) {
                                    if (freights.get(o).IdAddress == _freights.get(i).IdAddress) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (found) {
                                    break;
                                }
                            }
                            if (!found) {
                                freights.addAll(_freights);
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                    error = true;
                    messageError = e.getMessage();
                    if(e.getStatusCode() == 400){
                        mError = -1;
                    }
                }

            }
            return null;
        }

        private int getQuantityItemsInOrder(int defaultValue) {

            if(mOrder.Items != null){
                int quantity = 0;
                for (ItemOrder item:
                     mOrder.Items) {
                    if(!item.CustomMenu){
                        quantity += item.Quantity;
                    }
                }
                return quantity;
            }

            return defaultValue;
        }
    }

    private void updateFreight() {
        Freight freight = null;
        if(mOrder.IdAddress != null) {
            if (radioManha.isChecked()) {
                if (freights != null) {
                    for (Freight f :
                            freights) {
                        if (f.Period == 0 && f.IdAddress == mOrder.IdAddress) {
                            freight = f;
                            break;
                        }
                    }

                }
            } else if (radioTarde.isChecked()) {
                if (freights != null) {
                    for (Freight f :
                            freights) {
                        if (f.Period == 1 && f.IdAddress == mOrder.IdAddress) {
                            freight = f;
                            break;
                        }
                    }

                }
            } else {
                if (freights != null) {
                    for (Freight f :
                            freights) {
                        if (f.Period == 2 && f.IdAddress == mOrder.IdAddress) {
                            freight = f;
                            break;
                        }
                    }

                }
            }
        }
        if (freight != null) {
            totalFreight = freight.Price;
            txtTxEntrega.setText(String.format("Taxa entrega: R$ %.2f", freight.Price));
        }
    }

    private class Tasks extends AsyncTask<Void, Void, Void> {
        private Context _context;
        int orderId;
        boolean error = false;
        String message;

        public Tasks(Context context) {
            _context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideDialog();
            if (!error) {
                finalizeOk(String.valueOf(orderId));
            } else {
                Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                orderId = orderService.createOrder();
            } catch (Exception e) {
                e.printStackTrace();
                message = "Houve algum erro ao gerara seu pedido =(";
                error = true;
            }

            return null;
        }
    }

}

