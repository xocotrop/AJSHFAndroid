package br.com.irweb.ajshf.Fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.irweb.ajshf.API.Service.OrderService;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.AddressUserAJSHF;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.PaymentMethod;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOrderFragment extends Fragment {

    private RadioButton radioManha;
    private RadioButton radioTarde;
    private RadioGroup radioGroup;
    private Spinner spinnerAddress;
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

    private Order mOrder;
    private UserAuthAJSHF user;
    private AddressUserAJSHF addressUserAJSHF;

    private OrderService orderService;
    private AlertDialog loadingDialog;


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
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Estamos preparando a sua encomenda =). Aguarde mais um instante.");

        loadingDialog = builder.create();
    }

    private void setupButtonFinish() {

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                now.add(Calendar.DAY_OF_MONTH, 1);

                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));

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
                        .setPreselectedDate(now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH))
                        .setDateRange(minDate, null)
                        .setDoneText("Aplicar")
                        .setCancelText("Cancelar");

                cdp.show(getChildFragmentManager(), "picker");

                /*Calendar now = Calendar.getInstance();
                now.add(Calendar.DAY_OF_MONTH, 1);
                DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.show(getFragmentManager(), "dialog");*/

            }
        });

        btnFinishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Observation = editTextObservation.getText().toString();
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
            }
        });

        radioManha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 0;
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
                } else {
                    textPickup.setVisibility(View.INVISIBLE);
                    mOrder.IdAddress = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdAddress;
                    mOrder.IdNeighborhood = addressUserAJSHF.addresses.get(spinnerAddress.getSelectedItemPosition()).IdNeighborhood;
                    imgPickupDelivery.setImageResource(R.drawable.if_truck_1054949);
                }
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
        if (mOrder.IdAddress == null) {
            Toast.makeText(getContext(), "Deve ser selecionado um endereço", Toast.LENGTH_SHORT).show();
            error = true;
        }
        if (mOrder.PaymentMethod == 0) {
            if (mOrder.ChangeOfMoney <= 0) {
                Toast.makeText(getContext(), "Qual o valor para troco?", Toast.LENGTH_SHORT).show();
                error = true;
            }
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
    }

    private void hideDialog() {
        loadingDialog.dismiss();
    }

    private void finalizeOk() {
        MessageBus bus = new MessageBus();
        bus.className = CloseOrderFragment.class + "";
        bus.message = "pedidoFechado";
        EventBus.getDefault().post(bus);
    }

    private void initViews(View v) {
        txtDateSelected = (TextView) v.findViewById(R.id.date_selected);
        radioManha = (RadioButton) v.findViewById(R.id.radio_manha);
        radioTarde = (RadioButton) v.findViewById(R.id.radio_tarde);
        spinnerAddress = (Spinner) v.findViewById(R.id.select_address);
        spinnerMethodPayment = (Spinner) v.findViewById(R.id.select_method_payment);
        editTextChangeMoney = (EditText) v.findViewById(R.id.change_money);
        editTextObservation = (EditText) v.findViewById(R.id.text_observation);
        checkBoxPickup = (CheckBox) v.findViewById(R.id.pickup);
        btnFinishOrder = (Button) v.findViewById(R.id.btn_finish_order);
        textPickup = (TextView) v.findViewById(R.id.text_pickup);
        radioGroup = (RadioGroup) v.findViewById(R.id.group);
        imgPickupDelivery = (ImageView) v.findViewById(R.id.image_pickup_delivery);
        btnDate = (Button) v.findViewById(R.id.btn_date);
    }

    private class Tasks extends AsyncTask<Void, Void, Void> {
        private Context _context;
        int orderId;
        boolean error = false;

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
                finalizeOk();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                orderId = orderService.createOrder();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(_context, "Houve algum erro ao gerara seu pedido =(", Toast.LENGTH_SHORT).show();
                error = true;
            }

            return null;
        }
    }

}

