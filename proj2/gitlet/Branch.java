package gitlet;

public class Branch extends Pointer {
    private String name;
    public Branch(String sha1, String name) {
        super(sha1);
        this.name = name;
    }

    public void store() {
        Repository.storeInName(Repository.BRANCHES_DIR, this, name);
    }
}
