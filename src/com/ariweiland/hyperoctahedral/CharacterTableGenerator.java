package com.ariweiland.hyperoctahedral;

import com.ariweiland.hyperoctahedral.partition.AbstractPartition;
import com.ariweiland.hyperoctahedral.partition.IntegerPartition;
import com.ariweiland.hyperoctahedral.partition.SignedIntegerPartition;
import com.ariweiland.hyperoctahedral.young.AbstractYoungDiagram;
import com.ariweiland.hyperoctahedral.young.SignedYoungDiagram;
import com.ariweiland.hyperoctahedral.young.YoungDiagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class CharacterTableGenerator {

    private final Map<AbstractYoungDiagram, Map<Integer, Integer>> record = new HashMap<>();

    private boolean checkRecord(AbstractYoungDiagram yd, int p) {
        if (!record.containsKey(yd)) {
            record.put(yd, new HashMap<Integer, Integer>());
        }
        return record.get(yd).containsKey(p);
    }

    private int getRecord(AbstractYoungDiagram syd, int p) {
        return record.get(syd).get(p);
    }

    private void putRecord(AbstractYoungDiagram yd, int p, int chi) {
        record.get(yd).put(p, chi);
    }

    public int[][] generateTable(String type) {
        type = type.toLowerCase();
        if (!Pattern.matches("[hs]\\d+", type)) {
            throw new IllegalArgumentException(
                    "Illegal table argument. Must be 'H' or 'S' followed by a positive integer.\n" +
                    "For example, for the symmetric group with n=10, specify S10.\n" +
                    "For the hyperoctahedral group with n=3, specify H3.");
        }
        int n = Integer.parseInt(type.substring(1, type.length()));
        if (type.charAt(0) == 's') {
            return generateSnTable(n);
        } else {
            return generateHnTable(n);
        }
    }

    public int[][] generateHnTable(int n) {
        List<SignedIntegerPartition> sips = SignedIntegerPartition.all(n);
        List<SignedYoungDiagram> syds = SignedYoungDiagram.all(n);
        int length = sips.size();
        int[][] table = new int[length][length];
        for (int i=0; i<length; i++) { // row = syd
            for (int j=0; j<length; j++) { // col = sip
                table[i][j] = calculateChi(syds.get(i), sips.get(j), 0);
            }
        }
        return table;
    }

    public int[][] generateSnTable(int n) {
        List<IntegerPartition> sips = IntegerPartition.all(n);
        List<YoungDiagram> syds = YoungDiagram.all(n);
        int length = sips.size();
        int[][] table = new int[length][length];
        for (int i=0; i<length; i++) { // row = syd
            for (int j=0; j<length; j++) { // col = sip
                table[i][j] = calculateChi(syds.get(i), sips.get(j), 0);
            }
        }
        return table;
    }

    public int calculateChi(AbstractYoungDiagram yd, AbstractPartition partition, int index) {
        if (yd.isEmpty()) {
            return 1;
        }
        int p = partition.getPartition()[index];
        if (checkRecord(yd, p)) {
            return getRecord(yd, p);
        }
        Map<AbstractYoungDiagram, Integer> map = yd.reduce(Math.abs(p));
        int sum = 0;
        for (AbstractYoungDiagram next : map.keySet()) {
            sum += signFactor(map.get(next), p < 0) * calculateChi(next, partition, index + 1);
        }
        return sum;
    }

    public int signFactor(int height, boolean isNegative) {
        return (isNegative && height < 0) == (Math.abs(height) % 2 == 0) ? 1 : -1;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Specify the type and size of the table as an argument!\n" +
                    "For example, for the symmetric group with n=10, specify S10.\n" +
                    "For the hyperoctahedral group with n=3, specify H3.");
        }

        CharacterTableGenerator gen = new CharacterTableGenerator();
        int[][] table = gen.generateTable(args[0]);
        System.out.print("{");
        for (int i=0; i<table.length; i++) {
            System.out.print("{");
            for (int j=0; j<table.length; j++) {
                System.out.print(table[i][j]);
                if (j < table.length - 1) {
                    System.out.print(",");
                }
            }
            if (i < table.length - 1) {
                System.out.println("},");
            }
        }
        System.out.println("}}");
    }
}
