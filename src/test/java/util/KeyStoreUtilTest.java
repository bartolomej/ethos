package util;

import core.Account;
import org.json.JSONObject;
import org.junit.Test;

public class KeyStoreUtilTest {

    @Test
    public void persistAccount() {
        JSONObject jsonObject = new Account().toJson();
        KeyStoreUtil.persistAccount(jsonObject);
    }
}