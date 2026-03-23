import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter board size n (for n x n): ");
        int n = sc.nextInt();

        System.out.print("Enter number of players: ");
        int players = sc.nextInt();

        System.out.print("Enter difficulty level (easy/hard): ");
        String difficultyInput = sc.next();
        DifficultyLevel difficultyLevel = DifficultyLevel.fromString(difficultyInput);

        GamePresenter presenter = new ConsoleGamePresenter();
        Game game = new Game(n, players, difficultyLevel, presenter);
        game.startGame();

        sc.close();
    }
}
