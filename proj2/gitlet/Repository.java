package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  Contains head and area.
 *
 *  @author OvidEros
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    Head head;
    StagingArea area;

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
            Main.exitWithMessage("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        BRANCHES_DIR.mkdir();
        Commit initCommit = new Commit("initial commit", "");
        Branch master = new Branch(initCommit.toSha1(), "master");
        head = new Head(initCommit.toSha1());
        area = new StagingArea();
        initCommit.store();
        master.store();
        head.store();
        area.store();
    }

    /** Construct repo with head and staging area. */
    public Repository(Head h, StagingArea sa) {
        head = h;
        area = sa;
    }

    /** Using object and its sha1 to store in path. */
    public static void storeInHashTable(File path, Serializable o, String sha1) {
        File folder = join(path, sha1.substring(0, 2));
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = join(folder, sha1);
        writeObject(file, o);
    }

    /** Return the file by the path and SHA1 value. */
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
        Commit currCommit = Commit.read(head.next());
        if (currCommit.files().get(fileName) != null
                && currCommit.files().get(fileName).equals(sha1)) {
            area.getAdditionArea().remove(fileName);
            System.exit(0);
        }
        blob.store();
        area.getAdditionArea().put(fileName, sha1);
        area.store();
    }

    /** Commit with message. */
    public void commit(String msg) {
        if (area.getAdditionArea().isEmpty() && area.getRemovalArea().isEmpty()) {
            Main.exitWithMessage("No changes added to the commit.");
        }
        Commit currCommit = Commit.read(head.next());
        Commit newCommit = currCommit.newCommit(msg);
        newCommit.updateFiles(area);
        area.clear();
        head.updateNext(newCommit.toSha1());
        newCommit.store();
        area.store();
        head.store();
        // TODO: branch change
    }

    /** Remove file. */
    public void rm(String fileName) {
        boolean flag = false;
        String result1 = area.getAdditionArea().remove(fileName);
        if (result1 != null) {
            flag = true;
        }
        Commit currCommit = Commit.read(head.next());
        if (currCommit.files().containsKey(fileName)) {
            String sha1 = currCommit.files().get(fileName);
            area.getRemovalArea().put(fileName, sha1);
            File rmFile = join(CWD, fileName);
            Utils.restrictedDelete(rmFile);
            flag = true;
        }
        if (!flag) {
            Main.exitWithMessage("No reason to remove the file.");
        }
        area.store();
    }

    /** Print log. */
    public void log() {
        Head p = head;
        while (!p.next().isEmpty()) {
            Commit currCommit = Commit.read(p.next());
            currCommit.dump();
            p.updateNext(currCommit.getParent());
        }
    }
}
