package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;

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
    private Head head;
    private StagingArea area;
    private Commit currCommit;
    private Branches branches;

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The .gitlet/blobs directory. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** The .gitlet/commits directory. */
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final int HASH_PREFIX_LENGTH = 2;
    public static final String DEFAULT_BRANCH_NAME = "master";


    /** Initializing a repository, creating necessary folders and files.
     *  Check whether current .gitlet exists,
     *  if so, exit with error message.
     *
     * <p>
     *  Desired file structure:
     *  <ul>
     *  <li>.gitlet/        -- overall folder</li>
     *      <ul>
     *      <li> blobs/        -- storing files in the working directory</li>
     *      <li> commits/      -- storing commits in history</li>
     *      <li> BRANCHES      -- storing branches</li>
     *      <li> HEAD          -- the current HEAD pointer</li>
     *      <li> STAGING_AREA  -- staging area</li>
     *      </ul>
     *  </ul>
     *  </p>
     */
    public Repository() {
        if (GITLET_DIR.exists()) {
            Main.exitWithMessage("A Gitlet version-control system "
                    + "already exists in the current directory.");
        }
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        Commit initCommit = new Commit("initial commit", "");
        branches = new Branches();
        branches.putBranch(DEFAULT_BRANCH_NAME, initCommit.toSha1());
        branches.setActiveBranch(DEFAULT_BRANCH_NAME);
        head = new Head(initCommit.toSha1());
        area = new StagingArea();
        initCommit.store();
        saveState();
    }

    /** Construct repo with head and staging area. */
    public Repository(Head h, StagingArea sa, Commit cm, Branches b) {
        head = h;
        area = sa;
        currCommit = cm;
        branches = b;
    }

    /** Save current state. */
    private void saveState() {
        branches.store();
        head.store();
        area.store();
    }

    /** Using object and its sha1 to store in path. */
    public static void storeInHashTable(File path, Serializable o, String sha1) {
        File folder = join(path, sha1.substring(0, HASH_PREFIX_LENGTH));
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = join(folder, sha1);
        writeObject(file, o);
    }

    /** Return the file by the path and SHA1 value. */
    public static File fileFromHashTable(File path, String sha1) {
        File folder = join(path, sha1.substring(0, HASH_PREFIX_LENGTH));
        if (!folder.exists()) {
            Main.exitWithMessage("ERROR!!!!");
        }
        return join(folder, sha1);
    }

    /** Return the file from hashTable with only prefix SHA1 value. */
    public static File fileFromHTPrefix(File path, String pSha1) {
        File folder = join(path, pSha1.substring(0, HASH_PREFIX_LENGTH));
        List<String> fileSha1s = plainFilenamesIn(folder);
        if (fileSha1s != null) {
            for (String sha1 : fileSha1s) {
                if (sha1.startsWith(pSha1)) {
                    return join(folder, sha1);
                }
            }
        }
        return null;
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
        Commit currCommit = getCurrCommit(head);
        Branches branches = Branches.read();
        return new Repository(head, area, currCommit, branches);
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
        if (currCommit.files().get(fileName) != null
                && currCommit.files().get(fileName).equals(sha1)) {
            area.getAdditionArea().remove(fileName);
            System.exit(0);
        }
        blob.store();
        area.getAdditionArea().put(fileName, sha1);
        saveState();
    }

    /** Commit with message. */
    public void commit(String msg) {
        if (area.getAdditionArea().isEmpty() && area.getRemovalArea().isEmpty()) {
            Main.exitWithMessage("No changes added to the commit.");
        }
        Commit newCommit = currCommit.newCommit(msg);
        newCommit.updateFiles(area);
        area.clear();
        head.updateNext(newCommit.toSha1());
        newCommit.store();
        branches.updateToNextCommit(head);
        saveState();
    }

    /** Remove file. */
    public void rm(String fileName) {
        boolean flag = false;
        String result1 = area.getAdditionArea().remove(fileName);
        if (result1 != null) {
            flag = true;
        }
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
        saveState();
    }

    /** Print log. */
    public void log() {
        Head p = head;
        while (!p.next().isEmpty()) {
            Commit pCommit = Commit.read(p.next());
            pCommit.dump();
            p.updateNext(pCommit.getParent());
        }
    }

    /** Get current commit through Head. */
    private static Commit getCurrCommit(Head p) {
        return Commit.read(p.next());
    }

    /** Checkout out the file in current commit.
     *  Replace the working directory with file in blob.
     */
    public void checkoutFile(String fileName) {
        String sha1 = currCommit.files().get(fileName);
        if (sha1 == null) {
            Main.exitWithMessage("File does not exist in that commit.");
        }
        Blob blob = Blob.read(sha1);
        blob.writeToCWD(fileName);
    }

    /** Checkout the file in specific commit.
     *  Replace the working directory.
     */
    public void checkoutCommitFile(String pSha1, String fileName) {
        File commitFile = fileFromHTPrefix(COMMITS_DIR, pSha1);
        if (commitFile == null) {
            Main.exitWithMessage("No commit with that id exists.");
            return;
        }
        Commit commit = Commit.read(commitFile);
        String blobSha1 = commit.files().get(fileName);
        if (blobSha1 == null) {
            Main.exitWithMessage("File does not exist in that commit.");
        }
        Blob blob = Blob.read(blobSha1);
        blob.writeToCWD(fileName);
    }

    /** Print out current status. */
    public void status() {
        System.out.println("=== Branches ===");
        branches.dump();
        System.out.println();
        System.out.println("=== Staged Files ===");
        area.dumpStagedFiles();
        System.out.println();
        System.out.println("=== Removed Files ===");
        area.dumpRemovedFiles();
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }


}
