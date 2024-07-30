package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.join;

public class StagingArea implements Serializable {
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
}
