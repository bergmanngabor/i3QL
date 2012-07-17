package unisson.query.code_model

import de.tud.cs.st.vespucci.interfaces.IFieldDeclaration
import sae.bytecode.model.FieldDeclaration

/**
 *
 * Author: Ralf Mitschke
 * Date: 12.12.11
 * Time: 12:48
 *
 */
class FieldDeclarationAdapter(val element: FieldDeclaration)
        extends IFieldDeclaration with SourceElement[FieldDeclaration]
{
    def getPackageIdentifier = element.declaringClass.packageName

    def getSimpleClassName = element.declaringClass.simpleName

    def getLineNumber = -1

    override def hashCode() = element.hashCode()

    override def equals(obj: Any): Boolean = {
        if (obj.isInstanceOf[FieldDeclarationAdapter]) {
            return element.equals(obj.asInstanceOf[FieldDeclarationAdapter].element)
        }
        if (obj.isInstanceOf[IFieldDeclaration]) {
            val other = obj.asInstanceOf[IFieldDeclaration]
            return this.getPackageIdentifier == other.getPackageIdentifier &&
            this.getSimpleClassName == other.getSimpleClassName &&
            this.getFieldName == other.getFieldName &&
            this.getTypeQualifier == other.getTypeQualifier
        }
        false
    }


    def getFieldName = element.name

    def getTypeQualifier = element.fieldType.signature

    override def toString = element.declaringClass.signature +
            element.name +
            ":" + element.fieldType.signature

    lazy val getSootIdentifier =
        "<" + element.declaringClass.toJava + ":" + element.fieldType.toJava + " " + element.name + ">"
}