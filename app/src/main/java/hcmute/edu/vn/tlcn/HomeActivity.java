package hcmute.edu.vn.tlcn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        //UID
        /*txtID = (TextView) findViewById(R.id.txtViewID);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int value = extras.getInt("key");
            txtID.setText("UID: "+ value);
        }*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFagment = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFagment = new HomeFragment();
                    break;
                case R.id.nav_ticket:
                    selectedFagment = new TicketFragment();
                    break;
                case R.id.nav_notice:
                    selectedFagment = new NoticeFragment();
                    break;
                case R.id.nav_account:
                    selectedFagment = new AccountFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFagment).commit();
            return true;
        }
    };
}