package hcmute.edu.vn.tlcn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Ticket;

public class DetailTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Book book = (Book) bundle.get("book");
    }
}