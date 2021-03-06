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

@CodeGenScope
data class Param(val type: Type,
                 val name: String,
                 val allowNull: Boolean = true,
                 val description: String = "",
                 val varargs: Boolean = false) : Annotated by Annotated.Impl(),
                                                 CodeElement,
                                                 CodeEmbeddable
{
    override fun build(): CodeGen
    {
        return {
            +getAnnotations()
            +type
            if (varargs) +"..."
            +" $name"
        }
    }

    /**
     * Returns the name of the parameter.
     */
    override fun toString(): String
    {
        return name
    }
}