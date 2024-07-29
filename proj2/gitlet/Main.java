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
        switch(firstArg) {
            case "init":
                validateArgsNum(args, 1);
                new Repository();
                break;
            case "add":
                validateArgsNum(args, 2);
                Repository currRepo = Repository.load();
                currRepo.add(args[1]);
                break;
            default:
                exitWithMessage("No command with that name exists.");
        }
    }

    static void exitWithMessage(String str) {
        System.out.println(str);
        System.exit(0);
    }

    private static void validateArgsNum(String[] args, int num) {
        if (args.length != num) {
            exitWithMessage("Incorrect operands.");
        }
    }
}
