package com.example.mystudy.map;

public class Node {

    //存储的是该Node在HashMap中的位置，在0-（length-1）之间的某个链表上。
    public int hash;
    public Object key;
    public Object value;
    //单链表
    public Node next;
}
