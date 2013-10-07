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
import java.util.Date;


public interface IMessage extends Serializable {

	/**
	 * Returns the {@link Date} and time when this message was received.
	 *
	 * @return the {@link Date} and time.
	 */
	public Date getDateReceived();

	/**
	 * Returns the name and e-mail address of the person who sent this
	 * {@link Message}.
	 *
	 * @return the sender name and e-mail.
	 */
	public String getFrom();

	/**
	 * Returns the id of this {@link Message}.
	 *
	 * @return the id.
	 */
	public String getId();

	/**
	 * Returns the size of this {@link Message} in bytes.
	 *
	 * @return the size.
	 */
	public long getSize();

	/**
	 * Returns the subject of this {@link Message}.
	 *
	 * @return the subject.
	 */
	public String getSubject();

	/**
	 * Returns whether this {@link Message} has attachments.
	 *
	 * @return <code>true</code> if the messages has attachments,
	 *         <code>false</code> otherwise.
	 */
	public boolean hasAttachments();

	/**
	 * Returns whether this {@link Message} has been read.
	 *
	 * @return <code>true</code> if the messages has been read,
	 *         <code>false</code> otherwise.
	 */
	public boolean isRead();

}
