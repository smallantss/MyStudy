package com.example.mystudy.map;

public class HashMap {

    public static void main(String[] args){
        HashMap map = new HashMap();
        for (int i = 0; i < 50; i++) {
            System.out.println(i+"->"+myHash(i,16));
        }
        map.put(10,"10");
        map.put(5,"5");
        map.put(13,"13");
        map.put(26,"26");
        map.put(42,"42");
        map.put(10,"42");
        System.out.println(map);
        //26 42
        map.remove(0);
        System.out.println(map);
    }

    //核心数组
    private Node[] table;
    private int size;

    public HashMap() {
        //默认长度16
        table = new Node[16];
    }

    public void put(Object key, Object value) {
        //设置新节点
        Node newNode = new Node();
        newNode.hash = myHash(key.hashCode(),table.length);
        newNode.key = key;
        newNode.value = value;
        newNode.next = null;

        Node temp = table[newNode.hash];
        if (temp==null){
            //此次对应的没有数组
            table[newNode.hash] = newNode;
        }else {
            //此处已经有数组了，则遍历链表。
            Node node = temp;
            Node a = null;
            while (node!=null){
                if (newNode.key.equals(node.key)){
                    node.value = value;
                    return;
                }
                a = node;
                node = node.next;
            }
            a.next = newNode;

        }
    }

    public Object get(Object key){
        //获取到在map的第几行链表中
        int hash = myHash(key.hashCode(), table.length);
        Node node = table[hash];
        while (node!=null){
            if (node.key.equals(key)){
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public void remove(Object key){
        //获取到在map的第几行链表中
        int hash = myHash(key.hashCode(), table.length);
        Node node = table[hash];
        if (node==null){
            return;
        }
        //说明是该链表的第一个
        if (key.equals(node.key)){
            table[hash] = node.next;
        }else {
            Node pre = node;
            Node cur = node.next;
            Node next = cur.next;
            while (cur!=null){
                if (cur.key.equals(key)){
                    pre.next = next;
                    return;
                }
                pre = cur;
                cur = pre.next;
                next = cur.next;
            }
        }

    }

    public static int myHash(int v, int length) {
        //要求length必须为2的幂  位运算 效率高
//        System.out.println("hash in myHash:" + (v & (length - 1)));
        //直接对长度取余
//        System.out.println("hash in myHash2:" + v % (length - 1));
        return v & (length - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int length = table.length;
        for (int i = 0; i< length; i++){
            Node node = table[i];
            while (node!=null){
                sb.append(node.key+":"+node.value+",");
                node = node.next;
            }
        }
        sb.setCharAt(sb.length()-1,']');
        return sb.toString();
    }
}
