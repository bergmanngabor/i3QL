package unisson.model.dependencies


import de.tud.cs.st.vespucci.interfaces.ICodeElement
import unisson.model.kinds.primitive.ImplementsKind

/**
 *
 * Author: Ralf Mitschke
 * Created: 12.09.11 10:23
 *
 */
@deprecated
case class ImplementsDependency(source: ICodeElement, target: ICodeElement)
    extends Dependency
{
    def kind = ImplementsKind
}