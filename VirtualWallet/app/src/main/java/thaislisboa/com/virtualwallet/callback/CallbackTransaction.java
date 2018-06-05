package thaislisboa.com.virtualwallet.callback;

import java.util.List;

import thaislisboa.com.virtualwallet.model.Transaction;

public interface CallbackTransaction {

    void onReturn(List<Transaction> transactionList);
}
