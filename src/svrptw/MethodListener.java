package svrptw;

import java.util.ArrayList;

public interface MethodListener extends java.io.Serializable, java.util.EventListener {

	public void newBestSolutionFound( ArrayList<Route> routes, String info );

    public void newCurrentSolutionFound( ArrayList<Route> routes, String info );
  
}
