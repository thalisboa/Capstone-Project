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

import java.util.ArrayList;
import java.util.List;

import thaislisboa.com.virtualwallet.R;
import thaislisboa.com.virtualwallet.callback.CallbackCategory;
import thaislisboa.com.virtualwallet.model.Category;
import thaislisboa.com.virtualwallet.model.Transaction;

public class FirebaseDB {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private static String USERS = "users";
    private static String TRANSACTION = "transaction";
    private static String CATEGORIES = "categories";

    final static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final static DatabaseReference usersRef = database.child(USERS);

    public static void save(Transaction transaction, final Context context) {

        transaction.setUid(usersRef.child(TRANSACTION).push().getKey());

        usersRef.child(Authenticator.getUser()).child(TRANSACTION).child(transaction.getUid()).setValue(transaction)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(context, "added", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

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


}


