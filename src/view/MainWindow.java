package view;

import tools.ReadFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {
    private ChartPanel rawChart;
    private ChartPanel secondChart;
    private ChartPanel threeChart;

    //构造方法
    public MainWindow(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点“×”直接退出，不在后台继续运行
        this.setUpMenu();
        this.showCenter();
        this.setUpCharts();
        this.setVisible(true);
    }

    private void setUpCharts() {
        GridBagLayout gridBagLayout = new GridBagLayout();//布局
        GridBagConstraints gridBagConstraints = new GridBagConstraints();//约束
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        JPanel jPanel = new JPanel(gridBagLayout);
        rawChart = new ChartPanel();
        secondChart = new ChartPanel();
        threeChart = new ChartPanel();
        gridBagLayout.addLayoutComponent(rawChart,gridBagConstraints);
        gridBagLayout.addLayoutComponent(secondChart,gridBagConstraints);
        gridBagLayout.addLayoutComponent(threeChart,gridBagConstraints);
        jPanel.add(rawChart);
        jPanel.add(secondChart);
        jPanel.add(threeChart);
        this.setContentPane(jPanel);
    }

    private void showCenter() {
        //获取电脑屏幕大小
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //默认的JFrame的大小为电脑屏幕的2/3，并使其居中
        int width = (screenWidth * 2) / 3;
        int height = (screenHeight * 2) / 3;
        this.setSize(width, height);
        this.setLocation(screenWidth / 6, screenHeight / 6);
    }

    //设置按钮事件
    private void setUpMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("文件");

        JMenuItem fileMenuItem = new JMenuItem("打开数据文件");
        fileMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//设置选择的模式：文件或者文件夹
                jFileChooser.showDialog(new JLabel(), "打开数据文件");
                File file = jFileChooser.getSelectedFile();
                if (file == null) {
                    System.out.println("no file selected");
                    return;
                }
                rawChart.setInputStream(file.getAbsolutePath());
                secondChart.setInputStream(file.getAbsolutePath());
                secondChart.cubeRoot();
                threeChart.setInputStream(file.getAbsolutePath());
                threeChart.abs();
            }
        });

        JMenuItem settingMenuItem = new JMenuItem("打开配置文件");
        settingMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jFileChooser.showDialog(new JLabel(), "打开配置文件");
                File file = jFileChooser.getSelectedFile();
                if (file == null) {
                    System.out.println("no file selected");
                    return;
                }
                rawChart.setSetting(ReadFile.readSettingFile(file.getAbsolutePath()));
                secondChart.setSetting(ReadFile.readSettingFile(file.getAbsolutePath()));
                threeChart.setSetting(ReadFile.readSettingFile(file.getAbsolutePath()));
            }
        });

        jMenu.add(fileMenuItem);
        jMenu.addSeparator();//两个选项之间的间隔线
        jMenu.add(settingMenuItem);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
    }
}
