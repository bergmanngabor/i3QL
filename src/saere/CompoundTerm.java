/* License (BSD Style License):
 * Copyright (c) 2010
 * Department of Computer Science
 * Technische Universität Darmstadt
 * All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  - Neither the name of the Software Technology Group or Technische 
 *    Universität Darmstadt nor the names of its contributors may be used to 
 *    endorse or promote products derived from this software without specific 
 *    prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package saere;

/**
 * A compound term is a term with a functor and at least one argument.
 * 
 * @author Michael Eichberg (mail@michael-eichberg.de)
 */
public abstract class CompoundTerm extends Term {

	/**
	 * @return <code>false</code>; always.
	 */
	@Override
	public final boolean isAtomic() {
		return false;
	}

	/**
	 * @return <code>true</code>; always.
	 */
	@Override
	public final boolean isCompoundTerm() {
		return true;
	}

	public abstract Term firstArg();

	public Term secondArg() {
		throw new IndexOutOfBoundsException();
	}

	/**
	 * @return Always returns "<code>this</code>"
	 */
	@Override
	public final CompoundTerm asCompoundTerm() {
		return this;
	}

	@Override
	public final int termTypeID() {
		return Term.COMPUND_TERM_TYPE_ID;
	}

	/**
	 * Returns <code>true</code> if this term is ground; i.e. if all arguments are ground.
	 * 
	 * <p>
	 * <b>Performance Guideline</b><br/>
	 * This method (re)calculates the answer whenever this method is called. If you know that for
	 * your specific kind of compound term the answer is always <code>true</code> you should
	 * override this method and return <code>true</code> (see also {@link #manifestState()}).
	 * </p>
	 */
	@Override
	public boolean isGround() {
		final int arity = arity();
		for (int i = 0; i < arity; i++) {
			if (!arg(i).isGround()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * The state of the compound term's arguments is saved for later recovery.
	 * <p>
	 * <b>Performance Guideline</b><br/>
	 * In general, state manifestation requires a traversal of the complete compound term's
	 * structure. Hence, if all instances of a compound term are always ground then it is highly
	 * recommended to override the corresponding {@link #isGround()} method and to always return
	 * <code>true</code>. Overwriting {@link #isGround()} is more beneficial than overwriting this
	 * method, because this method directly uses the method {@link #isGround()} and returns
	 * <code>null</code> if this compound term is ground.
	 * </p>
	 */
	@Override
	public State manifestState() {
		// TODO reevaluate if the "isGround" test is still meaningful if we have completed the compiler support for variable dereferencing
		if (isGround()) {
			return null;
		} 

		CompoundTerm complexTerm = this;

		// Compared to the recursive implementation,
		// this implementation is ~5-10% faster (overall!).
		CompoundTermsList workList = null;
		StatesList statesList = null;

		do {
			final int arity = complexTerm.arity();
			/* for_each_argument: */for (int i = 0; i < arity; i++) {
				// We needed to integrate the state handling of
				// variables here, to avoid stack overflow errors.
				// E.g., if a complex term is bound to a variable
				// the manifestation of the variable would lead to
				// another call of the manifest method. If we now
				// manifest a large datastructure (e.g., a long list)
				// which contains a large number of (bound) variables
				// this easily leads to a stack overflow error.
				Term arg_i = complexTerm.arg(i).dereference();
				switch (arg_i.termTypeID()) {
				case Term.VARIABLE_TYPE_ID:
					Variable variable = arg_i.asVariable();
					Term value = variable.getValue();
					if (value == null) {
						statesList = new StatesList(variable,statesList);
					}
					break;

				case Term.COMPUND_TERM_TYPE_ID:
					if (workList == null)
						workList = new CompoundTermsList(arg_i.asCompoundTerm());
					else
						workList = workList.prepend(arg_i.asCompoundTerm());
					break;
				}
			}
			if (workList != null) {
				complexTerm = workList.first();
				workList = workList.rest();
			} else {
				complexTerm = null;
			}
		} while (complexTerm != null);

		return statesList;
	}

	/**
	 * Unifies this compound term with another compound term.
	 * <p>
	 * This method does not take care of state handling; i.e, <font color="ref">both compound terms
	 * may be partially bound after return.</font> The caller must take care to reset the state of
	 * this compound term as well as the passed in compound term.
	 * </p>
	 */
	public boolean unify(CompoundTerm other) {
		final int arity = arity();
		if (arity == other.arity() && functor().sameAs(other.functor())) {
			int i = 0;
			while (i < arity) {
				if (this.arg(i).unify(other.arg(i))) {
					i += 1;
				} else {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject instanceof CompoundTerm) {
			CompoundTerm other = (CompoundTerm) otherObject;
			int arity = arity();
			if (arity == other.arity() && functor().sameAs(other.functor())) {
				for (int i = 0; i < arity; i++) {
					if (!this.arg(i).equals(other.arg(i)))
						return false;
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return functor().hashCode() + arity();
	}

	@Override
	public final Term dereference() {
		return this;
	}

	@Override
	public String toProlog() {
		// a compound term always has at least one argument
		String s = arg(0).toProlog();
		for (int i = 1; i < arity(); i++) {
			if (i > 0)
				s += ", ";
			s += arg(i).toProlog();
		}
		return functor().toProlog() + "(" + s + ")";
	}
}
