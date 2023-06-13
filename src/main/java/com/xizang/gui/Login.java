package com.xizang.gui;


import com.xizang.data.WaterBean;
import com.xizang.utils.DataUtils;
import com.xizang.utils.DateUtils;
import com.xizang.utils.ExeclUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/11 23:38
 */
public class Login extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static int count = 0;
    JPanel jp1, jp2, jp3, jp4, top;
    JLabel jlb1, jlb2, jlb3;
    JButton jb1, jb2;
    JTextField jtf1;
    JTextField jpf1;


    Map<String,String> data = new HashMap<>();//存放密码
    // 构造函数
    public Login() {

        data.put("wq", "123");
        top = new JPanel();
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        top.setBackground( new Color(225,225,150));
        jp1.setBackground(new Color(225,225,140));
        jp2.setBackground(new Color(225,225,150));
        jp3.setBackground(new Color(225,225,130));
        jp4.setBackground(new Color(225,225,130));


        jlb1 = new JLabel("原始数据：");
        jlb2 = new JLabel("核验数据：");
        jlb3 = new JLabel("等待...");

        jb1 = new JButton("计算");
        jb2 = new JButton("清空");
        jb1.addActionListener(this);
        jb2.addActionListener(this);

        jb1.setBackground(Color.GREEN);
        //jb1.setForeground(Color.BLUE);


        jtf1 = new JTextField(28);
        jtf1.setTransferHandler(getTransferHandler(jtf1));
        jtf1.setPreferredSize(new Dimension (45,40));

        jpf1 = new JTextField(28);// 设置布局管理
        jpf1.setTransferHandler(getTransferHandler(jpf1));
        jpf1.setPreferredSize(new Dimension (45,40));

        this.setLayout(new GridLayout(5, 1));

        // 加入各个组件		第一个部分是用户名：空白的test
        jp1.add(jlb1);
        jp1.add(jtf1);
        //第二个部分是
        jp2.add(jlb2);
        jp2.add(jpf1);
        //第三个部分
        jp3.add(jb1);
        jp3.add(jb2);

        jp4.add(jlb3);
        // 加入到JFrame
        this.add(top);
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
        this.add(jp4);


        this.setSize(450,400);

        this.setLocation(300, 200);
        this.setTitle("数据处理");
        // this.getContentPane().setBackground(Color.red );
        //this.setBackground( Color.blue);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source==jb1&&count==0) {
            count = -1;
            jlb3.setText("计算中......");
            String dataPath = jtf1.getText().trim();
            String checkPath = jpf1.getText().trim();
            String outPath = dataPath.replace(".xlsx", "——结果" + DateUtils.getToday() + ".xlsx");

            List<List<String>> lists = ExeclUtils.readExcel(dataPath.trim());
            List<List<String>> check = ExeclUtils.readExcel(checkPath.trim());
            List<List<WaterBean>> result = DataUtils.parseData(lists, check);
            ExeclUtils.writeExcel(outPath, result);
            jlb3.setText("等待...");
            count = 0;

        }

        if (source == jb2) {
            jtf1.setText("");
            jpf1.setText("");
            count = 0;
        }

    }




	/*public void paint(Graphics g) {
		URL fileURL=this.getClass().getResource("resource/login.jpg");
		//ImageIcon icon = new ImageIcon("img/"+num+".jpg");
		ImageIcon icon = new ImageIcon(fileURL);
		g.drawImage(icon.getImage(), 0, 0, getWidth(),getHeight(),
		icon.getImageObserver());

	}*/

    public TransferHandler getTransferHandler(JTextField jtf1) {
        return new TransferHandler()
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
                    //System.out.println(filepath);
                    jtf1.setText(filepath);
                    //readFile = new ReadFile(filepath);
                    //readFile.getBigOrSmallDate();
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
        };
    }

    public static void main(String[] args) {
        new Login();

    }
}