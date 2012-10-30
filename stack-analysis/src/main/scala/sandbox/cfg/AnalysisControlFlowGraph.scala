package sandbox.cfg

import sae.bytecode.instructions.InstructionInfo
import sae.Relation

/**
 * This class implements the control flow graph of a program. WILL BE CHANGED!
 *
 * Created with IntelliJ IDEA.
 * User: Mirko
 * Date: 24.10.12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
case class AnalysisControlFlowGraph(methodInstruction: Relation[InstructionInfo]) {

  private val instList = res

  def getStartSequenceNumber = 0

  def getNextInstruction(seq: Int): InstructionInfo = {
    if (seq + 1 < instList.length)
      instList(seq + 1)
    else
      null
  }

  def getInstruction(seq: Int): InstructionInfo =
    if (seq >= 0 && seq < instList.length)
      instList(seq)
    else
      null

  def getPreviousInstruction(seq: Int): InstructionInfo = {
    if (seq > 0)
      instList(seq - 1)
    else
      null
  }


}
