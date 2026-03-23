import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Board board;
    private final Dice dice;
    private final DifficultyLevel difficultyLevel;
    private final List<Player> players;
    private final List<Player> finishedPlayers;

    public Game(int boardSize, int numberOfPlayers, DifficultyLevel difficultyLevel) {
        if (boardSize < 2) {
            throw new IllegalArgumentException("Board size should be at least 2.");
        }
        if (numberOfPlayers < 2) {
            throw new IllegalArgumentException("Need at least 2 players.");
        }

        this.difficultyLevel = difficultyLevel;
        Random random = new Random();
        this.board = new Board(boardSize, difficultyLevel, random);
        this.dice = new Dice(random);
        this.players = new ArrayList<>();
        this.finishedPlayers = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("P" + i));
        }
    }

    public void startGame() {
        System.out.println("\n=== Snake and Ladder Game Started ===");
        System.out.println("Difficulty: " + difficultyLevel);
        System.out.println("Target cell: " + board.getTotalCells());
        board.printBoardElements();

        while (!isGameOver()) {
            for (Player player : players) {
                if (player.isFinished()) {
                    continue;
                }

                int diceValue = rollDice();
                System.out.println(player.getName() + " rolled: " + diceValue);
                movePlayer(player, diceValue);

                if (player.getPosition() == board.getTotalCells() && !player.isFinished()) {
                    player.setFinished(true);
                    finishedPlayers.add(player);
                    System.out.println(player.getName() + " finished at rank " + finishedPlayers.size());
                }

                if (isGameOver()) {
                    break;
                }
            }
            System.out.println();
        }

        printResult();
    }

    public int rollDice() {
        return dice.rollDice();
    }

    public void movePlayer(Player player, int diceValue) {
        int currentPosition = player.getPosition();
        int newPosition = currentPosition + diceValue;

        if (newPosition > board.getTotalCells()) {
            System.out.println("  Move skipped. " + player.getName() + " stays at " + currentPosition);
            return;
        }

        System.out.println("  " + player.getName() + " moves from " + currentPosition + " to " + newPosition);
        int finalPosition = checkSnakeOrLadder(newPosition);
        player.setPosition(finalPosition);

        System.out.println("  Final position: " + finalPosition);
    }

    public int checkSnakeOrLadder(int position) {
        if (difficultyLevel == DifficultyLevel.EASY) {
            Integer snakeTail = board.getSnakeTail(position);
            if (snakeTail != null) {
                System.out.println("  Hit snake at " + position + ", goes down to " + snakeTail);
                return snakeTail;
            }

            Integer ladderEnd = board.getLadderEnd(position);
            if (ladderEnd != null) {
                System.out.println("  Climbs ladder at " + position + ", goes up to " + ladderEnd);
                return ladderEnd;
            }

            return position;
        }

        int current = position;
        int safety = 0;

        while (safety <= board.getTotalCells()) {
            Integer snakeTail = board.getSnakeTail(current);
            if (snakeTail != null) {
                System.out.println("  Hit snake at " + current + ", goes down to " + snakeTail);
                current = snakeTail;
                safety++;
                continue;
            }

            Integer ladderEnd = board.getLadderEnd(current);
            if (ladderEnd != null) {
                System.out.println("  Climbs ladder at " + current + ", goes up to " + ladderEnd);
                current = ladderEnd;
                safety++;
                continue;
            }

            break;
        }

        return current;
    }

    public boolean isGameOver() {
        int activePlayers = 0;
        for (Player player : players) {
            if (!player.isFinished()) {
                activePlayers++;
            }
        }

        return activePlayers <= 1;
    }

    private void printResult() {
        System.out.println("=== Game Over ===");
        System.out.println("Finish order:");

        if (finishedPlayers.isEmpty()) {
            System.out.println("  No one finished.");
        } else {
            for (int i = 0; i < finishedPlayers.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + finishedPlayers.get(i).getName());
            }
        }

        for (Player player : players) {
            if (!player.isFinished()) {
                System.out.println("Remaining player: " + player.getName() + " at " + player.getPosition());
            }
        }
    }
}
