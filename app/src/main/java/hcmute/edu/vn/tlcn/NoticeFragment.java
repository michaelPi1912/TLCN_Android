package hcmute.edu.vn.tlcn;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.tlcn.Models.Notice;
import hcmute.edu.vn.tlcn.Models.NoticeDB;
import hcmute.edu.vn.tlcn.adapter.NoticeAdapter;

public class NoticeFragment extends Fragment {

    RecyclerView rcvNotice;
    DBHelper mydb;
    NoticeAdapter noticeAdapter;
    List<Notice> mListNotice;
    SQLiteDatabase mdb;
    Button btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notice, container, false);

        mydb = new DBHelper(getActivity());

        mdb = mydb.getWritableDatabase();
        mListNotice = new ArrayList<>();

        rcvNotice =(RecyclerView) view.findViewById(R.id.rcv_notices);
        btnRefresh =(Button) view.findViewById(R.id.btn_refreshnotice);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvNotice.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcvNotice.addItemDecoration(itemDecoration);

        noticeAdapter = new NoticeAdapter(getActivity(), getAllNotice());
        rcvNotice.setAdapter(noticeAdapter);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        return view;
    }

    private void addData(){
        String date = "", time ="";
        int id =0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            date = extras.getString("date", "");
            time = extras.getString("time", "");
            id = extras.getInt("my_key");
        }catch (Exception ex){
            Toast.makeText(getActivity(), "Loi he thong", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        Boolean rsN = mydb.insertNotice("Bạn đã đặt vé thành công", date, time, id);
        if(rsN == true){
            //Toast.makeText(getActivity(), "Đặt vé thành công", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Hệ thống bị lỗi thông báo", Toast.LENGTH_SHORT).show();
        }
        noticeAdapter.swapCursor(getAllNotice());
    }
    public int getUID(){
        int id =0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }
        return id;
    }
    public Cursor getAllNotice(){
        int id =0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }
        return mdb.query(NoticeDB.NoticeEntry.TABLE_NAME, null, NoticeDB.NoticeEntry.COLUMN_UID+"="+String.valueOf(id),
                null,
                null,
                null,
                NoticeDB.NoticeEntry.COLUMN_DAY +","+ NoticeDB.NoticeEntry.COLUMN_TIME + " DESC");/*mydb.getNoticebyUID(id)*/
    }
}
