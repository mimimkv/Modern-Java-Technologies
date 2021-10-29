package bg.sofia.uni.fmi.mjt.rentalservice.location;

public class Location {
    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance(Location location) {
        double dx = Math.abs(location.x - x);
        double dy = Math.abs(location.y - y);

        return Math.sqrt(dx * dx + dy * dy);
    }
}
