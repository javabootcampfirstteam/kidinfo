package bot;

public class Point {
	private double lat;
	private double lng;
	private String pointAdr;

	public void setPointAdr(String pointAdr) {this.pointAdr = pointAdr;}
	public String getPointAdr() {return pointAdr;}

	public void setLatitude(double lat) {
		this.lat = lat;
	}
	public double getLatitude() {
		return lat;
	}

	public void setLongitude(double lng) {
		this.lng = lng;
	}
	public double getLongitude() {
		return lng;
	}
}
