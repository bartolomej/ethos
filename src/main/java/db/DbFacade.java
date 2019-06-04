package db;

import config.Constants;

import java.io.File;

public class DbFacade {

    public static void init() {
        initDbFolders();
    }

    public static void initDbFolders() {
        FileSystem.makeDirectory(Constants.DB_DIR);
        FileSystem.makeDirectory(Constants.ACCOUNT_STORE_DIR);
        FileSystem.makeDirectory(Constants.BLOCK_STORE_DIR);
        FileSystem.makeDirectory(Constants.INDEX_STORE_DIR);
        FileSystem.makeDirectory(Constants.PEERS_STORE_DIR);
        FileSystem.makeDirectory(Constants.TX_STORE_DIR);
        FileSystem.makeDirectory(Constants.IO_STORE_DIR);
        FileSystem.makeDirectory(Constants.INPUTS_STORE_DIR);
        FileSystem.makeDirectory(Constants.OUTPUTS_STORE_DIR);
        FileSystem.makeDirectory(Constants.UTXO_STORE_DIR);
    }

    public static void purgeStore() {
        FileSystem.removeAll(Constants.ACCOUNT_STORE_DIR);
        FileSystem.removeAll(Constants.BLOCK_STORE_DIR);
        FileSystem.removeAll(Constants.INDEX_STORE_DIR);
        FileSystem.removeAll(Constants.PEERS_STORE_DIR);
        FileSystem.removeAll(Constants.TX_STORE_DIR);
        FileSystem.removeAll(Constants.IO_STORE_DIR);
        FileSystem.removeAll(Constants.INPUTS_STORE_DIR);
        FileSystem.removeAll(Constants.OUTPUTS_STORE_DIR);
        FileSystem.removeAll(Constants.UTXO_STORE_DIR);
    }

}
