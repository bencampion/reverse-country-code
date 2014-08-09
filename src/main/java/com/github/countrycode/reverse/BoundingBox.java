package com.github.countrycode.reverse;

class BoundingBox implements Geometry {

    private final Point upper;
    private final Point lower;

    public BoundingBox(Point upper, Point lower) {
        this.upper = upper;
        this.lower = lower;
    }

    @Override
    public boolean contains(double lat, double lon) {
        return lat <= upper.getLatitude() && lat >= lower.getLatitude()
                && lon <= upper.getLongitude() && lon >= lower.getLongitude();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this;
    }

    public static class Builder {

        private double north = Double.NEGATIVE_INFINITY;
        private double south = Double.POSITIVE_INFINITY;
        private double east = Double.NEGATIVE_INFINITY;
        private double west = Double.POSITIVE_INFINITY;

        public Builder addPoints(Iterable<Point> points) {
            for (Point point : points) {
                addPoint(point);
            }
            return this;
        }

        public Builder addGeometries(Iterable<? extends Geometry> geometries) {
            for (Geometry geometry : geometries) {
                BoundingBox box = geometry.getBoundingBox();
                addPoint(box.upper);
                addPoint(box.lower);
            }
            return this;
        }

        public BoundingBox build() {
            return new BoundingBox(new Point(north, east), new Point(south,
                    west));
        }

        private void addPoint(Point point) {
            north = Math.max(point.getLatitude(), north);
            south = Math.min(point.getLatitude(), south);
            east = Math.max(point.getLongitude(), east);
            west = Math.min(point.getLongitude(), west);
        }

    }

}
