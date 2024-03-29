package com.xizang.gui;

import com.xizang.data.ReadFile;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * @author 剑雨江湖
 * @version 1.0
 * @date 2020/6/29 17:42
 * @desc TODO
 */
public class FetchData extends JFrame
{
    private static final long serialVersionUID = 1L;
    private JTextField field;
    private ReadFile readFile;
    public FetchData(){

        this.setTitle("拖拽文件至文本框显示文件路径");
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        field = new JTextField();
        field.setBounds(50, 50, 300, 30);

        field.setTransferHandler(new TransferHandler()
        {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    System.out.println(filepath);
                    field.setText(filepath);
                    readFile = new ReadFile(filepath);
                    readFile.getBigOrSmallDate();
                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });

        this.add(field);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new FetchData();
    }
}


