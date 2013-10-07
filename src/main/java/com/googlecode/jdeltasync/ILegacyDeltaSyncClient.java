/*
 * Copyright 2013 Scott.Dennison.
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

import com.googlecode.jdeltasync.message.SyncRequest;
import com.googlecode.jdeltasync.message.SyncResponse;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.conn.ClientConnectionManager;

/**
 <p/>
 @author Scott.Dennison
 */
public interface ILegacyDeltaSyncClient {
	@Deprecated public ClientConnectionManager getConnectionManager();
	public void setConnectionTimeout(int timeout);
	public void setSoTimeout(int timeout);
	public IDeltaSyncSession login(String username, String password)	throws AuthenticationException, DeltaSyncException, IOException;
	public IDeltaSyncSession renew(IDeltaSyncSession session) throws AuthenticationException, DeltaSyncException, IOException;
	public void downloadRawMessageContent(IDeltaSyncSession session, String messageId, OutputStream out) throws DeltaSyncException, IOException;
	public void downloadMessageContent(IDeltaSyncSession session, String messageId, OutputStream out) throws DeltaSyncException, IOException;
	public SyncResponse sync(IDeltaSyncSession session, SyncRequest syncRequest) throws DeltaSyncException, IOException;
}
