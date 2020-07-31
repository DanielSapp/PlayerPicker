# PlayerPicker
This program is a personal-use application designed to generate lineups for fantasy sports events.  It functions by taking data from a website that predicts individual player performance, such as numberfire.com, and checks all possible combinations of players to find the one that is expected to perform the best.

Although multi-thread support was added for learning purposes, the application is bound by memory I/O and therefore usually performs better with only one thread.

To use, obtain a CSV of estimated fantasy sports player performance.  The format of the input should be lines consisting of (playerName,estimatedPoints,playerCost).  When Main is executed, it will ask for a path to this file, the number of players to pick for a team, and the maximum cost of the team.  Execution should not take longer than one minute on most machines for team sizes of 7 players or below.  When execution is completed, the program will output the combination of players under the maximum team cost that is expected to score the highest.
