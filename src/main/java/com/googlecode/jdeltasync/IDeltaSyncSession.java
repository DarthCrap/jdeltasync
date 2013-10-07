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

import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
public interface IDeltaSyncSession {

	/**
	 * Returns the {@link Logger} which will be used by {@link DeltaSyncClient}
	 * to log things for this session.
	 *
	 * @return the {@link Logger}.
	 */
	public Logger getLogger();

	/**
	 * Returns the password.
	 *
	 * @return the password.
	 */
	public String getPassword();

	/**
	 * Returns the ticket associated with this session. This is what the login
	 * process returns and the ticket has to be used in every subsequent
	 * <code>Sync</code>, <code>ItemOperations</code>, etc request to the
	 * server.
	 *
	 * @return the ticket.
	 */
	public String getTicket();

	/**
	 * Returns the username.
	 *
	 * @return the username.
	 */
	public String getUsername();

	/**
	 * Sets the {@link Logger} which will be used by {@link DeltaSyncClient}
	 * to log things for this session.
	 *
	 * @param logger the {@link Logger}.
	 */
	public void setLogger(Logger logger);

	public void setTicket(String ticket);

	public String getBaseUri();

	public void setBaseUri(String dsBaseUri);

	public CookieStore getCookieStore();
}
