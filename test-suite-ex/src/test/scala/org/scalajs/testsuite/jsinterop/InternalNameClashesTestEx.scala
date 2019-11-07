/*
 * Scala.js (https://www.scala-js.org/)
 *
 * Copyright EPFL.
 *
 * Licensed under Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package org.scalajs.testsuite.jsinterop

import org.junit.Test
import org.junit.Assert._

/** Tests for potential name clashes between user-written identifiers and
 *  compiler-generated ones.
 */
class InternalNameClashesTestEx {
  import InternalNameClashesTestEx._

  @Test def testLocalVariableClashWithEnvField(): Unit = {
    /* This tests that user-defined local variables cannot clash with
     * compiler-generated "envFields".
     */
    @noinline def someValue(): Int = 42

    val $c_Lorg_scalajs_testsuite_jsinterop_InternalNameClashesTestEx$LocalVariableClashWithEnvField = someValue()
    val foo = new LocalVariableClashWithEnvField(5)
    assertEquals(42, $c_Lorg_scalajs_testsuite_jsinterop_InternalNameClashesTestEx$LocalVariableClashWithEnvField)
    assertEquals(5, foo.x)
  }

  @Test def testLocalVariableClashWithExplicitThisParam(): Unit = {
    /* Default methods in interfaces receive an explicit this parameter, which
     * is encoded as `$thiz` by the emitter. Here we make sure that we can
     * write a user-defined `$thiz` local variable without clashing with the
     * compiler-generated one.
     */
    val foo = new LocalVariableClashWithExplicitThisParamTrait {
      val x: Int = 5
    }

    assertEquals(121, foo.test(6))
  }

}

object InternalNameClashesTestEx {
  @noinline
  class LocalVariableClashWithEnvField(val x: Int)

  trait LocalVariableClashWithExplicitThisParamTrait {
    val x: Int

    @noinline
    def test(y: Int): Int = {
      val $thiz = x + y
      $thiz * $thiz
    }
  }
}