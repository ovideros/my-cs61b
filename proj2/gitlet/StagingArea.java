package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.join;

public class StagingArea implements Dumpable {
    private Map<String, String> additionArea;
    private Map<String, String> removalArea;
    private static final String NAME = "STAGING_AREA";

    /** Construct the staging area. */
    public StagingArea() {
        additionArea = new HashMap<>();
        removalArea = new HashMap<>();
    }

    /** Get the additionArea from staging area. */
    public Map<String, String> getAdditionArea() {
        return additionArea;
    }

    /** Get the removalArea from staging area. */
    public Map<String, String> getRemovalArea() {
        return removalArea;
    }

    /** Clear this staging area. */
    public void clear() {
        additionArea = new HashMap<>();
        removalArea = new HashMap<>();
    }

    /** Store this staging area. */
    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, NAME);
    }

    /** Read and return the staging area. */
    public static StagingArea read() {
        File areaFile = join(Repository.GITLET_DIR, NAME);
        return Utils.readObject(areaFile, StagingArea.class);
    }

    @Override
    public void dump() {
        System.out.println("Staged Files:::");
        dumpStagedFiles();
        System.out.println("Removed Files:::");
    }

    /** Dump staged files. */
    public void dumpStagedFiles() {
        Set<String> strs = additionArea.keySet();
        String[] names = Repository.setToArray(strs);
        Arrays.sort(names);
        for (String name : names) {
            System.out.println(name);
        }
    }

    /** Dump removed files. */
    public void dumpRemovedFiles() {
        Set<String> strs = removalArea.keySet();
        String[] names = Repository.setToArray(strs);
        Arrays.sort(names);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
