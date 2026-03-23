import java.util.*;
public class Board {
    private final int size;
    private final int totalCells;
    private final DifficultyLevel difficultyLevel;
    private final Random random;

    private final List<Snake> snakes;
    private final List<Ladder> ladders;
    private final Map<Integer, Integer> snakeMap;
    private final Map<Integer, Integer> ladderMap;

    public Board(int size, DifficultyLevel difficultyLevel, Random random) {
        this.size = size;
        this.totalCells = size * size;
        this.difficultyLevel = difficultyLevel;
        this.random = random;
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();

        generateBoard();
    }

    private void generateBoard() {
        Set<Integer> usedStarts = new HashSet<>();

        int requiredCount = size;
        int snakeCount = 0;
        int ladderCount = 0;

        int attempts = 0;
        int maxAttempts = 100000;

        while ((snakeCount < requiredCount || ladderCount < requiredCount) && attempts < maxAttempts) {
            attempts++;

            if (snakeCount < requiredCount) {
                int[] snakePair = createSnakePair();
                int head = snakePair[0];
                int tail = snakePair[1];

                if (isValidTransition(head, tail, usedStarts)) {
                    snakes.add(new Snake(head, tail));
                    snakeMap.put(head, tail);
                    usedStarts.add(head);
                    snakeCount++;
                }
            }

            if (ladderCount < requiredCount) {
                int[] ladderPair = createLadderPair();
                int start = ladderPair[0];
                int end = ladderPair[1];

                if (isValidTransition(start, end, usedStarts)) {
                    ladders.add(new Ladder(start, end));
                    ladderMap.put(start, end);
                    usedStarts.add(start);
                    ladderCount++;
                }
            }
        }

        if (snakeCount < requiredCount || ladderCount < requiredCount) {
            throw new RuntimeException("Unable to place snakes and ladders for this board size.");
        }
    }

    private int[] createSnakePair() {
        int head;
        int tail;

        if (difficultyLevel == DifficultyLevel.EASY) {
            int minHead = Math.max(4, totalCells / 2);
            head = getRandomNumber(minHead, totalCells - 1);
            tail = getRandomNumber(2, head - 1);
        } else {
            head = getRandomNumber(4, totalCells - 1);
            tail = getRandomNumber(2, head - 1);
        }

        return new int[]{head, tail};
    }

    private int[] createLadderPair() {
        int start;
        int end;

        if (difficultyLevel == DifficultyLevel.EASY) {
            int maxStart = Math.max(3, totalCells / 2);
            start = getRandomNumber(2, maxStart);
            end = getRandomNumber(start + 1, totalCells - 1);
        } else {
            start = getRandomNumber(2, totalCells - 2);
            end = getRandomNumber(start + 1, totalCells - 1);
        }

        return new int[]{start, end};
    }

    private int getRandomNumber(int min, int max) {
        if (min > max) {
            return min;
        }
        return random.nextInt(max - min + 1) + min;
    }

    private boolean isValidTransition(int start, int end, Set<Integer> usedStarts) {
        if (start <= 1 || start >= totalCells || end <= 1 || end >= totalCells) {
            return false;
        }

        if (usedStarts.contains(start)) {
            return false;
        }

        if (difficultyLevel == DifficultyLevel.EASY && usedStarts.contains(end)) {
            return false;
        }

        return !createsCycle(start, end);
    }

    private boolean createsCycle(int start, int end) {
        Map<Integer, Integer> allTransitions = new HashMap<>();
        allTransitions.putAll(snakeMap);
        allTransitions.putAll(ladderMap);
        allTransitions.put(start, end);

        int current = end;
        int steps = 0;

        while (allTransitions.containsKey(current) && steps <= totalCells) {
            current = allTransitions.get(current);
            if (current == start) {
                return true;
            }
            steps++;
        }

        return false;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public Integer getSnakeTail(int position) {
        return snakeMap.get(position);
    }

    public Integer getLadderEnd(int position) {
        return ladderMap.get(position);
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Ladder> getLadders() {
        return ladders;
    }
}