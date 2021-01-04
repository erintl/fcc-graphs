package graphs;

public class DValue implements Comparable<DValue> {
    private String label;
    private double value;

    public DValue(String label, double value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public int compareTo(DValue dValue) {
        int compareValue = 0;
        if (value == dValue.getValue()) {
            compareValue = 0;
        } else if (value < dValue.getValue()) {
            compareValue = -1;
        } else {
            compareValue = 1;
        }
        return compareValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DValue{" +
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
