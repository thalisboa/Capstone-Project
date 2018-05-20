package thaislisboa.com.virtualwallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import thaislisboa.com.virtualwallet.model.Transaction;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private Context context;

    public ListAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {

        Transaction transaction = transactions.get(position);

        holder.mName.setText(transaction.getName());

        if(transaction.isDeposit()){
            holder.mType.setText("Deposit");

        } else{

            holder.mType.setText("Expense");

        }

        holder.mValue.setText(String.valueOf(transaction.getValue()));
        //holder.mImageView.set(transaction.getName());

        Format formatter = new SimpleDateFormat("yyyy-mm-dd");
        String s = formatter.format(transaction.getDate());

        holder.mDate.setText(s);


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mName;
        TextView mType;
        TextView mValue;
        TextView mDate;


        public ViewHolder(View itemView) {

            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_recycler);
            mName = itemView.findViewById(R.id.tv_recyclerview_name);
            mType = itemView.findViewById(R.id.tv_recyclerview_type);
            mValue = itemView.findViewById(R.id.tv_recyclerview_value);
            mDate = itemView.findViewById(R.id.tv_recyclerview_date);


        }
    }
}
