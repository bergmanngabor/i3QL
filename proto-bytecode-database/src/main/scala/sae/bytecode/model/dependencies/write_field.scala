package sae.bytecode.model.dependencies

import sae.bytecode.model.{Field, Method}

/**
 * 
 * Author: Ralf Mitschke
 * Created: 22.05.11 13:56
 *
 */

class write_field (val source: Method, val target: Field)
        extends Dependency[Method, Field] {

}