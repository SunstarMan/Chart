package tools;

import java.awt.*;
import java.util.List;

public class Setting {
    private List<Integer> axisColor;//坐标轴颜色
    private List<Integer> lineColor;//线条颜色

    public Setting() {

    }

    public List<Integer> getAxisColor() {
        return axisColor;
    }

    public Color getAxisColorUse() {
        //配置文件rgb通道数量不对
        if (axisColor == null || axisColor.size() != 3) {
            return new Color(0, 0, 0);
        }

        //配置文件rgb超出正常数值范围
        for (int i = 0; i < axisColor.size(); i++) {
            if (axisColor.get(i) < 0 || axisColor.get(i) > 255) {
                return new Color(0, 0, 0);
            }
        }

        return new Color(axisColor.get(0), axisColor.get(1), axisColor.get(2));
    }

    public void setAxisColor(List<Integer> axisColor) {
        this.axisColor = axisColor;
    }

    public List<Integer> getLineColor() {
        return lineColor;
    }

    public Color getLineColorUse() {
        if (lineColor == null || lineColor.size() != 3) {
            return new Color(0, 0, 0);
        }

        for (Integer integer : lineColor) {
            if (integer < 0 || integer > 255) {
                return new Color(0, 0, 0);
            }
        }

        return new Color(lineColor.get(0), lineColor.get(1), lineColor.get(2));
    }

    public void setLineColor(List<Integer> lineColor) {
        this.lineColor = lineColor;
    }
}
