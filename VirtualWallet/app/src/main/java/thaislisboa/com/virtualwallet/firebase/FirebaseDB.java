package thaislisboa.com.virtualwallet.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.activities.HomeWalletActivity;
import thaislisboa.com.virtualwallet.callback.CallbackCategory;
import thaislisboa.com.virtualwallet.callback.CallbackMonths;
import thaislisboa.com.virtualwallet.callback.CallbackTransaction;
import thaislisboa.com.virtualwallet.model.Category;
import thaislisboa.com.virtualwallet.model.Transaction;
import thaislisboa.com.virtualwallet.util.DateUtils;

public class FirebaseDB {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }

    private static String USERS = "users";
    private static String TRANSACTION = "transaction";
    private static String CATEGORIES = "categories";
    private static String DATE = "dateTransaction";


    final static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final static DatabaseReference usersRef = database.child(USERS);

    public static Category saveCategory(final Category category, final Context context) {

        String uid = usersRef.child(CATEGORIES).push().getKey();

        category.setUid(uid);

        usersRef.child(Authenticator.getUser()).child(CATEGORIES).child(category.getUid()).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, context.getString(R.string.added) + " " + category.getName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            }
        });

        return category;
    }

    public static List<Category> loadCategories(final CallbackCategory callbackCategory) {

        Log.d("thais-log", "loadCategories: ----------------");

        final List<Category> categories = new ArrayList<>();

        usersRef.child(Authenticator.getUser()).child(CATEGORIES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotCategory : dataSnapshot.getChildren()) {
                    Category category = dataSnapshotCategory.getValue(Category.class);
                    categories.add(category);
                    Log.d("thais-log", "loadCategories: " + category.getName());
                }

                callbackCategory.onReturn(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return categories;
    }

    public static void deleteCategory(Category category, Context context) {

        usersRef.child(Authenticator.getUser()).child(CATEGORIES).child(category.getUid()).removeValue();
        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();

    }


    public static Transaction saveTransaction(final Transaction transaction, final Context context) {

        String uid = usersRef.child(TRANSACTION).push().getKey();
        transaction.setUid(uid);
        usersRef.child(Authenticator.getUser()).child(TRANSACTION).child(transaction.getUid()).setValue(transaction)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.added) + " " + transaction.getName(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return transaction;
    }

    public static List<String> getMonths(final CallbackMonths callbackMonths) {

        DatabaseReference transactionsRef = usersRef.child(Authenticator.getUser()).child(TRANSACTION);
        transactionsRef.keepSynced(true);

        final List<String> months = new ArrayList<>();

        transactionsRef.orderByChild(DATE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotCategory : dataSnapshot.getChildren()) {
                    Transaction transaction = dataSnapshotCategory.getValue(Transaction.class);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = sdf.parse(transaction.getDateTransaction());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(parse);

                    //c.get(Calendar.MONTH) + c.get(Calendar.DATE) + c.get(Calendar.YEAR)

                    String monthYear = DateUtils.getMonth(c.get(Calendar.MONTH)) + "/" + c.get(Calendar.YEAR);

                    if (!months.contains(monthYear)) {
                        months.add(monthYear);
                        Log.d("thais-log", "loadTMonths: " + monthYear);
                    }
                }

                Collections.reverse(months);
                callbackMonths.onReturnMonths(months);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return months;
    }

    public static List<Transaction> loadTransactions(final HomeWalletActivity callbackTransaction, int month, int year, int maxResults) {

        DatabaseReference transactionsRef = usersRef.child(Authenticator.getUser()).child(TRANSACTION);
        transactionsRef.keepSynced(true);

        Log.d("thais-log", "loadTransactions: ----------------" + year + "-" + month + "-01" + "  " + year + "-" + month + "-31");

        final List<Transaction> transactions = new ArrayList<>();


        transactionsRef.orderByChild(DATE).startAt(year + "-" + month + "-01").endAt(year + "-" + month + "-31").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshotCategory : dataSnapshot.getChildren()) {
                    Transaction transaction = dataSnapshotCategory.getValue(Transaction.class);
                    transactions.add(transaction);
                    Log.d("thais-log", "loadTransactions: " + transaction.getName());
                }

                Collections.reverse(transactions);
                callbackTransaction.onReturn(transactions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return transactions;
    }

    public static void deleteTransaction(Transaction transaction, Context context) {

        usersRef.child(Authenticator.getUser()).child(TRANSACTION).child(transaction.getUid()).removeValue();
        Toast.makeText(context, "Deleted " + transaction.getName(), Toast.LENGTH_SHORT).show();

    }

}


