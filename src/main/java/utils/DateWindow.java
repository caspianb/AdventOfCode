package utils;

public class DateWindow implements Comparable<DateWindow> {

    public DateWindow() {
        this(NEGATIVE_INFINITY, POSITIVE_INFINITY);
    }

    public DateWindow(Long startTime, Long endTime) {
        if (startTime != null && endTime != null && startTime > endTime) {
            throw new RuntimeException("Window start time [" + startTime.toString() + "] must be before or equal to window end time [" + endTime.toString() + "].");
        }
        startTime = startTime != null ? confineValue(startTime, NEGATIVE_INFINITY, POSITIVE_INFINITY) : NEGATIVE_INFINITY;
        endTime = endTime != null ? confineValue(endTime, NEGATIVE_INFINITY, POSITIVE_INFINITY) : POSITIVE_INFINITY;

        _startTime = startTime;
        _endTime = endTime;
        _validWindow = _startTime <= _endTime;
    }

    protected long confineValue(long value, long minValue, long maxValue) {
        return Math.min(Math.max(value, minValue), maxValue);
    }

    public boolean isWideOpen() {
        return _startTime == NEGATIVE_INFINITY && _endTime == POSITIVE_INFINITY;
    }

    public long getStartTime() {
        return _startTime;
    }

    public long getEndTime() {
        return _endTime;
    }

    /**
     * Returns the <b>overlapping</b> intersection between this DateWindow object
     * and the specified DateWindow object. If no overlap occurs, null is returned.<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[10, 20] intersect [15, 25] => [15, 20]</i><br>
     */
    public DateWindow intersect(DateWindow other) {
        DateWindow intersection = null;
        if (this.overlaps(other)) {
            final long startTime = Math.max(this._startTime, other._startTime);
            final long endTime = Math.min(this._endTime, other._endTime);
            intersection = new DateWindow(startTime, endTime);
        }
        return intersection;
    }

    /**
     * Returns the <b>overlapping</b> intersection between this DateWindow
     * object and the specified DateWindowSpan object. If no overlap occurs, 
     * an empty DateWindowSpan is returned.
     */
    public DateWindowSpan intersect(DateWindowSpan other) {
        return other.intersect(this);
    }

    /**
     * Returns the combined range between this DateWindow object and the
     * specified DateWindow object. If no overlap OR border
     * exists between the DateWindows, null is returned.<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[5, 15] union [10, 20] => [5, 20]</i><br>
     * <i>[10, 20] union [21, 30] => [10, 30]</i><br>
     */
    public DateWindow union(DateWindow other) {
        DateWindow union = null;
        if (this.overlaps(other) || this.borders(other)) {
            final long startTime = Math.min(this._startTime, other._startTime);
            final long endTime = Math.max(this._endTime, other._endTime);
            union = new DateWindow(startTime, endTime);
        }
        return union;
    }

    protected long safeAdd(long num1, long num2) {
        // Determine if overflow/underflow would occur
        if (num2 > 0 && num1 > POSITIVE_INFINITY - num2) {
            return POSITIVE_INFINITY;
        }
        else if (num2 < 0 && num1 < NEGATIVE_INFINITY - num2) {
            return NEGATIVE_INFINITY;
        }

        return num1 + num2;
    }

    protected long safeSubtract(long num1, long num2) {
        // Determine if overflow/underflow would occur
        if (num2 > 0 && num1 < NEGATIVE_INFINITY + num2) {
            return NEGATIVE_INFINITY;
        }
        else if (num2 < 0 && num1 > POSITIVE_INFINITY + num2) {
            return POSITIVE_INFINITY;
        }

        return num1 - num2;
    }

    public DateWindow sum(long value) {
        if (value == 0) {
            return this;
        }

        // Calculate start time - NEGATIVE_INFINITY takes precedence over POSITIVE_INFINITY
        final long startTime = (this._startTime == NEGATIVE_INFINITY || value == NEGATIVE_INFINITY) ?
                NEGATIVE_INFINITY : (this._startTime == POSITIVE_INFINITY || value == POSITIVE_INFINITY) ?
                        POSITIVE_INFINITY : safeAdd(this._startTime, value);

        // Calculate end time - POSITIVE_INFINITY takes precedence over NEGATIVE_INFINITY
        final long endTime = (this._endTime == POSITIVE_INFINITY || value == POSITIVE_INFINITY) ?
                POSITIVE_INFINITY : (this._endTime == NEGATIVE_INFINITY || value == NEGATIVE_INFINITY) ?
                        NEGATIVE_INFINITY : safeAdd(this._endTime, value);

        return new DateWindow(startTime, endTime);
    }

    /**
     * Returns a new DateWindow object containing the summed values of this
     * DateWindow object with the specified DateWindow object while respecting
     * negative or positive infinite bounds on the window.<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[5, 10] + [20, 30] => [25, 40]</i><br>
     * <i>[-inf, 10] + [10, 20] => [-inf, 30]<i><br> 
     */
    public DateWindow sum(DateWindow other) {
        // Calculate start time - NEGATIVE_INFINITY takes precedence over POSITIVE_INFINITY
        final long startTime = (this._startTime == NEGATIVE_INFINITY || other._startTime == NEGATIVE_INFINITY) ?
                NEGATIVE_INFINITY : (this._startTime == POSITIVE_INFINITY || other._startTime == POSITIVE_INFINITY) ?
                        POSITIVE_INFINITY : safeAdd(this._startTime, other._startTime);

        // Calculate end time - POSITIVE_INFINITY takes precedence over NEGATIVE_INFINITY
        final long endTime = (this._endTime == POSITIVE_INFINITY || other._endTime == POSITIVE_INFINITY) ?
                POSITIVE_INFINITY : (this._endTime == NEGATIVE_INFINITY || other._endTime == NEGATIVE_INFINITY) ?
                        NEGATIVE_INFINITY : safeAdd(this._endTime, other._endTime);

        return new DateWindow(startTime, endTime);
    }

    public DateWindow subtract(long value) {
        if (value == 0) {
            return this;
        }

        // Calculate start time - NEGATIVE_INFINITY takes precedence over POSITIVE_INFINITY
        final long startTime = (this._startTime == NEGATIVE_INFINITY || value == POSITIVE_INFINITY) ?
                NEGATIVE_INFINITY : (this._startTime == POSITIVE_INFINITY || value == NEGATIVE_INFINITY) ?
                        POSITIVE_INFINITY : safeSubtract(this._startTime, value);

        // Calculate end time - POSITIVE_INFINITY takes precedence over NEGATIVE_INFINITY
        final long endTime = (this._endTime == POSITIVE_INFINITY || value == NEGATIVE_INFINITY) ?
                POSITIVE_INFINITY : (this._endTime == NEGATIVE_INFINITY || value == POSITIVE_INFINITY) ?
                        NEGATIVE_INFINITY : safeSubtract(this._endTime, value);

        return new DateWindow(startTime, endTime);
    }

    /**
     * Returns a new DateWindow object containing the subtracted values of this
     * DateWindow object with the specified DateWindow object while respecting
     * negative or positive infinite bounds on the window.<br>
     * Subtraction is defined as [x1, x2] - [y1, y2] = [x1-y2, x2-y1]<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[25, 50] - [10, 20] => [5, 40]</i><br>
     * <i>[25, 50] - [20, 30] => [-5, 20]</i><br>
     * <i>[-inf, 30] - [10, 20] => [-inf, 20]<i><br>
     * <br>
     * Note: Subtraction IS NOT the inverse of addition. Subtraction is defined
     * in this way to ensure that the resultant difference plus the subtrahend
     * spans the entire potential unknown minuend when attempting to reverse 
     * an addition operation.<br>
     */
    public DateWindow subtractFlip(DateWindow other) {
        // Calculate start time - NEGATIVE_INFINITY takes precedence over POSITIVE_INFINITY
        final long startTime = (this._startTime == NEGATIVE_INFINITY || other._endTime == POSITIVE_INFINITY) ?
                NEGATIVE_INFINITY : (this._startTime == POSITIVE_INFINITY || other._endTime == NEGATIVE_INFINITY) ?
                        POSITIVE_INFINITY : safeSubtract(this._startTime, other._endTime);

        // Calculate end time - POSITIVE_INFINITY takes precedence over NEGATIVE_INFINITY
        final long endTime = (this._endTime == POSITIVE_INFINITY || other._startTime == NEGATIVE_INFINITY) ?
                POSITIVE_INFINITY : (this._endTime == NEGATIVE_INFINITY || other._startTime == POSITIVE_INFINITY) ?
                        NEGATIVE_INFINITY : safeSubtract(this._endTime, other._startTime);

        return new DateWindow(startTime, endTime);
    }

    /**
     * Returns true if the specified DateWindow <b>overlaps</b> this DateWindow object.
     * Overlapping occurs when both DateWindow ranges share some common element.<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[10, 15] overlaps [15, 20] => true</i><br>
     * <i>[10, 20] overlaps [12, 18] => true</i><br>
     */
    public boolean overlaps(DateWindow other) {
        return _validWindow && other._validWindow && !(this._startTime > other._endTime || this._endTime < other._startTime);
    }

    /**
     * Returns offset value between this object and the specified DateWindow object.
     * The offset is defined as the smallest value to add to this object (positive or negative) in order to
     * overlap with the specified object.  If an overlap is detected, offset will always be zero.<br>
     * <b>Examples:</b><br>
     * <i>[10, 15] offset [20, 25] => 5</i><br>
     * <i>[20, 25] offset [10, 15] => -5</i><br>
     * <i>[10, 20] offset [15, 25] => 0</i><br>
     */
    public long offset(DateWindow other) {
        if (!_validWindow || !other._validWindow || this.overlaps(other)) return 0;
        return (this._endTime < other._startTime) ?
                other._startTime - this._endTime :
                other._endTime - this._startTime;
    }

    /**
     * Returns true if the specified DateWindow <b>borders</b> this DateWindow object but
     * <i>no overlap exists</i> between the objects. Bordering is when the start time of one 
     * object and the end time of the other are directly adjacent integral values<br>
     * <br>
     * <b>Examples:</b><br>
     * <i>[10, 14] borders [15, 20] => true</i><br>
     * <i>[10, 14] borders [14, 20] => false because they overlap</i><br>
     */
    public boolean borders(DateWindow other) {
        if (!_validWindow || !other._validWindow || this.overlaps(other)) return false;
        return Math.abs(this._startTime - other._endTime) == 1 ||
                Math.abs(this._endTime - other._startTime) == 1;
    }

    /**
     * Returns true if the specified value is contained within the window range
     * of this DateWindow object.
     */
    public boolean contains(long value) {
        return _validWindow && this._startTime <= value && this._endTime >= value;
    }

    /**
     * Compares the relative position of two windows and returns the result. For overlapping windows,
     * window end time is compared prior to checking window start time. Thus, if window1 starts before
     * and ends after window2, window1 will be considered <b>after</b> window2.
     */
    public int compareTo(DateWindow other) {
        if (this._endTime < other._endTime) return -1;
        if (this._endTime > other._endTime) return 1;
        if (this._startTime < other._startTime) return -1;
        if (this._startTime > other._startTime) return 1;
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (_startTime ^ (_startTime >>> 32));
        result = prime * result + (int) (_endTime ^ (_endTime >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DateWindow)) return false;
        DateWindow other = (DateWindow) obj;
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        final String startStr = (_startTime == NEGATIVE_INFINITY) ?
                "-inf" : (_startTime == POSITIVE_INFINITY) ? "inf" : Long.toString(_startTime);
        final String endStr = (_endTime == NEGATIVE_INFINITY) ?
                "-inf" : (_endTime == POSITIVE_INFINITY) ? "inf" : Long.toString(_endTime);
        return "[" + startStr + ", " + endStr + "]";
    }

    private final long _startTime;
    private final long _endTime;
    private final boolean _validWindow;
    public static final long NEGATIVE_INFINITY = Long.MIN_VALUE;
    public static final long POSITIVE_INFINITY = Long.MAX_VALUE;
}
