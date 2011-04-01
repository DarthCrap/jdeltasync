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

/**
 * Exception thrown by {@link DeltaSyncClient} when an authentication failure is 
 * received in response to a <code>Sync</code> or <code>ItemOperations</code> 
 * request. For now it is assumed that this only happens when the session's 
 * ticket has expired.
 */
@SuppressWarnings("serial")
public class SessionExpiredException extends DeltaSyncException {

    public SessionExpiredException(String message) {
        super(message);
    }

}
