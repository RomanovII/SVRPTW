//package ru.bpc.cm.items.routing.heneticmethod;
package heneticmethod;

import java.util.ArrayList;

public class Matrix {
	public static final int NORMAL_ATM_WINDOW_MODE = 0;
	public static final int DEFAULT_ATM_WINDOW_MODE = 1;

	public static final int CONSTRAINTS_OK = 0;
	public static final int TIME_CONSTRAINT_VIOLATION = 1;
	public static final int CARS_OR_ATMS_CONSTRAINT_VIOLATION = 2;
	public static final int SUMM_CONSTRAINT_VIOLATION = 3;

	public int[] ENC;
	public String[] ATM;
	public int[][] distanceCoeffs;
	public int[][] timeCoeffs;
	private ArrayList<TimeWindow> timeWindows;
	private ArrayList<RiderTimeWindow> riderTimeWindows;
	public int[] amountOfMoney;
	public int[] serviceTime;
	public int MaxMoney;
	public int[] amountOfCassettes;
	public int VolumeOneCar;
	public double FixPrice;
	public double LengthPrice;
	public int MaxATMInWay;
	public int MaxTime;
	public int MaxLength;
	public String depot;
	public int maxCars;
	public double[] AtmPrice;
	public int currCode;
	public int windowMode = Matrix.NORMAL_ATM_WINDOW_MODE;
	public boolean useWindowsFlag;

	public int type;

	public int constraintsStatus = Matrix.CONSTRAINTS_OK;

	public Matrix(int size) {
		this.ENC = new int[size];
		this.ATM = new String[size];
		this.amountOfMoney = new int[size];
		this.amountOfMoney = new int[size];
		this.AtmPrice = new double[size];
		this.serviceTime = new int[size];
		this.amountOfCassettes = new int[size];
		this.distanceCoeffs = new int[size][size];
		this.timeCoeffs = new int[size][size];
		useWindowsFlag = false;
	}

	public int getServiceTime(int num) {
		return num == 0 ? 0 : 15;
	}

	public int getStartMinute(int num) {
		return riderTimeWindows.get(num).StartWork;
	}

	public void addRiderTimeWindow(int start, int end) {
		if (riderTimeWindows == null)
			riderTimeWindows = new ArrayList<RiderTimeWindow>();
		RiderTimeWindow rtmp = new RiderTimeWindow();
		rtmp.StartWork = start;
		rtmp.EndWork = end;
		riderTimeWindows.add(rtmp);
	}

	public void addTimeWindow(int numATM, int startWork, int endWork, boolean emergencyWindow) {
		if (timeWindows == null)
			timeWindows = new ArrayList<TimeWindow>();
		TimeWindow tmp = new TimeWindow();
		tmp.NumATM = numATM;
		tmp.StartWork = startWork;
		tmp.EndWork = endWork;
		tmp.emergencyWindow = emergencyWindow;
		timeWindows.add(tmp);
	}

	public void adjustEmergencyTimeWindows() {
		int emergencyWindowsCount = getEmergencyTimeWindowsCount();
		for (TimeWindow currentWindow : timeWindows) {
			if (currentWindow.emergencyWindow) {
				currentWindow.EndWork = currentWindow.StartWork + emergencyWindowsCount;
			}
		}
	}

	public int getEmergencyTimeWindowsCount() {
		int emergencyWindowsCount = 0;
		if (timeWindows != null) {

			for (TimeWindow currentWindow : timeWindows) {
				if (currentWindow.emergencyWindow) {
					emergencyWindowsCount++;
				}
			}
		}
		return emergencyWindowsCount;
	}

	public void removeAtmTimeWindows() {
		timeWindows.clear();
	}

	public void removeTimeWindowsForAtm(int numATM) {
		for (int i = 0; i <= timeWindows.size(); i++) {
			if (timeWindows.get(i).NumATM == numATM) {
				timeWindows.remove(i);
			}
		}
	}

	public boolean isInRiderWindow(int currTime, int timeWindowNum) {
		return currTime >= riderTimeWindows.get(timeWindowNum).StartWork
				&& currTime <= riderTimeWindows.get(timeWindowNum).EndWork;
	}

	public int geRiderWindowCount() {
		return riderTimeWindows.size();
	}

	public int getWayNumber() {
		return 1;
	}

	public ArrayList<RiderTimeWindow> getRiderTimeWindows() {
		return riderTimeWindows;
	}

	public TimeWindow getTimeWindow(int ATM) {
		return timeWindows.get(ATM);
	}

	public void setRiderTimeWindows(ArrayList<RiderTimeWindow> riderTimeWindows) {
		this.riderTimeWindows = riderTimeWindows;
	}

	public ArrayList<TimeWindow> getTimeWindows() {
		return timeWindows;
	}

	public void setTimeWindows(ArrayList<TimeWindow> timeWindows) {
		this.timeWindows = timeWindows;
	}

	public int isInWindow(int numAtm, int currTime) {
		Boolean flag = false;
		int i = 0;
		int result = 0;
		while (!flag && i < timeWindows.size()) {
			TimeWindow currentWindow = timeWindows.get(i);
			if (numAtm == currentWindow.NumATM) {
				if (currentWindow.StartWork < currTime && currentWindow.EndWork > (currTime + getServiceTime(numAtm))) {
					flag = true;
				} else {
					if (currentWindow.StartWork > currTime) {
						if (result == 0)
							result = currentWindow.StartWork - currTime;
						else {
							if (result > currentWindow.StartWork - currTime)
								result = currentWindow.StartWork - currTime;
						}
					}
				}
			}
			i++;
		}
		if (flag)
			return 0;
		else {
			return result > 0 ? result : -1;
		}
	}

}