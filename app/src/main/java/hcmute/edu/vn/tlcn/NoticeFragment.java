package hcmute.edu.vn.tlcn;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Notice;
import hcmute.edu.vn.tlcn.Models.NoticeDB;
import hcmute.edu.vn.tlcn.adapter.NoticeAdapter;

public class NoticeFragment extends Fragment {

    ListView listViewNotice;
    DBHelper mydb;
    NoticeAdapter noticeAdapter;
    SQLiteDatabase mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_notice, container, false);

        mydb = new DBHelper(getActivity());
        mdb = mydb.getWritableDatabase();

        listViewNotice = view.findViewById(R.id.lv_notices);
        List<Notice> mListNotice = new ArrayList<>();

        int id =0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }

        Cursor cursor = mydb.getNoticebyUID(id);

        if(cursor !=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                mListNotice.add(new Notice(cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }
        }

        noticeAdapter = new NoticeAdapter(getActivity(), mListNotice);
        listViewNotice.setAdapter(noticeAdapter);

        return view;
    }

    /*private void addData(){
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
    }*/
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
        Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        return mdb.query(NoticeDB.NoticeEntry.TABLE_NAME, null, NoticeDB.NoticeEntry.COLUMN_UID+"="+String.valueOf(id),
                null,
                null,
                null,
                NoticeDB.NoticeEntry.COLUMN_DAY +","+ NoticeDB.NoticeEntry.COLUMN_TIME + " DESC");/*mydb.getNoticebyUID(id)*/
    }
}
