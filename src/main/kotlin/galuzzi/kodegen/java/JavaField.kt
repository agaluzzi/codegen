/*
 * Copyright 2018 Aaron Galuzzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package galuzzi.kodegen.java

import galuzzi.kodegen.CodeElement
import galuzzi.kodegen.CodeEmbeddable
import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.CodeGenScope
import galuzzi.kodegen.java.support.Annotated
import galuzzi.kodegen.java.support.Documented

/**
 * A single field of a class or an interface.
 */
@CodeGenScope
class JavaField internal constructor(val scope: Scope,
                                     val type: Type,
                                     val name: String,
                                     val description: String,
                                     val owner: Type) : CodeElement,
                                                                     Annotated by Annotated.Impl(),
                                                                     Documented by Documented.Impl(),
                                                                     CodeEmbeddable
{
    private val modifiers = Modifiers()
    var value: CodeGen? = null

    fun static()
    {
        modifiers += Modifier.STATIC
    }

    fun final()
    {
        modifiers += Modifier.FINAL
    }

    fun volatile()
    {
        modifiers += Modifier.VOLATILE
    }

    override fun build(): CodeGen
    {
        return {
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +modifiers
            +type
            +' '
            +name
            value?.apply {
                +" = "
                +this
            }
            +";\n"
        }
    }

    fun isPrimitive(): Boolean = type.isPrimitive()

    fun isStatic(): Boolean = modifiers.contains(Modifier.STATIC)

    fun isFinal(): Boolean = modifiers.contains(Modifier.FINAL)

    /**
     * @return a reference to this field, in the form: `this.{name}` for member fields or `{class}.{name}` for static fields
     */
    override fun toString(): String = if (isStatic()) "$owner.$name" else "this.$name"
}