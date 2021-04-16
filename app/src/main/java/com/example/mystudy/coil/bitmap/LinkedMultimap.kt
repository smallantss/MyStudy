package com.example.mystudy.coil.bitmap

/**
 * 这种数据结构
 * 10-----bitmap1->bitmap2->bitmap3
 * | prev
 * 20-----bitmap1->bitmap2->bitmap3
 * | next
 * 30-----bitmap1->bitmap2->bitmap3
 */
internal class LinkedMultimap<K, V> {
    private val head = LinkedEntry<K, V>(null)
    private val entries = HashMap<K, LinkedEntry<K, V>>()

    //10-----bitmap1->bitmap2->bitmap3 添加到末尾
    fun put(key: K, value: V) {
        val entry = entries.getOrPut(key) {
            LinkedEntry<K, V>(key).apply(::makeTail)
        }
        entry.add(value)
    }

    //移除最后一个bitmap
    @OptIn(ExperimentalStdlibApi::class)
    fun removeLast(key: K): V? {
        val entry = entries.getOrPut(key) {
            LinkedEntry(key)
        }
        makeHead(entry)
        return entry.removeLast()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun removeLast(): V? {
        var last = head.prev

        while (last != head) {
            val removed = last.removeLast()
            if (removed != null) {
                //只移除values里最后一个
                return removed
            } else {
                //移除最后一组数据
                removeEntry(last)
                entries.remove(last.key)
            }
            last = last.prev
        }

        return null
    }

    override fun toString() = buildString {
        append("LinkedMultimap( ")

        var current = head.next

        while (current != head) {
            append('{')
            append(current.key)
            append(':')
            append(current.size)
            append('}')

            current = current.next
            if (current != head) append(", ")
        }

        append(" )")
    }

    private fun makeHead(entry: LinkedEntry<K, V>) {
        removeEntry(entry)
        entry.prev = head
        entry.next = head.next
        insertEntry(entry)
    }

    private fun makeTail(entry: LinkedEntry<K, V>) {
        removeEntry(entry)
        entry.prev = head.prev
        entry.next = head
        insertEntry(entry)
    }

    private fun <K, V> insertEntry(entry: LinkedEntry<K, V>) {
        entry.next.prev = entry
        entry.prev.next = entry
    }

    private fun <K, V> removeEntry(entry: LinkedEntry<K, V>) {
        entry.prev.next = entry.next
        entry.next.prev = entry.prev
    }


    private class LinkedEntry<K, V>(val key: K?) {

        private var values: MutableList<V>? = null

        var prev: LinkedEntry<K, V> = this
        var next: LinkedEntry<K, V> = this

        val size: Int get() = values?.size ?: 0

        @ExperimentalStdlibApi
        fun removeLast(): V? = values?.removeLastOrNull()

        fun add(value: V) {
            (values ?: mutableListOf<V>().also { values = it }) += value
        }
    }
}