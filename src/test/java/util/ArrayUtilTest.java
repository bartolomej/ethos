package util;

import core.transaction.TxInput;
import core.transaction.TxOutput;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void toStringWithSuffix() {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        outputs.add(new TxOutput(new byte[]{0,0,0,0}, 100));
        outputs.add(new TxOutput(new byte[]{0,0,0,0}, 200));

        // TODO: toString() doesn't work with interface
        // String stringOutput = ArrayUtil.toStringWithSuffix(outputs, "");
    }
}