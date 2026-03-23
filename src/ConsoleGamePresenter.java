import java.util.List;

public class ConsoleGamePresenter implements GamePresenter {

    @Override
    public void displayGameStart(int targetCells, DifficultyLevel difficulty) {
        System.out.println();
        System.out.println("Starting game on a " + targetCells + " cell board");
        System.out.println("Difficulty: " + difficulty);
        System.out.println();
    }

    @Override
    public void displayBoardElements(Board board) {
        System.out.println("Snakes on the board:");
        for (Snake snake : board.getSnakes()) {
            System.out.println("  " + snake.getHead() + " down to " + snake.getTail());
        }
        System.out.println("Ladders on the board:");
        for (Ladder ladder : board.getLadders()) {
            System.out.println("  " + ladder.getStart() + " up to " + ladder.getEnd());
        }
        System.out.println();
    }

    @Override
    public void displayPlayerRoll(Player player, int diceValue) {
        System.out.println(player.getName() + " rolled a " + diceValue);
    }

    @Override
    public void displayPlayerMove(Player player, int fromPosition, int toPosition) {
        System.out.println("  Moves to " + toPosition);
    }

    @Override
    public void displaySnakeEncounter(int head, int tail) {
        System.out.println("  Oops, landed on a snake. Slid down to " + tail);
    }

    @Override
    public void displayLadderEncounter(int start, int end) {
        System.out.println("  Great, found a ladder. Climbed up to " + end);
    }

    @Override
    public void displayMoveSkipped(Player player, int currentPosition) {
        System.out.println("  Can't move beyond the board. Stays at " + currentPosition);
    }

    @Override
    public void displayPlayerFinished(Player player, int rank) {
        System.out.println(player.getName() + " reached the end!");
    }

    @Override
    public void displayGameOver(List<Player> finishedPlayers, List<Player> activePlayers) {
        System.out.println();
        System.out.println("Game finished");
        System.out.println();
        
        if (finishedPlayers.isEmpty()) {
            System.out.println("No one reached the end");
        } else {
            System.out.println("Winners:");
            for (int i = 0; i < finishedPlayers.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + finishedPlayers.get(i).getName());
            }
        }

        for (Player player : activePlayers) {
            if (!player.isFinished()) {
                System.out.println(player.getName() + " stopped at position " + player.getPosition());
            }
        }
        System.out.println();
    }
}
