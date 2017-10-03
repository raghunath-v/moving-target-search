import javax.xml.stream.Location;

public class Utils {

    /**
     * cimputes the distance between two locations given their latitude and longtitude
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return distance between the two points
     */
    public static double computeDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = (float) (earthRadius * c);

        return dist;
    }
}
