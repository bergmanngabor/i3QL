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
package idb.typing

import scala.collection.mutable

import org.junit.Test
import idb.{BagExtent, Extent}

/**
 *
 * @author Ralf Mitschke
 */
class TestVariances
{

    abstract class Person
    {
        def firstName: String

        def lastName: String
    }


    case class Student (firstName: String, lastName: String, matriculationNumber: String)

    case class Employee (firstName: String, lastName: String, salary: Int)

    @Test
    def testVarianceInScala ()
    {
        val s1: mutable.Set[Student] = mutable.Set.empty
        val s2: mutable.Set[Employee] = mutable.Set.empty

        // val sP1: Set[Person] = s1 // should not compile
        // val sP2: Set[Person] = s2 // should not compile

        //val sally = Student ("Sally", "Fields", "123456")

        //s1.add (sally)
        //assertEquals (mutable.Set (sally), sP1)
    }

    @Test
    def testVarianceForIDB ()
    {
        val e1: Extent[Student] = BagExtent()
        val e2: Extent[Employee] = BagExtent()

        // val sP1: Set[Person] = s1 // should not compile
        // val sP2: Set[Person] = s2 // should not compile

        //val sally = Student ("Sally", "Fields", "123456")

        //s1.add (sally)
        //assertEquals (mutable.Set (sally), sP1)
    }

}