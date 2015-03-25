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

import java.util.Date;


/**
 * {@link Command} exchanged when a message has been changed.
 */
public class MessageChangeCommand extends AbstractMessageDetailsCommand {
	/**
	@see AbstractMessageDetailsCommand#AbstractMessageDetailsCommand(java.lang.String, java.lang.String, java.util.Date, long, boolean, java.lang.String, java.lang.String, boolean, boolean)
	*/
	public MessageChangeCommand(String id, String folderId, Date dateReceived, long size, boolean read,
            String subject, String from, boolean hasAttachments, boolean hasFlag) {
		super(id, folderId, dateReceived, size, read, subject, from, hasAttachments, hasFlag);
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
		sb.append("(");
		sb.append("MessageChangeCommand");
		sb.append(")");
		return sb.toString();
	}
}
