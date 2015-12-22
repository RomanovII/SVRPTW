package tabu;

import org.coinor.opents.*;

@SuppressWarnings("serial")
public class MyObjectiveFunction implements ObjectiveFunction {

	public MyObjectiveFunction() {
	}

	public double[] evaluate(Solution solution, Move proposedMove) {
		MySolution sol = (MySolution) solution;
		MyRelocateMove move = (MyRelocateMove) proposedMove;
		double[] objectiveValue;
		if ( proposedMove == null ) {
			sol.evaluateAbsolutely();
			objectiveValue = sol.getObjectiveValue();
		}
		else {
			move.operateOn( sol );
			objectiveValue = sol.getObjectiveValue();
			move.undoOperation( sol );
		}
		return new double[] { objectiveValue[0] };
	}
	
} // end class MyObjectiveFunction