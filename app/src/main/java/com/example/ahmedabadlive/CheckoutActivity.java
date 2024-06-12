package com.example.ahmedabadlive;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import kotlinx.coroutines.flow.SharedFlowSlot;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText name,phone,eamil,address,pincode;
    Button pay_btn;
    RadioGroup payvia;
    Spinner city;
    String scity = "", spayvia = "";
    String email_syntax = "[A-Z0-9a-z._-]+@[a-z]+\\.[a-z]+";

    String[] cityname = {"Select the city","Rajkot","Ahmedabad","Surat"};
    SQLiteDatabase db;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        name = findViewById(R.id.checkout_name);
        phone = findViewById(R.id.checkout_phone);
        eamil = findViewById(R.id.checkout_email);
        address = findViewById(R.id.checkout_address);
        pincode = findViewById(R.id.checkout_pincode);
        payvia = findViewById(R.id.checkout_payvia);
        city = findViewById(R.id.checkout_city);
        pay_btn = findViewById(R.id.checkout_paybtn);

        sp = getSharedPreferences(contentsp.PREF,MODE_PRIVATE);

        name.setText(sp.getString(contentsp.NAME,""));
        phone.setText(sp.getString(contentsp.PHONE,""));
        eamil.setText(sp.getString(contentsp.EMAIL,""));

        db = openOrCreateDatabase("ahmedabadlive_user.db",MODE_PRIVATE,null);
        String tablequery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME VARCHAR(100),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tablequery);

        String categorytablequery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categorytablequery);

        String subcategorytablequery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategorytablequery);

        String producttablequery = "CREATE TABLE IF NOT EXISTS PRODUCTS(PRODUCTSID INTEGER PRIMARY KEY AUTOINCREMENT,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),DESCRIPTION TEXT,PRICE VARCHAR(20))";
        db.execSQL(producttablequery);

        String wishlistTablequery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10), PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTablequery);

        String cartTablequery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT, USERID VARCHAR(10),ORDERID VARCHAR(10), PRODUCTID VARCHAR(10),QTY VARCHAR(10))";
        db.execSQL(cartTablequery);

        String checkoutTablequery = "CREATE TABLE IF NOT EXISTS CHECKOUT(ORDERID INTEGER PRIMARY KEY AUTOINCREMENT ,USERID VARCHAR(10),NAME VARCHAR(100),PHONE BIGINT(10),EMAIL VARCHAR(100), ADDRESS TEXT, PINCODE VARCHAR(6), CITY VARCHAR(20), PAYVIA VARCHAR(20) ,TRANSACTIONID VARCHAR(50),TOTALAMOUNT VARCHAR(20))";
        db.execSQL(checkoutTablequery);

        payvia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                spayvia = radioButton.getText().toString();
                new CommonMethod(CheckoutActivity.this,radioButton.getText().toString());
            }
        });


        ArrayAdapter adapter = new ArrayAdapter(CheckoutActivity.this,android.R.layout.simple_list_item_checked,cityname);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    scity = "";
                }
                else {

                    scity = cityname[position];
                    new CommonMethod(CheckoutActivity.this,cityname[position]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scity = sp.getString(contentsp.CITY,"");
        int city_position = 0;
        for (int i=0; i<cityname.length;i++){
            if (cityname[i].equalsIgnoreCase(scity)){
                city_position = i;
                break;
            }
        }
        city.setSelection(city_position);

        pay_btn.setText("Pay Total : " + contentsp.PRICE_SYMBOL + sp.getString(contentsp.TOTAL_CART_PRICE,""));

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Enter Name");
                }else if (phone.getText().toString().trim().equals("")) {
                    phone.setError("Enter Phone No");
                }else if (eamil.getText().toString().trim().equals("")) {
                    eamil.setError("Enter Email");
                }
                else if (!eamil.getText().toString().trim().matches(email_syntax)) {
                    eamil.setError("Enter Email in proper way");
                }
                else if (address.getText().toString().trim().equals("")) {
                    address.setError("Enter address");
                }
                else if (pincode.getText().toString().trim().equals("")) {
                    pincode.setError("Enter ConfirmPassword");
                }
                else if (pincode.getText().toString().trim().length()<6) {
                    pincode.setError("Pincode have minmun 6 numbers");
                }
                else if (payvia.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(v,"Select payment method");
                }
                else if (scity == "") {
                    new CommonMethod(v,"Select the city");
                }
                else {
                    if (spayvia.equalsIgnoreCase("COD")) {
                        String insertQuery = "INSERT INTO CHECKOUT VALUES(NULL,'" + sp.getString(contentsp.USERID, "") + "','" + name.getText().toString() + "','" + phone.getText().toString() + "','" + eamil.getText().toString() + "','" + address.getText().toString() + "','" + pincode.getText().toString() + "','" + scity + "', '" + spayvia + "','','" + sp.getString(contentsp.TOTAL_CART_PRICE, "") + "')";
                        db.execSQL(insertQuery);

                        String selectQuery = "SELECT MAX(ORDERID) FROM CHECKOUT LIMIT 1";
                        Cursor cursor = db.rawQuery(selectQuery, null);

                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                String updateQuery = "UPDATE CART SET ORDERID = '" + cursor.getString(0) + "' WHERE USERID = '" + sp.getString(contentsp.USERID, "") + "' AND ORDERID = '0'";
                                db.execSQL(updateQuery);
                            }
                        }

                        new CommonMethod(CheckoutActivity.this, "Order placed successfully");
                        new CommonMethod(v, "Order placed successfully");
                        new CommonMethod(CheckoutActivity.this, DeshboardActivity.class);
                        finish();
                    }

                    else {
                        checkout();
                    }
                }
            }
        });

    }

    private void checkout() {

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_wZIxniRrvMw4C2");

            checkout.setImage(R.drawable.ic_launcher_foreground);

            /**
             * Reference to current activity
             */
            final Activity activity = this;

            /**
             * Pass your payment options to the Razorpay Checkout as a JSONObject
             */
            try {
                JSONObject options = new JSONObject();

                options.put("name", getResources().getString(R.string.app_name));
                options.put("description", "Reference No. #123456");
                options.put("send_sms_hash", true);
                options.put("allow_rotation", true);
               // options.put("image", "http://example.com/image/rzp.jpg");
                //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
              //  options.put("theme.color", "#3399cc");
                options.put("currency", "INR");
                options.put("amount", String.valueOf(Integer.parseInt(sp.getString(contentsp.TOTAL_CART_PRICE,""))*100));//pass amount in currency subunits
                options.put("prefill.email", sp.getString(contentsp.EMAIL,""));
                options.put("prefill.contact",sp.getString(contentsp.PHONE,""));
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open(activity, options);

            } catch(Exception e) {
                Log.e("Exeption_for_starting", "Error in starting Razorpay Checkout", e);
            }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Log.d("RESPONSE_PAYMENT_SUCCESS",s);
        String insertQuery = "INSERT INTO CHECKOUT VALUES(NULL,'" + sp.getString(contentsp.USERID, "") + "','" + name.getText().toString() + "','" + phone.getText().toString() + "','" + eamil.getText().toString() + "','" + address.getText().toString() + "','" + pincode.getText().toString() + "','" + scity + "', '" + spayvia + "','"+s+"','" + sp.getString(contentsp.TOTAL_CART_PRICE, "") + "')";
        db.execSQL(insertQuery);

        String selectQuery = "SELECT MAX(ORDERID) FROM CHECKOUT LIMIT 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String updateQuery = "UPDATE CART SET ORDERID = '" + cursor.getString(0) + "' WHERE USERID = '" + sp.getString(contentsp.USERID, "") + "' AND ORDERID = '0'";
                db.execSQL(updateQuery);
            }
        }

        new CommonMethod(CheckoutActivity.this, "Payment successfully");
        new CommonMethod(CheckoutActivity.this, DeshboardActivity.class);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.d("RESPONSE_PAYMENT_FAILED",s);
    }
}