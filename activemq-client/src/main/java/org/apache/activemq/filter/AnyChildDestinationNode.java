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
package org.apache.activemq.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An implementation of {@link DestinationNode} which navigates all the children of the given node
 * ignoring the name of the current path (so for navigating using * in a wildcard).
 *
 * 
 */
public class AnyChildDestinationNode implements DestinationNode {
    private DestinationNode node;

    public AnyChildDestinationNode(DestinationNode node) {
        this.node = node;
    }

    public void appendMatchingValues(Set<DestinationNode> answer, String[] paths, int startIndex) {
        Iterator<DestinationNode> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = iter.next();
            child.appendMatchingValues(answer, paths, startIndex);
        }
    }


    public void appendMatchingWildcards(Set<DestinationNode> answer, String[] paths, int startIndex) {
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            child.appendMatchingWildcards(answer, paths, startIndex);
        }
    }


    public void appendDescendantValues(Set<DestinationNode> answer) {
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            child.appendDescendantValues(answer);
        }
    }

    public DestinationNode getChild(String path) {
        final Collection<DestinationNode> list = new ArrayList<DestinationNode>();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            DestinationNode answer = child.getChild(path);
            if (answer != null) {
                list.add(answer);
            }
        }
        if (!list.isEmpty()) {
            return new AnyChildDestinationNode(this) {
                protected Collection<DestinationNode> getChildNodes() {
                    return list;
                }
            };
        }
        return null;
    }

    public Collection getDesendentValues() {
        Collection answer = new ArrayList();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            answer.add(child.getDesendentValues());
        }
        return answer;
    }

    public Collection getValues() {
        Collection answer = new ArrayList();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            answer.add(child.getValues());
        }
        return answer;
    }

    public Collection getChildren() {
        Collection answer = new ArrayList();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            answer.add(child.getChildren());
        }
        return answer;
    }

    public Collection removeDesendentValues() {
        Collection answer = new ArrayList();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            answer.add((Collection<?>) child.removeDesendentValues());
        }
        return answer;
    }

    public Collection removeValues() {
        Collection answer = new ArrayList<Object>();
        Iterator<?> iter = getChildNodes().iterator();
        while (iter.hasNext()) {
            DestinationNode child = (DestinationNode) iter.next();
            answer.add((Collection<?>) child.removeValues());
        }
        return answer;
    }

    protected Collection<DestinationNode> getChildNodes() {
        return node.getChildren();
    }
}


