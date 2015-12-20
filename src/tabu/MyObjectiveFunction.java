package tabu;

import org.coinor.opents.*;

@SuppressWarnings("serial")
public class MyObjectiveFunction implements ObjectiveFunction {

	public MyObjectiveFunction() {
	}

	public double[] evaluate(Solution solution, Move proposedMove) {
		MySolution sol = (MySolution) solution;
		sol = (MySolution) sol.clone();
		if ( proposedMove == null ) {
			sol.evaluateAbsolutely();
		}
		else {
			proposedMove.operateOn( sol );
		}
		return sol.getObjectiveValue();
	}
	
} // end class MyObjectiveFunction