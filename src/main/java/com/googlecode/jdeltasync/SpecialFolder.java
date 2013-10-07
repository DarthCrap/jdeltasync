package com.googlecode.jdeltasync;

public enum SpecialFolder {
	INBOX                   ("Inbox"                   , "ACTIVE"      ),
	DRAFTS                  ("Drafts"                  , "drAfT"       ),
	JUNK                    ("Junk"                    , "HM_BuLkMail_"),
	SENT                    ("Sent"                    , "sAVeD"       ),
	DELETED                 ("Deleted"                 , "trAsH"       ),
	OFFLINE_INSTANT_MESSAGES("Offline Instant Messages", ".!!OIM"      );

	private String strDisplayName;
	private String strRawName;

	private SpecialFolder(String strDisplayName, String strRawName) {
		this.strDisplayName = strDisplayName;
		this.strRawName = strRawName;
	}

	public String getRawName() {
		return this.strRawName;
	}

	public String getDisplayName() {
		return this.strDisplayName;
	}

	public static SpecialFolder getSpecialFolderForDisplayName(String strDisplayName) {
		for (SpecialFolder spFolder : SpecialFolder.values()) {
			if (spFolder.getDisplayName().equals(strDisplayName)) {
				return spFolder;
			}
		}
		return null;
	}

	public static SpecialFolder getSpecialFolderForRawName(String strRawName) {
		for (SpecialFolder spFolder : SpecialFolder.values()) {
			if (spFolder.getRawName().equals(strRawName)) {
				return spFolder;
			}
		}
		return null;
	}
}
