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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public interface IDeltaSyncClientHelper {
	public static final int DEFAULT_WINDOW_SIZE = 256;
	public Collection<String> deleteMessagesByID(IFolder folder, Collection<String> ids) throws DeltaSyncException, IOException;
	public Collection<String> deleteMessages(IFolder folder, Collection<IMessage> messages) throws DeltaSyncException, IOException;
	public void downloadMessageContent(IMessage message, OutputStream out) throws DeltaSyncException, IOException;
	public void downloadRawMessageContent(IMessage message, OutputStream out) throws DeltaSyncException, IOException;
	public Collection<IFolder> getFoldersCollection() throws DeltaSyncException, IOException;
	public IFolder getInbox() throws DeltaSyncException, IOException;
	public IFolder getSpecialFolder(SpecialFolder sfFolder) throws DeltaSyncException, IOException;
	public IFolder getFolderByName(String strName) throws DeltaSyncException, IOException;
	public IFolder getFolderByRawName(String strName) throws DeltaSyncException, IOException;
	public IFolder getFolderByID(String strName) throws DeltaSyncException, IOException;
	public Collection<IMessage> getMessagesCollection(IFolder folder) throws DeltaSyncException, IOException;
	public IDeltaSyncSession getSession();
	public void login() throws AuthenticationException, DeltaSyncException, IOException;
	public void logout();
	public int getWindowSize();
	public void setWindowSize(int windowSize);
}
