# Contract First Web Services Guide

A simple guide on contract first web services using a trivial "Echo" web service example.

## Write the contract

We need to express the service methods and related types with a WSDL and a number of XSD files.

The following XSD file defines a complex type with one string property and exports two named types that will be the input and output of the service.

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <xsd:schema version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
      xmlns:tns="http://www.dkateros.gr/ws/types" targetNamespace="http://www.dkateros.gr/ws/types"
      elementFormDefault="qualified">

      <xsd:element name="EchoInput" type="tns:SimpleMessage" />
      <xsd:element name="EchoOutput" type="tns:SimpleMessage" />

      <xsd:complexType name="SimpleMessage">
        <xsd:sequence>
          <xsd:element name="payload" type="xsd:string" minOccurs="1">
            <xsd:annotation>
              <xsd:documentation>
                Message content.
              </xsd:documentation>
            </xsd:annotation>
          </xsd:element>
        </xsd:sequence>
      </xsd:complexType>
    </xsd:schema>

The following WSDL file imports the types from the XSD and defines the service contract. Note that we could have added an Error message as well. This would show up as a checked Exception in the service implementation code. The service defines an `echo` operation that accepts an argument of type `EchoInput`

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>

    <wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
      xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" 
      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
      xmlns:tps="http://www.dkateros.gr/ws/types" 
      xmlns:tns="http://www.dkateros.gr/ws"
      targetNamespace="http://www.dkateros.gr/ws">

      <wsdl:types>
        <xsd:schema>
          <xsd:import namespace="http://www.dkateros.gr/ws/types" schemaLocation="types.xsd" />
        </xsd:schema>
      </wsdl:types>

      <wsdl:message name="EchoRequest">
        <wsdl:part name="EchoInput" element="tps:EchoInput" />
      </wsdl:message>

      <wsdl:message name="EchoResponse">
        <wsdl:part name="EchoOutput" element="tps:EchoOutput" />
      </wsdl:message>

      <wsdl:portType name="EchoPortType">
        <wsdl:operation name="Echo">
          <wsdl:input message="tns:EchoRequest" name="EchoRequest" />
          <wsdl:output message="tns:EchoResponse" name="EchoResponse" />
        </wsdl:operation>
      </wsdl:portType>

      <wsdl:binding name="EchoBinding" type="tns:EchoPortType">
        <soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document" />
        <wsdl:operation name="Echo">
          <soap:operation soapAction="Echo" />
          <wsdl:input name="EchoRequest">
            <soap:body use="literal" />
          </wsdl:input>
          <wsdl:output name="EchoResponse">
            <soap:body use="literal" />
          </wsdl:output>
        </wsdl:operation>
      </wsdl:binding>

      <wsdl:service name="Echo">
        <wsdl:documentation>
          Echo operation: Returns the input message.
        </wsdl:documentation>
        <wsdl:port binding="tns:EchoBinding" name="EchoPort">
          <soap:address location="REPLACE_WITH_ACTUAL_URL" />
        </wsdl:port>
      </wsdl:service>

    </wsdl:definitions>
