package debug;

import javax.swing.*;

import svrptw.MethodListener;
import svrptw.Route;
import svrptw.SVRPTW;
import heneticmethod.*;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author   Ilya
 */
@SuppressWarnings("serial")
public class RunTest extends JFrame  implements MethodListener {
	/**
	 * @uml.property  name="graph"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	Graphic graph;
	/**
	 * @uml.property  name="cost"
	 */
	TextField cost;
	/**
	 * @uml.property  name="size"
	 */
	int size = 101;
	/**
	 * @uml.property  name="x"
	 */
	public int[] x = new int[size];
	/**
	 * @uml.property  name="y"
	 */
	public int[] y = new int[size];
	/**
	 * @uml.property  name="m"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public Matrix m = new Matrix(size);
	/**
	 * @uml.property  name="distances"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	int distances[][];
	
	private void readFile(String filename) {
		try {
			Scanner in = new Scanner(new FileReader(System.getProperty("user.dir") + "\\input\\"/*"/home/ilya/workspace/SVRPTW/input/"*/ + filename));

			// skip useless lines
			in.nextLine(); // skip filename
			in.nextLine(); // skip empty line
			in.nextLine(); // skip vehicle line
			in.nextLine();
			m.maxCars = in.nextInt(); // Amount of vehicles

			// read D and Q
			m.MaxMoney = in.nextInt(); // Capacity of all the vehicles

			// skip useless lines (Labels and white lines)
			in.nextLine();
			in.nextLine();
			in.nextLine();
			in.nextLine();
			in.nextLine();

			// read customers data
			ArrayList<TimeWindow> time = new ArrayList<TimeWindow>();
			for (int index = 0; index < size && in.hasNextInt(); ++index) {
				int i = in.nextInt(); //number
				m.ENC[i] = i;
				x[i] = (int)in.nextDouble();
				y[i] = (int)in.nextDouble();
				m.amountOfMoney[i] = (int)in.nextDouble();
				TimeWindow tmw = new TimeWindow();
				tmw.StartWork = (int)in.nextDouble();
				tmw.EndWork = (int)in.nextDouble();
				time.add(tmw);
				m.serviceTime[i] = (int)in.nextDouble();
			}// end for customers
			in.close();
			calculateDistances();
			m.distanceCoeffs = distances.clone();
			m.timeCoeffs = distances.clone();
			m.setTimeWindows(time);
			
		} catch (FileNotFoundException e) {
			// File not found
			System.out.println("File not found!");
			System.exit(-1);
		}
	}
	
	private void calculateDistances() 
	{
		distances = new int[size][size];
		for (int i = 0; i  < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				distances[i][j] = (int)Math.sqrt(Math.pow(x[i] - x[j], 2)
							+ Math.pow(y[i] - y[j], 2));
				distances[j][i] = distances[i][j];
			}
		}
	}
	
	public void start(String filename, int size) {
		this.size = size;
		
		readFile(filename);
		
		for (int i = 0; i < size; ++i){
			x[i] = 8 * x[i] + 5;
			y[i] = 8 * y[i] + 5;
		}
		graph.x = x;
		graph.y = y;
		graph.size = size;
		
		SVRPTW svrptw = new SVRPTW(m, this);
		svrptw.start();
		
	}

	public RunTest() {
		super("Romanov - The Best");
		JPanel jcp = new JPanel(new BorderLayout());
		setContentPane(jcp);
		
		graph = new Graphic();
		cost = new TextField();
		
		jcp.add(graph, BorderLayout.CENTER);
		jcp.add(cost, BorderLayout.PAGE_START);
		
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void newBestSolutionFound(ArrayList<Route> routes, String info) {
		// TODO Auto-generated method stub
		graph.ans = routes;
		cost.setText(info);
		this.repaint();
	}

	public void newCurrentSolutionFound(ArrayList<Route> routes, String info) {
		// TODO Auto-generated method stub
		graph.ans = routes;
		cost.setText(info);
		this.repaint();
	}

	public static void main(String[] args) throws InterruptedException {
		String filename = "C109.txt";
		Integer count = 101;
		
		RunTest runtest = new RunTest();
		runtest.setVisible(true);
		runtest.start(filename, count);
	}
}