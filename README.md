# Jaxws web services demo

1. wsdl-utils utilities used elsewhere in this project
2. test-generated generated web service stubs for integration testing a java first service
3. generated generated web service stubs for implementing a contract first service
4. webservice service implementations and integration (e2e) tests
5. webapp a web application to deploy the services on a suitable runtime (more recently tested with openliberty)

## Contract First Web Services

A simple guide on contract first web services using a trivial "Echo" stateless web service example.

### Write the contract (webservice module)

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

### Generate the java code (generated module) and implement the service (webservice module)

Use the wsimport tool of your JDK to generate java sources from the WSDL and XSD files. Alternatively, use the ant scripts on wsdl-utils module.

Delete the service class, this is useful only when you are building a client. Instead, create a service implementation class that implements the defined port type interface.

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

### Deploy as a servlet (webapp module)

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

## Java First Web Services Guide

A simple guide on java first web services. Actually a stateful service this time around.

Start as simple as creating a class with a number of public methods and supplying the appropriate
annotations from the `javax.jws` package.

1. @WebService on the class level marking a wsdl service
2. @WebMethod on the method level marking a wsdl operation
3. @WebParam on the method arguments level marking an input type, if applicable

Deploy the service, most application servers will pick up the annotation meta-data and deploy it automatically.

### Consuming (or e2e testing) the service

Get the server generated wsdl by asking the server for it, for example `http://host:port/wsdldemo/CounterService?wsdl`
should get the server generated wsdl for the `gr.dkateros.ws.CounterImpl` service. Use `wsimport` (or the ant script in
wsdl-utils) to generate client stubs. Client stubs include a proxy factory that should be used to instantiate clients.

### Stateful services

Stateful service implementations normally preserve state on the HTTP session scope. JAXWS implementations leverage the
standard Servlet API for this. The web request response context can be injected like so:

	@Resource WebServiceContext ctx;

and used within the service operations implementation to retrieve and store information on the HTTP session.

	MessageContext mc = ctx.getMessageContext();
	HttpSession session = ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
	//session.getAttribute("someKey");
	//session.setAttribute("someKey", someValue);

If the web service conversational scope is other than that of the HttpSession (e.g. multiple 
conversations may start from the same HttpSession), it is a good idea to provide semantics 
for discarding state. This could be achieved, for instance, by providing a web service method 
that discards the existing state.

### Stateful service clients

On the client side one has to indicate to the client proxy that it is supposed to maintain the session
AND reuse the same proxy for all calls that belong in the same session.

Under the hood this causes the service to return a `Set-Cookie` HTTP header on the first request.
The proxy should then use to this header's value to set the `Cookie` HTTP header so that the server
can associate the request with the same HTTP session.

### Why bother with contract first?

WSDL services come from point when people were generally more concerned about their payloads, their type systems etc.
As a result, the spec is more strict and complicated than what we are used to nowadays. A result of this complexity
is that it is not uncommon that the same java definition can be translated as a wsdl definition in slightly different 
ways by different runtimes. Breaking your clients when changing runtimes is not fun and being contract first reduces
this kind of risk (note that I did not say it eliminates it).

## Extras

### Log the soap request and response (wsdl-utils module)

Class `SoapLogger` is a servlet filter that intercepts HttpServletRequest and HttpServletResponse objects in order to log the input / output SOAP messages. Define the filter in web.xml

	<filter>
		<filter-name>SoapLogger</filter-name>
		<filter-class>gr.dkateros.ws.util.log.SoapLogger</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>SoapLogger</filter-name>
		<url-pattern>/Echo</url-pattern>
	</filter-mapping>

### Generate documentation from WSDL / XSD files (wsdl-utils, webservice modules)

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

### Integration test (wsdl-utils, echo-ws)

The `SoapWsClient` utility sends a SOAP payload to a specified web service URL and web service method. It uses the apache-http-client utility. Alternatively, if you do not want to write the SOAP request, one can create a client using the `wsimport` command.

### Useful patterns

1. Encapsulate request and response types to allow for extensibility. Using an xsd:int as input for example does not allow for extend the service definition to accept additional input.
2. Extend input / output types with optional attributes placed as the last attributes of the type to preserve client backwards compatibility.
