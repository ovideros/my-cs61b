package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.List;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {

    Head head;
    StagingArea area;

    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The .gitlet/blobs directory. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The .gitlet/commits directory. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    /** The .gitlet/branches directory. */
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");



    /** Initializing a repository, creating necessary folders and files.
     *  Check whether current .gitlet exists,
     *  if so, exit with error message.
     *
     *  Desired file structure:
     *  .gitlet/        -- overall folder
     *      - blobs/        -- storing files in the working directory
     *      - commits/      -- storing commits in history
     *      - branches/     -- storing different branches
     *      - HEAD          -- the current HEAD pointer
     *      - STAGING_AREA  -- staging area
     */
    public Repository() {
        if (GITLET_DIR.exists()) {
            Main.exitWithMessage("A Gitlet version-control system " +
                    "already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        Commit initCommit = new Commit("initial commit", null);
        Branch master = new Branch(initCommit.toSha1(), "master");
        head = new Head(initCommit.toSha1());
        area = new StagingArea();
        initCommit.store();
        master.store();
        head.store();
        area.store();
    }

    public Repository(Head h, StagingArea sa) {
        head = h;
        area = sa;
    }

    /** Using object and its sha1 to store in path. */
    public static void storeInHashTable(File path, Serializable o , String sha1) {
        File folder = join(path, sha1.substring(0, 2));
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = join(folder, sha1);
        writeObject(file, o);
    }

    public static File fileFromHashTable(File path, String sha1) {
        File folder = join(path, sha1.substring(0, 2));
        if (!folder.exists()) {
            Main.exitWithMessage("ERROR!!!!");
        }
        return join(folder, sha1);
    }

    /** Store current object by name. */
    public static void storeInName(File path, Serializable o, String name) {
        File file = join(path, name);
        writeObject(file, o);
    }


    /** Load all the needed persistent files,
     *  and return current Repository object.
     */
    public static Repository load() {
        Head head = Head.read();
        StagingArea area = StagingArea.read();
        return new Repository(head, area);
    }

    /** Add file into staging area. */
    public void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            Main.exitWithMessage("File does not exist");
        }
        String fileContents = Utils.readContentsAsString(file);
        Blob blob = new Blob(fileContents);
        String sha1 = blob.toSha1();
        Commit currCommit = Commit.read(head.next);
        if (currCommit.getFiles().get(fileName) != null &&
                currCommit.getFiles().get(fileName).equals(sha1)) {
            area.removeFileAddition(fileName, sha1);
        }
        blob.store();
        area.addFileAddition(fileName, sha1);
        area.store();
    }



}
