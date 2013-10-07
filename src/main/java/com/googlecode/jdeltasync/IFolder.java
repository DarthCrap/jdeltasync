/*
 * Copyright (c) 2011, the JDeltaSync project. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jdeltasync;

import java.io.Serializable;


public interface IFolder extends Serializable {
	/**
	 * Returns the id of this {@link Folder}.
	 *
	 * @return the id.
	 */
	String getId();

	/**
	 * Returns the name of this {@link Folder}.
	 *
	 * @return the name.
	 */
	String getName();

	/**
	 * Returns the parent id of this {@link Folder}.
	 *
	 * @return the parent id.
	 */
	String getParentID();
}
