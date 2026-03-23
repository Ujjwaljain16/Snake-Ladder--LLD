# Snake and Ladder (LLD Assignment)

## 1. Class Diagram

```text
+-------------------+
|   DifficultyLevel |
+-------------------+
| EASY, HARD        |
| +fromString()     |
+-------------------+

+-------------------+         +-------------------+
|      Player       |         |       Dice        |
+-------------------+         +-------------------+
| -name:String      |         | -random:Random    |
| -position:int     |         +-------------------+
| -finished:boolean |         | +rollDice():int   |
+-------------------+         +-------------------+
| getters/setters   |
+-------------------+

+-------------------+         +-------------------+
|      Snake        |         |      Ladder       |
+-------------------+         +-------------------+
| -head:int         |         | -start:int        |
| -tail:int         |         | -end:int          |
+-------------------+         +-------------------+
| getters           |         | getters           |
+-------------------+         +-------------------+

+--------------------------------------------------+
|                      Board                       |
+--------------------------------------------------+
| -size:int                                        |
| -totalCells:int                                  |
| -difficultyLevel:DifficultyLevel                 |
| -random:Random                                   |
| -snakes:List<Snake>                              |
| -ladders:List<Ladder>                            |
| -snakeMap:Map<Integer,Integer>                   |
| -ladderMap:Map<Integer,Integer>                  |
+--------------------------------------------------+
| +getTotalCells():int                             |
| +getSnakeTail(position):Integer                  |
| +getLadderEnd(position):Integer                  |
| +printBoardElements():void                       |
+--------------------------------------------------+

+--------------------------------------------------+
|                       Game                       |
+--------------------------------------------------+
| -board:Board                                     |
| -dice:Dice                                       |
| -difficultyLevel:DifficultyLevel                 |
| -players:List<Player>                            |
| -finishedPlayers:List<Player>                    |
+--------------------------------------------------+
| +startGame():void                                |
| +rollDice():int                                  |
| +movePlayer(player,diceValue):void               |
| +checkSnakeOrLadder(position):int                |
| +isGameOver():boolean                            |
+--------------------------------------------------+

+-------------------+
|       Main        |
+-------------------+
| +main(args):void  |
+-------------------+

Relationships:
- Main creates Game
- Game has Board, Dice, Players
- Board has Snakes and Ladders
```

## 2. Code

All complete code is inside `src/`:

- `src/DifficultyLevel.java`
- `src/Player.java`
- `src/Snake.java`
- `src/Ladder.java`
- `src/Dice.java`
- `src/Board.java`
- `src/Game.java`
- `src/Main.java`

### Run

```bash
javac src/*.java
java -cp src Main
```

### Sample Execution (one run)

```text
Enter board size n (for n x n): 4
Enter number of players: 3
Enter difficulty level (easy/hard): hard

=== Snake and Ladder Game Started ===
Difficulty: HARD
Target cell: 16
Snakes:
  15 -> 6
  13 -> 10
  9 -> 8
  14 -> 11
Ladders:
  2 -> 14
  5 -> 9
  12 -> 14
  6 -> 7

... (turns continue)

=== Game Over ===
Finish order:
  1. P1
  2. P3
Remaining player: P2 at 10
```

## 3. Explanation

### Classes and Responsibilities

- `Player`: stores player name, current position, and whether player already finished.
- `Snake`: stores one snake (`head -> tail`).
- `Ladder`: stores one ladder (`start -> end`).
- `Dice`: gives random value between 1 and 6.
- `Board`: creates board size `n x n`, randomly places exactly `n` snakes and `n` ladders, and provides lookup methods.
- `Game`: controls the game loop, player turns, move logic, snake/ladder checks, and game-over condition.
- `Main`: takes input and starts the game.

### How Board is Generated

- Board has cells from `1` to `n^2`.
- We place exactly `n` snakes and `n` ladders.
- Snake rules: `head > tail`.
- Ladder rules: `start < end`.
- Random placement is done with `Random`.
- No overlap of snake-head/ladder-start is allowed.
- Simple cycle prevention is done by checking transition chains before adding a new snake/ladder.

### How Snakes/Ladders are Stored

- Objects are stored in lists: `List<Snake>` and `List<Ladder>`.
- Fast checks are done using maps:
  - `snakeMap` (`head -> tail`)
  - `ladderMap` (`start -> end`)

### Game Loop Logic

- Every player starts at `0`.
- In each turn:
  - roll dice (`1` to `6`)
  - try to move to `current + dice`
  - if beyond `n^2`, move is skipped
  - check snake/ladder and update final position
- If a player reaches exactly `n^2`, that player is marked finished.
- Game keeps running until only one active player remains (`at least 2 players no longer remain`).

### Difficulty Handling

- `easy`: one snake/ladder jump per move (no chaining).
- `hard`: chaining allowed (after ladder, if snake exists there, it applies, and so on).

### Assumptions

- Minimum board size is 2.
- Minimum players are 2.
- Cell `1` and cell `n^2` are not used as snake/ladder start points.
- Because placement is random, each run gives different board layout and different output.
# Snake-Ladder--LLD
