package svrptw;

import java.util.Comparator;

public class CompareTime implements Comparator<Customer> {
	private Instance instance = Instance.getInstance();
	private int from;
	
	public CompareTime(int from) {
		this.from = from;
	}
	
	@Override
	public int compare(Customer o1, Customer o2)
	{
		Double d1 = instance.getTime(from, o1.getNumber());
		Double d2 = instance.getTime(from, o2.getNumber());
		if(d1.compareTo(d2) > 0)
			return 1;
		if(d1.compareTo(d2) < 0)
			return -1;
		return 0;			
	}
}
