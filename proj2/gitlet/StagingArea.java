package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.join;

public class StagingArea implements Serializable {
    public Map<String, String> additionArea;
    public Map<String, String> removalArea;
    private static final String name = "STAGING_AREA";

    public StagingArea() {
        additionArea = new HashMap<>();
        removalArea = new HashMap<>();
    }

    public void addFileAddition(String name, String sha1) {
        additionArea.put(name, sha1);
    }

    public void removeFileAddition(String name, String sha1) {
        additionArea.remove(name, sha1);
    }

    public void clear() {
        additionArea = new HashMap<>();
        removalArea = new HashMap<>();
    }


    public void store() {
        Repository.storeInName(Repository.GITLET_DIR, this, name);
    }

    public static StagingArea read() {
        File areaFile = join(Repository.GITLET_DIR, name);
        return Utils.readObject(areaFile, StagingArea.class);
    }
}
