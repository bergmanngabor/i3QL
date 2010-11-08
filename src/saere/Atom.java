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
 * Atoms are used to represent atomic informations.
 * 
 * @author Michael Eichberg
 */
public abstract class Atom extends Term {

	/**
	 * @return 0. By definition the arity of Atoms is always 0. 
	 */
	final public int arity() { return 0; }
	
	/**
	 * @param i <i>"ignored"</i>.
	 * @throws IndexOutOfBoundsException always. 
	 */
	final public Term arg(int i) {  
		throw new IndexOutOfBoundsException("Atoms have no arguments.");
	}

	/**
	 * @return <code>null</code>; an atom's state is immutable and, hence, no 
	 * state information need to be preserved.<br/> 
	 * In general, we try to avoid explicit manifestation of an Atom's state.
	 * This – i.e., avoiding useless calls to manifestState – however, requires 
	 * whole program analyses in general. 
	 */
	final public State manifestState(){ return null; } 

	/**
	 * Since an Atom's state is immutable, this method does nothing. 
	 * 
	 * @param state the value is <i>"ignored"</i>, but we actually test that
	 * 	the value is <code>null</code> to catch programming errors early on.
	 */
	public final void setState(State state) {
		assert (state == null); 
	}
}


