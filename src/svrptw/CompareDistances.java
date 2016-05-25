package svrptw;

import java.util.Comparator;

public class CompareDistances implements Comparator<Customer> {

	@Override
	public int compare(Customer o1, Customer o2)
	{
		Double d1 = o1.getDistanceFromDepot();
		Double d2 = o2.getDistanceFromDepot();
		if(d1.compareTo(d2) < 0)
			return 1;
		if(d1.compareTo(d2) > 0)
			return -1;
		return 0;			
	}
}
