public interface GamePresenter {
    void displayGameStart(int targetCells, DifficultyLevel difficulty);
    void displayBoardElements(Board board);
    void displayPlayerRoll(Player player, int diceValue);
    void displayPlayerMove(Player player, int fromPosition, int toPosition);
    void displaySnakeEncounter(int head, int tail);
    void displayLadderEncounter(int start, int end);
    void displayMoveSkipped(Player player, int currentPosition);
    void displayPlayerFinished(Player player, int rank);
    void displayGameOver(java.util.List<Player> finishedPlayers, java.util.List<Player> activePlayers);
}
