package hcmute.edu.vn.tlcn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_username, edt_password, edt_repassword, edt_email, edt_phone;
    Button btn_register;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Declare();
        myDB = new DBHelper(this);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                String repassword = edt_repassword.getText().toString();
                String email = edt_email.getText().toString();
                String phone = edt_phone.getText().toString();

                if(username.equals("") ||password.equals("") || repassword.equals("") ||email.equals("") || phone.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Xin hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(repassword)){
                        Boolean check = myDB.checkUserName(username);
                        if (check == false){
                            Boolean regResult = myDB.insertUser(username,password,email,phone);
                            if(regResult == true){
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                int uid = myDB.getUIDbyName(username);
                                Date currentTime = new Date();

                                Boolean profileResuilt = myDB.insertProfile(uid, "Nguyen Van A","1/1/2001", "Nam");
                                if (profileResuilt == true){
                                    Toast.makeText(RegisterActivity.this, "Đã thêm thông tin tài khoản", Toast.LENGTH_SHORT).show();
                                    edt_username.setText("");
                                    edt_password.setText("");
                                    edt_repassword.setText("");
                                    edt_email.setText("");
                                    edt_phone.setText("");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(RegisterActivity.this, "Thêm thông tin tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Tài Khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không tương thích", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void Declare(){

        btn_register = (Button) findViewById(R.id.btnRegister);
        edt_username = (EditText) findViewById(R.id.edtUsername);
        edt_password = (EditText) findViewById(R.id.edtPassword);
        edt_repassword = (EditText) findViewById(R.id.edtRepassword);
        edt_email = (EditText) findViewById(R.id.edtEmail);
        edt_phone = (EditText) findViewById(R.id.edtPhone);
    }
}