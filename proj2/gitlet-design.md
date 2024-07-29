# Gitlet Design Document

**Name**: OvidEros

## Classes and Data Structures

### Main
Main class is intended to be short, 
the remaining part will in the Repository class.



### Repository
#### Variables
Including all the needed Folder paths.

#### Constructor
Check whether .gitlet folder exists.
If so, exit the program.
Otherwise, initialize the folders.
Then, create the first commit.


### Commit
#### Variables
Include message, timeStamp, parent.
The static final EPOCH_TIME means the 
default timeStamp of first commit.

#### Constructor
Using msg and parent to create a new commit.
If parent is null, set timeStamp to default;
otherwise, set timeStamp to now.

## Algorithms

## Persistence

