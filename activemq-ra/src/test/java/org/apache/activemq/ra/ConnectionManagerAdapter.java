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

import java.util.ArrayList;
import java.util.Iterator;

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
 * A simple implementation of a ConnectionManager that can be extended so that
 * it can see how the RA connections are interacting with it.
 */
public class ConnectionManagerAdapter implements ConnectionManager, ConnectionEventListener {

    private static final long serialVersionUID = 5205646563916645831L;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManagerAdapter.class);
    private ArrayList<ConnectionEventListener> listners = new ArrayList<ConnectionEventListener>();
    private ArrayList<ManagedConnection> connections = new ArrayList<ManagedConnection>();

    /**
     * Adds a listener to all connections created by this connection manager.
     * This listener will be added to all previously created connections.
     *
     * @param l
     */
    public void addConnectionEventListener(ConnectionEventListener l) {
        for (Iterator<ManagedConnection> iter = connections.iterator(); iter.hasNext();) {
            ManagedConnection c = iter.next();
            c.addConnectionEventListener(l);
        }
        listners.add(l);
    }

    /**
     * @see jakarta.resource.spi.ConnectionManager#allocateConnection(jakarta.resource.spi.ManagedConnectionFactory,
     *      jakarta.resource.spi.ConnectionRequestInfo)
     */
    @Override
    public Object allocateConnection(ManagedConnectionFactory connectionFactory, ConnectionRequestInfo info) throws ResourceException {
        Subject subject = null;
        ManagedConnection connection = connectionFactory.createManagedConnection(subject, info);
        connection.addConnectionEventListener(this);
        for (Iterator<ConnectionEventListener> iter = listners.iterator(); iter.hasNext();) {
            ConnectionEventListener l = iter.next();
            connection.addConnectionEventListener(l);
        }
        connections.add(connection);
        return connection.getConnection(subject, info);
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionClosed(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void connectionClosed(ConnectionEvent event) {
        connections.remove(event.getSource());
        try {
            ((ManagedConnection)event.getSource()).cleanup();
        } catch (ResourceException e) {
            LOG.warn("Error occured during the cleanup of a managed connection: ", e);
        }

        // should go back in a pool, no destroy
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionStarted(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionStarted(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionCommitted(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionCommitted(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#localTransactionRolledback(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
    public void localTransactionRolledback(ConnectionEvent event) {
    }

    /**
     * @see jakarta.resource.spi.ConnectionEventListener#connectionErrorOccurred(jakarta.resource.spi.ConnectionEvent)
     */
    @Override
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
