package unisson.model.dependencies


import de.tud.cs.st.vespucci.interfaces.ICodeElement
import unisson.model.kinds.primitive.InvokeInterfaceKind

/**
 *
 * Author: Ralf Mitschke
 * Created: 12.09.11 10:04
 *
 */
@deprecated
case class InvokeInterfaceDependency(source: ICodeElement, target: ICodeElement)
        extends Dependency
{
    def kind = InvokeInterfaceKind
}