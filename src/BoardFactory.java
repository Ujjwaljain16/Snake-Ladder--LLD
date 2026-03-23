import java.util.Random;

public class BoardFactory {
    public static Board createBoard(int boardSize, DifficultyLevel difficulty) {
        return new Board(boardSize, difficulty, new Random());
    }
}
