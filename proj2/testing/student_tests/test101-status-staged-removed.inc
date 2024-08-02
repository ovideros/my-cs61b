# Check Status functionality
I definitions.inc
> init
<<<
+ try.txt try.txt
> add try.txt
<<<
> status
=== Branches ===
*master

=== Staged Files ===
try.txt

=== Removed Files ===

=== Modifications Not Staged For Commit ===

=== Untracked Files ===

<<<
> commit "added try.txt"
<<<
> rm try.txt
<<<
> status
=== Branches ===
*master

=== Staged Files ===

=== Removed Files ===
try.txt

=== Modifications Not Staged For Commit ===

=== Untracked Files ===

<<<
> commit "deleted try.txt"
<<<
> log
===
${COMMIT_HEAD}
deleted try.txt

===
${COMMIT_HEAD}
added try.txt

===
${COMMIT_HEAD}
initial commit

<<<*
> status
=== Branches ===
*master

=== Staged Files ===

=== Removed Files ===

=== Modifications Not Staged For Commit ===

=== Untracked Files ===

<<<