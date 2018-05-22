package thaislisboa.com.virtualwallet.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import thaislisboa.com.virtualwallet.model.Transaction;

public class FirebaseDB {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private static String USERS = "users";
    private static String TRANSACTION = "transaction";

    final static DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final static DatabaseReference usersRef = database.child(USERS);




    public static void save(Transaction transaction, final Context context) {

        transaction.setUid(usersRef.child(TRANSACTION).push().getKey());

        usersRef.child(Authenticator.getUser()).child(TRANSACTION).child(transaction.getUid()).setValue(transaction)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(context, "added",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "error",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}


