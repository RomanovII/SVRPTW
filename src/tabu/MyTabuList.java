package tabu;

import org.coinor.opents.*;

import svrptw.Instance;

/**
 * @author   Ilya
 */
@SuppressWarnings("serial")
public class MyTabuList extends ComplexTabuList implements TabuSearchListener{
	/**
	 * @uml.property  name="counter"
	 */
	private int counter;
	/**
	 * @uml.property  name="reset"
	 */
	private int reset=7;
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Instance instance = Instance.getInstance();

	public MyTabuList(int tenure, int[] attrDim) {
		super(tenure, attrDim.length);
	}

	public void tabuSearchStarted(TabuSearchEvent arg0) {}

	public void tabuSearchStopped(TabuSearchEvent arg0) {}

	public void newBestSolutionFound(TabuSearchEvent arg0) {
		counter--;
		setTenure(getTenure()-2);
		if (getTenure()<1)
			setTenure(reset);

	}

	public void newCurrentSolutionFound(TabuSearchEvent arg0) {}

	public void unimprovingMoveMade(TabuSearchEvent arg0) {
		counter++;
		if (counter==20){
			setTenure(getTenure()+3);
			if (getTenure()>instance.getCustomersNr()*2)
				setTenure(reset);
			counter=0;
		}
	}

	public void improvingMoveMade(TabuSearchEvent arg0) {}

	public void noChangeInValueMoveMade(TabuSearchEvent arg0) {}
}
