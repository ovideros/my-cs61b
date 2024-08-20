package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            saveState();
            System.exit(0);
        }
        if (area.getRemovalArea().get(fileName) != null) {
            area.getRemovalArea().remove(fileName);
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

    /** Commit with message. */
    public void commit(String msg, String secondParentCommitSha1) {
        Commit newCommit = currCommit.newCommit(msg, secondParentCommitSha1);
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
        // TODO: EC
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** Print global-log. */
    public void globalLog() {
        List<String> folderNames = plainFilenamesIn(COMMITS_DIR);
        if (folderNames == null) {
            System.out.println("COMMITS NOT EXIST!!!");
            return;
        }
        for (String folderName : folderNames) {
            File folder = join(COMMITS_DIR, folderName);
            List<String> commitSha1s = plainFilenamesIn(folder);
            for (String sha1 : commitSha1s) {
                Commit commit = Commit.read(sha1);
                commit.dump();
            }
        }
    }

    /** Find commits with message, and print IDs. */
    public void find(String msg) {
        String[] folderNames = COMMITS_DIR.list();
        if (folderNames.length == 0) {
            System.out.println("COMMITS NOT EXIST!!!");
            return;
        }
        for (String folderName : folderNames) {
            File folder = join(COMMITS_DIR, folderName);
            List<String> commitSha1s = plainFilenamesIn(folder);
            for (String sha1 : commitSha1s) {
                Commit commit = Commit.read(sha1);
                if (commit.getMessage().equals(msg)) {
                    System.out.println(commit.toSha1());
                }
            }
        }
    }

    /** Convert Set of String to Array of String. */
    public static String[] setToArray(Set<String> setOfString) {
        // source: https://www.geeksforgeeks.org/convert-set-of-string-to-array-of-string-in-java/
        String[] arrayOfString = new String[setOfString.size()];
        int index = 0;
        for (String str : setOfString) {
            arrayOfString[index++] = str;
        }
        return arrayOfString;
    }

    /** Create new branch. */
    public void branch(String branchName) {
        if (branches.getMap().containsKey(branchName)) {
            Main.exitWithMessage("A branch with that name already exists.");
        }
        branches.putBranch(branchName, head.next());
        saveState();
    }

    /** Remove branch with that name. */
    public void rmBranch(String branchName) {
        if (!branches.getMap().containsKey(branchName)) {
            Main.exitWithMessage("A branch with that name does not exists.");
        }
        if (branches.getActiveBranchName().equals(branchName)) {
            Main.exitWithMessage("Cannot remove the current branch.");
        }
        branches.removeBranch(branchName);
        saveState();
    }

    /** Checkout to commit. */
    private void checkoutCommit(String commitSha1) {
        List<String> fileNames = plainFilenamesIn(CWD);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (!isTrackedFile(fileName)) {
                    Main.exitWithMessage("There is an untracked file in the way;" +
                            " delete it, or add and commit it first.");
                }
            }
        }
        Commit checkoutCommit = Commit.read(commitSha1);
        Map<String, String> newFiles = checkoutCommit.getFiles();
        for (String fileName : newFiles.keySet()) {
            Blob blob = Blob.read(newFiles.get(fileName));
            blob.writeToCWD(fileName);
        }
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (!newFiles.containsKey(fileName)) {
                    File delFile = join(CWD, fileName);
                    restrictedDelete(delFile);
                }
            }
        }
        head.updateNext(commitSha1);
        area.clear();
    }

    /** Checkout branch. */
    public void checkoutBranch(String branchName) {
        if (!branches.getMap().containsKey(branchName)) {
            Main.exitWithMessage("No such branch exists.");
        }
        if (branches.getActiveBranchName().equals(branchName)) {
            Main.exitWithMessage("No need to checkout the current branch.");
        }
        String checkoutCommitSha1 = branches.getMap().get(branchName);
        checkoutCommit(checkoutCommitSha1);
        branches.setActiveBranch(branchName);
        saveState();
    }

    /** Reset to commit id. */
    public void reset(String commitId) {
        File commitFile = fileFromHTPrefix(COMMITS_DIR, commitId);
        if (commitFile == null) {
            Main.exitWithMessage("No commit with that id exists.");
        }
        Commit commit = Commit.read(commitFile);
        checkoutCommit(commit.toSha1());
        String branchName = branches.getActiveBranchName();
        branches.getMap().put(branchName, commit.toSha1());
        saveState();
    }

    /** Check whether tracked a file with that name. */
    public boolean isTrackedFile(String fileName) {
        if (area.getRemovalArea().containsKey(fileName)) {
            return false;
        }
        return currCommit.getFiles().containsKey(fileName)
                || area.getAdditionArea().containsKey(fileName);
    }

    /** Merge  from the given branch into current branch. */
    public void merge(String branchName) {
        if (!area.isClean()) {
            Main.exitWithMessage("You have uncommitted changes.");
        }
        if (branches.getMap().get(branchName) == null) {
            Main.exitWithMessage("A branch with that name does not exist.");
        }
        if (branches.getActiveBranchName().equals(branchName)) {
            Main.exitWithMessage("Cannot merge a branch with itself.");
        }
        // TODO: more cases
        String splitPointSha1 = findSplitPoint(branchName);
        String mergeCommitSha1 = branches.getMap().get(branchName);
        String currentCommitSha1 = head.next();
        if (splitPointSha1.equals(mergeCommitSha1)) {
            Main.exitWithMessage("Given branch is an ancestor of the current branch.");
        }
        if (splitPointSha1.equals(currentCommitSha1)) {
            checkoutBranch(branchName);
            Main.exitWithMessage("Current branch fast-forwarded.");
        }

        Commit splitCommit = Commit.read(splitPointSha1);
        Commit mergeCommit = Commit.read(mergeCommitSha1);
        Set<String> fileNames = new HashSet<>(splitCommit.files().keySet());
        fileNames.addAll(mergeCommit.files().keySet());
        fileNames.addAll(currCommit.files().keySet());
        boolean hasMergeConflict = false;
        for (String fileName : fileNames) {
            String splitSha1 = splitCommit.files().get(fileName);
            String headSha1 = currCommit.files().get(fileName);
            String otherSha1 = mergeCommit.files().get(fileName);
            String newFileSha1 = mergeFile(splitSha1, headSha1, otherSha1);
            if (newFileSha1.isEmpty() && !(headSha1 == null)) {
                rm(fileName);
            } else if (!newFileSha1.equals(headSha1) && !newFileSha1.isEmpty()) {
                if (!newFileSha1.equals(otherSha1)) {
                    hasMergeConflict = true;
                }
                Blob blob = Blob.read(newFileSha1);
                blob.writeToCWD(fileName);
                area.getAdditionArea().put(fileName, newFileSha1);
            }
        }
        String message = "Merged " + branchName + " into "
                + branches.getActiveBranchName() + ".";
        if (hasMergeConflict) {
            System.out.println("Encountered a merge conflict.");
        }
        commit(message, mergeCommitSha1);

        saveState();
    }

    /** Return the result blob SHA1 of merge. */
    private String mergeFile(String splitSha1, String headSha1, String otherSha1) {
        if (headSha1 == null && otherSha1 == null) {
            // case 3 special
            return "";
        }
        if (splitSha1 == null && headSha1 == null) {
            // case 5
            return otherSha1;
        }
        if (splitSha1 == null && otherSha1 == null) {
            // case 4
            return headSha1;
        }
        if (splitSha1 == null) {
            if (otherSha1.equals(headSha1)) {
                // case 3
                return otherSha1;
            } else {
                Blob newBlob = createMergeBlob(headSha1, otherSha1);
                newBlob.store();
                return newBlob.toSha1();
            }
        }
        if (splitSha1.equals(otherSha1) && headSha1 == null) {
            // case 7
            return "";
        } else if (splitSha1.equals(headSha1) && otherSha1 == null) {
            // case 6
            return "";
        } else if (splitSha1.equals(headSha1) && !splitSha1.equals(otherSha1)) {
            // case 1
            return otherSha1;
        } else if (!splitSha1.equals(headSha1) && splitSha1.equals(otherSha1)) {
            // case 2
            return headSha1;
        } else if (!splitSha1.equals(headSha1) && headSha1.equals(otherSha1)) {
            // case 3
            return headSha1;
        } else {
            Blob newBlob = createMergeBlob(headSha1, otherSha1);
            newBlob.store();
            return newBlob.toSha1();
        }
    }

    /** Create a new Blob of merge message. */
    private Blob createMergeBlob(String headSha1, String otherSha1) {
        String headText, otherText;
        if (headSha1 == null) {
            headText = "";
        } else {
            Blob headBlob = Blob.read(headSha1);
            headText = headBlob.text();
        }
        if (otherSha1 == null) {
            otherText = "";
        } else {
            Blob otherBlob = Blob.read(otherSha1);
            otherText = otherBlob.text();
        }
        String newText;
        newText = "<<<<<<< HEAD\n" + headText
                + "=======\n" + otherText + ">>>>>>>\n";
        return new Blob(newText);
    }

    /** Find and return split point commit SHA1. */
    private String findSplitPoint(String branchName) {
        // my thought comes from https://programmercarl.com/%E9%9D%A2%E8%AF%95%E9%A2%9802.07.%E9%93%BE%E8%A1%A8%E7%9B%B8%E4%BA%A4.html#%E6%80%9D%E8%B7%AF
        int lenA = ancestorLength(branches.getActiveBranchSha1());
        int lenB = ancestorLength(branches.getMap().get(branchName));
        String curA = head.next();
        String curB = branches.getMap().get(branchName);
        if (lenB > lenA) {
            int tmpNum = lenA;
            lenA = lenB;
            lenB = tmpNum;
            String tmpStr = curA;
            curA = curB;
            curB = tmpStr;
        }
        int gap = lenA - lenB;
        while (gap > 0) {
            curA = ancestor(curA);
            gap -= 1;
        }
        while (!curA.isEmpty()) {
            if (curA.equals(curB)) {
                return curA;
            }
            curA = ancestor(curA);
            curB = ancestor(curB);
        }
        return null;
    }

    /** Find and return the SHA1 of the ancestor of this commit. */
    private String ancestor(String commitSha1) {
        if (commitSha1.isEmpty()) {
            Main.exitWithMessage("Null Pointer!!!");
        }
        Commit commit = Commit.read(commitSha1);
        return commit.getParent();
    }

    /** Return the length of this commit. */
    private int ancestorLength(String commitSha1) {
        int count = 0;
        while (!commitSha1.isEmpty()) {
            commitSha1 = ancestor(commitSha1);
            count += 1;
        }
        return count;
    }

}
