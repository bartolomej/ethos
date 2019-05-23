package db;

import config.Constants;

public class DbFacade {

    public static void init() {
        initDbFolders();
    }

    private static void initDbFolders() {
        FileSystemStore.makeDirectory(Constants.DB_DIR);
        FileSystemStore.makeDirectory(Constants.ACCOUNT_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.BLOCK_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.INDEX_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.PEERS_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.TX_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.IO_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.INPUTS_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.OUTPUTS_STORE_DIR);
        FileSystemStore.makeDirectory(Constants.UTXO_STORE_DIR);
    }

}
