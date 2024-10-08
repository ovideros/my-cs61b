package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author OvidEros
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            exitWithMessage("Please enter a command.");
        }
        String firstArg = args[0];
        Repository currRepo = null;
        if (!args[0].equals("init")) {
            currRepo = Repository.load();
            if (currRepo == null) {
                exitWithMessage("Not in an initialized Gitlet directory.");
            }
        }
        switch (firstArg) {
            case "init":
                validateArgsNum(args, 1);
                currRepo = new Repository();
                break;
            case "add":
                validateArgsNum(args, 2);
                currRepo.add(args[1]);
                break;
            case "commit":
                if (args.length == 1 || args[1].isEmpty()) {
                    exitWithMessage("Please enter a commit message.");
                }
                validateArgsNum(args, 2);
                currRepo.commit(args[1]);
                break;
            case "rm":
                validateArgsNum(args, 2);
                currRepo.rm(args[1]);
                break;
            case "log":
                validateArgsNum(args, 1);
                currRepo.log();
                break;
            case "checkout":
                if (args.length == 3) {
                    currRepo.checkoutFile(args[2]);
                } else if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        exitWithMessage("Incorrect operands.");
                    }
                    currRepo.checkoutCommitFile(args[1], args[3]);
                } else {
                    validateArgsNum(args, 2);
                    currRepo.checkoutBranch(args[1]);
                    break;
                }
                break;
            case "status":
                validateArgsNum(args, 1);
                currRepo.status();
                break;
            case "global-log":
                validateArgsNum(args, 1);
                currRepo.globalLog();
                break;
            case "find":
                validateArgsNum(args, 2);
                currRepo.find(args[1]);
                break;
            case "branch":
                validateArgsNum(args, 2);
                currRepo.branch(args[1]);
                break;
            case "rm-branch":
                validateArgsNum(args, 2);
                currRepo.rmBranch(args[1]);
                break;
            case "reset":
                validateArgsNum(args, 2);
                currRepo.reset(args[1]);
                break;
            case "merge":
                validateArgsNum(args, 2);
                currRepo.merge(args[1]);
                break;
            default:
                exitWithMessage("No command with that name exists.");
        }
    }

    /** Print the str, and exit the program with code 0.
     *
     * @param str  exit message to be printed
     */
    public static void exitWithMessage(String str) {
        System.out.println(str);
        System.exit(0);
    }

    /** Validate the args contains desired num of Strings,
     * otherwise exit with message.
     *
     * @param args  the args from Main
     * @param num  desired number of params
     */
    private static void validateArgsNum(String[] args, int num) {
        if (args.length != num) {
            exitWithMessage("Incorrect operands.");
        }
    }
}
