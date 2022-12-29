package hcmute.edu.vn.tlcn.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.R;
import hcmute.edu.vn.tlcn.my_interface.IClickItemListener;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder>{

    private final List<Ticket> mListTicket;
    private IClickItemListener iClickItemListener;


    public TicketAdapter(List<Ticket> mListTicket, IClickItemListener listener) {
        this.mListTicket = mListTicket;
        this.iClickItemListener = listener;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        final Ticket ticket = mListTicket.get(position);
        if(ticket == null){
            return;
        }
        holder.tvAirline.setText("Hãng bay: " + ticket.getAirline());
        holder.tvFrom.setText("Từ: " +ticket.getFrom());
        holder.tvTo.setText("Đến: " + ticket.getTo());
        holder.tvPrice.setText("Giá vé: " + String.valueOf(ticket.getPrice()));
        holder.tvTime.setText("Thời gian: " + ticket.getDate() +" -"+ ticket.getTime());
        holder.tvType.setText("Hạng ghế : " + ticket.getType());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemListener.onClickItemTicket(ticket);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mListTicket!=null){
            return mListTicket.size();
        }
        return 0;
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAirline, tvFrom, tvTo, tvTime, tvPrice, tvType;

        private final LinearLayout layoutItem;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item);
            tvAirline = itemView.findViewById(R.id.tv_airline);
            tvFrom = itemView.findViewById(R.id.tv_from);
            tvTo = itemView.findViewById(R.id.tv_to);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvType= itemView.findViewById(R.id.tv_type);
        }
    }
}
