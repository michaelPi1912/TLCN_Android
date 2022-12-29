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

public class DialogEditContact extends AppCompatDialogFragment {
    EditText edTEmail, edTPhoneNumber;
    DBHelper myDB ;
    int id = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_contact, null);
        myDB = new DBHelper(getActivity());

        builder.setView(view).setTitle("Cập nhật thông tin liên hệ")
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

                String email = edTEmail.getText().toString();
                String phonenumber = edTPhoneNumber.getText().toString();

                try{
                    Bundle extras = getActivity().getIntent().getExtras();
                    id = extras.getInt("my_key");
                }catch (Exception ex){
                    id = -1;
                }

                Boolean result = myDB.UpdateContact(id, email, phonenumber);
                if(result == false) {
                    Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Đã cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    setTextContact(id);
                }
            }
        });
        edTEmail = (EditText) view.findViewById(R.id.edit_contact_email);
        edTPhoneNumber =(EditText) view.findViewById(R.id.edit_edit_phone_number);

        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }
        Cursor profile = myDB.getContactbyUID(id);
        if (profile != null && profile.moveToFirst()) {
            edTEmail.setText(profile.getString(0));
            edTPhoneNumber.setText(profile.getString(1));

        }
        return builder.create();
    }
    public void setTextContact(int id){
        myDB = new DBHelper(getActivity());
        TextView txt_email, txt_phonenum;
        txt_email = (TextView)getActivity().findViewById(R.id.txtView_Email);
        txt_phonenum = (TextView)getActivity().findViewById(R.id.txtView_Phone);
        Cursor profile = myDB.getContactbyUID(id);
        if (profile != null && profile.moveToFirst()) {
            txt_email.setText("Email: " + profile.getString(0));
            txt_phonenum.setText("Số điện thoại: " + profile.getString(1));

        }
    }

}
