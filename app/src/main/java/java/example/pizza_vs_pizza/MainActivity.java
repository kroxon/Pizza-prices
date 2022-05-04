package java.example.pizza_vs_pizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.example.pizza_vs_pizza.MainActivity;
import java.util.Currency;

import pizza_vs_pizza.R;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private LinearLayout linearLayout, lLayoutPizza3, lpizzainfo1, lpizzainfo2, lpizzainfo3;
    private EditText etPrice1, etPrice2, etPrice3, etDiameter1, etDiameter2, etDiameter3;
    private Button bnextpizza, bcalculate;
    private TextView tarea1, tarea2, tarea3, tunitprice1, tunitprice2, tunitprice3, unitp11, unitp12,
            unitp13, unitp21, unitp22, unitp23, unitp31, unitp32, unitp33, title;
    private ImageView imgbg;
    private AutoCompleteTextView actvCurrencies, actvUnits;
    private String sCurrency, sUnit;
    ArrayAdapter<String> adapter1, adapter2;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        linearLayout = findViewById(R.id.linearLayout);
        lLayoutPizza3 = findViewById(R.id.layoutPizza3);
        lpizzainfo1 = findViewById(R.id.layoutpinfo1);
        lpizzainfo2 = findViewById(R.id.layoutpinfo2);
        lpizzainfo3 = findViewById(R.id.layoutpinfo3);
        etPrice1 = findViewById(R.id.etPrice1);
        etPrice2 = findViewById(R.id.etPrice2);
        etPrice3 = findViewById(R.id.etPrice3);
        etDiameter1 = findViewById(R.id.etDiameter1);
        etDiameter2 = findViewById(R.id.etDiameter2);
        etDiameter3 = findViewById(R.id.etDiameter3);
        bnextpizza = findViewById(R.id.bnextPizza);
        bcalculate = findViewById(R.id.bCalculate);
        tarea1 = findViewById(R.id.area1);
        tarea2 = findViewById(R.id.area2);
        tarea3 = findViewById(R.id.area3);
        tunitprice1 = findViewById(R.id.unitprice1);
        tunitprice2 = findViewById(R.id.unitprice2);
        tunitprice3 = findViewById(R.id.unitprice3);
        unitp11 = findViewById(R.id.unitp11);
        unitp12 = findViewById(R.id.unitp12);
        unitp13 = findViewById(R.id.unitp13);
        unitp21 = findViewById(R.id.unitp21);
        unitp22 = findViewById(R.id.unitp22);
        unitp23 = findViewById(R.id.unitp23);
        unitp31 = findViewById(R.id.unitp31);
        unitp32 = findViewById(R.id.unitp32);
        unitp33 = findViewById(R.id.unitp33);
        title = findViewById(R.id.title);
//        imgbg = findViewById(R.id.imgBackground);
        actvCurrencies = findViewById(R.id.currenciesActv);
        actvUnits = findViewById(R.id.unitActv);

        bnextpizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lLayoutPizza3.getVisibility() == View.GONE) {
                    lLayoutPizza3.setVisibility(View.VISIBLE);
                    bnextpizza.setText("- pizza");
                } else {
                    lLayoutPizza3.setVisibility(View.GONE);
                    bnextpizza.setText("+ pizza");
                }
            }
        });

        bcalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEntries()) {
                    final Animation animFade = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade);
                    calc();
                    lpizzainfo1.setVisibility(View.VISIBLE);
                    lpizzainfo2.setVisibility(View.VISIBLE);
                    lpizzainfo3.setVisibility(View.VISIBLE);
                    lpizzainfo1.startAnimation(animFade);
                    lpizzainfo2.startAnimation(animFade);
                    lpizzainfo3.startAnimation(animFade);
                } else {
                    lpizzainfo1.setVisibility(View.GONE);
                    lpizzainfo2.setVisibility(View.GONE);
                    lpizzainfo3.setVisibility(View.GONE);
                }
            }
        });

        actvUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fillUnits();
                insertData();
                updateData();
            }
        });

        actvCurrencies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fillUnits();
                insertData();
                updateData();
            }
        });

        readData();

        etListeners();
//        fillUnits();


        String currencies[] = getResources().getStringArray(R.array.currencies);
        adapter1 = new ArrayAdapter<String>(
                this, R.layout.dropdown_currency, currencies);

        actvCurrencies.setAdapter(adapter1);

        String units[] = getResources().getStringArray(R.array.units);
        adapter2 = new ArrayAdapter<String>(
                this, R.layout.dropdown_currency, units);
        actvUnits.setAdapter(adapter2);


    }

    public boolean checkEntries() {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        boolean flag = true;
        boolean flagZero = true;

        if (etDiameter1.getText().toString().equals("") || etDiameter1.getText().toString().equals("0")) {
            etDiameter1.setBackgroundResource(R.drawable.et_bg_error);
            etDiameter1.startAnimation(animShake);
            etDiameter1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            flag = false;
            if (etDiameter1.getText().toString().equals("0"))
                flagZero = false;
        }
        if (etPrice1.getText().toString().equals("")) {
            etPrice1.setBackgroundResource(R.drawable.et_bg_error);
            etPrice1.startAnimation(animShake);
            etPrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            flag = false;
        }
        if (etDiameter2.getText().toString().equals("") || etDiameter2.getText().toString().equals("0")) {
            etDiameter2.setBackgroundResource(R.drawable.et_bg_error);
            etDiameter2.startAnimation(animShake);
            etDiameter2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            flag = false;
            if (etDiameter2.getText().toString().equals("0"))
                flagZero = false;
        }
        if (etPrice2.getText().toString().equals("")) {
            etPrice2.setBackgroundResource(R.drawable.et_bg_error);
            etPrice2.startAnimation(animShake);
            etPrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
            flag = false;
        }
        if (lLayoutPizza3.getVisibility() == View.VISIBLE) {
            if (etDiameter3.getText().toString().equals("") || etDiameter3.getText().toString().equals("0")) {
                etDiameter3.setBackgroundResource(R.drawable.et_bg_error);
                etDiameter3.startAnimation(animShake);
                etDiameter3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                flag = false;
            }
            if (etPrice3.getText().toString().equals("")) {
                etPrice3.setBackgroundResource(R.drawable.et_bg_error);
                etPrice3.startAnimation(animShake);
                etPrice3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                flag = false;
                if (etDiameter1.getText().toString().equals("0"))
                    flagZero = false;
            }
        }
        if (flag == false) {
            if (flagZero == false) {
                Toast.makeText(MainActivity.this, R.string.zero, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    public void calc() {
        float diameter1, diameter2, diameter3, price1, price2, price3;
        int area1, area2, area3;
        diameter1 = Float.parseFloat(etDiameter1.getText().toString());
        diameter2 = Float.parseFloat(etDiameter2.getText().toString());
        price1 = Float.parseFloat(etPrice1.getText().toString());
        price2 = Float.parseFloat(etPrice2.getText().toString());
        area1 = (int) (0.5 + diameter1 * diameter1 / 4 * 3.14159265);
        area2 = (int) (0.5 + diameter2 * diameter2 / 4 * 3.14159265);
        tarea1.setText("" + area1);
        tarea2.setText("" + area2);
        tunitprice1.setText(String.format("%.3f", price1 / area1));
        tunitprice2.setText(String.format("%.3f", price2 / area2));

        if (lLayoutPizza3.getVisibility() == View.VISIBLE) {
            diameter3 = Float.parseFloat(etDiameter3.getText().toString());
            price3 = Float.parseFloat(etPrice3.getText().toString());
            area3 = (int) (0.5 + diameter3 * diameter3 / 4 * 3.14159265);
            tarea3.setText("" + area3);
            tunitprice3.setText(String.format("%.3f", price3 / area3));
        }
    }

    public void etListeners() {
        etDiameter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etDiameter1.getText().toString().equals("")) {
                    etDiameter1.setBackgroundResource(R.drawable.et_bg);
                    etDiameter1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }
        });
        etDiameter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etDiameter2.getText().toString().equals("")) {
                    etDiameter2.setBackgroundResource(R.drawable.et_bg);
                    etDiameter2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        etDiameter3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etDiameter3.getText().toString().equals("")) {
                    etDiameter3.setBackgroundResource(R.drawable.et_bg);
                    etDiameter3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        etPrice1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etPrice1.getText().toString().equals("")) {
                    etPrice1.setBackgroundResource(R.drawable.et_bg);
                    etPrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        etPrice2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etPrice2.getText().toString().equals("")) {
                    etPrice2.setBackgroundResource(R.drawable.et_bg);
                    etPrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
        etPrice3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etPrice3.getText().toString().equals("")) {
                    etPrice3.setBackgroundResource(R.drawable.et_bg);
                    etPrice3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

            }
        });
    }

    public void fillUnits() {
        String unit = actvUnits.getText().toString();
        String cur = actvCurrencies.getText().toString();
        if (cur.equals("EUR")) {
            cur = "\u20ac";
        }
        if (cur.equals("USD")) {
            cur = "\u0024";
        }
        if (cur.equals("GBP")) {
            cur = "\u00a3";
        }
        if (cur.equals("JPY")) {
            cur = "\u00a5";

        }
        if (cur.equals("PLN")) {
            cur = "zł";

        }
        if (cur.equals("UAH")) {
            cur = "\u20b4";
        }
//        if (cur.equals("local")) {
//            Locale defaultLocale = Locale.getDefault();
//            Currency currency = Currency.getInstance(defaultLocale);
//            String currencyCode = currency.getCurrencyCode();
//            cur = currencyCode;
//        }

//        Currency currency = Currency.getInstance(cur);
//        String symbol = currency.getSymbol();
        unitp11.setText(unit + "\u00B2");
        unitp12.setText(cur);
        unitp13.setText(unit + "\u00B2");
        unitp21.setText(unit + "\u00B2");
        unitp22.setText(cur);
        unitp23.setText(unit + "\u00B2");
        unitp31.setText(unit + "\u00B2");
        unitp32.setText(cur);
        unitp33.setText(unit + "\u00B2");
    }

    public void insertData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        databaseHelper.insertData(actvCurrencies.getText().toString().trim(), actvUnits.getText().toString().trim());
    }

    public void updateData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        databaseHelper.updateData("1", actvCurrencies.getText().toString().trim(), actvUnits.getText().toString().trim());
    }

    public void readData() {
        Cursor cursor = databaseHelper.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String a = cursor.getString(0);
                actvCurrencies.setText(cursor.getString(1));
                actvUnits.setText(cursor.getString(2));
                actvCurrencies.clearFocus();
                actvCurrencies.setAdapter(adapter1);
                actvUnits.setAdapter(adapter2);
                fillUnits();
            }
        }
    }
}