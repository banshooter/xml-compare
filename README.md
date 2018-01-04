# xml-compare: XML Compare

**xml-compare** is a Java library for comparing 2 XMLs with condition to ignore sequence of children nodes and sequence of attributes.


**xml-compare** implemented by customized lexer and parser for educational purpose.

xml-compare is mainly built for using in unit tests to comparing 2 xmls and also as an example of simple and stupid lexer and parser of XML.

## Example
add this in project's pom.xml:
&lt;dependency&gt;
&nbsp;&nbsp;&lt;groupId&gt;com.jarunj.xml&lt;/groupId&gt;
&nbsp;&nbsp;&lt;artifactId&gt;xml-compare&lt;/artifactId&gt;
&nbsp;&nbsp;&lt;version&gt;0.7.0&lt;/version&gt;
&lt;/dependency&gt;

```java
    import com.jarunj.xml.XML;
    .
    .
    .
    XML xml1 = new XML("<ns:Legacies><Legacy version=\"3.0.0\" year=10>a lot of them<Legacy><ns:Legacies>");
    XML xml2 = new XML("<ns:Legacies><Legacy year=10 version=\"3.0.0\">a lot of them<Legacy><ns:Legacies>");
    assert(xml1.equals(xml2)); // not throw Exception
```
