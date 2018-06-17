package thaislisboa.com.virtualwallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.model.Transaction;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public ArrayList<Transaction> selected_usersList = new ArrayList<>();
    public List<Transaction> transactions;
    private Context context;

    public ListAdapter(List<Transaction> transactions, Context context, ArrayList<Transaction> selectedList) {
        this.transactions = transactions;
        this.context = context;
        this.selected_usersList = selectedList;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);

        holder.mName.setText(transaction.getName());

        if (transaction.isDeposit()) {
            holder.mType.setText(context.getString(R.string.deposit));
        } else {
            holder.mType.setText(context.getString(R.string.expense));
        }

        Locale current = context.getResources().getConfiguration().locale;
        NumberFormat format = NumberFormat.getCurrencyInstance(current);
        String currency = format.format(transaction.getValue());
        holder.mValue.setText(currency);

/*        Format formatter = new SimpleDateFormat("yyyy-mm-dd");
        String s = formatter.format(transaction.getDateTransaction());*/

        holder.mDate.setText(transaction.getDateTransaction());
        holder.mCategory.setText(transaction.getCategory());

        if (transaction.isDeposit()) {
            holder.mTimelineView.setMarker(context.getDrawable(R.drawable.circle_green));
        } else {
            holder.mTimelineView.setMarker(context.getDrawable(R.drawable.circle_red));
        }

        if (selected_usersList.contains(transactions.get(position))) {
            holder.mCardViewTransaction.setCardBackgroundColor(context.getResources().getColor(R.color.primary_light));
        } else {
            holder.mCardViewTransaction.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }


    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardViewTransaction;
        TextView mName;
        TextView mType;
        TextView mValue;
        TextView mDate;
        TextView mCategory;
        TimelineView mTimelineView;


        public ViewHolder(View itemView, int viewType) {

            super(itemView);

            mCardViewTransaction = itemView.findViewById(R.id.card_transaction);
            mName = itemView.findViewById(R.id.tv_recyclerview_name);
            mType = itemView.findViewById(R.id.tv_recyclerview_type);
            mValue = itemView.findViewById(R.id.tv_recyclerview_value);
            mDate = itemView.findViewById(R.id.tv_recyclerview_date);
            mCategory = itemView.findViewById(R.id.tv_recyclerview_category);
            mTimelineView = itemView.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);


        }
    }
}
