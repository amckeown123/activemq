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

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple implementation of a ConnectionManager. An Application Server will
 * have a better implementation with pooling and security etc.
 * 
 * 
 */
public class SimpleConnectionManager implements ConnectionManager, ConnectionEventListener {

    private static final long serialVersionUID = -7662970495944876239L;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConnectionManager.class);

    /**
     * @see jakarta.resource.spi.ConnectionManager#allocateConnection(jakarta.resource.spi.ManagedConnectionFactory,
     *      jakarta.resource.spi.ConnectionRequestInfo)
     */
    public Object allocateConnection(ManagedConnectionFactory connectionFactory, ConnectionRequestInfo info) throws ResourceException {
        Subject subject = null;
        ManagedConnection connection = connectionFactory.createManagedConnection(subject, info);
        connection.addConnectionEventListener(this);
        return connection.getConnection(subject, info);
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionClosed(jakarta.resource.spi.ConnectionEvent)
     */
    public void connectionClosed(ConnectionEvent event) {
        try {
            ((ManagedConnection)event.getSource()).cleanup();
        } catch (ResourceException e) {
            LOG.warn("Error occured during the cleanup of a managed connection: ", e);
        }
        try {
            ((ManagedConnection)event.getSource()).destroy();
        } catch (ResourceException e) {
            LOG.warn("Error occured during the destruction of a managed connection: ", e);
        }
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionStarted(jakarta.resource.spi.ConnectionEvent)
     */
    public void localTransactionStarted(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionCommitted(jakarta.resource.spi.ConnectionEvent)
     */
    public void localTransactionCommitted(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionRolledback(jakarta.resource.spi.ConnectionEvent)
     */
    public void localTransactionRolledback(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionErrorOccurred(jakarta.resource.spi.ConnectionEvent)
     */
    public void connectionErrorOccurred(ConnectionEvent event) {
        LOG.warn("Managed connection experiened an error: ", event.getException());
        try {
            ((ManagedConnection)event.getSource()).cleanup();
        } catch (ResourceException e) {
            LOG.warn("Error occured during the cleanup of a managed connection: ", e);
        }
        try {
            ((ManagedConnection)event.getSource()).destroy();
        } catch (ResourceException e) {
            LOG.warn("Error occured during the destruction of a managed connection: ", e);
        }
    }

}
