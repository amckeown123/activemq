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
package org.apache.activemq.ra;

import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;

public class ConnectionEventListenerAdapter implements ConnectionEventListener {

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionClosed(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void connectionClosed(ConnectionEvent arg0) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionStarted(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionStarted(ConnectionEvent arg0) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionCommitted(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionCommitted(ConnectionEvent arg0) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionRolledback(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionRolledback(ConnectionEvent arg0) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionErrorOccurred(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void connectionErrorOccurred(ConnectionEvent arg0) {
    }
}
