package it.cnr.istc.psts.wikitell;



import it.cnr.istc.pst.oratio.Item.ArithItem;
import it.cnr.istc.pst.oratio.Solver;
import it.cnr.istc.pst.oratio.SolverException;

public class LessonManager {
	
	
	
	public static void pippo() throws SolverException, NoSuchFieldException {
		Solver s = new Solver();
		s.read("real x, y; x + y => 5.0");
		s.solve();

		System.out.println(((ArithItem) s.get("x")).getValue());
		System.out.println(((ArithItem) s.get("y")).getValue());

		
	}
	
	


}
