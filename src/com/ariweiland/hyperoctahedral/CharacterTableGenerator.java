package com.ariweiland.hyperoctahedral;

import com.ariweiland.hyperoctahedral.partition.SignedIntegerPartition;
import com.ariweiland.hyperoctahedral.young.SignedYoungDiagram;

import java.util.*;

/**
 * @author Ari Weiland
 */
public class CharacterTableGenerator {

    private final Map<SignedYoungDiagram, Map<Integer, Integer>> record = new HashMap<>();

    private boolean checkRecord(SignedYoungDiagram syd, int p) {
        if (!record.containsKey(syd)) {
            record.put(syd, new HashMap<Integer, Integer>());
        }
        return record.get(syd).containsKey(p);
    }

    private int getRecord(SignedYoungDiagram syd, int p) {
        return record.get(syd).get(p);
    }

    private void putRecord(SignedYoungDiagram syd, int p, int chi) {
        record.get(syd).put(p, chi);
    }

    public int[][] generateHnTable(int n) {
        List<SignedIntegerPartition> sips = SignedIntegerPartition.signedIntegerPartitions(n);
        Collections.sort(sips);
        List<SignedYoungDiagram> syds = new ArrayList<>();
        for (SignedIntegerPartition p : sips) {
            syds.add(new SignedYoungDiagram(p));
        }
        Collections.sort(syds);
        int length = sips.size();
        int[][] table = new int[length][length];
        for (int i=0; i<length; i++) { // row = syd
            for (int j=0; j<length; j++) { // col = sip
                table[i][j] = calculateChi(syds.get(i), sips.get(j), 0, false);
            }
        }
        return table;
    }

    public int calculateChi(SignedYoungDiagram syd, SignedIntegerPartition sip, int index, boolean isNegative) {
        if (syd.isEmpty()) {
            return 1;
        }
        int[] partition;
        if (!isNegative) {
            if (index >= sip.getPositive().length) {
                isNegative = true;
                index = 0;
            }
        }
        if (isNegative) {
            partition = sip.getNegative();
        } else {
            partition = sip.getPositive();
        }
        int p = partition[index];
        if (checkRecord(syd, p)) {
            return getRecord(syd, p);
        }
        Map<SignedYoungDiagram, Integer> map = syd.reduce(p);
        int sum = 0;
        for (SignedYoungDiagram next : map.keySet()) {
            sum += signFactor(map.get(next), isNegative) * calculateChi(next, sip, index + 1, isNegative);
        }

        return sum;
    }

    public int signFactor(int height, boolean isNegative) {
        return (isNegative && height < 0) == (Math.abs(height) % 2 == 0) ? 1 : -1;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Specify the size of the table as an argument!");
        }
        int size = Integer.parseInt(args[0]);
        CharacterTableGenerator gen = new CharacterTableGenerator();
        int[][] table = gen.generateHnTable(size);
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
