/* License (BSD Style License):
 *  Copyright (c) 2009, 2011
 *  Software Technology Group
 *  Department of Computer Science
 *  Technische Universität Darmstadt
 *  All rights reserved.
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
package sae.operators.impl

import sae.deltas.{Deletion, Addition, Update}
import sae.{Observable, Observer, Relation}
import sae.operators.Projection

/**
 *
 *
 *
 * The bag projection has the usual SQL meaning of a projection
 * The projection is always self maintained and requires no additional data apart from the provided delta.
 *
 * @author Ralf Mitschke
 *
 */
class ProjectionSetRetainingView[Domain, Range](val relation: Relation[Domain],
                                                val projection: Domain => Range)
    extends Projection[Domain, Range]
    with Observer[Domain]
{
    relation addObserver this

    override protected def childObservers(o: Observable[_]): Seq[Observer[_]] = {
        if (o == relation) {
            return List (this)
        }
        Nil
    }

    /**
     * Applies f to all elements of the view.
     */
    def foreach[T](f: (Range) => T) {
        relation.foreach ((v: Domain) => f (projection (v)))
    }

    @deprecated
    def updated(oldV: Domain, newV: Domain) {
        element_updated (projection (oldV), projection (newV))
    }

    def removed(v: Domain) {
        element_removed (projection (v))
    }

    def added(v: Domain) {
        element_added (projection (v))
    }

    def updated[U <: Domain](update: Update[U]) {
        if (update.affects (projection)) {
            element_updated (Update (projection (update.oldV), projection (update.newV), update.count, update.project (projection)))
        }
    }

    def modified[U <: Domain](additions: Set[Addition[U]], deletions: Set[Deletion[U]], updates: Set[Update[U]]) {
        {
            val nextAdditions = additions.map (e => Addition (projection (e.value), 1))

            val nextDeletions = deletions.map (e => Deletion (projection (e.value), 1))

            val nextUpdates = updates.map (e => Update (projection (e.oldV), projection (e.newV), 1, e.project (projection)))

            element_modifications (nextAdditions, nextDeletions, nextUpdates)
        }
    }

}
