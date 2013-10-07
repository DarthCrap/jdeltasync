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
package com.googlecode.jdeltasync.message;


/**
 * {@link Command} an abstract class for commands involving folder details.
 */
public abstract class AbstractFolderDetailsCommand extends Command {
    private final String id;
	private final String parentId;
    private final String displayName;

    /**
     * Creates a new {@link FolderAddCommand}.
     *
     * @param id the id of the folder.
     * @param displayName the <code>DisplayName</code>.
	 * @paran parentID the id of the parent folder.
     */
    public AbstractFolderDetailsCommand(String id, String displayName, String parentID) {
        this.id = id;
        this.displayName = displayName;
		this.parentId = parentID;
    }

    /**
     * Returns the id of the folder that has been added.
     *
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the <code>DisplayName</code> of the folder that has been added.
     *
     * @return the <code>DisplayName</code>.
     */
    public String getDisplayName() {
        return displayName;
    }

	/**
	 * Returns the id of the parent of the folder that has been added.
	 * @return the parent id
	 */
	public String getParentID() {
		return this.parentId;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString() + "(");
        sb.append("id").append("=").append(id).append(",");
        sb.append("displayName").append("=").append(displayName).append(",");
		sb.append("parentID").append("=").append(parentId);
        sb.append(")");
        return sb.toString();
    }
}
