package hcmute.edu.vn.tlcn;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.adapter.TicketAdapter;
import hcmute.edu.vn.tlcn.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    EditText edtDateStart, edtDateBack;
    Spinner spinnerFrom, spinnerTo;
    RadioButton radioBtnOneWay, radioBtnRoundtrip;
    RadioGroup radioGroup;
    Button btn_find_ticker;
    Calendar calendarStart, calendarBack;


    /*RecyclerView rcvTickets;
    List<Ticket> mListTicket, mListView;*/
    String day, month, year, hour, minute;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*rcvTickets =(RecyclerView) view.findViewById(R.id.rcv_tickets);*/

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTickets.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvTickets.addItemDecoration(itemDecoration);*/

       /* */
        //Declare
        btn_find_ticker = (Button) view.findViewById(R.id.btn_Find);
        edtDateStart = (EditText) view.findViewById(R.id.edtView_DateStart);
        edtDateBack =(EditText) view.findViewById(R.id.edtView_DateBack);
        spinnerFrom =(Spinner) view.findViewById(R.id.SpinnerFrom);
        spinnerTo =(Spinner) view.findViewById(R.id.SpinnerTo);
        radioBtnOneWay = (RadioButton) view.findViewById(R.id.radioButton_oneway);
        radioBtnRoundtrip = (RadioButton) view.findViewById(R.id.radioButton_roundtrip);
        radioGroup = (RadioGroup) view.findViewById(R.id.linear_check);

        edtDateBack.setEnabled(false);
        initSpinner(spinnerFrom);
        initSpinner(spinnerTo);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radioBtnRoundtrip.isChecked()){
                    edtDateBack.setEnabled(true);
                }else{
                    edtDateBack.setEnabled(false);
                }
            }
        });

        //Set Calender
        calendarStart = Calendar.getInstance();
        calendarBack = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalender();
            }
            private void updateCalender(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.JAPAN);

                edtDateStart.setText(sdf.format(calendarStart.getTime()));
            }
        };
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarBack.set(Calendar.YEAR, year);
                calendarBack.set(Calendar.MONTH, month);
                calendarBack.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalender();
            }
            private void updateCalender(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.JAPAN);

                edtDateBack.setText(sdf.format(calendarBack.getTime()));
            }
        };
        edtDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        edtDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date1, calendarBack.get(Calendar.YEAR), calendarBack.get(Calendar.MONTH),
                        calendarBack.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_find_ticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if((spinnerFrom.getSelectedItem().toString() == (spinnerTo.getSelectedItem().toString()))
                            || (!edtDateStart.getText().toString().equals("") && compareTime(edtDateStart.getText().toString()) <= 0)){
                        Toast.makeText(getActivity(), "Vui lòng kiểm tra lại thông tin tìm ", Toast.LENGTH_SHORT).show();
                    }else{
                        if(radioBtnRoundtrip.isChecked()){

                        } else {
                            //open Oneway
                            String from = spinnerFrom.getSelectedItem().toString();
                            String to =  spinnerTo.getSelectedItem().toString();
                            String date =  edtDateStart.getText().toString();

                            int id = 0;
                            try{
                                Bundle extras = getActivity().getIntent().getExtras();
                                id = extras.getInt("my_key");
                            }catch (Exception ex){
                                id = -1;
                            }


                            Intent intent = new Intent(getActivity(), OnewayActivity.class);
                            intent.putExtra("from", from);
                            intent.putExtra("to", to);
                            intent.putExtra("date", date);
                            intent.putExtra("cusid", id);
                            startActivity(intent);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        /*mListTicket = new ArrayList<>();
        mListView = new ArrayList<>();*/
        LocalDateTime now = LocalDateTime.now();
        year = String.valueOf(now.getYear());
        month = String.valueOf(now.getMonthValue());
        day = String.valueOf(now.getDayOfMonth());
        hour = String.valueOf(now.getHour());
        minute = String.valueOf(now.getMinute());

        //callAPIGetTickets();
        return view;
    }

    private void initSpinner(Spinner dropdown) {
        String[] items = new String[]{"Hà Nội", "TP.HCM",};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    /*public void openDialogSearch(){
        DialogSearchTicket dialogSearchTicket = new DialogSearchTicket();
        dialogSearchTicket.show(getChildFragmentManager(), "");
    }*/

   /* private void callAPIGetTickets(){
        ApiService.apiService.getListTicket().enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                mListTicket = response.body();
                for(Ticket d: mListTicket){
                    try {
                        if(d.getDate()!= null && d.getTime() !=null && d.getDate().contains(day+"/"+month+"/"+year)
                                && compareTime(d.getTime()) > 7200000){
                            mListView.add(d);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    *//*if(d.getTo() !=null && d.getTo().contains("TP.HCM")){
                        mListView.add(d);
                    }*//*
                }
                TicketAdapter ticketAdapter = new TicketAdapter(mListView);
                rcvTickets.setAdapter(ticketAdapter);
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
    private long compareTime(String time) throws ParseException {
        String startTime = day+"/"+month+"/"+year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = sdf.parse(startTime);
        Date d2 = sdf.parse(time);
        long elapsed = d2.getTime() - d1.getTime();

        return elapsed;
    }

}
