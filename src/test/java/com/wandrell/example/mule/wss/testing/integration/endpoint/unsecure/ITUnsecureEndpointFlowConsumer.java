/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.example.mule.wss.testing.integration.endpoint.unsecure;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wandrell.example.mule.wss.testing.util.config.context.EndpointContextPaths;
import com.wandrell.example.mule.wss.testing.util.test.AbstractITEndpointFlow;

/**
 * Implementation of {@code AbstractITEndpointFlow} for the unsecure consumer
 * endpoint flow.
 * <p>
 * It adds the following cases:
 * <ol>
 * <li>A SOAP payload is processed and a valid response returned.</li>
 * </ol>
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(EndpointContextPaths.ENDPOINT_UNSECURE)
@TestPropertySource({
        "classpath:config/endpoint/test-endpoint-unsecure-consumer.properties",
        "classpath:config/soap/test-soap-consumer.properties" })
public final class ITUnsecureEndpointFlowConsumer extends
        AbstractITEndpointFlow {

    /**
     * Name of the flow being tested.
     */
    @Value("${endpoint.flow}")
    private String endpointFlow;
    /**
     * Path to the SOAP payload for the request.
     */
    @Value("${soap.unsecure.request.payload.path}")
    private String requestPath;
    /**
     * Path to the SOAP payload for the response.
     */
    @Value("${soap.unsecure.response.payload.path}")
    private String responsePath;

    /**
     * Default constructor.
     */
    public ITUnsecureEndpointFlowConsumer() {
        super();
    }

    /**
     * Tests that a SOAP payload is processed and a valid response returned.
     * 
     * @throws Exception
     *             never, this is a required declaration
     */
    @Test
    public final void testEndpoint_Payload_ReturnsExpected() throws Exception {
        final String result;   // Response from the endpoint
        final String encoding; // Files encoding
        final String request;  // SOAP request
        final String response; // SOAP response

        // Loads the messages
        encoding = "UTF-8";
        response = IOUtils.toString(
                new ClassPathResource(responsePath).getInputStream(), encoding);
        request = IOUtils.toString(
                new ClassPathResource(requestPath).getInputStream(), encoding);

        // Sends the request to the flow
        result = runFlow(endpointFlow, request).getMessageAsString();

        // Verifies results
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(response, result);
    }

}