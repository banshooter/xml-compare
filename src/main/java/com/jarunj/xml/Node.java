package com.jarunj.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by banshooter on 12/14/17.
 */
public class Node {
    public static Map<String, String> EmptyAttributes() {
        return new TreeMap<String, String>();
    }

    private String tagName;
    private String value;
    private Map<String, String> attributes = EmptyAttributes();
    private Map<Node, Integer> nodes = new HashMap<Node, Integer>();

    public Node() {
    }

    public Node(String tagName) {
        this.tagName = tagName;
    }
    public Node(String tagName, Map<String, String> attributes) {
        this.tagName = tagName;
        this.attributes = attributes;
    }
    public Node(String tagName, Map<String, String> attributes, String value) {
        this.tagName = tagName;
        this.attributes = attributes;
        this.value = value.trim();
    }
    public Node(String tagName, Map<String, String> attributes, Map<Node, Integer> nodes) {
        this.tagName = tagName;
        this.attributes = attributes;
        this.nodes = nodes;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Node addAttribute(String name, String value) {
        this.attributes.put(name, value);
        return this;
    }

    public Map<Node, Integer> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Node, Integer> nodes) {
        this.nodes = nodes;
    }

    public Node addNode(Node n) {
        if (nodes.containsKey(n)) {
            nodes.put(n, nodes.get(n) + 1);
        } else {
            nodes.put(n, 1);
        }
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (!stringEquals(getTagName(), node.getTagName())) return false;
        if (!stringEquals(getValue(), node.getValue())) return false;
        if (!getAttributes().equals(node.getAttributes())) return false;
        return getNodes().equals(node.getNodes());
    }

    private boolean stringEquals(String a, String b) {
        if (a == b) return true;
        if ((a == null && b != null) || (a != null && b == null)) return false;
        return a.equals(b);
    }

    @Override
    public int hashCode() {
        int result = getTagName()==null?0:getTagName().hashCode();
        result = 31 * result + (getValue()==null?0:getValue().hashCode());
        result = 31 * result + getAttributes().hashCode();
        result = 31 * result + getNodes().hashCode();
        return result;
    }
}
