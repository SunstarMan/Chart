package view;

import tools.ReadFile;
import tools.Setting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChartPanel extends JPanel {
    private int paddingLeft = 40;
    private int paddingRight = 20;
    private int paddingTop = 20;
    private int paddingBottom = 20;

    private int minValueX = 0;
    private int maxValueX;
    private int xPixelInterval = 30;//打的像素点之间的间隔
    private int xShowInterval = 1;//显示坐标数值之间的间隔

    private int minValueY = -32768;//16bit
    private int maxValueY = 32767;
    private int yPixelInterval = 15;

    private List<Integer> data;
    private int startX;
    private int lastMinValueX;

    private Setting setting;

    public ChartPanel() {
        super();
        this.setMouseWheel();
        this.setMouseDrag();
        setting = new Setting();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(255, 255, 255));//每一个chartPanel的背景色
        g.fillRect(0, 0, this.getWidth(), this.getHeight());//背景色作用的范围：整个chartPanel
        this.paintXAxis(g);
        this.paintYAxis(g);
        this.paintPoints(g);
        this.paintXAxis(g);
        this.paintYAxis(g);
    }

    public void setInputStream(String filePath) {
        this.data = ReadFile.readDataFile(filePath);
        repaint();//重新绘制一遍
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
        repaint();
    }

    /*画X轴：
    设定最小值和两个间隔，每一个数值就根据它们
    相对应最值的比例，换算成在坐标轴上对应的位置 */
    private void paintXAxis(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, this.getHeight() - paddingBottom, this.getWidth(), paddingBottom);
        g.setColor(setting.getAxisColorUse());

        g.drawLine(paddingLeft,
                this.getHeight() - paddingBottom,
                this.getWidth() - paddingRight,
                this.getHeight() - paddingBottom);
        maxValueX = (this.getWidth() - paddingLeft - paddingRight) / xPixelInterval * xShowInterval + minValueX;

        for (int i = minValueX; i <= maxValueX; i += xShowInterval) {
            g.drawString(String.valueOf(i),
                    paddingLeft + (i - minValueX) / xShowInterval * xPixelInterval,
                    this.getHeight() - paddingBottom + 15);
        }
    }

    /*画y轴：
    设定最大值和最小值，确定中点的位置，再确定能显
    示几个数值点，根据它是第几个点的比例算出它的数值 */
    private void paintYAxis(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, paddingLeft, this.getHeight() - paddingBottom);
        g.setColor(setting.getAxisColorUse());

        g.drawLine(paddingLeft, paddingTop, paddingLeft, this.getHeight() - paddingBottom);
        int mid = (this.getHeight() - paddingBottom + paddingTop) / 2;//Y轴中心点在ChartPanel中的绝对坐标
        int num = (this.getHeight() - paddingTop - paddingBottom) / 2 / yPixelInterval;//Y轴能显示的点的数量
        g.drawString("0", 0, mid);//Y轴0点的坐标

        for (int i = 1; i <= num; i++) {
            g.drawString(String.valueOf(i * maxValueY / num), 0, mid - i * yPixelInterval);//y轴正半轴显示点
            g.drawString(String.valueOf(i * minValueY / num), 0, mid + i * yPixelInterval);//y轴负半轴显示点
        }
    }

    /*取点+画图：
    确定两个点的坐标，将这个点与上一个点相连*/
    private void paintPoints(Graphics g) {
        if (data == null || data.size() == 0) {
            return;
        }

        g.setColor(setting.getLineColorUse());

        //确定第一个点的坐标
        int lastX = paddingLeft;
        int lastY = (int) (((data.get(0) - maxValueY) * 1.0 / (minValueY - maxValueY)) *
                (this.getHeight() - paddingTop - paddingBottom) + paddingTop);

        for (int i = 1; i < data.size(); i++) {
            //每一个数值点的坐标以坐标轴的长度比例来进行计算
            int x = (int) (((i - minValueX) * 1.0 / (maxValueX - minValueX)) *
                    (this.getWidth() - paddingLeft - paddingRight) + paddingLeft);
            int y = (int) (((data.get(i) - maxValueY) * 1.0 / (minValueY - maxValueY)) *
                    (this.getHeight() - paddingTop - paddingBottom) + paddingTop);

            g.drawLine(lastX, lastY, x, y);//使用上一个点和这个点相连接画成一条直线
            //点往后移动
            lastX = x;
            lastY = y;
        }
    }

    /*X轴的拖拽：
    首先记录下鼠标按下的位置，然后再计算拖到的位置与初始位置
    的差值，x轴的最小值做相应的变换，往左托变小，往右托变大*/
    private void setMouseDrag() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dragInterval = (e.getX() - startX) / xPixelInterval * xShowInterval;
                minValueX = lastMinValueX - dragInterval;
                if (minValueX < 0) {
                    minValueX = 0;
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                lastMinValueX = minValueX;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    //放大缩小
    private void setMouseWheel() {
        int minY = 2048;
        int maxY = 32768;
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getX() > paddingLeft && e.getY() > getHeight() - paddingBottom) {
                    if (e.getWheelRotation() < 0) {
                        if (xShowInterval > 1) {
                            xShowInterval = xShowInterval - 1;
                        } else {
                            xPixelInterval = xPixelInterval + 1;
                        }
                    } else {
                        if (xPixelInterval > 20) {
                            xPixelInterval = xPixelInterval - 1;
                        } else {
                            xShowInterval = xShowInterval + 1;
                        }
                    }
                }

                //鼠标坐标点在Y轴上滑动
                if (e.getX() < paddingLeft && e.getY() < getHeight() - paddingBottom) {
                    if (e.getWheelRotation() < 0) {
                        maxValueY = maxValueY + 100;
                        minValueY = minValueY - 100;
                        if (minValueY < -maxY) {
                            minValueY = -maxY;
                        }
                        if (maxValueY > maxY - 1) {
                            maxValueY = maxY - 1;
                        }
                    } else {
                        maxValueY = maxValueY - 100;
                        minValueY = minValueY + 100;
                        if (maxValueY < minY - 1) {
                            maxValueY = minY - 1;
                        }
                        if (minValueY > -minY) {
                            minValueY = -minY;
                        }
                    }
                }
                repaint();
            }
        });
    }

    //开三次方根
    public void cubeRoot() {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            data.set(i, (int) Math.pow(data.get(i), 1.0/3));
        }
    }

    //绝对值
    public void abs() {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            data.set(i, Math.abs(data.get(i)));
        }
    }
}
