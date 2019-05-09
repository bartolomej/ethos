package db;

import core.Account;
import core.Block;
import core.transaction.Transaction;

import java.util.ArrayList;

public interface Repository {

    Account createAccount(String address);

    Account getAccount(String address);

    Account update(Account account);

    Transaction add(Transaction transaction);

    ArrayList<Transaction> getTransactions(String address);

    ArrayList<Block> getCommitedChain();

    void commit();
}
