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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DialogEditProfile extends AppCompatDialogFragment {
    EditText edTFullname, edTbob;
    Spinner spinnerGender;
    Calendar calendar;
    DBHelper myDB ;

    int id =0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_profile, null);
        myDB = new DBHelper(getActivity());

        builder.setView(view).setTitle("Cập nhật thông tin người dùng")
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*String fullname = edTFullname.getText().toString();
                Toast.makeText(getActivity(), fullname, Toast.LENGTH_SHORT).show();
                String bob = edTbob.getText().toString();
                Toast.makeText(getActivity(), fullname, Toast.LENGTH_SHORT).show();
                String gender = spinnerGender.getSelectedItem().toString();
                Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();*/
            }
        }).setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String fullname = edTFullname.getText().toString();
                String bob = edTbob.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();

                try{
                    Bundle extras = getActivity().getIntent().getExtras();
                    id = extras.getInt("my_key");
                }catch (Exception ex){
                    id = -1;
                }
                Boolean result = myDB.UpdateProfile(id, fullname, bob, gender);
                if(result == false) {
                    Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Đã cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    setTextProfile(id);
                }
            }
        });
        edTFullname = (EditText) view.findViewById(R.id.edit_fullname);
        edTbob =(EditText) view.findViewById(R.id.edit_bob);
        spinnerGender =(Spinner) view.findViewById(R.id.SpinerGender);
        initSpinner(spinnerGender);
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalender();
            }
            private void updateCalender(){
                String Format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.JAPAN);

                edTbob.setText(sdf.format(calendar.getTime()));
            }
        };
        edTbob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }
        Cursor profile = myDB.getProfilebyUID(id);
        if (profile != null && profile.moveToFirst()) {
            edTFullname.setText(profile.getString(0).toString());
            edTbob.setText(profile.getString(1).toString());
            spinnerGender.setSelection(0);
            if(profile.getString(2).toString().equals("Nữ")){
                spinnerGender.setSelection(1);
            }

        }
        return builder.create();
    }
    private void initSpinner(Spinner dropdown) {
        String[] items = new String[]{
                "Nam", "Nữ",
        };
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
    public void setTextProfile(int id){
        myDB = new DBHelper(getActivity());
        TextView txt_fullname, txt_bob, txt_gender;
        txt_fullname = (TextView)getActivity().findViewById(R.id.txtView_fullname);
        txt_bob = (TextView)getActivity().findViewById(R.id.txtView_birthday);
        txt_gender = (TextView)getActivity().findViewById(R.id.txtView_Gender);
        Cursor profile = myDB.getProfilebyUID(id);
        if (profile != null && profile.moveToFirst()) {
            txt_fullname.setText("Họ tên: " + profile.getString(0));
            txt_bob.setText("Ngày sinh: " + profile.getString(1));
            txt_gender.setText("Giới tính: " + profile.getString(2));
        }
    }

}
