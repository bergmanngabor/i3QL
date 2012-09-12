package sae.bytecode

import instructions.InstructionInfo
import java.io.InputStream
import sae.collections.{BagResult, SetResult}
import sae.bytecode.structure._

/**
 *
 * Author: Ralf Mitschke
 * Author: Malte V
 * Created: 22.06.11 15:22
 *
 */

class MaterializedBytecodeDatabase(val database: BytecodeDatabase)
    extends BytecodeDatabase
{
    lazy val classDeclarations = new SetResult[ClassDeclaration](database.classDeclarations)

    lazy val instructions = new SetResult[InstructionInfo](database.instructions)

    lazy val methodDeclarations = new SetResult[MethodDeclaration](database.methodDeclarations)

    lazy val fieldDeclarations = new SetResult[FieldDeclaration](database.fieldDeclarations)

    lazy val classInheritance = new SetResult[InheritanceRelation](database.classInheritance)

    lazy val interfaceInheritance = new SetResult[InheritanceRelation](database.interfaceInheritance)

    lazy val codeAttributes = new SetResult[CodeAttribute](database.codeAttributes)

    lazy val exceptionHandlers = new SetResult[ExceptionHandler](database.exceptionHandlers)

    def fieldReadInstructions = null

    def inheritance = null

    def addClassFile(stream: InputStream) {
        database.addClassFile (stream)
    }

    def removeClassFile(stream: InputStream) {
        database.removeClassFile (stream)
    }

    def addArchive(stream: InputStream) {
        database.addArchive (stream)
    }

    def removeArchive(stream: InputStream) {
        database.removeArchive (stream)
    }


}