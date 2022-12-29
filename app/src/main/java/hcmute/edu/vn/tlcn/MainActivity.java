package hcmute.edu.vn.tlcn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_register, btn_login;
    EditText edt_login_username, edt_login_password;

    DBHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Declare();
        myDB = new DBHelper(this);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edt_login_username.getText().toString();
                String password = edt_login_password.getText().toString();

                if(username.equals("")||password.equals("")){
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ mật khẩu và tài ", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkuser = myDB.checkUserNamePassword(username,password);
                    if(checkuser == true){
                        int uid = myDB.getUIDbyName(username);
                        Bundle bundle = new Bundle();
                        bundle.putInt("my_key", uid);
                        AccountFragment myFrag = new AccountFragment();
                        myFrag.setArguments(bundle);
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(bundle);
                        NoticeFragment noticeFragment= new NoticeFragment();
                        noticeFragment.setArguments(bundle);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void Declare(){
        btn_login = (Button) findViewById(R.id.btnLogin);
        btn_register = (Button) findViewById(R.id.btnOpenRegister);
        edt_login_username = (EditText) findViewById(R.id.edtLoginUSerName);
        edt_login_password = (EditText) findViewById(R.id.edtLoginPassword);
    }
}