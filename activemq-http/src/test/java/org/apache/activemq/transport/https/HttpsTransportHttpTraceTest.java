/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.transport.https;

import org.apache.activemq.transport.http.HttpTransportHttpTraceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class HttpsTransportHttpTraceTest extends HttpTransportHttpTraceTest {

    public HttpsTransportHttpTraceTest(String enableTraceParam, int expectedStatus) {
        super(enableTraceParam, expectedStatus);
    }

    @Override
    protected String getConnectorUri() {
        return "https://localhost:0?" + enableTraceParam;
    }

    @Override
    public void additionalConfig() {
        System.setProperty("jakarta.net.ssl.trustStore", "src/test/resources/client.keystore");
        System.setProperty("jakarta.net.ssl.trustStorePassword", "password");
        System.setProperty("jakarta.net.ssl.trustStoreType", "jks");
        System.setProperty("jakarta.net.ssl.keyStore", "src/test/resources/server.keystore");
        System.setProperty("jakarta.net.ssl.keyStorePassword", "password");
        System.setProperty("jakarta.net.ssl.keyStoreType", "jks");
    }
}
