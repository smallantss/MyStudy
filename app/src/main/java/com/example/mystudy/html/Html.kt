package com.example.mystudy.html

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface Element {

    //每个节点都需要实现render方法
    fun render(builder: StringBuilder, indent: String): String

}

/**
 * 每个节点都有name,content: <title>ABC</title>
 */
open class BaseElement(val name: String, val content: String = "") : Element {

    //每个节点都会有很多子节点
    val children = ArrayList<Element>()

    //拼接HTML: <title>ABC</title>
    override fun render(builder: StringBuilder, indent: String): String {
        builder.append("$indent<$name>\n")
        if (content.isNotBlank()) {
            builder.append("    $indent$content\n")
        }
        children.forEach {
            it.render(builder, "$indent  ")
        }
        builder.append("$indent</$name>\n")
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }

}

class Html : BaseElement("html") {

    fun body(block: Body.() -> Unit): Body {
        val body = Body()
        body.block()
        children += body
        return body
    }

    fun head(block: Head.() -> Unit): Head {
        val head = Head()
        head.block()
        children += head
        return head
    }
}

class Body : BaseElement("body") {

    fun h1(block: () -> String): H1 {
        val content = block()
        val h1 = H1(content)
        children += h1
        return h1
    }

    fun span(block: () -> String): Span {
        val span = Span(block())
        children += span
        return span
    }

}

class Head : BaseElement("head") {
    fun title(block: () -> String): Title {
        val content = block()
        val title = Title(content)
        children += title
        return title
    }
}

class P(content: String) : BaseElement("p", content)
class H1(content: String) : BaseElement("h1", content)
class Title(content: String) : BaseElement("title", content)
class Span(content: String) : BaseElement("span", content)

fun html(block: Html.() -> Unit): Html {
    val html = Html()
    html.block()
    return html
}

//fun p(block: () -> String): P {
//    val content = block()
//    val p = P(content)
//    return p
//}

class Test {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
//            println(
//                    html {
//                        head {
//                            title { "title" }
//                        }
//                        body {
//                            h1 { "aaa" }
//                            span { "span" }
//                        }
//                    }
//            )
            val s = "aaa\r\n"
            println(s.trim())
            println("---")
        }
    }
}