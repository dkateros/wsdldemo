# Contract First Web Services Guide

A simple guide on contract first web services using a trivial "Echo" web service example.

## Write the contract (echo-ws)

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

The following WSDL file imports the types from the XSD and defines the service contract. Note that we could have added an Error message as well. This would show up as a checked Exception in the service implementation code. The service defines an `echo` operation that accepts an argument of type `EchoInput` and produces a result of type `EchoOutput`. Note the `REPLACE_WITH_ACTUAL_URL` value for the `location` attribute of the `soap:address` element. This will be replaced by the servlet container automatically to the correct URL.

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

## Generate the java code (echo-gen) and implement the service (echo-ws)

Use the wsimport tool of your JDK to generate java sources from the WSDL and XSD files. Delete the service class, this is useful only when you are building a client. Instead, create a service implementation class that implements the defined port type interface.

Annotate the class with `@WebService`. The result should look like the code below. All annotation attributes are based on the WSDL. The wsdlLocation attribute should point to the resource path of the actual packaged wsdl file.

    @WebService(
                    serviceName="Echo",
                    portName="EchoPort",
                    targetNamespace="http://www.dkateros.gr/ws",
                    endpointInterface="gr.dkateros.ws.EchoPortType",
                    wsdlLocation="gr/dkateros/ws/contract/echo.wsdl")
    public class Echo implements EchoPortType {
        //implementation ommited
    }

## Deploy as a servlet (echo-webapp)

Most servlet containers auto-detect @WebService annotated files and publish the web services as servlet. If this is not the case, you need to include the servlet declaration in the web.xml file. The servlet container should create a servlet that processes web service requests by delegating to the implementation class methods.

	<servlet>
		<display-name>Echo</display-name>
		<servlet-name>Echo</servlet-name>
		<servlet-class>gr.dkateros.ws.Echo</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>Echo</servlet-name>
		<url-pattern>/Echo</url-pattern>
	</servlet-mapping>

## Log the soap request and response (wsdl-utils)

Class SoapLogger is a servlet filter that intercepts

## Generate documentation from WSDL / XSD files (wsdl-utils, echo-ws)

It is possible to use an XSL transformation to convert the WSDL / XSD files to HTML documentation that leverages the `documentation` elements. Class `Wsdl2Html` loads the wsdl files in a specified directory, resolves the imported XSD files and uses an XSL file in order to transform these into HTML.

The pom.xml of module echo-ws runs this class as an ant task in order to produce the documentation as HTML files. A good practice is to bundle the documentation with the web app automatically, so that you can be sure that the documentation matches the deployed web services.

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-antrun-plugin</artifactId>
                <executions>
                <execution>
                        <id>install</id>
                        <phase>install</phase>
                        <configuration>
                            <target>
                                <taskdef name="wsdldoc" classname="gr.dkateros.ws.util.document.Wsdl2Html">
                                    <classpath id="cp">
                                        <path refid="maven.plugin.classpath" />
                                    </classpath>
                                </taskdef>

                                <wsdldoc sourceDir="${project.build.directory}"                                         targetDir="${project.build.directory}/wsdocs" />
                            </target>
                        </configuration>
                        <goals>
                            <goal>run</goal>
                        </goals>
                    </execution>
                </executions>

                    <dependencies>
                            <dependency>
                                    <groupId>gr.dkateros.demo</groupId>
                                    <artifactId>wsdl-utils</artifactId>
                                    <version>${project.version}</version>
                            </dependency>
                    </dependencies>
            </plugin>

        </plugins>
    </build>

## Integration test (wsdl-utils, echo-ws)

The `SoapWsClient` utility sends a SOAP payload to a specified web service URL and web service method. It uses the apache-http-client utility. Alternatively, if you do not want to write the SOAP request, one can create a client using the `wsimport` command.

## Useful patterns

Encapsulate request and response types to allow for extensibility
Extend input / output types with optional attributes placed as the last attributes of the type to preserve backwards compatibility
