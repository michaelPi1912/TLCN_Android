package hcmute.edu.vn.tlcn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Ticket;

public class DetailTicketActivity extends AppCompatActivity {

    TextView tvFrom, tvTo, tvAirline, tvDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        tvFrom = findViewById(R.id.tv_detailfrom);
        tvTo = findViewById(R.id.tv_detailto);
        tvAirline = findViewById(R.id.tv_detailhangbay);
        tvDay = findViewById(R.id.tv_detailtime);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Book book = (Book) bundle.get("book");

        tvFrom.setText("Bay từ: " +book.getStartpoint());
        tvTo.setText("Đến: "+ book.getEndpoint());
        tvAirline.setText(book.getHangbay());
        tvDay.setText("Thời gian: "+ book.getDate() + " "+ book.getTime());
    }
}