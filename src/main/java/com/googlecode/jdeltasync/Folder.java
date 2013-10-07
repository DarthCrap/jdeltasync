/*
 * Copyright (c) 2011, the JDeltaSync project. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jdeltasync;

import java.io.Serializable;


/**
 * Represents a folder on the server.
 */
public class Folder implements IFolder {
    private final String id;
    private final String name;
	private final String parentID;

    /**
     * Creates a new {@link Folder} without a parent ID.
     *
     * @param id the id of the {@link Folder}.
     * @param name the name.
     */
    public Folder(String id, String name) {
		this(id,name,null);
    }

    /**
     * Creates a new {@link Folder}.
     *
     * @param id the id of the {@link Folder}.
     * @param name the name.
	 * @param parentID the parent id of the {@lik Folder}.
     */
    public Folder(String id, String name, String parentID) {
        this.id = id;
        this.name = name;
		this.parentID = parentID;
    }

    /**
     * Returns the id of this {@link Folder}.
     *
     * @return the id.
     */
	@Override
    public String getId() {
        return id;
    }

    /**
     * Returns the name of this {@link Folder}.
     *
     * @return the name.
     */
	@Override
    public String getName() {
        return name;
    }


    /**
     * Returns the parent id of this {@link Folder}.
     *
     * @return the parent id.
     */
	@Override
    public String getParentID() {
        return this.parentID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString() + "(");
        sb.append("id").append("=").append(id).append(",");
        sb.append("name").append("=").append(name).append(",");
		sb.append("parentid").append("=").append(parentID);
        sb.append(")");
        return sb.toString();
    }
}
