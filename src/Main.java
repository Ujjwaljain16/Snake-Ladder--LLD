import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter board size n (for n x n): ");
        int n = scanner.nextInt();

        System.out.print("Enter number of players: ");
        int players = scanner.nextInt();

        System.out.print("Enter difficulty level (easy/hard): ");
        String difficultyInput = scanner.next();
        DifficultyLevel difficultyLevel = DifficultyLevel.fromString(difficultyInput);

        Game game = new Game(n, players, difficultyLevel);
        game.startGame();

        scanner.close();
    }
}
