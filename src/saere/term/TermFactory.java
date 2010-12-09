package saere.term;

import saere.Atom;
import saere.CompoundTerm;
import saere.FloatAtom;
import saere.IntegerAtom;
import saere.StringAtom;
import saere.Term;
import saere.Variable;

public class TermFactory {

	private static TermFactory instance = new TermFactory();

	public static TermFactory getInstance() {
		return instance;
	}

	private TermFactory() {
	}

	public Variable newVariable() {
		return new Variable();
	}

	public Variable newNamedVariable(String name) {
		// TODO support named variables
		return new Variable();
	}

	public Atom newAtom(long value) {
		return IntegerAtom.IntegerAtom(value);
	}

	public Atom newAtom(double value) {
		return FloatAtom.instance(value);
	}

	
	public Atom newAtom(String string) {
		return StringAtom.instance(string);
	}

	private CompoundTerm newCompoundTerm(String functor, Term[] args) {
	
		assert(args.length > 0);
		
		// arithmetic expression require special support
		switch (args.length) {
		case 1:
			// Terms with one argument
			switch (functor.charAt(0)) {
			case '-' :
				return new Minus1(args[0]);
			default:
				return new GenericCompoundTerm(StringAtom.instance(functor), args);
			}
		case 2:
			// Terms with two arguments
			switch (functor.charAt(0)) {
			
			// control-flow related terms
			case ',' :
				return new And2(args[0],args[1]);
			case ';' :
				return new Or2(args[0],args[1]);

			// arithmetic terms
			case '-' :
				return new Minus2(args[0],args[1]);
			case '+' :
				return new Plus2(args[0],args[1]);
				
			// other commonly used terms
			case '.' :
				return new ListElement2(args[0],args[1]);
			default:
				return new GenericCompoundTerm(StringAtom.instance(functor), args);
			}

		default:
			return new GenericCompoundTerm(StringAtom.instance(functor), args);
		}
	}

	// The static methods are just convenience methods that always use
	// the default instance of the TermFactory.

	public static Term atom(String value) {
		return getInstance().newAtom(value);
	}

	public static Term atom(long value) {
		return getInstance().newAtom(value);
	}

	public static Term atom(double value) {
		return getInstance().newAtom(value);
	}

	
	public static Variable variable() {
		return getInstance().newVariable();
	}

	public static Variable namedVariable(String name) {
		return getInstance().newNamedVariable(name);
	}

	public static CompoundTerm compoundTerm(String functor, Term... args) {
		return getInstance().newCompoundTerm(functor, args);
	}

	public static Atom cut() {
		return getInstance().newAtom("!");
	}
	
	public static CompoundTerm and(Term t1, Term t2) {
		return getInstance().newCompoundTerm(",", new Term[]{t1,t2});
	}
	
	public static CompoundTerm or(Term t1, Term t2) {
		return getInstance().newCompoundTerm(";", new Term[]{t1,t2});
	}

}