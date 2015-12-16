package svrptw;

public class Parameters {
	private double coefDelay;
	private double coefEarliness;
	private double coefDistance;
	private double coefOvertime;
	private double coefVechile;
	private boolean flagTotal;
	private double coefService;
	private double coefTransportation;
	private double coefRho;
	private double shape;
	private double scale;
	
	public Parameters() {
		this.coefDelay = 0;
		this.coefEarliness = 0;
		this.coefDistance = 0;
		this.coefOvertime = 0;
		this.coefVechile = 0;
		this.flagTotal = false;
		this.coefService = 0;
		this.coefTransportation = 0;
		this.coefRho = 0;
		this.shape = 0;
		this.scale = 0;
	}

	public double getCoefDelay() {
		return this.coefDelay;
	}
	
	public void setCoefDelay(double coefDelay) {
		this.coefDelay = coefDelay;
	}

	public double getCoefEarliness() {
		return this.coefEarliness;
	}
	
	public void setCoefEarliness(double coefEarliness) {
		this.coefEarliness = coefEarliness;
	}

	public double getCoefDistance() {
		return this.coefDistance;
	}
	
	public void setCoefDistance(double coefDistance) {
		this.coefDistance = coefDistance;
	}

	public double getCoefOvertime() {
		return this.coefOvertime;
	}
	
	public void setCoefOvertime(double coefOvertime) {
		this.coefOvertime = coefOvertime;
	}

	public double getCoefVechile() {
		return this.coefVechile;
	}
	
	public void setCoefVechile(double coefVechile) {
		this.coefVechile = coefVechile;
	}
	
	public boolean getFlagTotal() {
		return this.flagTotal;
	}
	
	public void setFlagTotal(boolean flagTotal) {
		this.flagTotal = flagTotal;
	}

	public double getCoefService() {
		return this.coefService;
	}
	
	public void setCoefService(double coefService) {
		this.coefService = coefService;
	}

	public double getCoefTransportation() {
		return this.coefTransportation;
	}
	
	public void setCoefTransportation(double coefTransportation) {
		this.coefTransportation = coefTransportation;
	}

	public double getCoefRho() {
		return this.coefRho;
	}
	
	public void setCoefRho(double coefRho) {
		this.coefRho = coefRho;
	}
	
	public double getShape() {
		return this.shape;
	}
	
	public void setShape(double shape) {
		this.shape = shape;
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
}
