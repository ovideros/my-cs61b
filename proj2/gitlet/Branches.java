package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.join;

public class Branches implements Dumpable {
    private Map<String, String> nameToSha1;
    private String activeBranchName;
    private String activeBranchSha1;
    private static final String name = "BRANCHES";

    /** Constructor of Branches. */
    public Branches() {
        nameToSha1 = new HashMap<>();
    }

    /** Get Map from name to SHA1. */
    public Map<String, String> getMap() {
        return nameToSha1;
    }

    /** Set active branch. */
    public void setActiveBranch(String name) {
        String sha1 = nameToSha1.get(name);
        if (sha1 == null) {
            Main.exitWithMessage("Branch name not found!!!");
        }
        activeBranchName = name;
        activeBranchSha1 = sha1;
    }

    /** Put branch into branches. */
    public void putBranch(String name, String sha1) {
        nameToSha1.put(name, sha1);
    }

    /** Store current branches. */
    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, name);
    }

    /** Read branches from file. */
    public static Branches read() {
        File file = join(Repository.GITLET_DIR, name);
        return Utils.readObject(file, Branches.class);
    }

    /** Update current branch to next commit,
     *  Head now points to the next commit.
     *  */
    public void updateToNextCommit(Head head) {
        activeBranchSha1 = head.next();
        putBranch(activeBranchName, head.next());
    }

    @Override
    public void dump() {
        Set<String> strs = nameToSha1.keySet();
        String[] names = Repository.setToArray(strs);
        Arrays.sort(names);
        for (String name : names) {
            if (name.equals(activeBranchName)) {
                System.out.println("*" + name);
            } else {
                System.out.println(name);
            }
        }
    }

    /** Get active branch name. */
     public String getActiveBranchName() {
        return activeBranchName;
     }

     /** Remove branch. */
     public void removeBranch(String name) {
         nameToSha1.remove(name);
     }
}
