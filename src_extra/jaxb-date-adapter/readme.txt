This class enables JAXB generated code to use the java.util.Date or java.util.Calendar types.

Add the following to your schema or custom binding file globalBindings section
(adjust namespace prefixes if necessary):

<jaxb:javaType name="java.util.Date"
               xmlType="xsd:date"
               parseMethod="com.blogspot.ozgwei.jaxb.DateConverter.parseDate"
               printMethod="com.blogspot.ozgwei.jaxb.DateConverter.printDate"/>

<jaxb:javaType name="java.util.Date"
               xmlType="xsd:time"
               parseMethod="com.blogspot.ozgwei.jaxb.DateConverter.parseDate"
               printMethod="com.blogspot.ozgwei.jaxb.DateConverter.printDate"/>

<jaxb:javaType name="java.util.Date"
               xmlType="xsd:dateTime"
               parseMethod="com.blogspot.ozgwei.jaxb.DateConverter.parseDate"
               printMethod="com.blogspot.ozgwei.jaxb.DateConverter.printDate"/>


The support for java.util.Calendar is built-in, but it's done the same way:

<jaxb:javaType name="java.util.Calendar"
               xmlType="xsd:date"
               parseMethod="javax.xml.bind.DatatypeConverter.parseDate"
               printMethod="javax.xml.bind.DatatypeConverter.printDate"/>

<jaxb:javaType name="java.util.Calendar"
               xmlType="xsd:time"
               parseMethod="javax.xml.bind.DatatypeConverter.parseDate"
               printMethod="javax.xml.bind.DatatypeConverter.printDate"/>

<jaxb:javaType name="java.util.Calendar"
               xmlType="xsd:dateTime"
               parseMethod="javax.xml.bind.DatatypeConverter.parseDate"
               printMethod="javax.xml.bind.DatatypeConverter.printDate"/>



More information available at:
http://ozgwei.blogspot.com/2007/08/generating-developer-friendly-codes.html

And also at:
https://jaxb.dev.java.net/guide/Using_different_datatypes.html
