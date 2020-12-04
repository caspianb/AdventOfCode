package util.misc;

public class LineSegment {

    private final Point p1;
    private final Point p2;
    private final double slope;
    private final double offset;

    public static LineSegment from(int x1, int y1, int x2, int y2) {
        return new LineSegment(new Point(x1, y1), new Point(x2, y2));
    }

    public LineSegment(Point p1, Point p2) {
        // Prefer storing segments "left to right"
        if (p1.getX() <= p2.getX()) {
            this.p1 = p1;
            this.p2 = p2;
        }
        else {
            this.p1 = p2;
            this.p2 = p1;
        }

        // Create our line equation y=mx+b
        // -- calculate m and b (slope / offset)
        // If we're vertical, then slope is infinite and no offset
        if (p1.getX() == p2.getX()) {
            this.slope = Double.POSITIVE_INFINITY;
            this.offset = Double.NaN;
        }

        // Otherwise, calculate!
        else {
            this.slope = (p2.getY() - p1.getY()) / (double) (p2.getX() - p1.getX());
            this.offset = p1.getY() - (slope * p1.getX());
        }
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public double getSlope() {
        return slope;
    }

    public double getOffset() {
        return offset;
    }

    public Point intersects(LineSegment other) {
        double x = (other.offset - this.offset) / (this.slope - other.slope);

        // If x is in range of BOTH segments, they intersect!
        boolean inRange = this.p1.getX() <= x && x <= this.p2.getX();
        inRange &= other.p1.getX() <= x && x <= other.p2.getX();

        if (inRange) {
            double y = slope * x + offset;
            return new Point(x, y);
        }

        return null;
    }

    public double angleBetweenRads(LineSegment other) {
        double theta = (other.slope - this.slope) / (1 + this.slope * other.slope);
        return Math.atan(theta);
    }

    public double angleBetween(LineSegment other) {
        return Math.toDegrees(angleBetweenRads(other));
    }

    @Override
    public String toString() {
        return String.format("%s y = (%.3f)x + %.3f",
                this.getClass().getSimpleName(), slope, offset);
    }
}
