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

public interface IDeltaSyncClientHelper {
	public String[] delete(Folder folder, String[] ids) throws DeltaSyncException, IOException;
	public String[] delete(Folder folder, Message[] messages) throws DeltaSyncException, IOException;
	public void downloadMessageContent(Message message, OutputStream out) throws DeltaSyncException, IOException;
	public void downloadRawMessageContent(Message message, OutputStream out) throws DeltaSyncException, IOException;
	public Folder[] getFolders() throws DeltaSyncException, IOException;
	public Folder getInbox() throws DeltaSyncException, IOException;
	public Message[] getMessages(Folder folder) throws DeltaSyncException, IOException;
	public DeltaSyncSession getSession();
	public int getWindowSize();
	public void login() throws AuthenticationException, DeltaSyncException, IOException;
	public void setWindowSize(int windowSize);
}
