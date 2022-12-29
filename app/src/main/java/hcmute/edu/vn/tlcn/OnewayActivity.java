package hcmute.edu.vn.tlcn;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.adapter.TicketAdapter;
import hcmute.edu.vn.tlcn.api.ApiService;
import hcmute.edu.vn.tlcn.my_interface.IClickItemListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnewayActivity extends AppCompatActivity {

    Button btnHome;
    TicketAdapter ticketAdapter;
    RecyclerView rcvTickets;
    List<Ticket> mListTicket, mListView;
    String day, month, year, hour, minute, from, to, date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneway);

        btnHome = (Button) findViewById(R.id.btnhome);
        rcvTickets =(RecyclerView) findViewById(R.id.rcv_tickets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTickets.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvTickets.addItemDecoration(itemDecoration);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        mListTicket = new ArrayList<>();
        mListView = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        year = String.valueOf(now.getYear());
        month = String.valueOf(now.getMonthValue());
        day = String.valueOf(now.getDayOfMonth());
        hour = String.valueOf(now.getHour());
        minute = String.valueOf(now.getMinute());

        callAPIGetTickets();
    }

    private void callAPIGetTickets(){
        ApiService.apiService.getListTicket().enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {

                Intent intent = getIntent();
                from =  intent.getStringExtra("from");
                to =  intent.getStringExtra("to");
                date =  intent.getStringExtra("date");

                final int uid  = intent.getIntExtra("cusid",0);

                //Toast.makeText(OnewayActivity.this, String.valueOf(uid), Toast.LENGTH_SHORT).show();

                mListTicket = response.body();

                if(date !=null && date.contains(day+"/"+month+"/"+year)){
                    for(Ticket d: mListTicket){
                        try {
                            if(d.getDate()!= null && d.getTime() !=null && d.getDate().contains(day+"/"+month+"/"+year)
                                    && compareTime(d.getTime()) > 7200000){
                                mListView.add(d);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    for(Ticket d: mListTicket){
                        try {
                            if(d.getDate() != null && d.getFrom() != null && d.getTo() !=null
                            && d.getFrom().contains(from) && d.getTo().contains(to) && d.getDate().contains(date) && compareDate(d.getDate()) >= 0){
                                mListView.add(d);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ticketAdapter = new TicketAdapter(mListView, new IClickItemListener() {
                    @Override
                    public void onClickItemTicket(Ticket ticket) {
                        onClickGoToBook(ticket, uid);
                    }
                });
                rcvTickets.setAdapter(ticketAdapter);
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(OnewayActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private long compareTime(String time) throws ParseException {
        String startTime = hour+":"+minute;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d1 = sdf.parse(startTime);
        Date d2 = sdf.parse(time);
        final long elapsed = d2.getTime() - d1.getTime();

        return elapsed;
    }

    private long compareDate(String time) throws ParseException {
        String startTime = day+"/"+month+"/"+year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = sdf.parse(startTime);
        Date d2 = sdf.parse(time);
        long elapsed = d2.getTime() - d1.getTime();

        return elapsed;
    }
    private void onClickGoToBook(Ticket ticket, int uid){
        Intent intent = new Intent(this, BookOneWayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticket", ticket);
        bundle.putInt("personID", uid);
        TicketFragment ticketFragment = new TicketFragment();
        ticketFragment.setArguments(bundle);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        if(ticketAdapter !=null){
            ticketAdapter.release();
        }
    }*/
}
