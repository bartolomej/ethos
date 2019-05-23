package config;

public class Constants {

    // folder structure constants
    public static String ROOT_DIR = System.getProperty("user.dir") + "/";
    public static String DB_DIR = ROOT_DIR + "database/";
    public static String LOG_DIR = ROOT_DIR + "logs/";
    public static String BLOCK_STORE_DIR = DB_DIR + "blocks/";
    public static String TX_STORE_DIR = DB_DIR + "tx/";
    public static String IO_STORE_DIR = DB_DIR + "io/";
    public static String INPUTS_STORE_DIR = IO_STORE_DIR + "inputs/";
    public static String OUTPUTS_STORE_DIR = IO_STORE_DIR + "outputs/";
    public static String UTXO_STORE_DIR = IO_STORE_DIR + "utxo/";
    public static String INDEX_STORE_DIR = DB_DIR + "index/";
    public static String ACCOUNT_STORE_DIR = DB_DIR + "accounts/";
    public static String PEERS_STORE_DIR = DB_DIR + "peers/";

    // coin value constants
    public static long ETO = 100000;
    public static long ZETO = 1000;
    public static long METO = 1;

    public static int START_DIFFICULTY = 2;
    public static int MIN_TX_FEE = 10;

    // initial dynamic constants (tx fee,..)
    // ...
}
