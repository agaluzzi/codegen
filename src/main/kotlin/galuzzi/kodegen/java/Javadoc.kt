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
import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.CodeGenScope
import java.util.*

/**
 * A Javadoc comment for a code element.
 */
@CodeGenScope
class Javadoc : CodeElement
{
    private val description = StringBuilder()
    private val tags = ArrayList<Tag>()
    private var params: List<Param> = emptyList()

    operator fun String.unaryPlus()
    {
        description.append(this)
    }

    fun pad()
    {
        if (description.isBlank())
        {
            return
        }
        if (!description.endsWith('\n'))
        {
            +"\n"
        }
        if (!description.endsWith(PARAGRAPH_SEPARATOR))
        {
            +PARAGRAPH_SEPARATOR
        }
    }

    fun pre(text: String)
    {
        +"<pre>\n$text\n</pre>\n"
    }

    fun code(text: String)
    {
        +"<code>$text</code>"
    }

    fun list(items: Iterable<String>, ordered: Boolean = false)
    {
        val tag = if (ordered) "ol" else "ul"
        +"<$tag>\n"
        items.forEach { +"  <li>$it</li>\n" }
        +"</$tag>\n"
    }

    fun list(vararg items: String, ordered: Boolean = false)
    {
        list(items.asIterable(), ordered)
    }

    fun link(name: TypeName)
    {
        +"{@link ${name.fqcn}}"
    }

    fun author(value: String)
    {
        tags += Tag("author", value, 1)
    }

    fun version(value: String)
    {
        tags += Tag("version", value, 2)
    }

    fun param(name: String, description: String)
    {
        tags += Tag("param", "$name $description", 3)
    }

    fun returns(description: String)
    {
        tags += Tag("return", description, 4)
    }

    fun throws(type: String, description: String)
    {
        tags += Tag("throws", "$type $description", 5)
    }

    fun see(value: String)
    {
        tags += Tag("see", value, 6)
    }

    fun since(value: String)
    {
        tags += Tag("since", value, 7)
    }

    fun deprecated(description: String)
    {
        tags += Tag("deprecated", description, 9)
    }

    fun tag(name: String, value: String)
    {
        tags += Tag(name, value, 10)
    }

    fun isEmpty(): Boolean = (description.isBlank() && tags.isEmpty())

    override fun build(): CodeGen
    {
        val descr = description.toString()
        val haveDescription = descr.isNotBlank()
        val haveTags = tags.isNotEmpty() || params.isNotEmpty()

        if (!haveDescription && !haveTags)
        {
            return {}
        }

        return {
            pad()
            +"/**\n"
            if (haveDescription)
            {
                descr.split('\n').forEach { +" * $it\n" }
                if (haveTags) +" *\n"
            }
            tags.sorted().forEach { +" * @${it.name} ${it.value}\n" }
            +" */\n"
        }
    }
}

private data class Tag(val name: String,
                       val value: String,
                       val order: Int) : Comparable<Tag>
{
    override fun compareTo(other: Tag): Int
    {
        return order.compareTo(other.order)
    }
}

private const val PARAGRAPH_SEPARATOR = "<p>\n"
