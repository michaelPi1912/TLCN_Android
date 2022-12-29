package hcmute.edu.vn.tlcn;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AccountFragment extends Fragment {
    Button btn_logout, btn_edit_profile, btn_edit_contact;
    TextView txt_birthday, txt_phone, txt_fullname, txt_gender, txt_email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        DBHelper mydb = new DBHelper(getActivity());
        Declare(view, mydb);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        btn_edit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogContact();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    private void Declare(View view, DBHelper mydb){
        btn_logout = (Button) view.findViewById(R.id.btnLogout);
        btn_edit_profile = (Button) view.findViewById(R.id.button_edit_profile);
        btn_edit_contact = (Button) view.findViewById(R.id.button_edit_contact);
        txt_birthday = (TextView) view.findViewById(R.id.txtView_birthday);
        txt_phone = (TextView) view.findViewById(R.id.txtView_Phone);
        txt_fullname = (TextView) view.findViewById(R.id.txtView_fullname);
        txt_gender = (TextView) view.findViewById(R.id.txtView_Gender);
        txt_email = (TextView) view.findViewById(R.id.txtView_Email);


        int id = 0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }
        FillProfile(mydb, id);
        FillContact(mydb, id);
    }
    public void FillProfile(DBHelper mydb, Integer id ){
        Cursor profile = mydb.getProfilebyUID(id);
        if (profile != null && profile.moveToFirst()) {
            txt_fullname.setText("Họ tên: " + profile.getString(0));
            txt_birthday.setText("Ngày sinh: " + profile.getString(1));
            txt_gender.setText("Giới tính: " + profile.getString(2));
        }
    }
    public void FillContact(DBHelper mydb, Integer id ){
        Cursor contact = mydb.getContactbyUID(id);
        if (contact != null && contact.moveToFirst()) {
            txt_email.setText("email: " + contact.getString(0));
            txt_phone.setText("Số điện thoại: "+ String.valueOf(contact.getInt(1)));
        }
    }
    public void openDialog(){
        DialogEditProfile dialogEditProfile = new DialogEditProfile();
        dialogEditProfile.show(getChildFragmentManager(), "");
    }
    public void openDialogContact(){
        DialogEditContact dialogEditContact = new DialogEditContact();
        dialogEditContact.show(getChildFragmentManager(), "");
    }
}
