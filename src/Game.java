import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Board board;
    private final Dice dice;
    private final DifficultyLevel difficultyLevel;
    private final MovementStrategy movementStrategy;
    private final GamePresenter presenter;
    private final List<Player> players;
    private final List<Player> finishedPlayers;

    public Game(int boardSize, int numberOfPlayers, DifficultyLevel difficultyLevel, GamePresenter presenter) {
        if (boardSize < 2) {
            throw new IllegalArgumentException("Board size should be at least 2.");
        }
        if (numberOfPlayers < 2) {
            throw new IllegalArgumentException("Need at least 2 players.");
        }

        this.presenter = presenter;
        this.difficultyLevel = difficultyLevel;
        this.movementStrategy = createMovementStrategy(difficultyLevel);
        this.board = BoardFactory.createBoard(boardSize, difficultyLevel);
        this.dice = new Dice(new Random());
        this.players = new ArrayList<>();
        this.finishedPlayers = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("P" + i));
        }
    }

    private MovementStrategy createMovementStrategy(DifficultyLevel difficulty) {
        return difficulty == DifficultyLevel.HARD ? new HardMovementStrategy() : new EasyMovementStrategy();
    }

    public void startGame() {
        presenter.displayGameStart(board.getTotalCells(), difficultyLevel);
        presenter.displayBoardElements(board);

        while (!isGameOver()) {
            for (Player player : players) {
                if (player.isFinished()) {
                    continue;
                }

                int diceValue = rollDice();
                presenter.displayPlayerRoll(player, diceValue);
                movePlayer(player, diceValue);

                if (player.getPosition() == board.getTotalCells() && !player.isFinished()) {
                    player.setFinished(true);
                    finishedPlayers.add(player);
                    presenter.displayPlayerFinished(player, finishedPlayers.size());
                }

                if (isGameOver()) {
                    break;
                }
            }
            System.out.println();
        }

        endGame();
    }

    public int rollDice() {
        return dice.rollDice();
    }

    public void movePlayer(Player player, int diceValue) {
        int currentPosition = player.getPosition();
        int newPosition = currentPosition + diceValue;

        if (newPosition > board.getTotalCells()) {
            presenter.displayMoveSkipped(player, currentPosition);
            return;
        }

        presenter.displayPlayerMove(player, currentPosition, newPosition);
        int finalPosition = movementStrategy.applyTransitions(newPosition, board, presenter);
        player.setPosition(finalPosition);

        System.out.println("  Final position: " + finalPosition);
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

    private void endGame() {
        presenter.displayGameOver(finishedPlayers, players);
    }
}
