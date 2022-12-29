package hcmute.edu.vn.tlcn;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.adapter.TicketAdapter;
import hcmute.edu.vn.tlcn.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogSearchTicket extends AppCompatDialogFragment {
    EditText edtDateStart, edtDateBack;
    Spinner spinnerFrom, spinnerTo;
    RadioButton radioBtnOneWay, radioBtnRoundtrip;
    RadioGroup radioGroup;
    RecyclerView rcvTickets;
    Calendar calendarStart, calendarBack;
    DBHelper myDB ;

    List<Ticket> mListTicket, mListView;

    //int id =0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_find_ticket, null);
        myDB = new DBHelper(getActivity());

        builder.setView(view).setTitle("Cập nhật thông tin người dùng")
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("Tìm Kiếm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //Declare
        edtDateStart = (EditText) view.findViewById(R.id.edtView_DateStart);
        edtDateBack =(EditText) view.findViewById(R.id.edtView_DateBack);
        spinnerFrom =(Spinner) view.findViewById(R.id.SpinnerFrom);
        spinnerTo =(Spinner) view.findViewById(R.id.SpinnerTo);
        radioBtnOneWay = (RadioButton) view.findViewById(R.id.radioButton_oneway);
        radioBtnRoundtrip = (RadioButton) view.findViewById(R.id.radioButton_roundtrip);
        radioGroup = (RadioGroup) view.findViewById(R.id.linear_check);
        //rcvTickets =(RecyclerView)view.findViewById(R.id.rcv_tickets);

        edtDateBack.setEnabled(false);
        initSpinner(spinnerFrom);
        initSpinner(spinnerTo);

        mListTicket = new ArrayList<>();
        mListView = new ArrayList<>();

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


        return builder.create();
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

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    /*String start = spinnerFrom.getSelectedItem().toString();
                    String to = spinnerTo.getSelectedItem().toString();*/

                    if(spinnerFrom.getSelectedItem().toString() == spinnerTo.getSelectedItem().toString()){
                        Toast.makeText(getActivity(), "Điểm khởi hành trùng với điểm đến", Toast.LENGTH_SHORT).show();

                    }else{
                        String date_start = edtDateStart.getText().toString();
                        if(radioBtnRoundtrip.isChecked()){

                        }
                        callAPIGetTickets();
                        wantToCloseDialog = true;
                    }
                    if(wantToCloseDialog)
                        d.dismiss();
                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }
    private void callAPIGetTickets(){
        ApiService.apiService.getListTicket().enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                mListTicket = response.body();
                String start = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();
                for(Ticket d: mListTicket){
                        if(d.getDate()!= null && d.getDate().contains(edtDateStart.getText().toString())
                        && d.getFrom() !=null && d.getFrom().contains(start)
                                && d.getTo() !=null && d.getTo().contains(to)){
                            mListView.add(d);
                        }
                    }
                //TicketAdapter ticketAdapter = new TicketAdapter(mListView);
                //rcvTickets.setAdapter(ticketAdapter);
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
