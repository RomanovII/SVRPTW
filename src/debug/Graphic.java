package debug;

import javax.swing.*;

import svrptw.Route;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author   Ilya
 */
@SuppressWarnings("serial")
public class Graphic extends JPanel{
	/**
	 * @uml.property  name="size"
	 */
	public int size;
	/**
	 * @uml.property  name="x"
	 */
	public int[] x = new int[101];
	/**
	 * @uml.property  name="y"
	 */
	public int[] y = new int[101];
	/**
	 * @uml.property  name="ans"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="svrptw.Route"
	 */
	ArrayList<Route> ans = new ArrayList<Route>();
	
	public static int[] convertIntegers(ArrayList<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
	
	@Override
	protected void paintComponent(Graphics gh) {
		Graphics2D drp = (Graphics2D) gh;
		ArrayList<Integer> xo = new ArrayList<Integer>();
		ArrayList<Integer> yo = new ArrayList<Integer>();
		for (int i = 0; i < ans.size(); ++i) {
			Route route = ans.get(i);
			xo.add(x[0]);
			yo.add(y[0]);
			for (int j = 0; j < route.getCustomersLength(); ++j) {
				int index = route.getCustomerNr(j);
				xo.add(x[index]);
				yo.add(y[index]);
			}
		}
		xo.add(x[0]);
		yo.add(y[0]);
		drp.setStroke(new BasicStroke(10, 1, 0));
		for (int i = 0; i < 101; ++i) {
			drp.setColor(new Color(0, 0, 255));
			drp.drawLine(x[i], y[i], x[i], y[i]);
			drp.setColor(new Color(255, 0, 0));
			drp.drawString(Integer.toString(i), x[i] + 1, y[i] - 5);
		}
		drp.setColor(new Color(0));
		drp.setStroke(new BasicStroke(1));
		drp.drawPolyline(convertIntegers(xo), convertIntegers(yo), yo.size());
	}
}