/*
  * Copyright (c) 2012, the JDeltaSync project. All Rights Reserved.
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

/**
 <p/>
 @author Scott.Dennison
 */
public interface ILegacyDeltaSyncClientHelper {
	public static final int DEFAULT_WINDOW_SIZE = 256;
	public IDeltaSyncSession getSession();
	public int getWindowSize();
	public void setWindowSize(int windowSize);
	public void login() throws AuthenticationException, DeltaSyncException, IOException;
	public IFolder[] getFolders() throws DeltaSyncException, IOException;
	public IFolder getInbox() throws DeltaSyncException, IOException;
	public IMessage[] getMessages(IFolder folder) throws DeltaSyncException, IOException;
	public String[] delete(IFolder folder, IMessage[] messages) throws DeltaSyncException, IOException;
	public String[] delete(IFolder folder, String[] ids) throws DeltaSyncException, IOException;
	public void downloadMessageContent(IMessage message, OutputStream out) throws DeltaSyncException, IOException;
	public void downloadRawMessageContent(IMessage message, OutputStream out) throws DeltaSyncException, IOException;
}
