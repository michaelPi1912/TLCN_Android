package hcmute.edu.vn.tlcn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.time.LocalDateTime;

import hcmute.edu.vn.tlcn.Models.CreateOrder;
import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.adapter.NoticeAdapter;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BookOneWayActivity extends AppCompatActivity {

    EditText edtName, edtCMND, edtPhone, edtType1, edtType2, edtType3;
    Button btnBook, btnBHome, btnZaloPay;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_one_way);

        mydb = new DBHelper(this);

        edtName =(EditText) findViewById(R.id.edtbookname);
        edtCMND =(EditText) findViewById(R.id.edtbookname);
        edtPhone = (EditText) findViewById(R.id.bookphone);

        edtType1 = (EditText) findViewById(R.id.edtvenguoilon);
        edtType2 = (EditText) findViewById(R.id.vetreem);
        edtType3 = (EditText) findViewById(R.id.edtveembe);

        btnBook = (Button) findViewById(R.id.btnbookticket);
        btnBHome = (Button) findViewById(R.id.btnbackhome);
        btnZaloPay = (AppCompatButton) findViewById(R.id.btnzalopay);

        //zalo
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnBHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }

        Ticket ticket = (Ticket) bundle.get("ticket");
        int uid = bundle.getInt("personID");

        /*Toast.makeText(this, ticket.getFrom(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(uid), Toast.LENGTH_SHORT).show();*/

        btnBook.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(edtName.getText().toString().equals("") || edtCMND.getText().toString().equals("")||
                edtPhone.getText().toString().equals("") || Integer.valueOf(edtType1.getText().toString()) >= 20
                        || Integer.valueOf(edtType2.getText().toString()) >= 20 || Integer.valueOf(edtType3.getText().toString()) >= 20){
                    Toast.makeText(BookOneWayActivity.this, "Kiểm tra lại thông tin hoặc số lượng vé được mua vượt quá gới hạn (20 vé / mỗi loại hành khách)", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean result = mydb.insertInfor(uid, edtName.getText().toString(), edtCMND.getText().toString(), edtPhone.getText().toString(),
                            ticket.getId());

                    if(result == true){
                        int Iid = mydb.getIidbyName(ticket.getId());

                        Boolean rs = mydb.insertBooking(Iid, ticket.getFrom(), ticket.getTo(), ticket.getDate(), ticket.getTime()
                                , ticket.getType(), edtType1.getText().toString(), edtType2.getText().toString(), edtType3.getText().toString(), ticket.getAirline());

                        if(rs == true){
                            LocalDateTime now = LocalDateTime.now();
                            String year = String.valueOf(now.getYear());
                            String month = String.valueOf(now.getMonthValue());
                            String day = String.valueOf(now.getDayOfMonth());
                            String hour = String.valueOf(now.getHour());
                            String minute = String.valueOf(now.getMinute());
                            String date = day+"/"+month+"/"+year;
                            String time = hour+":"+minute;
                            Boolean rsN = mydb.insertNotice("Bạn đã đặt vé thành công", day+"/"+month+"/"+year, hour+":"+minute, uid);
                            if(rsN == true){
                                Toast.makeText(BookOneWayActivity.this, "Đặt vé thành công", Toast.LENGTH_SHORT).show();
                                NoticeFragment noticeFragment = new NoticeFragment();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(BookOneWayActivity.this, "Hệ thống bị lỗi thông báo", Toast.LENGTH_SHORT).show();
                            }
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("date", date);
                            bundle1.putString("time", time);
                            bundle1.putInt("my_key", uid);
                            NoticeFragment noticeFragment = new NoticeFragment();
                            noticeFragment.setArguments(bundle1);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.putExtras(bundle1);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(BookOneWayActivity.this, "Hệ thống lỗi, đặt vé thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //zalo pay
        btnZaloPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(edtName.getText().toString().equals("") || edtCMND.getText().toString().equals("")||
                        edtPhone.getText().toString().equals("") || Integer.valueOf(edtType1.getText().toString()) >= 20
                        || Integer.valueOf(edtType2.getText().toString()) >= 20 || Integer.valueOf(edtType3.getText().toString()) >= 20){
                    Toast.makeText(BookOneWayActivity.this, "Kiểm tra lại thông tin hoặc số lượng vé được mua vượt quá gới hạn (20 vé / mỗi loại hành khách)", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean result = mydb.insertInfor(uid, edtName.getText().toString(), edtCMND.getText().toString(), edtPhone.getText().toString(),
                            ticket.getId());

                    if (result == true) {
                        int Iid = mydb.getIidbyName(ticket.getId());

                        Boolean rs = mydb.insertBooking(Iid, ticket.getFrom(), ticket.getTo(), ticket.getDate(), ticket.getTime()
                                , ticket.getType(), edtType1.getText().toString(), edtType2.getText().toString(), edtType3.getText().toString(), ticket.getAirline());

                        if (rs == true) {
                            //
                            LocalDateTime now = LocalDateTime.now();
                            String year = String.valueOf(now.getYear());
                            String month = String.valueOf(now.getMonthValue());
                            String day = String.valueOf(now.getDayOfMonth());
                            String hour = String.valueOf(now.getHour());
                            String minute = String.valueOf(now.getMinute());
                            String date = day+"/"+month+"/"+year;
                            String time = hour+":"+minute;
                            Boolean rsN = mydb.insertNotice("Bạn đã đặt vé thành công", day+"/"+month+"/"+year, hour+":"+minute, uid);
                            if(rsN == true){
                                Toast.makeText(BookOneWayActivity.this, "1", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(BookOneWayActivity.this, "Hệ thống bị lỗi thông báo", Toast.LENGTH_SHORT).show();
                            }
                            CreateOrder orderApi = new CreateOrder();
                            try {
                                JSONObject data = orderApi.createOrder("100000");
                                String code = data.getString("return_code");

                                if (code.equals("1")) {
                                    String token = data.getString("zp_trans_token");
                                    ZaloPaySDK.getInstance().payOrder(BookOneWayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                        @Override
                                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new AlertDialog.Builder(BookOneWayActivity.this)
                                                            .setTitle("Payment Success")
                                                            .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Toast.makeText(BookOneWayActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(BookOneWayActivity.this, HomeActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            })
                                                            .setNegativeButton("Cancel", null).show();
                                                }

                                            });
                                        }

                                        @Override
                                        public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                            new AlertDialog.Builder(BookOneWayActivity.this)
                                                    .setTitle("User Cancel Payment")
                                                    .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", null).show();
                                        }

                                        @Override
                                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                            new AlertDialog.Builder(BookOneWayActivity.this)
                                                    .setTitle("Payment Fail")
                                                    .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", null).show();
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(BookOneWayActivity.this, "Hệ thống lỗi, đặt vé thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}