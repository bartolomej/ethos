package util;

import com.google.gson.JsonObject;
import core.HelperGenerator;
import core.block.Block;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObjectParserTest {

    @Test
    public void parseInvalidJsonInput() {
        String invalidBlock = "{\n" +
                "  \"difficulty\":3,\n" +
                "  \"prev_block_hash\":\"00000000000000\",\n" +
                "  \"tx_root\":\"02020202020202\",\n" +
                "  \"index\":23,\n" +
                "  \"hash\":\"01010101010101\",\n" +
                "  \"miner\":\"030303030303\",\n" +
                "}";
        invalidBlock = invalidBlock.replace("\n", "");
        JSONObject jsonObject = new JSONObject(invalidBlock);

        try {
            Block tx = ObjectParser.parseJsonBlock(jsonObject);
        } catch (JSONException e) {
            assertNotNull(e);
        }

    }

    @Test
    public void parseJsonInput() {
        TxInput input = generateTestInput();
        TxInput parsedInput = ObjectParser.parseJsonInput(input.toJson());

        assertTrue(input.equals(parsedInput));
    }

    @Test
    public void parseJsonOutput() {
        TxOutput output = generateTestOutput();
        TxOutput parsedOutput = ObjectParser.parseJsonOutput(output.toJson());

        assertTrue(output.equals(parsedOutput));
    }

    @Test
    public void parseJsonInputArray() {
        ArrayList<TxInput> inputs = generateTestInputArray(2);
        ArrayList<TxInput> parsedInputs = ObjectParser.parseJsonInputArray(TxInput.arrayToJson(inputs));

        for (int i = 0; i < parsedInputs.size(); i++) {
            assertTrue(parsedInputs.get(i).equals(inputs.get(i)));
        }
    }

    @Test
    public void parseJsonOutputArray() {
        ArrayList<TxOutput> outputs = generateTestOutputArray(2);
        ArrayList<TxOutput> parsedOutputs = ObjectParser.parseJsonOutputArray(TxOutput.arrayToJson(outputs));

        for (int i = 0; i < parsedOutputs.size(); i++) {
            assertTrue(parsedOutputs.get(i).equals(outputs.get(i)));
        }
    }

    @Test
    public void parseJsonTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx = HelperGenerator.generateTestValidTransaction();
        Transaction parsedTx = ObjectParser.parseJsonTransaction(tx.toJson());

        assertTrue(tx.equals(parsedTx));
    }

    private ArrayList<TxInput> generateTestInputArray(int length) {
        ArrayList<TxInput> arrayList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            arrayList.add(generateTestInput());
        }
        return arrayList;
    }

    private ArrayList<TxOutput> generateTestOutputArray(int length) {
        ArrayList<TxOutput> arrayList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            arrayList.add(generateTestOutput());
        }
        return arrayList;
    }

    private TxInput generateTestInput() {
        byte[] signature = new byte[]{0,0,0,0};
        byte[] txHash = new byte[]{1,1,1,1};
        return new TxInput(signature, txHash, generateTestOutput());
    }

    private TxOutput generateTestOutput() {
         byte[] recipientPubKey = new byte[]{0,0,0,0};
        return new TxOutput(recipientPubKey, 100, 0);
    }

}