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

import com.googlecode.jdeltasync.message.Clazz;
import com.googlecode.jdeltasync.message.Command;
import com.googlecode.jdeltasync.message.FolderAddCommand;
import com.googlecode.jdeltasync.message.FolderChangeCommand;
import com.googlecode.jdeltasync.message.FolderDeleteCommand;
import com.googlecode.jdeltasync.message.MessageAddCommand;
import com.googlecode.jdeltasync.message.MessageChangeCommand;
import com.googlecode.jdeltasync.message.MessageDeleteCommand;
import com.googlecode.jdeltasync.message.SyncRequest;
import com.googlecode.jdeltasync.message.SyncResponse;
import com.googlecode.jdeltasync.message.SyncResponse.Collection.EmailDeleteResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DeltaSyncClientHelper implements IDeltaSyncClientHelper, ILegacyDeltaSyncClientHelper {
    /**
     * The default number of messages to request at a time in
     * {@link #getMessages(IFolder)}. Max value seems to be 2000. If a higher
     * value is used the server never returns more than 2000 in each Sync
     * response.
     * <p>
     * Each Add in the response is about 1 kB so 256 should mean each
     * response is about 256 kB maximum.
     */
    public static final int DEFAULT_WINDOW_SIZE = 256;

    private final IDeltaSyncClient client;
    private final IStore store;
    private final String username;
    private final String password;
    private IDeltaSyncSession session;

    private int windowSize = DEFAULT_WINDOW_SIZE;

    public DeltaSyncClientHelper(IDeltaSyncClient client, String username, String password) {
        this(client, username, password, new InMemoryStore());
    }

    public DeltaSyncClientHelper(IDeltaSyncClient client, String username, String password, IStore store) {
        if (client == null) {
            throw new NullPointerException("client");
        }
        if (username == null) {
            throw new NullPointerException("username");
        }
        if (password == null) {
            throw new NullPointerException("password");
        }
        if (store == null) {
            throw new NullPointerException("store");
        }
        this.client = client;
        this.username = username;
        this.password = password;
        this.store = store;
    }

    public IDeltaSyncSession getSession() {
        return session;
    }

    /**
     * Returns the current <code>windowSize</code> which specifies the maximum
     * number of {@link Command} returned by a call to
     * {@link DeltaSyncClient#sync(IDeltaSyncSession, SyncRequest)} made by
     * {@link #getMessages(IFolder)}. {@link #getMessages(IFolder)} needs to do
     * <code>totalNumberOfMessagesInFolder / windowSize</code> requests to
     * get all messages in a folder.
     *
     * @return the current <code>windowSize</code>.
     * @see #DEFAULT_WINDOW_SIZE
     */
    public int getWindowSize() {
        return windowSize;
    }

    /**
     * Sets the <code>windowSize</code>.
     *
     * @param windowSize the new <code>windowSize</code>.
     * @throws IllegalArgumentException if the specified value is negative or 0.
     * @see #DEFAULT_WINDOW_SIZE
     * @see #getWindowSize()
     */
    public void setWindowSize(int windowSize) {
        if (windowSize <= 0) {
            throw new IllegalArgumentException("windowSize");
        }
        this.windowSize = windowSize;
    }

    /**
     * Returns the <code>DisplayName</code> of a folder mapped to a nicer name.
     * The standard folders have funny display names (e.g. drAfT).
     */
    private String getMappedDisplayName(String displayName) {
		SpecialFolder sfFolder = SpecialFolder.getSpecialFolderForRawName(displayName);
		if (sfFolder != null) {
			return sfFolder.getDisplayName();
		}
		else {
			return displayName;
		}
    }

    private void checkLoggedIn() {
        if (session == null) {
            throw new IllegalStateException("Not logged in");
        }
    }

    /**
     * Logs in.
     *
     * @throws AuthenticationException if authentication fails.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if already logged in.
     */
    public void login() throws AuthenticationException, DeltaSyncException, IOException {
        if (session != null) {
            throw new IllegalStateException("Already logged in");
        }
        this.session = client.login(username, password);
    }

    /**
     * Returns all {@link IFolder}s.
     *
     * @return all {@link IFolder}s.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public Collection<IFolder> getFoldersCollection() throws DeltaSyncException, IOException {
        checkLoggedIn();
        try {
            return doGetFolders();
        } catch (SessionExpiredException e) {
            session = client.renew(session);
            return doGetFolders();
        } catch (InvalidSyncKeyException e) {
            session.getLogger().warn("Invalid folders sync key. All folders "
                    + "will be retrieved anew.");
            store.resetFolders(username);
            return doGetFolders();
        }
    }

    private Collection<IFolder> doGetFolders() throws DeltaSyncException, IOException {

        while (true) {

            SyncRequest syncRequest = new SyncRequest(new SyncRequest.Collection(
                    store.getFoldersSyncKey(username), Clazz.Folder, true));
            SyncResponse response = client.sync(session, syncRequest);

            if (response.getCollections().isEmpty()) {
                throw new DeltaSyncException("No <Collection> in Sync response");
            }
            SyncResponse.Collection collection = response.getCollections().get(0);
            if (collection.getStatus() != 1) {
                throw new DeltaSyncException("Sync request failed with status "
                        + collection.getStatus());
            }

            List<IFolder> added = new ArrayList<IFolder>();
			List<IFolder> changed = new ArrayList<IFolder>();
            List<String> deleted = new ArrayList<String>();
            for (Command cmd : collection.getCommands()) {
                if (cmd instanceof FolderAddCommand) {
                    FolderAddCommand addCmd = (FolderAddCommand) cmd;
                    added.add(new Folder(addCmd.getId(),
                            getMappedDisplayName(addCmd.getDisplayName()),
							addCmd.getParentID()));
                }
				else if (cmd instanceof FolderChangeCommand) {
                    FolderChangeCommand changeCmd = (FolderChangeCommand) cmd;
                    changed.add(new Folder(changeCmd.getId(),
                            getMappedDisplayName(changeCmd.getDisplayName()),
							changeCmd.getParentID()));
				}
				else if (cmd instanceof FolderDeleteCommand) {
                    FolderDeleteCommand delCmd = (FolderDeleteCommand) cmd;
                    deleted.add(delCmd.getId());
                }
            }

            store.updateFolders(username, collection.getSyncKey(), added, changed, deleted);

            if (!collection.isMoreAvailable()) {
                break;
            }
        }

		return Collections.unmodifiableCollection(new ArrayList<IFolder>(store.getFolders(username)));
    }

    /**
     * Returns the Inbox {@link Folder}.
     *
     * @return the Inbox {@link Folder} or <code>null</code> if not found.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public IFolder getInbox() throws DeltaSyncException, IOException {
		return getSpecialFolder(SpecialFolder.INBOX);
    }


    /**
     * Returns all messages in the specified {@link Folder}.
     *
     * @param folder the {@link Folder}.
     * @return all messages in the specified {@link Folder}.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public Collection<IMessage> getMessagesCollection(IFolder folder) throws DeltaSyncException, IOException {
        checkLoggedIn();
        try {
            return doGetMessages(folder);
        } catch (SessionExpiredException e) {
            session = client.renew(session);
            return doGetMessages(folder);
        } catch (InvalidSyncKeyException e) {
            session.getLogger().warn("Invalid messages sync key. All messages "
                    + "will be retrieved anew.");
            store.resetMessages(username, folder);
            return doGetMessages(folder);
        } catch (DeltaSyncException e) {
            if (e.getMessage().contains("Sync request failed with status 4104")) {
                session.getLogger().warn("Got 4104 error. All messages "
                        + "will be retrieved anew.");
                store.resetMessages(username, folder);
                return doGetMessages(folder);
            }
            throw e;
        }
    }

    private Collection<IMessage> doGetMessages(IFolder folder) throws DeltaSyncException, IOException {

        while (true) {

            SyncRequest syncRequest = new SyncRequest(new SyncRequest.Collection(
                    store.getMessagesSyncKey(username, folder), Clazz.Email, folder.getId(), true, windowSize));
            SyncResponse response = client.sync(session, syncRequest);

            if (response.getCollections().isEmpty()) {
                throw new DeltaSyncException("No <Collection> in Sync response");
            }
            SyncResponse.Collection collection = response.getCollections().get(0);
            if (collection.getStatus() != 1) {
                throw new DeltaSyncException("Sync request failed with status "
                        + collection.getStatus());
            }

            List<IMessage> added = new ArrayList<IMessage>();
			List<IMessage> changed = new ArrayList<IMessage>();
            List<String> deleted = new ArrayList<String>();
            for (Command cmd : collection.getCommands()) {
                if (cmd instanceof MessageAddCommand) {
                    MessageAddCommand addCmd = (MessageAddCommand) cmd;
                    added.add(new Message(addCmd.getId(),
                            addCmd.getDateReceived(), addCmd.getSize(), addCmd.isRead(),
                            addCmd.getSubject(), addCmd.getFrom(), addCmd.hasAttachments()));
				}
				else if (cmd instanceof MessageChangeCommand) {
                    MessageChangeCommand changeCmd = (MessageChangeCommand) cmd;
                    changed.add(new Message(changeCmd.getId(),
                            changeCmd.getDateReceived(), changeCmd.getSize(), changeCmd.isRead(),
                            changeCmd.getSubject(), changeCmd.getFrom(), changeCmd.hasAttachments()));
				}
                else if (cmd instanceof MessageDeleteCommand) {
                    MessageDeleteCommand delCmd = (MessageDeleteCommand) cmd;
                    deleted.add(delCmd.getId());
                }
            }

            store.updateMessages(username, folder, collection.getSyncKey(), added, changed, deleted);

            if (!collection.isMoreAvailable()) {
                break;
            }
        }

        return Collections.unmodifiableCollection(new ArrayList<IMessage>(store.getMessages(username, folder)));
    }

    /**
     * Deletes the specified {@link Email}s from the specified {@link Folder}.
     *
     * @param folder the {@link Folder}.
     * @param messages the {@link Email}s to be deleted.
     * @return the ids of the {@link Email}s actually deleted.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public Collection<String> deleteMessages(IFolder folder, Collection<IMessage> messages) throws DeltaSyncException, IOException {
        List<String> ids = new ArrayList<String>(messages.size());
		for (IMessage message : messages) {
			ids.add(message.getId());
		}
        return deleteMessagesByID(folder, ids);
    }

    /**
     * Deletes the {@link Email}s with the specified ids from the specified
     * {@link Folder}.
     *
     * @param folder the {@link Folder}.
     * @param ids the ids to be deleted.
     * @return the ids of the {@link Email}s actually deleted.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public Collection<String> deleteMessagesByID(IFolder folder, Collection<String> ids) throws DeltaSyncException, IOException {
        checkLoggedIn();
        LinkedList<String> idsList = new LinkedList<String>(ids);
        ArrayList<String> deleted = new ArrayList<String>();
        try {
            doDelete(folder, idsList, deleted);
        } catch (SessionExpiredException e) {
            session = client.renew(session);
            doDelete(folder, idsList, deleted);
        } catch (InvalidSyncKeyException e) {
            session.getLogger().debug("Invalid messages sync key. Delete will "
                    + "be retried with sync key 0");
            store.resetMessages(username, folder);
            doDelete(folder, idsList, deleted);
        }
        return Collections.unmodifiableList(deleted);
    }

    private void doDelete(IFolder folder, Collection<String> ids, Collection<String> deleted) throws DeltaSyncException, IOException {
		LinkedList<String> idsll = new LinkedList<String>(ids);
        while (!idsll.isEmpty()) {

            /*
             * Only delete up to 64 messages in each request. We don't know the
             * exact limit but we know that 160 is too many. 64 works fine.
             */
            List<Command> commands = new ArrayList<Command>();
            for (String id : idsll.size() < 64 ? idsll : idsll.subList(0, 64)) {
                commands.add(new MessageDeleteCommand(id));
            }

            SyncRequest syncRequest = new SyncRequest(new SyncRequest.Collection(
                    store.getMessagesSyncKey(username, folder), Clazz.Email, folder.getId(), commands));
            SyncResponse response = client.sync(session, syncRequest);

            if (response.getCollections().isEmpty()) {
                throw new DeltaSyncException("No <Collection> in Sync response");
            }
            SyncResponse.Collection collection = response.getCollections().get(0);
            if (collection.getStatus() != 1) {
                throw new DeltaSyncException("Sync request failed with status "
                        + collection.getStatus());
            }

            if (collection.getCommands() != null && !collection.getCommands().isEmpty()) {
                // We don't send <GetChanges> so we shouldn't get any Commands in the response
                store.resetMessages(username, folder);
                throw new DeltaSyncException("Delete should not return any Commands");
            }

            // Remove commands.size() elements from ids
            idsll.subList(0, commands.size()).clear();

            List<String> deletedInRun = new ArrayList<String>();
            for (SyncResponse.Collection.Response rsp : collection.getResponses()) {
                if (rsp instanceof SyncResponse.Collection.EmailDeleteResponse) {
                    SyncResponse.Collection.EmailDeleteResponse delRsp =
                        (EmailDeleteResponse) rsp;
                    deletedInRun.add(delRsp.getId());
                    // status == 4403 means no such message found
                    if (delRsp.getStatus() == 1) {
                        deletedInRun.add(delRsp.getId());
                    }
                }
            }

            deleted.addAll(deletedInRun);

            store.updateMessages(username, folder, collection.getSyncKey(),
                    new ArrayList<IMessage>(), new ArrayList<IMessage>(), deletedInRun);
        }
    }

    /**
     * Downloads the content of the specified {@link Email} and writes it to
     * the specified {@link OutputStream}.
     *
     * @param message the {@link Email} to download the content for.
     * @param out the stream to write the message content to.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public void downloadMessageContent(IMessage message, OutputStream out)
            throws DeltaSyncException, IOException {

        checkLoggedIn();
        try {
            client.downloadMessageContent(session, message.getId(), out);
        } catch (SessionExpiredException e) {
            session = client.renew(session);
            client.downloadMessageContent(session, message.getId(), out);
        }
    }

    /**
     * Downloads the HU01 compressed content of the specified {@link Email}
     * and writes it to the specified {@link OutputStream}.
     *
     * @param message the {@link Email} to download the content for.
     * @param out the stream to write the HU01 compressed message content to.
     * @throws SessionExpiredException if the session has expired and couldn't
     *         be renewed.
     * @throws DeltaSyncException on errors returned by the server.
     * @throws IOException on communication errors.
     * @throws IllegalStateException if not logged in.
     */
    public void downloadRawMessageContent(IMessage message, OutputStream out)
            throws DeltaSyncException, IOException {

        checkLoggedIn();
        try {
            client.downloadRawMessageContent(session, message.getId(), out);
        } catch (SessionExpiredException e) {
            session = client.renew(session);
            client.downloadRawMessageContent(session, message.getId(), out);
        }
    }

	public IFolder getSpecialFolder(SpecialFolder sfFolder) throws DeltaSyncException, IOException {
		return getFolderByName(sfFolder.getDisplayName());
	}

	public IFolder getFolderByName(String strName) throws DeltaSyncException, IOException {
        checkLoggedIn();

        for (IFolder folder : store.getFolders(username)) {
            if (strName.equals(folder.getName())) {
                return folder;
            }
        }
        getFoldersCollection();
        for (IFolder folder : store.getFolders(username)) {
            if (strName.equals(folder.getName())) {
                return folder;
            }
        }
        return null;
	}

	public IFolder getFolderByRawName(String strName) throws DeltaSyncException, IOException {
		return getFolderByName(getMappedDisplayName(strName));
	}

	public IFolder getFolderByID(String strID) throws DeltaSyncException, IOException {
        checkLoggedIn();

        for (IFolder folder : store.getFolders(username)) {
            if (strID.equals(folder.getId())) {
                return folder;
            }
        }
        getFoldersCollection();
        for (IFolder folder : store.getFolders(username)) {
            if (strID.equals(folder.getId())) {
                return folder;
            }
        }
        return null;
	}

	public void logout() {
		if (session == null) {
            throw new IllegalStateException("Already logged out");
        }
		else {
			session = null;
		}
	}

	public IFolder[] getFolders() throws DeltaSyncException, IOException {
		Collection<IFolder> colFolders = this.getFoldersCollection();
		return colFolders.toArray(new IFolder[colFolders.size()]);
	}

	public IMessage[] getMessages(IFolder folder) throws DeltaSyncException, IOException {
		Collection<IMessage> colMessages = this.getMessagesCollection(folder);
		return colMessages.toArray(new IMessage[colMessages.size()]);
	}

	public String[] delete(IFolder folder, IMessage[] messages) throws DeltaSyncException, IOException {
		Collection<String> colIDs = this.deleteMessages(folder, Arrays.asList(messages));
		return colIDs.toArray(new String[colIDs.size()]);
	}

	public String[] delete(IFolder folder, String[] ids) throws DeltaSyncException, IOException {
		Collection<String> colIDs = this.deleteMessagesByID(folder, Arrays.asList(ids));
		return colIDs.toArray(new String[colIDs.size()]);
	}
}
