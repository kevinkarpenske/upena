/*
 * Copyright 2013 Jive Software, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.jivesoftware.os.upena.service;

import com.jivesoftware.os.jive.utils.ordered.id.OrderIdProvider;
import com.jivesoftware.os.upena.service.UpenaMap.UpenaKeyProvider;
import com.jivesoftware.os.upena.shared.Project;
import com.jivesoftware.os.upena.shared.ProjectKey;

public class ProjectKeyProvider implements UpenaKeyProvider<ProjectKey, Project> {

    private final OrderIdProvider idProvider;

    public ProjectKeyProvider(OrderIdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Override
    public ProjectKey getNodeKey(UpenaMap<ProjectKey,Project> table, Project value) {
        String k = Long.toString(Math.abs(idProvider.nextId()));
        return new ProjectKey(k);
    }
}
