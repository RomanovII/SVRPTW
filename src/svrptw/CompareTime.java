package svrptw;

import java.util.Comparator;

/**
 * @author   Ilya
 */
public class CompareTime implements Comparator<Customer> {
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Instance instance = Instance.getInstance();
	/**
	 * @uml.property  name="from"
	 */
	private int from;
	
	public CompareTime(int from) {
		this.from = from;
	}
	
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
