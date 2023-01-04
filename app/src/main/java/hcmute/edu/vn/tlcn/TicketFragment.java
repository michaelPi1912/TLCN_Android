package hcmute.edu.vn.tlcn;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.adapter.BookAdapter;

public class TicketFragment extends Fragment {

    private BookAdapter bookAdapter;
    private ListView lvBook;

    DBHelper mydb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        lvBook = view.findViewById(R.id.lv_book);

        mydb = new DBHelper(getActivity());

        int id = 0;
        try{
            Bundle extras = getActivity().getIntent().getExtras();
            id = extras.getInt("my_key");
        }catch (Exception ex){
            id = -1;
        }

        //Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_SHORT).show();



        List<Book> mListBoook = new ArrayList<>();
        Cursor cursor = mydb.getBookbyUid(id);

        if(cursor !=null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                mListBoook.add(new Book(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5)));
            }
        }

        bookAdapter = new BookAdapter(getActivity(), mListBoook);
        lvBook.setAdapter(bookAdapter);
        lvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = new Book(mListBoook.get(position).getStartpoint(), mListBoook.get(position).getEndpoint(),
                        mListBoook.get(position).getDate(), mListBoook.get(position).getTime(), mListBoook.get(position).getType(),
                        mListBoook.get(position).getHangbay());

                Intent intent = new Intent(getActivity(), DetailTicketActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}