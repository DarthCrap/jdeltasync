/*
 * Copyright (c) 2012, the JDeltaSync project. All Rights Reserved.
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

import java.util.Collection;

/**
 * Used by {@link DeltaSyncClientHelper} to cache {@link Folder}s and {@link Message}.
 */
public interface IStore {
    public String getFoldersSyncKey(String username);
    public String getMessagesSyncKey(String username, IFolder folder);
    public void updateFolders(String username, String syncKey, Collection<IFolder> added, Collection<IFolder> modified, Collection<String> deleted);
    public void resetFolders(String username);
    public void updateMessages(String username, IFolder folder, String syncKey, Collection<IMessage> added, Collection<IMessage> modified, Collection<String> deleted);
    public void resetMessages(String username, IFolder folder);
    public Collection<? extends IFolder> getFolders(String username);
    public Collection<? extends IMessage> getMessages(String username, IFolder folder);
}
