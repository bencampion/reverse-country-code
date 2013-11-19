package com.github.countrycode.reverse;

class BoundingBox implements Geometry {

    private final double north;
    private final double south;
    private final double east;
    private final double west;

    public BoundingBox(double north, double south, double east, double west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    @Override
    public boolean contains(double lat, double lon) {
        return lat <= north && lat >= south && lon <= east && lon >= west;
    }

    public static class Builder {

        private double north = Double.NEGATIVE_INFINITY;
        private double south = Double.POSITIVE_INFINITY;
        private double east = Double.NEGATIVE_INFINITY;
        private double west = Double.POSITIVE_INFINITY;

        public Builder addPoint(Point point) {
            addPoint(point.getLatitude(), point.getLongitude());
            return this;
        }

        public Builder addBox(BoundingBox box) {
            addPoint(box.north, box.east);
            addPoint(box.south, box.west);
            return this;
        }

        public BoundingBox build() {
            return new BoundingBox(north, south, east, west);
        }

        private void addPoint(double lat, double lon) {
            north = Math.max(lat, north);
            south = Math.min(lat, south);
            east = Math.max(lon, east);
            west = Math.min(lon, west);
        }

    }

}
