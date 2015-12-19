package com.ariweiland.hyperoctahedral;

import acm.program.ConsoleProgram;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

/**
 * @author Ari Weiland
 */
public class GeneratorGUI extends ConsoleProgram {

    private final CharacterTableGenerator gen = new CharacterTableGenerator();

    private final ButtonGroup group = new ButtonGroup();
    private final JRadioButton symmetric = new JRadioButton("Symmetric");
    private final JRadioButton hyperoctahedral = new JRadioButton("Hyperoctahedral");
    private boolean isSymmetric;

    private final JTextField size = new JTextField("0");

    private final JButton generate = new JButton("Generate!");
    private final JButton clear = new JButton("Clear");

    @Override
    public void init() {
        setSize(800, 600);

        group.add(symmetric);
        add(symmetric, WEST);
        symmetric.addActionListener(this);

        group.add(hyperoctahedral);
        add(hyperoctahedral, WEST);
        hyperoctahedral.addActionListener(this);

        symmetric.doClick();

        add(size, WEST);
        size.addActionListener(this);

        add(generate, WEST);
        add(clear, WEST);

        addActionListeners();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(symmetric)) {
            isSymmetric = true;
        } else if (source.equals(hyperoctahedral)) {
            isSymmetric = false;
        } else if (source.equals(size) || source.equals(generate)) {
            String temp = size.getText();
            if (Pattern.matches("\\d+", temp)) {
                clear();
                generateTable(Integer.parseInt(temp));
            } else {
                JOptionPane.showMessageDialog(null, "\'" + temp + "\' is not a valid size. Please specify a non-negative integer.", "Invalid Size", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source.equals(clear)) {
            clear();
        }
    }

    public void generateTable(int n) {
        int[][] table;
        if (isSymmetric) {
            table = gen.generateSnTable(n);
        } else {
            table = gen.generateHnTable(n);
        }
        print("{");
        for (int i=0; i<table.length; i++) {
            print("{");
            for (int j=0; j<table.length; j++) {
                print(table[i][j]);
                if (j < table.length - 1) {
                    print(",");
                }
            }
            if (i < table.length - 1) {
                println("},");
            }
        }
        println("}}");
    }

    public void clear() {
        getConsole().clear();
    }

    public static void main(String[] args) {
        new GeneratorGUI().start();
    }
}
