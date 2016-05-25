package tabu;

import org.coinor.opents.AspirationCriteria;
import org.coinor.opents.Move;
import org.coinor.opents.SingleThreadedTabuSearch;
import org.coinor.opents.Solution;
import org.coinor.opents.TabuSearch;

@SuppressWarnings("serial")
public class MyAspirationCriteria implements AspirationCriteria
{
	
    public boolean overrideTabu( 
            final Solution solution, 
            final Move proposedMove, 
            final double[] proposedValue, 
            final TabuSearch tabuSearch )
        {
    		MySolution sol = (MySolution) solution;
    		boolean best = SingleThreadedTabuSearch.isFirstBetterThanSecond( 
                    proposedValue, 
                    tabuSearch.getBestSolution().getObjectiveValue(),
                    tabuSearch.isMaximizing() )
                ? true
                : false;
    		boolean feasible = sol.isFeasible();
    		return (feasible && best);
        }
	
}
