package util.misc;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point translate(double dx, double dy) {
        return new Point(this.x + dx, this.y + dy);
    }

    public double distance(Point other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("%s (%.3f, %.3f)",
                this.getClass().getSimpleName(), this.x, this.y);
    }
}
