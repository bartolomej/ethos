package core;

import org.junit.Test;

import java.util.ArrayList;

public class InterfaceTest {

    public static void main(String[] args) {
        System.out.println(sumValuesWithArray());
        System.out.println(sumValuesWithArrayList());
    }

    public static int sumValuesWithArrayList() {
        ArrayList<Value> values = new ArrayList<>();
        values.add(new Tx1(1));
        values.add(new Tx2(2));

        int sum = 0;
        for (Value value : values) {
            sum += value.getValue();
        }
        return sum;
    }

    public static int sumValuesWithArray() {
        Tx1 tx1 = new Tx1(1);
        Tx2 tx2 = new Tx2(2);
        Value[] values = new Value[]{tx1, tx2};

        int sum = 0;
        for (Value value : values) {
            sum += value.getValue();
        }
        return sum;
    }

}

class Tx1 implements Value {

    private int value;

    public Tx1(int value) {
        this.value = value;
    }

    public void printValue() {
        System.out.println(this.value);
    }

    @Override
    public int getValue() {
        return this.value;
    }
}

class Tx2 implements Value {

    private int value;

    public Tx2(int value) {
        this.value = value;
    }

    public void printValue() {
        System.out.println(this.value);
    }

    @Override
    public int getValue() {
        return this.value;
    }
}

interface Value {
    int getValue();
}