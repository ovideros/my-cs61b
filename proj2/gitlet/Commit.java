package gitlet;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author OvidEros
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;
    /** The timestamp of this Commit. */
    public String timeStamp;
    /** The parent node of this Commit. */
    public String parent;
    public Map<String, String> files;
    private static final Timestamp EPOCH_TIME = new Timestamp(0);

    /** Construct a new commit with message and parent. */
    public Commit(String msg, String parent) {
        this.message = msg;
        this.parent = parent;
        if (parent.isEmpty()) {
            this.timeStamp = EPOCH_TIME.toString();
            this.files = new HashMap<>();
        } else {
            Timestamp timeNow = new Timestamp(System.currentTimeMillis());
            this.timeStamp = timeNow.toString();
            this.files = read(parent).getFiles();
        }
    }

    /** Creates a new commit from old one. */
    public Commit newCommit(String msg) {
        return new Commit(msg, this.toSha1());
    }

    public void updateFiles(StagingArea sa) {
        Map<String, String> newFiles = sa.additionArea;
        Map<String, String> rmFiles = sa.removalArea;
        for (String name : newFiles.keySet()) {
            files.put(name, newFiles.get(name));
        }
        for (String name : rmFiles.keySet()) {
            files.remove(name);
        }
    }

    public Map<String, String> getFiles() {
        return files;
    }

    /** Get sha1 value for commit as filename,
     *  and store in .gitlet/commits/FT/ ,
     *  FT means first two character.
     */
    public void store() {
        Repository.storeInHashTable(Repository.COMMITS_DIR, this, toSha1());
    }

    /** Convert message and timeStamp to sha1. */
    public String toSha1() {
        StringBuilder tmp = new StringBuilder();
        for (String name : files.keySet()) {
            tmp.append(files.get(name));
        }
        return Utils.sha1(message, timeStamp, parent, tmp.toString());
    }

    public void addFile(String name, String sha1) {
        files.put(name, sha1);
    }

    public static Commit read(String sha1) {
        File file = Repository.fileFromHashTable(Repository.COMMITS_DIR, sha1);
        return readObject(file, Commit.class);
    }
}
