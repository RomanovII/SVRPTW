package svrptw;

/**
 * @author   Ilya
 */
public class Parameters {
	/**
	 * @uml.property  name="precision"
	 */
	private double 	precision;
	/**
	 * @uml.property  name="coefDelay"
	 */
	private double coefDelay;
	/**
	 * @uml.property  name="coefEarliness"
	 */
	private double coefEarliness;
	/**
	 * @uml.property  name="coefDistance"
	 */
	private double coefDistance;
	/**
	 * @uml.property  name="coefOvertime"
	 */
	private double coefOvertime;
	/**
	 * @uml.property  name="coefVechile"
	 */
	private double coefVechile;
	/**
	 * @uml.property  name="flagTotal"
	 */
	private boolean flagTotal;
	/**
	 * @uml.property  name="coefService"
	 */
	private double coefService;
	/**
	 * @uml.property  name="coefTransportation"
	 */
	private double coefTransportation;
	/**
	 * @uml.property  name="coefRho"
	 */
	private double coefRho;
	/**
	 * @uml.property  name="coefPhi"
	 */
	private double coefPhi;
	/**
	 * @uml.property  name="shape"
	 */
	private double shape;
	/**
	 * @uml.property  name="scale"
	 */
	private double scale;
	/**
	 * @uml.property  name="tenure"
	 */
	private int tenure;
	/**
	 * @uml.property  name="iterations"
	 */
	private int iterations;
	
	public Parameters() {
		this.precision = 1E-2;
		this.coefDelay = 1;
		this.coefEarliness = 0.0;
		this.coefDistance = 1;
		this.coefOvertime = 0;
		this.coefVechile = 10;
		this.flagTotal = false;
		this.coefService = 1;
		this.coefTransportation = 1;
		this.coefRho = 0.7;
		this.coefPhi = 0.5;
		this.shape = 1;
		this.scale = 1;
		this.tenure = 10;
		this.iterations = 500;
	}

	/**
	 * @return
	 * @uml.property  name="coefDelay"
	 */
	public double getCoefDelay() {
		return this.coefDelay;
	}
	
	/**
	 * @param  coefDelay
	 * @uml.property  name="coefDelay"
	 */
	public void setCoefDelay(double coefDelay) {
		this.coefDelay = coefDelay;
	}

	/**
	 * @return
	 * @uml.property  name="coefEarliness"
	 */
	public double getCoefEarliness() {
		return this.coefEarliness;
	}
	
	/**
	 * @param  coefEarliness
	 * @uml.property  name="coefEarliness"
	 */
	public void setCoefEarliness(double coefEarliness) {
		this.coefEarliness = coefEarliness;
	}

	/**
	 * @return
	 * @uml.property  name="coefDistance"
	 */
	public double getCoefDistance() {
		return this.coefDistance;
	}
	
	/**
	 * @param  coefDistance
	 * @uml.property  name="coefDistance"
	 */
	public void setCoefDistance(double coefDistance) {
		this.coefDistance = coefDistance;
	}

	/**
	 * @return
	 * @uml.property  name="coefOvertime"
	 */
	public double getCoefOvertime() {
		return this.coefOvertime;
	}
	
	/**
	 * @param  coefOvertime
	 * @uml.property  name="coefOvertime"
	 */
	public void setCoefOvertime(double coefOvertime) {
		this.coefOvertime = coefOvertime;
	}

	/**
	 * @return
	 * @uml.property  name="coefVechile"
	 */
	public double getCoefVechile() {
		return this.coefVechile;
	}
	
	/**
	 * @param  coefVechile
	 * @uml.property  name="coefVechile"
	 */
	public void setCoefVechile(double coefVechile) {
		this.coefVechile = coefVechile;
	}
	
	public boolean getFlagTotal() {
		return this.flagTotal;
	}
	
	/**
	 * @param  flagTotal
	 * @uml.property  name="flagTotal"
	 */
	public void setFlagTotal(boolean flagTotal) {
		this.flagTotal = flagTotal;
	}

	/**
	 * @return
	 * @uml.property  name="coefService"
	 */
	public double getCoefService() {
		return this.coefService;
	}
	
	/**
	 * @param  coefService
	 * @uml.property  name="coefService"
	 */
	public void setCoefService(double coefService) {
		this.coefService = coefService;
	}

	/**
	 * @return
	 * @uml.property  name="coefTransportation"
	 */
	public double getCoefTransportation() {
		return this.coefTransportation;
	}
	
	/**
	 * @param  coefTransportation
	 * @uml.property  name="coefTransportation"
	 */
	public void setCoefTransportation(double coefTransportation) {
		this.coefTransportation = coefTransportation;
	}

	/**
	 * @return
	 * @uml.property  name="coefRho"
	 */
	public double getCoefRho() {
		return this.coefRho;
	}
	
	/**
	 * @param  coefRho
	 * @uml.property  name="coefRho"
	 */
	public void setCoefRho(double coefRho) {
		this.coefRho = coefRho;
	}
	
	/**
	 * @return
	 * @uml.property  name="coefPhi"
	 */
	public double getCoefPhi() {
		return this.coefPhi;
	}
	
	/**
	 * @param  coefPhi
	 * @uml.property  name="coefPhi"
	 */
	public void setCoefPhi(double coefPhi) {
		this.coefPhi = coefPhi;
	}
	
	/**
	 * @return
	 * @uml.property  name="shape"
	 */
	public double getShape() {
		return this.shape;
	}
	
	/**
	 * @param  shape
	 * @uml.property  name="shape"
	 */
	public void setShape(double shape) {
		this.shape = shape;
	}
	
	/**
	 * @return
	 * @uml.property  name="scale"
	 */
	public double getScale() {
		return this.scale;
	}
	
	/**
	 * @param  scale
	 * @uml.property  name="scale"
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	/**
	 * @return
	 * @uml.property  name="precision"
	 */
	public double getPrecision() {
		return this.precision;
	}
	
	/**
	 * @param  precision
	 * @uml.property  name="precision"
	 */
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	
	/**
	 * @return
	 * @uml.property  name="tenure"
	 */
	public int getTenure() {
		return this.tenure;
	}
	
	/**
	 * @param  tenure
	 * @uml.property  name="tenure"
	 */
	public void setTenure(int tenure) {
		this.tenure = tenure;
	}
	
	/**
	 * @return
	 * @uml.property  name="iterations"
	 */
	public int getIterations() {
		return this.iterations;
	}
	
	/**
	 * @param  iterations
	 * @uml.property  name="iterations"
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
}
