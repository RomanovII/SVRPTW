package tabu;

import org.coinor.opents.*;

import svrptw.Instance;

@SuppressWarnings("serial")
public class MyTabuList extends ComplexTabuList implements TabuSearchListener{
	private int counter;
	private int reset=7;
	private Instance instance;

	public MyTabuList(int tenure, int[] attrDim, Instance instance) {
		super(tenure, attrDim.length);
		this.instance= instance;
	}

	@Override
	public void tabuSearchStarted(TabuSearchEvent arg0) {}

	@Override
	public void tabuSearchStopped(TabuSearchEvent arg0) {}

	@Override
	public void newBestSolutionFound(TabuSearchEvent arg0) {
		counter--;
		setTenure(getTenure()-2);
		if (getTenure()<1)
			setTenure(reset);

	}

	@Override
	public void newCurrentSolutionFound(TabuSearchEvent arg0) {}

	@Override
	public void unimprovingMoveMade(TabuSearchEvent arg0) {
		counter++;
		if (counter==20){
			setTenure(getTenure()+3);
			if (getTenure()>instance.getCustomersNr()*2)
				setTenure(reset);
			counter=0;
		}
	}

	@Override
	public void improvingMoveMade(TabuSearchEvent arg0) {}

	@Override
	public void noChangeInValueMoveMade(TabuSearchEvent arg0) {}
}
