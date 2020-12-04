package util.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DateWindowSpan implements Iterable<DateWindow> {

    public DateWindowSpan() {
    }

    public DateWindowSpan(Long startTime, Long endTime) {
        this(new DateWindow(startTime, endTime));
    }

    public DateWindowSpan(DateWindow window) {
        _windows.add(window);
    }

    public DateWindowSpan(DateWindowSpan other) {
        _windows = new ArrayList<DateWindow>(other._windows);
    }

    public DateWindowSpan(Collection<DateWindow> windows) {
        _windows = new ArrayList<DateWindow>(windows);
        collapseWindows();
    }

    public DateWindowSpan(Long[] windows) {
        if (windows.length % 2 != 0) {
            throw new RuntimeException("Window array does not contain an even number of elements: " + windows);
        }

        for (int i = 0; i < windows.length; i += 2) {
            _windows.add(new DateWindow(windows[i], windows[i + 1]));
        }
        collapseWindows();
    }

    public DateWindowSpan(long[] windows) {
        if (windows.length % 2 != 0) {
            throw new RuntimeException("Window array does not contain an even number of elements: " + windows);
        }

        for (int i = 0; i < windows.length; i += 2) {
            _windows.add(new DateWindow(windows[i], windows[i + 1]));
        }
        collapseWindows();
    }

    /**
     * WARNING: This method is extremely slow for adding windows!<br>
     * Should be used for TESTING purposes only.
     * NOTE: This method MODIFIES the DateWindowSpan object. It breaks the immutability
     * of this class guaranteed through all public methods.
     * @param window
     */
    protected void addWindow(DateWindow window) {
        _windows.add(window);
        collapseWindows();
    }

    /**
     * WARNING: This method is extremely slow for adding windows!<br>
     * Should be used for TESTING purposes only.
     * NOTE: This method MODIFIES the DateWindowSpan object. It breaks the immutability
     * of this class guaranteed through all public methods.
     * @param window
     */
    protected void addWindowSpan(DateWindowSpan windowSpan) {
        _windows.addAll(windowSpan.getWindows());
        collapseWindows();
    }

    /**
     * This method will sort and iterate through the windows in the span,
     * remove any subsequent windows that overlap and replace them with
     * a single element which joins both of the spans together.
     */
    protected void collapseWindows() {
        Collections.sort(_windows);
        for (int i = 0; i < _windows.size() - 1; i++) {
            joinAdjacentWindows(i);
        }
    }

    /**
     * Detects all overlapping/bordering windows starting from 'startIndex' argument
     * and builds a single window to replace all overlapping spans in the list 
     */
    private void joinAdjacentWindows(int startIndex) {
        int numOverlapsDetected = 0;
        DateWindow currWindow = getWindow(startIndex);
        for (int i = startIndex; i < _windows.size() - 1; i++) {
            DateWindow nextWindow = getWindow(i + 1);
            DateWindow newWindow = currWindow.union(nextWindow);

            if (newWindow != null) {
                numOverlapsDetected = (i + 1) - startIndex;
                currWindow = newWindow;
            }
        }

        if (numOverlapsDetected > 0) {
            // second argument to List.sublist is *exclusive*, so add '1' to ensure we clear all overlapping windows
            _windows.subList(startIndex, startIndex + numOverlapsDetected + 1).clear();
            _windows.add(startIndex, currWindow);
        }
    }

    public DateWindow getWindow(int i) {
        return _windows.get(i);
    }

    public DateWindow getFirstWindow() {
        return (!_windows.isEmpty()) ? _windows.get(0) : null;
    }

    public DateWindow getLastWindow() {
        return (!_windows.isEmpty()) ? _windows.get(_windows.size() - 1) : null;
    }

    public List<DateWindow> getWindows() {
        return Collections.unmodifiableList(_windows);
    }

    public int size() {
        return _windows.size();
    }

    public boolean isEmpty() {
        return _windows.isEmpty();
    }

    public boolean isWideOpen() {
        if (!isEmpty()) {
            return _windows.get(0).isWideOpen();
        }
        return false;
    }

    /**
     * Locates the index of the first window that *overlaps* the specified window object (may be equal).
     * Returns -1 if no such window could be located in the span.
     */
    protected int binarySearchFirstOverlap(DateWindow other) {
        int firstOverlapIndex = -1;
        int min = 0;
        int max = _windows.size() - 1;

        while (min <= max) {
            int mid = (min + max) / 2;

            DateWindow currWindow = _windows.get(mid);
            if (currWindow.overlaps(other)) {
                // Check if current window is the first overlapping window in the list
                if (mid == 0 || !_windows.get(mid - 1).overlaps(other)) {
                    firstOverlapIndex = mid;
                    break;
                }
                max = mid - 1;
            }
            else {
                if (currWindow.compareTo(other) <= 0) {
                    min = mid + 1;
                }
                else {
                    max = mid - 1;
                }
            }
        }

        return firstOverlapIndex;
    }

    /**
     * Locates the index of the first window that is *less than but not equal* (but may overlap) the specified window object.
     * Returns -1 if no such window could be located in the span.
     */
    protected int binarySearchLastWindowBefore(DateWindow other) {
        int lastElementBeforeIndex = -1;
        int min = 0;
        int max = _windows.size() - 1;

        while (min <= max) {
            int mid = (min + max) / 2;

            DateWindow currWindow = _windows.get(mid);
            if (currWindow.compareTo(other) < 0) {
                // Check if current window is the last window in the list less than other
                if (mid == _windows.size() - 1 || _windows.get(mid + 1).compareTo(other) >= 0) {
                    lastElementBeforeIndex = mid;
                    break;
                }
                min = mid + 1;
            }
            else {
                max = mid - 1;
            }
        }

        return lastElementBeforeIndex;
    }

    /**
     * Locates the index of the first window that is *greater than but not equal* (but may overlap) the specified window object.
     * Returns -1 if no such window could be located in the span.
     */
    protected int binarySearchFirstWindowAfter(DateWindow other) {
        int firstElementAfterIndex = -1;
        int min = 0;
        int max = _windows.size() - 1;

        while (min <= max) {
            int mid = (min + max) / 2;

            DateWindow currWindow = _windows.get(mid);
            if (currWindow.compareTo(other) > 0) {
                // Check if current window is the first window in the list greater than other
                if (mid == 0 || _windows.get(mid - 1).compareTo(other) <= 0) {
                    firstElementAfterIndex = mid;
                    break;
                }
                max = mid - 1;
            }
            else {
                min = mid + 1;
            }
        }

        return firstElementAfterIndex;
    }

    public boolean overlaps(DateWindow other) {
        int firstOverlapIndex = binarySearchFirstOverlap(other);
        return firstOverlapIndex >= 0;
    }

    /**
     * Returns the first window in the span which is strictly greater than the specified window.<br>
     * <b>Note:</b> Due to the ability to compare windows which overlap, the returned 
     * window MAY overlap with the specified window, but it WILL NOT be equal to the specified window.<br> 
     * If no eligible window can be located, null is returned.
     */
    public DateWindow getFirstWindowAfter(DateWindow other) {
        int index = binarySearchFirstWindowAfter(other);
        DateWindow window = (index != -1) ? _windows.get(index) : null;
        return window;
    }

    /**
     * Returns the last window in the span which is strictly less than the specified window.<br>
     * <b>Note:</b> Due to the ability to compare windows which overlap, the returned 
     * window MAY overlap with the specified window, but it WILL NOT be equal to the specified window.<br> 
     * If no eligible window can be located, null is returned.
     */
    public DateWindow getLastWindowBefore(DateWindow other) {
        int index = binarySearchLastWindowBefore(other);
        DateWindow window = (index != -1) ? _windows.get(index) : null;
        return window;
    }

    public DateWindowSpan intersect(DateWindow other) {
        if (other.isWideOpen()) {
            return this;
        }

        int firstOverlapIndex = binarySearchFirstOverlap(other);

        // intersect all windows from the first overlapping index until we can no longer intersect
        List<DateWindow> windows = new ArrayList<DateWindow>();
        if (firstOverlapIndex >= 0) {
            for (; firstOverlapIndex < size(); firstOverlapIndex++) {
                DateWindow thisWindow = this.getWindow(firstOverlapIndex);
                DateWindow intersectionWindow = thisWindow.intersect(other);
                if (intersectionWindow == null) break;

                windows.add(intersectionWindow);
            }
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    public DateWindowSpan intersect(Collection<DateWindow> other) {
        return this.intersect(new DateWindowSpan(other));
    }

    public DateWindowSpan intersect(DateWindowSpan other) {
        if (other.isWideOpen()) {
            return this;
        }

        // To avoid O(n^2) performance here, we will separately track and slide the
        // current index of each span object and attempt to intelligently intersect
        // windows together until we reach the end of one object.

        int thisIndex = 0;
        int otherIndex = 0;

        List<DateWindow> windows = new ArrayList<DateWindow>();
        while (thisIndex < this.size() && otherIndex < other.size()) {
            DateWindow thisWindow = this.getWindow(thisIndex);
            DateWindow otherWindow = other.getWindow(otherIndex);

            DateWindow window = thisWindow.intersect(otherWindow);
            if (window != null) {
                windows.add(window);
            }

            int compareVal = thisWindow.compareTo(otherWindow);
            if (compareVal <= 0) thisIndex++;
            if (compareVal >= 0) otherIndex++;
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    /**
     * Returns true if this date window span contains the specified value in some window range.
     */
    public boolean contains(long value) {
        if (this.isWideOpen()) {
            return true;
        }

        DateWindow window = new DateWindow(value, value);
        return binarySearchFirstOverlap(window) >= 0;
    }

    /**
     * Returns smallest positive offset located between all windows in this span and the specified DateWindow object.
     * The POSITIVE offset is the smallest *positive* value that, when added to this window span object, will allow
     * it to overlap with the specified window object.<br>
     * <br>
     * NULL will be returned if all windows in this span are strictly greater than the specified window.<br>
     * If an overlap is detected, offset will always be zero.<br>
     */
    public Long offsetPositive(DateWindow other) {
        if (this.overlaps(other)) return 0l;

        // No overlap was located, so get the largest window that is less than 'other'
        Long minPositiveOffset = null;
        int beforeIndex = binarySearchLastWindowBefore(other);
        if (beforeIndex >= 0) {
            // beforeIndex window should form POSITIVE offset with other
            long positiveOffset = getWindow(beforeIndex).offset(other);
            minPositiveOffset = positiveOffset;
        }

        return minPositiveOffset;
    }

    /**
     * This method will return the smallest positive offset that exists between ALL windows in this window span
     * and the specified window span. The smallest positive offset is defined as the smallest value that, when added
     * to this window span object, will allow it to overlap with the specified window span object.<br>
     * <br>
     * NULL will be returned if all windows in this span are strictly greater than all windows in the specified span.<br>
     * If an overlap is detected, offset will always be zero.<br>
     */
    public Long offsetPositive(DateWindowSpan other) {
        int thisIndex = 0;
        int otherIndex = 0;

        Long minPositiveOffset = null;

        while (thisIndex < this.size() && otherIndex < other.size()) {
            DateWindow thisWindow = this.getWindow(thisIndex);
            DateWindow otherWindow = other.getWindow(otherIndex);

            long thisOffset = thisWindow.offset(otherWindow);
            if (thisOffset == 0l) {
                minPositiveOffset = 0l;
                break;
            }

            if (thisOffset > 0 && (minPositiveOffset == null || minPositiveOffset > thisOffset)) {
                minPositiveOffset = thisOffset;
            }

            int compareVal = thisWindow.compareTo(otherWindow);
            if (compareVal <= 0) thisIndex++;
            if (compareVal >= 0) otherIndex++;
        }

        return minPositiveOffset;
    }

    public DateWindowSpan union(DateWindowSpan other) {
        List<DateWindow> windows = new ArrayList<DateWindow>();
        windows.addAll(_windows);
        windows.addAll(other._windows);

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    public DateWindowSpan sum(long value) {
        if (value == 0) {
            return this;
        }

        List<DateWindow> windows = new ArrayList<DateWindow>();
        for (DateWindow thisWindow : _windows) {
            DateWindow window = thisWindow.sum(value);
            windows.add(window);
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    public DateWindowSpan sum(DateWindow other) {
        if (other.getStartTime() == other.getEndTime()) {
            return this.sum(other.getStartTime());
        }

        List<DateWindow> windows = new ArrayList<DateWindow>();
        for (DateWindow thisWindow : _windows) {
            DateWindow window = thisWindow.sum(other);
            windows.add(window);
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    public DateWindowSpan subtract(long value) {
        if (value == 0) {
            return this;
        }

        List<DateWindow> windows = new ArrayList<DateWindow>();
        for (DateWindow thisWindow : _windows) {
            DateWindow window = thisWindow.subtract(value);
            windows.add(window);
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    public DateWindowSpan subtract(DateWindow other) {
        if (other.getStartTime() == other.getEndTime()) {
            return this.subtract(other.getStartTime());
        }

        List<DateWindow> windows = new ArrayList<DateWindow>();
        for (DateWindow thisWindow : _windows) {
            DateWindow window = thisWindow.subtractFlip(other);
            windows.add(window);
        }

        DateWindowSpan newSpan = new DateWindowSpan(windows);
        return newSpan;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_windows == null) ? 0 : _windows.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DateWindowSpan)) return false;
        DateWindowSpan other = (DateWindowSpan) obj;
        if (_windows == null) {
            if (other._windows != null) return false;
        }
        else if (!_windows.equals(other._windows)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder windowStrings = new StringBuilder();
        for (DateWindow window : _windows) {
            if (windowStrings.length() > 0) windowStrings.append(", ");
            windowStrings.append(window.toString());
        }
        return "[" + windowStrings.toString() + "]";
    }

    @Override
    public Iterator<DateWindow> iterator() {
        return _windows.iterator();
    }

    private List<DateWindow> _windows = new ArrayList<DateWindow>();
}
