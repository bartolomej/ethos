package core;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void toStringTest() {
        Account account = new Account();
        assertNotNull(account.toString());
    }

    @Test
    public void toJSONKeyStore() {
        Account account = new Account();
        JSONObject jsonObject = account.toJson();
        assertNotNull(jsonObject);
    }
}