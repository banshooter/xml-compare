package com.benqian.xml;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by benqian on 12/14/17.
 */
public class Node {
    public static Map<String, String> EmptyAttributes() {
        return new TreeMap<String, String>();
    }

    private String tagName;
    private Map<String, String> attributes = EmptyAttributes();
    private Map<Node, Integer> nodes = new TreeMap<Node, Integer>();

    public Node() {
    }

    public Node(String tagName) {
        this.tagName = tagName;
    }
    public Node(String tagName, Map<String, String> attributes) {
        this.tagName = tagName;
        this.attributes = attributes;
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

    public Map<Node, Integer> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Node, Integer> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        if (!getTagName().equals(node.getTagName())) return false;
        if (!getAttributes().equals(node.getAttributes())) return false;
        return getNodes().equals(node.getNodes());
    }

    @Override
    public int hashCode() {
        int result = getTagName().hashCode();
        result = 31 * result + getAttributes().hashCode();
        result = 31 * result + getNodes().hashCode();
        return result;
    }
}
